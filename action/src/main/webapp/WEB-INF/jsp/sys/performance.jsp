<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>            
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/performance.css" rel="stylesheet" type="text/css"/>
   <script type="text/javascript" src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
   <script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统性能</title>
</head>
<body>

<div class="per_body">

	<div id="container" style="width:400px;height:300px" >
	
	</div>

</div>


<button onclick="updateChart()">aa</button>

  <script>
  var  chart;
  $(function () { 
	   chart=new Highcharts.Chart({                   //图表展示容器，与div的id保持一致
	    	 chart: {
	    		 renderTo : "container",  // 注意这里一定是 ID 选择器
	             zoomType: 'x',
	             spacingRight: 20
	         },
	        title: {
	            text: '线程'      //指定图表标题
	        },
	        xAxis: {
					type:'datetime',
					maxZoom: 3600*1000
	        },
	        yAxis: {
	            title: {
	                text: '线程数'                  //指定y轴的标题
	            }
	        },
	        series: [{

	            name: 'TIME',
	            pointInterval: 3 * 1000,
				pointStart:Date.now()+8*3600*1000,  //一个小时前

	            
	            data: [
					
	                
	        	]
	        }]


	        
	    });
	});



  onload=function(){
		updateChart();
	  }

 function updateChart(){


var data=	chart.series[0].yData;
var jsonData=JSON.stringify(data);

//alert(data[0]);
//var data_str=JSON.stringify(data);

//alert(data_str);

	$(function (){
		$.post("${pageContext.request.contextPath}/sys/getPerformance.do",{"threadNums":jsonData},
				function(data,status){
					if(status=="success"){
						chart.series[0].setData(JSON.parse(data));
					}
					
				}
			);
		}
	);

		
	 setTimeout("updateChart()",3*1000);



		
	  }
   </script>




</body>
</html>