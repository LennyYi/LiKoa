<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
	import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.CompanyHelper"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.*, com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%
	Collection staffList = (Collection) request.getAttribute("eflowuserList");
%>

<html>
<head>
	<title>Export form list to excel</title>
	<meta Content-Disposition="attachment; filename=formlist.xls">
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">
<table width="100%" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align='center' ><strong><i18n:message key="housekeeping_user.status"/></strong></td>
		<td align='center' ><strong><i18n:message key="common.company"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_user.staffcode"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_user.staffname"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_user.logonid"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_user.team"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_user.title"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_user.usertype"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_approvergroup.groupname"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_role.rolename"/></strong></td>
	</tr>
	<%
	    if (staffList != null) {
					Iterator staffIt = staffList.iterator();
					int i = 1;
					while (staffIt.hasNext()) {
					    EflowStaffVO staff = (EflowStaffVO) staffIt.next();
					    // Get approver groups
					    String approverGroups = "";
					    Iterator groupIt = staff.getApproverGroups().iterator();
					    boolean start = true;
					    while (groupIt.hasNext()) {
					        ApproverGroupVO approverGroup = (ApproverGroupVO) groupIt.next();
					        if (start) {
					            approverGroups = approverGroup.getGroupName();
					            start = false;
					        } else {
					            approverGroups += ", " + approverGroup.getGroupName();
					        }
					    }
					    // Get roles
					    String roles = "";
					    Iterator roleIt = staff.getRoles().iterator();
					    start = true;
					    while (roleIt.hasNext()) {
					        RoleVO role = (RoleVO) roleIt.next();
					        if (start) {
					            roles = role.getRoleName();
					            start = false;
					        } else {
					            roles += ", " + role.getRoleName();
					        }
					    }
	%>
	<tr>
		<td><%=staff.getStatus()%>&nbsp;&nbsp;</td>
		<td><%=staff.getOrgName()%>&nbsp;&nbsp;</td>
		<td><%=staff.getStaffCode()%>&nbsp;&nbsp;</td>
		<td><%=staff.getStaffName()==null?"":staff.getStaffName()%>&nbsp;&nbsp;</td>
		<td><%=staff.getLogonId()==null?"":staff.getLogonId()%>&nbsp;&nbsp;</td>
		<td><%=staff.getTeamName()==null?"":staff.getTeamName()%>&nbsp;&nbsp;</td>
		<td><%=staff.getTitleName()==null?"":staff.getTitleName()%>&nbsp;&nbsp;</td>
		<td><%="0".equals(staff.getUsertype())?"Permanent":"Temporary"%>&nbsp;&nbsp;</td>
		<td><%=approverGroups%>&nbsp;&nbsp;</td>
		<td><%=roles%>&nbsp;&nbsp;</td>
	</tr>
	<%
	    i++;
					}
				} else {
	%>
	<tr>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;</td>
	</tr>
	<%
	    }
	%>
</table>
</body>
</html>
