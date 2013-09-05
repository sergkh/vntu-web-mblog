package edu.vntu.mblog.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.UsersService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final int USERS_LIMIT = 100;
	
	private final UsersService usersService = UsersService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	getServletContext().log("Rendering admin page");
    	
		try {
			request.setAttribute("users", usersService.getUsersList(0, USERS_LIMIT));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
    	
        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/admin_users_list.jsp");
        view.forward(request, response);
	}

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        long userId = Long.parseLong(req.getParameter("userId"));

        switch(action) {
            case "enableUser" :
                usersService.toggleUser(userId, true);
                break;
            case "disableUser" :
                usersService.toggleUser(userId, false);
                break;
            case "hireModerator" :
                usersService.togglePermission(userId, User.Permission.MODERATE_POSTS, true);
                break;
            case "fireModerator" :
                usersService.togglePermission(userId, User.Permission.MODERATE_POSTS, false);
                break;
            case "hireAdministrator" :
                usersService.togglePermission(userId, User.Permission.MANAGE_USERS, true);
                break;
            case "fireAdministrator" :
                usersService.togglePermission(userId, User.Permission.MANAGE_USERS, false);
                break;

            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                               "Unknown or missing action parameter value: " + action);
        }
    }
}
