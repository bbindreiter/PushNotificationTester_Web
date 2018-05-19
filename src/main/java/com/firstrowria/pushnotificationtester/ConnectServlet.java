package com.firstrowria.pushnotificationtester;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(
	    name = "ConnectServlet",
	    urlPatterns = {"/connect"}
	)
public class ConnectServlet extends HttpServlet {

	private static final long serialVersionUID = 964628898403346597L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("1");
	}
}
