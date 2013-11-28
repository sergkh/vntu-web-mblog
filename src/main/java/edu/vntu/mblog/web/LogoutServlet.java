package edu.vntu.mblog.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Logout servlet just clears user session and redirects to site root.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
		HttpSession s = request.getSession(false);
		if(s != null) s.invalidate();
		response.sendRedirect(request.getContextPath());
	}

}
