<%-- 
Task_ID   Author   Modify_Date   Description
IT0958    Robin    11/02/2007    DS-007 During search form, it should add filter field "Requested_By"
IT1368    Mario    07/30/2013    Gray the forms locked by other users
--%>
<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.housekeeping.vo.StaffVO" %>
<%@page import="com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.helper.StaffTeamHelper"%>
<%@page import="com.aiait.framework.i18n.*,com.aiait.eflow.common.*"%>
<%@ include file="/common/loading.jsp" %>
<html>
<head>
<title>Wait For Deal Form List</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script language="javascript">
   var xmlhttp = createXMLHttpRequest();
  function getOptionList(formType){
    type = formType;
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+formType;
    xmlhttp.open("GET", url, true);
    xmlhttp.onreadystatechange=handleStateChange;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);	
  }
  
   function searchForm(isExport){
	  var comparer = 'finance';
      var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=listWaitForDealForm&isExport="+isExport;
      if(comparer=='<%=request.getParameter("comefrom")%>' && document.forms[0].formSystemId.value==""){
       	filterFinanceObj("<%=ParamConfigHelper.getInstance().getParamValue("fin_batch_forms")%>");
       	return;
      } else if(comparer=='<%=request.getParameter("comefrom")%>'){
		url += "&comefrom=finance";
		document.forms[0].processType.value='';
      }
      if(document.forms[0].beginSubmissionDate.value.Trim()!=""){
        if(isDate(document.forms[0].beginSubmissionDate,"Begin Submission Date")==false){
          return;
        }
      }
      if(document.forms[0].endSubmissionDate.value.Trim()!=""){
        if(isDate(document.forms[0].endSubmissionDate,"End Submission Date")==false){
          return;
        }
      }
      if(document.forms[0].beginSubmissionDate.value.Trim()!="" && document.forms[0].endSubmissionDate.value.Trim()!=""){
         if(compareDate(document.forms[0].beginSubmissionDate.value.Trim(),document.forms[0].endSubmissionDate.value.Trim())==false){
            alert("Begin Date must be earlier or equal to End Date");
            document.forms[0].beginSubmissionDate.focus();
            return;
         }
      }
      if(isExport=="1"||isExport=="2")document.forms[0].target="_blank"
      document.forms[0].action = url;
      document.forms[0].submit();
   }

   function approve(btn) {
       if (checkSelect("id") <= 0) {
           alert('<i18n:message key="common.check_select_form" />');
           return;
       }
       var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=batchApproveSetting";
       returnValue = showModalDialog(url, window, "dialogWidth:350px;dialogHeight:200px;help:0;status:0;resizeable:1;");
       if (returnValue == undefined) {
           // Cancel
           return;
       }
       //alert("returnValue: " + returnValue);
       url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=batchApprove&" + returnValue;
       document.forms[0].action = url;
       document.forms[0].processType.value = "";
       document.forms[0].submit();
       btn.disabled = true;
   }

   function reject(btn) {
       if (checkSelect("id") <= 0) {
           alert('<i18n:message key="common.check_select_form" />');
           return;
       }
       var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=batchRejectSetting";
       returnValue = showModalDialog(url, window, "dialogWidth:350px;dialogHeight:200px;help:0;status:0;resizeable:1;");
       if (returnValue == undefined) {
           // Cancel
           return;
       }
       //alert("returnValue: " + returnValue);
       url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=batchReject&" + returnValue;
       document.forms[0].action = url;
       document.forms[0].submit();
       btn.disabled = true;
   }

   // For CHO ePayment
   function batchPayment(btn) {
       $("processType").value="";
       if (checkSelect("id") <= 0) {
           alert('<i18n:message key="common.check_select_form" />');
           return;
       }
       var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=batchPaymentSetting";
       returnValue = showModalDialog(url, window, "dialogWidth:350px;dialogHeight:270px;help:0;status:0;resizeable:1;");
       if (returnValue == undefined) {
           // Cancel
           return;
       }
       //alert("returnValue: " + returnValue);
       url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=batchPayment&" + returnValue;
       document.forms[0].action = url;
       document.forms[0].submit();
       btn.disabled = true;
   }
   
   function selectCompany() {
       var url = "<%=request.getContextPath()%>/teamManageAction.it?method=getTeamList";

       var orgId = document.getElementById("orgId").value;
   	  var param = "orgId=" + orgId + "&enableSearchAll=true";
       
    	  var myAjax = new Ajax.Request(
           url,
           {
           method:"get",
           parameters:param,
           asynchronous:false,
           setRequestHeader:{"If-Modified-Since":"0"},
           onComplete:function(x) {
               var teamCode = document.getElementById("teamCode");
               clearSelect(teamCode);
               teamCode.appendChild(createOptionEle("", "", true));
               var options = x.responseXML.getElementsByTagName("team");
               for (var i = 0; i < options.length; i++) {
                   teamCode.appendChild(createElementWithValue(options[i], "code"));
               }
               selectTeam();
           },
           onError:function(x) {
                   alert('Failed to get the team list!');
           }
           }
       );
   }

   function selectTeam() {
       var url = "<%=request.getContextPath()%>/userManageAction.it?method=searchStaffByName";

       var orgId = document.getElementById("orgId").value;
       var teamCode = document.getElementById("teamCode").value;
       var param = "orgId=" + orgId + "&teamCode=" + teamCode;

    	  var myAjax = new Ajax.Request(
           url,
           {
           method:"get",
           parameters:param,
           asynchronous:false,
           setRequestHeader:{"If-Modified-Since":"0"},
           onComplete:function(x) {
               var requestedBy = document.getElementById("requestedBy");
               clearSelect(requestedBy);
               requestedBy.appendChild(createOptionEle("", "", true));
               var options = x.responseXML.getElementsByTagName("staff");
               for (var i = 0; i < options.length; i++) {
                   requestedBy.appendChild(createElementWithValue(options[i], "code"));
               }
           },
           onError:function(x) {
                   alert('Failed to get the staff list!');
           }
           }
       );
   }
  function filterFinanceObj(scope) //   
  {
	  var scope1 = scope;
	  var formSysIdObj = document.getElementById('formSystemId');
	  if(formSysIdObj.value!='')scope1=formSysIdObj.options[formSysIdObj.selectedIndex].text.split(" ")[0];
      var allFormOptions = document.getElementById('formSystemId').options; 
      var alltbDetailUsed = document.all('mytable').rows; 
      var tmp = scope1.split(";"); 
      for(var i=1;i<=alltbDetailUsed.length;i++)   
      {   
         if(alltbDetailUsed[i]!=null && 
         	alltbDetailUsed[i].all('id')!=null)   
         { 
           var j=0;
           for(;j<tmp.length;j++){			
      		var reg = new RegExp("^"+tmp[j]+"_\\d{8}_\\d{2,3}");
      		if(reg.test(alltbDetailUsed[i].all('id').value))break;
      	   }
      	   if(j==tmp.length){          
           	document.all('mytable').deleteRow(i);   
           	i=i-1;
           }
         }
      }

      var tmp = scope.split(";"); 
      for(var i=allFormOptions.length-1; i>=0; i--){
          var j=0;
          for(;j<tmp.length;j++){			
     		var reg = new RegExp("^"+tmp[j]+"\\s-\\s");
     		if(reg.test(allFormOptions[i].text)||allFormOptions[i].value=='')break;
     	  }
     	  if(j==tmp.length){          
     		document.getElementById('formSystemId').remove(i);
          }    		  
      }
     
      document.all.FinBtn.style.display='inline';
      document.getElementById('formType').disabled=true;
  }
</script>
<%
  Collection formList = (ArrayList)request.getAttribute("dealFormList");
  String formSystemId = (String)request.getParameter("formSystemId");
  Collection formSelectList = (ArrayList)request.getAttribute("formSelectList"); // select form list
  String requestNo = (String)request.getParameter("requestNo");
  String formType = (String)request.getParameter("formType");
  String beginSubmissionDate = (String)request.getParameter("beginSubmissionDate");
  String endSubmissionDate = (String)request.getParameter("endSubmissionDate");
  String comeFrom = (String)request.getParameter("comefrom");
  String orgId = (String) request.getParameter("orgId");
  String teamCode = (String) request.getParameter("teamCode");
  String requestedBy = (String)request.getParameter("requestedBy");
  Collection companyList = (Collection) request.getAttribute("companyList");
  Collection teamList = (Collection) request.getAttribute("teamList");
  Collection staffList = (Collection) request.getAttribute("staffList");
  if(comeFrom!=null && "left".equals(comeFrom)){
	  beginSubmissionDate = (String)request.getAttribute("beginSubmissionDate");
	  endSubmissionDate = (String)request.getAttribute("endSubmissionDate");
  }
  
  String processType = request.getParameter("processType");
  String checkOnly = request.getParameter("checkOnly");
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  
  boolean contentField2 = false;
  boolean contentField3 = false;
  String contentFields = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_LIST_CONTENT_FIELDS);
  if (contentFields != null && !"".equals(contentFields)) {
	  String[] aryContentFields = contentFields.split(";");
	  for (int i = 0; i < aryContentFields.length; i++) {
	      if (aryContentFields[i].equals("2")) {
	          contentField2 = true;
	      } else if (aryContentFields[i].equals("3")) {
	          contentField3 = true;
        }
	  }
  }
%>
<body>
<form nane=AVActionForm method="post"> 
<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="navigate.waiting_list"/></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1"><i18n:message key="module.waiting_list"/></TD>
   </TR>
   <TR > 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.request_no"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
      <INPUT Name="requestNo" Type="text" value="<%=requestNo==null?"":requestNo%>" class="text2" style="WIDTH: 130px" value="" id="requestNo" size="20">
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form_type"/></span></div></TD>
      <TD width=35% height="20" ><font size="2"> 
        <select name="formType" onchange="getOptionList(this.value)">
          <option value="">All</option>
          <% 
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if(typeList!=null && typeList.size()>0){
  		      Iterator it = typeList.iterator();
  		      while(it.hasNext()){
  			    FormTypeVO typeVo = (FormTypeVO)it.next();
          %>
          <option value="<%=typeVo.getFormTypeId()%>" <%=(formType!=null && (typeVo.getFormTypeId()).equals(formType))?"selected" : ""%>><%=typeVo.getFormTypeName()%></option>
          <%}
          }
          %>
        </select>
      </TD>
    </TR>
    <tr>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form"/></span></div></TD>
      <TD width=35% height="20" colspan='3'><font size="2"> 
        <select name="formSystemId">
        <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        <% 
           if(formSelectList!=null && formSelectList.size()>0){
        	   Iterator formIt = formSelectList.iterator();
        	   while(formIt.hasNext()){
        		   FormManageVO form = (FormManageVO)formIt.next();
        		   out.print("<option value='" + form.getFormSystemId()+"'");
        		   if(formSystemId!=null && !"".equals(formSystemId) && formSystemId.equals(""+form.getFormSystemId())){
        			   out.print(" selected ");
        		   }
        		   out.print(">"+form.getFormId()+" - "+form.getFormName()+"</option>");
               }
         }%>
       </select>
      </TD>
    </tr>
    <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_date_from"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
       <INPUT Name="beginSubmissionDate" onclick='setday(this)'  Type="text" value="<%=(beginSubmissionDate==null||"left".equals(request.getParameter("comefrom")))?"":beginSubmissionDate%>" 
       class="text2" style="WIDTH: 130px" id="beginSubmissionDate">(mm/dd/yyyy)
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_date_to"/></span></div></TD>
      <TD width=35% height="20" ><font size="2"> 
       <INPUT Name="endSubmissionDate" onclick='setday(this)'  Type="text" value="<%=endSubmissionDate==null?"":endSubmissionDate%>" class="text2" style="WIDTH: 130px" id="endSubmissionDate">(mm/dd/yyyy) 
      </TD>
    </TR>
    <TR>
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.company"/></span></div></TD>
      <TD width="35%" height="20">
        <select id="orgId" name="orgId" onchange="selectCompany()">
          <option value=""></option>
         <%
           if (companyList != null) {
               Iterator it = companyList.iterator();
               while (it.hasNext()) {
                   CompanyVO company = (CompanyVO) it.next();
                   String selected = company.getOrgId().equals(orgId) ? "selected" : "";
                   out.print("<option value='" + company.getOrgId() + "' " + selected + ">" + company.getOrgName() + "</>");
               }
           }
         %>
        </select>
      </TD>
      <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.team"/></span></div></TD>
      <TD width="35%" height="20">
        <select id="teamCode" name="teamCode" onchange="selectTeam()">
          <option value=""></option>
         <%
           if (teamList != null) {
        	   Iterator teamIt = teamList.iterator();
        	   while (teamIt.hasNext()) {
        		   TeamVO team = (TeamVO) teamIt.next();
        		   String selected = team.getTeamCode().equals(teamCode) ? "selected" : "";
        		   out.print("<option value='" + team.getTeamCode() + "' " + selected + ">" + team.getTeamName().trim() + "</>");
        	   }
           }
         %>
        </select>
      </TD>
   </TR>
    <tr>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.request_by"/></span></div></TD>
      <TD width=35% height="20" colspan='1'>
      <select id="requestedBy" name="requestedBy">
      <option value=""></option>
      <%
       if (staffList != null) {
    	   Iterator staffIt = staffList.iterator();
    	   while(staffIt.hasNext()){
    		   StaffVO staff = (StaffVO)staffIt.next();
    		   if(staff.getStaffCode().equals(requestedBy)){
    		     out.println("<option value='"+staff.getStaffCode()+"' selected>"+staff.getStaffName()+"</option>");
    		   }else{
    			   out.println("<option value='"+staff.getStaffCode()+"'>"+staff.getStaffName()+"</option>");
    		   }
    	   }
       }
      %>
      </select>
      </TD>
        <TD width="15%" height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="epayment.process_type"/></span></div></TD>
        <TD width="35%" height="20">
        <select name="processType">
        <option value=""></option>
                    <option value="<%=CommonName.NODE_TYPE_APPROVAL %>" <%=(CommonName.NODE_TYPE_APPROVAL.equals(processType))?"selected":"" %> ><i18n:message key="flow_node.nodetype_approval"/></option>
                    <option value="<%=CommonName.NODE_TYPE_PROCESS %>" <%=(CommonName.NODE_TYPE_PROCESS.equals(processType))?"selected":"" %> ><i18n:message key="flow_node.nodetype_process"/></option>
                    <option value="<%=CommonName.NODE_TYPE_WAITING %>" <%=(CommonName.NODE_TYPE_WAITING.equals(processType)) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_waiting"/></option>
        <%
        if (CompanyHelper.getInstance().getEFlowCompany().equals(
                CompanyHelper.EFlow_AIA_CHINA)) {
        %>
			        <option value="<%=CommonName.NODE_TYPE_PAYMENT %>" <%=(CommonName.NODE_TYPE_PAYMENT.equals(processType)) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_payment"/></option>
			        <option value="F" <%=("F".equals(processType)) ? "selected" : ""%>><i18n:message key="flow_node.nodetype_finance"/></option>
        <%
        }
        %>
         </select>
         <%
        if (CompanyHelper.getInstance().getEFlowCompany().equals(
                CompanyHelper.EFlow_AIA_CHINA)) {
        %>
        &nbsp;&nbsp;
        <input id="checkOnly" name="checkOnly" type="checkbox" value="Y" <%="Y".equals(checkOnly) ? "checked" : ""%>>
        <i18n:message key="epayment.for_check_only"/>
        <%
        }
        %>
        </TD>
    </tr>
    <tr>
      <td colspan='4' align='center'>
         <input type="button" name="searchBtn" value="<i18n:message key="button.search"/>" onclick="searchForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         &nbsp;&nbsp;
         <input type="Reset" name="resetBtn" value="<i18n:message key="button.reset"/>" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         &nbsp;&nbsp;
          <input type="button" name="expBtn" value="<i18n:message key="button.export_excel"/>" onclick="searchForm(1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">

        <%
        if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)){
        %>
         &nbsp;&nbsp;
          <input type="button" name="expBtn" value="<i18n:message key="button.export_payment_form"/>" onclick="searchForm(2)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <%
        }
        %>
        
        <%
        if (CommonName.NODE_TYPE_WAITING.equals(processType)) {
        %>
         &nbsp;&nbsp;
         <input type="button" name="approveBtn" value="<i18n:message key="button.approve"/>" onclick="approve(this)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <%
        }
        %>         
         &nbsp;&nbsp;
         <input type="button" name="rejectBtn" value="<i18n:message key="button.reject"/>" onclick="reject(this)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <%
        if (CommonName.NODE_TYPE_PAYMENT.equals(processType)) {
        %>
         &nbsp;&nbsp;
         <input type="button" name="paymentBtn" value="<i18n:message key="epayment.batch_payment"/>" onclick="batchPayment(this)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        <%
        }
        %>        
         &nbsp;&nbsp;
         <input type="button" name="FinBtn" value="<i18n:message key="button.fin_batch"/>" onclick="approve(this)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" style='display:none'>
        
      </td>
    </tr>
 </TABLE>
 
  <table width="100%"  border="0" cellpadding="0" cellspacing="1"  style="border:1px #8899cc solid;">
    <tr>
      <td class="liebiao_nr2" width="5%" align='center'><image src='<%=request.getContextPath()%>/images/new1.gif' alt="New"></td>
      <td class="liebiao_nr5" width="8%"><i18n:message key="form.status_new"/></td>
      <td class="liebiao_nr2" width="5%" align='center'><image src='<%=request.getContextPath()%>/images/lock.gif' alt="Locked"></td>
      <td class="liebiao_nr5" width="8%"><i18n:message key="form.status_locked"/></td>   
      <td class="liebiao_nr2" width="5%" align='center'><image src='<%=request.getContextPath()%>/images/urgent.gif' alt="Urgent"></td>
      <td class="liebiao_nr5" width="8%"><i18n:message key="form.status_urgent"/></td>
      <td class="liebiao_nr2" width="5%" align='center'><image src='<%=request.getContextPath()%>/images/deputy.gif' alt="Deputy"></td>
      <td class="liebiao_nr5" width="8%"><i18n:message key="form.status_deputy"/></td>    
      <td class="liebiao_nr2" width="5%" align='center'><image src='<%=request.getContextPath()%>/images/expert_advise.gif' alt="Advise"></td>
      <td class="liebiao_nr5" width="8%"><i18n:message key="form.status_for_advice"/></td>
      <td class="liebiao_nr2" width="5%" align='center'><image src='<%=request.getContextPath()%>/images/invite_expert.gif' alt="Invited"></td>
      <td class="liebiao_nr5" width="8%"><i18n:message key="form.status_invited_adviser"/></td>
	  <td class="liebiao_nr2" width="5%" align='center'><image src='<%=request.getContextPath()%>/images/expert_replied.gif' alt="Advised"></td>
      <td class="liebiao_nr5" width="8%"><i18n:message key="form.status_expert_replied"/></td>        
    </tr>
  </table>
  <br>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center'><input type="checkbox" name="allBtn" onclick="selectAll(this, 'id')"></td>
            <td align='center' ><i18n:message key="common.request_no"/></td>
            <td align='center' ><i18n:message key="common.request_form"/></td>
            <td align='center' ><i18n:message key="common.highlight_content"/></td>
<% if (contentField2) { %>
            <td align='center' ><i18n:message key="common.highlight_content"/> 2</td>
<% }
   if (contentField3) {%>
            <td align='center' ><i18n:message key="common.highlight_content"/> 3</td>
<% } %>            
            <td align='center' ><i18n:message key="common.status"/></td>
            <td align='center' ><i18n:message key="common.submit_date"/></td>            
<%if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {%>
            <td align='center' ><i18n:message key="common.company"/></td>			<%}%>
            <td align='center' ><i18n:message key="common.request_by"/></td>
            <td align='center' ><i18n:message key="common.processed_by"/></td>
            <td align='center'><i18n:message key="common.remaining"/></td>
          </tr>
          <%
            if(formList!=null){
            	int i=1;
            	SimpleDateFormat   df   =new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	Iterator formIt = formList.iterator();
            while(formIt.hasNext()){
            	WorkFlowProcessVO vo = (WorkFlowProcessVO)formIt.next();
            	Date cDate = null;
            	if(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr())){
            	   cDate = df.parse(vo.getSubmissionDateStr());
            	}
            	String somebodyLocked = "";
            	if("1".equals(vo.getInProcess()) && !vo.getInProcessStaffCode().equals(currentStaff.getStaffCode())
            			&& (vo.getInvitedExpert()+",").indexOf(currentStaff.getStaffCode())==-1){
            		somebodyLocked=" style=\"color:#AFAFAF\" ";
            	}            	
          %>

            <tr class="tr_change" <%=somebodyLocked %>>
              <td align='center'><input type="checkbox" name="id" value="<%=vo.getRequestNo()%>"></td>
              <td >
              <%if("2".equals(vo.getIsUrgent())){%>
                 <image src='<%=request.getContextPath()%>/images/urgent.gif' alt="Urgent">
              <%}%>
              <%if("0".equals(vo.getOpenFlag())){%>
                <image src='<%=request.getContextPath()%>/images/new1.gif' alt="New">
              <%}%>
              <%if("1".equals(vo.getInProcess())){
                    String staffName = StaffTeamHelper.getInstance().getStaffNameByCode(vo.getInProcessStaffCode()).trim();
					String title = staffName + " " + I18NMessageHelper.getMessage("form.locked_form");
              %>
                <a href='#' title='<%=title%>'><img border='0' src='<%=request.getContextPath()%>/images/lock.gif'></a>
              <%}%>
              
              <%
				String strDeputy = "";
				if (vo.isDealByDeputy()) {
					String staffName = StaffTeamHelper.getInstance().getStaffNameByCode(vo.getOriginProcessor()).trim();
					strDeputy = I18NMessageHelper.getMessage("form.deal_by_deputy", new String[] { "", staffName, "" });
					strDeputy = "<a href='#' title='" + strDeputy + "'><img border=0 src='" + request.getContextPath() + "/images/deputy.gif'></a>";
				}
              %>
              <%=strDeputy%>
              <%if(vo.getExpertAdviceFlag()){%>
                <image src='<%=request.getContextPath()%>/images/expert_advise.gif' alt="Advise">
                <a href="javascript:openFormWithLayer('<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=advise&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>')"
                	<%=vo.getHtmlTitleAttr()%> <%=somebodyLocked %>><%=vo.getRequestNo()%>
              <%}else{%>
                <%if (vo.isExpertReplied()) {%>
                  <image src='<%=request.getContextPath()%>/images/expert_replied.gif' alt="Advised">
                <%} else if (vo.getInvitedExpert() != null && !"".equals(vo.getInvitedExpert())) {%>
                  <image src='<%=request.getContextPath()%>/images/invite_expert.gif' alt="Invited">
                <%}%>
                <a href="javascript:openFormWithLayer('<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=deal&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>')"
                	<%=vo.getHtmlTitleAttr()%> <%=somebodyLocked %>><%=vo.getRequestNo()%>
              <%}%>
              &nbsp;&nbsp;</td>
              <td ><%=vo.getFormName()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getHighlightContent()%></td>
<% if (contentField2) { %>
              <td><%=vo.getHighlightContent2()%></td>
<% }
   if (contentField3) {%>
              <td><%=vo.getHighlightContent3()%></td>
<% } %>                  
              <td ><%=DataMapUtil.covertNodeStatus(vo.getStatus())%>&nbsp;&nbsp;</td>
              <td><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%>&nbsp;&nbsp;</td>
              <%
              StaffVO requester = StaffTeamHelper.getInstance().getStaffByCode(vo.getRequestStaffCode());
              String orgName = requester == null ? "" : CompanyHelper.getInstance().getOrgName(requester.getOrgId());

              if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {
              %>
              <td><%=orgName%>&nbsp;&nbsp;</td><%}%>
              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getRequestStaffCode())%>&nbsp;&nbsp;</td>
              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getPreviousProcessor())%>&nbsp;&nbsp;</td>
              <td align='right'>
              <%
                String remainTime = vo.getRemainTime();
                //IT1197
                //if(!"".equals(remainTime) && !"0".equals(remainTime)){
                if(!"2".equals(vo.getIsUrgent()) && !"".equals(remainTime) && !"0".equals(remainTime)){
                	double tmp = Double.parseDouble(remainTime);
                	if(tmp<0){
                		out.print("<b><font color='red'>" + tmp + "</font></b>");
                	}else{
                		out.print(tmp);
                	}
                }
              %>
              &nbsp;&nbsp;
              </td>
            </tr>
          <%
            i++;}
          }else{
          %>
            <tr class="liebiao_nr2">
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
<% if (contentField2) { %>
              <td>&nbsp;&nbsp;</td>
<% }
   if (contentField3) {%>
              <td>&nbsp;&nbsp;</td>
<% } %>                
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>             
<%if (CompanyHelper.getInstance().getEFlowCompany().equals(CompanyHelper.EFlow_AIA_CHINA)) {%>
			  <td>&nbsp;&nbsp;</td>
<% } %>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
            </tr>
          <%}%>
        </table>
        <input type=hidden name=comefrom value=''>
</form>
      <div id="loading2" style="display:none;align:center">
	     <div class="loading-indicator">
		    It is loading page...
	     </div>
      </div>
  </body>
  </html>
 <script language="javascript">
	setResizeAble(mytable);
	window.onload=function(){
	enableTooltips();
<%if("finance".equals(request.getParameter("comefrom"))){%>
    if(document.all('mytable').rows.length > 1){//Table header
        document.forms[0].processType.value="F";
        document.forms[0].comefrom.value="finance";        
        filterFinanceObj("<%=ParamConfigHelper.getInstance().getParamValue("fin_batch_forms")%>");
    } else {
        document.forms[0].processType.value="";
        document.forms[0].comefrom.value="";       
    }
<%}%>
	};
 </script>