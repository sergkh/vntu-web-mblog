package edu.vntu.mblog.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.errors.AuthenticationExeption;
import edu.vntu.mblog.services.UsersService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Date: 8/13/13, 10:57 AM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 7828326412316643125L;

	private UsersService usersService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        usersService = context.getBean(UsersService.class);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("session-login");
		String password = request.getParameter("session-password");
		
		try {
			User user = usersService.login(login, password);

			HttpSession s = request.getSession();
			s.setAttribute(SessionConstants.USER, user);

			response.sendRedirect(request.getContextPath() + "/users/"+ login);

		} catch (AuthenticationExeption authEx) {
			authEx.printStackTrace();
			response.sendRedirect(request.getContextPath() + "?errorMsg=" + URLEncoder.encode(authEx.getMessage(), "UTF-8"));	
		}
    }

}
