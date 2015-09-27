<%@ page import="java.util.List"  import="com.skyline.pojo.Customer" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/index.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/progress.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/addRate.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ckeditor/ckeditor.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送报价</title>
</head>
<body>
 <!-- 更新错误提示:-->   <span style="color:red;display:inline-block;margin-left:5%;" id="notice">${Message}</span> 
<div class="pagebody" >

<div class="div">
<!--  所有地区列表-->
<div class="leftfloat">
地区列表
<form method="post"  action="<%=request.getContextPath()%>/setOften.do"  class="formM">
<select multiple="multiple"  class="selectM"  name="selectedCC"  id="selectedCC">   <%-- 获取select 中option 的值，不是通过form 的name而是select 的name --%>
  <c:forEach items="${cList}" var="cn">
  <option   value ="${cn.ccId}">${cn.country}--${cn.CCode}</option>
</c:forEach>
  </select>
<input type="submit"  value="添加"  onclick="return is_selected('selectedCC')" />
</form>
</div>


<!-- 常用地区列表 -->
<div class="leftfloat">
常用地区表
<form class="formM"  method="post" action="<%=request.getContextPath()%>/getOperators.do">
 <select multiple="multiple"  class="selectM"  id="oftenCC"  name="oftenCC">
  <c:forEach items="${ocList}" var="oc">
  <option   value ="${oc.ccId}">${oc.country}--${oc.CCode}</option>
</c:forEach>
  </select>
<input type="button"  value="移除"   onclick="if(is_selected('oftenCC'))location.href='${pageContext.request.contextPath}/rmOften.do?rmCC='+selected('oftenCC')" >
<input type="submit"  value="确定"   onclick=" return is_selected('oftenCC') "> <!-- 这里必须有return，否则还是会提交到action 中 -->
</form>

</div>

<!--运营商列表 -->
<div class="leftfloat">
运营商列表
<form class="formM"  method="post" action="<%=request.getContextPath()%>/getRate.do">
 <select multiple="multiple"  class="selectM"  id="opList" name="opList" required>
  <c:forEach items="${cl}" var="cl">
  <option   value ="${cl.ccId}">${cl.operator}--${cl.code}</option>
</c:forEach>
  </select>
 
<input type="button"  value="移除"   onclick="if(is_selected('opList'))location.href='${pageContext.request.contextPath}/rmOperators.do?rmOp='+selected('opList')" >
<input type="submit"    value="确定"   />
</form>
</div>

<!-- 删除运营商列表中的运营商 -->
<form id="del_op_form" action="${pageContext.request.contextPath}/rmOperators.do" method="post">
<input type="hidden" name="rmOp"/>

</form>


<!-- 页面布局使用的div -->
<div  style="clear:both">
</div>

</div>

<!-- 发送的客户 -->
 <div class="div1">
发送的客户:
 <c:forEach items="${cusList}"  var="cus">
${cus.vosId}/${cus.CId}
<c:if test="${cusList.size()>1}">
<span style="color:red;"> |</span>
</c:if>
</c:forEach> 
 </div>
 


 
 <!-- rate 表中的内容 -->
<div class="midbody">
<form action="${pageContext.request.contextPath}/sendMail/setMailInfo.do"  method="post"  name="rate_form"  id="rate_form"  onsubmit="return false">
	报价模板:
	<select name="excel">
		<c:forEach items="${eList }" var="e">
			<option value="${e.filePathName}"  ${dExcelTp==e.name?"selected":null }>${e.name }</option>
		</c:forEach>
	</select>
<span style="color: red">附件名称</span>:<input  type="text" name="mail_attach"  class="mail_attach" placeholder="名称为空,则使用默认附件名称"  value="${mail_attach }"/>
<table border=1>
<tr>
<th>国家</th>
<th>运营商</th>
<th>code</th>
<th>last_rate</th>
<th>rate</th>
<th>change</th>
<th>B_unit</th>
<th>level</th> 
<th>prefix</th>
<th >effective_time</th>
<th >expire_time</th>
<th >备注</th>
</tr>

<!-- 控制所有的值--> 
<tr>
<td></td>
<td></td>
<td></td>
<td></td>
<td><input type="text" id="a_rate" class="input" onChange="all_rate_change('a_rate','new_rate')"></td>
<td></td>
<td>
<select id="billing_type"  onChange="fill_all('billing_type','billingType')">
<c:forEach items="${bList}" var="billing_type">
<option ${dfBilling==billing_type?"selected":""}>${billing_type}</option>
</c:forEach>
</select>
</td>

<td  class="num_input">
<select id="a_level" onChange="fill_all('a_level','level')">
<c:forEach items="${lList}" var="level">
<option ${dfLevel==level?"selected":null}>${level}</option>
</c:forEach>
</select>
</td>

<td>
<input type="text"  name="a_level_prefix"  value="${dfPrefix}" id="a_level_prefix" class="num_input" onChange="fill_all('a_level_prefix','levelPrefix')"/>
</td>
<td>
<input type="date" name="effect_time"  id="effect_time"  onChange="fill_all('effect_time','effectiveTime')" />
</td>
<td>
<input type="date" name="expire_time"  id="expire_time"  onChange="fill_all('expire_time','expireTime')" />
</td>
<td >
<input type="text" name="remark_h"  id="remark_h"  onChange="fill_all('remark_h','remark')"  class="input"/>
</td>

</tr>
<br />
邮件主题:<input type="text"  name="mail_subject"   class="mail_subject"  placeholder="主题为空,则使用默认邮件主题"  value="${mail_subject }"/>
<!--rate 表里的数据  -->

 <c:forEach items="${rateList}" var="rate"  varStatus="i">
<tr>
<td><input type="text"  class="input"  name="RateList[${i.index }].country"  value="${rate.country}"  /></td>
<td><input type="text"  class="input"  name="RateList[${i.index }].operator"  value="${rate.operator}"/></td>
<td><input type="text"  class="input"  name="RateList[${i.index }].code"  value="${rate.code}"/></td>
<td name="rate"><input type="text"  class="input"  name=""  readOnly  <c:if test="${rate.rate!=null }"> value="${rate.doubleF(rate.rate)}"</c:if>  
 <c:if test="${rate.rate==null }"> value="${rate.rate }"</c:if> /></td> 
<td name="new_rate" id="RateList[${i.index }]" ><input type="text"  class="input"  name="RateList[${i.index }].rate"    <c:if test="${rate.rate==null }"> value="${rate.rate }"</c:if>
<c:if test="${rate.rate!=null }"> value="${rate.doubleF(rate.rate)}"</c:if> 
 OnInput="single_rate_change('RateList[${i.index }]')"   /></td>
<td><input type="text"  class="input"  name="RateList[${i.index }].isChange"  value="Current" /></td>
<td name="billingType">
<select name="RateList[${i.index }].billingType">
<c:forEach items="${bList}" var="billing_type">

<option  <c:if test="${dfBilling==billing_type}"> selected="selected" </c:if>>${billing_type}</option>
</c:forEach>
</select>
</td>

<td name="level">
<select name="RateList[${i.index }].level" >
<c:forEach items="${lList}" var="level">
<option <c:if test="${level==dfLevel}"> selected="selected" </c:if> >${level}</option>
</c:forEach>
</select>

</td>
<td name="levelPrefix"><input type="text"  class="num_input"  name="RateList[${i.index }].levelPrefix"   value="${dfPrefix}"  style="color:red;font-weight: bolder;"/></td>
<td name="effectiveTime"><input type="date"   name="RateList[${i.index }].effectiveTime"    value="${rate.effectiveTime }"   /></td>
<td name="expireTime"><input type="date"   name="RateList[${i.index }].expireTime"    value="${rate.expireTime }"/></td>
<td name="remark"><input type="text"   name="RateList[${i.index }].remark"  /></td>
</tr>
</c:forEach>
</table>

<div class="mail_content">
<span style="float:left">邮件内容:</span>
	<div class="mail_content_text">
		<textarea  id="mail_content_org" name="mail_content"  >

		</textarea>
	</div>
	
	<div style="clear:both;"></div>
</div>


<div class="form_submit_div">
<input type="hidden"  id="dfLevel" value="${dfLevel==null?'error':dfLevel }"/>
<input type="button"  value="Check"    onclick="getRateArray()"  class="form_submit"/>
<input type="hidden" id="rateEmpty" value="${rateList.size() }"/>
<input type="button"  value="发送"  id="ok_commit"  onclick="check_and_load()" disabled class="form_submit"/>
</div>

</form>

</div>
<!-- 进度条部分 -->
<div id="add_override">
<!--  <div class="div_override"></div> 
<div class="div_prog">
进度条:
<progress id="prog"  value="0"  max="100" ></progress>
</div>
  -->
</div>

</div>


<script type="text/javascript">

CKEDITOR.replace('mail_content_org');
var ckeditor=document.getElementById("mail_content_org");
ckeditor.innerHTML='${mail_content}';
//清空 session 中关于进度条的 变量
<% request.getSession().removeAttribute("sendMailProgress");%>;


function check_and_load(){
	var rateSize=document.getElementById("rateEmpty");
//	alert(rateSize.value);
	if(rateSize.value>0 && rateSize.value!=""){
		document.getElementById("rate_form").submit();
		loading();
		}
	else{
		alert("请先选择发送的运营商");

	}

}


// 返回multi select 元素中选中的option value字符串，以 ；分割
function selected(sl){
		var mSl=document.getElementById(sl);
		var opVals="";
		for(var i=0;i<mSl.length;i++){
				if(mSl.options[i].selected){
					opVals+=mSl.options[i].value+";";
				}
			}
	return opVals;
	//不需要在这里提交到某个特定的action ，而是直接返回选择的option 的字符串，可以将这一串在location.href 中当做参数传到action 中
//	location.href="${pageContext.request.contextPath}/rmOften.do?rmCC="+rmCC;

}

//检查optoin 列表是否为空
function checkSelectM(id){
	var mSl=document.getElementById(id);
//	alert(mSl);
	if(mSl.length==0){
		alert("列表为空！请先添加数据");
		return false;
		}else return true;
	
}

//检查是否有选择select 的option 
function is_selected(id){
	var mSl=document.getElementById(id);
//	alert(mSl);;
	for(var i=0;i<mSl.length;i++){
			if(mSl.options[i].selected){
					return true;//如果有存在选择的就返回
				}
		}
	alert("请先选择，再操作");
	return false; /* 假如没有被选的就返回false */
	
}

//将multi select 的所有option设置为已选 
function selectAll(id){
	var mSl=document.getElementById(id);
	for(var i=0;i<mSl.length;i++){
			mSl.options[i]=selected;
		}
	
}

//给date 类型的input元素赋一个默认值, 暂时没有用到
function curDate(){
	var curDate=new Date();
/* 	<input type="date" id="effect_time"/> ;date 类型必须是2015-06-07这种格式,才会生效,所以凡是小于10的都要在前面添加0*/
	var mStr=curDate.getMonth()+1;
	if(mStr<10){
		mStr="0"+mStr;
		}
	
	var dStr=curDate.getDate();
	if(dStr<10){
		dStr="0"+dStr;
		}
	var reDate=curDate.getFullYear()+"-"+mStr+"-"+dStr;
	//alert(reDate);
	document.getElementById("effect_time").value=reDate;
	
//通过td 获取 input 子元素
 etimes=document.getElementsByName("effectiveTime");

	for(var i=0;i<etimes.length;i++){
		//每一个td元素的第一个子结点,(input) 可以使用childNodes.length 查看有多少子节点
			if(etimes[i].childNodes[0].value==""){
				etimes[i].childNodes[0].value=reDate;
				}

//			alert(etimes[i].childNodes.length);
		} 

}

//用某一行作为基准,用这一行的值去修改其他所有行的值,
//不过这里是用<td> 的子结点来找到需要改的元素,因为需要要改的元素的name 是动态的
function fill_all(id,name){
	var id_value=document.getElementById(id).value;
	var elements=document.getElementsByName(name);

	//给input 的所有都赋值
	for(var i=0;i<elements.length;i++){
	//alert(elements[i].childNodes[0]);
			elements[i].childNodes[0].value=id_value;
	//		alert(elements[i].getElementsByTagName("select")[0]);
		}
	//给select 标签的赋值
	if(elements[0].getElementsByTagName("select")[0]!=undefined){
		for(var i=0;i<elements.length;i++){
			elements[i].getElementsByTagName("select")[0].value=id_value;
			}
			
		}

}

/* //同时提交多个form 表单
function MForm_submit(name){
	var forms=document.getElementsByName(name);
		for(var i=0;i<forms.length;i++){
		forms[i].submit();
		}
} */

// 当单个new_rate input 的值被改变时(非js 改变),修改对应的change input值 
function single_rate_change(id){

	var new_rate_td=document.getElementById(id); //new_rate td
	var rate=new_rate_td.previousSibling.previousSibling.firstChild;  //rate input 元素
	var change=new_rate_td.nextSibling.nextSibling.firstChild //change input 元素
	if(rate.value!=null && rate.value!=""){
		if(new_rate_td.firstChild.value>rate.value){
			change.value="Increase";
			}
		else if(new_rate_td.firstChild.value<rate.value){
			change.value="Decrease";
			}
		else{
			change.value="Current";
			}
		}
	else{
			change.value="New";
			}
	
}

function all_rate_change(id,name){
	var id_value=document.getElementById(id).value;
	var new_rate_tds=document.getElementsByName(name);
	
	
	//给input 的所有都赋值
	for(var i=0;i<new_rate_tds.length;i++){
		var rate_value=new_rate_tds[i].previousSibling.previousSibling.firstChild.value;
		var change=new_rate_tds[i].nextSibling.nextSibling.firstChild;

		if(rate_value=="" || rate_value==null){
			change.value="New";
			}
		else if(id_value<rate_value){
			change.value="Decrease";
			}
		else if(id_value>rate_value){
			change.value="Increase";
			}
		else{
			change.value="Current";
			}
		new_rate_tds[i].firstChild.value=id_value;
		
		}

	
}



function loading(){

	var parent_div = document.getElementById("add_override");	//获取父级div 元素,这个div 是用来帮助定位一个覆盖层
	 var div_over=document.createElement("div"); //添加覆盖层
	div_over.setAttribute("class", "div_override");
	parent_div.appendChild(div_over); 

	var div_progress=document.createElement("div"); //添加 包含progress元素的div 层
	div_progress.setAttribute("class", "div_prog");
	parent_div.appendChild(div_progress);

	var ele_prog=document.createElement("progress"); //添加progress 元素
	ele_prog.setAttribute("id", "prog");
	ele_prog.value=0;
	ele_prog.setAttribute("max", "100");
	div_progress.appendChild(ele_prog);

	getProgress();
}


	var xmlhttp;
 function getProgress(){
	 xmlhttp=null;
	 	if(window.XMLHttpRequest){
	 		xmlhttp=new XMLHttpRequest();
	 		}
	 	else if (window.ActiveXObject)
	 	  {// code for IE5 and IE6
	 		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	 		  }
	 	if (xmlhttp!=null)
	 		{
	 	 		 var url="getProgress.do";
				xmlhttp.onreadystatechange=updateProg;
			 	 xmlhttp.open("GET",url,true);
			 	 xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			 	 xmlhttp.setRequestHeader("Access-Control-Allow-Origin","*");
		 	 	 xmlhttp.send(null);

			 }
	 }

  function updateProg(){

	 if (xmlhttp.readyState==4)
	  {console.log("xmlhttp.status"+xmlhttp.status);
	  console.log("xmlhttp.readyStatus"+xmlhttp.readyState);
		  // 4 = "loaded"
	  if (xmlhttp.status==200)
	    {// 200 = OK


		  
			var xmlRes=xmlhttp.responseText;
			console.log(xmlRes);
		 	var xmlRes_json=eval("("+xmlRes+")"); // 转换字符串为json 对象
			var prog=document.getElementById("prog");
			prog.value=xmlRes_json.sendMailProgress;
			console.log("prog.value  is "+prog.value);
			
			if(xmlRes_json.sendMailProgress<=100){
		 		 setTimeout("getProgress()",2000);
				}//使用异步来处理,看是否ok


//	 		console.log("value is "+prog.value);
    }
	  else
	    {
	    alert("Problem retrieving XML data");
	    }
	  }
	 }

//获取rate list 中的部分域的key 和value的json 格式.
function getRateArray(){

	var rate_tds=document.getElementsByName("new_rate");
	var level_tds=document.getElementsByName("level");
	var ef_time_tds=document.getElementsByName("effectiveTime");
	var dfLevel=document.getElementById("dfLevel");

	var str="{dfLevel:"+"'"+dfLevel.value+"',"+"array:";
	str=str+"[";
	for(var i=0;i<rate_tds.length;i++){
		str=str+"{"+"rate:"+"'"+rate_tds[i].firstChild.value+"',"+"level:"+"'"+level_tds[i].getElementsByTagName("select")[0].value+"',"+"ef_time:"+"'"+ef_time_tds[i].firstChild.value+"'}";
		if((i+1)<rate_tds.length){
			str=str+",";
			}
		}
	str=str+"]}";
//	alert(str);

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

 	 	 	var url="/SkylineRate/checkBeforeMail.do";
 		  	xmlhttp.open("POST",url,false);
 		  	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
 		 	xmlhttp.send("params="+str);

 			var json_str=xmlhttp.responseText;

 			var json=JSON.parse(json_str)
 	//		alert(cus_arr.length);
 		 	if(json.isTrue=="true"){
 	 			document.getElementById("ok_commit").removeAttribute("disabled");
 	 		 	}

 		 	if(json.Message!="")	{
	 			alert(json.Message);
		 		}
			document.getElementById("notice").innerHTML=json.Message;


		}

	
}

 



</script>





</body>
</html>