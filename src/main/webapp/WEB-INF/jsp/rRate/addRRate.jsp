<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="${pageContext.request.contextPath}/js/jquery.min.js" ></script>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商报价记录</title>
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
		<!-- 权限控制 -->
		<c:if test="${pStatus['rrate-import']==true }">
		<input type="submit"   value="上传" />
		</c:if>
	</form>	
	</div>
	
<!-- 权限控制 -->
	<c:if test="${pStatus['rrate-export']==true }">
	<div >
		下载供应商最新报价
	<form action="${pageContext.request.contextPath }/baseRate/exportLastRate.do" method="post" >
		供应商:
			<select   name="vosId" id="export_vosId">
				<c:forEach items="${cusList }" var="cus">
					<option value="${cus.vosId }"  ${vosId==cus.vosId?"selected":null }>${cus.vosId }</option>
				</c:forEach>
			</select>
		<input type="hidden" name="className"  value="com.skyline.pojo.RRate"/>
		<input type="submit"   value="导出"  />
	</form>	
	</div>
	</c:if>
</div>


</body>

<script type="text/javascript">
function downloadRate(){
	var vosId=document.getElementById("export_vosId").value;
	alert(vosId);
	var className="com.skyline.pojo.RRate";
	var url="${pageContext.request.contextPath }/baseRate/exportLastRate.do";
	
	$(function(){
		$.post(url,{"vosId":vosId,"className":className},function(data){
			if(data!=""){
				alert(data);
			}
		});
	});
	
	
}

</script>



</html>