<%-- 
New Page: IT0958    Robin    11/02/2007    DS-013 Form can export to excel file 
modified:	N/A		asnpg9a	 11/16/2009	  added search criteria "completed date".
--%>
<%@ include file="/common/head.jsp"%>
<%@ include file="/common/loading.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page
	import="java.util.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.formmanage.vo.*,
    com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.*"%>
<%
    StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);	
    FormManageVO form = (FormManageVO) request.getAttribute("form");
    List exportFields = (List) request.getAttribute("exportFields");
    HashMap exportFieldsMap = new HashMap();
    for (int i = 0; i < exportFields.size(); i++) {
        FormSectionFieldVO field = (FormSectionFieldVO) exportFields.get(i);
        exportFieldsMap.put(field.getSectionId() + "_" + field.getFieldId(), field);
    }
    
	String queryType = (String) request.getParameter("queryType");
	String status = (String) request.getParameter("status");
	String orgId = (String) request.getParameter("companyId");
	String requestedBy = (String) request.getParameter("requestedBy");
	String beginDate = (String) request.getParameter("beginDate");
	String endDate = (String) request.getParameter("endDate");
	String requestNo = (String) request.getParameter("requestNo");
	String beginCompletedDate = (String) request.getParameter("beginCompletedDate");
	String endCompletedDate = (String) request.getParameter("endCompletedDate");
	String processType = (String) request.getParameter("processType");
    
	if (status == null) {
		status = "";
	}
	
	orgId = orgId == null ? "" : orgId;
	
	if (requestedBy == null) {
		requestedBy = "";
	}
	if (beginDate == null) {
		beginDate = "";
	}
	if (endDate == null) {
		endDate = "";
	}
	if (queryType == null) {//01----form inquiry?,02-----Advance Query?
		queryType = "01";
	}
%>
<HTML>
<HEAD>
<title>Please select fields to export</title>
</HEAD>
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">
<STYLE type=text/css>
</STYLE>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<script language="javascript"
	src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript">
    function changeFileType(fileType){
      var obj = document.all['pageSize'];
      if(fileType=="pdf"){
        for(i=0;i<obj.length;i++){
           document.all['pageSize'][i].disabled = ""; 
        }
      }else{
        for(i=0;i<obj.length;i++){
           document.all['pageSize'][i].disabled = "true"; 
        }
      }
    }
    
    function exportExcel(){
      var conditionStr = "";
      var type = "<%=queryType%>";
      if(type=="02"){
        var cons = window.opener.document.getElementsByName('conditionStr');
        if(cons.length>0){ //?????????
          for(i=0;i<cons.length;i++){
            conditionStr = conditionStr + "&conditionStr="+cons[i].value;
          }
        }
      }
      if (confirm(exportexcel_sure)) {
          var date = new Date();
          var f_name = "excel_" + date.getTime();
   	      document.forms[0].action = "<%=request.getContextPath()%>/wkfProcessAction.it?method=exportToExcel" + conditionStr + "&f_name=" + f_name;
		  document.forms[0].submit();
   	      window.close();	
   	  }
	}
	
	window.onload=function(){resize(600);}
	
	function selectAll(checked) {
		var fields = document.forms[0].fieldId;
		for (var i = 0; i < fields.length; i++) {
			fields[i].checked = checked;
		}
	}

    function saveConfig(obj) {
        if (obj.checked) {
            alert(save_export_config);
        }
    }
	
  </script>
<BODY>
<form id="Form1" name="Form1" method="post" target="_blank"><input
	type="hidden" name="queryType" value="<%=queryType%>"> <input
	type="hidden" name="formSystemId" value="<%=form.getFormSystemId()%>">
	<input type="hidden" name="status" value="<%=status%>"> 
	<input type="hidden" name="orgId" value="<%=orgId%>"> <input
	type="hidden" name="requestedBy" value="<%=requestedBy%>"> <input
	type="hidden" name="beginDate" value="<%=beginDate%>"> <input
	type="hidden" name="endDate" value="<%=endDate%>"> <input
	type="hidden" name="requestNo" value="<%=requestNo%>">
 	<input type="hidden" name="beginCompletedDate" value="<%=beginCompletedDate%>">
 	<input type="hidden" name="endCompletedDate" value="<%=endCompletedDate%>">
    <input type="hidden" name="processType" value="<%=processType%>">
<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#6595D6" style="border-collapse: collapse;">
	<TR>
		<TD style="WIDTH: 100%" class="tr1" colspan='2' align='center'><b><%=form.getFormName()%></b>(<%=form.getFormId()%>)
		</TD>
	</TR>
	<TR>
		<TD style="WIDTH: 100%" class="tr3" colspan='2' align='left'>
            <%
            String selectAll = "";
            if (exportFieldsMap.isEmpty()) {
                selectAll = "checked";
            }
            %>
            <input type="checkbox" name="select_all" <%=selectAll%> onclick="selectAll(this.checked)">
			<i18n:message key="button.selectAll"/>&nbsp;&nbsp;
            <%if (AuthorityHelper.getInstance().checkAuthority(currentStaff.getCurrentRoleId(), ModuleOperateName.MODULE_FORM_MANAGE, ModuleOperateName.OPER_FORM_DESIGN)) {%>
            <input type="checkbox" name="save_config" value="Yes" onclick="saveConfig(this)">
            <i18n:message key="button.save_config"/></TD>
            <%}%>
	</TR>
	<%
		Collection sectionList = form.getSectionList();
		if (sectionList != null && sectionList.size() > 0) {
			Iterator sectionIt = sectionList.iterator();
			while (sectionIt.hasNext()) {
				FormSectionVO section = (FormSectionVO) sectionIt.next();
                // System.out.println("Section Type: " + section.getSectionType());
				if ("00".equals(section.getSectionType())) {
					continue;
				}
	%>
	<TR>
		<TD style="WIDTH: 30%" noWrap class='tr3'><%=section.getSectionId()%>.<%=section.getSectionRemark()%>
		</TD>
		<TD style="WIDTH: 70%" noWrap>
		<%
			Collection fieldList = section.getFieldList();
					if (fieldList != null && fieldList.size() > 0) {
						Iterator fieldIt = fieldList.iterator();
						int count = 1;
						while (fieldIt.hasNext()) {
							FormSectionFieldVO field = (FormSectionFieldVO) fieldIt.next();
                            if (field.getFieldId().equalsIgnoreCase("id")) {
                                continue;
                            }
                            String checked = "";
                            if (exportFieldsMap.isEmpty() || exportFieldsMap.get(field.getSectionId() + "_" + field.getFieldId()) != null) {
                                checked = "checked";
                            }
							out.println("<input type='checkbox' name='fieldId' " + checked + " value='"
											+ field.getSectionId()
											+ "||"
											+ field.getFieldId()
											+ "'>"
											+ field.getFieldLabel()
											+ "&nbsp;&nbsp;");
							if (count % 3 == 0) {
								out.print("<br>");
							}
							count++;
						}
					}
		%>
		</TD>
	</TR>
	<%
		}
		}
	%>
	<TR>
		<TD style="WIDTH: 30%" noWrap class='tr3'><i18n:message
			key="export_Excel.exportfiletype" /> :</TD>
		<td style="WIDTH: 70%" noWrap><input type="radio" name="fileType"
			value="excel" checked onclick="javascript:changeFileType(this.value)">Excel
		<input type="radio" name="fileType" value="pdf"
			onclick="javascript:changeFileType(this.value)">PDF</td>
	</TR>
	<TR>
		<TD style="WIDTH: 30%" noWrap class='tr3'><i18n:message
			key="export_Excel.pdfpagesize" /> :</TD>
		<td style="WIDTH: 70%" noWrap><input type="radio" name="pageSize"
			value="1" disabled>A3 <input type="radio" name="pageSize"
			value="2" checked disabled>A4 <input type="radio"
			name="pageSize" value="3" disabled>B3 <input type="radio"
			name="pageSize" value="4" disabled>B4</td>
	</TR>
	<TR>
		<TD align='center' colspan='2'><input id="btnSelect"
			type="button" value='<i18n:message key="button.submit"/>'
			onclick="javascript:exportExcel()" class=btn3_mouseout
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp; <input
			name="btnClose" type="button" onclick="javascript:window.close();"
			value='<i18n:message key="button.close"/>' class=btn3_mouseout
			onmouseover="this.className='btn3_mouseover'"
			onmouseout="this.className='btn3_mouseout'"
			onmousedown="this.className='btn3_mousedown'"
			onmouseup="this.className='btn3_mouseup'"></TD>
	</TR>
</TABLE>

</form>
</BODY>
</HTML>
