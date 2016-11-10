package com.xui.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xui.utils.ConfigUtil;
import com.xui.utils.LogUtils;
import com.xui.utils.MD5;
import com.xui.utils.Utils;

public class IndexAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String key;
	private String mdKey;
	public IndexAction() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowDate = sdf.format(new Date());
		mdKey = MD5.getMD5(nowDate+ConfigUtil.KEY);
		LogUtils.log("day:"+nowDate);
	}
	@Override
	public String execute() throws Exception {
		LogUtils.log("key:"+key);
		LogUtils.log("nowKey:"+mdKey);
		if(StringUtils.isEmpty(key)){
			this.addFieldError("errorMessage", "呼~呼~呼~呼~，测的太累了，不测了。这里什么都没有(⊙o⊙)哦！");
			return ERROR;
		}
		if(key.equals(mdKey)){
			ActionContext context = ActionContext.getContext();
			Map<String, Object> map = context.getSession();
			map.put("key", mdKey);
			return SUCCESS;
		}else{
			this.addFieldError("errorMessage", "(⊙﹏⊙)b！！！");
			return ERROR;
		}
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
