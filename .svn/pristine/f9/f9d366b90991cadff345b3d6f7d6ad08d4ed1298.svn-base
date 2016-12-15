<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.CommonName,com.aiait.eflow.housekeeping.vo.ContractVO" %>
<%@page import="com.aiait.eflow.common.helper.CompanyHelper,com.aiait.eflow.housekeeping.vo.CompanyVO,com.aiait.eflow.housekeeping.vo.TeamVO,com.aiait.eflow.common.helper.OptionHelper" %>
<%@page import="com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.util.StringUtil" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
<title>Edit Contract</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">
function submitCode() {
	var frm = document.forms[0];
	if (formValidate(frm) == false) return;
	frm.action = "<%=request.getContextPath()%>/contractAction.it?method=saveCntr";
	frm.submit();
}
</script>
<%
ContractVO vo = (ContractVO)request.getAttribute("vo");
  if(vo==null){
	  vo = new ContractVO();
  }
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  
  String pop = (String) request.getParameter("pop");
%>
<body>
<form name="dbForm" action="" method="post">
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
	<tr class="tr1">
		<td align='center' colspan='4'><B><i18n:message key="housekeeping_contract.title"/></B></td>
	</tr>
  <tr> 
      	<TD width=15% height="20" class="tr3">
      	<i18n:message key="housekeeping_contract.contract_no"/>
      	</TD>
      	<TD width=30% height="20">
         <input type='text' name='contractNo' size='30' maxLength='9' required="true" value="<%=vo.getContractNo()%>" <%=vo.getContractNo() == 0 ? "" : "readonly"%>/>
         (<font color="red">*</font>)
		</TD>
      	<TD width=15% height="20" class="tr3">
      	 <i18n:message key="housekeeping_contract.contract_name"/>
      	</TD>
      	<TD width=30% height="20">
         <input type='text' name='contractName' size='30' maxLength='50' required="true" value="<%=vo.getContractName()==null?"":vo.getContractName()%>"/>
         (<font color="red">*</font>)
		</TD>
    </tr>
  <tr> 
		<TD width=15% height="20" class="tr3">
		  <i18n:message key="housekeeping_supplier.city"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='city' size='30' maxLength='50' value="<%=vo.getCity()==null?"":vo.getCity()%>"/>
		</TD>
		<TD width=15% height="20" class="tr3">
		  <i18n:message key="housekeeping_contract.org_id"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='orgName' size='30' maxLength='50' required="true" value="<%=vo.getOrgName()==null?"":vo.getOrgName()%>"/>
         (<font color="red">*</font>)		
		</TD>
  </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3">
		   <i18n:message key="housekeeping_contract.resp_team_code"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='respDept' size='30' maxLength='50' required="true" value="<%=vo.getRespDept()==null?"":vo.getRespDept()%>"/>
          (<font color="red">*</font>)
		</TD>
      	<TD width=15% height="20" class="tr3">
      	<i18n:message key="housekeeping_contract.resp_staff_code"/>
      	</TD>
      	<TD width=30% height="20">
         <input type='text' name='respStaff' size='30' maxLength='50' required="true" value="<%=vo.getRespStaff()==null?"":vo.getRespStaff()%>"/>
         (<font color="red">*</font>)
		</TD>
    </tr>
  <tr> 
		<TD width=15% height="20" class="tr3">
		   <i18n:message key="housekeeping_contract.contact_tel"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='contactTel' size='30' maxLength='50' required="true" value="<%=vo.getContactTel()==null?"":vo.getContactTel()%>"/>
          (<font color="red">*</font>)
		</TD>
		<TD width=15% height="20" class="tr3">
		  <i18n:message key="housekeeping_contract.receive_date"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='receiveDate' size='30' maxLength='50' onclick='setday(this)' value="<%=vo.getReceiveDate()==null?"":vo.getReceiveDate()%>"/>(MM/DD/YYYY)
		</TD>
		</TD>
    </tr> 
  <tr> 
		<TD width=15% height="20" class="tr3">
		  <i18n:message key="housekeeping_contract.sign_1"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='sign1' size='30' maxLength='50' required="true" value="<%=vo.getSign1()==null?"":vo.getSign1()%>"/>
          (<font color="red">*</font>)
		</TD>
      	<TD width=15% height="20" class="tr3">
      	  <i18n:message key="housekeeping_contract.sign_2"/>
      	  </TD>
      	<TD width=30% height="20">
          <input type='text' name='sign2' size='30' maxLength='50' required="true" value="<%=vo.getSign2()==null?"":vo.getSign2()%>"/>
          (<font color="red">*</font>)
		</TD>
    </tr>
  <tr> 
		<TD width=15% height="20" class="tr3">
		  <i18n:message key="housekeeping_contract.sign_3"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='sign2' size='30' maxLength='50' value="<%=vo.getSign3()==null?"":vo.getSign3()%>"/>
		</TD>
      	<TD width=15% height="20" class="tr3">
      	  <i18n:message key="housekeeping_contract.sign_date"/>
      	</TD>
      	<TD width=30% height="20">
          <input type='text' name='signDate' size='30' maxLength='50' required="true" onclick='setday(this)' value="<%=vo.getSignDate()==null?"":vo.getSignDate()%>"/>(MM/DD/YYYY)
          (<font color="red">*</font>)
		</TD>
    </tr> 
  <tr> 
      	<TD width=15% height="20" class="tr3">
      	  <i18n:message key="housekeeping_contract.content"/>
      	</TD>
      	<TD width=30% height="20">
          <input type='text' name='content' size='30' maxLength='50' required="true" value="<%=vo.getContent()==null?"":vo.getContent()%>"/>
          (<font color="red">*</font>)
		</TD>
		<TD width=15% height="20" class="tr3">
		  <i18n:message key="housekeeping_contract.amount"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='amount' size='30' maxLength='12' required="true" value="<%=StringUtil.formatDouble(vo.getAmount())%>"
          onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
          (<font color="red">*</font>)
		</TD>
    </tr>
  <tr> 
		<TD width=15% height="20" class="tr3">
		    <i18n:message key="housekeeping_contract.eff_from_date"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='effFromDate' size='30' maxLength='50' required="true" value="<%=vo.getEffPeriod()==null?"":vo.getEffPeriod()%>"/>
          (<font color="red">*</font>)
		</TD>
		<TD width=15% height="20" class="tr3">
		    <i18n:message key="housekeeping_contract.issue_date"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='issueDate' size='30' maxLength='50' required="true" onclick='setday(this)' value="<%=vo.getIssueDate()==null?"":vo.getIssueDate()%>"/>(MM/DD/YYYY)
          (<font color="red">*</font>)
		</TD>
    </tr> 
  <tr> 
      	<TD width=15% height="20" class="tr3">
      	  <i18n:message key="housekeeping_contract.sign_doc"/>
      	</TD>
      	<TD width=30% height="20">
         <select name="signDoc" required="true" >
         	<option value="Y" <%="Y".equals(vo.getSignDoc())?"selected":""%>> Y </option>
         	<option value="N" <%="N".equals(vo.getSignDoc())?"selected":""%>> N </option>
         </select>
         (<font color="red">*</font>)
		</TD>
		<TD width=15% height="20" class="tr3">
		    <i18n:message key="housekeeping_contract.remark"/>
		  </TD>
		<TD width=35% height="20" > 
          <input type='text' name='remark' size='30' maxLength='50' value="<%=vo.getRemark()==null?"":vo.getRemark()%>"/>
		</TD>
    </tr> 
     <tr>
       <td align="center" colspan="4">
        <%
            if ("true".equals(pop)) {
        %>
        <input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick='javascript:window.close();' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
        onmouseout="this.className='btn3_mouseout'"
        onmousedown="this.className='btn3_mousedown'"
        onmouseup="this.className='btn3_mouseup'">
        <%
            } else {
        %>
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitCode()" class=btn3_mouseout  onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'" 
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <%
            }
        %>
       </td>
     </tr>
  </table>
</form>
</body>
</html>