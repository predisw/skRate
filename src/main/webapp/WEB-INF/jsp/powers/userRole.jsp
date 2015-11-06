<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>               
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/userRole.css" rel="stylesheet" type="text/css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户And角色</title>
</head>
<body>
<div class="ur_body">

	
	<div class="leftfloat">
	<!--用户列表 -->
		<select size="10" onclick="">
			<c:forEach items="${users }" var="user">
				<option>${user.UName }</option>
			
			</c:forEach>
		</select>
	
	</div>


	<div class="leftfloat">
	<!-- 用户拥有的角色 -->
	<select size="10">
		
	
	</select>
	</div>

	<div class="leftfloat">
		<!--  所有可用角色-->
		<select size="10">
			<c:forEach items="${roles }" var="role">
				<option>${role.name }</option>
			</c:forEach>
		</select>
	</div>



</div>



</body>
</html>