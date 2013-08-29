package edu.vntu.mblog.web.tags;

import edu.vntu.mblog.domain.User;
import edu.vntu.mblog.domain.User.Permission;
import edu.vntu.mblog.web.SessionConstants;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class PermissionsTagHandler extends TagSupport {
	
	private static final long serialVersionUID = -4953710291722438417L;
	
	private Permission permissions;

	public PermissionsTagHandler() {}

	public String getPermissions() {
		return permissions.name();
	}

	public void setPermissions(String permissions) {
		this.permissions = Permission.valueOf(permissions.toUpperCase());
	}
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		
		if(session == null || session.getAttribute(SessionConstants.USER) == null) {
			return SKIP_BODY;
		}


        User user = (User) session.getAttribute(SessionConstants.USER);

		boolean result = user.getPermissions().contains(permissions);
		
		return result ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}


}
