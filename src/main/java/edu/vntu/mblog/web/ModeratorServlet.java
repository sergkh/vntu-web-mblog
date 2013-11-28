package edu.vntu.mblog.web;

import edu.vntu.mblog.domain.Post.State;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.PostsService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 */
@WebServlet("/moderator")
public class ModeratorServlet extends AbstractMblogSpringServlet {
	private static final long serialVersionUID = 1L;
    
	private static final int POSTS_LIMIT = 100;
	
	//private final UsersService usersService = UsersService.getInstance();
	private PostsService postsService;

    @Override
    public void init() throws ServletException {
        super.init();
        postsService = getBean(PostsService.class);
    }
	
	
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
            case "disablePost":
            	postsService.moderatePost(postId, State.DISABLED);
                break;
            case "confirmPost":
                postsService.moderatePost(postId, State.CONFIRMED);
                break;

            default:
            	resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Unknown or missing action parameter value: " + action);
        }

        resp.sendRedirect(req.getContextPath() + "/moderator");

	}

}
