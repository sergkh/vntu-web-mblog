package edu.vntu.mblog.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.UsersService;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Date: 8/13/13, 10:57 AM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@WebServlet(value = "/register")
public class RegisterServlet extends AbstractMblogSpringServlet{

	private static final long serialVersionUID = 7828326412316643125L;

	private UsersService usersService;

    @Override
    public void init() throws ServletException {
        super.init();
        usersService = getBean(UsersService.class);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try {
			User user = usersService.register(login, email, password);
			
			HttpSession s = request.getSession(); 
			s.setAttribute(SessionConstants.USER, user);

			response.sendRedirect(request.getContextPath() + "/users/"+ login);
			
		} catch (ValidationException ve) {
			ve.printStackTrace();
			response.sendRedirect(request.getContextPath() + 
					"?errorMsg=" + URLEncoder.encode(ve.getMessage(), "UTF-8"));	
		}
		
		
    }

}
