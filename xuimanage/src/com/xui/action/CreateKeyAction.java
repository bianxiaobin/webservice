package com.xui.action;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.xui.utils.ConfigUtil;
import com.xui.utils.LogUtils;
import com.xui.utils.Utils;

public class CreateKeyAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static Map<String,String> configMap = Utils.getConfig();
	@Override
	public String execute() throws Exception {
		Calendar cal = Calendar.getInstance();
		int nowYear = cal.get(Calendar.YEAR);
		String oldKeyPath = configMap.get(ConfigUtil.KEYPATH)+(nowYear-1)+ConfigUtil.KEYFILE;
		File oldFile = new File(oldKeyPath);
		//��һ���keyɾ��
		if(oldFile.exists()){
			oldFile.delete();
		}
		String nowKeyPath = configMap.get(ConfigUtil.KEYPATH)+nowYear+ConfigUtil.KEYFILE;
		File file = new File(nowKeyPath);
		if(file.exists()){
			this.addFieldError("errorMessage", "key�ļ��Ѿ����ڲ���Ҫ������");
			return ERROR;
		}
		LogUtils.log(file.getPath());
		Utils.createYearKey(file);
		return SUCCESS;
	}
	
}
