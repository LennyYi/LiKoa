<%-- 
Task_ID   Author   Modify_Date   Description
IT1002    Robin    03/28/2007    
--%>
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.wkf.util.DataMapUtil,java.util.*,com.aiait.eflow.wkf.vo.WorkFlowItemVO"%>

<html>
<head>
  <title>eFlow - AuditForm</title>
  <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
  <script language="javascript">
      function submitForm(){
      	  var comments = document.all['rejectComments'].value;
          if(comments.Trim()==""){
             alert(mealallowance_fillcomm);
             return;
          }
          comments = formatStringAllChar(comments);
          var formSystemIdStr = window.opener.document.forms[0].all['otRecordIds'].value;
          var url = "<%=request.getContextPath()%>/otFormAction.it?method=rejectOTRecords&"+formSystemIdStr+"&rejectComments="
                    + comments;
          var  xmlhttp = createXMLHttpRequest();
          var result;
          if(xmlhttp){
            xmlhttp.open('POST',url,false);
            xmlhttp.onreadystatechange = function()
            {
              if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 if(result.Trim()=="success"){
                   alert(mealallowance_sucreject)
                   window.opener.document.forms[0].submit();
                   window.close();
                 }else{
                   alert(mealallowance_failreject);
                 }
              }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
        }
      }
   	window.onload=function(){resize(380);}
   	document.oncontextmenu = function() { return false;} 
  </script>
</head>

<body>
<form enctype="multipart/form-data" name="myForm" method="post" > 
   <input type="hidden" name="textareaLimitLength">
   <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD colspan="2" class="tr1"><i18n:message key="mealallowance_title"/></TD>
   </TR>
    <TR>
      <TD width=15% class="tr3"><div align="right"><span class="style1"><i18n:message key="mealallowance_comments"/></span></div></TD>
      <TD width=30% ><font size="2"> 
        <textarea  cols="40" rows="6" name="rejectComments" onKeyDown="javascript:textCounter(this,document.getElementById('textareaLimitLength'),'500')"></textarea>
      </TD>
    </TR>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value='<i18n:message key="button.submit"/>' onclick='submitForm()'>&nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>'>
         <input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick="window.close()">
      </td>
    </tr>
 </TABLE>
 </form>
</body>
</html>