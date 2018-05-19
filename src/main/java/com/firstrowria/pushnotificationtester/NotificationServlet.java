package com.firstrowria.pushnotificationtester;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.appengine.api.taskqueue.*;

@WebServlet(
	    name = "NotificationServlet",
	    urlPatterns = {"/notification"}
	)
public class NotificationServlet extends HttpServlet {

	private static final long serialVersionUID = 7599957607525706252L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String pushId = req.getParameter("pushId");
		String deliveryPrio = optParameter(req.getParameter("deliveryPrio"), "0");
		String notificationPrio = optParameter(req.getParameter("notificationPrio"), "0");
		long delay = Long.parseLong(optParameter(req.getParameter("delay"), "0"));
		
		resp.setContentType("text/plain");

		try {
			(QueueFactory.getQueue("NotificationQueue")).add(createSendWatchListTask(pushId, delay, deliveryPrio, notificationPrio));
			resp.getWriter().println("1");
		}
		catch (Exception e) {
			resp.getWriter().println("0");
		}
	}
	

	private String optParameter(String parameter, String defaultValue) {
		if (parameter == null)
			return defaultValue;
		else
			return parameter;
	}
	
	private static TaskOptions createSendWatchListTask(String pushId, long delay, String deliveryPrio, String notificationPrio) {

		TaskOptions to = TaskOptions.Builder.withParam("id", pushId);

		to = to.param("count", "0");
		to = to.param("deliveryPrio", deliveryPrio);
		to = to.param("notificationPrio", notificationPrio);
		to = to.countdownMillis(delay * 1000);
		to = to.url("/notificationTask");

		return to;
	}

}
