<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/index.jsp" %>        
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/common.css"  rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>版本信息</title>
</head>
<body>
<div class="main_body">
	<div>
<h2>SkylineRate System</h2>

2015-11-3 version 1.8.6-1 <br />
1.落地历史报价添加了查询"ALL" 功能,可以查询所有报价. <br /> 

2015-10-30 version 1.8.6 <br />
0.修改了事务层<br />
1.在历史报价中,不再显示被删除的code 的报价,只会在查询code 所有的报价的时候才会显示.<br />
2.修复 重复发送 生效时间比现在大的报价,报价excel中显示和第一次发送不一样的问题.<br />
3.修复修改rrate 历史报价不触发事务的问题<br />

2015-10-29 version 1.8.5 <br />
1.如果落地报价费率的生效时间在于未来,那么就会设置当前落地报价的失效时间为未来生效时间的前一天.<br />
2.增加删掉落地报价的功能.<br />


2015-10-28 version 1.8.4<br />
附件报价:按code 排序,查看记录: 按照country,operate 排序<br />
上传文件大小限制改为15m <br />
修复数据转移的问题 <br />


2015-10-15 version 1.8.2 <br />
修复一个bug:生效时间不一致时,导致生效时间为now 的报价不写入附件.<br />

2015-10-12 version 1.8.1 <br />
报价附件内容按照A-Z 排序 <br />
countryCode 也按照A-Z 排序<br />
2015-9-28 version 1.8 <br />
关闭了性能日志记录.还需要完善<br />
日志记录系统需要完善.<br />

	
<hr />
2015-8-21<br />	
添加了 附件名自定义.<br />
添加了 单个客户默认显示上次发送邮件的附件,内容,主题的内容.<br />
2015-8-20 <br />
添加了 Check 按钮,Check按钮会检查level 是否一致,effective_time 是否为空,rate 是否为空.<br />
2015-8-19 <br />
添加了操作日志.<br />
2015-8-17 <br />
添加了系统日志.<br />
2015-8-14 version 1.06 <br />
修改了页面的显示样式<br />
	<hr />
2015-8-13 version 1.05 <br />
历史报价中添加了"过期时间" <br />
发送记录中添加了"错误" 标记功能,以标记这封报价是错误的,可以修正历史报价中错误的历史报价.同一个客户在发送新的报价前需要先将错误的报价标记.<br />
	<hr />
2015-8-10 version 1.04 <br />
删除数据时会先提示<br />
在添加报价时如果多个客户的level 或者billing_unit 或者prefix 不一致则会提示,直到重新选择客户<br />
修复 小数超过4位显示不正常的问题.目前最多正常显示8位小说<br />
修复 不小心按到回车 导致立刻发送报价的问题<br />
如果在报价记录中添加了备注,则可以在历史报价中查询该报价的备注<br />
如果有效时间超过了当天时间,则附件excel 中自动添加 带有失效时间的一行.但是!!!!,历史报价中还不能记录这个失效时间.<br />
<hr />
	2015-7-29  Version beta 1.0  测试版<br />
	<hr />
2015-8-3  Version beta 1.01  测试版<br />
修复了 邮件内容中上传插入多图片的功能<br />
添加了 如果有多人同时修改信息时出现冲突,会提示<br />
<hr />
2015-8-4 <br />
历史报价中添加了该报价所属的vosId<br />
添加客户抄送,暗送邮箱地址<br />
<hr />
2015-8-5 <br />
重新进入发报价处,运营商列表会清空上一次的选择.
<hr />
2015-8-6 Version beta 1.02  测试版 <br />
增加程序的异常的检测和提示,和完善事务管理,保证数据的一致.<br />
地区详情 添加/删除之后 会停留在同一页<br />
<hr />
2015-8-7 <br />
保证地区详情中的运营商code 必须唯一.
<hr />
2015-8-8 version 1.03 测试版<br />
邮件附件 excel 中的时间格式为 "月-日-年"<br />
报价中添加了 expire data 过期时间<br />
如果 "level_prefix" 标题行(不是level prefix内容行)上没有内容,则在邮件附件中不会显示Prefix 这个标题<br />
历史报价中如果不选 "'from"的时间, 则结果只显示 到to 时间的最新报价记录,而不是所有<br />


	</div>
</div>
</body>
</html>
