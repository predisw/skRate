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


	<div class="leftfloat">
		用户列表
		<div  >
		<select class="selectS"    id="user" name="user"   onchange="getRolesByUser('user')" >
			<option></option>
			<c:forEach items="${users }" var="user">
				<option  id="${user.UId }"  value="${user.UId}" >${user.UName }</option>
			
			</c:forEach>
		</select>
		</div>
	</div>


		<div class="leftfloat">
		用户拥有的角色
			<select multiple="multiple"    class="selectM"  id="used_roles"  name="used_roles">

			</select>
			<input type="button" value="移除" onclick="rmSelected('used_roles')">
			<input type="button" value="修改" onclick="if(confirm('确定修改?'))updateUserRole('user','used_roles')">
		</div>

	
	<div class="leftfloat">
		所有可用角色
		<select multiple="multiple"     class="selectM" id="all_roles">
			<c:forEach items="${roles }" var="role">
				<option value="${role.id}">${role.name }</option>
			</c:forEach>
		</select>
		<input type="button" value="选择" onclick="cpSelected('all_roles','used_roles')">
		
	</div>



</div>

<script type="text/javascript">

function getRolesByUser(id){
	var user = document.getElementById(id);

	var id=user.options[user.selectedIndex].value;

	var url="${pageContext.request.contextPath}/powers/getRolesByUser.do";
	
	$( function(){
		$.post(url, {"id":id}, function(data,status){
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


function updateUserRole(user,selectId){
	var user= document.getElementById(user);
	var uId=user.value;

	
	var url="${pageContext.request.contextPath }/powers/saveOrUpdateRolesToUser.do";
	var select = document.getElementById(selectId);
	var ids="";
	for(var i=0;i<select.length;i++){
		ids=ids+select.options[i].value;
		if((i+1)<select.length){
			ids+=",";
		}
	}
	$(function(){
			$.post(url,{"ids":ids,"uId":uId},function(data){
					alert(data);
	
			});
		});
}
	

	




</script>


</body>
</html>