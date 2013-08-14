package edu.vntu.mblog.web.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 8/13/13, 10:59 AM
 *
 * @author Sergey Khruschak (sergey.khruschak@gmail.com)
 */
@WebFilter("/")
public class ExceptionsFilter implements Filter {

    private final Logger log = Logger.getLogger(ExceptionsFilter.class.getName());

    @Override
    public void destroy() {}

    public void init(FilterConfig config) throws ServletException {

    }


    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, resp);
        } catch (Exception e) {
            HttpServletRequest httpReq = (HttpServletRequest) req;

            log.log(Level.WARNING,
                    "Exception while processing request " + httpReq.getMethod() + ' ' + httpReq.getRequestURI(),
                    e);
        }
    }


}
