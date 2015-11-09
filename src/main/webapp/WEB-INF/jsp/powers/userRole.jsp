<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>               
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/userRole.css" rel="stylesheet" type="text/css"/>
<script src="${pageContext.request.contextPath}/js/my.js" ></script>
<script src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js" ></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户And角色</title>
</head>
<body>
<div class="ur_body">

	<form action="${pageContext.request.contextPath }/powers/saveOrUpdateRolesToUser.do"  method="post">
	<div class="leftfloat">
		用户列表

		<select size="30"  class="selectM" id="user" name="user">
			<c:forEach items="${users }" var="user">
				<option  id="${user.UId }"  value="${user.UId}"  onclick="getRolesByUser(${user.UId})">${user.UName }</option>
			
			</c:forEach>
		</select>
	
	</div>


		<div class="leftfloat">
		用户拥有的角色
			<select multiple="multiple"    class="selectM"  id="used_roles"  name="used_roles">
	
			</select>
			<input type="button" value="移除" onclick="mvSelected('used_roles','all_roles')">
			<input type="submit" value="添加" onclick="">
		</div>
	</form>
	
	<div class="leftfloat">
		所有可用角色
		<select multiple="multiple"     class="selectM" id="all_roles">
			<c:forEach items="${roles }" var="role">
				<option value="${role.id}">${role.name }</option>
			</c:forEach>
		</select>
		<input type="button" value="选择" onclick="mvSelected('all_roles','used_roles')">
		
	</div>



</div>

<script type="text/javascript">

function getRolesByUser(id){
	var user = document.getElementById(id);
	var url="${pageContext.request.contextPath}/powers/getRolesByUser.do";

	
	$( function(){
		$.post(url, {"id":user.value}, function(data,status){
			if(status=="success"){
				var roleNames =JSON.parse(data);
//				alert(data);
				addOptionToSelect('used_roles',roleNames);

				}


			});	
		});
	

	
}

function addOptionToSelect(id,dataArr){
	var select=document.getElementById(id);

//	alert(cus.length);
	select.options.length=0;
	for(var i=0;i<dataArr.length;i++){
				var op=document.createElement("option"); 
				op.value=dataArr[i].value
				op.innerHTML=dataArr[i].name;
				select.appendChild(op);
				
		}
	
}



</script>


</body>
</html>