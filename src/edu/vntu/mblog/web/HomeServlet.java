package edu.vntu.mblog.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Date: 8/13/13, 4:40 PM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@WebServlet(value = "")
public class HomeServlet extends HttpServlet {

    private Logger log = Logger.getLogger(HomeServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("Rendering login page");
        RequestDispatcher view = request.getRequestDispatcher("WEB-INF/jsp/login.jsp");
        view.forward(request, response);
    }
}
