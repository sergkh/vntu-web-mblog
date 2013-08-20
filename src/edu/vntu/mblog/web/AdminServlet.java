package edu.vntu.mblog.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.vntu.mblog.jdbc.ConnectionFactory;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Connection c = ConnectionFactory.createConnection();
			Statement st = c.createStatement();
			st.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
    	getServletContext().log("Rendering admin page");
        RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/jsp/admin_users_list.jsp");
        view.forward(request, response);
	}
}
