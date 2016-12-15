<%--
    Task_ID    Author     Modify_Date    Description
1.  IT0973     Robin.Hou   12/21/2007     initial
--%>

<%@ include file="/common/head.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.util.*,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.wkf.vo.*,com.aiait.eflow.wkf.util.DataMapUtil,com.aiait.eflow.common.helper.StaffTeamHelper"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.FormTypeHelper,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.AuthorityHelper"%>
<%@ include file="/common/loading.jsp"%>
<html>
<head>
  <META HTTP-EQUIV="Cache-Control" CONTENT="PRIVATE">
  <META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 2020 08:21:57 GMT">
<title>Advance Query Form List</title>
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css">

<%
  String queryType = (String)request.getParameter("queryType");
  //queryType: 01 --- query his requested forms
  //           02 --- query his processed forms
  if(queryType==null || "".equals(queryType)){
	  queryType="01"; 
  }
%>
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/ajax.js"></script>
<SCRIPT language="javascript"
	src="<%=request.getContextPath()%>/js/sorttable.js"></SCRIPT>
<script language="javascript"
	src="<%=request.getContextPath()%>/js/resizeCol.js"></script>
<script language="javascript">
   var xmlhttp = createXMLHttpRequest();
   var fieldValueId;
   var count = 1;
   function getOptionList(formType){
    type = formType;
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormList&formType="+formType+"&moduleId=<%=ModuleOperateName.MODULE_COMMON_QUERY%>";
    xmlhttp.open("GET", url, true);
    var objId = "formSystemId";
    xmlhttp.onreadystatechange=handleStateChange;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);	
    
        var select;
        select = document.all("fieldValueDisplay");

          select.innerHTML = "<input type='text' name='fieldsValue' size='40'>";
 
        select = document.all("fieldId");

          select.value = "";
          clearSelect(select);
  }
  
  function getFieldOptionList(formSystemId){
    var rows = document.all.tbDetailPrepare.rows.length;
    
    if(rows>3){
      for(i = 3;i<document.all.tbDetailPrepare.rows.length;i++){
        document.all.tbDetailPrepare.deleteRow(i);
        i = i - 1;
      }
    }
    count = 1;
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormFieldList&formSystemId="+formSystemId;
    xmlhttp.open("GET", url, true);
    //var objId = "fieldList";
    xmlhttp.onreadystatechange=handleFormChange;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);	
    var select;
    select = document.all("fieldValueDisplay");

       select.innerHTML = "<input type='text' name='fieldsValue' size='40'>";

       var select_all_fields;
       select_all_fields = document.all("select_all_fields");
       select_all_fields.checked="true";    
  }
  
  function getFieldValueList(fieldId,fieldValueDisplay){
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormFieldValue&formSystemId="+document.all['formSystemId'].value
                +"&fieldId="+fieldId;
    xmlhttp.open("GET", url, true);
    fieldValueId = fieldValueDisplay;
    xmlhttp.onreadystatechange=handleFieldChange;
    xmlhttp.setRequestHeader("If-Modified-Since","0");
    xmlhttp.send(null);	    
  }

  function selectAllFields(checked) {

		var fields = document.forms[0].columnId;
		for (var i = 0; i < fields.length; i++) {
			if(fields[i].value=="01||request_no"){
				continue;
			}	
			fields[i].checked = checked;
		}
	}
 
  function handleFormChange(){
      if(xmlhttp.readyState == 4) {
        if(xmlhttp.status == 200) {
          var select;
          select = document.all("fieldId");
          select.value = "";
          clearSelect(select);
          var options = xmlhttp.responseXML.getElementsByTagName("option");
          if(options.length>0){
              select.appendChild(createEmptyElement(""));
              //clear the condition list table
              var alltbDetailUsed = document.all("conditionTable").rows; 
              if(alltbDetailUsed.length>2){
                for(var i=2;i<alltbDetailUsed.length;i++)   
                {   
                   document.all("conditionTable").deleteRow(i);   
                   i=i-1;
                }   
              }
              //clear the column select table
              var columnSelectTable = document.all("columnSelectTable"); 
              //create the select column
              var selectColumnStr = "";
              for (var i = 0, n = options.length; i < n; i++) {
                if(options[i].getAttribute("id")=="01||request_no"){
                   selectColumnStr = selectColumnStr + "<input type='checkbox' name='columnId' disabled checked value='"+ options[i].getAttribute("id") 
                                  +"'><input type='hidden' name='columnId' value='"+options[i].getAttribute("id")+"'>"+options[i].firstChild.nodeValue+"&nbsp;&nbsp;";
                }else{
                   selectColumnStr = selectColumnStr + "<input type='checkbox' name='columnId' checked value='"+ options[i].getAttribute("id") 
                                  +"'>"+options[i].firstChild.nodeValue+"&nbsp;&nbsp;";
                }
              }
              if(columnSelectTable.rows.length>2){
                columnSelectTable.rows[2].cells[0].innerHTML = selectColumnStr;
              }else{
                var newRow = columnSelectTable.insertRow(columnSelectTable.rows.length);
                var td = newRow.insertCell();
                td.innerHTML = selectColumnStr;
              }
          }
          for (var i = 0, n = options.length; i < n; i++) {
             select.appendChild(createElementWithValue(options[i],"id"));
          }
        }
     }
  }
  
  function handleFieldChange(){
      if(xmlhttp.readyState == 4) {
        if(xmlhttp.status == 200) {
          var select;
          select = document.getElementById(fieldValueId);
          select.innerHTML = xmlhttp.responseText;
        }
     }
  }
  
  function delRow1(tableId,checkBoxId) //?????   
  {   
      var alltbDetailUsed = document.all(tableId).rows; 
      
      if(checkSelect(checkBoxId)<=0){
        alert(have_no_select);
        return;
      }  
      if(confirm(confirm_delete_records)==false)   
        return false;   
      for(var i=0;i<alltbDetailUsed.length;i++)   
      {   
         if(alltbDetailUsed[i].all(checkBoxId)!=null && alltbDetailUsed[i].all(checkBoxId).checked==true)   
         {   
           document.all(tableId).deleteRow(i);   
           i=i-1;   
         }   
      }   
  } 
    
    function addRow(){
      var insertValueStr = "";  //?????????server????

      var fieldId = document.all['fieldId'].value;
       
      if(document.all['fieldId'].options.length<=0){
        alert(select_field);
        document.all['fieldId'].focus();
        return;
      }
       
      var fieldLabel = document.all['fieldId'].options[document.all['fieldId'].selectedIndex].text;
      if(fieldLabel.Trim()==""){
        alert(select_field);
        document.all['fieldId'].focus();
        return;
      }

      insertValueStr = insertValueStr + fieldId + "||";
      
      var compareTypeId = document.all['compareType'].value;
      var compareTypeLabel = document.all['compareType'].options[document.all['compareType'].selectedIndex].text;
      compareTypeLabel = compareTypeLabel.replace(/</g, "&lt;");
      insertValueStr = insertValueStr + compareTypeId + "||";
      
      
      var valueType = document.getElementById('fieldsValue').tagName;
         valueType = valueType.toUpperCase();

         var valueId =  "";
         if(valueType=="SELECT"){
            valueId = document.all['fieldsValue'].options[document.all['fieldsValue'].selectedIndex].text;
         }else{
            valueId = document.all['fieldsValue'].value;
         }
         if(valueId.Trim()==""){
           alert(fill_value);
           document.all['fieldsValue'].focus();
           return;
         }
         insertValueStr = insertValueStr + document.all['fieldsValue'].value + "||";
     
      
      
      var logicValue = document.all['logicFlag'].value;
      //alert(logicValue)
      var logicLabel = document.all['logicFlag'].options[document.all['logicFlag'].selectedIndex].text;
      insertValueStr = insertValueStr + logicValue + "||";
    
      insertValueStr = insertValueStr + fieldLabel + "||" + valueId + "||"+type;
 
      var objTable = document.getElementById('conditionTable');
      var oTR = objTable.insertRow(objTable.rows.length);
       var oTD0 = oTR.insertCell();
       insertValueStr = formatStringAllChar(insertValueStr);
       insertValueStr = insertValueStr.replace(/\'/g,"&#39;");
       oTD0.innerHTML = "<input type='checkbox' name='conditionId'>"+"<input type='hidden' name='conditionStr' value='"+insertValueStr+"'>";
       
       var oTD1 = oTR.insertCell();
       oTD1.innerHTML = fieldLabel;
       var oTD2 = oTR.insertCell();
       oTD2.innerHTML = compareTypeLabel;
       var oTD3 = oTR.insertCell();
       oTD3.innerHTML = valueId;
       var oTD4 = oTR.insertCell();
       oTD4.innerHTML = logicLabel;
    }
    
   function getFieldValueList(fieldId){
      
      var url = "<%=request.getContextPath()%>/formManageAction.it?method=getFormFieldValue&formSystemId="+document.all['formSystemId'].value
                +"&fieldId="+fieldId;
      xmlhttp.open("GET", url, true);
      xmlhttp.onreadystatechange=handleFieldChange;
      xmlhttp.setRequestHeader("If-Modified-Since","0");
      xmlhttp.send(null);	    
   }
   
   function handleFieldChange(){
      if(xmlhttp.readyState == 4) {
        if(xmlhttp.status == 200) {
          var select;
          select = document.getElementById("fieldValueDisplay");
          select.innerHTML = xmlhttp.responseText;
        }
     }
  }
  
  function changeValueType(obj){
    if(obj.value=="1"){
       document.all['fieldsValue'].disabled = "";
       document.all['functionName'].disabled = "true";
    }else{
      document.all['fieldsValue'].disabled = "true";
      document.all['functionName'].disabled = "";
    }
    document.all['valueTypeHidden'].value = obj.value;
  }
  
  function searchForm(isExport){
      var formName = document.all['formSystemId'].value;
      if(formName==""){
          
        if(isExport=="1"){
        	alert(select_export_form);
        }else{
            alert(select_search_form);
        }
        
        document.all['formSystemId'].focus();
        return;
      }

      var url = "<%=request.getContextPath()%>/wkfProcessAction.it?method=listRequesterAdvanceQuery&queryType=<%=queryType%>&isExport="+isExport;
      if(isExport=="1"){
          document.forms[0].target="_blank"
      }else{
          document.forms[0].target="iframeName";
      }
      document.forms[0].action = url;
      document.forms[0].submit();
   }
</script>
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
			  //StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
			  //AuthorityHelper authority = AuthorityHelper.getInstance();
			%>
<body>
<FORM nane=AVActionForm method="post">
<input type="hidden" name="count">
<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
		<td height="10"></td>
	</tr>
	<!--<tr>
		<td><strong><font color='#5980BB' family='Times New Roman'>Current
		Location:</font> <font color='#5980BB' family='Times New Roman'>
		&gt;My Workspace</font> <font color='#5980BB' family='Times New Roman'>
		&gt; <span class='submenu'>
		<%if("01".equals(queryType)){%>
		  Requested Form
		<%}else if("02".equals(queryType)){%>
		  Processed History
		<%}%>
		</span></font>
		<font color='#5980BB' family='Times New Roman'>
		&gt; <span class='submenu'>
		Advanced Query Form</span></font>
		</strong>
		</td>
	</tr>
--></table>
<TABLE WIDTH=100% bordercolor="#6595D6"
	style="border-collapse:collapse;" BORDER=1 CELLPADDING=3 CELLSPACING=0
	class="tr0" id="queryTable">
	<TR align="center">
		<TD height="25" colspan="8" class="tr1"><i18n:message key="menu.monitor.advancy_query"/></TD>
	</TR>
	<tbody id='tbDetailPrepare'>
	<TR>
		<TD width=15% height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message key="common.form_type"/></span></div>
		</TD>
		<TD width=30% height="20" colspan='7'><font size="2"> <select
			name="formType" onchange="getOptionList(this.value)">
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
		</select></TD>
	</TR>
	<tr>
		<TD width=18% height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message key="common.form"/></span></div>
		</TD>
		<TD width=30% height="20" colspan='7'><font size="2"> <select
			name="formSystemId" onchange="getFieldOptionList(this.value)">
			<option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
			<%if (formSelectList != null && formSelectList.size() > 0) {
				Iterator formIt = formSelectList.iterator();
				while (formIt.hasNext()) {
					FormManageVO form = (FormManageVO) formIt.next();
					out.print("<option value='" + form.getFormSystemId() + "'");
					if (formSystemId != null && !"".equals(formSystemId)
							&& formSystemId.equals("" + form.getFormSystemId())) {
						out.print(" selected ");
					}
					out.print(">" + form.getFormId() + " - "
							+ form.getFormName() + "</option>");
				}
			}%>
		</select></TD>
	</tr>
	<tr>
		<TD width=10% height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message key="comnon.field"/></span></div>
		</TD>
		<TD width=20% height="20"><font size="2"> 
		<select
			name="fieldId" onchange="getFieldValueList(this.value,'fieldValueDisplay')">
			<option value=""></option>
		</select></TD>
		<TD width=10% height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message key="comnon.compare_type"/></span></div>
		</TD>
		<TD width=10% height="20"><font size="2"> 
	      <select name="compareType">
			<option value="like">like</option>
			<option value="not like">not like</option>
			<option value="=" selected>=</option>
			<option value=">">></option>
			<option value=">=">>=</option>
			<option value="<">&lt;</option>
			<option value="<=">&lt;=</option>
			<option value="<>"><></option>
		   </select>
		</TD>
		<TD width=10% height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message key="comnon.value"/></span></div>
		</TD>
		<TD width=20% height="20"><font size="2"> 
		  <div id="fieldValueDisplay">
		  <input type="text" name="fieldsValue" size='40'>
		  </div>
		</TD>
		<TD width=10% height="20" class="tr3">
		<div align="right"><span class="style1"><i18n:message key="comnon.logic"/></span></div>
		</TD>
		<TD width=10% height="20">
		  <select name="logicFlag">
		    <option value="01">And</option>
		    <option value="02">Or</option>
		  </select>
		</TD>
	</tr>
	</tbody>
	<tr>
          <td style="WIDTH:20%" align='center' colspan='8'>
            <input type="button" value='<i18n:message key="button.add_codition"/>' name="addBtn" onclick="addRow()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
            <input type="button" value='<i18n:message key="button.remove_condition"/>' name="delBtn" onclick="delRow1('conditionTable','conditionId')" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
          </td>
   </tr>
  </table>
  <br>
 <table width="100%" id="conditionTable"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
        <tr>
          <td colspan='5' class='tr3' align='center'><b><i18n:message key="comnon.search_condition_list"/></b></td>
        </tr>
        <tr class='tr3'>
          <td align="center" style="WIDTH:4%"><input type="checkbox" name="allBtn" onclick="selectAll(this,'conditionId')"></td>
          <td align="center" style="WIDTH:40%"><b><i18n:message key="comnon.field"/></b></td>
          <td align="center" style="WIDTH:9%"><b><i18n:message key="comnon.compare_type"/></b></td>
          <td align="center" style="WIDTH:40%"><b><i18n:message key="comnon.value"/></b></td>
          <td align="center" style="WIDTH:7%"><b><i18n:message key="comnon.logic"/></b></td>
        </tr> 
</TABLE>
<br>
 <table width="100%" id="columnSelectTable"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
    <tr>
          <td class='tr3' align='center'><b><i18n:message key="comnon.display_column_select"/></b></td>
   </tr>
    <tr>     
          <td class='tr3' align='left'> 
          <input type='checkbox' name='select_all_fields' checked onclick="selectAllFields(this.checked)"> 
          <i18n:message key="button.selectAll"/></td>
   </tr>
 </table>
 <table width="100%">
        <tr>
          <td align='center' colspan='2'>
            <input type="button" value='<i18n:message key="button.search"/>' name="subBtn" onclick="searchForm()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
           <input type="button" value='<i18n:message key="button.export_excel"/>' name="expBtn" onclick="searchForm(1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;       
           <input type="button" value='<i18n:message key="button.back"/>' name="backBtn" onclick="javascript:history.go(-1)" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
          </td>
        </tr>
</table>
<br>
<table width="100%">
  <tr>
     <td width="100%">
	 <IFRAME frameBorder=0 id='iframeName' name="iframeName" scrolling="no" onload="Javascript:setCwinHeight(this)"  src="<%=request.getContextPath()%>/workflow/requesterAdvanceQueryList.jsp" width="100%"></IFRAME>
	</td>
  </tr>
</table>
</FORM>
</body>
</html>
