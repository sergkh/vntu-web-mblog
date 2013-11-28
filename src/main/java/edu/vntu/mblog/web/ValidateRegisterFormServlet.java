package edu.vntu.mblog.web;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.services.UsersService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;


@WebServlet(value = "/validate")
public class ValidateRegisterFormServlet extends HttpServlet {

	private static final long serialVersionUID = 7828326412316643125L;

	private UsersService usersService;

    private final ObjectMapper serializer = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        usersService = context.getBean(UsersService.class);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		//String exists = request.getParameter("exists");
		    
		if(login != null) {
			User user = usersService.getUser(login);

            serializer.writeValue(response.getOutputStream(),
                                  new UserExistsResponse(user != null));
		} else if(password != null) {
			final int len = password.length();

            boolean valid = len >= 6 && len <= 256;

            serializer.writeValue(response.getOutputStream(),
                    new PasswordValidResponse(valid));
		}

    }

    public static class UserExistsResponse {

        private final boolean exists;

        public UserExistsResponse(boolean exists) {
            this.exists = exists;
        }

        public boolean isExists() {
            return exists;
        }
    }

    public static class PasswordValidResponse {

        private final boolean valid;

        public PasswordValidResponse(boolean valid) {
            this.valid = valid;
        }

        public boolean isValid() {
            return valid;
        }
    }

}
