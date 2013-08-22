package edu.vntu.mblog.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.vntu.mblog.services.UsersService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final int USERS_LIMIT = 100;
	
	private final UsersService usersService = new UsersService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	getServletContext().log("Rendering admin page");
    	
		request.setAttribute("users", usersService.getUsersList(0, USERS_LIMIT));
    	
        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/admin_users_list.jsp");
        view.forward(request, response);
	}
}
