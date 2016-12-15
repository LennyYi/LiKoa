<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.common.CommonName" %>
<html>
<head>
 <title>Email Template Param Select</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript">
   function submitForm(){
      var frm = document.forms[0];
      var selectParam = frm.paramValue.value;
      window.returnValue=selectParam;
      window.close();	
   }
 </script>
<body>

<form name="groupForm" action="" method="post">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr>
       <td align='center' colspan='2' class="tr1">
          <b> <i18n:message key="emailparamsel.title"/>
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
        <i18n:message key="emailparamsel.param"/> : 
       </td>
       <td>
          <select name="paramValue">
            <option value="<%=CommonName.EMAIL_TEMPLATE_PARAM_REQUEST_NO%>">Request no</option>
            <option value="<%=CommonName.EMAIL_TEMPLATE_PARAM_REQUESTED_BY%>">Requested By</option>
            <option value="<%=CommonName.EMAIL_TEMPLATE_PARAM_HANDLE_BY%>">Current Handle By</option>
            <option value="<%=CommonName.EMAIL_TEMPLATE_PARAM_RECEIVE_STAFF%>">Email Receive Staff</option>
            <option value="<%=CommonName.EMAIL_TEMPLATE_PARAM_CURRENT_DATE%>">System Current Date</option>
            <option value="<%=CommonName.EMAIL_TEMPLATE_PARAM_COMMENTS%>">Handle Comments</option>
            <option value="<%=CommonName.EMAIL_TEMPLATE_PARAM_FORM_SYSTEM_ID%>">Form System Id</option>
          </select>
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick="javascript:window.close()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>