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
	private String loginKey;
	private String key;
	private String name;
	private String pwd;
	private static Map<String,String> configMap = Utils.getConfig();
	public LoginAction(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowDate = sdf.format(new Date());
		key = Utils.getKey(configMap.get(ConfigUtil.KEYPATH), nowDate);
		name = configMap.get(ConfigUtil.LOGINNAME);
		pwd = configMap.get(ConfigUtil.LOGINPWD);
		LogUtils.log("key:"+key);
	}
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
		if(StringUtils.isEmpty(loginKey)){
			this.addFieldError("errorMessage", "key不正确！");
			return ERROR;
		}
		if(loginName.equals(name) && loginPwd.equals(pwd)){
			if(!key.equals(loginKey)){
				this.addFieldError("errorMessage", "key不正确！");
				return ERROR;
			}
			ActionContext context = ActionContext.getContext();
			Map<String, Object> map = context.getSession();
			map.put("userName", loginName);
			return SUCCESS;
		}else{
			this.addFieldError("errorMessage", "密码与用户不匹配！");
			return ERROR;
		}
	}
	
	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
}
