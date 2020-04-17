<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户登录</title>
</head>
<body>
用户登录信息：
<!-- 使用了thymeleaf模板进行渲染，这里就要使用对应的标签才能展示内容 -->
<span th:text="${errorMsg}"></span>
<form action="/login/doLogin" method="post">
<table border="1px">
	<tr>
		<td width="20%">用户名</td>
		<td width="80%"><input type="text" name="userName"/></td>
	</tr>
	<tr>
		<td width="20%">密码</td>
		<td width="80%"><input type="password" name="password"/></td>
	</tr>
	<tr>
		<td colspan="2">
		   <input type="submit" value="登录">
		   <input type="reset" value="重置">
		</td>
	</tr>
</table>
</form>
<a href="/register">跳转注册页面</a>
</body>
</html>
