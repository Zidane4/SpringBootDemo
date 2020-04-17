<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户锁定及解锁</title>
</head>
<body>
管理员页面，管理员可以对普通用户进行锁定或解锁

<form action="/userLock" method="post">
<table border="1px" width="100%">
	<tr>
		<td width="20%">用户</td>
		<td width="80%"><select name="userName">
		     <option>请选择</option>
			 <div th:each="user:${users}">
				<option th:value="${user.userName}"><div th:text="${user.userName}"></div></option>
			 </div>
		</select></td>
	</tr>
	<tr>
		<td width="20%">锁定/解锁</td>
		<td width="80%">
		   <input type="radio" name="locked" value="1" />锁定
		   <input type="radio" name="locked" value="0" />解锁
		</td>
	</tr>
	<tr>
		<td colspan="2">
		   <input type="submit" value="保存">
		   <input type="reset" value="重置">
		</td>
	</tr>
</table>
</form>
<a href="/login/doLogout">退出登录</a>
</body>
</html>
