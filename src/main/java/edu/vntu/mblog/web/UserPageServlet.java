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
import edu.vntu.mblog.services.PostsService;
import edu.vntu.mblog.services.UsersService;

/**
 * 
 */
@WebServlet("/users/*")
public class UserPageServlet extends AbstractMblogSpringServlet {
	private static final long serialVersionUID = 3203753443583716314L;
	
	private PostsService postsService;
	private UsersService usersService;

    @Override
    public void init() throws ServletException {
        super.init();
        usersService = getBean(UsersService.class);
        postsService = getBean(PostsService.class);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().log("Rendering users page");
		
		String login = request.getPathInfo().replace("/", "");
		
		try {

            User user = usersService.getUser(login);

			request.setAttribute("user", user);
			request.setAttribute("userStat", usersService.getStatistics(login));

			HttpSession session = request.getSession(false);
			
			if(session != null && session.getAttribute(SessionConstants.USER) != null) {
                User curUser = (User) session.getAttribute(SessionConstants.USER);
				request.setAttribute("subscribed", usersService.isSubscribed(login, curUser.getLogin()));
                request.setAttribute("isMe", login.equals(curUser.getLogin()));
			} else {
				request.setAttribute("subscribed", false);
                request.setAttribute("isMe", false);
			}
			
			request.setAttribute("posts", postsService.getUsersFeed(login));
			
			
	        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/user_page.jsp");
	        view.forward(request, response);
        
		} catch (UserNotFoundException unfe) {
			unfe.printStackTrace();

            request.setAttribute("user", unfe.getIdentifier());
	        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/errors/user_not_found.jsp");
	        view.forward(request, response);
        
		}
	}


}
