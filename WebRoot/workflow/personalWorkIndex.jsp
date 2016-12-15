<%@ page contentType="text/html; charset=gb2312"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.*"%>
<%@page import="com.aiait.framework.i18n.*"%>
<html>
<head>
  <title>eFlow personal work index</title>
  <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">

<style>
.out {
  position:absolute;
  background:#bbb; 
  margin:10px auto;
  width:400px;
  top:20%;
  left:30%;
  bottom:20%;
  text-align:left;
  }
.in {
  background:#fff; 
  border:1px solid #555;
  padding:10px 5px;
  position:relative;
  top:-5px;
  left:-5px;
  vertical-align:middle;
  }


</style>


</head>
<% 
   StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
   AuthorityHelper authority = AuthorityHelper.getInstance();
%>
<body>
<div class="out"><div class="in">
<table width="100%"  border="0" >
 <tr>
   <td align="center">
  <b><img src="<%=request.getContextPath()%>/images/People_m.gif">&nbsp;<%=currentStaff.getStaffName()%></b>(<%=currentStaff.getLogonId()%>)<br>
  </td>
  </tr>
<tr>
<td>
<table width="100%"  border="0" style="background-color:#EEEEE6" >
     <tr>
       <td align='left'><B><i18n:message key="menu.workspace"/></B></td>
     </tr>
     <tr>
       <td width="100%" align="left">
       <font color="#4F4F4F">
       <%if(CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA)){ %>
             &nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/images/Computer_File_017.gif">&nbsp;<a href="<%=request.getContextPath()%>/formManageAction.it?method=listAvailableForm"><%=I18NMessageHelper.getMessage("personal_index.published_forms",new String[]{(String)request.getAttribute("publishedFormCount")})%></a>
       <%} else {%>
             &nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/images/Computer_File_017.gif">&nbsp;<a href="<%=request.getContextPath()%>/formManageAction.it?method=listAvailableForm"><%=I18NMessageHelper.getMessage("personal_index.can_apply",new String[]{(String)request.getAttribute("publishedFormCount")})%></a>
       <%} %>          
             &nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/images/Computer_File_017.gif">&nbsp;<a href="<%=request.getContextPath()%>/wkfProcessAction.it?method=listPersonalApplyForm"><%=I18NMessageHelper.getMessage("personal_index.have_applied",new String[]{(String)request.getAttribute("appliedCount")})%></a><br><br>
             &nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/images/Computer_File_017.gif">&nbsp;<a href="<%=request.getContextPath()%>/wkfProcessAction.it?method=listWaitForDealForm"><%=I18NMessageHelper.getMessage("personal_index.need_handle",new String[]{(String)request.getAttribute("needHandleFormCount")})%></a>
        </font>
       </td>
     </tr>
   </table>
   </td>
   </tr>
   <tr>
    <td></td>
    </tr>
    <tr>
    <td>
    <%if(authority.checkAuthority(currentStaff.getCurrentRoleId(),ModuleOperateName.MODULE_FORM_INQUIRY,1) || authority.checkAuthority(currentStaff.getCurrentRoleId(),ModuleOperateName.MODULE_OVERDUE_FORM,1)){%>
    <table width="100%"  border="0" style="background-color:#EEEEE6;align:center">
     <tr>
       <td align='left'><B><i18n:message key="menu.monitor"/></B></td>
     </tr>
     <tr>
       <td width="100%" align="left">
       <font color="#4F4F4F">
             &nbsp;&nbsp;&nbsp;&nbsp;
             <%if(authority.checkAuthority(currentStaff.getCurrentRoleId(),ModuleOperateName.MODULE_FORM_INQUIRY,1)){%>
             <img src="<%=request.getContextPath()%>/images/Computer_File_017.gif">&nbsp;<a href="<%=request.getContextPath()%>/wkfProcessAction.it?method=listInquiryForm&needquery=true"><%=I18NMessageHelper.getMessage("personal_index.created",new String[]{(String)request.getAttribute("canQueryFormCount")})%></a>         
             <%}%>
             <%if(authority.checkAuthority(currentStaff.getCurrentRoleId(),ModuleOperateName.MODULE_OVERDUE_FORM,1)){%>
             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             <img src="<%=request.getContextPath()%>/images/Computer_File_017.gif">&nbsp;<a href="<%=request.getContextPath()%>/wkfProcessAction.it?method=listOvertimeForm"><%=I18NMessageHelper.getMessage("personal_index.overdue",new String[]{(String)request.getAttribute("overtimeFormCount")})%></a><br>
             <%}%>
        </font>
       </td>
     </tr>
   </table>
   <%}%>
   </td>
   </tr>
   </table>
   </div></div>
</body>
</html>