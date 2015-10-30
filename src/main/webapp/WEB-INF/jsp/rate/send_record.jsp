<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>            
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/send_record.css"  rel="stylesheet" type="text/css" />
<title>发送记录</title>
</head>
<body>

<div class="record_body" >
<!--  数据-->
	<div>
		发送记录    <!-- 错误提示:-->   <span style="color:red;display:inline-block;margin-left:5%;">${Message}</span> 
		<table>
		<tr>
			<th>ID</th>
			<th>处理人</th>
			<th>邮件主题</th>
			<th>发送时间</th>
			<th>客户vosId</th>
			<th>附件模板</th>
			<th>发送</th>
			<th>重发</th>
			<th>有效</th>
			<th>操作</th>
		</tr>
		<c:forEach items="${page.data }" var="sr">
			<tr>
				<td>${sr.id }</td>
				<td>${sr.UName }</td>
				<c:forEach items="${emailList }" var="email">
					<c:if test="${email.name==sr.emailName }">
						<td>${email.subject }</td>
					</c:if>
				</c:forEach>
				<td>${sr.sendTime }</td>
				<td>${sr.vosId}</td>
				<td>${sr.excelTp }</td>
				
				<c:choose>
					<c:when test="${sr.sendStatus==1}"><td>成功</td></c:when>
					<c:when test="${sr.sendStatus==-1}"><td>失败</td></c:when>
					<c:when test="${sr.sendStatus==0}"><td>发送中</td></c:when>
					<c:otherwise><td>${sr.sendStatus}</td></c:otherwise>
				</c:choose>
				
								
				<c:choose>
					<c:when test="${sr.reSendStatus==true}"><td>成功</td></c:when>
					<c:when test="${sr.reSendStatus==false}"><td>失败</td></c:when>
					<c:otherwise><td>${sr.reSendStatus}</td></c:otherwise>
				</c:choose>
			
				<c:choose>
					<c:when test="${sr.isCorrect==false}"><td>失效</td></c:when>
					<c:otherwise><td>${sr.isCorrect}</td></c:otherwise>
				</c:choose>
				
				<td><input type="button" value="重发" onclick=" if(confirm('确定要重发吗?')) 
				location.href='${pageContext.request.contextPath}/sendMail/resendEmail.do?id=${sr.id }' ">
				<input type="button" value="错误"  onclick=" if(confirm('同一个客户之后已存在的报价都会被设置为不正确的.')) 
				location.href='${pageContext.request.contextPath}/sendRecord/setRateRecordIncorrect.do?id=${sr.id }' " />
				</td>
			</tr>
		</c:forEach>
		</table> 
		
	</div>
	
<!--  分页代码-->
	<div>
		    <form action="${pageContext.request.contextPath}/sendRecord/getSendRecords.do" method="post" name="frmData">
			<b>	<font color="red">${page.totalCount } </font> </b>条记录- 分
											<b><font color="red">${page.totalPage}</font> </b>页 - 这是第
											<b><font color="red">${page.curPage} </font> </b>页&nbsp; | &nbsp;
											<a onClick="first()" href="#">首页</a>&nbsp;
											<a onClick="prev()" href="#">前一页</a>&nbsp;
											<a onClick="next()" href="#">后一页</a>&nbsp;
											<a onClick="last()" href="#">末页</a>&nbsp; | &nbsp; 到第
											<input id="page.curPage" name="curPage" type="text"
												size="4" maxlength="4" style='width: 30px; height: 18px;'
												value="${page.curPage}"
												onKeyUp="onlyNum();">
											<input type="submit" value="跳转" class=mmBtn />
											<input type="hidden" name="pageSize"
												value="${page.pageSize }" />
											<input type="hidden" name="totalPage"
												value="${page.totalPage }" />
												</form>
						<script type="text/javascript" src="${pageContext.request.contextPath}/js/list.js"></script>
  
	</div>


</div>


</body>
</html>