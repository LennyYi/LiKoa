<%@ page contentType="application/vnd.ms-excel;charset=GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
    import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.helper.CompanyHelper"%>
<%@page
    import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%
	StaffTeamHelper staffteam = StaffTeamHelper.getInstance();
	CompanyHelper company = CompanyHelper.getInstance();
    Collection formList = (ArrayList) request.getAttribute("exportFormList");
    boolean contentField2 = false;
    boolean contentField3 = false;
    String contentFields = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LIST_CONTENT_FIELDS);
    if (contentFields != null && !"".equals(contentFields)) {
        String[] aryContentFields = contentFields.split(";");
        for (int i = 0; i < aryContentFields.length; i++) {
            if (aryContentFields[i].equals("2")) {
                contentField2 = true;
            } else if (aryContentFields[i].equals("3")) {
                contentField3 = true;
            }
        }
    }
%>

<html>
<head>
<title>Export form list to excel</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=GBK">
<meta Content-Disposition="attachment; filename=formlist.xls">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">
<table width="100%" border="1" cellpadding="0" cellspacing="0">
    <tr>
        <td align='center'><strong><i18n:message key="common.request_no" /></strong></td>
        <td align='center'><strong><i18n:message key="common.request_form" /></strong></td>
        <td align='center'><strong><i18n:message key="common.submit_by" /></strong></td>
        <td align='center'><strong><i18n:message key="common.company" /></strong></td>
        <td align='center'><strong><i18n:message key="common.team" /></strong></td>
        <td align='center'><strong><i18n:message key="common.request_by" /></strong></td>
        <td align='center'><strong><i18n:message key="common.submit_date" /></strong></td>
        <td align='center'><strong><i18n:message key="common.tip_content" /></strong></td>
        <td align='center'><strong><i18n:message key="common.highlight_content" /></strong></td>
        <%
            if (contentField2) {
        %>
        <td align='center'><strong><i18n:message key="common.highlight_content" /> 2</strong></td>
        <%
            }
            if (contentField3) {
        %>
        <td align='center'><strong><i18n:message key="common.highlight_content" /> 3</strong></td>
        <%
            }
        %>
        <td align='center'><strong><i18n:message key="common.status" /></strong></td>
        <td align='center'><strong><i18n:message key="common.complete_date" /></strong></td>
        <td align='center'><strong><i18n:message key="common.processed_by" /></strong></td>
        <td align='center'><strong><i18n:message key="common.processing_by" /></strong></td>
    </tr>
    <%
        if (formList != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Iterator formIt = formList.iterator();
            int i = 1;
            while (formIt.hasNext()) {
                WorkFlowProcessVO vo = (WorkFlowProcessVO) formIt.next();
                Date cDate=null ,handleDate = null;
                if (vo.getSubmissionDateStr() != null && !"".equals(vo.getSubmissionDateStr())) {
                    cDate = df.parse(vo.getSubmissionDateStr());
                }

            	if (vo.getHandleDateStr() != null && !"".equals(vo.getHandleDateStr())) {
            	    handleDate = df.parse(vo.getHandleDateStr());
            	}
    %>
    <tr>
        <td><%=vo.getRequestNo()%>&nbsp;</td>
        <td><%=vo.getFormName()%>&nbsp;</td>
        <%
            //if (vo.getFormBasicData() != null) {
        %>
        <%-- <td><%=vo.getFormBasicData().getSubmiterName()%>&nbsp;</td>
        <td><%=vo.getFormBasicData().getCompanyName()%>&nbsp;</td>
        <td><%=vo.getFormBasicData().getTeamName()%>&nbsp;</td>
        <td><%=vo.getFormBasicData().getRequesterName()%>&nbsp;</td> --%>
        <td><%=staffteam.getStaffNameByCode(vo.getSubmittedBy())%>&nbsp;</td>
        <td><%=company.getOrgName(vo.getOrgId())%>&nbsp;</td>
        <td><%=staffteam.getTeamNameByCode(vo.getTeamCode())%>&nbsp;</td>
        <td><%=staffteam.getStaffNameByCode(vo.getRequestStaffCode())%>&nbsp;</td>
        <%
            //} else {
        %>
        <%--<td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>--%>
        <%
            //}
        %>
        <td><%=(vo.getSubmissionDateStr() != null && !"".equals(vo.getSubmissionDateStr())) ? StringUtil
                                    .getDateStr(cDate, "MM/dd/yyyy HH:mm:ss")
                                    : ""%>&nbsp;</td>
        <td><%=vo.getTipContent()%></td>
        <td><%=vo.getHighlightContent()%></td>
        <%
            if (contentField2) {
        %>
        <td><%=vo.getHighlightContent2()%></td>
        <%
            }
            if (contentField3) {
        %>
        <td><%=vo.getHighlightContent3()%></td>
        <%
            }
        %>
        <td><%=DataMapUtil.covertNodeStatus(vo.getStatus())%>&nbsp;</td>
        <%
           // if (vo.getFormBasicData() != null) {
        	if("04".equals(vo.getStatus())){
        %>
<%--         <td><%=(vo.getFormBasicData().getCompleteDate() != null) ? df2.format(vo.getFormBasicData().getCompleteDate()) : ""%>&nbsp;</td> --%>
			<td><%=StringUtil.getDateStr(handleDate, "MM/dd/yyyy HH:mm:ss") %></td>			
        <%
            } else {
        %>
        <td>&nbsp;</td>
        <%
            }
        %>
        <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getPreviousProcessor())%>&nbsp;</td>
        <td>
        <%
            String processing_by = null;
                    if (CommonName.NODE_TYPE_WAITING.equals(vo.getNodeType())) {
                        processing_by = vo.getNodeName();
                    } else {
                        processing_by = StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor());
                    }
        %> <%=processing_by%></td>
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
        <%
            if (contentField2) {
        %>
        <td>&nbsp;&nbsp;</td>
        <%
            }
            if (contentField3) {
        %>
        <td>&nbsp;&nbsp;</td>
        <%
            }
        %>
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
