<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/index.jsp" %>    
<html>
<head>
<script src="${pageContext.request.contextPath}/js/jquery.min.js" ></script>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/my.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加用户</title>
</head>
<body>

<div class="main_body">

	<div>
		添加用户<br />
		<form method="post" action="<%=request.getContextPath()%>/user/addUser.do" >
			用户名字
			<input type="text" name="UName" required  id="uName"  onblur="check('uName','Message')"/> <span id="Message" style="color:red;"></span>       
			<br /> 用户密码
			<input type="password" name="password"/> <br />
			<input type="submit" value="添加"  id="add" disabled /> <!-- disabled 是防止添加不符合条件的user,例如 不可用的名字依然可以提交. -->
			
		</form>
	</div>
<span style="color:red;">${Message}</span>
	<div>
	修改用户
		<form action="${pageContext.request.contextPath}/user/updateCheck.do" method="POST">  
			<select name="AllUName" id="AllUName" onchange="getUser('AllUName')" >
				<option value="${user.UName }" selected >${user.UName }</option>
			</select>
			<input type="hidden"  name="UId"  id="UId"  value="${user.UId }" />
			<input type="hidden"  name="version"  id="version"  value="${user.version}" />
			<br />用户名:<input type="text" name="UName"  id="UName"  value="${user.UName}"  required/>	 
			<br />密码:<input type="password" name="old_pass"  id="old_pass"  onInput="getUser('AllUName')"/>	
			<br />新密码:<input type="password" name="new_pass"/>	
			<br />确认密码:<input type="password" name="password" required/>			<br />
			<input type="submit" value="修改"/>  
			
			<input type="button" value="删除" onclick="delUser()"/>
			
		</form>
	</div>
</div>


<script type="text/javascript">
//如果没有这个权限,则不加载全部用户
onload=getAllUName("AllUName");

//提示新增用户名是否可以使用
function check(id,message){
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
	 		 var ele=document.getElementById(id); 

	 	 	var url="addCheck.do";
		  	xmlHttp.open("POST",url,false);
		  	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
		 	xmlHttp.send("uName="+ele.value);
			var json_str=xmlHttp.responseText;
			
			var json=eval("("+json_str+")"); // 转换字符串为json 对象

			var message=document.getElementById(message);
			message.innerHTML=json.Message;
			if(json.isTrue=="true"){
				document.getElementById("add").removeAttribute("disabled");
				}
		  }
}

//获取所有用户名字,显示所有可以修改的用户名
function getAllUName(id){
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
	 	 	var url="getAllUName.do";
		  	xmlHttp.open("GET",url,false);
		  	xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
		 	xmlHttp.send(null);
			var json_str=xmlHttp.responseText;
			
			var json_arr=eval("("+json_str+")"); // 转换字符串为json 对象数组
			var select=document.getElementById(id); //select 元素
	//		select.innerHTML="";//在获取某个员工的客户之前,先移除之前的客户
			for(var i=0;i<json_arr.length;i++){
				var op=document.createElement("option"); //创建一个select 的option 元素
				op.value=json_arr[i]; //给这个元素添加一个value属性
				op.innerHTML=json_arr[i]; //option 显示的text 文本值
				  //添加option 元素到select 中
				if("${user.UName}"!=op.value){
	//					op.setAttribute("selected", "selected");
						select.appendChild(op); // 如果和当前用户相等则不再添加,因为默认已经添加了.
					}
				}
		  }
}

	//显示默认值
	function getUser(id){
		var name_ele=document.getElementById(id);
		var data = "UName="+name_ele.value;
		var xmlHttp=loadAjax("POST","getUserByName.do",data,function(){
			 if (xmlHttp.readyState==4 && xmlHttp.status==200){
					var json_str=xmlHttp.responseText;
					var json_arr=eval("("+json_str+")"); 
					document.getElementById('UId').value=json_arr.UId;
					document.getElementById("UName").value=json_arr.UName;
					document.getElementById("version").value=json_arr.version;
				 }
			}
				);
	}



function delUser(){
	var user_id =document.getElementById('UId').value;
	var pass=document.getElementById('old_pass').value;

	if(pass==""){
		alert('请输入密码');
		return false;
	}
	
	var url = "${pageContext.request.contextPath}/user/delUser.do";

	$(function(){
		$.post(url,{"id":user_id,"pass":pass},function(data,status){
			if(status=="success"){
				
				alert(data);
				location="${pageContext.request.contextPath}/user/toUser.do";
			}
		});
	});
	
}



</script>

</body>
</html>