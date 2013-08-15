package edu.vntu.mblog.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Date: 8/13/13, 10:57 AM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 7828326412316643125L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
