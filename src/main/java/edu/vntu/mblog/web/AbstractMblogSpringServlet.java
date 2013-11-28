package edu.vntu.mblog.web;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class AbstractMblogSpringServlet extends HttpServlet{

    private ApplicationContext context;

    @Override
    public void init() throws ServletException {
        super.init();
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    public <T> T getBean(Class<T> type){
        return context.getBean(type);
    }
}
