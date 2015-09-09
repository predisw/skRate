<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加供应商报价记录</title>
</head>
<body>

<div class="main_body">

	<div >
		导入供应商报价 <!-- 错误提示:-->   <span style="color:red;display:inline-block;margin-left:5%;" id="notice">${Message}</span> 
	<form action="${pageContext.request.contextPath }/rRate/importRRate.do"  method=post  enctype="multipart/form-data"  >
		供应商:
			<select   name="vosId">
				<c:forEach items="${cusList }" var="cus">
					<option value="${cus.vosId }"  ${vosId==cus.vosId?"selected":null }>${cus.vosId }</option>
				</c:forEach>
			</select>
		
		<input type="file" name="upload"  value="choose" required style="display: inline-block;margin-left: 50px;">
		<input type="submit"   value="上传" />
	</form>	
	</div>

	
</div>









</body>
</html>