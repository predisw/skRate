<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/index.jsp" %>                
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统日志</title>
</head>
<body>
<div class="main_body">
	<div >
		日志等级:            <!-- 错误提示:-->   <span style="color:red;display:inline-block;margin-left:5%;">${Message}</span> 
		<form action="${pageContext.request.contextPath}/log/updateLogSetting.do"  method="POST">
			操作:
			<select name="logParams['com.skyline']" >
				<option selected>${params['com.skyline'] }</option>
				<option>WARN</option>
				<option>DEBUG</option>
			</select>
			spring:
			<select name="logParams['org.springframework']" >
				<option selected>${params['org.springframework'] }</option>
				<option>WARN</option>
				<option>INFO</option>
				<option>DEBUg</option>
			</select>
			hibernate:
			<select name="logParams['org.hibernate']" >
				<option selected>${params['org.hibernate'] }</option>
				<option>WARN</option>
				<option>INFO</option>
				<option>DEBUG</option>
			</select>
			mysql-driver:
			<select name="logParams['com.zaxxer']" >
				<option selected >${params['com.zaxxer'] }</option>
				<option>WARN</option>
				<option>INFO</option>
				<option>DEBUG</option>
			</select>

			<input type="submit" value="确定"  style="display:inline-block; margin-left:10%;"/>
		</form>
	</div>

	<div>
	日志文件:
		<table>
			<c:forEach items="${logsSet}" var="log" begin="10">
				<tr>
						<td>${log}</td>
						<td><a href="${pageContext.request.contextPath}/log/downloadFile.do?name=${log}">下载</a></td>
				</tr>
		</c:forEach>
		
		
		
		
		</table>
	</div>
	

</div>

</body>
</html>