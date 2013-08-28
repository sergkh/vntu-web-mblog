package edu.vntu.mblog.web.tags;

import java.util.EnumSet;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import edu.vntu.mblog.domain.User.Permission;

public class PermissionsTagHandler extends TagSupport {
	
	private static final long serialVersionUID = -4953710291722438417L;
	
	private String permissions;

	public PermissionsTagHandler() {}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		
		if(session == null || session.getAttribute("permissions") == null) {
			return SKIP_BODY;
		}
		
		EnumSet<Permission> userPerm = 
				(EnumSet<Permission>) session.getAttribute("permissions");
		
		boolean result = userPerm.contains(Permission.valueOf(permissions.toUpperCase()));
		
		return result ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}


}
