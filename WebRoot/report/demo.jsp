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
//---����ͼ
div1.innerHTML=ct1.draw("Month,Lio Xue,Ned Zhang,Robin Hou; Month 1,20.4,30.6,90; Month 2,27.4,38.6,34.6; Month 3,45.9,0,0", "type:bar; showVal:t; vname:��������; title:Mohthly Summary")
//---����ͼ
//div2.innerHTML=ct1.draw("����,����,�в�,����;1����,20.4,30.6,90;2����,27.4,38.6,34.6;3����,45.9,0,0","type:lines;showVal:t;vname:���۶�;title:��ͳ��ͼ")
//---����ͼ
//div3.innerHTML=ct1.draw("����,����,�в�,����;1����,20.4,30.6,90;2����,27.4,38.6,34.6;3����,45.9,0,0","type:pie;showVal:t;vname:���۶�;title:��ͳ��ͼ")
</script>