<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffHolidayVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.*" %>

<html>
<head>
	<title>Personal Holiday</title>
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript">
	</script>
</head>
<%
   StaffHolidayVO  holidayAnnualVo = (StaffHolidayVO)request.getAttribute("holidayAnnualVo");
   StaffHolidayVO  holidaySickVo = (StaffHolidayVO)request.getAttribute("holidaySickVo");
   StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'>Current Location:</font> <font color='#5980BB' family='Times New Roman'> &gt;My Workspace</font> <font color='#5980BB' family='Times New Roman'> &gt; <span class='submenu'>My Holiday</span></font></strong>
 	 </td>
 </tr>
--></table>
 <table width="100%"  border="0">
   <tr class="tr1">
       <td align='center'><B><%=staff.getStaffName()%>,Your Remanent Holidays</B></td>
   </tr>
 </table>
 <br>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
  <tr>
   <td align='center' colspan='4'>
     您的年假所剩天数为 <b><%=(holidayAnnualVo.getThisYearDays()+holidayAnnualVo.getLastYearDays()-holidayAnnualVo.getUsedDays())%></b> 天
   </td>
  </tr>
    <tr class="liebiao_tou">
            <td align='center' colspan='2'>今年可享有的年假(天)</td>
            <td align='center' >去年自动放弃的年假(天)</td>
            <td align='center' >今年已用的年假(天)</td>
    </tr>
    <tr class="tr_change">
      <td align='center'><%=holidayAnnualVo.getLastYearDays()%>（去年剩余年假）</td>
      <td align='center'><%=holidayAnnualVo.getThisYearDays()%>（今年年假）</td>
      <td align='center'><%=holidayAnnualVo.getLastAbandonDays()%></td>
      <td align='center'><%=holidayAnnualVo.getUsedDays()%></td>
    </tr>
 </table>
  <br>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
  <tr>
   <td align='center' colspan='2'>
     您今年的有薪病假所剩天数为 <b><%=(holidaySickVo.getThisYearDays()-holidaySickVo.getUsedDays())%></b> 天
   </td>
  </tr>
    <tr class="liebiao_tou">
            <td align='center' >今年已用的病假(天)</td>
            <td align='center' >Remark</td>
    </tr>
    <tr class="tr_change">
      <td align='center'><%=(holidaySickVo.getUsedDays())%></td>
      <td >如超过24天，超出天数为负数显示的按以下方法扣薪：基本工资/20.92*超出病假天数*40% </td>
    </tr>
 </table>         
 <br>
  <table width="100%"  border="0">
   <tr>
       <td align='left'>点击 <a href='<%=request.getContextPath()%>/staffHolidayAction.it?method=listPersonalLeave'>这里</a> ，可看到您的请假记录</td>
   </tr>
 </table>
 </body>
 </html>