<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>        
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加员工</title>
</head>
<body>
<div class="main_body">
	<div>
	导入员工<br><br>
		<form id="im_form" action="${pageContext.request.contextPath }/emp/importEmp.do" method=post  enctype="multipart/form-data">
			<input type="file" name="upload" value="选择文件"   required/> 只支持excel 格式     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="submit"  value="上传" />
		</form>
	</div>

	<div>
			添加员工:
		<form method="post" action="<%=request.getContextPath()%>/emp/addEmp.do">
			<span class="input_notice">工号:</span>
			<input type="text" name="ENum" required/><br />
			<span class="input_notice">英文名:</span>
			<input type="text" name="ENameEn"/><br />
			<span class="input_notice">中文名:</span>
			<input type="text" name="ENameCn" required/><br />
			<span class="input_notice">职称:</span>
			<input type="text" name="ETitle"/><br />
			<input type="submit" value="新增">
		</form>

	</div>
	
	
	
</div>

<script type="text/javascript">
if("${Message}"!=null && "${Message}"!=""){ //假如Message 传过来的信息带有 '' 就必须用"",如果是"",这里就用'' 要不upInfo 将会被视为空值
	alert("${Message}");
	location.href="${pageContext.request.contextPath}/emp/toAddEmp.do";
}
</script>

</body>
</html>