<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html charset=UTF-8">
<title>国家code参照</title>
</head>
<body>

<div class="main_body">

<div >

	<form action="<%=request.getContextPath()%>/cc/importCountry.do"  method=post  enctype="multipart/form-data">
上传文件: 
		<input type="file"  name="upload" value="选择文件"  required/> 只支持excel 格式     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		
		<!-- 权限控制 -->
		<c:if test="${pStatus['cc-import']==true }">
			<input type=submit value="上传"/>     要判断excel 中 destination 之类是否存在
		 </c:if>
		 
	</form>
</div>

<div >
添加单个国家 code 参照
<form method="post" action="<%=request.getContextPath()%>/cc/saveCountryCode.do">
国家名:
<input type="text" name="country" required/>
国家代码:
<input type="text" name="CCode"  class="num_input"/>
运营商:
<input type="text" name="operator"/>
code:
<input type="text"  name="code" class="num_input" required/>
		<!-- 权限控制 -->
	<c:if test="${pStatus['cc-add']==true }">
	<input type="submit"  value="添加"/>
	</c:if>
</form>


搜索:
<form method="post" action="<%=request.getContextPath()%>/cc/getCountrys.do">
国家名:
<input type="text" name="scountry" value="${scountry}"/>
或者
code:
<input type="text"  name="scode" value="${scode}"/>
		<!-- 权限控制 -->
	<c:if test="${pStatus['cc-search']==true }">
		<input type="submit"  value="搜索"/>
	</c:if>
</form>


</div>





<!-- 以下是数据list -->
<div>
<span style="display:inline-block;margin-right: 30px;">已添加的国家code 对照表</span>
	<!-- 权限控制 -->
	<c:if test="${pStatus['cc-export']==true }">
		<input type="button" value="导出"  onclick="exportAllCC()" />
	</c:if> 
<!-- 更新错误提示:-->   <span style="color:red;display:inline-block;margin-left:5%;">${Message}</span> 
<table  style="margin-left:0px;">
<tr>
<th style="width:20px;"></th>
<th>国家名</th>
<th  class="num_th">国家代码</th>
<th>运营商</th>
<th  class="num_th">code</th>
</tr>

<input  type="checkbox"  id="countrys" style="display:none;" value=""  ><!--这是第一个，永远没效。保证是一个数组 -->
<c:forEach items="${ccList}" var="c">

<form action="<%=request.getContextPath()%>/cc/updateCountryCode.do"  method=post  id="${c.ccId}"  >

<tr>
<td><input type="checkbox" id="countrys"   value="${c.ccId}" /> </td>

<input  type="hidden" name="ccId" value="${c.ccId }" />
<input  type="hidden" name="version" value="${c.version }" />
<td><input type="text"    name="country"   value="${c.country }"    disabled    /></td> 
<td><input type="text"  class="num_input"  name="CCode"   value="${c.CCode }"    disabled    /></td> 
<td><input  type = text  name="operator"  value="${c.getOperator() }"  disabled  /> </td>
<td><input type=text  name="code"  value="${c.code}"   disabled  /></td>
<td>
<!-- 隐藏信息-->
<input type="hidden" name="scountry"  value="${scountry}"/>
<input type="hidden"  name="scode"  value="${scode}"/>
<input type="hidden" name="curP" value="${page.curPage}"/>

<input type=button value="激活"  onclick =" return editorEnable( ${c.ccId})" />
		<!-- 权限控制 -->
	<c:if test="${pStatus['cc-update']==true }">
		<input type="submit"  value="修改"  onClick="return checkUpdate(${c.ccId})"/>
	</c:if>
</td>

</tr>


</form>
</c:forEach>

 
 <!-- 批量删除 -->

<tr>
<td><input type='checkbox' value='click' onclick='selectall(this);'/></td>
<td>当页全选/取消</td>
</tr>
<form id="del_country_form"  action="${pageContext.request.contextPath}/cc/delCountryCode.do" method="post">
<!-- 隐藏信息-->
<input type="hidden" name="scountry"  value="${scountry}"/>
<input type="hidden"  name="scode"  value="${scode}"/>
<input type="hidden" name="curP" value="${page.curPage}"/>
<input type="hidden"  name="ids" id="del_ids" />

		<!-- 权限控制 -->
<c:if test="${pStatus['cc-del']==true }">
<tr>
<td><input type=button  value="删除"' onclick="  selectCheck('del_ids')"/></td>
</tr>
</c:if>
</form>
 
</table>

</div>

<!-- 分页 -->
<div style="border: none;">

           <form action="${pageContext.request.contextPath}/cc/getCountrys.do" method="post" name="frmData" >
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
												<input type="hidden" name="scountry" value="${scountry}"/>
												<input type="hidden" name="scode" value="${scode}"/>
												</form>
						<script type="text/javascript" src="${pageContext.request.contextPath}/js/list.js"></script>
 
</div>

</div>


<script>

function editorEnable(d){
//	alert(d);
 var x=document.getElementById(d);
 // var x=document.getElementsByTagName("input");
	// alert(x);
	for (var i=0;i<x.length;i++)
  {
//		 x.disabled=false;
	//  x.removeAttribute("disabled");  //把select 的disable 属性去掉
	 	 x.elements[i].removeAttribute("disabled");  //把select 的disable 属性去掉
	 } 
}
//全部选中
function selectall(tp)
{
var obj=document.all.countrys;

for(var i=0;i<obj.length;i++)
{
obj[i].checked=tp.checked;
}

}
//获取选中的id
function selectCheck(id){
	
	if(confirm('确定要删除吗？')){
		var check=document.all.countrys;
//		alert(check);
		var strId=",";
		if(check.length>1) {
			for(var i=0;i<check.length;i++){
				if(check[i].checked){
						strId=strId+check[i].value+",";
//					alert(strId);
					}
				}
			}
		document.getElementById(id).value=strId;
		document.getElementById("del_country_form").submit();
	}


}

function	checkUpdate(id){
	 var x=document.getElementById(id);
	 for (var i=0;i<x.length;i++)
	  {
		 	if( x.elements[i].getAttribute("disabled")!=null){
				alert("请先激活,才可以修改!");
				return false;
			 	}
		 } 
	 if(!confirm("确定修改?")){
		return false;
		 }
	 return true;
}


function exportAllCC(){
	
	location="${pageContext.request.contextPath}/cc/exportAllCC.do";
}


</script>

</body>
</html>