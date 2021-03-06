<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" %>  
<%@page import="java.util.*,java.math.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.housekeeping.vo.OTSummaryVO,com.aiait.eflow.common.CommonName" %>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<html>
<%
    String yearMonth = (String)request.getParameter("yearMonth");
    String location = (String)request.getParameter("location");
    String locName = location == null || location.equals("") ? "All" : location.equals("GZ") ? "Guangzhou" : "Beijing";
    String printFlag = (String)request.getParameter("printFlag");
    Collection summaryList = (ArrayList)request.getAttribute("summaryList");
%>
<head>
<title>Company OT Summary</title>
<meta Content-Disposition="attachment; filename=a.xls"> 
</head>
<body>
<table width="300"  border="0" cellpadding="3" cellspacing="0"  style="border-collapse:collapse;" >
    <tr >
 	  <td height="10" colspan='4'></td>
    </tr>
    <tr >
 	  <td height="10" colspan='4'></td>
    </tr>
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
          <td colspan='11' align='center'><b><font size='2'><%=companyTitle%></font></b></td>
        </tr>
     <tr>
       <td colspan='11' align='center'><b><font size='2'>公司误餐津贴报销汇总 / COMPANY MEAL ALLOWANCE RECORD</font></b></td>
     </tr>
 </table>
 <table border="0" width="300" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="10" colspan='4'></td>
    </tr>
    <tr>
       <td colspan='7' align='right'>年月　Year/Monthx: <b><u><%=yearMonth%></u></b></td>
       <td colspan='4' align='right'>Location: <b><u><%=locName%></u></b></td>
    </tr>
 </table>
 <table border="0" width="300" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="10" colspan='4'></td>
    </tr>
    <tr>
 	  <td height="10" colspan='4'></td>
    </tr>
 </table>
 <table id='formTable02' width="300"  border="1">
   <tr>
     <td align='center'><b>序号</b></td>
     <td align='center'><b>部门</b></td>
     <td align='center'><b>帐号</b></td>
     <td align='center'><b>户名</b></td>
     <td align='center'><b>员工编号</b></td>
     <td align='center'><b>Normal Meal Allowance<br>[1]</b></td>
     <td align='center'><b>Mid-night Support Allowance<br>[2]</b></td>
     <td align='center'><b>Day-time Support Allowance<br>[3]</b></td>
     <td align='center'><b>误餐津贴总金额<br>[4]=[1]+[2]+[3]</b></td>
     <td align='center'><b>的士费<br>[5]</b></td>
     <td align='center'><b>总金额<br>[6]=[4]+[5]</b></td>
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
              out.println("<tr>");
              out.println("<td align='center'>" + count + "</td>");
              out.println("<td align='center'>" + teamHelper.getTeamNameByCode(Integer.toString(vo.getTeamCode())) + "</td>");
              if (vo.getAccountNo() != null) {
                  out.println("<td align='center'>'" + vo.getAccountNo() + "</td>");
              } else {
                  out.println("<td align='center'></td>");
              }
              out.println("<td align='center'>" + vo.getStaffNameCn() + "</td>");
              out.println("<td align='center'>" + vo.getStaffCode() + "</td>");
              
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
</body>
</html>