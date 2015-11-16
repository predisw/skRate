//将父级checkbox (id) 的值 复制给所有name 属性为name 的checkbox
function selectCheckbox(id,name)
{
	var pCheckbox=document.getElementById(id);
	var checkboxs=document.getElementsByName(name);

	for(var i=0;i<checkboxs.length;i++)
	{
		checkboxs[i].checked=pCheckbox.checked;
	}
}


//在两个select 之间移动被选的option
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



//在两个select 之间复制被选的option
function cpSelected(from,to){
	var from=document.getElementById(from);
	var to=document.getElementById(to);
//	alert(cus.length);
	for(var i=0;i<from.length;i++){
			if(from.options[i].selected){  //如果有被选中的
				var op=document.createElement("option"); //创建一个select 的option 元素
				op.value=from.options[i].value; //给这个元素添加一个value属性
				op.innerHTML=from.options[i].innerHTML; //将select1 的显示text赋给select2这个元素
				to.appendChild(op); //将这个元素添加到select2 中
				}
		}

}

//删除被选的option
function rmSelected(id){
	var select=document.getElementById(id);
//	alert(cus.length);
	for(var i=0;i<select.length;i++){
			if(select.options[i].selected){  //如果有被选中的
				select.removeChild(select.options[i]);
				i=-1;
				}
		}

}

//设置select 所有的option 为被选true,或者都不选false
function setAllOption(select,selectValue){

	for(var i=0;i<select.length;i++){
		select.options[i].selected=selectValue;
	}

}

//返回mutil select 所有的option 的值 组成的字符串 ,如: "1,2,3,4";
function getAllOption(select){
	var value="";
	for(var i=0;i<select.length;i++){
		value=value+select.options[i].value;
		if((i+1)<select.length){
			value+=",";
		}
	}
	return values;
}



//返回一串字符串(如: 1,2,3,4  )或者空字符串.
function getSelected(select){

	var values="";
	for(var i=0;i<select.length;i++){
		if(select.options[i].selected){
			if(values.length>0){
				values+=",";
			}
			values+=select.options[i].value;
		}
	}
		
	return values;
}












//
//给date 类型的input元素赋一个值为现在的值,格式为 yyyy-MM-dd
function curDate(id){
	var curDate=new Date();

/* 	<input type="date" id="effect_time"/> ;date 类型必须是2015-06-07这种格式,才会生效,所以凡是小于10的都要在前面添加0,js 中 小于 10 的时间数字前面是没有0的.*/
	var mStr=curDate.getMonth()+1;  //月
	if(mStr<10){
		mStr="0"+mStr;
		}
	
	var dStr=curDate.getDate(); //日
	if(dStr<10){
		dStr="0"+dStr;
		}

	var reDate=curDate.getFullYear()+"-"+mStr+"-"+dStr;
	//alert(reDate);
	document.getElementById(id).value=reDate;

}

/*创建ajax*/

function  loadAjax(method,url,data,callback){
	var xmlHttp;
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlHttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
		xmlHttp.onreadystatechange=callback;
		xmlHttp.open(method,url,true);
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
		xmlHttp.send(data);
		return xmlHttp;
}

//判断页面是否出现进度条
function isScrolledBar(){

	var offsetHeight=document.documentElement.offsetHeight;
	var clientHeight = document.documentElement.clientHeight;
	if(offsetHeight>clientHeight){
		return true;
	}
	return false;
}

function toTop(){
//	var scrollTop=document.body.scrollTop+document.documentElement.scrollTop;
	window.scrollTo(0,0);
}

function toBottom(){
	var scrollTop=document.body.scrollTop+document.documentElement.scrollTop;
	var offsetHeight=document.documentElement.offsetHeight;
	var clientHeight = document.documentElement.clientHeight;
	scrollTop=offsetHeight-clientHeight;
	scrollTo(0,scrollTop);
}
