<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>            
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统操作</title>
</head>
<body>
<div class="main_body">
	<div>
	<table>
		<tr>
			<th>操作</th>
			<th>描述</th>		
		</tr>
		<tr>
			<td><input style="width:50%;height:90%;" type="button" value="重启" onclick="if(confirm('are you sure to reboot ?') ) location.href='${pageContext.request.contextPath}/sys/reload.do'" /></td>
			<td>重启${pageContext.request.contextPath}这个服务,并不是重启服务器</td>
		</tr>
	</table>
	</div>
</div>


<script type="text/javascript">
if("${Message}"!=''){
	alert("${Message}");
	location.href="${pageContext.request.contextPath}/sys/toSysOperation.do";
}

</script>
</body>
</html>