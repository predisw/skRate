<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/addRate.css" rel="stylesheet" type="text/css"/>
<title>发送报价</title>
</head>
<body >

<div class="pagebody">


<div class="leftfloat">
业务员列表:
<form method="post"  action="<%=request.getContextPath()%>/getCus.do"  class="formM">
<select multiple="multiple"  class="selectM"  name="checkCus" required>
  <c:forEach items="${empList }" var="emp">
  <option   value ="${emp.ENum}">${emp.ENum}-${emp.ENameCn}</option>
</c:forEach>
  </select>
<input type="submit"  id="checkCus" value="确定"  >
</form>
</div>


<div class="leftfloat">
客户vosID列表
<select id="cusL" multiple="multiple"  class="selectS">
 <c:forEach items="${cusList}" var="cus">
  <option value ="${cus.CId}">${cus.ENum}--${cus.vosId}</option>
</c:forEach>
</select>
  <input type="button" onclick="mvSelected('cusL','beSelected')"  value="确定" />
</div>


<div class="leftfloat">
被选客户
<!-- 可以通过js 提交选中的客户到“被选客户”栏，然后点提交的时候再将选择的客户id提交到服务器上去处理-->
<!--  参考这个http://zhoujingxian.iteye.com/blog/559804，可以将选中的提交到本页，保存到字符串？然后在这里历遍出来-->
<!-- 提交的是客户的id -->
<form method="post" action="<%=request.getContextPath()%>/getTargetCus.do"  class="formM">
<select id="beSelected"  multiple="multiple"  class="selectM"  name="cusList" required>
</select>
<input type="button"  onclick="mvSelected('beSelected','cusL')" value="移除"/>
<input type="submit" value="提交">
</form>
</div>


</div>



<script type="text/javascript">

function mvSelected(rm,mvTo){
	var rm=document.getElementById(rm);
	var mvTo=document.getElementById(mvTo);
//	alert(cus.length);
	for(var i=0;i<rm.length;i++){
			if(rm.options[i].selected){  //如果有被选中的
				var op=document.createElement("option"); //创建一个select 的option 元素
				op.value=rm.options[i].value; //给这个元素添加一个value属性
				op.innerHTML=rm.options[i].innerHTML; //将select1 的显示text赋给select2这个元素
				mvTo.appendChild(op); //将这个元素添加到select2 中
				rm.removeChild(rm.options[i]);  //从原来的select1 中移除添加到select2中的option 元素
				i=-1; //因为每次移除一个option 元素，select1的长度都会改变，而且i 也会递增，所以要重置i的值到0，才可以重新历 遍。
				//设置为-1 是因为 返回for 的时候，i 会执行 i++;
				}
		}

}



</script>



</body>
</html>