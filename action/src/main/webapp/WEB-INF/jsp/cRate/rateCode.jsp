<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/getRate.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/my.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Rate记录</title>
</head>
<body>
<div class="rate_body">

	<!--  import the history rateRecord function block-->
	<div>
	导入历史记录from 邮件附件<br /><br />
		<form id="im_form"   method=post  enctype="multipart/form-data">
			<input type="hidden" name="im_eNum"  id="im_eNUm" />
			<input type="hidden"  name="im_vosid" id="im_vosid"  />
			<input type="file" name="upload" value="选择文件"   /> 只支持excel 格式     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			线路类型:
			<select name="level">
				<c:forEach items="${lList }" var="level">
					<option value="${level }">${level }</option>
				</c:forEach>
			</select>

			
			<select id="im_type">
				<option value="0">from ISR</option>
				<option  value="1">from 附件</option>
			</select>
			
			<input type="button"  value="上传" onclick="on_submit()"/>
		</form>
	</div>
	


	<!--  选择业务员和客户的功能块-->
	<div>
		<form method="post" action="${pageContext.request.contextPath}/cRate/getRateListOfEmp.do">
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
		
	</div>

	<!-- 对应客户的rate 记录List-->
	<div>
	<!--  form 是用来移除rate 的记录的,table用来显示,这个form 只提交rate 记录的id-->
		<form method="post" action="${pageContext.request.contextPath}/cRate/delCusRate.do">
			<input type="hidden" name="cus_vosid" value="${vosId }"/>
			<div style=" border:none;margin:4px 0px 10px 90%;" ><input type="submit"  value="删除"   onclick="return confirm('确定删除吗')"/> </div> 
			<table>
				<tr>
					<th style="text-align:left;width:20px;"><input  type="checkbox"  id="pCheckbox" onclick="selectCheckbox('pCheckbox','rateId')"/></th>
					<th>vosId</th>
					<th>国家</th>
					<th>运营商</th>
					<th>code</th>

				</tr>
				<c:forEach items="${rateList }" var="rate">
					<tr>
						<td><input  type="checkbox"  name="rateId"  value="${rate.RId}"></td>
						<td>${rate.vosId }</td>
						<td>${rate.country }</td>
						<td>${rate.operator }</td>
						<td>${rate.code }</td>
					</tr>
				</c:forEach>
			</table>
		</form>
	</div>

</div>


<script type="text/javascript">

if('${Message}'!=null && '${Message}'!=""){
	alert('${Message}');
	location.href="${pageContext.request.contextPath}/cRate/getRateList.do";
}


//获取被选业务员的客户列表
function getCus(id){
	var xmlhttp=null;
 	if(window.XMLHttpRequest){
 		xmlhttp=new XMLHttpRequest();
 		}
 	else if (window.ActiveXObject)
 	  {// code for IE5 and IE6
 		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
 		  }
 		if (xmlhttp!=null)
 		  {
 	 		 var ele=document.getElementById(id);  //属于业务的select 元素

 	 	 	var url="${pageContext.request.contextPath}/cus/getTypeCustomersViaAjax.do";
 		  	xmlhttp.open("POST",url,false);
 		  	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
 		  	var data="eNum="+ele.value+"&type=CUSTOMER";
 		 	xmlhttp.send(data);
 			var cus_str=xmlhttp.responseText;
 			
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

//上传部分使用:
// 上传button 点击时,会获取select 被选元素的值,赋给上传form中对应的隐藏input 元素
function on_submit(){
	var eNum=document.getElementById("empId");
	var vosid=document.getElementById("cus");
	var im_eNum=document.getElementById("im_eNUm");
	var im_vosid=document.getElementById("im_vosid");
	im_eNum.value=eNum.options[eNum.selectedIndex].value;
	im_vosid.value=vosid.options[vosid.selectedIndex].value;

	var im_type= document.getElementById('im_type');

	var form = document.getElementById('im_form');

	if(im_type.value=="0") form.action="${pageContext.request.contextPath}/cRate/importRateFromISR.do";
	if(im_type.value=="1")form.action="${pageContext.request.contextPath}/cRate/importRate.do";


	form.submit();
	
}



</script>


</body>
</html>