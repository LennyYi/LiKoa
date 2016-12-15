<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*,com.aiait.eflow.report.vo.LeaveDetailVO" %>
<%@page import="com.aiait.eflow.housekeeping.vo.TeamVO, com.aiait.eflow.formmanage.vo.DictionaryDataVO" %>
<%@ include file="/common/loading.jsp" %>
<html>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<head>
<title>Leave Detail Report</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">

<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script language="javascript">
 
   function searchForm(isExport){
 
	      if(document.forms[0].leaveDateBegin.value.Trim()!=""){
	        if(isDate(document.forms[0].leaveDateBegin,"From Date")==false){
	          return;
	        }
	      }else{
				alert("Please input the valid Date!");
				document.forms[0].onboard_date.focus();
				return
		  }
			
	      if(document.forms[0].leaveDateEnd.value.Trim()!=""){
	        if(isDate(document.forms[0].leaveDateEnd,"To Date")==false){
	          return;
	        }
	      }else{
				alert("Please input the valid Date!");
				document.forms[0].onboard_date.focus();
				return
		  }
		  
	      if(document.forms[0].leaveDateBegin.value.Trim()!="" && document.forms[0].leaveDateEnd.value.Trim()!=""){
	         if(compareDate(document.forms[0].leaveDateBegin.value.Trim(),document.forms[0].leaveDateEnd.value.Trim())==false){
	            alert(from_to_date_compare);
	            document.forms[0].leaveDateBegin.focus();
	            return;
	         }
	      }
	      
	      var url = "<%=request.getContextPath()%>/leaveReportAction.it?method=leaveDetailReport&isExport="+isExport;
	      if(isExport=="1")document.forms[0].target="_blank"  
	      document.forms[0].action = url;
	      document.forms[0].submit();
	   }      
</script>
<%
  String leaveDateBegin = (String)request.getParameter("leaveDateBegin");
  String leaveDateEnd = (String)request.getParameter("leaveDateEnd");
  String comeFrom = (String)request.getParameter("comefrom");
  if(comeFrom!=null && "left".equals(comeFrom)){
	  leaveDateBegin = (String)request.getAttribute("leaveDateBegin");
	  leaveDateEnd = (String)request.getAttribute("leaveDateEnd");
  }
  
  Collection teamList = (Collection) request.getAttribute("teamList");
  String selTeamCode = (String) request.getParameter("teamCode");
  Collection leaveDetailList = (Collection)request.getAttribute("leaveDetailList");
  
  String formSystemId = (String)request.getAttribute("formSystemId");
  BaseDataHelper dataHelper1 = BaseDataHelper.getInstance();
  Collection selectOptionList = null;
  selectOptionList = (Collection) dataHelper1.getDetailMap().get(formSystemId + "&02&field_02_1");
  
  String selectType = (String) request.getParameter("selectType");
%>
<body>
<FORM nane=myForm method="post"> 
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="report_leavedetailreport.location"/></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
        <TD height="25" colspan="4" class="tr1"><i18n:message key="report_leavedetailreport.title"/></TD>
   </TR>
   <TR >
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_companyholiday.from"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
       <INPUT Name="leaveDateBegin" onclick='setday(this)'  Type="text" value="<%=leaveDateBegin==null?"":leaveDateBegin%>" class="text2" style="WIDTH: 130px" id="leaveDateBegin">(mm/dd/yyyy)
      </font>
      </TD>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_companyholiday.to"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
       <INPUT Name="leaveDateEnd" onclick='setday(this)'  Type="text" value="<%=leaveDateEnd==null?"":leaveDateEnd%>" class="text2" style="WIDTH: 130px" id="leaveDateEnd">(mm/dd/yyyy) 
      </TD>
   </TR>
   <TR > 
    <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="team_meal_query.teamname"/></span></div></TD>
      <TD width=35% height="20"> 
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

    <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="housekeeping_leave.leave_type"/></span></div></TD>
      <TD width=35% height="20"> 
      <select name="selectType">
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
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
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
            	 
            	 double tempApplyCalendarDays=0;
            	 if("21".equals(vo.getTypeCode()) || "31".equals(vo.getTypeCode()) || "32".equals(vo.getTypeCode()) ){
            		 tempApplyCalendarDays = vo.getApplyCalendarDays();
            	 }
          %>
            <tr class="tr_change">
              <td ><a href="javascript:openFormWithLayer('<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&&viewFlag=false&requestNo=<%=vo.getFormBasicData().getRequestNo()%>&formSystemId=<%=vo.getFormBasicData().getFormSystemId()%>')"
              		<%=vo.getFormBasicData().getRequestNo()%>><%=vo.getFormBasicData().getRequestNo()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getFormBasicData().getRequestStaffCode()%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getFormBasicData().getRequestStaffCode())%>&nbsp;&nbsp;</td>
              <td ><%=vo.getFormBasicData().getTeamName()%>&nbsp;&nbsp;</td>
              <td><%=BaseDataHelper.getInstance().getLabelValue("",formSystemId+"&"+"01&field_01_1",vo.getStaffType())%>&nbsp;&nbsp;</td>
              <td ><%=vo.getTypeName()%>&nbsp;&nbsp;</td>
              <td align ='right'><%=vo.getApplyWorkingDays()%>&nbsp;&nbsp;</td>
              <td align ='right'><%=tempApplyCalendarDays%>&nbsp;&nbsp;</td>           
              <td ><%=StringUtil.getDateStr(vo.getFromDate(), "MM/dd/yyyy")+"("+vo.getFromTimeName()+")"%>&nbsp;&nbsp;</td>
              <td ><%=StringUtil.getDateStr(vo.getToDate(), "MM/dd/yyyy")+"("+vo.getToTimeName()+")"%>&nbsp;&nbsp;</td>
              <td ><%=vo.getRemark()==null ? "":vo.getRemark()%>&nbsp;&nbsp;</td>
              <td ><%=DataMapUtil.covertNodeStatus(vo.getFormBasicData().getStatus())%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getFormBasicData().getCurrentProcessor())%>&nbsp;&nbsp;</td>
            </tr>
          <%
           i++; }
          }%>
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
