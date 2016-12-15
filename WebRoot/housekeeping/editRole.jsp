<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.RoleVO" %>
<html>
<head>
 <title>Edit Role</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
   function submitForm(){
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      //frm.action = "<%=request.getContextPath()%>/roleAction.it?method=saveRole";
      //frm.submit();
      var url = "<%=request.getContextPath()%>/roleAction.it?method=saveRole&roleId="+document.forms[0].roleId.value.Trim()
                +"&roleName="+document.forms[0].roleName.value.Trim()
                +"&remark="+document.forms[0].remark.value.Trim()
                +"&saveType="+document.forms[0].saveType.value;
      
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
         window.location = "<%=request.getContextPath()%>/roleAction.it?method=listRole";
      }else{
         alert(result);
      } 
   }
 </script>
<body>
<% 
  String saveType = (String)request.getParameter("saveType");
  RoleVO vo = null;
  if(saveType==null){
	  saveType = "new";
  }
  if("new".equals(saveType)){
	  vo = new RoleVO();
  }else{
	  vo = (RoleVO)request.getAttribute("role");
  }
%>
<form name="systemForm" action="" method="post">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr>
       <td align='center' colspan='2' class="tr1">
          <b> <i18n:message key="housekeeping_role.title"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_role.roleid"/> : 
       </td>
       <td>
        <input type='text' name='roleId' size='4' maxLength='4' title="Role Id" required="true" value="<%=(vo.getRoleId()==null?"":vo.getRoleId())%>"
          <%=(vo.getRoleId()==null?"":"readonly")%>>(<font color='red'>*</font>)
         (<i18n:message key="housekeeping_approvergroup.remark"/>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_role.rolename"/> : 
       </td>
       <td>
        <input type='text' name='roleName' size='40' maxLength='50' title="Role Name" required="true" value="<%=(vo.getRoleName()==null?"":vo.getRoleName())%>">
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_role.remark"/> : 
       </td>
       <td>
         <textarea name="remark" rows="5" cols="15"><%=(vo.getRemark()==null?"":vo.getRemark().trim())%></textarea>
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