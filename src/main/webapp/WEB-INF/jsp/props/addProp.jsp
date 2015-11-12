<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/index.jsp" %>                
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/prop.css"  rel="stylesheet" type="text/css" /> 
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/my.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>可选属性</title>
</head>
<body>
<div class="prop_body">

	<div style="width:49%;float:left;">
		添加可选属性:<br />
		<form action="${pageContext.request.contextPath}/props/addProp.do" method="post">
			属性名:
			<select name="name" >
				<option value="billing_unit">billing_unit</option>
				<option value="level">level</option>
			</select>
			属性值:
			<input type="text" name="value" />
				
			<!-- 权限控制 -->
			<c:if test="${pStatus['props-add']==true }">
			<input type="submit" value="添加"/>
			</c:if>
				
		</form>
	</div>

	
	<div class="del_props">
		删除可选属性:
		<form action="${pageContext.request.contextPath}/props/getProps.do" method="post"  >
			属性名:
			<select name="name"  id="props_name" onChange="getOptions()">
				<option > </option>
				<option value="billing_unit">billing_unit</option>
				<option value="level">level</option>
			</select>
			属性值:
			<select name="value" id="props_value">
<%-- 				<c:forEach>
				
				</c:forEach> --%>
			</select>
			<!-- 权限控制 -->
			<c:if test="${pStatus['props-del']==true }">
				<input type="button" value="删除" onclick="if(confirm('sure to delete ?'))delProps()"/>
			</c:if>
		</form>
	
	</div>

	<div style="clear:both;"></div>

	<div>

	<form action="${pageContext.request.contextPath}/props/saveOrUpdate.do" method="post">
	全局抄送地址:
	<input type="hidden"  name="id" value="${bccAddr.id}"/>
	<input type="hidden"  name="version" value="${bccAddr.version}"/>
	<input style="width:230px;" type="hidden" name="name"  value="bccAddr"/> <!--  属性名-->
	<input style="width:230px;" type="text" name="value"   value="${bccAddr.value}"/> <!--  属性值-->
	
	<!-- 权限控制 -->
	<c:if test="${pStatus['global-email']==true }">
		<input type="submit"  value="添加"/>
	</c:if>
	</form>
	
	</div>

	<div>
	默认邮件内容
		<form action="${pageContext.request.contextPath}/props/saveOrUpdate.do" method="post">
			<input type="hidden"  name="id" value="${default_mail_content.id}"/>
			<input type="hidden"  name="version" value="${default_mail_content.version}"/>
			<input type="hidden" name="name"  value="default_mail_content"/>
			<textarea id="defualt_mail_content" name="value" >
			</textarea>
			
			<!-- 权限控制 -->
			<c:if test="${pStatus['email-content']==true }">
				<input type="submit"  value="确定"/>
			</c:if>
		</form>
	</div>
	
	<div>

	</div>

</div>

<script type="text/javascript">
CKEDITOR.replace('defualt_mail_content');
var ckeditor=document.getElementById("defualt_mail_content");
ckeditor.innerHTML='${default_mail_content.value}';

function getOptions(){
	var name = document.getElementById("props_name").value;
	var data="name="+name;
	var xmlHttp=loadAjax("post","getProps.do",data,function (){
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
			var ele=document.getElementById("props_value");
 			ele.innerHTML="";//清除上次获取的值
			var arr= xmlHttp.responseText;
			var json_arr =JSON.parse(arr);
			for(var i=0;i<json_arr.length;i++){
				var op=document.createElement("option"); //创建一个select 的option 元素
				op.value=json_arr[i].id  //给这个元素添加一个value属性
				op.id=json_arr[i].id  //给这个元素添加一个id属性
				op.innerHTML=json_arr[i].name; //option 显示的text 文本值
				ele.appendChild(op);  //添加option 元素到select 中
				}
			
			} 
	}
			);
	
}

function delProps(){
	var id = document.getElementById("props_value").value;
	var data="id="+id;
	var xmlHttp=loadAjax("post","delProps.do",data,function(){
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
			var json =JSON.parse(xmlHttp.responseText);
			alert(json.Message);
			if(json.isSuccess){ //移除删除成功的属性
				var child=document.getElementById(id);
				document.getElementById("props_value").removeChild(child);
					}	
				}
			}
		);
}



</script>
</body>
</html>