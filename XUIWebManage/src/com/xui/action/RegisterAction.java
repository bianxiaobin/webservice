package com.xui.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.xui.bean.User;
import com.xui.db.UserDB;
import com.xui.utils.MD5;

public class RegisterAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	UserDB userdb = new UserDB();
	private String userName;
	private String pwd;
	private String pwdAgain;
	@Override
	public String execute() throws Exception {
		if(checkRegisterForm())return ERROR;
		User user = new User();
		user.setUser_name(userName);
		user.setPwd(MD5.getMD5(pwd));
		int count = userdb.insertUser(user);
		if(count > 0){
			return SUCCESS;
		}else{
			return ERROR;
		}
	}
	private boolean checkRegisterForm(){
		if(StringUtils.isEmpty(userName)){
			this.addFieldError("errorMessage", "�û�������Ϊ�գ�");
			return true;
		}
		if(StringUtils.isEmpty(pwd)){
			this.addFieldError("errorMessage", "���벻��Ϊ��");
			return true;
		}
		if(StringUtils.isEmpty(pwdAgain) || !pwd.equals(pwdAgain)){
			this.addFieldError("errorMessage", "���벻һ�£�");
			return true;
		}
		if(userName.length()>8){
			this.addFieldError("errorMessage", "�û�������Ҫ��8���ַ��ڣ�");
			return true;
		}
		if(pwd.length() > 16){
			this.addFieldError("errorMessage", "�������С��ʮ��λ��");
			return true;
		}
		Pattern pattern = Pattern.compile("^\\w+$");
		Matcher matcher = pattern.matcher(pwd);
		if(!matcher.matches()){
			this.addFieldError("errorMessage", "�������������֣���ĸ���»��ߣ�");
			return true;
		}
		if(!pattern.matcher(userName).matches()){
			this.addFieldError("errorMessage", "�û�����������֣���ĸ���»��ߣ�");
			return true;
		}
		return false;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPwdAgain() {
		return pwdAgain;
	}
	public void setPwdAgain(String pwdAgain) {
		this.pwdAgain = pwdAgain;
	}
	
}
