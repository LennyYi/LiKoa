<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,java.math.*,java.text.DecimalFormat,com.aiait.eflow.common.helper.*,com.aiait.eflow.housekeeping.vo.OTSummaryVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.housekeeping.vo.OTSummaryCheckVO" %>

<html>
<%
    String yearMonth = (String)request.getParameter("yearMonth");
    String teamCode = (String)request.getParameter("teamCode");
    String location = (String)request.getParameter("location");
    String locName = location == null || location.equals("") ? "All" : location.equals("GZ") ? "Guangzhou" : "Beijing";
    String printFlag = (String)request.getParameter("printFlag");
    Collection summaryList = (ArrayList)request.getAttribute("summaryList");
    String status = ""; //用来标示当前年月所报销记录的状态。如果是“02”表示已经“结束(已付款)“，则不能再“取消”其中的记录
    Collection traceList = (ArrayList)request.getAttribute("traceList");
    String finFormNo = ParamConfigHelper.getInstance().getParamValue("ot_payment_form");
%>
<head>
<title>Personal Summary For OT</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
 function printForm(){
    var url = "<%=request.getContextPath()%>/otFormAction.it?method=showTeamOTSummary&printFlag=true&yearMonth=<%=yearMonth%>&teamCode=<%=teamCode%>&location=<%=location%>";
    openCenterWindow(url,800,600);
  }
 function exportFile(){
    var url = "<%=request.getContextPath()%>/otFormAction.it?method=showTeamOTSummary&fileType=excel&yearMonth=<%=yearMonth%>&teamCode=<%=teamCode%>&location=<%=location%>";
    //openCenterWindow(url,800,600);
     window.open(url);
  }
  function cancelForm(){
     if(checkSelect('staffCode')<=0){
          alert("You have not selected any records to cancel!");
          return;
       }
       if(confirm("Are you sure to Cancel these selected records")){
          var formSystemIdStr = getTableSelectRecordStr("staffCode","staffCode");
          var url = "<%=request.getContextPath()%>/otFormAction.it?method=cancelStaffOTSummary&yearMonth=<%=yearMonth%>&teamCode=<%=teamCode%>&"+formSystemIdStr;
          var  xmlhttp = createXMLHttpRequest();
          var result;
          if(xmlhttp){
            xmlhttp.open('POST',url,false);
            xmlhttp.onreadystatechange = function()
            {
              if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 if(result.Trim()=="success"){
                   alert("Successfully cancel these selected records!")
                   window.location.reload();
                 }else{
                   alert("Fail to cancel these selected records!");
                 }
              }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
        }
       }
  }
  function confirmForm(){
     if(confirm("Are you sure to confirm that this report is ok")){
          var formSystemIdStr = getTableSelectRecordStr("staffCode","staffCode");
          var url = "<%=request.getContextPath()%>/otFormAction.it?method=confirmStaffOTSummary&yearMonth=<%=yearMonth%>&teamCode=<%=teamCode%>&location=<%=location%>";
          var arr = showModalDialog('<%=request.getContextPath()%>/userManageAction.it?method=enterSelectStaff&pageTitle=SelectCCToStaff',window,'dialogWidth:500px; dialogHeight:450px;help:0;status:0;resizeable:1;')
          if(arr!=undefined){
            url = url + "&mailTo="+arr;
          }
          var  xmlhttp = createXMLHttpRequest();
          var result;
          if(xmlhttp){
            xmlhttp.open('POST',url,false);
            xmlhttp.onreadystatechange = function()
            {
              if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 if(result.Trim()=="fail"){
                   alert("Fail to confirm this report!");
                 }else{
                   alert("Successfully confirm this report!");
                   document.getElementById("confirmTrace").innerHTML = result;
                 }
              }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
        }
       }
  
  }
  function finalComplete(){
	if(document.getElementById('confirmTraceTable').rows.length<3){
		alert("Before final complete the summary, it should be confirmed by other party");
		return;
	}
    if(confirm("Are you sure to final complete this report")){
         var formSystemIdStr = getTableSelectRecordStr("staffCode","staffCode");
         var url = "<%=request.getContextPath()%>/otFormAction.it?method=confirmStaffOTSummary&yearMonth=<%=yearMonth%>&teamCode=<%=teamCode%>&location=<%=location%>&isComplete=1";
         var arr = showModalDialog('<%=request.getContextPath()%>/userManageAction.it?method=enterSelectStaff&pageTitle=SelectCCToStaff',window,'dialogWidth:500px; dialogHeight:450px;help:0;status:0;resizeable:1;')
         if(arr!=undefined){
           url = url + "&mailTo="+arr;
         }
         var  xmlhttp = createXMLHttpRequest();
         var result;
         if(xmlhttp){
           xmlhttp.open('POST',url,false);
           xmlhttp.onreadystatechange = function()
           {
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                result = xmlhttp.responseText;
                if(result.Trim()=="fail"){
                  alert("Fail to complete this report!");
                }else{
                  alert("Successfully complete this report!");
                  document.getElementById("confirmTrace").innerHTML = result;
                  window.document.location 
                    = "<%=request.getContextPath()%>/formManageAction.it?method=displayFormFill&formSystemId=<%=finFormNo%>&yearMonth=<%=yearMonth%>&teamCode=<%=teamCode%>";
                }
             }
          }
          xmlhttp.setRequestHeader("If-Modified-Since","0");
          xmlhttp.send(null);
       }
    }
  }
</script>
<style media=print> 
.Noprint{display:none;} 
</style> 
</head>
<body>
<%if(printFlag!=null && !"".equals(printFlag)){%>
  <OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0> 
  </OBJECT> 
 <%}%>
 <table width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
        <%
	    ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
	    String companyTitle = paramHelper.getParamValue("company_name");
	    if(companyTitle==null || "".equals(companyTitle)){
	    	companyTitle = CommonName.AIAIT_COMPANY_TITLE;
	    }
        %>
        <tr>
           <td colspan='2' align='center'><img src="<%=request.getContextPath()%>/images/aiait_logo.jpg"></td>
        </tr>
        <tr>
          <td colspan='2' align='center'> <b> <font size='2'><%=companyTitle%></font></b> </td>
        </tr>

     <tr>
       <td colspan='2' align='center'> <b><font size='2'>部门误餐津贴报销汇总 / DEPARTMENT MEAL ALLOWANCE RECORD</font></b></td>
     </tr>
 </table>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="10"></td>
    </tr>
    <tr>
 	  <td height="10"></td>
    </tr>
    <tr>
       <%if(!"true".equals(printFlag)){%>
        <td width="10%">&nbsp;&nbsp;
        </td>
        <td width="30%" align='center'>
          <div id="statusList">状态 status:</div>
       </td>
       <%}else{%>
         <td width="40%">&nbsp;&nbsp;
         </td>
       <%}%>
       <td>部门　Team: <b><u>
          <% 
          if(teamCode!=null && !"".equals(teamCode)){ 
            out.print(StaffTeamHelper.getInstance().getTeamNameByCode(teamCode));
          }else{
        	out.print("Whole Company");
          }
         %>
         </u></b>
       </td>
       <td>年月　Year/Month: <b><u><%=yearMonth%></u></b></td>
       <td>Location: <b><u><%=locName%></u></b></td>
 </table>
   <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <tr>
 	  <td height="10"></td>
    </tr>
    <tr>
 	  <td height="10"></td>
    </tr>
 </table>
  <table id='formTable02' width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;">
   <tr class="liebiao_tou">
     <td align='center' rowspan="2" ><input type="checkbox" name="allBtn" onclick="selectAll(this,'staffCode')"></td>
     <td colspan='2' align='center'>姓名 Name</td>
     <td rowspan="2" align='center'>员工编号 Staff Code</td>
     <td colspan='5' align='center'>误餐次数 No. of Meal Allowance</td>
     <td rowspan="2" align='center'>误餐津贴金额(元) Amount of Meal Allowance</td>
     <td rowspan="2" align='center'>的士费用总计 Taxi Fee Total</td>
   </tr>
   <tr class="liebiao_tou">
     <td align='center'>中文名 Chinese Name</td>
     <td align='center'>英文名 English Name</td>
     <td align='center'>正常工作日 Weekday</td>
     <td align='center'>公众假期 Public Holiday</td>
     <td align='center'>Mid-night Support</td>
     <td align='center'>Day-time Support</td>
     <td align='center'>小计 Sub-total</td>
   </tr>
   <%
      if (summaryList != null && summaryList.size() > 0) {
          BigDecimal totalWeekday = BigDecimal.ZERO;
          BigDecimal totalPublicDay = BigDecimal.ZERO;
          BigDecimal totalNightNum = BigDecimal.ZERO;
          BigDecimal totalDayNum = BigDecimal.ZERO;
          BigDecimal totalMealNum = BigDecimal.ZERO;
          BigDecimal totalTaxAmount = BigDecimal.ZERO;
          BigDecimal totalPreTaxAmount = BigDecimal.ZERO;
          BigDecimal totalAfterTaxAmount = BigDecimal.ZERO;
          BigDecimal totalTaxiFee = BigDecimal.ZERO;
          
          BigDecimal weekNum = BigDecimal.ZERO;
          BigDecimal publicNum = BigDecimal.ZERO;
          BigDecimal mealNum = BigDecimal.ZERO;
          BigDecimal taxAmount = BigDecimal.ZERO;
          BigDecimal taxiFee = BigDecimal.ZERO;
          
          Iterator it = summaryList.iterator();
    	  while (it.hasNext()) {
    		 //out.println("<tr class=\"tr_change\">");
    		 OTSummaryVO vo = (OTSummaryVO) it.next();
    		 boolean exceptionalCase = vo.getExceptionalCase();
    		 if (exceptionalCase) {
                 out.println("<tr class=\"tr_highlight\">");
             } else {
                 out.println("<tr class=\"tr_change\">");
             }
    		 String showStaffDetailLink = "<a href=\"javascript:openCenterWindow('"+request.getContextPath()+"/otFormAction.it?method=showPersonalOTSummary&printFlag=true"
			   +"&staffcode="+vo.getStaffCode()+"&yearMonth="+yearMonth+"&status=02&type=old&kind=02',1500,400)\">"+vo.getStaffCode()+"</a>";
    		 
    		 out.println("<td align='center' ><input type=\"checkbox\" name=\"staffCode\" value=\"" + vo.getStaffCode() + "\"></td>");
    		 out.println("<td>" + vo.getStaffNameCn() + "</td>");
    		 out.println("<td>" + vo.getStaffNameEn() + "</td>");
    		 out.println("<td align='center'>" + showStaffDetailLink + "</td>");
             
    		 weekNum = (new BigDecimal(vo.getWeekyNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
    		 totalWeekday = totalWeekday.add(weekNum);
    		 out.println("<td align='right'>" + weekNum + "</td>");
             
    		 publicNum = (new BigDecimal(vo.getPublicNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
    		 totalPublicDay = totalPublicDay.add(publicNum);
    		 out.println("<td align='right'>" + publicNum + "</td>");
             
    		 totalNightNum = totalNightNum.add(vo.getMidNightNum());
    		 out.println("<td align='right'>" + vo.getMidNightNum() + "</td>");
             
    		 totalDayNum = totalDayNum.add(vo.getDayTimeNum());
    		 out.println("<td align='right'>" + vo.getDayTimeNum() + "</td>");
             
             // Check for old forms without meal allowance column
    		 mealNum = vo.getMealAllowanceNum().equals(BigDecimal.ZERO) ? weekNum.add(publicNum) : vo.getMealAllowanceNum();
    		 totalMealNum = totalMealNum.add(mealNum);
    		 out.println("<td align='right'>" + mealNum + "</td>");
             
    		 taxAmount = (new BigDecimal(vo.getTotalTaxAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
    		 totalTaxAmount = totalTaxAmount.add(taxAmount);
    		 out.println("<td align='right'>" + taxAmount + "</td>");
             
    		 taxiFee = (new BigDecimal(vo.getTaxiFeeAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
    		 totalTaxiFee = totalTaxiFee.add(taxiFee);
    		 out.println("<td align='right'>" + taxiFee + "</td>");
             
    		 out.println("</tr>");
    		 status = vo.getStatus();
    	  }
    %>
  <tr>
     <td colspan='4' align='center'><b>总计 (Total)</b></td>
     <td align='right'><b><%=totalWeekday%></b></td>
     <td align='right'><b><%=totalPublicDay%></b></td>
     <td align='right'><b><%=totalNightNum%></b></td>
     <td align='right'><b><%=totalDayNum%></b></td>
     <td align='right'><b><%=totalMealNum%></b></td>
     <td align='right'><b><%=totalTaxAmount%></b></td>
     <td align='right'><b><%=totalTaxiFee%></b></td>
   </tr>    
    <%
      }
   %>
 </table>
 <br>
  <%if(printFlag==null || !"true".equals(printFlag)){%>
 <div id ="confirmTrace">
 <table id='confirmTraceTable' width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;">
   <tr class='liebiao_tou'>
     <td>No.</td>
     <td>Date</td>
     <td>Action</td>
     <td>Operate Staff</td>
     <td>Remark</td>
   </tr>
   <%
     if(traceList!=null && traceList.size()>0){
    	 Iterator it = traceList.iterator();
    	 int i = 1;
    	 StringBuffer str = new StringBuffer("");
    	 while(it.hasNext()){
		   OTSummaryCheckVO temp = (OTSummaryCheckVO)it.next();
		   str.append("<tr>");
			str.append("<td>"+i+"</td>")
			   .append("<td>").append(temp.getActionDate()).append("</td>")
			   .append("<td>").append(temp.getActionCode()).append("</td>")
			   .append("<td>").append(StaffTeamHelper.getInstance().getStaffNameByCode(temp.getStaffCode())).append("</td>")
			   .append("<td>").append(temp.getRemark()).append("</td>");
			str.append("</tr>");
			out.print(str.toString());
			str = new StringBuffer("");
		   i++;
    	 }
     }
   %>
 </table>
 </div>
 <%}%>
 <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <tr>
 	  <td height="10"></td>
    </tr>
      <tr>
 	  <td height="10"></td>
    </tr>
    <tr>
    <td align='center' colspan='5'>
    <%if(printFlag!=null && "true".equals(printFlag)){%>
    <center class="Noprint" > 
 <input type="button" name="setupBtn" value="Page Setup" onclick='document.all.WebBrowser.ExecWB(8,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 <input type="button" name="previewBtn" value="Print Preview" onclick='document.all.WebBrowser.ExecWB(7,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 <input type="button" name="previewBtn" value="Print" onclick='document.all.WebBrowser.ExecWB(6,1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
 <input type="button" name="closeBtn" value="Close" onclick='javascript:window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
   </center>
    <%}else{%>
    <%if("01".equals(status)){%>
     <input type="button" name="cancelBtn" value="Cancel"  onclick='javascript:cancelForm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
     <input type="button" name="confirmBtn" value="Confirm"  onclick='javascript:confirmForm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
     <input type="button" name="confirmBtn" value="Final Confirm"  onclick='javascript:finalComplete()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
    <%}%>
     <input type="button" name="printBtn" value="Print"  onclick='javascript:printForm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
<input type="button" name="exportBtn" value="Export Excel"  onclick='javascript:exportFile()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
        <input type="button" name="backBtn" value="Back" onclick='javascript:history.go(-1)' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
onmouseout="this.className='btn3_mouseout'"
onmousedown="this.className='btn3_mousedown'"
onmouseup="this.className='btn3_mouseup'">
<%}%>
    </td>
    </tr>
    </table>
</body>
</html>
<script language="javascript">
  var status = "<%=status%>";
  var printFlag = "<%=printFlag%>";
  if(printFlag!="true"){
    if(status=="01"){
       document.getElementById("statusList").innerHTML = "状态 Status: <img src='<%=request.getContextPath()%>/images/process.gif'> <b>Processing/未付款(处理中)</b>";
    }else if(status=="02"){
       document.getElementById("statusList").innerHTML = "状态 Status: <img src='<%=request.getContextPath()%>/images/paid.gif'> <b>Paid/已付款(已结束)</b>";
    }
  }
</script>