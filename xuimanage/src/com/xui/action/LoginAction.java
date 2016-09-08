package com.xui.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xui.utils.ConfigUtil;
import com.xui.utils.LogUtils;
import com.xui.utils.MD5;
import com.xui.utils.Utils;

public class LoginAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String loginName;
	private String loginPwd;
	private String name;
	private String pwd;
	private static Map<String,String> configMap = Utils.getConfig();
	@Override
	public String execute() throws Exception {
		name = configMap.get(ConfigUtil.LOGINNAME);
		pwd = configMap.get(ConfigUtil.LOGINPWD);
		if(StringUtils.isEmpty(loginName)){
			this.addFieldError("errorMessage", "用户名不能为空！");
			return ERROR;
		}
		if(StringUtils.isEmpty(loginPwd)){
			this.addFieldError("errorMessage", "密码不能为空！");
			return ERROR;
		}
		if(loginName.equals(name) && loginPwd.equals(pwd)){
			ActionContext context = ActionContext.getContext();
			Map<String, Object> map = context.getSession();
			map.put("userName", loginName);
			return SUCCESS;
		}else{
			this.addFieldError("errorMessage", "密码与用户不匹配！");
			return ERROR;
		}
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
}
