<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/skyline.ico" type="image/x-icon" rel="shortcut icon">
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet"  type="text/css" />
<title>登录</title>
</head>
<body>
<div class="login">
<form  method="POST"  id="login" action="${pageContext.request.contextPath}/user/login.do"  >
<div class="login_name" >
登录名:
<input type="text" name="UName" value=""  class="login_input" />
</div>
<div class="login_password">
密码:
<input type="password" name="password" value=""  class="login_input" /> 
</div>
<input type="submit" value="登录" class="login_submit"  > 
</form>

</div>

<script type="text/javascript">
if('${login_error}'!=null && '${login_error}'!=""){
	alert('${login_error}');
}



</script>

</body>
</html>
