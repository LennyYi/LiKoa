<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.util.StringUtil" %>
<%@page import="java.util.*" %>
<html>
<head>
 <title>Edit Building</title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
	function submitCode() {
		var frm = document.forms[0];
		if (formValidate(frm) == false) return;
		frm.action = "<%=request.getContextPath()%>/buildingAction.it?method=saveBuilding";
		frm.submit();
	}
 </script>
<body>
<% 
  String editType = request.getParameter("editType");
  BuildingVO vo = (BuildingVO) request.getAttribute("building");
  if (vo == null) {
	  vo = new BuildingVO();
  }
%>
<form name="dbForm" action="" method="post">
  <input type="hidden" name="editType" value="<%=editType%>">
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
   <tr class="tr1">
       <td align='center' colspan='4'>
          <b> <i18n:message key="housekeeping_building.title"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.code"/> : 
       </td>
       <td>
         <input type='text' name='code' size='10' maxLength='10'
          required="true" value="<%=vo.getCode() == null ? "" : vo.getCode()%>" <%=vo.getCode() == null ? "" : "readonly"%>/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.name"/> : 
       </td>
       <td>
         <input type='text' name='name' size='30' maxLength='50' required="true" value="<%=vo.getName() == null ? "" : vo.getName()%>"/>
         (<font color="red">*</font>)
       </td>
       <td align="right">
         <i18n:message key="housekeeping_building.renter"/> : 
       </td>
       <td>
         <input type='text' name='renter' size='30' maxLength='50' required="true" value="<%=vo.getRenter() == null ? "" : vo.getRenter()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
       <td align="right">
         <i18n:message key="housekeeping_supplier.province"/> : 
       </td>
       <td>
         <input type='text' name='province' size='30' maxLength='50' required="true" value="<%=vo.getProvince() == null ? "" : vo.getProvince()%>"/>
         (<font color="red">*</font>)
       </td>
       <td align="right">
         <i18n:message key="housekeeping_supplier.city"/> : 
       </td>
       <td>
         <input type='text' name='city' size='30' maxLength='50' required="true" value="<%=vo.getCity() == null ? "" : vo.getCity()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.period"/> : 
       </td>
       <td>
         <input type='text' name='period' size='30' maxLength='50' required="true" value="<%=vo.getPeriod() == null ? "" : vo.getPeriod()%>"/>
         (<font color="red">*</font>)
       </td>
       <td align="right">
         <i18n:message key="housekeeping_building.duration"/> : 
       </td>
       <td>
         <input type='text' name='duration' size='30' maxLength='50' required="true" value="<%=vo.getDuration() == null ? "" : vo.getDuration()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.acc_bank"/> : 
       </td>
       <td>
         <input type='text' name='acc_bank' size='30' maxLength='50' required="true" value="<%=vo.getAcc_bank() == null ? "" : vo.getAcc_bank()%>"/>
         (<font color="red">*</font>)
       </td>
       <td align="right">
         <i18n:message key="housekeeping_building.acc_name"/> : 
       </td>
       <td>
         <input type='text' name='acc_name' size='30' maxLength='50' required="true" value="<%=vo.getAcc_name()==null?"":vo.getAcc_name()%>"/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.acc_no"/> : 
       </td>
       <td>
         <input type='text' name='acc_no' size='30' maxLength='50' required="true" value="<%=vo.getAcc_no()==null?"":vo.getAcc_no()%>"/>
         (<font color="red">*</font>)
       </td>
       <td align="right">
         <i18n:message key="housekeeping_building.area"/> : 
       </td>
       <td>
         <input type='text' name='area' size='30' maxLength='10' required="true" value="<%=vo.getArea()%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.free_month"/> : 
       </td>
       <td>
         <input type='text' name='free_month' size='30' maxLength='10' value="<%=vo.getFree_month()%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
       </td>
       <td align="right">
         <i18n:message key="housekeeping_building.free_period"/> : 
       </td>
       <td>
         <input type='text' name='free_period' size='30' maxLength='30' value="<%=vo.getFree_period()==null?"":vo.getFree_period()%>"/>
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.month_rent_fee"/> : 
       </td>
       <td>
         <input type='text' name='month_rent_fee' size='30' maxLength='10' required="true" value="<%=vo.getMonth_rent_fee()%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
         (<font color="red">*</font>)
       </td>
       <td align="right">
         <i18n:message key="housekeeping_building.month_mang_fee"/> : 
       </td>
       <td>
         <input type='text' name='month_mang_fee' size='30' maxLength='10' required="true" value="<%=vo.getMonth_mang_fee()%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.depo_month"/> : 
       </td>
       <td>
         <input type='text' name='depo_month' size='30' maxLength='10' required="true" value="<%=vo.getDepo_month()%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.depo_fee_rent"/> : 
       </td>
       <td>
         <input type='text' name='depo_fee_rent' size='30' maxLength='10' required="true" value="<%=vo.getDepo_fee_rent()%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
         (<font color="red">*</font>)
       </td>
       <td align="right">
         <i18n:message key="housekeeping_building.depo_fee_prop"/> : 
       </td>
       <td>
         <input type='text' name='depo_fee_prop' size='30' maxLength='10' required="true" value="<%=vo.getDepo_fee_prop()%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
         (<font color="red">*</font>)
       </td>
     </tr>
     <tr>
       <td align="right">
         <i18n:message key="housekeeping_building.tot_amount"/> : 
       </td>
       <td>
         <input type='text' name='tot_amount' size='30' maxLength='10' required="true" value="<%=StringUtil.formatDouble(vo.getTot_amount())%>"
         onKeyPress='if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) event.returnValue = false'/>
         (<font color="red">*</font>)
       </td>
       <td align="right">
         <i18n:message key="housekeeping_contract.contract_no"/> : 
       </td>
       <td>
         <input type='text' name='contract_no' size='30' maxLength='50' required="true" value="<%=vo.getContract_no()==null?"":vo.getContract_no()%>"/>
         (<font color="red">*</font>)
       </td>
       <!--td align="right">
         <i18n:message key="housekeeping_building.org_name"/> : 
       </td>
       < td>
         <input type='text' name='org_name' size='30' maxLength='10' required="true" value="<%=vo.getOrg_name()==null?"":vo.getOrg_name()%>"/>
         (<font color="red">*</font>)
       </td> -->
     </tr>
     <tr>
       <td align="center" colspan="4">
         <input type="button" name="smBtn" value='<i18n:message key="button.submit"/>' onclick="submitCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
         <input type="button" name="returnBtn" value='<i18n:message key="button.back"/>' onclick="javascript:window.history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
       </td>
     </tr>
  </table>
</form>
</body>
</html>