<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>           
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js" ></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色页面</title>
</head>
<body>
<div>
	<div >
		<button type="button"  onclick="showInput('add_role')">添加 </button>
		<button type="button" onclick = "if(confirm('删除??')) delRole()" >删除 </button>
	</div>
	
	<div>
		<table>
			<tr>
				<th></th>
				<th>角色名</th>
				<th>操作</th>
			
			</tr>

			<c:forEach items="${roles}" var="role">

			<tr >
				

				<td><input type="radio" name="role_name"  value="${role.id }" ></td>

				<td><input type="text"  value="${role.name}"   ></td>
				<td><a href="${pageContext.request.contextPath }/powers/getPowers.do">赋权限</a></td>

			</tr>
			</c:forEach>

		 	<tr style="display:none" id="add_role">
				<td><input type="radio"></td>
				<td><input type="text"  id="role_name"    onblur="if(check())addRole()"></td>
				<td><a href="${pageContext.request.contextPath }/powers/getPowers.do"></a></td>
			</tr> 
			
		</table>
	</div>
</div>


<script type="text/javascript">



function check(){
	var role_name=document.getElementById('role_name');
	if(role_name.value==''){
		alert("不能为空");
		return false
		}
	return  true;
}

function showInput(id){
	var input = document.getElementById(id);
	input.style.display="";
}

function addRole(){

	var role_name =document.getElementById("role_name").value;
	
  	$(function(){
	
		$.ajax({ type:"post",url:"${pageContext.request.contextPath}/powers/addRole.do",data:{"role_name":role_name},async:true,dataType:"text",
			success: function(msg){
				if(msg!="success")alert(msg);
				location.href="${pageContext.request.contextPath}/powers/getRoles.do";
					}
				}
			);
		}
	);

}

function delRole(){
	var role_names=document.getElementsByName("role_name");
	var role_id="";
	for(var i=0;i<role_names.length;i++){
		if(role_names[i].checked){
			role_id=role_names[i].value;
			break;
			}
		}

	$(function(){
		
		$.ajax({ type:"post",url:"${pageContext.request.contextPath}/powers/delRole.do",data:{"role_id":role_id},async:true,dataType:"text",
			success:function(msg) {
				if(msg=="success"){
					location.href="${pageContext.request.contextPath}/powers/getRoles.do";
					}else{
					alert(msg);
					}
				}
			}
		);
	});
}



</script>

</body>
</html>