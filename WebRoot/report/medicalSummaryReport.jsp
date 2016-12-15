<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*,com.aiait.eflow.report.vo.MedicalVO" %>
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

    var StartDate=new Date(2012,01,01);
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
	      
	      var url = "<%=request.getContextPath()%>/medicalReportAction.it?method=medicalSummaryReport&isExport="+isExport;
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
  
  Collection teamList = (Collection) request.getAttribute("teamList");
  String selTeamCode = (String) request.getParameter("teamCode");
  Collection medicalSummaryList = (Collection)request.getAttribute("medicalSummaryList");
%>
<body>
<FORM nane=myForm method="post"> 
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr><!--
 <tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="report_medicalsummaryreport.location"/></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
        <TD height="25" colspan="4" class="tr1"><i18n:message key="report_medicalsummaryreport.title"/></TD>
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

<table width="200%" border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border: 1px #8899cc solid;">

	<tr class="liebiao_tou">
		<td align='center'><i18n:message key="housekeeping_user.staffcode" /></td>
		<td align='center'><i18n:message key="housekeeping_user.staffname"/></td>
		<td align='center'><i18n:message key="housekeeping_leavebalance.grade"/></td>
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message key="housekeeping_user.team" /></td>
		
		<td align='center'><i18n:message key="report_medicalsummaryreport.staff" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthBegin" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.staff" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthSpent" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.staff" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthEnd" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.staff" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthBegin" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.staff" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthSpent" /></td>
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message key="report_medicalsummaryreport.staff" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthEnd" /></td>		
		<td align='center'><i18n:message key="housekeeping_medicalbalance.connubialName" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.connubial" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthBegin" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.connubial" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthSpent" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.connubial" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthEnd" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.connubial" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthBegin" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.connubial" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthSpent" /></td>
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message key="report_medicalsummaryreport.connubial" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthEnd" /></td>
		<td align='center'><i18n:message key="housekeeping_medicalbalance.childName" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.child" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthBegin" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.child" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthSpent" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.child" /><i18n:message key="report_medicalsummaryreport.clinic" /><i18n:message key="report_medicalsummaryreport.monthEnd" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.child" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthBegin" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.child" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthSpent" /></td>
		<td align='center' style="border-right: #8899cc solid 1px"><i18n:message key="report_medicalsummaryreport.child" /><i18n:message key="report_medicalsummaryreport.hospitalization" /><i18n:message key="report_medicalsummaryreport.monthEnd" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.total" /><i18n:message key="report_medicalsummaryreport.monthBegin" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.total" /><i18n:message key="report_medicalsummaryreport.monthSpent" /><i18n:message key="report_medicalsummaryreport.aftax" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.total" /><i18n:message key="report_medicalsummaryreport.monthEnd" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.total" /><i18n:message key="report_medicalsummaryreport.monthSpent" /><i18n:message key="report_medicalsummaryreport.bftax" /></td>
		<td align='center'><i18n:message key="report_medicalsummaryreport.total" /><i18n:message key="report_medicalsummaryreport.monthSpent" /></td>
	</tr>
	<%
            if(medicalSummaryList!=null){
            	int i=1;
            	Iterator it = medicalSummaryList.iterator();
            	
             while(it.hasNext()){
            	 MedicalVO vo = (MedicalVO)it.next(); 
          %>
	<tr class="tr_change">
		<td><%=vo.getStaffCode()%>&nbsp;&nbsp;</td>
		<td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getStaffCode())%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getGrade()%>&nbsp;&nbsp;</td>
		<td style="border-right: #8899cc solid 1px"><%=StaffTeamHelper.getInstance().getTeamNameByCode(vo.getTeamCode())%>&nbsp;&nbsp;</td>
		
		<td align='right'><%=vo.getStaffCMonthBegin()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getStaffCMonthSpent()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getStaffCMonthEnd()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getStaffHMonthBegin()%>&nbsp;&nbsp;</td>
		<td align='right' style='format:#,##0.00'><%=vo.getStaffHMonthSpent()%>&nbsp;&nbsp;</td>
		<td align='right' style="border-right: #8899cc solid 1px"><%=vo.getStaffHMonthEnd()%>&nbsp;&nbsp;</td>
		<td><%=vo.getConnubialName()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getConnubialCMonthBegin()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getConnubialCMonthSpent()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getConnubialCMonthEnd()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getConnubialHMonthBegin()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getConnubialHMonthSpent()%>&nbsp;&nbsp;</td>
		<td align='right' style="border-right: #8899cc solid 1px"><%=vo.getConnubialHMonthEnd()%>&nbsp;&nbsp;</td>	
		<td><%=vo.getChildName()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getChildCMonthBegin()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getChildCMonthSpent()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getChildCMonthEnd()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getChildHMonthBegin()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getChildHMonthSpent()%>&nbsp;&nbsp;</td>
		<td align='right' style="border-right: #8899cc solid 1px"><%=vo.getChildHMonthEnd()%>&nbsp;&nbsp;</td>		
		<td align='right'><%=vo.getTotalMonthBegin()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getTotalMonthAfterTax()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getTotalMonthEnd()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getTotalMonthBeforeTax()%>&nbsp;&nbsp;</td>
		<td align='right'><%=vo.getTotalMonthSpent()%>&nbsp;&nbsp;</td>
	</tr>
	<%             	
           i++; }
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
