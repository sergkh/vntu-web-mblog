package edu.vntu.mblog.web;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.services.UsersService;

import java.io.IOException;


@WebServlet(value = "/validate")
public class ValidateRegisterFormServlet extends HttpServlet {

	private static final long serialVersionUID = 7828326412316643125L;

	private final UsersService usersService = UsersService.getInstance();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		//String exists = request.getParameter("exists");
		    
		if(login!=null){
			User user = usersService.getUser(login);    
				
				if(user != null) {
	              // request.setAttribute("exists", "true");
	               response.getWriter().println("{ \"exists\" : true }");
				} 
				else {
					//request.setAttribute("exists", "false");
					response.getWriter().println("{ \"exists\" : false }");
				}
		}
		else if(password!=null){
			final int len = password.length();
			 if (len >= 6 && len <= Integer.MAX_VALUE){
				 response.getWriter().println("{ \"r_pass\" : true }");
			 }
			 else{
				 response.getWriter().println("{ \"r_pass\" : false }");
			 }
			
		}
			
		
    }

}
