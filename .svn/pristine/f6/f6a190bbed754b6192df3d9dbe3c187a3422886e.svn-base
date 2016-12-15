<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.*" %>
<html>
<%
    EmailTemplateVO vo = (EmailTemplateVO) request.getAttribute("template");
    EmailTemplateVO templateFormType = (EmailTemplateVO) request.getAttribute("templateFormType");
    templateFormType = templateFormType == null ? new EmailTemplateVO() : templateFormType;
    String formType = (String) request.getParameter("formType");
    StaffVO currentStaff = (StaffVO) session.getAttribute(CommonName.CURRENT_STAFF_INFOR);
%>
<head>
 <title>Edit Email Template by Form Type</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript">
   function submitForm() {
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      frm.action = "<%=request.getContextPath()%>/emailTemplateAction.it?method=saveTemplateByFormType";
      frm.submit();
   }
   
   function deleteForm() {
       var frm = document.forms[0];
       if (confirm("Are you sure to delete the template?")) {
           frm.action = "<%=request.getContextPath()%>/emailTemplateAction.it?method=deleteTemplateByFormType";
           frm.submit();
       }
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
  
  function InsertTag(tagbegin,tagend) {  
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
</head>

<body>
<form name="groupForm" action="" method="post">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td align='center' colspan='2' class="tr1">
          <b><i18n:message key="housekeeping_emailtemplate.title_formtype"/></b>
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.id"/> : 
       </td>
       <td>
           <%="" + vo.getId()%><input type="hidden" name="id" value="<%="" + vo.getId()%>">
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.name"/> : 
       </td>
       <td><%=(vo.getName()==null?"":vo.getName())%></td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="common.form_type"/> : 
       </td>
       <td>
         <select name="formType">
          <%
          Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
          if (typeList != null && !typeList.isEmpty()) {
              Iterator it = typeList.iterator();
              while (it.hasNext()) {
                  FormTypeVO typeVo = (FormTypeVO)it.next();
                  if (AuthorityHelper.getInstance().checkAuthorityByFormType(currentStaff.getCurrentRoleId(), ModuleOperateName.MODULE_FORM_INQUIRY, typeVo.getFormTypeId())) {
                  %>
                  <option value="<%=typeVo.getFormTypeId()%>" <%=(formType != null && (typeVo.getFormTypeId()).equals(formType)) ? "selected" : ""%>><%=typeVo.getFormTypeName()%></option>
                  <%  
                  }
              }
          }
          %>
         </select>
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.subject"/> : 
       </td>
       <td>
        <input type='text' name='emailSubject' size='40' length='40' title="Email Subject" required="true" value="<%=(templateFormType.getEmailSubject() == null ? "" : templateFormType.getEmailSubject())%>">
        (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.content"/> : 
       </td>
       <td>
           <textarea name="emailContent" rows="10" cols="50"><%=templateFormType.getEmailContent() != null ? StringUtil.replaceInString(templateFormType.getEmailContent(),"&nbsp;","&amp;nbsp;") : ""%></textarea>
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
           <%=vo.convertActionToName()%>
       </td>
     </tr>
     <tr>
       <td align="right" class="tr3">
         <i18n:message key="housekeeping_emailtemplate.des"/> : 
       </td>
       <td>
           <textarea name="description" rows="5" cols="50"><%=templateFormType.getDescription() != null ? templateFormType.getDescription() : ""%></textarea>
       </td>
     </tr>
     <tr>
       <td align="center" colspan="2">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="delBtn" value='<i18n:message key="button.delete"/>' onclick="deleteForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
  </table>
</form>
</body>
</html>