<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>            
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/performance.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<script src="http://code.highcharts.com/stock/highstock.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统性能</title>
</head>
<body>
	${Message}
<div class="per_body">

	<div id="container" style="width:400px;height:300px" >
	
	</div>

	<input type="hidden" id ="thread" value="${threadNums }"/>
	<input type="hidden"  id="curPerformance" />
</div>


<button onclick="updateChart()">aa</button>

  <script>
  var  chart;
  $(function () { 

	    Highcharts.setOptions({
	        global : {
	            useUTC : false
	        }
	    });

	
		  
	   chart=new Highcharts.StockChart({                   //图表展示容器，与div的id保持一致
	         chart : {
	    		 renderTo : "container",  // 注意这里一定是 ID 选择器
	             events : {
	                 load : function () {
	                     // set up the updating of the chart each second
	                     var series = this.series[0];
	                     setInterval(function(){
	                    	 $.get("${pageContext.request.contextPath}/sys/getPerformance.do",
		                				function(data,status){
		        					if(status=="success"){
		            					var json=JSON.parse(data);
		      //      					alert(json.currentThreadNum);
		 	                         series.addPoint(json.currentThreadNum,true,true)

		        					}else{ series.addPoint([0, 0], true, true);}

		        				}
		        			);
		                     },1000);
	                 }
	             }
	         },

	         rangeSelector: {
	             buttons: [{
	                 count: 1,
	                 type: 'minute',
	                 text: '1M'
	             }, {
	                 count: 5,
	                 type: 'minute',
	                 text: '5M'
	             }, {
	                 type: 'all',
	                 text: 'All'
	             }],
	             inputEnabled: false,
	             selected: 0
	         },

	         title : {
	             text : 'Thread Number'
	         },

	         exporting: {
	             enabled: false
	         },

	         series : [{
	             name : 'Random data',
	             data :JSON.parse(document.getElementById('thread').value)
	         }]
	     });

	 });





 
   </script>




</body>
</html>