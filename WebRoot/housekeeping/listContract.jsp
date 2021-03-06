<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.*, com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.framework.util.*" %>
<%@page import="com.aiait.eflow.util.StringUtil" %>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<head>
	<title>Contract</title>
	  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
	<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script language="javascript">
	  function addCode() {
	     var url = "<%=request.getContextPath()%>/contractAction.it?method=editCntr&editType=new";
	     window.location = url;
	  }
	  
	  function deleteCode() {
	    if (checkSelect('contractNo') <= 0) {
	      alert("You have not selected any records to delete!");
	      return;
	    }
	    if (confirm("Are you sure to delete the selected records")) {
	      document.forms[0].action = "<%=request.getContextPath()%>/contractAction.it?method=deleteCntr";
	      document.forms[0].submit();
	    }
	  }
	  function fileUpload(){
	      var url = "<%=request.getContextPath()%>/contractAction.it?method=showExcelTemplateSelect";
	      openCenterWindow(url,450,100);
	  }
	</script>
</head>

<% 
  Collection list = (Collection) request.getAttribute("cntrList");
%>
<body>
<form name="myForm" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr><!--
 <tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="housekeeping_contract.location"/></font></strong>
 	 </td>
 </tr>
--></table>
   <table width="100%"  border="0">
     <tr class="tr1">
       <td align='center'><B><i18n:message key="housekeeping_contract.title"/></B></td>
     </tr>
     <tr>
       <td align='left'>
		<input type="button" name="addBtn" value='<i18n:message key="button.add"/>' onclick="addCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
		&nbsp;
		<input type="button" name="deleteBtn" value='<i18n:message key="button.delete"/>' onclick="deleteCode()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>
		&nbsp;
		<input type="button" name="uploadBtn1" value='<i18n:message key="button.selectFile"/>' onclick="fileUpload()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"/>  
       </td>
     </tr>
     <tr>
      <td>
        <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'codeId')"></td>
            <td align='center' ><i18n:message key="housekeeping_contract.contract_no"/></td>
            <td align='center' ><i18n:message key="housekeeping_contract.receive_date"/></td>
            <td align='center' ><i18n:message key="housekeeping_contract.contract_name"/></td>
            <td align='center' ><i18n:message key="housekeeping_contract.org_id"/></td>
            <td align='center' ><i18n:message key="housekeeping_contract.amount"/></td>
          </tr>
          <%
			if (list != null) {
				int i = 1;
				Iterator it = list.iterator();
				while (it.hasNext()) {
				    ContractVO vo = (ContractVO) it.next();
          %>
            <tr class="tr_change">
              <td align='center' ><input type="checkbox" name="contractNo" value="<%=vo.getContractNo()%>"></td>
              <td>
                <a href='<%=request.getContextPath()%>/contractAction.it?method=editCntr&editType=edit&contractNo=<%=String.valueOf(vo.getContractNo())%>'><%=String.valueOf(vo.getContractNo())%></a>
              </td>
              <td><%=vo.getReceiveDate()==null?"":vo.getReceiveDate()%>&nbsp;</td>
              <td><%=vo.getContractName()%>&nbsp;</td>
              <td><%=vo.getOrgName()%>&nbsp;</td>
              <td><%=StringUtil.formatDouble(vo.getAmount())%>&nbsp;</td>
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
   <script language="javascript">
	setResizeAble(mytable);
 </script>