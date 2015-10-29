<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/index.jsp" %>        
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>excel 附件模板</title>
</head>
<body>
<div class="main_body">
	<div>
	导入模板<br><br>
		<form id="im_form" action="${pageContext.request.contextPath }/excelTp/importTp.do" method=post  enctype="multipart/form-data">
			<input type="file" name="upload" value="选择文件"  required /> 只支持excel 格式     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="submit"  value="上传" />
		</form>
	</div>
	
	<div>
		所有模板:
		<table>
			<c:forEach items="${tpList }" var="tp">
				<tr>
					<td>${tp.name }</td>
					<td><a href="${pageContext.request.contextPath }/excelTp/downloadTp.do?id=${tp.id}">下载</a></td>
				</tr>
			
			</c:forEach>
		
		</table>
	</div>

</div>


</body>
<script type="text/javascript">
if("${Message}"!=null && "${Message}"!=""){ //假如Message 传过来的信息带有 '' 就必须用"",如果是"",这里就用'' 要不upInfo 将会被视为空值
	alert("${Message}");
	location.href="${pageContext.request.contextPath}/excelTp/getExcelTp.do";
}
</script>

</html>