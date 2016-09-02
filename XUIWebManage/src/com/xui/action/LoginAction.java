package com.xui.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xui.bean.User;
import com.xui.db.UserDB;
import com.xui.utils.MD5;

public class LoginAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String loginName;
	private String loginPwd;
	UserDB db = new UserDB();
	@Override
	public String execute() throws Exception {
		if(StringUtils.isEmpty(loginName)){
			this.addFieldError("errorMessage", "用户名不能为空！");
			return ERROR;
		}
		if(StringUtils.isEmpty(loginPwd)){
			this.addFieldError("errorMessage", "密码不能为空！");
			return ERROR;
		}
		User user = new User();
		user.setUser_name(loginName);
		String md5 = MD5.getMD5(loginPwd);
		user.setPwd(md5);
		User loginUser = db.login(user);
		if(loginUser != null && loginName.equals(loginUser.getUser_name()) && md5.equals(loginUser.getPwd())){
			ActionContext context = ActionContext.getContext();
			Map<String, Object> map = context.getSession();
			map.put("userName", user.getUser_name());
			return SUCCESS;
		}else{
			this.addFieldError("errorMessage", "密码与用户不匹配！");
			return ERROR;
		}
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
}
