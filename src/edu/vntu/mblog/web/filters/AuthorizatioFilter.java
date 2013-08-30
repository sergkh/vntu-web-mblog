package edu.vntu.mblog.web.filters;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.web.SessionConstants;

/**
 * Servlet Filter implementation class AuthorizatioFilter
 */
@WebFilter("/*")
public class AuthorizatioFilter implements Filter {

    public AuthorizatioFilter() {}

    private final AuthRule[] rules = {
    		new AuthRule("/admin.*", User.Permission.MANAGE_USERS),
    		new AuthRule("/messages/", User.Permission.USER)
    }; 
    
	public void init(FilterConfig fConfig) throws ServletException {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request; 
		int contextPathLen = httpReq.getContextPath().length();
		
		User.Permission requiredPerm = findRequredPermission(httpReq.getRequestURI().substring(contextPathLen));
		
		if(requiredPerm == null) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = httpReq.getSession(false);
		
		if(session != null) {
			User user = (User) session.getAttribute(SessionConstants.USER);
			
			if(user != null && user.getPermissions().contains(requiredPerm)) {
				// we have been authenticated - pass the request 
				chain.doFilter(request, response);
				return;
			}
		}
		
		authFailedResponse(httpReq, (HttpServletResponse)response);
	}
	
	private void authFailedResponse(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		// Will trigger auth_failed.jsp page as defined in web.xml
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
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
