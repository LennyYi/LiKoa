<%--
    Task_ID    Author     Modify_Date    Description
1.  ePayment   Mario.Cao   05/27/2009     initial
--%>

<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.wkf.util.DataMapUtil"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%@ include file="/common/loading.jsp"%>
<%@ include file="/common/ajaxmessage.jsp" %>
<%@ page contentType="text/html;charset=GBK" %> 

<%Collection formList = (ArrayList) request
			.getAttribute("inquiryFormList"); // search result list

	Collection formSelectList = (ArrayList) request
			.getAttribute("formSelectList"); // select form list

	String formSystemId = (String) request.getParameter("formSystemId");

	String requestNo = (String) request.getParameter("requestNo");
	if (requestNo == null) {
		requestNo = (String) request.getAttribute("requestNo");
	}
	String formType = (String) request.getParameter("formType");
	String status = (String) request.getParameter("status");
	String beginSubmissionDate = (String) request
			.getParameter("beginSubmissionDate");
	String endSubmissionDate = (String) request
			.getParameter("endSubmissionDate");
	String needquery = (String) request.getParameter("needquery");
	if (needquery != null && "false".equals(needquery)) {
		beginSubmissionDate = (String) request
				.getAttribute("beginSubmissionDate");
		endSubmissionDate = (String) request
				.getAttribute("endSubmissionDate");
	}
	  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	  AuthorityHelper authority = AuthorityHelper.getInstance();
	  String type = request.getParameter("reportType").toLowerCase();
	  String requestedBy = (String)request.getParameter("requestedBy");
	  String mergeAccount = (String) request.getParameter("mergeAccount");
%>
	
<html>
<head>
  <META HTTP-EQUIV="pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<title>Personal Invoice Report</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>

<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/tableColorChange.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>

<script language="javascript">

function document.onreadystatechange()
{
	try
	{
		if (document.readyState == "complete") 
		{
			getOptionList(document.all['formType'].value);
	     	delNode("loading");
	    }
    }catch(e){
    	alert("Fail to load page!");
    }
}

//DIV
function  delNode(nodeId)
{   
  try
  {   
	  var div =document.getElementById(nodeId);  
	  if(div !==null)
	  {
		  div.parentNode.removeChild(div);   
		  div=null;    
		  CollectGarbage(); 
	  }  
  }catch(e){   
  	   alert("It can find ID"+nodeId);
  }   
}
   var xmlhttp = createXMLHttpRequest();
   var formIds;
   var candidateForms = "<%=ParamConfigHelper.getInstance().getParamValue
   	(request.getParameter("reportType").toLowerCase()+"_form_ids")%>" + ";";
   var count = 1;
   
   function getOptionList(formType){
    type = formType;
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+formType+"&moduleId=<%=ModuleOperateName.MODULE_COMMON_QUERY%>";
    xmlhttp.open("GET", url, true);
    var objId = "formSystemId";
    xmlhttp.onreadystatechange=handleStateChange;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);	
    $('formType').disabled = true;
  }
   function getStaffList(orgId){

		<%
		if(type.equals("invoice")){ 
			 out.println("return; ");	
		}
		%>
	   
   	var url = "<%=request.getContextPath()%>/userManageAction.it?method=getCompanyStaffList";
    	var param = "orgId="+orgId;
    	var myAjax = new Ajax.Request(
        url,
       {
           method:"post",       //
           parameters:param,   //
           setRequestHeader:{"If-Modified-Since":"0"},     //
           onComplete:function(x){    //
                  updateStaffList(x.responseXML);
           },
           onError:function(x){          //
                   alert('Fail to get staff list for company');
           } 
       } 
      ); 
 	}
   function handleStateChange(){
	     if(xmlhttp.readyState == 4) {
	        if(xmlhttp.status == 200) {
	          updateList();
	        }
	     }
	  }
	function updateList(){
	  var select;
	  select = document.getElementById("formSystemId");
	  select.value = "";
	  clearSelect(select);
	  //alert(xmlhttp.responseXML.xml)
	  var options = xmlhttp.responseXML.getElementsByTagName("option");
	  var arr = new Array();
	  formIds = "";
	  for (var i = 0, n = options.length; i < n; i++) {
		if(candidateForms.indexOf(options[i].getAttribute("id")+";")>=0){
			//alert(options[i]);
	      	select.appendChild(createElementWithValue(options[i],"id"));
	      	//arr[i] = createElementWithValue(options[i],"id");
	      	formIds = formIds + options[i].getAttribute("id") + ",";
		}
	  }
	}

	function confirmInterCompany(){
		<%
		if(!type.equals("invoice")){ 
			 out.println("return true;");	
		}
		%>
		
		if('<%= currentStaff.getOrgId()%>' != $("companyId").value){
			if(confirm("您是否需要跨公司查询?")){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}

	  
  function searchForm(reportType){
	  if(!confirmInterCompany()) return ;
	   
	  var formName = document.all['formSystemId'].value;
      /*if(formName==""){
        alert(select_search_form);

        for(var i=0;i<document.all['formSystemId'].length;i++){
        	document.all['formSystemId'].options[i].selected=true;
        }
        document.all['formSystemId'].focus();
        return;
      }*/
      var url = "<%=request.getContextPath()%>/epaymentReportAction.it?method=summaryReportHandle&reportType="
          +reportType+"&formIds="+formIds;
      document.forms[0].action = url;
      document.forms[0].target="iframeName";
      document.forms[0].submit();
      if($("mail_form_id"))$("mail_form_id").value = "";
  }
  
  function exportToExcel(){
	  if(!confirmInterCompany()) return ;
      /*if(document.forms[0].formSystemId.value==""){
        alert(select_export_form);
        document.forms[0].formSystemId.focus();
        return;
      }*/
      //var url = "<%=request.getContextPath()%>/epaymentReportAction.it?method=exportToExcel";
     var newWindow = window.open("","newwindow","height=1, width=1, top=0, left=0,toolbar =no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no");
	  /*if(document.all){     
       newWindow.moveTo(350,350)     
       //newWindow.resizeTo(600,350)     
      }     
     newWindow.location=url*/
     //alert(getOrderPhrase());
     document.forms[0].action = "<%=request.getContextPath()%>/epaymentReportAction.it?method=exportToExcel&display=EXCEL&orderphrase="+getOrderPhrase();
     document.forms[0].target = newWindow;
     document.forms[0].submit();
     newWindow.close();	
  }

  function getOrderPhrase(){
  	//var table = [0].firstChild.data;
  	if(document.frames("iframeName").document.getElementsByTagName('table').length < 1) return '';
  	var table = document.frames("iframeName").document.getElementsByTagName('table')[0];
    if (table.rows && table.rows.length > 0) {
        var firstRow = table.rows[0];
    }
    var i = document.frames("iframeName").SORT_COLUMN_INDEX;
    if (!firstRow||!i) return '';
    var cell = firstRow.cells[i];
    var strAscDesc = '';
    if(cell.innerText.indexOf("↓")>-1) strAscDesc = ' Desc';
    else if(cell.innerText.indexOf("↑")>-1) strAscDesc = ' Asc';
    
	if(cell.getAttribute("order")!='') return cell.getAttribute("order") + strAscDesc;
	else return '';
  }
  function exportToPDF() {
      if (!confirmInterCompany()) return;
      document.forms[0].action = "<%=request.getContextPath()%>/epaymentReportAction.it?method=exportToExcel&display=PDF&orderphrase="+getOrderPhrase();
      document.forms[0].target = "_blank";
      document.forms[0].submit();
      document.forms[0].target = "";
  }

  function saveRemark(reportType) {
      var remarkReqNo = document.getElementById("remarkReqNo");
      if (remarkReqNo.value == "") {
          alert("请输入: (编辑备注) 表单号");
          remarkReqNo.focus();
          return;
      }
      var saveRemark = document.getElementById("save_remark");
      saveRemark.value = "Y";
      searchForm(reportType);
      saveRemark.value = "N";
  }
  
  function notify(rtype) {
	  if(!confirmInterCompany()) return ;
      var message = confirm_notify;
      if ($("mail_form_id").value == "") {
          alert('<i18n:message key="common.check_select_form" />');
          return;
      }
      if (!confirm(message)) {
          return;
      }
      url = '<%=request.getContextPath()%>/epaymentReportAction.it?method=overdueNotify&reportType=' + rtype + '&formIds='+formIds;
      document.forms[0].action = url;
      document.forms[0].submit();
  }
  
  function receive() {
	  if(!confirmInterCompany()) return ;
      var message = confirm_receive_invoice;
      if ($("mail_form_id").value == "") {
          alert('<i18n:message key="common.check_select_form" />');
          return;
      }
      if (!confirm(message)) {
          return;
      }
      url = "<%=request.getContextPath()%>/epaymentReportAction.it?method=invoiceReceive&reportType=Invoice&formIds="+formIds;
      document.forms[0].action = url;
      document.forms[0].submit();
  }
  
  function repaid() {
      var message = "确认标记所有选中为已还款？（此操作不可恢复）";
      if ($("mail_form_id").value == "") {
          alert('<i18n:message key="common.check_select_form" />');
          return;
      }
      if ($("repayable").value == "false") {
          alert('所选借款单有个别被在途的ePayment表单关联（参考ePayment ID列），现在不能标记还款，请从选择中去除');
          return;
      }
      if (!confirm(message)) {
          return;
      }
      url = "<%=request.getContextPath()%>/epaymentReportAction.it?method=caRepaid&reportType=CA&formIds="+formIds;
      document.forms[0].action = url;
      document.forms[0].submit();
  }
  
</script>

<!--<body onload="getOptionList(document.all['formType'].value)">-->
<body>
<FORM name=AVActionForm method="post">
<input type="hidden" name="count">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td height="10"></td>
	</tr>
	<!--<tr>
		<td><strong><font color='#5980BB' family='Times New Roman'>
<%if(type.equals("invoice")){ %>
		<i18n:message key="report_invoicereport.location"/>
<%} else if(type.equals("payment")){ %>
		<i18n:message key="report_paymentreport.location"/>		
<%} else if(type.equals("staffpayment")){%>
		<i18n:message key="report_staffpaymentreport.location"/>
<%} else {%>
		<i18n:message key="report_careport.location"/>
<%} %>
		</font></strong></td>
	</tr>
--></table>
<TABLE WIDTH=100% bordercolor="#6595D6"
	style="border-collapse:collapse;" BORDER=1 CELLPADDING=3 CELLSPACING=0
	class="tr0" id="queryTable">
	<TR align="center">
		<TD height="25" colspan="8" class="tr1">
<%if(type.equals("invoice")){ %>
		<i18n:message key="report_invoicereport.title"/>
<%} else if(type.equals("payment")){ %>
		<i18n:message key="report_paymentreport.title"/>		
<%} else if(type.equals("staffpayment")){%>
		<i18n:message key="report_staffpaymentreport.title"/>
<%} else {%>
		<i18n:message key="report_careport.title"/>
<%} %>
		</TD>
	</TR>
	<tbody id='tbDetailPrepare'>
	<TR>
		<TD width=15% height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message key="common.form_type"/></span></div>
		</TD>
		<TD width=30% height="20" colspan='3'><font size="2"> <select
			name="formType" onchange="getOptionList(this.value)">
			<option value="">All</option>
          <% 
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if(typeList!=null && typeList.size()>0){
  		      Iterator it = typeList.iterator();
  		      while(it.hasNext()){
  			    FormTypeVO typeVo = (FormTypeVO)it.next();
  			    if(authority.checkAuthorityByFormType(currentStaff.getCurrentRoleId(),ModuleOperateName.MODULE_COMMON_QUERY,typeVo.getFormTypeId())){
  			    %>
  			    <option value="<%=typeVo.getFormTypeId()%>" 
  			    <%=(formType!=null && (typeVo.getFormTypeId()).equals(formType))?"selected" : ""%>
  			    <%=(formType==null && typeVo.getFormTypeName().toUpperCase().indexOf("PAYMENT")>=0)?"selected":""%>>
  			    <%=typeVo.getFormTypeName()%></option>
  			    <% 	
  			    }
              }
            }
          %>
		</select></TD>
	</TR>
	<tr>  <!-- Right side, Row 1 -->
		<TD width=18% height="50" class="tr3" rowspan=3>
		<div align="right"><span class="style1"><i18n:message key="common.form"/></span></div>
		</TD>
		<TD width=30% height="50" rowspan=3><font size="2"> 
		<select style="height:100%" multiple name="formSystemId" disabled>
		</select></TD> 
		<TD width=18% height="50" class="tr3">
		<div align="right"><%if(type.indexOf("payment")>-1){%>
				完成支付者
			<%} else {%>
				所属公司
			<%} %>
		</div>
		</TD>
		<TD width=30% height="50" ><font size="2"> 
		<%
		final String companyId = currentStaff.getOrgId();
		if(type.equals("invoice")||type.equals("ca")){ 
		 out.println("<select name='companyId' onchange='getStaffList(this.value)'><option value=''></option>");

		 Collection companyList = null;
		 if(type.equals("invoice")){
			 companyList = CompanyHelper.getInstance().getCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());//CompanyHelper.getInstance().getCompanyList();			 
		 }else{
			 companyList = currentStaff.getOwnCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());//CompanyHelper.getInstance().getCompanyList();
		 }
		 if(companyList!=null){
		  Iterator companyIt = companyList.iterator();
		  while(companyIt.hasNext()){
		   CompanyVO company = (CompanyVO)companyIt.next();
		   if(company.getOrgId().equals(companyId)){
		     out.println("<option value='"+company.getOrgId()+"' selected>"+company.getOrgName()+"</option>");
		   }else{
			   out.println("<option value='"+company.getOrgId()+"'>"+company.getOrgName()+"</option>");
		   }
		  }
		 }
		 out.println("<input name='cashierCD' type=hidden>");
		} else {
		 out.println("<select name='cashierCD'>");
		 Collection cashierList = (ArrayList) request.getAttribute("cashierList");
		 if(cashierList!=null){
		  Iterator cashierIt = cashierList.iterator();
		  while(cashierIt.hasNext()){
			  StaffVO staff = (StaffVO)cashierIt.next();
		   if(currentStaff.getStaffCode().equals(staff.getStaffCode())){
		     out.println("<option value='"+staff.getStaffCode()+"' selected>"+staff.getStaffName()+"</option>");
		   }else{
			   out.println("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName()+"</option>");
		   }
		  }
		 }
		 out.println("<input name='companyId' type=hidden><input name='cashierCD' type=hidden>");
		}
		%>
  		</select></TD>
  	</tr>	
  	<tr>	<!-- Right side, Row 2 -->
<%
String timetype = "";
String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
if(type.indexOf("payment")>-1){ 
%>
		<TD width=18% height="50" class="tr3"><div align="right">支付方式</div></TD>
		<TD width=30% height="50" ><font size="2">
		<select name="paymode" >
			<option value='01'>Cash</option>
			<option value='02' selected>Bank Transfer</option>
<%if(type.equals("payment")){ %>
			<option value='03'>Check</option>
			<!-- option value='05'>Batch Payment</option-->
<%} %>			
			<option value='06'>AIACS</option>
		</select></TD>
<%
	timetype = "完成";
} else {
	timetype = "递交";
	today = "";
	if(type.equals("ca")){
	%>
	<TD width=18% height="50" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.request_by"/></span></div></TD>
    <TD width=35% height="50" >
    <select name="requestedBy">
    <option value=""></option>
    <%
     Collection staffList = StaffTeamHelper.getInstance().getStaffList();
     if(staffList!=null){
  	   Iterator staffIt = staffList.iterator();
  	   while(staffIt.hasNext()){
  		   StaffVO staff = (StaffVO)staffIt.next();
  		   if(staff.getStaffCode().equals(requestedBy)){
  		     out.println("<option value='"+staff.getStaffCode()+"' selected>"+staff.getStaffName()+"</option>");
  		   }else{
  			   out.println("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName()+"</option>");
  		   }
  	   }
     }%>
    </select>
<%	} else if(type.equalsIgnoreCase("invoice")){
	%>
	<TD width=18% height="50" class="tr3"><div align="right"><span class="style1">适用流程</span></div></TD>
    <TD width=35% height="50" >	
	<%
	 out.println("<select name='flowOrgId' onchange='getStaffList(this.value)'><option value=''></option>");

	 Collection companyList = null;
	 if(type.equals("invoice")){
		 companyList = CompanyHelper.getInstance().getCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());//CompanyHelper.getInstance().getCompanyList();			 
	 }else{
		 companyList = currentStaff.getOwnCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());//CompanyHelper.getInstance().getCompanyList();
	 }
	 if(companyList!=null){
	  Iterator companyIt = companyList.iterator();
	  while(companyIt.hasNext()){
	   CompanyVO company = (CompanyVO)companyIt.next();
	   if(company.getOrgId().equals(companyId)){
	     out.println("<option value='"+company.getOrgId()+"' selected>"+company.getOrgName()+"</option>");
	   }else{
		   out.println("<option value='"+company.getOrgId()+"'>"+company.getOrgName()+"</option>");
	   }
	  }
	 }		
	} else {
	%>
		<TD width=18% height="50" class="tr3">&nbsp;</TD><TD>&nbsp;</TD>
	<%
}
  }%>
	</tr>
  	<tr>	<!-- Right side, Row 3 -->
<%
  if (type.equals("ca")) {
%>
		<TD width=18% height="50" class="tr3"><div align="right">状态</div></TD>
		<TD width=30% height="50" ><font size="2">
		<select name="status" >
			<option ></option>
			<option value='01'>已还款</option>
			<option value='02'>未还款</option>
		</select></TD>
<%} else if (type.equalsIgnoreCase("StaffPayment") || type.equalsIgnoreCase("payment")) {
%>
		<TD width=18% height="50" class="tr3"><div align="right">按银行账号汇总</div></TD>
		<TD width=30% height="50" ><font size="2">
		<select name="mergeAccount">
			<option value='Y'>&nbsp;&nbsp;是&nbsp;&nbsp;</option>
			<option value='N'>&nbsp;&nbsp;否&nbsp;&nbsp;</option>
		</select></TD>
<%
  } else {
%>
		<TD width=18% height="50" class="tr3"><div align="right">状态</div></TD>
		<TD width=30% height="50" ><font size="2">
		<select name="status" onchange="if(this.value=='01')$('beginDateStr').value=((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getYear();else $('beginDateStr').value='';">
			<option value='01'>已确认</option>
			<option value='02' selected>未确认</option>
		</select></TD>
<%}%>
	</tr>
	
    <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><%=timetype%>时间范围从</span></div></TD>
      <TD width=35% height="20" > 
        <input type="text" readonly name="beginDateStr" onclick='setday(this)' value="<%=((String)request.getParameter("beginDateStr")==null || "".equals((String)request.getParameter("beginDateStr"))?today:(String)request.getParameter("beginDateStr"))%>">(MM/DD/YYYY)
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><%=timetype%>时间范围至</span></div></TD>
      <TD width=30% height="20" >
        <input type="text" readonly name="endDateStr" onclick='setday(this)' value="<%=((String)request.getParameter("endDateStr")==null || "".equals((String)request.getParameter("endDateStr"))?today:(String)request.getParameter("endDateStr"))%>">(MM/DD/YYYY)
      </TD>
    </TR>
    
<%
if (type.equalsIgnoreCase("StaffPayment")) {
%>
    <TR>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1">(编辑备注) 表单号</span></div></TD>
      <TD width=35% height="20" >
        <input type="text" id="remarkReqNo" name="remarkReqNo">
        <input type="hidden" id="save_remark" name="save_remark">
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1">编辑备注</span></div></TD>
      <TD width=35% height="20" >
        <textarea rows="2" cols="40" id="remark" name="remark"></textarea>
      </TD>
    </TR>
<%
}
%>
	</tbody>
  </table>
 <table width="100%">
        <tr>
          <td align='center' colspan='2'>
            <input type="button" value='<i18n:message key="button.search"/>' name="subBtn" onclick="searchForm('<%=request.getParameter("reportType")%>')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
           <input type="button" disabled value='<i18n:message key="button.export_file"/>' name="expBtn" onclick="exportToExcel()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
           <input type="button" disabled value='<i18n:message key="button.export_pdf"/>' name="expPDFBtn" onclick="exportToPDF()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
<%if(type.equals("invoice")){ %>
           <input type="button" disabled name="notifyBtn" value="<i18n:message key="button.notify"/>" onclick="notify('<%=request.getParameter("reportType")%>')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           <input type=hidden name="mail_form_id" value="">
           <input type="button" disabled name="receiveBtn" value="<i18n:message key="button.mark_receive"/>" onclick="receive()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
<%} else if(type.equals("ca")){ %>
           <input type="button" disabled name="notifyBtn" value="<i18n:message key="button.notify"/>" onclick="notify('<%=request.getParameter("reportType")%>')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           <input type=hidden name="mail_form_id" value="">
           <input type=hidden name="repayable" value="true">
           <input type="button" disabled name="receiveBtn" value="<i18n:message key="button.mark_repaid"/>" onclick="repaid()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
<%} else if (type.equalsIgnoreCase("StaffPayment")) {%>
           <input type="button" name="remarkBtn" value="保存备注" onclick="saveRemark('<%=request.getParameter("reportType")%>')" class="btn3_mouseout" onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
<%}%>
          </td>
        </tr>
</table>
<br>
<p>提示：导出EXCEL可与下面字段名为粗体的列同步排序</p>
<table width="100%">
  <tr>
     <td width="100%">
	 <IFRAME frameBorder=0 id='iframeName' name="iframeName" scrolling="no" src="<%=request.getContextPath()%>/report/list<%=request.getParameter("reportType")%>Report.jsp" style="HEIGHT:100%;VISIBILITY:inherit;WIDTH:100%;Z-INDEX:1"></IFRAME>
	</td>
  </tr>
</table>
</FORM>	
</body>
</html>

   <script language="javascript">
if($("requestedBy"))getStaffList($("companyId").value);
 </script>

