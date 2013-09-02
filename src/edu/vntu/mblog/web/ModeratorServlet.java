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
//import edu.vntu.mblog.domain.User;
//import edu.vntu.mblog.errors.UserNotFoundException;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.PostsService;
//import edu.vntu.mblog.services.UsersService;

/**
 * 
 */
@WebServlet("/moderator")
public class ModeratorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final int POSTS_LIMIT = 100;
	
	//private final UsersService usersService = UsersService.getInstance();
	private final PostsService postsService = PostsService.getInstance();
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	getServletContext().log("Rendering moderator page");

    	try {
			request.setAttribute("posts", postsService.getAllPosts(0, POSTS_LIMIT));
		} catch (ValidationException e) {
			e.printStackTrace();
		}
    	
        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/moderator_posts_page.jsp");
        view.forward(request, response);
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

        String action = req.getParameter("action");
        long postId = Long.parseLong(req.getParameter("postId"));

        switch (action) {
            case "deletePost":

                break;
            case "confirmPost":
                postsService.confirmPost();
                break;

            default:
                // TODO:
        }

        resp.sendRedirect(req.getContextPath() + "/moderator");


//		String login = req.getParameter("msg.authorLogin");
//		String text = req.getParameter("msg.text");
//		
//		
//		
//		try {
//			postsService.confirmPost(login, text);
//			
//			resp.sendRedirect(req.getContextPath() + "/moderator");
//		
//		}
//		catch (UserNotFoundException unfe) {
//			unfe.printStackTrace();
//			// TODO: show error page here
//		}
//		catch (ValidationException e) {
//			// TODO and this
//			e.printStackTrace();
//		}
//		 
	}


	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doDelete(req, resp);
		
	}
	
	
	
	
	


}
