<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.EmailTemplateVO,com.aiait.eflow.util.StringUtil" %>
<html>
<% 
  EmailTemplateVO vo = (EmailTemplateVO)request.getAttribute("template");
%>
<head>
 <title>Edit Email Template</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript">
   function submitForm(){
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      frm.action = "<%=request.getContextPath()%>/emailTemplateAction.it?method=saveTemplate";
      frm.submit();
   }
   function bindAction(){
      var url = "<%=request.getContextPath()%>/emailTemplateAction.it?method=showBindActionPage&id=<%=vo.getId()%>&appliedAction="+document.all['appliedAction'].value;
      var actionIds = showModalDialog(url,window,'dialogWidth:500px; dialogHeight:400px;help:0;status:0;resizeable:1;');
      if(actionIds){
        document.all['appliedAction'].value=actionIds;
        var name = convertActionName(actionIds);
        document.getElementById("actionName").innerHTML = name;
      }
   }
   
   function convertActionName(obj){
      var resultName = "";
      if(obj.indexOf("00")>-1){
        resultName = resultName + "DrawOut &nbsp;";
      }
      if(obj.indexOf("01")>-1){
        resultName = resultName + "Submit &nbsp;";
      }
      if(obj.indexOf("02")>-1){
        resultName = resultName + "Withdraw &nbsp;";
      }
      if(obj.indexOf("03")>-1){
        resultName = resultName + "Approved &nbsp;";
      }
      if(obj.indexOf("04")>-1){
        resultName = resultName + "Rejected &nbsp;";
      }
      if(obj.indexOf("05")>-1){
        resultName = resultName + "Completed &nbsp;";
      }
      if(obj.indexOf("06")>-1){
        resultName = resultName + "Invited Expert &nbsp;";
      }
      return resultName;
   }
   
   function runCode(obj) {
	var winname = window.open('', "_blank", '');
	winname.document.open('text/html', 'replace');
	winname.document.write(document.getElementById(obj).value);
	winname.document.close();
  }
  function selectParam(){
    var url = "<%=request.getContextPath()%>/housekeeping/emailParamSelect.jsp?tempDate="+Math.random()*100000,window;
    var paramValue = showModalDialog(url,window,'dialogWidth:400px; dialogHeight:160px;help:0;status:0;resizeable:1;');
    if(paramValue!="" && paramValue!=undefined){
      InsertTag("",paramValue+" ");
    }
  }
  function InsertTag(tagbegin,tagend)  
 {  
   if((document.selection)&&(document.selection.type == "Text"))
   {  
     var range = document.selection.createRange();  
     var ch_text=range.text;  
     range.text = tagbegin + ch_text + tagend;  
   }else{  
       document.all['emailContent'].value=tagbegin+document.all['emailContent'].value+tagend;  
       document.all['emailContent'].focus();  
   }  
  }
 </script>
<body>

<form name="groupForm" action="" method="post">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr>
       <td align='center' colspan='2' class="tr1">
          <b> <i18n:message key="housekeeping_emailtemplate.title"/>
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.id"/> : 
       </td>
       <td>
        <input type='text' name='id' size='4' maxLength='4' title="Id" required="true" value="<%=""+vo.getId()%>"
          readonly>
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.name"/> : 
       </td>
       <td>
        <input type='text' name='name' size='40' length='40' title="Name" required="true" value="<%=(vo.getName()==null?"":vo.getName())%>">
        (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.subject"/> : 
       </td>
       <td>
        <input type='text' name='emailSubject' size='40' length='40' title="Email Subject" required="true" value="<%=(vo.getEmailSubject()==null?"":vo.getEmailSubject())%>">
        (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.content"/> : 
       </td>
       <td>
           <textarea name="emailContent" rows="10" cols="50"><%=vo.getEmailContent()!=null?StringUtil.replaceInString(vo.getEmailContent(),"&nbsp;","&amp;nbsp;"):""%></textarea>         
           <input type="button" value='<i18n:message key="housekeeping_emailtemplate.addparam"/>' name="addParamBtn" onclick="selectParam()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           <input type="button" value='<i18n:message key="housekeeping_emailtemplate.preview"/>' name="previewBtn" onclick="runCode('emailContent')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.appliedaction"/> : 
       </td>
       <td>
           <div id="actionName"><%=vo.convertActionToName()%>&nbsp;</div>
           <input type="hidden" name="appliedAction" value="<%=(vo.getAppliedAction()==null?"":vo.getAppliedAction())%>">
           <input type="button" value='<i18n:message key="housekeeping_emailtemplate.bindaction"/>' name="bindActionBtn" onclick="bindAction()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.des"/> : 
       </td>
       <td>
           <textarea name="description" rows="5" cols="50"><%=vo.getDescription()!=null?vo.getDescription():""%></textarea>         
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>