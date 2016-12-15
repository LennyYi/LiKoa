<%@ include file="/common/head.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.housekeeping.vo.StaffVO,java.text.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.report.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.*" %>
<%@page import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper,com.aiait.eflow.housekeeping.vo.CompanyVO"%>
<%@page import="com.aiait.framework.i18n.I18NMessageHelper"%>
<%@ taglib uri="/WEB-INF/purview.tld" prefix="purview" %>
<%@ include file="/common/loading.jsp" %>
<%@ include file="/common/ajaxmessage.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-page.tld" prefix="pageTag" %>
<html>
<head>
<title>Inter Payment Inquiry</title>
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
        if (!(verifyCompletedDates())) return;
        
        var url = "<%=request.getContextPath()%>/epaymentReportAction.it?method=interPaymentInquiry";
        document.forms[0].action = url;
        document.forms[0].submit();
    }
   
	function exportExcel() {
	    if (!(verifyCompletedDates())) return;
        
	    var url = "<%=request.getContextPath()%>/epaymentReportAction.it?method=interPaymentReportXls";
	    document.forms[0].action = url;
		document.forms[0].target = "_blank";
		document.forms[0].submit();
		document.forms[0].target = "";
	}

</script>
<%
  StaffVO currentStaff = (StaffVO)session.getAttribute(CommonName.CURRENT_STAFF_INFOR);
  AuthorityHelper authority = AuthorityHelper.getInstance();
  
  List inquiryList = (List) request.getAttribute("inquiryList");
  
  List formSelectList = (List) request.getAttribute("formSelectList");
  
  String formSystemId = request.getParameter("formSystemId");
  String requestOrgId = request.getParameter("requestOrgId");
  String costOrgId = request.getParameter("costOrgId");
  
  //String formType = request.getParameter("formType");
  String formType = "12";
  
  String beginCompletedDate = request.getParameter("beginCompletedDate");
  String endCompletedDate = request.getParameter("endCompletedDate");
  
  String needquery = request.getParameter("needquery");
  if (needquery != null && "false".equals(needquery)) {
	  beginCompletedDate = (String)request.getAttribute("beginCompletedDate");
	  endCompletedDate = (String)request.getAttribute("endCompletedDate");	  
  }
%>
</head>
<body>
<FORM nane=AVActionForm method="post"> 

<table border="0" width="100%" cellspacing="0" cellpadding="0">
 <tr>
 	<td height="10"></td>
 </tr>
 <!--<tr>
 	<td>
 	  <strong><font color='#5980BB' family='Times New Roman'><i18n:message key="report_interpaymentinquiry.location"/></font></strong>
 	 </td>
 </tr>
--></table>
 <TABLE WIDTH=100% bordercolor="#6595D6" style="border-collapse:collapse;"  BORDER=1 CELLPADDING=3 CELLSPACING=0 class="tr0" >
   <TR align="center" >
      <TD height="25" colspan="4" class="tr1"><i18n:message key="menu.report.interpayment_inquiry"/></TD>
   </TR>
   <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form_type"/></span></div></TD>
      <TD width=35% height="20"><font size="2"> 
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
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.form"/></span></div></TD>
      <TD width=35% height="20"><font size="2"> 
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
    </TR>
    <tr>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_interpaymentinquiry.requestorg"/></span></div></TD>
      <TD width=35% height="20">
      <select name="requestOrgId">
      <%
      	if (requestOrgId == null) {
      	  requestOrgId = currentStaff.getOrgId();
      	}
      FormSectionFieldVO costCompanyVo = SystemFieldHelper.getInstance()
		.getSystemFieldById("cost_company"); 
       Collection companyList = costCompanyVo.getOptionList();
       //Collection companyList = CompanyHelper.getInstance().getCompanyList();
    	
       if(companyList!=null){
    	   out.println("<option value='' selected> </option>");
    	   Iterator companyIt = companyList.iterator();
    	   while(companyIt.hasNext()){
    		   //CompanyVO company = (CompanyVO)companyIt.next();
               DictionaryDataVO company = (DictionaryDataVO) companyIt.next();
    		   
    		   if(company.getId().equals(requestOrgId)){
    		     out.println("<option value='"+company.getId()+"' selected>"+company.getValue()+"</option>");
    		   }else{
    			   out.println("<option value='"+company.getId()+"'>"+company.getValue()+"</option>");
    		   }
    	   }
       }
      %>
      </select>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="report_interpaymentinquiry.costorg"/></span></div></TD>
      <TD width=35% height="20">
      <select name="costOrgId">
      <%
      	if (costOrgId == null) {
      	    costOrgId = currentStaff.getOrgId();
      	}
       if(companyList!=null){
    	   out.println("<option value='' selected> </option>");
    	   Iterator companyIt = companyList.iterator();
    	   while(companyIt.hasNext()){
    		   //CompanyVO company = (CompanyVO)companyIt.next();
               DictionaryDataVO company = (DictionaryDataVO) companyIt.next();
    		   
    		   if(company.getId().equals(requestOrgId)){
    		     out.println("<option value='"+company.getId()+"' selected>"+company.getValue()+"</option>");
    		   }else{
    			   out.println("<option value='"+company.getId()+"'>"+company.getValue()+"</option>");
    		   }
    	   }
       }
      %>
      </select>
      </TD>
    </tr>
   <TR> 
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.complete_date_from"/></span></div></TD>
      <TD width=35% height="20" > 
      <font size="2"> 
       <INPUT Name="beginCompletedDate" onclick='setday(this)'  Type="text" value="<%=beginCompletedDate==null?"":beginCompletedDate%>" class="text2" style="WIDTH: 130px" id="beginCompletedDate">(mm/dd/yyyy)
      </font>
      </TD>
      <TD width=15% height="20" class="tr3"><div align="right"><span class="style1"><i18n:message key="common.complete_date_to"/></span></div></TD>
      <TD width=35% height="20" ><font size="2"> 
       <INPUT Name="endCompletedDate" onclick='setday(this)'  Type="text" value="<%=endCompletedDate==null?"":endCompletedDate%>" class="text2" style="WIDTH: 130px" id="endCompletedDate">(mm/dd/yyyy) 
      </TD>
    </TR>
    <tr>
      <td colspan='4' align='center' noWrap>
         <input type="button" name="searchBtn" value='<i18n:message key="button.search"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="searchForm()">&nbsp;&nbsp;

         <input type="button" name="exportPDFBtn" value='<i18n:message key="button.export_excel"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'" onclick="exportExcel()">&nbsp;&nbsp;

         <input type="Reset" name="resetBtn" value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
      </td>
    </tr>
 </TABLE>
 <table width="100%"  border="0" cellpadding="0" cellspacing="1" class="sortable" id="mytable" style="border:1px #8899cc solid;">
          <tr class="liebiao_tou">
            <td align='center'>No.</td>
            <td align='center'><i18n:message key="common.date"/></td>
            <td align='center'><i18n:message key="report_interpaymentinquiry.noteno"/></td>
            <td align='center'><i18n:message key="report_interpaymentinquiry.costorg"/></td>
            <td align='center'><i18n:message key="common.request_no"/></td>
            <td align='center'><i18n:message key="common.highlight_content"/></td>
            <td align='center'><i18n:message key="common.amount"/></td>
            <td align='center'><i18n:message key="common.request_by"/></td>
            <td align='center'><i18n:message key="report_interpaymentinquiry.requestorg"/></td>
            <td align='center'><i18n:message key="common.team"/></td>
            <td align='center'><i18n:message key="report_interpaymentinquiry.invoicestatus"/></td>
            <td align='center'><i18n:message key="form.status"/></td>
          </tr>
          <%
            if (inquiryList != null && !inquiryList.isEmpty()) {
                CompanyHelper companyHelper = CompanyHelper.getInstance();
                StaffTeamHelper staffTeamHelper = StaffTeamHelper.getInstance();
            	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            	DecimalFormat nf = new DecimalFormat("#,##0.00");
            	Iterator it = inquiryList.iterator();
            	int i = 0;
                while (it.hasNext()) {
                    i++;
                    InterPaymentVO vo = (InterPaymentVO) it.next();
          %>
            <tr class="tr_change">
              <td align='center'><%=i%></td>
              <td align='center'><%=df.format(vo.getDate())%></td>
              <td><%=vo.getNoteNo()%></td>
              <td><%=companyHelper.getOrgName(vo.getCostOrg())%></td>
              <td><a href="<%=request.getContextPath()%>/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&requestNo=<%=vo.getRequestNo()%>"><%=vo.getRequestNo()%></a></td>
              <td><%=vo.getContent()%></td>
              <td align='right'><%=nf.format(vo.getAmount())%></td>
              <td><%=staffTeamHelper.getStaffNameByCode(vo.getRequestBy())%></td>
              <td><%=companyHelper.getOrgName(vo.getRequestOrg())%></td>
              <td><%=staffTeamHelper.getTeamNameByCode(vo.getTeam())%></td>
              <td><%=vo.getInvStatus2()%></td>
              <td><%=DataMapUtil.covertNodeStatus(vo.getStatus())%></td>
            </tr>
          <%
                }
            }
          %>
  </table>
</form>
</body>
</html>