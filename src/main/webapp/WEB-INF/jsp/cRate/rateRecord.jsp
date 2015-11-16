<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/rateRecord.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/my.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报价记录</title>
</head>
<body>

<div class="rr_body">
	
	<!--  选择业务员和客户的功能块-->
	<div class="search">
		<form method="post" action="${pageContext.request.contextPath}/cRate/getSuccessRate.do">
			客户:
			<select   name="vosId">
				<c:forEach items="${cusList }" var="cus">
					<option value="${cus.vosId }"  ${vosId==cus.vosId?"selected":null }>${cus.vosId }</option>
				</c:forEach>
			</select>

			地区:
			<select name="country" style="width:15%;overflow: visible;">
				<option value="all">ALL</option>
				<c:forEach items="${ccList }" var="cc">
					<option value="${cc.country }" ${country==cc.country?"selected":null }>${cc.country}--${cc.CCode }</option>
				</c:forEach>
			
			</select>
			from:
			<input type="date" name="sDate"  id="sDate"  value="${sDate}" />
			to:
			<input type="date" name="tDate"  id="tDate"  value="${tDate }"/>
			
 			<span style="float:right;color:red">country/page</span>
			<input type="text"  name="pageSize"  value="${page.pageSize }"   style="width:25px;float:right;color:red"/>
			
			
			<input type="submit" value="查询"/>

		</form>
		
	</div>

	
<div style="display: none;"  id="page_div"  >
		    <form action="${pageContext.request.contextPath}/cRate/getSuccessRate.do" method="post" name="frmData"  style="text-align: center">

											共<b><font color="red">${page.totalPage}</font> </b>页 - 这是第
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
											
			
											<input type="hidden"  name="vosId"  value="${vosId }"/>	
											<input type="hidden"  name="country" value="${country }"/>	
											<input type="date"  name="sDate" value="${sDate }"  style="display: none"/>
											<input type="date" name="tDate" value="${tDate }" style="display: none"/>
											<input type="button" value="toBottom" onclick="toBottom()">
											</form>
						<script type="text/javascript" src="${pageContext.request.contextPath}/js/list.js"></script>
  
	</div>


	<!-- 对应客户的rate 记录List-->
	<div class="content">
		<c:forEach items="${cList }" var="c" > <!--根据国家获取对应rate的记录  -->
			<div class="c_head">${c }</div>
			<table>
				<tr>
					<th  style="width:12%;">vosId</th>
					<th >operator</th>
					<th style="width:11%;">code</th>
					<th style="width:10%;">rate</th>
					<th style="width:6%;">b_unit</th>
					<th style="width:10%;">level</th>
					<th style="width:10%;">effective_date</th>
					<th style="width:10%;">expire_date</th>
					<th  style="width:10%;">备注</th>
				</tr>
		
					<c:forEach items="${page.data }" var="r">
						<c:if test="${c==r.country }">
							<tr>
								<td>${r.bakVosId }</td>
								<td>${r.operator } </td>
								<td>${r.code } </td>
								<td>${r.doubleF(r.rate) } </td>
								<td style="width:5px;">${r.billingType } </td>
								<td>${r.level } </td>
								<td>${r.effectiveTime } </td>
								<td>${r.expireTime } </td>

								<td>${r.remark }</td>
							</tr> 
 						</c:if>
				</c:forEach>
				</table>
		</c:forEach> 
	</div>
			<!--  分页代码-->
	<div >
		    <form action="${pageContext.request.contextPath}/cRate/getSuccessRate.do" method="post"  style="text-align: center">

											共<b><font color="red">${page.totalPage}</font> </b>页 - 这是第
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
											
			
											<input type="hidden"  name="vosId"  value="${vosId }"/>	
											<input type="hidden"  name="country" value="${country }"/>	
											<input type="date"  name="sDate" value="${sDate }"  style="display: none"/>
											<input type="date" name="tDate" value="${tDate }" style="display: none"/>
											<input type="button" value="toTop" onclick="toTop()">
											</form>

  
	</div>
		
</div>


<script type="text/javascript">

//避免提交的值被默认值覆盖
/* if("${sDate}"==null ||"${sDate}"=="" ){
	curDate("sDate");
}
 */
if("${tDate}"=="" ){
	curDate("tDate");
}

if('${Message}'!=null && '${Message}'!=""){
	alert('${Message}');
	location.href="${pageContext.request.contextPath}/cRate/getRateRecord.do";
}

onload=function isScrolled(){
		if(isScrolledBar()){
			var page_div =  document.getElementById('page_div');
			page_div.style.display="";
		}
}

</script>


</body>
</html>