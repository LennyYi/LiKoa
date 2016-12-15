<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.*" %>
<%@page import="java.util.*" %>
<html>
<head>
 <title>Edit Exchange Rate</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
   <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
	function submitRate() {
		var frm = document.forms[0];
		if (formValidate(frm) == false) return;
		frm.action = "<%=request.getContextPath()%>/exchangeRateAction.it?method=saveRate";
		frm.submit();
	}
 </script>
<body>
<% 
  String editType = request.getParameter("editType");
  CurrencyExVO vo = (CurrencyExVO) request.getAttribute("vo");
  if (vo == null) {
      vo = new CurrencyExVO();
  }
%>
<form name="dbForm" action="" method="post">
  <input type="hidden" name="editType" value="<%=editType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_exrate.title"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_exrate.code"/> : 
       </td>
       <td>
         <input type='text' name='code' size='10' maxLength='10' required="true" value="<%=vo.getCurrencyCode() == null ? "" : vo.getCurrencyCode()%>"
          <%=vo.getCurrencyCode() == null ? "" : "readonly"%>>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_exrate.name"/> : 
       </td>
       <td>
         <input type='text' name='name' size='10' maxLength='10' required="true" value="<%=vo.getCurrencyName() == null ? "" : vo.getCurrencyName()%>">
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_exrate.year"/> : 
       </td>
       <td>
         <input type='text' name='year' size='10' maxLength='4' required="true" value="<%=vo.getYear() == 0 ? "" : Integer.toString(vo.getYear())%>"
         onKeyPress='if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false'
          <%=vo.getCurrencyCode() == null ? "" : "readonly"%>>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_exrate.month"/> : 
       </td>
       <td>
         <input type='text' name='month' size='10' maxLength='2' required="true" value="<%=vo.getMonth() == 0 ? "" : Integer.toString(vo.getMonth())%>"
         onKeyPress='if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false'
          <%=vo.getCurrencyCode() == null ? "" : "readonly"%>>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_exrate.rate"/> : 
       </td>
       <td>
         <input type='text' name='rate' size='10' maxLength='10' required="true" value="<%=vo.getExchangeRate() == 0 ? "" : Double.toString(vo.getExchangeRate())%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitRate()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>