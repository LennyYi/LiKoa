<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
    import="java.util.*,com.aiait.eflow.housekeeping.vo.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.CompanyHelper"%>
<%@page
    import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%
    Collection leaveRecords = (Collection) request.getAttribute("leaveRecords");
%>

<html>
<head>
<title>Leave Records</title>
<meta Content-Disposition="attachment; filename=leave_records.xls">
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">
<table width="100%" border="1" cellpadding="0" cellspacing="0">
    <tr>
        <td align='center'><strong><i18n:message key="common.request_no" /></strong></td>
        <td align='center'><strong><i18n:message key="common.submit_by" /></strong></td>
        <td align='center'><strong><i18n:message key="common.submit_date" /></strong></td>
        <td align='center'><strong><i18n:message key="common.team" /></strong></td>
        <td align='center'><strong><i18n:message key="common.request_by" /></strong></td>
        <td align='center'><strong><i18n:message key="housekeeping_leave.leave_type" /></strong></td>
        <td align='center'><strong><i18n:message key="housekeeping_leave.apply_days" /></strong></td>
        <td align='center'><strong><i18n:message key="housekeeping_leave.from_date" /></strong></td>
        <td align='center'><strong><i18n:message key="housekeeping_leave.from_time" /></strong></td>
        <td align='center'><strong><i18n:message key="housekeeping_leave.to_date" /></strong></td>
        <td align='center'><strong><i18n:message key="housekeeping_leave.to_time" /></strong></td>
        <td align='center'><strong><i18n:message key="housekeeping_leave.remark" /></strong></td>
    </tr>
    <%
        if (leaveRecords != null) {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
            Iterator it = leaveRecords.iterator();
            int i = 1;
            while (it.hasNext()) {
                LeaveVO vo = (LeaveVO) it.next();
                FormBasicDataVO basicData = vo.getFormBasicData();
                String submiter = basicData.getSubmiterName() == null ? basicData.getSubmiterCode() : basicData.getSubmiterName();
                String requester = basicData.getRequesterName() == null ? basicData.getRequesterCode() : basicData.getRequesterName();
                String team = basicData.getTeamName() == null ? basicData.getTeamCode() : basicData.getTeamName();
    %>
    <tr>
        <td><%=basicData.getRequestNo()%></td>
        <td><%=submiter%></td>
        <td><%=df.format(basicData.getRequestDate())%></td>
        <td><%=team%></td>
        <td><%=requester%></td>
        <td><%=vo.getTypeName()%></td>
        <td><%=vo.getApplyDays()%></td>
        <td><%=df2.format(vo.getFromDate())%></td>
        <td><%=vo.getFromTimeName()%></td>
        <td><%=df2.format(vo.getToDate())%></td>
        <td><%=vo.getToTimeName()%></td>
        <td><%=vo.getRemark() == null ? "" : vo.getRemark()%></td>
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
        <td>&nbsp;&nbsp;</td>
        <td>&nbsp;&nbsp;</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
