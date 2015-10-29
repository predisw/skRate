<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.skyline.pojo.Partner" %>    
<%@include file="/index.jsp" %>    
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/my.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户管理</title>
</head>
<body>

<div class="main_body">

	<div>
	导入客户<br><br>
		<form id="im_form" action="${pageContext.request.contextPath }/cus/importCus.do" method=post  enctype="multipart/form-data">
			<input type="file" name="upload" value="选择文件"  required /> 只支持excel 格式     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="submit"  value="上传" />
		</form>
	</div>
	
<!-- 添加客户部分 -->
	<div>
		添加客户<br />
		<form method="post" action="<%=request.getContextPath()%>/cus/addCus.do" >
			<span class="input_notice">客户类型:</span>
			<select name="CType" >
				<option value="${Partner.CUSTOMER}">客户</option>
				<option value="${Partner.PROVIDER}">供应商</option>
			</select><br />
			<span class="input_notice">客户名字:</span>
			<input type="text" name="CName"  class="props_input" required/><br />
			<span class="input_notice">客户邮箱:</span>
			<input type="text" name="email"   onfocus="this.size=(this.value.length>30?this.value.length:30);" size="30"  placeholder="逗号,分开多邮箱"  /><br />
			<span class="input_notice">抄送邮箱:</span>
			<input type="text" name="ccEmail"   onfocus="this.size=(this.value.length>30?this.value.length:30);" size="30"  placeholder="逗号,分开多邮箱.可不填"  /><br />
			<span class="input_notice">暗送邮箱:</span>
			<input type="text" name="bccEmail"   onfocus="this.size=(this.value.length>30?this.value.length:30);" size="30"  placeholder="逗号,分开多邮箱.可不填"  /><span style="float:right;color:red;"> 暗送是指看不到邮箱地址的抄送</span><br />
			<span class="input_notice">VOSId:</span>
			<input type="text" name="vosId" class="props_input" required /> <br />
			<span class="input_notice">业务员工号:</span>
			<select name="ENum"  class="props_input">
				<c:forEach items="${empList }" var="emp"  >
					<option value="${emp.ENum }"  ${cus.ENum==emp.ENum?"selected":null }>${emp.ENum}</option>
				</c:forEach>
			</select><br />
			<input type="submit" value="添加">
		</form>
	</div>
	
	<!--修改客户信息部分  -->
	<div>
		修改客户:
		<!-- 获取客户的vosid -->
		<form method="post" action="${pageContext.request.contextPath}/cus/getCus.do">
			业务:
			<select id="empId" onchange="getCus('empId')" required>
				<option></option>
				<c:forEach items="${empList }" var="emp" >
					<option value="${emp.ENum }" >${emp.ENum}-${emp.ENameCn}</option>
				</c:forEach>
			</select>
			
			客户:
			<select id="cus" name="cus_vosid">
				<c:if test="${vosId!=null }"><option selected value="${vosId }">${vosId }</option></c:if>
				<!-- add in the js function  getCus(id) via ajax-->
			</select>
			
			<input type="submit" value="查询"/>
		</form>
		<!-- 显示客户信息 -->
		<form action="${pageContext.request.contextPath}/cus/update.do" method="post">
			<input type="hidden" name="CId" value="${cus.CId }"/>
			<input type="hidden" name="version" value="${cus.version }"/>

			<span class="input_notice">客户名字:</span>
			<input type="text" name="CName" value="${cus.CName}" class="props_input"/><br />
			<span class="input_notice">客户邮箱:</span>
			<input type="text" name="email"  value="${cus.email}"  onfocus="this.size=(this.value.length>30?this.value.length:30);" size="30" /><span style="float:right;color:red;"> 用,分开多个邮箱</span><br />
			<span class="input_notice">抄送邮箱:</span>
			<input type="text" name="ccEmail"  value="${cus.ccEmail}"  onfocus="this.size=(this.value.length>30?this.value.length:30);" size="30" /><br />
			<span class="input_notice">暗送邮箱:</span>
			<input type="text" name="bccEmail"  value="${cus.bccEmail}"  onfocus="this.size=(this.value.length>30?this.value.length:30);" size="30" /><br />
			<span class="input_notice">VOSId:</span>
			<input type="text" name="vosId" value="${cus.vosId}" class="props_input" required/> <br />
			<span class="input_notice">业务员工号:</span>
			<select name="ENum"  class="props_input">
				<c:if test="${ cus.ENum!=null}">
				<c:forEach items="${empList }" var="emp"  >
					<option value="${emp.ENum }"  ${cus.ENum==emp.ENum?"selected":null }>${emp.ENum}</option>
				</c:forEach>
				</c:if>
			</select><br />
			<span class="input_notice">客户类型:</span>
			<select name="CType" >
				<option ${Partner.CUSTOMER==cus.CType?"selected":null} value="${Partner.CUSTOMER}">客户</option>
				<option ${Partner.PROVIDER==cus.CType?"selected":null} value="${Partner.PROVIDER}">供应商</option>
			</select><br />
			
			<input type="submit" value="修改">
		</form>
	</div>
	
	
</div>

<script type="text/javascript">
if("${Message}"!=null && "${Message}"!=""){ //假如Message 传过来的信息带有 '' 就必须用"",如果是"",这里就用'' 要不upInfo 将会被视为空值
	alert("${Message}");
	location.href="${pageContext.request.contextPath}/cus/toAddCus.do"; 
}


//获取被选业务员的客户列表
function getCus(id){
	var xmlHttp=null;
 	if(window.XMLHttpRequest){
 		xmlHttp=new XMLHttpRequest();
 		}
 	else if (window.ActiveXObject)
 	  {// code for IE5 and IE6
 		  xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
 		  }
 		if (xmlHttp!=null)
 		  {
 	 		 var ele=document.getElementById(id);  //属于业务的select 元素

 	 	 	var url="/SkylineRate/cus/getCusByAjax.do";
 		  	xmlHttp.open("POST",url,false);
 		  	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
 		 	xmlHttp.send("eNum="+ele.value);
 			var cus_str=xmlHttp.responseText;
 			
 			var cus_arr=eval("("+cus_str+")"); // 转换字符串为json 对象
 	//		alert(cus_arr.length);
 			var cus_select=document.getElementById("cus");  //属于 客户的select 元素
 			cus_select.innerHTML="";//在获取某个员工的客户之前,先移除之前的客户
			for(var i=0;i<cus_arr.length;i++){
				var op=document.createElement("option"); //创建一个select 的option 元素
				op.value=cus_arr[i].vosId; //给这个元素添加一个value属性
				op.innerHTML=cus_arr[i].vosId; //option 显示的text 文本值
				cus_select.appendChild(op);  //添加option 元素到select 中
				}
		}
	}

</script>
</body>
</html>