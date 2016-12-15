<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.housekeeping.vo.OTRecordVO" %>
<html>
<head>
  <title>eForm - waitingStaffList</title>
  <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
    <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
  <script language="javascript">
     function otConfirm() {
         if (checkSelect('staffCode') <= 0) {
             alert("You have not selected any record to confirm!");
             return;
         }
         if (confirm("Are you sure to confirm the selected records?")) {
             var staffCodes = getTableSelectRecordStr("staffCode", "staffCode");
             var url = "<%=request.getContextPath()%>/otFormAction.it?method=confirmOTRecordsByStaff&" + staffCodes;
             //alert(url);
             var  xmlhttp = createXMLHttpRequest();
             var result;
             document.all['confirmBtn'].disabled = "true";
             if (xmlhttp) {
                 xmlhttp.open('POST', url, false);
                 xmlhttp.onreadystatechange = function() {
                     if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                         result = xmlhttp.responseText;
                         if (result.Trim() == "success") {
                             alert("Successfully confirm the selected records!")
                             document.forms[0].submit();
                         } else {
                             alert("Failed to confirm the selected records!");
                         }
                         document.all['confirmBtn'].disabled = "";
                     }
                 };
                 xmlhttp.setRequestHeader("If-Modified-Since", "0");
                 xmlhttp.send(null);
             }
         }
     }
     
     function otReject() {
         if (checkSelect('staffCode') <= 0) {
             alert("You have not selected any record to reject!");
             return;
         }
         if (confirm("Are you sure to reject the selected records?")) {
             var staffCodes = getTableSelectRecordStr("staffCode", "staffCode");
             var url = "<%=request.getContextPath()%>/otFormAction.it?method=rejectOTRecordsByStaff&" + staffCodes;
             //alert(url);
             var  xmlhttp = createXMLHttpRequest();
             var result;
             document.all['rejectBtn'].disabled = "true";
             if (xmlhttp) {
                 xmlhttp.open('POST', url, false);
                 xmlhttp.onreadystatechange = function() {
                     if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                         result = xmlhttp.responseText;
                         if (result.Trim() == "success") {
                             alert("Successfully reject the selected records!")
                             document.forms[0].submit();
                         } else {
                             alert("Failed to reject the selected records!");
                         }
                         document.all['rejectBtn'].disabled = "";
                     }
                 };
                 xmlhttp.setRequestHeader("If-Modified-Since", "0");
                 xmlhttp.send(null);
             }
         }
     }
  </script>
 </head>
 <%
    String requestStaffCode = (String)request.getParameter("requestStaffCode");
    Collection waitingStaffList = (ArrayList)request.getAttribute("waitingStaffList");
 %>
 <body>
 <table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="meal_form_confirm.location"/></font></strong>
 	 </td>
 </tr>
--></table>
<form name="searchForm" method="post" action="<%=request.getContextPath()%>/otFormAction.it?method=enterWaitingStaff">
  <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
    <TR align="center" >
      <TD height="25" colspan="4" class="tr1"><i18n:message key="meal_form_confirm.title"/></TD>
    </TR>
    <tr>
      <td colspan='4' align='left'>
        <input type="button" name="confirmBtn" value="Confirm" onclick='otConfirm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
           onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
        <input type="button" name="rejectBtn" value="Reject" onclick='otReject()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
           onmouseout="this.className='btn3_mouseout'" onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
      </td>
    </tr>
  </TABLE>
  <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
   <tr class="liebiao_tou">
     <td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this,'staffCode')"></td>
     <td align='center'><i18n:message key="common.team"/></td>
     <td align='center'><i18n:message key="meal_form_confirm.staffname"/></td>
     <td align='center'><i18n:message key="meal_form_confirm.otrecordsnum"/></td>
     <td align='center'><i18n:message key="meal_form_confirm.action"/></td>
   </tr>
   <%
     if (waitingStaffList != null) {
         StaffTeamHelper staffTeamHelper = StaffTeamHelper.getInstance();
    	 Iterator it = waitingStaffList.iterator();
    	 while (it.hasNext()) {
    		 OTRecordVO vo = (OTRecordVO)it.next();
   %>
     <tr class="tr_change" align='center'>
     <td><input type="checkbox" name="staffCode" value="<%=vo.getStaffCode()%>"></td>
     <td><%=staffTeamHelper.getTeamNameByCode(staffTeamHelper.getStaffByCode(vo.getStaffCode()).getTeamCode())%></td>
     <td><%=staffTeamHelper.getStaffNameByCode(vo.getStaffCode())%></td>
     <td><%=vo.getOtRecordNum()%></td>
     <td><a href="<%=request.getContextPath()%>/otFormAction.it?method=enterWaitingOTList&requestStaffCode=<%=vo.getStaffCode()%>">Detail Meal Allowance Records</a></td>
     </tr>
   <%
         }
     } else {
   %>
    <tr class="liebiao_nr2">
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
     </tr>
   <%}%>
  </table>
 </form>
 </body>
 </html>
