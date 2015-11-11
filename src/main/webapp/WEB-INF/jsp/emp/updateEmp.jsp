<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/modifyEmp.css"  rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>业务员的修改与删除</title>
</head>
<body>

<div class="mc_body">

	<!--获取业务列表  -->
	<div class="leftfloat">
		业务员列表:
		<form method="post"  action="<%=request.getContextPath()%>/emp/getEmpInfo.do"  class="formM">
			<select multiple="multiple"  class="selectM"  name="eids" required>
		  		<c:forEach items="${empList }" var="emp">
		  			<option   value ="${emp.EId}">${emp.ENum}-${emp.ENameCn}</option>
				</c:forEach>
		  	</select>
			<input type="submit"  id="checkCus" value="查询"  >
		</form>
	</div>
	<!-- 显示业务员信息细节 -->
	
	<div class="modify_part">
	<span style="color:red;">多选时,只会修改第一个业务员的信息</span>
	<form action="${pageContext.request.contextPath}/emp/updateCheck.do" method="post">
		<input type="hidden" name="EId" value="${emp.EId}"/>
		<input type="hidden" name="version" value="${emp.version}"/>
		<span class="input_notice">工号:</span><input type="text"  name="ENum" value="${emp.ENum }" required readOnly/> <br />
		<span class="input_notice">英文名:</span><input type="text"  name="ENameEn" value="${emp.ENameEn }" /> <br />
		<span class="input_notice">中文名:</span><input  type="text" name="ENameCn" value="${emp.ENameCn }" /> <br />
		<span class="input_notice">职称:</span><input type="text" name="ETitle"  value="${emp.ETitle }" /> <br />
		<span class="input_notice">状态:</span>在职:<input type="radio"  name="EStatus"  ${emp.EStatus==true?"checked":null }  value="true" /> 
		离职:<input type="radio"  name="EStatus"  ${emp.EStatus!=true?"checked":null }  value="false" />   
		<span style="display:inline-block;margin-left:50px;color:red;">设置为离职,则发送报价处不可选</span> <br />
		
		<!-- 权限控制 -->
		<c:if test="${pStatus['emp-update']==true }">
				<input type="submit" value="修改">
		</c:if>

	</form>
	
	</div>
	
</div>

<script type="text/javascript">
if("${Message}"!=null && "${Message}"!=""){ //假如Message 传过来的信息带有 '' 就必须用"",如果是"",这里就用'' 要不upInfo 将会被视为空值
	alert("${Message}");

}
</script>
</body>
</html>