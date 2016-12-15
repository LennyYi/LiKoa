<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.report.vo.LeaveDetailVO" %>
<%@page import="com.aiait.eflow.common.helper.*, com.aiait.eflow.common.helper.StaffTeamHelper"%>
<%@page import="com.aiait.framework.i18n.*"%>
<html>
<head>
<title>Export Leave Detail Report to Excel</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=GBK">
<meta Content-Disposition="attachment; filename=a.xls">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<%  Collection leaveDetailList = (Collection)request.getAttribute("leaveDetailList");
	String formSystemId = (String)request.getAttribute("formSystemId");
response.setContentType("application/vnd.ms-excel;charset=GBK");%>
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">
<table width="100%"  border="1" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><i18n:message key="common.request_no"/></td>
            <td align='center' ><i18n:message key="housekeeping_user.staffcode"/></td>
            <td align='center' ><i18n:message key="housekeeping_user.staffname"/></td>
            <td align='center' ><i18n:message key="housekeeping_user.team"/></td>
            <td align='center' ><i18n:message key="housekeeping_leavebalance.staff_type"/></td>
            <td align='center' ><i18n:message key="housekeeping_leave.leave_type"/></td>
            <td align='center' ><i18n:message key="housekeeping_leave.apply_days"/></td>
            <td align='center' ><i18n:message key="housekeeping_leave.apply_calendar_days"/></td>
            <td align='center' ><i18n:message key="housekeeping_companyholiday.from"/></td>
            <td align='center' ><i18n:message key="housekeeping_companyholiday.to"/></td>
            <td align='center' ><i18n:message key="housekeeping_leave.remark"/></td>
            <td align='center' ><i18n:message key="common.status"/></td>
            <td align='center' ><i18n:message key="common.processing_by"/></td>
          </tr>
      <%
        if(leaveDetailList!=null){
        	int i=1;
        	Iterator it = leaveDetailList.iterator();
        	String tempStatus = "";
        	
        	while(it.hasNext()){
        		LeaveDetailVO vo = (LeaveDetailVO)it.next();
        		tempStatus = vo.getFormBasicData().getStatus();

        		double tempApplyCalendarDays=0;
           	    if("21".equals(vo.getTypeCode()) || "31".equals(vo.getTypeCode()) || "32".equals(vo.getTypeCode()) ){
           		 tempApplyCalendarDays = vo.getApplyCalendarDays();
           	    }
      %>

            <tr class="tr_change">          
              <td ><%=vo.getFormBasicData().getRequestNo()%></td>
              <td ><%=vo.getFormBasicData().getRequestStaffCode()%></td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getFormBasicData().getRequestStaffCode())%></td>
              <td ><%=vo.getFormBasicData().getTeamName()%></td>
              <td><%=BaseDataHelper.getInstance().getLabelValue("",formSystemId+"&"+"01&field_01_1",vo.getStaffType())%></td>
              <td ><%=vo.getTypeName()%></td>
              <td align ='right'><%=vo.getApplyWorkingDays()%></td>
              <td align ='right'><%=tempApplyCalendarDays%></td>           
              <td ><%=StringUtil.getDateStr(vo.getFromDate(), "MM/dd/yyyy")+"("+vo.getFromTimeName()+")"%></td>
              <td ><%=StringUtil.getDateStr(vo.getToDate(), "MM/dd/yyyy")+"("+vo.getToTimeName()+")"%></td>
              <td ><%=vo.getRemark()==null ? "":vo.getRemark()%></td>
              <td ><%=DataMapUtil.covertNodeStatus(vo.getFormBasicData().getStatus())%></td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getFormBasicData().getCurrentProcessor())%></td>                   
            </tr>
          <%
            i++;}
          }%>
       </table>
   </body>
</html>