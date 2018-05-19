package com.firstrowria.pushnotificationtester.manager;

import java.io.*;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class ExceptionManager {
	private static final Logger log = Logger.getLogger(ExceptionManager.class.getName());

	public static void handleException(Exception e, String additional) {
		try {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			String exceptionText = writer.toString();

			log.severe(exceptionText);
			log.severe(additional);
	
		}
		catch (Exception ex) {
		}

	}

	public static String getRequestInformation(HttpServletRequest request) {
		String info = "";

		try {

			info = "Request Method: " + request.getMethod() + "\n";
			info = info + "Request URI: " + request.getRequestURI() + "\n";
			info = info + "Request Protocol: " + request.getProtocol() + "\n";
			info = info + "Servlet path: " + request.getServletPath() + "\n";
			info = info + "Path info: " + request.getPathInfo() + "\n";
			info = info + "Path translated: " + request.getPathTranslated() + "\n";
			info = info + "Query string: " + request.getQueryString() + "\n";
			info = info + "Content length: " + request.getContentLength() + "\n";
			info = info + "Content type: " + request.getContentType() + "\n";
			info = info + "Server name: " + request.getServerName() + "\n";
			info = info + "Server port: " + request.getServerPort() + "\n";
			info = info + "Remote user: " + request.getRemoteUser() + "\n";
			info = info + "Remote address: " + request.getRemoteAddr() + "\n";
			info = info + "Remote host: " + request.getRemoteHost() + "\n";
			info = info + "Authorization scheme: " + request.getAuthType() + "\n";
			info = info + "User agent: " + request.getHeader("User-Agent") + "\n";

			@SuppressWarnings("rawtypes")
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				info = info + headerName + " (HEADER): " + request.getHeader(headerName) + "\n";
			}
		}
		catch (Exception e) {
			info = "Request Information error: " + e.getMessage();
		}

		return info;
	}

}
