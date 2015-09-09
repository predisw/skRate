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






