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
			<input type="button" style="position: absolute;right: 10%;" value="修改"  onclick="if(confirm('确定修改?'))submit_rRates()"/>
		</form>

	</div>

	<!-- 对应供应商的rate 记录List-->
	<div class="content">
		<c:forEach items="${cList }" var="c" >  <!--根据国家获取对应rate的记录  -->
			<div class="c_head">${c }</div>
			<table>
				<tr>
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
							<input type="hidden" name="id"  value="${r.id}"/>
							<tr >
								<td>${r.bakVosId }</td>
								<td>${r.operator } </td>
								<td>${r.code } </td>
								<td name="rate"><input type="text"  name="${r.country}rate"  style="width:90%;" value="${r.doubleF(r.rate) }"/> </td>
								
								<td style="width:8px;" name="billing_unit"> 
									<select name="${r.country}billing_unit"  style="width:100%;">
										<c:forEach items="${bList }" var="bUnit" >
										<option ${r.billingType==bUnit.value?"selected":null} >${bUnit.value}</option>
										</c:forEach>
									</select>
								 </td>
								 
								<td name="effect_time"><input type="date"  name="${r.country}effect_time"  style="width:90%;" value="${r.effectiveTime }" /> </td>
								
								<td name="expire_time"><input type="date"  name="${r.country}expire_time" style="width:90%;"  value="${r.expireTime }"/> </td>

								<td name="remark"><input type="text"  name="${r.country}remark"  value="${r.remark }" style="width:95%;" /></td>
							</tr> 
 						</c:if>
				</c:forEach>
			</table>

		</c:forEach> 
	</div>
			<!--  分页代码-->
	<div >
		    <form action="${pageContext.request.contextPath}/rRate/getRRateRecord.do" method="post" name="frmData"  style="text-align: center">

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

function all_change(id,name){
	var id_ele= document.getElementById(id);
	var name_eles=document.getElementsByName(name);

	for(var i=0;i<name_eles.length;i++){
			name_eles[i].value=id_ele.value;

		}
	
}


function submit_rRates(){
	var ids=document.getElementsByName("id");
	var rates=document.getElementsByName("rate");
	var bUnits=document.getElementsByName("billing_unit");
	var effect_times=document.getElementsByName("effect_time");
	var expire_times=document.getElementsByName("expire_time");
	var remarks=document.getElementsByName("remark");


	var rRates="[";
	
	for(var i=0;i<ids.length;i++){

		rRates+="{\"id\":\""+ids[i].value+"\",\"rate\":\""+rates[i].firstChild.value+"\",\"billing_unit\":\""+bUnits[i].children[0].value+"\",\"effect_time\":\""+effect_times[i].firstChild.value+"\",\"expire_time\":\""+expire_times[i].firstChild.value+"\",\"remark\":\""+remarks[i].firstChild.value+"\"}";
	//	rRates+='{"id":'+ids[i].value+',"rate":'+rates[i].firstChild.value+'}';
	//	rRates+={"id":ids[i].value,"rate":rates[i].firstChild.value,"billing_unit":bUnits[i].children[0].value,"effect_time":effect_times[i].firstChild.value,"effect_times":expire_times[i].firstChild.value,"remark":remarks[i].firstChild.value};
		if(i+1<ids.length)rRates+=",";
		}
	rRates+="]";
	
  	$(function(){
		$.post("${pageContext.request.contextPath}/rRate/modifyrRate.do",{"rRates":rRates},
				 function(data,status){
					if(status=="success"){
						alert(JSON.parse(data).Message);
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








 


</script>


</body>
</html>