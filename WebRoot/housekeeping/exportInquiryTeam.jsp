<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
	import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.CompanyHelper"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.*, com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%
	Collection teamList = (Collection) request.getAttribute("eflowteamList");
%>

<html>
<head>
	<title>Export form list to excel</title>
	<meta Content-Disposition="attachment; filename=formlist.xls">
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">
<table width="100%" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align='center' ><strong><i18n:message key="housekeeping_team.status"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_team.teamcode"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_team.teamname"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_team.superiorteam"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_team.teamleader"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_team.isdepteam"/></strong></td>
        <td align='center' ><strong><i18n:message key="housekeeping_team.company"/></strong></td>
	</tr>
	<%
	    if (teamList != null) {
					Iterator teamIt = teamList.iterator();
					int i = 1;
					while (teamIt.hasNext()) {
					    TeamVO team = (TeamVO)teamIt.next();
	%>
	<tr>
		<td><%=team.getStatus()%>&nbsp;&nbsp;</td>
		<td><%=team.getTeamCode()%>&nbsp;&nbsp;</td>
		<td><%=team.getTeamName()%>&nbsp;&nbsp;</td>
		<td><%=StaffTeamHelper.getInstance().getTeamNameByCode(team.getSuperiorsCode())%>&nbsp;&nbsp;</td>
		<td><%=team.getTLeaderName()%>&nbsp;&nbsp;</td>
		<td><%="Y".equalsIgnoreCase(team.getDepartment())? "Yes":""%>&nbsp;&nbsp;</td>	
		<td><%=CompanyHelper.getInstance().getCompany(team.getOrgId())==null?"":CompanyHelper.getInstance().getCompany(team.getOrgId()).getOrgName()%>&nbsp;&nbsp;</td>
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
	</tr>
	<%
	    }
	%>
</table>
</body>
</html>
