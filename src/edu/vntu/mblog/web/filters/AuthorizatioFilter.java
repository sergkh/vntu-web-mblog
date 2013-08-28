package edu.vntu.mblog.web.filters;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vntu.mblog.domain.User;

/**
 * Servlet Filter implementation class AuthorizatioFilter
 */
@WebFilter("/*")
public class AuthorizatioFilter implements Filter {

    public AuthorizatioFilter() {}

    private final AuthRule[] rules = {
    		new AuthRule("/admin/.*", User.Permission.MANAGE_USERS),
    		new AuthRule("/messages/", User.Permission.USER)
    }; 
    
	public void init(FilterConfig fConfig) throws ServletException {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request; 
		int contextPathLen = httpReq.getContextPath().length();
		
		User.Permission requiredPerm = findRequredPermission(httpReq.getRequestURI().substring(contextPathLen));
		
		if(requiredPerm != null) {
			HttpSession session = httpReq.getSession(false);
			
			if(session != null) {
				User.Permission perm = (User.Permission) session.getAttribute("permissions");
				
				if(requiredPerm == perm) {
					chain.doFilter(request, response);
				}
			}
			
			authFailedResponse(httpReq, (HttpServletResponse)response);
			
		} else {
			chain.doFilter(request, response);	
		}
	}
	
	private void authFailedResponse(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		
		RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/errors/auth-failed.jsp");
        view.forward(req, response);
	}

	private User.Permission findRequredPermission(String url) {
		for(AuthRule r: rules) {
			if(r.matchesUrl(url)) return r.getPermission();
		}
		
		return null;
	}
	
	
	private static class AuthRule {
		private Pattern pattern;
		private User.Permission permission;
		
		public AuthRule(String urlPattern, User.Permission perm) {
			this.pattern = Pattern.compile(urlPattern);
			this.permission = perm;
		}
		
		boolean matchesUrl(String url) {
			return pattern.matcher(url).matches();
		}
		
		User.Permission getPermission() {
			return permission;
		}
	}
}
