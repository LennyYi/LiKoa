<%@ page contentType="text/html; charset=gb2312"%>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/graphic.js"></script>

<html>
<body style="margin-top:50}"  onselect="return false" oncopy="return false" oncontextmenu="return false">
	<div id=div1 style="position:absolute;top:100;left:50;cursor:default;text-align:center">
	</div>
	<div id=div2 style=position:absolute;top:300;cursor:default>
	</div>
	<div id=div3 style=position:absolute;top:600;cursor:default>
	</div>
</body>
</html>

<script>
//---柱形图
div1.innerHTML=ct1.draw("Month,Lio Xue,Ned Zhang,Robin Hou; Month 1,20.4,30.6,90; Month 2,27.4,38.6,34.6; Month 3,45.9,0,0", "type:bar; showVal:t; vname:处理数量; title:Mohthly Summary")
//---线形图
//div2.innerHTML=ct1.draw("季度,东部,中部,北部;1季度,20.4,30.6,90;2季度,27.4,38.6,34.6;3季度,45.9,0,0","type:lines;showVal:t;vname:销售额;title:月统计图")
//---饼形图
//div3.innerHTML=ct1.draw("季度,东部,中部,北部;1季度,20.4,30.6,90;2季度,27.4,38.6,34.6;3季度,45.9,0,0","type:pie;showVal:t;vname:销售额;title:月统计图")
</script>