<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.DBOwnerVO, com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.*" %>
<html>
<head>
 <title>Edit DB Owner</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
   <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
   function submitForm(){
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      //frm.action = "<%=request.getContextPath()%>/dbOwnerAction.it?method=saveDBOwner";
      //frm.submit();
      var saveDbName = formatStringAllChar(document.forms[0].dbName.value.Trim());
       var url = "<%=request.getContextPath()%>/dbOwnerAction.it?method=saveDBOwner&dbId="+document.forms[0].dbId.value.Trim()
                +"&dbName="+saveDbName
                +"&dbStaffCode="+document.forms[0].dbStaffCode.value
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
         window.location = "<%=request.getContextPath()%>/dbOwnerAction.it?method=listDBOwner";
      }else{
         alert(result);
      } 
   }
 </script>
<body>
<% 
  String saveType = (String)request.getParameter("saveType");
  DBOwnerVO vo = null;
  if(saveType==null){
	  saveType = "new";
  }
  if("new".equals(saveType)){
	  vo = new DBOwnerVO();
  }else{
	  vo = (DBOwnerVO)request.getAttribute("vo");
  }
  Collection staffList = StaffTeamHelper.getInstance().getStaffList();
  Iterator staffIt = staffList.iterator();
%>
<form name="dbForm" action="" method="post">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_resowner.subtitle"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_resowner.resid"/> : 
       </td>
       <td>
        <input type='text' name='dbId' size='4' maxLength='4' title="Resource Id" required="true" value="<%=(vo.getDBId()==null?"":vo.getDBId())%>"
          <%=(vo.getDBId()==null?"":"readonly")%>>
         (<font color="red">*</font>)(<i18n:message key="housekeeping_resowner.remark"/>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_resowner.resname"/> : 
       </td>
       <td>
        <input type='text' name='dbName' size='40' length='40' title="Resource Name" required="true" value="<%=(vo.getDBName()==null?"":vo.getDBName())%>">
        (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_resowner.resowner"/> : 
       </td>
       <td>
         <select name="dbStaffCode">
           <%
             while(staffIt.hasNext()){
            	 StaffVO staff = (StaffVO)staffIt.next();
            	 out.print("<option value='" + staff.getStaffCode()+"'");
            	 if(vo.getDBStaffCode()!=null && vo.getDBStaffCode().equals(staff.getStaffCode())){
            		 out.print(" selected ");
            	 }
            	 out.print(">"+staff.getStaffName()+"</option>");
             }
           %>
         </select>
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