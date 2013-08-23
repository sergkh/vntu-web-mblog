package edu.vntu.mblog.web.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Servlet Filter implementation class AuthorizatioFilter
 */
@WebFilter("/*")
public class AuthorizatioFilter implements Filter {

    public AuthorizatioFilter() {}

	public void init(FilterConfig fConfig) throws ServletException {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}
}
