<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.*" %>
<%@page import="java.util.*" %>
<html>
<head>
 <title>Edit Bank</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
	function submitCode() {
		var frm = document.forms[0];
		if (formValidate(frm) == false) return;
		frm.action = "<%=request.getContextPath()%>/bankAction.it?method=saveBank";
		frm.submit();
	}
 </script>
<body>
<% 
  String editType = request.getParameter("editType");
  BankVO vo = (BankVO) request.getAttribute("Bank");
  if (vo == null) {
	  vo = new BankVO();
  }
%>
<form name="dbForm" action="" method="post">
  <input type="hidden" name="editType" value="<%=editType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_bank.title"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_bank.bankcode"/> : 
       </td>
       <td>
         <input type='text' name='bankcode' size='10' maxLength='10'
          required="true" value="<%=vo.getBankCode() == null ? "" : vo.getBankCode()%>" <%=vo.getBankCode() == null ? "" : "readonly"%>/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_bank.bankname"/> : 
       </td>
       <td>
         <input type='text' name='bankname' size='30' maxLength='50' required="true" value="<%=vo.getBankName() == null ? "" : vo.getBankName()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_bank.accountcode"/> : 
       </td>
       <td>
         <input type='text' name='accountcode' size='30' maxLength='50' required="true" value="<%=vo.getAccountCode() == null ? "" : vo.getAccountCode()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_bank.accountname"/> : 
       </td>
       <td>
         <input type='text' name='accountname' size='30' maxLength='50' required="true" value="<%=vo.getAccountName() == null ? "" : vo.getAccountName()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_supplier.city"/> : 
       </td>
       <td>
         <input type='text' name='city' size='30' maxLength='50' required="true" value="<%=vo.getCity() == null ? "" : vo.getCity()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_contract.remark"/> : 
       </td>
       <td>
         <input type='text' name='type' size='30' maxLength='50' value="<%=vo.getType() == null ? "" : vo.getType()%>"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         SUN code : 
       </td>
       <td>
         <input type='text' name='sun_code' size='30' maxLength='50' required="true" value="<%=vo.getSunCode() == null ? "" : vo.getSunCode()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_bank.is_default"/> : 
       </td>
       <td>
         <input type='checkbox' name='is_default' <%=vo.getIsDefault()==0 ? "" :"checked" %>/>
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
       </td>
     </tr>
  </table>
</form>
</body>
</html>