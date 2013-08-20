package edu.vntu.mblog.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.ValidationException;
import edu.vntu.mblog.services.UsersService;

import java.io.IOException;

/**
 * Date: 8/13/13, 10:57 AM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@WebServlet(value = "/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 7828326412316643125L;

	private final UsersService usersService = new UsersService();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try {
			User user = usersService.register(login, email, password);
			
			request.getSession().setAttribute("login", login);
			request.getSession().setAttribute("email", email);
			request.getSession().setAttribute("roles", user.getRoles());
			
			response.sendRedirect(request.getContextPath() + "/users/"+ login);
		} catch (ValidationException ve) {
			ve.printStackTrace(); // TODO: handle exception
		}
		
		
    }

}
