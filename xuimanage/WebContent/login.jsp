<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>登录</title>
	<link rel="stylesheet" type="text/css" href="jquery/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="jquery/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="jquery/demo.css">
	<script type="text/javascript" src="jquery/jquery.min.js"></script>
	<script type="text/javascript" src="jquery/jquery.easyui.min.js"></script>
	<script type="text/javascript">
		function onLoad(){
			$.post("loginAction.action");
		}
	</script>
</head>
<body>
	<h2>XUI管理系统</h2>
	<div style="margin:20px 0;"></div>
	<div class="easyui-panel" title="登录" style="width:400px">
		<div style="padding:10px 60px 20px 60px">
	    <form id="loginForm" method="post" action="loginAction.action">
	    	<table cellpadding="5">
	    		<tr>
	    			<td>用户名:</td>
	    			<td><input class="easyui-textbox" type="text" name="loginName" data-options="required:false"></input></td>
	    		</tr>
	    		<tr>
	    			<td>密码:</td>
	    			<td><input class="easyui-textbox" type="password" name="loginPwd" data-options="required:false"></input></td>
	    		</tr>
	    	</table><br>
	    	<input type="submit" value="登录"/><br><br><br><br>
	    </form>
	</div>
	</br>
	<a href="pro.html">去看看进度条</a>
</body>
</html>