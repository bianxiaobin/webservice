package com.xui.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.xui.utils.LogUtils;
/***
 * 用来拦截key的，检查key是否正确
 * @author 17310
 *
 */
public class IndexInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionContext context = invocation.getInvocationContext();
		Map<String, Object> map = context.getSession();
		LogUtils.log("key:"+(map.get("key")==null));
		if(map.get("key")==null)
			return Action.LOGIN;
		else
			return invocation.invoke();
	}

}
