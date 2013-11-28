package edu.vntu.mblog.web;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.UsersService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/admin")
public class AdminServlet extends AbstractMblogSpringServlet {
	private static final long serialVersionUID = 1L;
    
	private static final int USERS_LIMIT = 100;
	
	private UsersService usersService;

    @Override
    public void init() throws ServletException {
        super.init();
        usersService = getBean(UsersService.class);
    }

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
            case "unblock" :
                usersService.toggleUser(userId, true);
                break;
            case "block" :
                usersService.toggleUser(userId, false);
                break;
            case "addPermission" :
                usersService.togglePermission(userId,
                                              User.Permission.valueOf(req.getParameter("permission")),
                                              true);
                break;
            case "removePermission" :
                usersService.togglePermission(userId,
                                              User.Permission.valueOf(req.getParameter("permission")),
                                              false);
                break;

            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                               "Unknown or missing action parameter value: " + action);
        }

        resp.getWriter().println("{}");
    }
}
