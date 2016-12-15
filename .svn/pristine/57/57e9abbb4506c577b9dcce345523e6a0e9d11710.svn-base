<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
    import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,java.text.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.report.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.*"%>
<%@page
    import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%@page import="com.aiait.framework.i18n.I18NMessageHelper"%>
<%@page language="java" contentType="application/vnd.ms-excel; charset=GBK" pageEncoding="GBK"%>
<html>
<head>
<title>Inter Payment List</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=GBK">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%
    List inquiryList = (List) request.getAttribute("inquiryList");

    String title = "Inter Payment - ";
    String fileName = "Inter Payment_";

    String requestOrg = CompanyHelper.getInstance().getOrgName(request.getParameter("requestOrgId"));
    String costOrg = CompanyHelper.getInstance().getOrgName(request.getParameter("costOrgId"));
    title += requestOrg + " / " + costOrg;
    fileName += requestOrg + "_" + costOrg;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    if (inquiryList != null && !inquiryList.isEmpty()) {
        InterPaymentVO item1 = (InterPaymentVO) inquiryList.get(0);
        InterPaymentVO itemN = (InterPaymentVO) inquiryList.get(inquiryList.size() - 1);
        title += " (" + df.format(item1.getDate()) + " ~ " + df.format(itemN.getDate());
        fileName += "_(" + df.format(item1.getDate()) + "_" + df.format(itemN.getDate());
    }

    title += ")";
    fileName += ").xls";
    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

    String printTime = df2.format(new Date());
%>
</head>
<body style="font-family: Arial; font-size: 11px;">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align='center' style="font-family: Arial; font-size: 12px; font-weight: bold; height: 30px;"><%=title%></td>
    </tr>
    <tr>
        <td align='right' style="font-family: Arial; font-size: 11px; font-weight: bold;">Print Time: <%=printTime%></td>
    </tr>
    <tr>
        <td>
        <table width="100%" border="1" cellpadding="0" cellspacing="0" style="font-family: Arial; font-size: 11px;">
            <tr style="font-weight: bold; background-color: #DDEEDD;">
                <td align='center'>No.</td>
                <td align='center'><i18n:message key="common.date" /></td>
                <td align='center'><i18n:message key="report_interpaymentinquiry.noteno" /></td>
                <td align='center'><i18n:message key="report_interpaymentinquiry.costorg" /></td>
                <td align='center'><i18n:message key="common.request_no" /></td>
                <td align='center'><i18n:message key="common.highlight_content" /></td>
                <td align='center'><i18n:message key="common.amount" /></td>
                <td align='center'><i18n:message key="common.request_by" /></td>
                <td align='center'><i18n:message key="report_interpaymentinquiry.requestorg" /></td>
                <td align='center'><i18n:message key="common.team" /></td>
                <td align='center'><i18n:message key="report_interpaymentinquiry.invoicestatus" /></td>
                <td align='center'><i18n:message key="form.status" /></td>
            </tr>
            <%
                if (inquiryList != null && !inquiryList.isEmpty()) {
                    CompanyHelper companyHelper = CompanyHelper.getInstance();
                    StaffTeamHelper staffTeamHelper = StaffTeamHelper.getInstance();
                    DecimalFormat nf = new DecimalFormat("#,##0.00");
                    Iterator it = inquiryList.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        i++;
                        InterPaymentVO vo = (InterPaymentVO) it.next();
            %>
            <tr>
                <td align='center'><%=i%></td>
                <td align='center'><%=df.format(vo.getDate())%></td>
                <td><%=vo.getNoteNo()%></td>
                <td><%=companyHelper.getOrgName(vo.getCostOrg())%></td>
                <td><%=vo.getRequestNo()%></td>
                <td><%=vo.getContent()%></td>
                <td align='right'><%=nf.format(vo.getAmount())%></td>
                <td><%=staffTeamHelper.getStaffNameByCode(vo.getRequestBy())%></td>
                <td><%=companyHelper.getOrgName(vo.getRequestOrg())%></td>
                <td><%=staffTeamHelper.getTeamNameByCode(vo.getTeam())%></td>
                <td><%=vo.getInvStatus2()%></td>
                <td><%=DataMapUtil.covertNodeStatus(vo.getStatus())%></td>
            </tr>
            <%
                //
                    }
                }
            %>
        </table>
        </td>
    </tr>
    <tr>
        <td align='center' style="font-family: Arial; font-size: 11px; font-weight: bold; height: 30px;">[End of
        Report]</td>
    </tr>
</table>
</body>
</html>