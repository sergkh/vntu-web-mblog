package edu.vntu.mblog.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.services.PostsService;
import edu.vntu.mblog.services.UsersService;

/**
 * 
 */
@WebServlet("/users/*")
public class UserPageServlet extends HttpServlet {
	private static final long serialVersionUID = 3203753443583716314L;
	
	private final PostsService postsService = PostsService.getInstance();
	private final UsersService usersService = UsersService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().log("Rendering users page");
		
		String login = request.getPathInfo().replace("/", "");
		
		try {
			request.setAttribute("user", login);
			request.setAttribute("userStat", usersService.getStatistics(login));
			
			HttpSession session = request.getSession(false); 
			
			if(session != null && session.getAttribute("login") != null) {
				String curUserLogin = (String) session.getAttribute("login");
				request.setAttribute("subscribed", usersService.isSubscribed(login, curUserLogin));
			} else {
				request.setAttribute("subscribed", false);
			}
			
			request.setAttribute("posts", postsService.getUsersFeed(login));
			
			
	        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/user_page.jsp");
	        view.forward(request, response);
        
		} catch (UserNotFoundException unfe) {
			unfe.printStackTrace();
			// TODO: show error page here
		}
	}


}
