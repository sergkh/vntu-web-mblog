package edu.vntu.mblog.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.PostsService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


@WebServlet("/messages")
public class PostsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private PostsService postsService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        postsService = context.getBean(PostsService.class);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String text = request.getParameter("text");
		
		HttpSession s = request.getSession();

        User user = (User) s.getAttribute(SessionConstants.USER);
		
		try {
			postsService.createPost(user.getLogin(), text);
			
			response.sendRedirect(request.getContextPath() + "/users/"+ user.getLogin());
		} catch (UserNotFoundException e) {
	        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/errors/user_not_found.jsp");
	        view.forward(request, response);
	        
	        e.printStackTrace();
	        
		} catch (ValidationException e) {
			// TODO and this
			e.printStackTrace();
		}
	}

}
