<%@ page contentType="application/vnd.ms-excel;charset=UTF-8" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*, com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.framework.util.*" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>
<html>
<head>
	<title>Supplier</title>		
	<meta Content-Disposition="attachment; filename=formlist.xls">
	
</head>

<% 
	StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	Collection list = (Collection) request.getAttribute("supplierList");
	Collection teamList = StaffTeamHelper.getInstance().getTeamList();
%>
<body>
<form name="myForm" method="post">
   <table width="100%" bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
     <tr>
      <td colspan=4>
        <table width="100%"  border="1" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><i18n:message key="housekeeping_supplier.code"/></td>
            <td align='center' ><i18n:message key="common.company"/></td>            
            <td align='center' ><i18n:message key="housekeeping_supplier.type"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.name_c"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.name_e"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.product"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.eff_date"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.contacter"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.tel"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.address_c"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.address_e"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.province"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.city"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.bank"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.bank_account"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.team_name"/></td>
            <td align='center' ><i18n:message key="housekeeping_supplier.team_contacter"/></td>
            <td align='center' ><i18n:message key="common.status"/></td>
          </tr>
          <%
			if (list != null) {
				int i = 1;
				Iterator it = list.iterator();
				while (it.hasNext()) {
				    SupplierVO vo = (SupplierVO) it.next();
          %>
            <tr class="tr_change">
            	<td><%=          vo.getCode()       %></td>
              	<td><%=CompanyHelper.getInstance().getOrgName(vo.getOrgId())%>&nbsp;</td>
              	<td><%=             vo.getType()       %></td>
				<td><%=             vo.getNameC()      %></td>
				<td><%=           vo.getNameE()        %></td>
				<td><%=             vo.getProduct()    %></td>
				<td><%=         	vo.getEffDate()    %></td>
				<td><%=       	   vo.getContacter()   %></td>
				<td><%=           vo.getTel()          %></td>
				<td><%=           vo.getAddressC()     %></td>
				<td><%=           vo.getAddressE()     %></td>
				<td><%=           vo.getProvince()     %></td>
				<td><%=           vo.getCity()         %></td>
				<td><%=           vo.getBank()         %></td>
				<td><%=           vo.getBankAccount()  %></td>
				<td><%=           vo.getTeamName()     %></td>
				<td><%=           vo.getTeamContacter()%></td>
				<td><%=           vo.getStatus()       %></td>
            </tr>
          <%i++;}
          }%>
        </table>
      </td>
     </tr>
   </table>
</form>
</body>
</html>