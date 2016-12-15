<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.ApproverGroupVO" %>
<html>
<head>
 <title>Edit Approver Group</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
   function submitForm(){
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      //frm.action = "<%=request.getContextPath()%>/approverGroupAction.it?method=saveApproverGroup";
      //frm.submit();
      var saveGroupName = formatStringAllChar(document.forms[0].groupName.value.Trim());
      var saveDescription = formatStringAllChar(document.forms[0].description.value.Trim());
       var url = "<%=request.getContextPath()%>/approverGroupAction.it?method=saveApproverGroup&groupId="+document.forms[0].groupId.value.Trim()
                +"&groupName="+saveGroupName
                +"&groupType="+document.forms[0].groupType.value.Trim()
                +"&description="+saveDescription
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
         window.location = "<%=request.getContextPath()%>/approverGroupAction.it?method=listApproverGroup";
      }else{
         alert(result);
      } 
   }
 </script>
<body>
<% 
  String saveType = (String)request.getParameter("saveType");
  ApproverGroupVO vo = null;
  if(saveType==null){
	  saveType = "new";
  }
  if("new".equals(saveType)){
	  vo = new ApproverGroupVO();
  }else{
	  vo = (ApproverGroupVO)request.getAttribute("approverGroup");
  }

%>
<form name="groupForm" action="" method="post">
   <input type="hidden" name="have">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_approvergroup.subtitle"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_approvergroup.groupid"/> : 
       </td>
       <td>
        <input type='text' name='groupId' size='4' maxLength='2' title="Approver Group Id" required="true" value="<%=(vo.getGroupId()==null?"":vo.getGroupId())%>"
          <%=(vo.getGroupId()==null?"":"readonly")%>>(<font color='red'>*</font>)
         (<i18n:message key="housekeeping_approvergroup.remark"/>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_approvergroup.groupname"/> : 
       </td>
       <td>
        <input type='text' name='groupName' size='40' maxLength='40' title="Approver Group Name" required="true" value="<%=(vo.getGroupName()==null?"":vo.getGroupName())%>">
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_approvergroup.grouptype"/> : 
       </td>
       <td>
         <select name="groupType">
           <option value="01" <%=(vo.getGroupType()!=null && "01".equals(vo.getGroupType())?"selected":"")%>>Head Office</option>
           <option value="02" <%=(vo.getGroupType()!=null && "02".equals(vo.getGroupType())?"selected":"")%>>Branch</option>
           <option value="03" <%=(vo.getGroupType()!=null && "03".equals(vo.getGroupType())?"selected":"")%>>Sub Branch</option>
         </select>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_approvergroup.des"/> : 
       </td>
       <td>
           <textarea name="description" rows="3" cols="20" onKeyDown="javascript:textCounter(this,document.getElementById('have'),50);" onKeyUp="javascript:textCounter(this,document.getElementById('have'),50);"><%=vo.getDescription()!=null?vo.getDescription():""%></textarea>         
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