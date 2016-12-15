<%@ page contentType="application/vnd.ms-excel;charset=GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
    import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.CompanyHelper"%>
<%@page
    import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%
    Collection formList = (ArrayList) request.getAttribute("overtimeFormList");
%>

<html>
<head>
<title>Export overtime form list to excel</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=GBK">
<meta Content-Disposition="attachment; filename=overtimeformlist.xls">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">
<table width="100%" border="1" cellpadding="0" cellspacing="0">
    <tr>
        <td align='center'><strong><i18n:message key="common.request_no" /></strong></td>
        <td align='center'><strong><i18n:message key="common.request_form" /></strong></td>
        <td align='center'><strong><i18n:message key="common.highlight_content" /></strong></td>
        <td align='center'><strong><i18n:message key="common.submit_date" /></strong></td>
        <td align='center'><strong><i18n:message key="common.request_by" /></strong></td>
        <td align='center'><strong><i18n:message key="form_overdue.current_overdue" /></strong></td>
        <td align='center'><strong><i18n:message key="form_overdue.receive_date" /></strong></td>
        <td align='center'><strong><i18n:message key="common.remaining" /></strong></td>
    </tr>
    <%
        if (formList != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Iterator formIt = formList.iterator();
            int i = 1;
            while (formIt.hasNext()) {
                WorkFlowProcessVO vo = (WorkFlowProcessVO) formIt.next();
                Date submitDate = null;
                if (vo.getSubmissionDateStr() != null && !"".equals(vo.getSubmissionDateStr())) {
                    submitDate = df.parse(vo.getSubmissionDateStr());
                }
                Date revDate = null;
                if (vo.getReceivingDateStr() != null && !"".equals(vo.getReceivingDateStr())) {
                    revDate = df.parse(vo.getReceivingDateStr());
                }
    %>
    <tr>
        <td><%=vo.getRequestNo()%></td>
        <td><%=vo.getFormName()%></td>
        <td><%=vo.getHighlightContent()%></td>
        <td><%=submitDate != null ? StringUtil.getDateStr(submitDate, "MM/dd/yyyy HH:mm:ss") : ""%></td>
        <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getRequestStaffCode())%></td>
        <td><%=vo.getNodeName()%>&nbsp;(<%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor())%>)</td>
        <td><%=revDate != null ? StringUtil.getDateStr(revDate, "MM/dd/yyyy HH:mm:ss") : ""%></td>
        <td align='right'><b><font color='red'><%=vo.getRemainTime()%></font></b></td>
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
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
