<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="${pageContext.request.contextPath}/skyline.ico" type="image/x-icon" rel="shortcut icon">
<link href="${pageContext.request.contextPath}/css/menu.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>
<div  class="menu_body">

<div style="position:absolute;right:10%;">
	version <%=request.getServletContext().getInitParameter("version") %>
</div>
<div class="top_notice">
	${user.UName},Welcome here! Have you smiled today !
</div>

<div class="menu_part">
	<div>
	<ul   class="menu">
		<c:forEach items="${menu }"  var="mTop" >
 			<c:if test="${mTop.parentId==0}"> 
				<li><a href="${pageContext.request.contextPath}">${mTop.name }</a>
 					<ul>
					<c:forEach items="${menu }" var="m2">
						<c:if test="${m2.parentId==mTop.id}">
							<li><a href="${pageContext.request.contextPath}${m2.url}.do">${m2.name }</a>

						</c:if>	
					</c:forEach>
					</ul> 
				</li>
			</c:if>

		</c:forEach>
		<li><a href="${pageContext.request.contextPath}/user/logout.do" onclick="return confirm('确定退出?');">Logout</a></li>
	</ul>
	</div>

		<%-- <ul class="menu">
			<li><a href="${pageContext.request.contextPath}/getEmps.do">邮件报价</a>
				<ul>
					<li><a href="${pageContext.request.contextPath}/getEmps.do">发送报价</a></li>
					<li><a href="${pageContext.request.contextPath}/sendRecord/getSendRecords.do">发送记录</a></li>
				</ul>
			</li>
			
			<li><a href="#">客户Rate</a>
				<ul>
					<li><a href="${pageContext.request.contextPath}/cRate/getRateRecord.do">历史报价</a></li>
					<li><a href="${pageContext.request.contextPath}/cRate/getRateList.do">报价Code</a></li>
				</ul>
			</li>
			
			<li><a href="#">供应商Rate</a>
				<ul>
					<li><a href="${pageContext.request.contextPath}/rRate/toRRateRecord.do">历史报价</a></li>
					<li><a href="${pageContext.request.contextPath}/rRate/toAddRRate.do">添加报价</a></li>
				</ul>
			</li>
			
			<li><a href="${pageContext.request.contextPath}/cus/toAddCus.do">客户管理</a></li>
			<li><a href="${pageContext.request.contextPath}/user/toUser.do">用户管理</a></li>
			
			<li><a href="#">员工管理</a>
				<ul>
					<li><a href="${pageContext.request.contextPath}/emp/toAddEmp.do">添加员工</a>
					<li><a href="${pageContext.request.contextPath}/emp/toUpdateEmp.do">修改员工</a>
				</ul>
			</li>
			<li><a href="#">Others</a>
				<ul>
					<li><a href="${pageContext.request.contextPath}/cc/getCountrys.do">地区详情</a></li>
					<li><a href="${pageContext.request.contextPath}/props/toAddProp.do">添加属性</a></li>
					<li><a href="${pageContext.request.contextPath}/excelTp/getExcelTp.do">excel 模板</a></li>
				</ul>
			</li>
			<li><a href="#">日志</a>
				<ul>
					<li><a href="${pageContext.request.contextPath}/log/getPageLog.do">操作日志</a></li>
					<li><a href="${pageContext.request.contextPath}/log/getSysLog.do">系统日志</a></li>
					
				</ul>
			</li>
			<li><a href="#">权限</a>
				<ul>
					<li><a href="${pageContext.request.contextPath}/powers/toUserRole.do">用户权限</a></li>
					<li><a href="${pageContext.request.contextPath}/powers/getRoles.do">配置权限</a></li>
					
				</ul>
			</li>
			<li><a href="#">系统</a>
				<ul>
					<li><a href="${pageContext.request.contextPath}/sys/toPerformance.do">系统性能</a></li>
					<li><a href="${pageContext.request.contextPath}/sys/getVerInfo.do">About Us</a></li>
					<li><a href="${pageContext.request.contextPath}/sys/toSysOperation.do">系统操作</a></li>
					<li><a href="${pageContext.request.contextPath}/sys/getOnliner.do">在线人数</a></li>
				</ul>
			</li>
			<li><a href="${pageContext.request.contextPath}/user/logout.do" onclick="return confirm('确定退出?');">Logout</a></li>
		</ul>
 --%>
	</div>
</div>	
</body>
</html>
