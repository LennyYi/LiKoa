<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">

<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
	import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.housekeeping.vo.StaffVO"%>
<%@page import="com.aiait.framework.i18n.*,com.aiait.eflow.common.*"%>
<%@ include file="/common/loading.jsp"%>

<html>

<head>
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/NovaJS/jqGrid/CSS/ui.jqgrid.css" media="screen">

<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">

<style>
#pager451{
	width:1038px !important;
}

</style>
<style> 
#txt{ 
    width:74px; 
    height:74px; 
    line-height:74px; 
    position:absolute; 
    text-align:center; 
    color:#9e9fa3; 
    font-size:18px; 
    font-family:Arial; 
} 

#txt1{ 
    width:74px; 
    height:74px; 
    line-height:74px; 
    position:absolute; 
    text-align:center; 
    color:#9e9fa3; 
    font-size:18px; 
    font-family:Arial; 
} 
#txt2{ 
    width:74px; 
    height:74px; 
    line-height:74px; 
    position:absolute; 
    text-align:center; 
    color:#9e9fa3; 
    font-size:18px; 
    font-family:Arial; 
} 
#txt3{ 
    width:74px; 
    height:74px; 
    line-height:74px; 
    position:absolute; 
    text-align:center; 
    color:#9e9fa3; 
    font-size:18px; 
    font-family:Arial; 
} 
#txt4{ 
    width:74px; 
    height:74px; 
    line-height:74px; 
    position:absolute; 
    text-align:center; 
    color:#9e9fa3; 
    font-size:18px; 
    font-family:Arial; 
} 

#circleBox div{
 margin-left:30px; 
 float: left;
}
</style> 

<script type=text/javascript   src="<%=request.getContextPath()%>/js/raphael-source.js"></script> 
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>

<script type=text/javascript src="<%=request.getContextPath()%>/js/jquery.js"></script>

<script type=text/javascript src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/jquery-ui-custom.min.js"></script>

<script type=text/javascript src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/jquery.layout.js"></script>

<script type=text/javascript src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/grid.locale-cn.js"></script>


<script type=text/javascript src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/ui.multiselect.js"></script>
<script type=text/javascript src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/jquery.jqGrid.js"></script>
<script type=text/javascript src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/jquery.tablednd.js"></script>
<script type=text/javascript src="<%=request.getContextPath()%>/js/NovaJS/jqGrid/jquery.contextmenu.js"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script> 
//paperId=画布ID,textId=数字的ID,strokecolor=填充颜色,渐变颜色1=sidecolor,渐变颜色2=innercolor,totol=总共的量,textNum=圆圈中显示的量
 function draw(paperId,textId,strokecolor,sidecolor,innercolor,totol,textNum){
	 //初始化Raphael画布 
     this.paper = Raphael(paperId, 74, 74); 
     //把底图先画上去 
     this.paper.image("images/circle_bg.png", 0, 0, 74, 74); 
     //进度比例，0到1，在本例中我们画65% 
     //需要注意，下面的算法不支持画100%，要按99.99%来画 
     var percent = textNum/100, 
         drawPercent = percent >= 1 ? 0.9999 : percent; 
     //开始计算各点的位置，见后图 
     //r1是内圆半径，r2是外圆半径 
     var r1 = 26, r2 = 31, PI = Math.PI, 
         p1 = { 
             x:37,  
             y:69 
         }, 
         p4 = { 
             x:p1.x, 
             y:p1.y - r2 + r1 
         }, 
         p2 = {  
             x:p1.x + r2 * Math.sin(2 * PI * (1 - drawPercent)), 
             y:p1.y - r2 + r2 * Math.cos(2 * PI * (1 - drawPercent)) 
         }, 
         p3 = { 
             x:p4.x + r1 * Math.sin(2 * PI * (1 - drawPercent)), 
             y:p4.y - r1 + r1 * Math.cos(2 * PI * (1 - drawPercent)) 
         }, 
         path = [ 
             'M', p1.x, ' ', p1.y, 
             'A', r2, ' ', r2, ' 0 ', percent > 0.5 ? 1 : 0, ' 1 ', p2.x, ' ', p2.y, 
             'L', p3.x, ' ', p3.y, 
             'A', r1, ' ', r1, ' 0 ', percent > 0.5 ? 1 : 0, ' 0 ', p4.x, ' ', p4.y, 
             'Z' 
         ].join(''); 
     //用path方法画图形，由两段圆弧和两条直线组成，画弧线的算法
     this.paper.path(path) 
         //填充渐变色
         .attr({"stroke-width":0.5, "stroke":strokecolor, "fill":"90-"+sidecolor+"-"+innercolor}); 

     //显示进度文字 
     $("#"+textId).text(Math.round(percent * totol) ); 
 } 
 
 
$(function(){
	//paperId,strokecolor,textId,sidecolor,innercolor,totol,textNum
	draw("bg","txt","#0099ff","#0066ff","#00ccff",100,65);
	draw("bg1","txt1","#ff3300","#000000","#000000",100,65);
	draw("bg2","txt2","#ffff00","#cc0000","#cc0000",100,65);
	draw("bg3","txt3","#009900","#ffffff","#ffffff",100,65);
	draw("bg4","txt4","#cccccc","#cccccc","#ffffff",100,65);
});
</script> 
</head>
<body class="noimage" style="overflow:hidden;">
	 <div>
		<input type='hidden' name='requestUrl' id='requestUrl' value='<%=request.getContextPath()%>'>
		<%StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);%>
		<input type='hidden' name='currentStaffCode' value='<%=currentStaff.getStaffCode()%>'>
		<span id="CompanyID"></span>
		<table id="list451" ></table>
		<div id="pager451"></div>
        <div id="circleBox">
		 <div id="div">
				 <div id="bg">
				 	 <div id="txt"></div> 
				 </div> 
			
		 </div>

		 <div id="div1">
				 <div id="bg1">
				   <div id="txt1"></div> 
				 </div> 
				 
		 </div>
		 
		  <div id="div2">
				 <div id="bg2">
				  <div id="txt2"></div> 
				 </div> 
				
		 </div>
		 
		  <div id="div3">
				 <div id="bg3">
				 <div id="txt3"></div> 
				 </div> 
				 
		 </div>
		 
		  <div id="div4">
				 <div id="bg4">
				  <div id="txt4"></div> 
				 </div> 
				
		 </div>
    </div> 
	</div>
	<input type=hidden id=requestedForm value="<i18n:message key='menu.workspace.requested_form'/>">
	<input type=hidden id=form_inquiry value="<i18n:message key='menu.monitor.form_inquiry'/>">
	<input type=hidden id=advancy_query value="<i18n:message key='menu.monitor.advancy_query'/>">
	<input type=hidden id=myrequestedForm value="<i18n:message key='menu.workspace.requested_form_subitem1'/>">
	<input type=hidden id=myapprovedForm value="<i18n:message key='menu.workspace.requested_form_subitem2'/>">

	<%
	if (request.getParameter("sort")!=null){
		if (request.getParameter("sort").equals("approval")){
			out.print("<input type=hidden id=sort value='approval'>");
			out.print("<input type=hidden id=css_submit_bold value=''>");
			out.print("<input type=hidden id=css_approval_bold value='font-weight:bolder;'>");
		}
		else{
			out.print("<input type=hidden id=sort value='submit'>");
			out.print("<input type=hidden id=css_submit_bold value='font-weight:bolder;'>");
			out.print("<input type=hidden id=css_approval_bold value=''>");
		}
	}
	else{
		out.print("<input type=hidden id=sort value='submit'>");
		out.print("<input type=hidden id=css_submit_bold value='font-weight:bolder;'>");
		out.print("<input type=hidden id=css_approval_bold value=''>");
	}
	%> 
</body>
<script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/js/NovaJS/loadingList.js"></script>



</html>