package com.xui.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.xui.utils.LogUtils;

public class LoginInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionContext context = invocation.getInvocationContext();
		Map<String, Object> map = context.getSession();
		LogUtils.log("userName:"+(map.get("userName")==null));
		if(map.get("userName")==null)
			return Action.LOGIN;
		else
			return invocation.invoke();
	}

}
