<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>                  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
					<input type="checkbox" id="${pl1.id }" value="${pl1.id }"  name ="power_id"  /> ${pl1.name }
				</div>
				<c:forEach items="${powers }" var="pl2" >
					<c:if test="${pl2.parentId==pl1.id }">
						<div class="level2">
							<input type="checkbox" id="${pl2.id }"  value="${pl2.id }"   name ="power_id"  />${pl2.name }
						</div>
					</c:if>
				</c:forEach>
			</c:if>
		</c:forEach>

		<input type="submit" value="确定"/> 
	</form>

</div>



<script type="text/javascript">

	<c:forEach items="${role.powerses}" var="power">
			document.getElementById("${power.id}").checked=true;
	
	</c:forEach>


</script>








</body>
</html>
