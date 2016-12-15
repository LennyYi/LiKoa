<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.SystemOwnerVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.*" %>
<html>
<head>
 <title>Edit System Owner</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
   <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
   function submitForm(){
      var frm = document.forms[0];
      if(formValidate(frm)==false) return;
      //frm.action = "<%=request.getContextPath()%>/systemOwnerAction.it?method=saveSystemOwner";
      //frm.submit();
      var systemName = document.forms[0].systemName.value.Trim();
      systemName = formatStringAllChar(systemName);
      var url = "<%=request.getContextPath()%>/systemOwnerAction.it?method=saveSystemOwner&systemId="+document.forms[0].systemId.value.Trim()
                +"&systemName="+systemName
                +"&staffCode="+document.forms[0].staffCode.value
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
         window.location = "<%=request.getContextPath()%>/systemOwnerAction.it?method=listSystemOwner";
      }else{
         alert(result);
      } 
   }
 </script>
<body>
<% 
  String saveType = (String)request.getParameter("saveType");
  SystemOwnerVO vo = null;
  if(saveType==null){
	  saveType = "new";
  }
  if("new".equals(saveType)){
	  vo = new SystemOwnerVO();
  }else{
	  vo = (SystemOwnerVO)request.getAttribute("vo");
  }
  Collection staffList = StaffTeamHelper.getInstance().getStaffList();
  Iterator staffIt = staffList.iterator();
%>
<form name="systemForm" action="" method="post">
  <input type="hidden" name="saveType" value="<%=saveType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='2'>
          <b> <i18n:message key="housekeeping_systemowner.subtitle"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_systemowner.systemid"/> : 
       </td>
       <td>
        <input type='text' name='systemId' size='4' maxLength='4' title="System Id" required="true" value="<%=(vo.getSystemId()==null?"":vo.getSystemId())%>"
          <%=(vo.getSystemId()==null?"":"readonly")%>>
         (<font color="red">*</font>)(<i18n:message key="housekeeping_resowner.remark"/>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_systemowner.systemname"/> : 
       </td>
       <td>
        <input type='text' name='systemName' size='40' length='40' title="System Name" required="true" value="<%=(vo.getSystemName()==null?"":vo.getSystemName())%>">
        (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_systemowner.systemowner"/> : 
       </td>
       <td>
         <select name="staffCode">
           <%
             while(staffIt.hasNext()){
            	 StaffVO staff = (StaffVO)staffIt.next();
            	 out.print("<option value='" + staff.getStaffCode()+"'");
            	 if(vo.getSystemStaffCode()!=null && vo.getSystemStaffCode().equals(staff.getStaffCode())){
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