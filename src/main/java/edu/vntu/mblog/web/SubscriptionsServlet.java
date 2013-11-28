package edu.vntu.mblog.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.services.UsersService;
import edu.vntu.mblog.services.impl.UsersServiceImpl;

/**
 * Servlet implementation class SubscriptionsServlet
 */
@WebServlet("/subscriptions/*")
public class SubscriptionsServlet extends AbstractMblogSpringServlet {
	private static final long serialVersionUID = 1L;
	
	private UsersService usersService;

    @Override
    public void init() throws ServletException {
        super.init();
        usersService = getBean(UsersService.class);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		
		switch(action) {
		case "subscribe" : 
			manageSubscription(req, resp, true); 
			break; 
		case "unsubscribe" :
			manageSubscription(req, resp, false);
			break;
		default:
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown or missing action parameter value: " + action); 
		}
	}
	
	private void manageSubscription(HttpServletRequest req, HttpServletResponse resp, boolean addOrRemove) throws ServletException, IOException {
		String login = req.getPathInfo().replace("/", "");
		try {
			HttpSession session = req.getSession(false); 
			User curUser = (User) session.getAttribute(SessionConstants.USER);

            usersService.toggleSubscription(login, curUser.getLogin(), addOrRemove);

			// redirect back to users page
			resp.sendRedirect(req.getContextPath() + "/users/"+ login);
			
		} catch (UserNotFoundException unfe) {
	        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/errors/user_not_found.jsp");
	        view.forward(req, resp);
	        
	        unfe.printStackTrace();
		}
	}
	
}
