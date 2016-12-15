<%-- 
NO  Task_ID    Author     Modify_Date    Description
1   IT0958     Robin.Hou   10/31/2007    Initial.(Expert can give the advice during processing form)
--%>
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.wkf.util.DataMapUtil,java.util.*,com.aiait.eflow.wkf.vo.WorkFlowItemVO"%>
<%
  String requestNo = (String)request.getParameter("requestNo");
%>
<html>
<head>
  <title>eFlow - Expert Advice</title>
  <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <script language="javascript">
      function submitForm(){
        var comments = document.getElementById('handleComments').value;       
        if(comments.Trim()==""){
            alert(expertadvice_fillremark);
            document.getElementById('handleComments').focus();

            return;
        }
        comments = formatStringAllChar(comments);
        if(!confirm("Are you sure to save the advice for form? (<%=requestNo%>)")) {
          return;
        }
        var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=saveExpertAdvice&requestNo=<%=requestNo%>";
        url = url + "&handleComments=" + encodeURI(encodeURI(comments))+"&nonIE=1";

        document.forms[0].target = '_parent';
        document.forms[0].action = url;
        document.forms[0].submit();
      }
  </script>
</head>

<body>
<form enctype="multipart/form-data" name="myForm" method="post" > 
   <input type="hidden" name="requestNo" value="<%=requestNo%>">
   <input type="hidden" name="textareaLimitLength">
   <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD colspan="2" class="tr1">Expert advice for <%=requestNo%></TD>
   </TR>
   <TR> 
      <TD width="80px" class="tr3"><div align="right"><span class="style1">Request No </span></div></TD>
      <TD><font size="2"><%=requestNo%></font></TD>
    </TR>
    <TR>
      <TD width="80px" class="tr3"><div align="right"><span class="style1">Attachment </span></div></TD>
      <TD><font size="2"><input type="file" name="path" size='20'></font></TD>
    </TR>
    <TR>
      <TD width="80px" class="tr3"><div align="right"><span class="style1">Remark </span></div></TD>
      <TD><font size="2">
        <textarea cols="50" rows="8" id="handleComments" name="handleComments" onKeyDown="javascript:textCounter(this,document.getElementById('textareaLimitLength'),'500')"></textarea>
        </font>
      </TD>
    </TR>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value='Submit' onclick='submitForm()'>&nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>'>
      </td>
    </tr>
 </TABLE>
 <input type=hidden name="nonIE" id="nonIE" value="1">
 </form>
</body>
</html>