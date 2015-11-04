<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>            
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线人数</title>
</head>
<body>
<div class="main_body" >
	<div style="border-bottom:none;">
		<table >
			<tr>
				<th>姓名</th>
				<th>IP</th>
				<th>浏览器</th>
				<th>登录时间</th>

			</tr>
			<c:forEach items="${onliners }"  var="onliner">
				<tr>
					<td>${onliner.name}</td>
					<td>${onliner.ip}</td>
					<td>${onliner.browser}</td>
					<td > <fmt:formatDate value="${onliner.loginTime }"  pattern="yyyy-MM-dd HH:mm:ss"/></td>

				</tr>
			
			</c:forEach>
			
			
		
		
		</table>
	</div>
</div>

<script type="text/javascript">

function formatT(tt){
//	var td = document.getElementById("loginTime");
	var lt =new Date( tt);  //important !!


	var mStr=lt.getMonth()+1;  //月
	if(mStr<10){
		mStr="0"+mStr;
		}

	var dStr=lt.getDate(); //日
	if(dStr<10){
		dStr="0"+dStr;
		}
	//alert(lt.toLocaleString());
	alert(lt.getFullYear()+"-"+mStr+"-"+dStr+" "+lt.getHours()+":"+lt.getMinutes()+":"+lt.getSeconds());



	
}



</script>


</body>
</html>