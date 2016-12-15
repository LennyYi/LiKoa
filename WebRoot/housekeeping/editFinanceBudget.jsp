<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.housekeeping.vo.FinanceBudgetVO" %>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO,com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.common.helper.OptionHelper" %>
<%@page import="com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.util.StringUtil" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
<title>Edit Finance Budget</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">
function shiftEditMode(){
    var frm = document.forms[0];
    for (i=0; i<frm.length; i++) {
    	if(frm[i].type.toUpperCase()=="TEXT"){
    		frm[i].readOnly =false;
    	}
    }
}
</script>
<%
  FinanceBudgetVO vo = (FinanceBudgetVO)request.getAttribute("vo");
  if(vo==null){
	  vo = new FinanceBudgetVO();
  }
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  String isReadOnly = "viewPage".equals(request.getParameter("method"))?"readonly":"";
%>
<body>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
 <form method="post" action="<%=request.getContextPath()%>/financeBudgetAction.it?method=save">
	<tr class="tr1">
		<td align='center' colspan='4'><B><i18n:message key="housekeeping_financebudget.editTitle"/></B></td>
	</tr>
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		  <i18n:message key="housekeeping_financebudget.org"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=CompanyHelper.getInstance().getOrgName(vo.getOrgId())%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	<i18n:message key="housekeeping_financebudget.department"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <%=StaffTeamHelper.getInstance().getTeamNameByCode(vo.getDepartmentId())%>
		</TD>
    </tr>
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		   <i18n:message key="housekeeping_financebudget.category"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=OptionHelper.getInstance().getOptionLabel("expenses_category",vo.getCategoryId())%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	<i18n:message key="housekeeping_financebudget.subcategory"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <%=vo.getSubCategoryName()%>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		   <i18n:message key="housekeeping_financebudget.accountDC"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=vo.getAccountDC()%>
		</TD>
      	<TD width=15% height="20" class="tr3"></TD>
      	<TD width=30% height="20">
		</TD>
    </tr>
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		   <i18n:message key="housekeeping_financebudget.budgetYear"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=vo.getBudgetYear()%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	 <i18n:message key="housekeeping_financebudget.currentMonth"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <input type=text readonly name='currentMonth' value='<%=vo.getCurrentMonth()%>'>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		  <i18n:message key="housekeeping_financebudget.fullYearBudget"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <input type=text readonly name='fullYearBudget' value='<%=StringUtil.formatDouble(vo.getFullYearBudget())%>'>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	  <i18n:message key="housekeeping_financebudget.adjustfullYearBudget"/>
      	  </span></div></TD>
      	<TD width=30% height="20">
          <input type=text readonly name='adjustfullYearBudget' value='<%=StringUtil.formatDouble(vo.getAdjustFullYearBudget())%>'>
		</TD>
    </tr>
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		  <i18n:message key="housekeeping_financebudget.ytdBudget"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <input type=text readonly name='ytdBudget' value='<%=StringUtil.formatDouble(vo.getYtnBudget())%>'>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	  <i18n:message key="housekeeping_financebudget.actualExpense"/>
      	</span></div></TD>
      	<TD width=30% height="20">
          <input type=text readonly name='actualExpense' value='<%=StringUtil.formatDouble(vo.getYtnActualExpense())%>'>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		  <i18n:message key="housekeeping_financebudget.balance"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <input type=text readonly name='balance' value='<%=StringUtil.formatDouble(vo.getBanlance())%>'>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	</span></div></TD>
      	<TD width=30% height="20">
		</TD>
    </tr>
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		    <i18n:message key="housekeeping_financebudget.month1"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=StringUtil.formatDouble(vo.getOriginalBudget1())%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	  <i18n:message key="housekeeping_financebudget.month2"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <%=StringUtil.formatDouble(vo.getOriginalBudget2())%>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		    <i18n:message key="housekeeping_financebudget.month3"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=StringUtil.formatDouble(vo.getOriginalBudget3())%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	  <i18n:message key="housekeeping_financebudget.month4"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <%=StringUtil.formatDouble(vo.getOriginalBudget4())%>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		    <i18n:message key="housekeeping_financebudget.month5"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=StringUtil.formatDouble(vo.getOriginalBudget5())%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	  <i18n:message key="housekeeping_financebudget.month6"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <%=StringUtil.formatDouble(vo.getOriginalBudget6())%>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		    <i18n:message key="housekeeping_financebudget.month7"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=StringUtil.formatDouble(vo.getOriginalBudget7())%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	  <i18n:message key="housekeeping_financebudget.month8"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <%=StringUtil.formatDouble(vo.getOriginalBudget8())%>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		    <i18n:message key="housekeeping_financebudget.month9"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=StringUtil.formatDouble(vo.getOriginalBudget9())%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	  <i18n:message key="housekeeping_financebudget.month10"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <%=StringUtil.formatDouble(vo.getOriginalBudget10())%>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
		    <i18n:message key="housekeeping_financebudget.month11"/>
		  </span></div></TD>
		<TD width=35% height="20" > 
          <%=StringUtil.formatDouble(vo.getOriginalBudget11())%>
		</TD>
      	<TD width=15% height="20" class="tr3"><div align="right"><span class="style1">
      	  <i18n:message key="housekeeping_financebudget.month12"/>
      	</span></div></TD>
      	<TD width=30% height="20">
         <%=StringUtil.formatDouble(vo.getOriginalBudget12())%>
		</TD>
    </tr> 
     <tr>
       <td align="center" colspan="4">
         <input type="button" name="editBtn" value='<i18n:message key="button.edit"/>' onclick="javascript:shiftEditMode();this.disabled=true;" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="submit" name="saveBtn" value='<i18n:message key="button.save"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
     <input type=hidden name=orgId value='<%=vo.getOrgId()%>'>
     <input type=hidden name=departmentId value='<%=vo.getDepartmentId()%>'>
     <input type=hidden name=categoryId value='<%=vo.getCategoryId() %>'>
     <input type=hidden name=subCategoryId value='<%=vo.getSubCategoryId() %>'>
     <input type=hidden name=budgetYear value='<%=vo.getBudgetYear()%>'>
     </form>
  </table>
</body>

</html>