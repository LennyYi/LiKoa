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
     ���������ʣ����Ϊ <b><%=(holidayAnnualVo.getThisYearDays()+holidayAnnualVo.getLastYearDays()-holidayAnnualVo.getUsedDays())%></b> ��
   </td>
  </tr>
    <tr class="liebiao_tou">
            <td align='center' colspan='2'>��������е����(��)</td>
            <td align='center' >ȥ���Զ����������(��)</td>
            <td align='center' >�������õ����(��)</td>
    </tr>
    <tr class="tr_change">
      <td align='center'><%=holidayAnnualVo.getLastYearDays()%>��ȥ��ʣ����٣�</td>
      <td align='center'><%=holidayAnnualVo.getThisYearDays()%>��������٣�</td>
      <td align='center'><%=holidayAnnualVo.getLastAbandonDays()%></td>
      <td align='center'><%=holidayAnnualVo.getUsedDays()%></td>
    </tr>
 </table>
  <br>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
  <tr>
   <td align='center' colspan='2'>
     ���������н������ʣ����Ϊ <b><%=(holidaySickVo.getThisYearDays()-holidaySickVo.getUsedDays())%></b> ��
   </td>
  </tr>
    <tr class="liebiao_tou">
            <td align='center' >�������õĲ���(��)</td>
            <td align='center' >Remark</td>
    </tr>
    <tr class="tr_change">
      <td align='center'><%=(holidaySickVo.getUsedDays())%></td>
      <td >�糬��24�죬��������Ϊ������ʾ�İ����·�����н����������/20.92*������������*40% </td>
    </tr>
 </table>         
 <br>
  <table width="100%"  border="0">
   <tr>
       <td align='left'>��� <a href='<%=request.getContextPath()%>/staffHolidayAction.it?method=listPersonalLeave'>����</a> ���ɿ���������ټ�¼</td>
   </tr>
 </table>
 </body>
 </html>