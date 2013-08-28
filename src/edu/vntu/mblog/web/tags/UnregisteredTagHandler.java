package edu.vntu.mblog.web.tags;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class UnregisteredTagHandler extends TagSupport {
	
	private static final long serialVersionUID = -4953710291722438417L;
	
	private String permissions;

	public UnregisteredTagHandler() {}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		
		return (session == null || session.getAttribute("permissions") == null) ?
				EVAL_BODY_INCLUDE : SKIP_BODY; 
	}
}
