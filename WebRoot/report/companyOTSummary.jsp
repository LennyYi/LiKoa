<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,java.math.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.housekeeping.vo.OTSummaryVO,com.aiait.eflow.common.CommonName" %>
<html>
<%
    String yearMonth = (String)request.getParameter("yearMonth");
    String location = (String)request.getParameter("location");
    String locName = location == null || location.equals("") ? "All" : location.equals("GZ") ? "Guangzhou" : "Beijing";
    String printFlag = (String)request.getParameter("printFlag");
    Collection summaryList = (ArrayList)request.getAttribute("summaryList");
    String status = ""; //用来标示当前年月所报销记录的状态。如果是“02”表示已经“结束(已付款)“，则不能再进行结束操作
%>
<head>
<title>Personal Summary For OT</title>
 <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript">
 function printForm(){
    var url = "<%=request.getContextPath()%>/otFormAction.it?method=showCompanyOTSummary&printFlag=true&yearMonth=<%=yearMonth%>&location=<%=location%>";
    openCenterWindow(url,800,600);
  }
 function exportFile(){
    var url = "<%=request.getContextPath()%>/otFormAction.it?method=showCompanyOTSummary&fileType=excel&yearMonth=<%=yearMonth%>&location=<%=location%>";
    //openCenterWindow(url,800,600);
    window.open(url);
  }
  
  function exportFileByTemplate(){
    var url = "<%=request.getContextPath()%>/otFormAction.it?method=showExcelTemplateSelect&yearMonth=<%=yearMonth%>&location=<%=location%>";
    openCenterWindow(url,450,100);
     //window.open(url);
  }
  
  function completeSummary(){
    if(confirm("Are you sure to complete the Meal Allowance for <%=yearMonth%> ? It will send email to inform staffs.")){
          var url = "<%=request.getContextPath()%>/otFormAction.it?method=completeOTSummary&yearMonth=<%=yearMonth%>&location=<%=location%>";
          var  xmlhttp = createXMLHttpRequest();
          var result;
          if(xmlhttp){
            xmlhttp.open('POST',url,false);
            xmlhttp.onreadystatechange = function()
            {
              if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 if(result.Trim()=="success"){
                   alert("Successfully completed Meal Allowance of <%=yearMonth%>!")
                   window.document.all['confirmBtn'].diabled = "true";
                 }else{
                   alert("Fail to completed Meal Allowance of <%=yearMonth%>!");
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
        BigDecimal feeStandard = new BigDecimal(75); //default is 75 (RMB)
        try {
            feeStandard = new BigDecimal(paramHelper.getParamValue(CommonName.PARAM_OT_FEE_STANDARD_BY_TIMES));
        } catch (Exception e) {
            //
        }
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
       <td colspan='2' align='center'> <b><font size='2'>公司误餐津贴报销汇总 / COMPANY MEAL ALLOWANCE RECORD</font></b></td>
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
     <td align='center'>序号</td>
     <td align='center'>部门</td>
     <td align='center'>帐号</td>
     <td align='center'>户名</td>
     <td align='center'>员工编号</td>
     <td align='center'>Normal Meal Allowance<br>(1)</td>
     <td align='center'>Mid-night Support Allowance<br>(2)</td>
     <td align='center'>Day-time Support Allowance<br>(3)</td>
     <td align='center'>误餐津贴总金额<br>(4)=(1)+(2)+(3)</td>
     <td align='center'>的士费<br>(5)</td>
     <td align='center'>总金额<br>(6)=(4)+(5)</td>
   </tr>
   <%
      if (summaryList != null && summaryList.size() > 0) {
          BigDecimal weekNum = BigDecimal.ZERO;
          BigDecimal publicNum = BigDecimal.ZERO;
          BigDecimal nightNum = BigDecimal.ZERO;
          BigDecimal dayNum = BigDecimal.ZERO;
          BigDecimal mealNum = BigDecimal.ZERO;
          
          BigDecimal normalAmount = BigDecimal.ZERO;
          BigDecimal nightAmount = BigDecimal.ZERO;
          BigDecimal dayAmount = BigDecimal.ZERO;
          BigDecimal taxAmount = BigDecimal.ZERO;
          BigDecimal taxiFee = BigDecimal.ZERO;
          BigDecimal subtotalAmount = BigDecimal.ZERO;
          
          BigDecimal totalNormalAmount = BigDecimal.ZERO;
          BigDecimal totalNightAmount = BigDecimal.ZERO;
          BigDecimal totalDayAmount = BigDecimal.ZERO;
          BigDecimal totalTaxAmount = BigDecimal.ZERO;
          BigDecimal totalTaxiFee = BigDecimal.ZERO;
          BigDecimal totalAmount = BigDecimal.ZERO;
          
    	  int count = 1;
    	  StaffTeamHelper teamHelper = StaffTeamHelper.getInstance();
    	  Iterator it = summaryList.iterator();
    	  while (it.hasNext()) {
    	      OTSummaryVO vo = (OTSummaryVO)it.next();
    	      out.println("<tr class=\"tr_change\">");
    	      out.println("<td align='center'>" + count + "</td>");
    	      out.println("<td align='center'>" + teamHelper.getTeamNameByCode(Integer.toString(vo.getTeamCode())) + "</td>");
    	      out.println("<td align='center'></td>");
    	      out.println("<td align='center'>" + vo.getStaffNameCn() + "</td>");
              
    	      String showStaffDetailLink = "<a href=\"javascript:openCenterWindow('" + request.getContextPath() + "/otFormAction.it?method=showPersonalOTSummary&printFlag=true"
			  + "&staffcode=" + vo.getStaffCode() + "&yearMonth=" + yearMonth + "&status=02&type=old&kind=02',1500,400)\">" + vo.getStaffCode() + "</a>";
    	      out.println("<td align='center'>" + showStaffDetailLink + "</td>");
              
    	      weekNum = (new BigDecimal(vo.getWeekyNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
    	      publicNum = (new BigDecimal(vo.getPublicNum())).setScale(2, BigDecimal.ROUND_HALF_UP);
    	      normalAmount = weekNum.add(publicNum).multiply(feeStandard);
    	      totalNormalAmount = totalNormalAmount.add(normalAmount);
    	      out.println("<td align='right'>" + normalAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "</td>");
              
    	      nightAmount = vo.getMidNightNum().multiply(new BigDecimal("2.4")).multiply(feeStandard);
    	      totalNightAmount = totalNightAmount.add(nightAmount);
    	      out.println("<td align='right'>" + nightAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "</td>");
              
    	      dayAmount = vo.getDayTimeNum().multiply(feeStandard);
    	      totalDayAmount = totalDayAmount.add(dayAmount);
    	      out.println("<td align='right'>" + dayAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "</td>");
              
    	      taxAmount = (new BigDecimal(vo.getTotalTaxAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
    	      totalTaxAmount = totalTaxAmount.add(taxAmount);
    	      out.println("<td align='right'>" + taxAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "</td>");
              
    	      taxiFee = (new BigDecimal(vo.getTaxiFeeAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
    	      totalTaxiFee = totalTaxiFee.add(taxiFee);
    	      out.println("<td align='right'>" + taxiFee.setScale(2, BigDecimal.ROUND_HALF_UP) + "</td>");
              
    	      subtotalAmount = taxAmount.add(taxiFee);
    	      totalAmount = totalAmount.add(subtotalAmount);
    	      out.println("<td align='right'>" + subtotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "</td>");
              
    	      out.println("</tr>");
    	      status = vo.getStatus();
    	      count++;
    	  }
    %>
   <tr>
     <td colspan='5' align='center'><b>总计 (Total)</b></td>
     <td align='right'><b><%=totalNormalAmount.setScale(2, BigDecimal.ROUND_HALF_UP)%></b></td>
     <td align='right'><b><%=totalNightAmount.setScale(2, BigDecimal.ROUND_HALF_UP)%></b></td>
     <td align='right'><b><%=totalDayAmount.setScale(2, BigDecimal.ROUND_HALF_UP)%></b></td>
     <td align='right'><b><%=totalTaxAmount.setScale(2, BigDecimal.ROUND_HALF_UP)%></b></td>
     <td align='right'><b><%=totalTaxiFee.setScale(2, BigDecimal.ROUND_HALF_UP)%></b></td>
     <td align='right'><b><%=totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP)%></b></td>
   </tr>    
   <%
      }
   %>
 </table>
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
     <input type="button" name="confirmBtn" value="Complete"  onclick='javascript:completeSummary()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
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
<input type="button" name="export2Btn" value="Export Excel By Template"  onclick='javascript:exportFileByTemplate()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
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