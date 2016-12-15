<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*,com.aiait.eflow.report.vo.LeaveSummaryVO" %>
<%@page import="com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.formmanage.vo.DictionaryDataVO" %>
<%@ include file="/common/loading.jsp" %>
<html>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<head>
<title>Leave Summary Report</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">

<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script language="javascript">

function createYearMonth(fieldId,comeFrom, yearMonth){ 

    document.write("<select name='"+fieldId+"'>");
    document.write("<option></option>");
    var NowDate=new Date();

    var StartDate=new Date(2011,01,01);
    var EndDate=new Date();
    EndDate.setMonth(EndDate.getMonth()+6);
    
    //StartDate.setMonth(StartDate.getMonth()-Span, 1);
    EndDate.setMonth(EndDate.getMonth(), 1);
    var Month,year;
    //for(var d=StartDate;d<EndDate;d.setMonth(d.getMonth()+1))
    for(var d=EndDate;d>StartDate;d.setMonth(d.getMonth()-1))
    {
       Month=d.getMonth()+1;
       year=d.getFullYear();
       //alert("Year/Month: " + year + "/" + Month);
       //if((NowDate.getMonth()-1)==Month&&d.getFullYear()==year)
       if(comeFrom == "left"){
         if((NowDate.getMonth()+1)==Month&&NowDate.getFullYear()==year)
           document.write("<option value='"+year+"/"+((Month)<10?"0"+(Month):(Month))+"' selected>"+year+"/"+((Month)<10?"0"+(Month):(Month))+"</option>");
         else
           document.write("<option value='"+year+"/"+((Month)<10?"0"+(Month):(Month))+"'>"+year+"/"+((Month)<10?"0"+(Month):(Month))+"</option>");
       }else{
         if(yearMonth == year+"/"+((Month)<10?"0"+(Month):(Month)))
           document.write("<option value='"+year+"/"+((Month)<10?"0"+(Month):(Month))+"' selected>"+year+"/"+((Month)<10?"0"+(Month):(Month))+"</option>");
         else
           document.write("<option value='"+year+"/"+((Month)<10?"0"+(Month):(Month))+"'>"+year+"/"+((Month)<10?"0"+(Month):(Month))+"</option>");
       }
       
    }
     document.write("</select>");
 }

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

   function searchForm(isExport){
 
	      if(document.forms[0].yearMonth.value.Trim()==""){
	    	  alert("Please fill in the field 'Year/Month'!")
	          document.forms[0].yearMonth.focus();
	          return;
	      }
	      
	      var url = "<%=request.getContextPath()%>/leaveReportAction.it?method=leaveSummaryReport&isExport="+isExport;
	      if(isExport=="1")document.forms[0].target="_blank"  
	      document.forms[0].action = url;
	      document.forms[0].submit();
	   }      
</script>
<%
  String yearMonth = (String)request.getParameter("yearMonth");
  yearMonth = yearMonth == null ? "" : yearMonth;
  
  String comeFrom = (String)request.getParameter("comefrom");
  comeFrom = comeFrom ==null ? "" : comeFrom;
  
  //if(comeFrom!=null && "left".equals(comeFrom)){

 // }
  
  Collection teamList = (Collection) request.getAttribute("teamList");
  String selTeamCode = (String) request.getParameter("teamCode");
  Collection leaveSummaryList = (Collection)request.getAttribute("leaveSummaryList");
  
  String formSystemId = (String)request.getAttribute("formSystemId");
  BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
  Collection selectOptionList = null;
  selectOptionList = (Collection) dataHelper1.getDetailMap().get(formSystemId + "&02&field_02_1");
  
  String selectType = (String) request.getParameter("selectType");
  selectType = selectType == null ? "" : selectType;
%>
<body>
<FORM nane=myForm method="post"> 
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr><!--
 <tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="report_leavesummaryreport.location"/></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
        <TD height="25" colspan="4" class="tr1"><i18n:message key="report_leavesummaryreport.title"/></TD>
   </TR>
   <TR > 
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="team_meal_query.teamname"/></span></div></TD>
      <TD width=30% height="20"> 
      <select name="teamCode">
        <%
          if(teamList!=null && teamList.size()>0){
        	  %><option value="">All</option><%
        	  Iterator it = teamList.iterator();
        	  while(it.hasNext()){
        		  TeamVO team = (TeamVO)it.next();
        		  String selected = team.getTeamCode().equals(selTeamCode) ? "selected" : "";
        		  out.print("<option value='" + team.getTeamCode() + "' " + selected + ">" + StaffTeamHelper.getInstance().getTeamNameByCode(team.getTeamCode()) + "</>");   
            }
         }
        %>
      </select>
      </TD>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_leavesummaryreport.monthtag"/></span></div></TD>
      <TD width=30% height="20" >
       <script>
         createYearMonth("yearMonth", '<%=comeFrom%>','<%=yearMonth%>');
       </script>
          (yyyy/mm)
      </TD>
    </TR>
    <TR >
        <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_leave.leave_type"/></span></div></TD>
        <TD width=35% height="20"  colspan='3'>
          <select name="selectType" > 
          <option value="">All</option>
        <%
          if(selectOptionList!=null && selectOptionList.size()>0){
        	  Iterator it = selectOptionList.iterator();
        	  while(it.hasNext()){
        		  DictionaryDataVO type = (DictionaryDataVO)it.next();
        		  String selected = type.getId().equals(selectType) ? "selected" : "";
        		  out.print("<option value='" + type.getId() + "' " + selected + ">" + type.getValue() + "</>");   
            }
         }
        %>
          </select>
    </TR>

    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="searchForm()">&nbsp;&nbsp; 
          <input type="button" name="expBtn" value="<i18n:message key="button.export_excel"/>" onclick="searchForm(1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
      </td>
    </tr>
 </TABLE>
 </FORM>

<table width="100%" border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border: 1px #8899cc solid;">
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
		<td align='center'>Annual <script>monthName('<%=yearMonth%>');</script></td>
		<td align='center'>Annual <i18n:message
			key="housekeeping_leavebalance.ytd" /></td>
		<td align='center'><i18n:message
			key="housekeeping_leavebalance.annual_forfeit" /></td>
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message
			key="housekeeping_leavebalance.annual_balance" /></td>

		<td align='center'><i18n:message
			key="housekeeping_leavebalance.sick_total_entitle" /></td>
		<td align='center'>Sick <script>monthName('<%=yearMonth%>');</script></td>
		<td align='center'>Sick <i18n:message
			key="housekeeping_leavebalance.ytd" /></td>
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message
			key="housekeeping_leavebalance.sick_balance" /></td>

		<td align='center'>Other <script>monthName('<%=yearMonth%>');</script></td>
		<td align='center'>Other <i18n:message
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
		<td><%=tempEffectiveDate%>&nbsp;&nbsp;</td>
		<td><%=BaseDataHelper.getInstance().getLabelValue("",formSystemId+"&"+"01&field_01_1",vo.getStaffType())%>&nbsp;&nbsp;</td>
		<td style="border-right: #8899cc solid 1px"><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getLeaveBasicData().getStaffCode())%>&nbsp;&nbsp;</td>

		<td align='right'><%=vo.getLeaveBasicData().getAnnualCarryForwardDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualStatutoryEntitleDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualCompanyEntitleDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualTotalEntitleDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getAnnualEnquiryMonth()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getAnnualYTD()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualForfeitDays()%>&nbsp;&nbsp;</td>
		<td align='right' style="border-right: #8899cc solid 1px"><%=vo.getLeaveBasicData().getAnnualBalanceDays()%>&nbsp;&nbsp;</td>

		<td align='right'><%=vo.getLeaveBasicData().getSickTotalEntitleDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getSickEnquiryMonth()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getSickYTD()%>&nbsp;&nbsp;</td>
		<td align='right' style="border-right: #8899cc solid 1px"><%=vo.getLeaveBasicData().getSickBalanceDays()%>&nbsp;&nbsp;</td>

		<td align='right'><%=vo.getOtherEnquiryMonth()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getOtherYTD()%>&nbsp;&nbsp;</td>
		<td><%=vo.getRemark()==null ? "":vo.getRemark()%>&nbsp;&nbsp;</td>
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
		<td><%=vo.getLeaveBasicData().getStaffCode()%>&nbsp;&nbsp;</td>
		<td><%=vo.getDepartment()%>&nbsp;&nbsp;</td>
		<td><%=tempEffectiveDate%>&nbsp;&nbsp;</td>
		<td><%=BaseDataHelper.getInstance().getLabelValue("",formSystemId+"&"+"01&field_01_1",vo.getStaffType())%>&nbsp;&nbsp;</td>
		<td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getLeaveBasicData().getStaffCode())%>&nbsp;&nbsp;</td>

		<%// Annual leave 
               if("01".equals(selectType)){ %>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualCarryForwardDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualStatutoryEntitleDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualCompanyEntitleDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualTotalEntitleDays()%>&nbsp;&nbsp;</td>
		<%}// Sick leave 
               else if("11".equals(selectType)){ %>
		<td align='right'><%=vo.getLeaveBasicData().getSickTotalEntitleDays()%>&nbsp;&nbsp;</td>
		<%}%>

		<td align='right'><%=vo.getCommonEnquiryMonth()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getCommonYTD()%>&nbsp;&nbsp;</td>

		<%// Annual leave 
               if("01".equals(selectType)){ %>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualForfeitDays()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getLeaveBasicData().getAnnualBalanceDays()%>&nbsp;&nbsp;</td>
		<%}// Sick leave 
               else if("11".equals(selectType)){ %>
		<td align='right'><%=vo.getLeaveBasicData().getSickBalanceDays()%>&nbsp;&nbsp;</td>
		<%}// Other 
               else { %>
		<td><%=vo.getRemark()==null ? "":vo.getRemark()%>&nbsp;&nbsp;</td>
		<%}%>

	</tr>
	<%            	
           i++; }
        }
     }
   %>
</table>
<div id="loading2" style="display:none;align:center">
	     <div class="loading-indicator">
		    It is loading page...
	     </div>
      </div>
  </body>
  </html>
   <script language="javascript">
	setResizeAble(mytable);
	window.onload=function(){enableTooltips()};
   </script>
