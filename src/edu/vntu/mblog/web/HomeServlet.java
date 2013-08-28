package edu.vntu.mblog.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Date: 8/13/13, 4:40 PM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@WebServlet(value = "")
public class HomeServlet extends HttpServlet {

	private static final long serialVersionUID = 4289764775386171808L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	getServletContext().log("Rendering users page");
    	
    	HttpSession s = request.getSession(false);
		
    	if(s != null && s.getAttribute("login") != null) {
			// redirect to users page
			response.sendRedirect(request.getContextPath() + "/users/" + s.getAttribute("login"));
		} else {
			// no user session found - show login/register page
	        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
	        view.forward(request, response);
		}
    	
    }
}
