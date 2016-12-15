<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*,com.aiait.eflow.report.vo.LeaveSummaryVO" %>
<%@page import="com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.formmanage.vo.DictionaryDataVO" %>
<%@ include file="/common/loading.jsp" %>
<html>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<head>
<title>Export Leave Summary Report to Excel</title>
<meta http-equiv="Content-Type" content="application/vnd.ms-excel; charset=GBK">
<meta Content-Disposition="attachment; filename=a.xls">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript">

function monthName(yearMonth){ 

	var month=new Array(12);
	month[0]="Jan";
	month[1]="Feb";
	month[2]="Mar";
	month[3]="Apr";
	month[4]="May";
	month[5]="Jun";
	month[6]="Jul";
	month[7]="Aug";
	month[8]="Sept";
	month[9]="Oct";
	month[10]="Nov";
	month[11]="Dec";

	if(yearMonth ==""){
		document.write("Enquiry Month");
	}
	else{
	  var monthNum = yearMonth.substring(5);  
	  document.write(month[parseInt(monthNum)-1]);		
	}
 }
 
</script>
<%
  String yearMonth = (String)request.getParameter("yearMonth");
  yearMonth = yearMonth == null ? "" : yearMonth;

  Collection leaveSummaryList = (Collection)request.getAttribute("leaveSummaryList");
  String formSystemId = (String)request.getAttribute("formSystemId");
  String selectType = (String) request.getParameter("selectType");
  selectType = selectType == null ? "" : selectType;
  
  response.setContentType("application/vnd.ms-excel;charset=GBK");
%>
</head>
<body style="font-family: Arial, Helvetica, sans-serif;">

<table width="100%" border="1" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border: 1px #8899cc solid;">
	<% if("".equals(selectType)) {%>
	<tr class="liebiao_tou">
		<td align='center'><i18n:message
			key="housekeeping_user.staffcode" /></td>
		<td align='center'><i18n:message key="housekeeping_user.team" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.onboard_date" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.staff_type" /></td>		
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message
			key="housekeeping_user.staffname" /></td>

		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_carry_forward" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_statutory_entitle" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_company_entitle" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_total_entitle" /></td>
		<td align='center'><script>monthName('<%=yearMonth%>');</script></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.ytd" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_forfeit" /></td>
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message
			key="housekeeping_leavebalance.annual_balance" /></td>

		<td align='center'><i18n:message
			key="housekeeping_leavebalance.sick_total_entitle" /></td>
		<td align='center'><script>monthName('<%=yearMonth%>');</script></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.ytd" /></td>
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message
			key="housekeeping_leavebalance.sick_balance" /></td>

		<td align='center'><script>monthName('<%=yearMonth%>');</script></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.ytd" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.remark" /></td>
	</tr>
	<%
            if(leaveSummaryList!=null){
            	int i=1;
            	Iterator it = leaveSummaryList.iterator();
            	
             while(it.hasNext()){
            	 LeaveSummaryVO vo = (LeaveSummaryVO)it.next(); 
            	 String tempEffectiveDate = vo.getEffectiveDate()== null?"":StringUtil.getDateStr(vo.getEffectiveDate(), "MM/dd/yyyy");
          %>
	<tr class="tr_change">
		<td><%=vo.getLeaveBasicData().getStaffCode()%>&nbsp;&nbsp;</td>
		<td><%=vo.getDepartment()%>&nbsp;&nbsp;</td>
		<td><%=tempEffectiveDate%> </td>
		<td><%=BaseDataHelper.getInstance().getLabelValue("",formSystemId+"&"+"01&field_01_1",vo.getStaffType())%></td>
		<td style="border-right: #8899cc solid 1px"><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getLeaveBasicData().getStaffCode())%> </td>

		<td align='right'><%=vo.getLeaveBasicData().getAnnualCarryForwardDays()%> </td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualStatutoryEntitleDays()%> </td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualCompanyEntitleDays()%> </td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualTotalEntitleDays()%> </td>
		<td align='right'><%=vo.getAnnualEnquiryMonth()%> </td>
		<td align='right'><%=vo.getAnnualYTD()%> </td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualForfeitDays()%> </td>
		<td align='right' style="border-right: #8899cc solid 1px"><%=vo.getLeaveBasicData().getAnnualBalanceDays()%> </td>

		<td align='right'><%=vo.getLeaveBasicData().getSickTotalEntitleDays()%> </td>
		<td align='right'><%=vo.getSickEnquiryMonth()%> </td>
		<td align='right'><%=vo.getSickYTD()%> </td>
		<td align='right' style="border-right: #8899cc solid 1px"><%=vo.getLeaveBasicData().getSickBalanceDays()%> </td>

		<td align='right'><%=vo.getOtherEnquiryMonth()%> </td>
		<td align='right'><%=vo.getOtherYTD()%> </td>
		<td><%=vo.getRemark()==null ? "":vo.getRemark()%> </td>
	</tr>
	<%             	
           i++; }
          } 
   }
	else{ %>
	<tr class="liebiao_tou">
		<td align='center'><i18n:message
			key="housekeeping_user.staffcode" /></td>
		<td align='center'><i18n:message key="housekeeping_user.team" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.onboard_date" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.staff_type" /></td>				
		<td align='center'><i18n:message
			key="housekeeping_user.staffname" /></td>
		<%// Annual leave 
               if("01".equals(selectType)){ %>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_carry_forward" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_statutory_entitle" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_company_entitle" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_total_entitle" /></td>
		<%}// Sick leave 
               else if("11".equals(selectType)){ %>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.sick_total_entitle" /></td>
		<%}%>

		<td align='center'><script> monthName('<%=yearMonth%>'); </script></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.ytd" /></td>

		<%// Annual leave 
               if("01".equals(selectType)){ %>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_forfeit" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_balance" /></td>
		<%}// Sick leave 
               else if("11".equals(selectType)){ %>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.sick_balance" /></td>
		<%}// Other 
               else { %>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.remark" /></td>
		<%}%>
	</tr>
	<%
            if(leaveSummaryList!=null){
            	int i=1;
            	Iterator it = leaveSummaryList.iterator();
            	
             while(it.hasNext()){
            	 LeaveSummaryVO vo = (LeaveSummaryVO)it.next(); 
            	 if(vo.getCommonEnquiryMonth()==0){
            		 continue;
            	 }
            	 String tempEffectiveDate = vo.getEffectiveDate()== null?"":StringUtil.getDateStr(vo.getEffectiveDate(), "MM/dd/yyyy");
          %>
	<tr class="tr_change">
		<td><%=vo.getLeaveBasicData().getStaffCode()%> </td>
		<td><%=vo.getDepartment()%> </td>
		<td><%=tempEffectiveDate%> </td>
		<td><%=BaseDataHelper.getInstance().getLabelValue("",formSystemId+"&"+"01&field_01_1",vo.getStaffType())%></td>
		<td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getLeaveBasicData().getStaffCode())%> </td>

		<%// Annual leave 
               if("01".equals(selectType)){ %>
		<td><%=vo.getLeaveBasicData().getAnnualCarryForwardDays()%></td>
		<td><%=vo.getLeaveBasicData().getAnnualStatutoryEntitleDays()%></td>
		<td><%=vo.getLeaveBasicData().getAnnualCompanyEntitleDays()%></td>
		<td><%=vo.getLeaveBasicData().getAnnualTotalEntitleDays()%></td>
		<%}// Sick leave 
               else if("11".equals(selectType)){ %>
		<td><%=vo.getLeaveBasicData().getSickTotalEntitleDays()%></td>
		<%}%>

		<td><%=vo.getCommonEnquiryMonth()%></td>
		<td><%=vo.getCommonYTD()%></td>

		<%// Annual leave 
               if("01".equals(selectType)){ %>
		<td><%=vo.getLeaveBasicData().getAnnualForfeitDays()%></td>
		<td><%=vo.getLeaveBasicData().getAnnualBalanceDays()%></td>
		<%}// Sick leave 
               else if("11".equals(selectType)){ %>
		<td><%=vo.getLeaveBasicData().getSickBalanceDays()%></td>
		<%}// Other 
               else { %>
		<td><%=vo.getRemark()==null ? "":vo.getRemark()%></td>
		<%}%>

	</tr>
	<%            	
           i++; }
        }
     }
   %>
 </table>
</body>
</html>

