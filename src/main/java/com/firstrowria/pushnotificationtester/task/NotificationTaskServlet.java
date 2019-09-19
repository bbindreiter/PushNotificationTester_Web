package com.firstrowria.pushnotificationtester.task;

import java.io.OutputStream;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.firstrowria.pushnotificationtester.manager.ExceptionManager;
import com.google.appengine.api.taskqueue.*;

@WebServlet(
	    name = "NotificationTaskServlet",
	    urlPatterns = {"/notificationTask"}
	)
public class NotificationTaskServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7629227131764824109L;
	private static final Logger log = Logger.getLogger(NotificationTaskServlet.class.getName());
	
	private static final String API_KEY = "";

	// the automatic call of a TASK is always a post!
	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		String queueNameHeader = request.getHeader("X-AppEngine-QueueName");

		if (queueNameHeader != null && queueNameHeader.equals("NotificationQueue")) {
			String pushId = request.getParameter("id");
			String deliveryPrio = request.getParameter("deliveryPrio");
			String notificationPrio = request.getParameter("notificationPrio");
	
			int count = Integer.parseInt(request.getParameter("count"));

			log.info("pushId: " + pushId);
			log.info("count: " + count);
			log.info("deliveryPrio:" + deliveryPrio);
			log.info("notificationPrio:" + notificationPrio);
			
			if (API_KEY == null || API_KEY.equals(""))
				log.warning("API KEY NOT SET - not sending notification");
			else {
				try {
					JSONObject obj = new JSONObject();

					LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
					data.put("title", "Test notification");
					data.put("serverTime", String.valueOf(System.currentTimeMillis()));
					data.put("notificationPrio", notificationPrio);

					JSONArray registrationsIDList = new JSONArray();
					registrationsIDList.put(pushId);
					obj.put("registration_ids", registrationsIDList);
					obj.put("data", data);
					obj.put("priority", deliveryPrio.equals("0") ? "normal" : "high"); // normal or high
					obj.put("time_to_live", 86400); // 1day

					URL url = new URL("https://fcm.googleapis.com/fcm/send");
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setDoOutput(true);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-Type", "application/json ; charset=utf-8");
					connection.setRequestProperty("Authorization", "key=" + API_KEY);

					OutputStream writer = connection.getOutputStream();
					writer.write(obj.toString().getBytes("UTF-8"));
					writer.close();

					int responseCode = connection.getResponseCode();
					String message = connection.getResponseMessage();

					if (responseCode != HttpURLConnection.HTTP_OK) {
						log.severe("GCM Error, HTTP RC: " + responseCode + ", message: " + message);
						throw new Exception("GCM Error, HTTP RC: " + responseCode + ", message: " + message);
					}
					else
						log.info("GCM Success, HTTP RC: " + responseCode);
				}
				catch (Exception e) {
					ExceptionManager.handleException(e, "Exeption during sending GCM Message");

					if (count == 0) // retry
					{
						count++;
						(QueueFactory.getQueue("NotificationQueue")).add(createSendWatchListTask(pushId, count, deliveryPrio, notificationPrio));
					}
				}
			}
		}
	}

	private static TaskOptions createSendWatchListTask(String pushId, int count, String deliveryPrio, String notificationPrio) {

		TaskOptions to = TaskOptions.Builder.withParam("id", pushId);
		to = to.param("count", String.valueOf(count));
		to = to.param("deliveryPrio", deliveryPrio);
		to = to.param("notificationPrio", notificationPrio);
		to = to.countdownMillis(5000); //try again in 5 sec
		to = to.url("/notificationTask");

		return to;
	}

}
