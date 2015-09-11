<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/index.jsp" %>              
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作日志</title>
</head>
<body>
<div class="main_body">
	<div >
		<form action="" method="post" style="text-align: center;">
			用户:<input type="text" name="user"  value="${cons['userName'] }"/> 
			from:<input type="date"  name="from"  value="${cons['fDate'] }" />
			to:<input type="date" name="to"  value="${cons['tDate'] }"/>
			<input type="submit" value="搜索"/>	
		</form>
	</div>

	<div>
		<table>
			<tr>
				<th>用户</th>
				<th>操作</th>
				<th>类型</th>
				<th>内容</th>
				<th>时间</th>
			</tr>
			<c:forEach items="${page.data }" var="log">
				<tr>
					<td>${log.who }</td>
					<td>${log.how }</td>
					<td>${log.what }</td>
					<td>${log.content }</td>
					<td>${log.time }</td>
				</tr>
			</c:forEach>
		</table>
	</div>


	<div>
		 <form action="${pageContext.request.contextPath}/log/getPageLog.do" method="post" name="frmData" style="text-align: center">
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
											<input type="hidden" name="user"  value="${cons['userName'] }"/> 
											<input type="hidden" name="from"  value="${cons['fDate'] }"/> 
											<input type="hidden" name="to"  value="${cons['tDate'] }"/> 
												</form>
						<script type="text/javascript" src="${pageContext.request.contextPath}/js/list.js"></script>
  
	
	</div>




</div>


</body>
</html>