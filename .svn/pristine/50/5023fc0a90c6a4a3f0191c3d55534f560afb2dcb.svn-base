<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper,com.aiait.eflow.common.helper.*" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%@page import="com.aiait.framework.i18n.I18NMessageHelper"%>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ include file="/common/loading.jsp" %>
<%@ include file="/common/ajaxmessage.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>
<html>
<head>
<title>Payment Form Inquiry</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/tableColorChange.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/BubbleTooltips.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">
   var xmlhttp = createXMLHttpRequest();
   function getOptionList(formType){
    type = formType;
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+formType+"&moduleId=<%=ModuleOperateName.MODULE_FORM_INQUIRY%>";
    xmlhttp.open("GET", url, true);
    var objId = "formSystemId";
    xmlhttp.onreadystatechange=handleStateChange;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);	
  }
  function getStaffList(orgId){
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


  function verifyCompletedDates(){
	      if(document.forms[0].beginCompletedDate.value!=""){
	        if(isDate(document.forms[0].beginCompletedDate,"<%=I18NMessageHelper.getMessage("common.complete_date_from")%>")==false){
	          return false;
	        }
	      }
	      if(document.forms[0].endCompletedDate.value!=""){
	        if(isDate(document.forms[0].endCompletedDate,"<%=I18NMessageHelper.getMessage("common.complete_date_to")%>")==false){
	          return false;
	        }
	      }
	      if(document.forms[0].beginCompletedDate.value!="" && document.forms[0].endCompletedDate.value!=""){
	         if(compareDate(document.forms[0].beginCompletedDate.value.Trim(),document.forms[0].endCompletedDate.value.Trim())==false){
	            alert(from_to_date_compare);
	            document.forms[0].beginCompletedDate.focus();
	            return false;
	         }
	      }
	      
	      return true;
	}   
		
   function searchForm(){
      if(document.forms[0].beginSubmissionDate.value.Trim()!=""){
        if(isDate(document.forms[0].beginSubmissionDate,"Submitted From")==false){
          return;
        }
      }
      if(document.forms[0].endSubmissionDate.value.Trim()!=""){
        if(isDate(document.forms[0].endSubmissionDate,"Submitted To")==false){
          return;
        }
      }
      if(document.forms[0].beginSubmissionDate.value.Trim()!="" && document.forms[0].endSubmissionDate.value.Trim()!=""){
         if(compareDate(document.forms[0].beginSubmissionDate.value.Trim(),document.forms[0].endSubmissionDate.value.Trim())==false){
            alert(from_to_date_compare);
            document.forms[0].beginSubmissionDate.focus();
            return;
         }
      }

      if (!(verifyCompletedDates())) return;
      
      var url = "<%=request.getContextPath()%>/epaymentReportAction.it?method=paymentInquiry";
      document.forms[0].action = url;
      document.forms[0].submit();
   }
  
   /**
   IT0973
   **/
   function openSelectStaffWindow(requestNo,nodeId,requestStaffCode,formSystemId){
    if(confirm(confirm_reassign_form)){
       document.all['reassignRequestNo'].value = requestNo;
       document.all['reassignNodeId'].value = nodeId;
       document.all['reassignRequestStaffCode'].value = requestStaffCode;
       document.all['reassignFormSystemId'].value = formSystemId;
       var returnValue = showModalDialog('<%=request.getContextPath()%>/userManageAction.it?method=enterSelectStaff&moduleId=<%=ModuleOperateName.MODULE_FORM_INQUIRY%>&operateId=<%=ModuleOperateName.OPER_FORM_INQUIRY_REASSIGN%>&tempDate='+Math.random()*100000,
                   window,'dialogWidth:475px; dialogHeight:500px;help:0;status:0;resizable:1;');
       if (returnValue != null){
          document.all['selectStaff'].value = returnValue;
          reassignForms();
       }
    }
   }
   
   function reassignForms(){
       var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=adjustProcessorForm"
                 +"&reassignRequestNo="+document.all['reassignRequestNo'].value
                 +"&reassignNodeId="+document.all['reassignNodeId'].value
                 +"&selectStaff="+document.all['selectStaff'].value
                 +"&reassignRequestStaffCode="+document.all['reassignRequestStaffCode'].value
                 +"&reassignFormSystemId="+document.all['reassignFormSystemId'].value;
       xmlhttp.open("POST", url, true);
       xmlhttp.onreadystatechange=function(){
           if(xmlhttp.readyState == 4){
		      if(xmlhttp.status == 200){
		           result = xmlhttp.responseText;
		           if(result=="success"){
   		             hideMessage();
		             alert(operate_success);
		             window.document.forms[0].submit();
		           }else{
		             alert("Fail:"+result);
		             hideMessage();
		           }
		      }
		   }else{
		      showMessage(operating_tip);
		   }
       }
       xmlhttp.setRequestHeader("If-Modified-Since","0");
       xmlhttp.send(null);	
   }
   
   /**
   IT0958 DS-013 Begin
   **/
   function openExportPage(){
      if(document.forms[0].formSystemId.value==""){
        alert(select_export_form);
        document.forms[0].formSystemId.focus();
        return;
      }

      if (!(verifyCompletedDates())) return;
      
      var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=enterSettingExportExcel&formSystemId="+document.forms[0].formSystemId.value
                +"&status="+document.forms[0].status.value+"&requestedBy="+document.forms[0].requestedBy.value
                +"&beginDate="+document.forms[0].beginSubmissionDate.value+"&endDate="+document.forms[0].endSubmissionDate.value
                +"&beginCompletedDate="+document.forms[0].beginCompletedDate.value+"&endCompletedDate="+document.forms[0].endCompletedDate.value
                +"&requestNo="+document.forms[0].requestNo.value+"&companyId="+document.forms[0].companyId.value;
      var date = new Date();
      var w_name = "w_" + date.getTime();
      openCenterWindow(url, 600, 350, w_name);
   }
   //IT0958 DS-013 End
   
	function openExportQueryPage() {
		if (document.forms[0].beginSubmissionDate.value.Trim() != "") {
			if (isDate(document.forms[0].beginSubmissionDate, "Submitted From") == false) {
				return;
			}
		}
		if (document.forms[0].endSubmissionDate.value.Trim() != "") {
			if (isDate(document.forms[0].endSubmissionDate, "Submitted To") == false) {
				return;
			}
		}
		if (document.forms[0].beginSubmissionDate.value.Trim() != "" && document.forms[0].endSubmissionDate.value.Trim() != "") {
			if (compareDate(document.forms[0].beginSubmissionDate.value.Trim(), document.forms[0].endSubmissionDate.value.Trim()) == false) {
				alert(from_to_date_compare);
				document.forms[0].beginSubmissionDate.focus();
				return;
			}
		}

		if (!(verifyCompletedDates())) return;
		
		var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=exportInquiryForm";
		document.forms[0].action = url;
		document.forms[0].target = "_blank";
		document.forms[0].submit();
		document.forms[0].target = "";
		//openCenterWindow(url,600,350);
	}

	function exportPDF() {
	    if (checkSelect('requestNos') <= 0) {
	        alert(have_no_select);
	        return;
	    }
	    if (confirm(confirm_export_pdfs) == false) {
	        return;
	    }
        var date = new Date();
        var w_name = "pdf_" + date.getTime();
        // alert("w_name: " + w_name);
	    openCenterWindow2(w_name, 800, 600);
	    var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=exportPDF";
		document.forms[0].action = url;
		document.forms[0].target = w_name;
		document.forms[0].submit();
		document.forms[0].target = "";
	}

</script>
<%
  StaffVO currentStaff = (StaffVO)session.getAttribute(
		CommonName.CURRENT_STAFF_INFOR);
  AuthorityHelper authority = AuthorityHelper.getInstance();
  
  Collection formList = (ArrayList)request.getAttribute("inquiryFormList"); // search result list
  
  Collection formSelectList = (ArrayList)request.getAttribute("formSelectList"); // select form list
  
  String formSystemId = (String)request.getParameter("formSystemId");
  
  String requestedBy = (String)request.getParameter("requestedBy");
  
  String companyId = (String)request.getParameter("companyId");
  
  Collection staffList = (Collection) request.getAttribute("staffList");
  
  String requestNo = (String)request.getParameter("requestNo");
  if(requestNo==null){
	  requestNo = (String)request.getAttribute("requestNo");
  }
  
  //String formType = (String)request.getParameter("formType");
  String formType = "12";
  
  String status = (String)request.getParameter("status");
  status = status == null ? "04" : status;
  
  String beginSubmissionDate = (String)request.getParameter("beginSubmissionDate");
  String endSubmissionDate = (String)request.getParameter("endSubmissionDate");
  
  String beginCompletedDate = (String)request.getParameter("beginCompletedDate");
  String endCompletedDate = (String)request.getParameter("endCompletedDate");
  
  String needquery = (String)request.getParameter("needquery");
  if(needquery!=null && "false".equals(needquery)){
	  //beginSubmissionDate = (String)request.getAttribute("beginSubmissionDate");
	  //endSubmissionDate = (String)request.getAttribute("endSubmissionDate");
	  
	  beginCompletedDate = (String)request.getAttribute("beginCompletedDate");
	  endCompletedDate = (String)request.getAttribute("endCompletedDate");	  
  }
  String lockedForm = (String)request.getParameter("lockedForm");
%>
</head>
<body>
<FORM nane=AVActionForm method="post"> 
<input type="hidden" name="selectStaff">
<input type="hidden" name="reassignRequestNo">
<input type="hidden" name="reassignFormSystemId">
<input type="hidden" name="reassignRequestStaffCode">
<input type="hidden" name="reassignNodeId">

<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="report_paymentforminquiry.location"/></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1"><i18n:message key="menu.report.payment_inquiry"/></TD>
   </TR>
   <TR > 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.request_no"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
      <INPUT Name="requestNo" Type="text" value="<%=requestNo==null?"":requestNo%>" class="text2" style="WIDTH: 130px" id="requestNo">
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form_type"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
        <select name="formType">
          <% 
            Collection typeList = FormTypeHelper.getInstance().getFormTypeList();
            if(typeList!=null && typeList.size()>0){
  		      Iterator it = typeList.iterator();
  		      while(it.hasNext()){
  			    FormTypeVO typeVo = (FormTypeVO)it.next();
  			    if(typeVo.getFormTypeId().equals("12")){
  			    %>
  			    <option value="<%=typeVo.getFormTypeId()%>" selected><%=typeVo.getFormTypeName()%></option>
  			    <% 	
  			    }
              }
          }
          %>
        </select>
      </TD>
    </TR>
    <tr>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form"/></span></div></TD>
      <TD width=30% height="20" colspan='3'><font size="2"> 
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
    <tr>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.company"/></span></div></TD>
      <TD width=35% height="20">
      <select name="companyId" onchange="getStaffList(this.value)">
      <option value=""></option>
      <%
      	if (companyId == null) {
      		companyId = currentStaff.getOrgId();
      	}
       Collection companyList = currentStaff.getOwnCompanyList(); //CompanyHelper.getInstance().getOwnCompanyList(currentStaff.getOrgId());//CompanyHelper.getInstance().getCompanyList();
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
      %>
      </select>
      </TD>
      <!-- New Company List above -------------------------------------------------------------------------------->
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.request_by"/></span></div></TD>
      <TD width=35% height="20">
      <select name="requestedBy">
      <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      <%
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
       }
      %>
      </select>
      </TD>
    </tr>
   <TR> 
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_date_from"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
       <INPUT Name="beginSubmissionDate" onclick='setday(this)'  Type="text" value="<%=beginSubmissionDate==null?"":beginSubmissionDate%>" class="text2" style="WIDTH: 130px" id="beginSubmissionDate">(mm/dd/yyyy)
      </font>
      </TD>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.submit_date_to"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
       <INPUT Name="endSubmissionDate" onclick='setday(this)'  Type="text" value="<%=endSubmissionDate==null?"":endSubmissionDate%>" class="text2" style="WIDTH: 130px" id="endSubmissionDate">(mm/dd/yyyy) 
      </TD>
    </TR>
   <TR> 
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.complete_date_from"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
       <INPUT Name="beginCompletedDate" onclick='setday(this)'  Type="text" value="<%=beginCompletedDate==null?"":beginCompletedDate%>" class="text2" style="WIDTH: 130px" id="beginCompletedDate">(mm/dd/yyyy)
      </font>
      </TD>
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.complete_date_to"/></span></div></TD>
      <TD width=30% height="20" ><font size="2"> 
       <INPUT Name="endCompletedDate" onclick='setday(this)'  Type="text" value="<%=endCompletedDate==null?"":endCompletedDate%>" class="text2" style="WIDTH: 130px" id="endCompletedDate">(mm/dd/yyyy) 
      </TD>
    </TR>    
   <TR> 
      <TD width=18% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="epayment.cashier"/></span></div></TD>
      <TD width=35% height="20"> 
        <select name="cashier">
      <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      <%
       String cashier = request.getParameter("cashier");
       cashier = cashier == null ? currentStaff.getStaffCode() : cashier;
       Collection cashierList = (List) request.getAttribute("cashierList");
       if (cashierList != null) {
           Iterator cashierIt = cashierList.iterator();
           while (cashierIt.hasNext()) {
               StaffVO staff = (StaffVO) cashierIt.next();
               if (cashier.equals(staff.getStaffCode())) {
                   out.println("<option value='" + staff.getStaffCode() + "' selected>" + staff.getStaffName() + "</option>");
               } else {
                   out.println("<option value='" + staff.getStaffCode() + "'>" + staff.getStaffName() + "</option>");
               }
           }
       }
      %>
      </select>
      </TD>
      <!-- moved from above to here -->     
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.status"/></span></div></TD>
      <TD width=35% height="20">
        <select name="status" id="status">
          <option value="">All</option>
          <option value="00" <%=(status!=null && "00".equals(status))?"selected" : ""%>>Drafted</option>
          <option value="01" <%=(status!=null && "01".equals(status))?"selected" : ""%>>Submitted</option>
          <option value="02" <%=(status!=null && "02".equals(status))?"selected" : ""%>>IN Progress</option>
          <option value="03" <%=(status!=null && "03".equals(status))?"selected" : ""%>>Rejected</option>
          <option value="04" <%=(status!=null && "04".equals(status))?"selected" : ""%>>Completed</option>
        </select>
      </TD>           
    </TR>
    <tr>
      <td colspan='4' align='center' noWrap>
         <input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="searchForm()">&nbsp;&nbsp;

         <input type="button" name="exportPDFBtn" value='<i18n:message key="button.export_pdf"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="exportPDF()">&nbsp;&nbsp;

         <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
      </td>
    </tr>
 </TABLE>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class=sortable id=mytable style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center' >&nbsp;</td>
            <td align='center' ><input type="checkbox" name="allBtn" onclick="selectAll(this,'requestNos')"></td>
            <td align='center' ><i18n:message key="common.request_no"/></td>
            <td align='center' ><i18n:message key="common.request_form"/></td>
            <td align='center' ><i18n:message key="common.highlight_content"/></td>
            <td align='center' ><i18n:message key="common.status"/></td>
            <td align='center' ><i18n:message key="common.submit_draft_date"/></td>
            <td align='center' ><i18n:message key="common.request_by"/></td>
            <td align='center' ><i18n:message key="common.processed_by"/></td>
            <td align='center' ><i18n:message key="common.handle_date"/></td>
            <td align='center' ><i18n:message key="common.processing_by"/></td>
          </tr>
          <%
            if(formList!=null){
            	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	Iterator formIt = formList.iterator();
            	int i = 1;
            while(formIt.hasNext()){
            	WorkFlowProcessVO vo = (WorkFlowProcessVO)formIt.next();
            	Date cDate = null;
            	if(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr())){
            	   cDate = df.parse(vo.getSubmissionDateStr());
            	}
            	String handleDate = "";
            	if (vo.getHandleDateStr() != null && !"".equals(vo.getHandleDateStr())) {
            	    handleDate = StringUtil.getDateStr(df.parse(vo.getHandleDateStr()), "MM/dd/yyyy HH:mm:ss");
            	}
          %>
            <tr class="tr_change">
              <td align='center'>&nbsp;<%=i%>&nbsp;</td>
              <td align='center'><input type="checkbox" name="requestNos" value="<%=vo.getRequestNo()%>"></td>
              <td ><a href="<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&requestNo=<%=vo.getRequestNo()%>&formSystemId=<%=vo.getFormSystemId()%>"
              		<%=vo.getHtmlTitleAttr()%>><%=vo.getRequestNo()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getFormName()%>&nbsp;&nbsp;</td>
              <td ><%=vo.getHighlightContent()%></td>
              <td ><%=DataMapUtil.covertNodeStatus(vo.getStatus())%>&nbsp;&nbsp;</td>
              <td ><%=(vo.getSubmissionDateStr()!=null && !"".equals(vo.getSubmissionDateStr()))?StringUtil.getDateStr(cDate,"MM/dd/yyyy HH:mm:ss"):""%>&nbsp;&nbsp;</td>
              <td><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getRequestStaffCode())%>&nbsp;&nbsp;</td>
              <td ><%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getPreviousProcessor())%>&nbsp;&nbsp;</td>
              <td ><%=handleDate%></td>
              <td >
              <%=((!"-1".equals(vo.getNodeId()) && vo.getIsDeputy()!=null && "1".equals(vo.getIsDeputy()) && vo.getOriginProcessor()!=null && vo.getOriginProcessor().indexOf(",")==-1 && (!"".equals(vo.getCurrentProcessor())))?"<a href='#' title=\"It is "+StaffTeamHelper.getInstance().getStaffNameByCode(vo.getOriginProcessor()).trim()+"'s deputy.\"><img  border=0 src='"+request.getContextPath()+"/images/deputy.gif'></a>":"")%>
              <%=StaffTeamHelper.getInstance().getStaffNameByCode(vo.getCurrentProcessor())%>&nbsp;&nbsp;</td>
            </tr>
          <%
           i++; }
          }else{
          %>
            <tr class="liebiao_nr2">
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
              <td>&nbsp;&nbsp;</td>
            </tr>
          <%}%>
        </table>
        <pageTag:page action="/epaymentReportAction.it?method=paymentInquiry"></pageTag:page>
        </form>
  </body>
  </html>
   <script language="javascript">
	//setResizeAble(mytable);
	//ZebraTable.stripe('mytable');
	window.onload=function(){enableTooltips()};
 </script>
