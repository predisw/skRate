<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>                  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js" ></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/powers.css" type="text/css"></link>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>权限</title>
</head>
<body>

<div class="p_body">

	<form  action ="${pageContext.request.contextPath}/powers/savePowers.do"  method ="post">
		<input type ="hidden"  name="role_id"   value="${role.id}" />
		
		<c:forEach items="${powers}" var="pl1">
			<c:if test="${pl1.parentId==0}">
				<div class="level1">
					<input type="checkbox" id="${pl1.id }" value="${pl1.id }"  name ="power_id"  onclick ="clickAllCheckbox('${pl1.id}')"/> ${pl1.name }
				</div>
				<c:forEach items="${powers }" var="pl2" >
					<c:if test="${pl2.parentId==pl1.id }">
						<div class="level2">
							<input type="checkbox" id="${pl2.id }"  value="${pl2.id }"   name ="power_id"  onclick ="if(check('${pl2.id }','${pl2.parentId}'))clickAllCheckbox('${pl2.id}')" />${pl2.name }
						</div>
						
						<c:forEach items="${powers }" var="pl3">
							<c:if test="${pl3.parentId==pl2.id }" >
								<div class="level3">
									<input type="checkbox" id="${pl3.id }"  value="${pl3.id }"   name ="power_id"  onclick="check('${pl3.id }','${pl3.parentId}')"  />${pl3.name }
								</div>
							
							</c:if>
						</c:forEach>

							
						
					</c:if>
				</c:forEach>
			</c:if>
		</c:forEach>
		
		<!-- 权限控制 -->
		<c:if test="${pStatus['powers-add']==true }">
				<input type="submit" value="确定"/> 
		</c:if>

		
	</form>

</div>



<script type="text/javascript">
//将有权限的选中
	<c:forEach items="${role.powerses}" var="power">
			document.getElementById("${power.id}").checked=true;
	</c:forEach>


//勾选所有的checkbox
function clickAllCheckbox(id){
	
		var power=document.getElementById(id);
		var id=power.value;
		var value=power.checked;

		getChildIds(id, value);
		
	
}
//递归获取子权限的id
function getChildIds(id,value){

	var url = "${pageContext.request.contextPath}/powers/getChildPowersId.do";

	$(function(){
		$.post(url, {"id":id}, function(data,status){
			if(status=="success"){ //成功返回,但是可能没有成功获取数据
				var info = JSON.parse(data);
				if(info.Message=="success"){  //成功获取数据
					var ids=info.ids;
					for(var i=0;i<ids.length;i++){
						document.getElementById(ids[i]).checked=value;
						getChildIds(ids[i],value);
					}
					
				}					
			}
		});
	});

	
}

//判断其父权限是否被选
function check(id,parentId){
	var power=document.getElementById(id);
	var parentPower = document.getElementById(parentId);
	if(parentPower.checked==true){
		power.checked=power.checked;
		return true;
	}
	power.checked=false;
	return false;
}



</script>








</body>
</html>
