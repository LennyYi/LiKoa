<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.housekeeping.vo.CheckInoutVO" %>
<html>
<head>
  <title>eFlow - list staff check records</title>
  <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
  <script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
   <script language="javascript">
  window.onload=function(){resize(500);}
 </script>
 </head>
 <%
   CheckInoutVO checkVo = (CheckInoutVO)request.getAttribute("checkVo");
   StaffVO staff = (StaffVO)request.getAttribute("staff");
 %>
 <body>
<form name="searchForm" method="post" action="">
<input type="hidden" name="otRecordIds">
<TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1">Check In/Out Records - Staff: <%=staff.getLogonId()%> <%=staff.getStaffName()%></TD>
   </TR>
    </TABLE>
  </form>
   <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
    <tr class="liebiao_tou">
            <td align='center' >Date / 日期</td>
            <td align='center' >Arrival / 到达时间</td>
            <td align='center' >Leave / 离开时间</td>
    </tr>
    <%if(checkVo!=null){%>
    <tr>
      <td align='center'><%=checkVo.getOtDate()%></td>
      <td align='center'><%=checkVo.getArrivalTime()%></td>
      <td align='center'><%=checkVo.getLeaveTime()%></td>
    </tr>
    <%}else{%>
     <tr>
       <td colspan='3' align='center'>No check in/out record</td>
     </tr>
    <%}%>
 </table>
 <TABLE WIDTH=100% border='0'>
    <tr>
      <td  align='center'>
         <input type="submit" name="searchBtn" value="Close" onclick='javascript:window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
    </td>
    </tr>
    </table>
 </body>
 </html>
