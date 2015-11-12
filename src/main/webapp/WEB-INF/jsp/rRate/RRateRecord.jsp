<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/rateRecord.css"  rel="stylesheet" type="text/css" />
<script src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/my.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<title>报价记录</title>
</head>
<body>

<div class="rr_body">
	
	<!--  选择业务员和客户的功能块-->
	<div class="search">
		<form method="post" action="${pageContext.request.contextPath}/rRate/getRRateRecord.do">
			供应商:
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
			<input type="date" name="sDate"  id="sDate"  value="${sDate}"  style="width:120px;"/>
			to:
			<input type="date" name="tDate"  id="tDate"  value="${tDate }" style="width:120px;"/>

 			<span style="float:right;color:red">country/page</span>
			<input type="text"  name="pageSize"  value="${page.pageSize }"   style="width:25px;float:right;color:red"/>
			
			
			<input type="submit" value="查询"/>
			<input type="submit" value="ALL"  onclick="setDateNull()"/>
			
			<!-- 权限控制 -->
			<c:if test="${pStatus['rrate-modify']==true }">
				<input type="button" style="position: absolute;right: 10%;" value="修改"  onclick="if(check())if(confirm('确定修改?'))submit_rRates()"/>
			</c:if>
						<!-- 权限控制 -->
			<c:if test="${pStatus['rrate-del']==true }">
				<input type="button" style="position: absolute;left: 10%;" value="删除"  onclick="if(check())if(confirm('确定删除?删除!'))submit_del()"/>
			</c:if>
		</form>

	</div>

	<!-- 对应供应商的rate 记录List-->
	<div class="content">
		<c:forEach items="${cList }" var="c" >  <!--根据国家获取对应rate的记录  -->
			<div class="c_head">${c }</div>
			<table>
				<tr>
					<th style="width:5px;"></th>
					<th  style="width:12%;">vosId</th>
					<th >operator</th>
					<th style="width:11%;">code</th>
					<th style="width:10%;">rate</th>
					<th style="width:8%;">b_unit</th>
					<th style="width:10%;">effective_date</th>
					<th style="width:10%;">expire_date</th>
					<th  style="width:10%;">备注</th>
				</tr>
				<!-- 统一修改行 -->
				<tr>
					<td><input type="checkbox" id="${c}id"     onChange="select_checkbox('${c}id','${c }id')"  /></td>
					<td ></td>
					<td></td>
					<td></td>
					<td ><input type="text"  id="${c}rate"  style="width:90%;background-color: white"  onChange="all_change('${c}rate','${c}rate')" /></td>
					<td>
						<select id="${c}billing_unit"  style="width:100%;background-color: white" onChange="all_change('${c}billing_unit','${c}billing_unit')"  >
							<c:forEach items="${bList }" var="bUnit">
								<option>${bUnit.value }</option>
							</c:forEach>
						</select>
				   </td>
					<td style="width:10%;"><input type="date" id="${c}effect_time"  style="width:90%;background-color: white" onChange="all_change('${c}effect_time','${c}effect_time')"  /></td>
					<td style="width:10%;"><input type="date" id="${c}expire_time" style="width:90%;background-color: white" onChange="all_change('${c}expire_time','${c}expire_time')"  /></td>
					<td style="width:10%;"><input type="text"  id="${c}remark" style="width:95%;background-color: white" onChange="all_change('${c}remark','${c}remark')"  /></td>
				</tr> 
		
				
					<c:forEach items="${page.data }" var="r" varStatus="index">
						<c:if test="${c==r.country }">
						
							<tr >
								<td  name="id" ><input type="checkbox"  name="${r.country}id"  value="${r.id}"/></td>
								<td>${r.bakVosId }</td>
								<td>${r.operator } </td>
								<td>${r.code } </td>
								<td ><input type="text"  name="${r.country}rate"   id="${r.id}rate"  style="width:90%;" value="<c:if test='${r.rate!=NULL}'> ${r.doubleF(r.rate) }</c:if>"/> </td>
								
								<td style="width:8px;"> 
									<select name="${r.country}billing_unit"  style="width:100%;" id="${r.id}billing_unit">
										<c:forEach items="${bList }" var="bUnit" >
										<option ${r.billingType==bUnit.value?"selected":null} >${bUnit.value}</option>
										</c:forEach>
									</select>
								 </td>
								 
								<td><input type="date"  name="${r.country}effect_time"  style="width:90%;" value="${r.effectiveTime }"  id="${r.id}effect_time" /> </td>
								
								<td><input type="date"  name="${r.country}expire_time" style="width:90%;"  value="${r.expireTime }"  id="${r.id}expire_time" /> </td>

								<td ><input type="text"  name="${r.country}remark"  value="${r.remark }" style="width:95%;" id="${r.id }remark" /></td>
							</tr> 
 						</c:if>
				</c:forEach>
			</table>

		</c:forEach> 
	</div>
			<!--  分页代码-->
	<div >
		    <form action="${pageContext.request.contextPath}/rRate/getRRateRecord.do" method="post" name="frmData"  style="text-align: center" id="pageForm">

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
												</form>
						<script type="text/javascript" src="${pageContext.request.contextPath}/js/list.js"></script>
  
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
	}

function select_checkbox(id,name){
	var check=document.getElementById(id);
	var checks = document.getElementsByName(name);
	for(var j=0;j<checks.length;j++){
			checks[j].checked=check.checked;
		}
}
 
function all_change(id,name){
	var id_ele= document.getElementById(id);
	var name_eles=document.getElementsByName(name);

	for(var i=0;i<name_eles.length;i++){
			name_eles[i].value=id_ele.value;

		}
	
}

function check(){

	var checktd=document.getElementsByName('id');
	for(var i=0;i<checktd.length;i++){
		if(checktd[i].firstChild.checked){
			return true;
		}
	}
	alert("请先选择需要修改的行!!");
	return false;
}

function submit_rRates(){

	var checktd=document.getElementsByName('id');

	
 	var rRates="[";

	for(var i=0;i<checktd.length;i++){

		if(checktd[i].firstChild.checked){
			if(rRates.length>1)rRates+=",";
			
			var id = checktd[i].firstChild.value;
			var rate = document.getElementById(id+'rate');
			var billing_unit = document.getElementById(id+'billing_unit');
			var effect_time = document.getElementById(id+'effect_time');
			var expire_time = document.getElementById(id+'expire_time');
			var remark = document.getElementById(id+'remark');
			rRates+="{id:\""+id+"\",rate:\""+rate.value+"\",billing_unit:\""+billing_unit.value+"\",effect_time:\""+effect_time.value+"\",expire_time:\""+expire_time.value+"\",remark:\""+remark.value+"\"}";
/* 			if(i+1<checktd.length && checktd[i+1].firstChild.checked){
				rRates+=",";
				} */
			}

 	}

	rRates+="]";

	
  	$(function(){
		$.post("${pageContext.request.contextPath}/rRate/modifyrRate.do",{"rRates":rRates},
				 function(data,status){
					if(status=="success"){
						alert(JSON.parse(data).Message);
						document.getElementById('pageForm').submit();
						}
				} 
			);
		}
	); 
			
			//下面是个人用原生ajax 写的方法
 /* 	var xmlHttp=loadAjax("post","${pageContext.request.contextPath}/rRate/modifyrRate.do","rRates="+rRates ,function(){
		if(xmlHttp.readyState==4 && xmlHttp.status==200){
			var text=JSON.parse(xmlHttp.responseText).Message;
			alert(text);
			}
		}
	); */
 	
}


function submit_del(){
	var checktd=document.getElementsByName('id');
	//找出被选的checkbox
	var ids="";  //不是json 数组
	for(var i=0;i<checktd.length;i++){
		if(checktd[i].firstChild.checked){
			if(ids.length>1)ids+=",";  //字符串的长度
			var id = checktd[i].firstChild.value;
			ids+=id;
		}
	}

	//alert(ids);


  	$(function(){
		$.post("${pageContext.request.contextPath}/rRate/delrRates.do",{"ids":ids},
				 function(data,status){
					if(status=="success"){
						alert(data);
						document.getElementById('pageForm').submit();
						}
				} 
			);
		}
	); 
}



function setDateNull(){
	var sDate = document.getElementById('sDate');
	var tDate =document.getElementById('tDate');
	sDate.value="";
	tDate.value="";
}



 


</script>


</body>
</html>