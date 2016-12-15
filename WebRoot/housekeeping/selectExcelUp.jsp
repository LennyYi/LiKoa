<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%
    String orgId = request.getParameter("orgId");
    orgId = orgId == null ? "" : orgId;
%>
<html>
   <title><i18n:message key="button.selectFile"/></title>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <script language="javascript">
      function submitForm(){
         var path = document.all['path'].value;
         var action = "";      
         if(path.Trim()==""){
           alert("Please select file!");
           document.all['path'].focus();
           return;
         }
         switch(window.opener.document.getElementsByTagName('title')[0].innerText){
         case "Bank":
             action = "bankAction.it?method=importExcelFile&temp=<%=System.currentTimeMillis()%>";
             break;
         case "Building":
           action = "buildingAction.it?method=importExcelFile&temp=<%=System.currentTimeMillis()%>";
           break;
         case "Contract":
           action = "contractAction.it?method=importExcelFile&temp=<%=System.currentTimeMillis()%>";
           break;
         case "Finance Code":
           action = "financeCodeAction.it?method=importExcelFile&temp=<%=System.currentTimeMillis()%>&flg=<%=(String)request.getAttribute("flg")%>&orgId=<%=orgId%>";
           break;
         case "Supplier":
           action = "supplierAction.it?method=importExcelFile&temp=<%=System.currentTimeMillis()%>";
          break;
         case "Personal Applied Form List":
           action = "formManageAction.it?method=importExcelFile&temp=<%=System.currentTimeMillis()%>";
          break; 
         }
         document.forms[0].action = "<%=request.getContextPath()%>/" + action;
         document.forms[0].submit();
         window.close();
     }
  </script>
 <body style="margin:0">
 <br>
<form enctype="multipart/form-data" method="post" action="" target="_blank">
<table width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>
<tr>
<td align='right' class="tr3">
<i18n:message key="button.selectFile"/> :
</td>
<td align='left'>
<input type="file" name="path" size="38" />
</td>
</tr>
<tr>
 <td colspan='2' align='center'>
   <input type="button"  onclick='submitForm()' name="submitBtn" value='<i18n:message key="button.upload"/>'>&nbsp;&nbsp;
   <input type="button"  onclick='javascript:window.close()' name="canBtm" value='<i18n:message key="button.cancel"/>'>
 </td>
</tr>
</table>
</form>
</body>
</html>