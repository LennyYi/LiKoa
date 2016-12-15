<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO" %>
<%@page contentType="text/html;charset=GBK" %> 
<html>
<head>
<META http-equiv=Content-Type content="text/html; charset=GBK">
 <title>Edit Form Type</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
   <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
   function submitForm(){
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      
      var url = encodeURI(encodeURI("<%=request.getContextPath()%>/formTypeAction.it?method=saveFormType&formTypeCode="+document.forms[0].formTypeCode.value.Trim()
                +"&description="+document.forms[0].description.value
                +"&formTypeName="+document.forms[0].formTypeName.value
                +"&saveType="+document.forms[0].saveType.value));
      var  xmlhttp = createXMLHttpRequest();
      var result;
      if(xmlhttp){
           xmlhttp.open('POST',url,false);
           xmlhttp.onreadystatechange = function()
           {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
             }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
      } 
      if(result=="success"){
         window.location = "<%=request.getContextPath()%>/formTypeAction.it?method=listFormType";
      }else{
         alert(result);
      } 
   }
 </script>
<body >
<% 
  String saveType = (String)request.getParameter("saveType");
  FormTypeVO vo = null;
  if(saveType==null){
	  saveType = "new";
  }
  if("new".equals(saveType)){
	  vo = new FormTypeVO();
  }else{
	  vo = (FormTypeVO)request.getAttribute("vo");
  }
%>
<form name="dbForm" action="" method="post">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_formtype.title"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_formtype.formtypecode"/> : 
       </td>
       <td>
        <input type='text' name='formTypeCode' size='4' maxLength='4' title="Form Type Id" required="true" value="<%=(vo.getFormTypeId()==null?"":vo.getFormTypeId())%>"
          <%=(vo.getFormTypeId()==null?"":"readonly")%>>
         (<font color="red">*</font>)(<i18n:message key="housekeeping_resowner.remark"/>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_formtype.formtypename"/> : 
       </td>
       <td>
        <input type='text' name='formTypeName' size='40' length='40' title="Form Type Name" required="true" value="<%=(vo.getFormTypeName()==null?"":vo.getFormTypeName())%>">
        (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_formtype.des"/> : 
       </td>
       <td>
         <textarea rows='3'name='description' cols='40'><%=vo.getDescription()==null?"":vo.getDescription()%></textarea>
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