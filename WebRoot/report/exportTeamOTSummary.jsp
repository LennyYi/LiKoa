<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" %>  
<%@page import="java.util.*,java.math.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.housekeeping.vo.OTSummaryVO,com.aiait.eflow.common.CommonName" %>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<html>
<%
    String yearMonth = (String)request.getParameter("yearMonth");
    String teamCode = (String)request.getParameter("teamCode");
    String location = (String)request.getParameter("location");
    String locName = location == null || location.equals("") ? "All" : location.equals("GZ") ? "Guangzhou" : "Beijing";
    String printFlag = (String)request.getParameter("printFlag");
    Collection summaryList = (ArrayList)request.getAttribute("summaryList");
%>
<head>
<title>Personal Summary For OT</title>
<meta Content-Disposition="attachment; filename=a.xls">   
</head>
<body>
<table width="300"  border="0" cellpadding="3" cellspacing="0"  style="border-collapse:collapse;" >
    <tr >
 	  <td height="10" colspan='11'></td>
    </tr>
    <tr >
 	  <td height="10" colspan='11'></td>
    </tr>
     <%
	    ParamConfigHelper paramHelper = ParamConfigHelper.getInstance();
	    String companyTitle = paramHelper.getParamValue("company_name");
	    if(companyTitle==null || "".equals(companyTitle)){
	    	companyTitle = CommonName.AIAIT_COMPANY_TITLE;
	    }
   %>
    <tr>
          <td colspan='11' align='center'> <b> <font size='2'><%=companyTitle%></font></b> </td>
     </tr>
     <tr>
       <td colspan='11' align='center'> <b><font size='2'>部门误餐津贴报销汇总 / DEPARTMENT MEAL ALLOWANCE RECORD</font></b></td>
     </tr>
 </table>
<table border="0" width="300" cellspacing="0" cellpadding="0">
      <tr>
 	  <td colspan='10'></td>
    </tr>
    <tr>
 	  <td colspan='10'></td>
    </tr>
    <tr>
       <td colspan='4'>
          部门　Team: <b><u>
          <% 
          if(teamCode!=null && !"".equals(teamCode)){ 
            out.print(StaffTeamHelper.getInstance().getTeamNameByCode(teamCode));
          }else{
        	out.print("Whole Company");
          }
         %>
         </u></b>
       </td>
       <td colspan='3' align='right'>年月　Year/Month: <b><u><%=yearMonth%></u></b></td>
       <td colspan='3' align='right'>Location: <b><u><%=locName%></u></b></td>
     </tr>
     <tr>
 	  <td colspan='10'></td>
    </tr>
  </table>
  <table id='formTable02' width="300"  border="1">
   <tr>
     <td colspan='2' align='center'><b>姓名 Name</b></td>
     <td rowspan="2" align='center'><b>员工编号 Staff Code</b></td>
     <td colspan='5' align='center'><b>误餐次数 No. of Meal Allowance</b></td>
     <td rowspan="2" align='center'><b>误餐津贴金额(元) Amount of Meal Allowance</b></td>
     <td rowspan="2" align='center'><b>的士费用总计 Taxi Fee Total</b></td>
   </tr>
   <tr >
     <td align='center'><b>中文名 Chinese Name</b></td>
     <td align='center'><b>英文名 English Name</b></td>
     <td align='center'><b>正常工作日 Weekday</b></td>
     <td align='center'><b>公众假期 Public Holiday</b></td>
     <td align='center'><b>Mid-night Support</b></td>
     <td align='center'><b>Day-time Support</b></td>
     <td align='center'><b>小计 Sub-total</b></td>
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
    		 out.println("<tr class=\"tr_change\">");
    		 OTSummaryVO vo = (OTSummaryVO) it.next();
    		 out.println("<td>" + vo.getStaffNameCn() + "</td>");
    		 out.println("<td>" + vo.getStaffNameEn() + "</td>");
    		 out.println("<td align='center'>" + vo.getStaffCode() + "</td>");
             
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
    	  }
    %>
   <tr>
     <td colspan='3' align='center'><b>总计 (Total)</b></td>
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
</body>
</html>