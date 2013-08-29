package edu.vntu.mblog.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.services.UsersService;

/**
 * Servlet implementation class SubscriptionsServlet
 */
@WebServlet("/subscriptions/*")
public class SubscriptionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final UsersService usersService = UsersService.getInstance();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		manageSubscription(req, resp, true);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		manageSubscription(req, resp, false);
	}
	
	private void manageSubscription(HttpServletRequest req, HttpServletResponse resp, boolean addOrRemove) {
		String login = req.getPathInfo().replace("/", "");
		try {
			HttpSession session = req.getSession(false); 
			User curUser = (User) session.getAttribute(SessionConstants.USER);
			
			if(addOrRemove) {
				usersService.subscribe(login, curUser.getLogin());
			} else {
				usersService.unsubscribe(login, curUser.getLogin());
			}
        
		} catch (UserNotFoundException unfe) {
			unfe.printStackTrace();
			// TODO: show error page here
		}
	}
	
}
