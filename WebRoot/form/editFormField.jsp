<!-- Task_ID	Author	Modify_Date	Description-->
<!-- IT0958		Young	 10/23/2007 Add a Comments Type of Field-->
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.basedata.vo.*,com.aiait.eflow.housekeeping.vo.*" %>
<%@page import="com.aiait.eflow.basedata.vo.BaseDataVO,com.aiait.eflow.util.StringUtil" %>
<% 
  String formSystemId = (String)request.getParameter("formSystemId");
  String sectionId = (String)request.getParameter("sectionId");
  String tableId = (String)request.getParameter("tableId");
  String rowId = (String)request.getParameter("rowId");
  String colId = (String)request.getParameter("colId");
  String type = (String)request.getParameter("type");
  String sectionType = (String)request.getParameter("sectionType");
  String formStatus = (String)request.getParameter("formStatus");
  FormSectionFieldVO field = (FormSectionFieldVO)request.getAttribute("field");
  BaseDataVO masterVo = (BaseDataVO)request.getAttribute("masterVo");
  Collection publishReports = (Collection)request.getAttribute("publishReports");
  int fieldType = 0;
  if(field!=null){
	  fieldType = field.getFieldType();
  }
  String initialFieldId = "";
  if(!"edit".equals(type)){
	  initialFieldId = (String)request.getAttribute("fieldId");
  }
%>
<html>
<head>
 <title><i18n:message key="form_design.field_edit_title"/></title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/loading.css" />
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
 
    var formStatus = window.opener.document.all['status'].value;
 
    function changeControlType(obj){
       
       if(obj.value!='4' && obj.value!='6'){
         document.all['fieldLabel'].readOnly = "";
       }else{
         //if(document.all['selectType'].value=="02"){
         //  document.all['fieldLabel'].readOnly = "true";
         //}
       }
       if(obj.value=="5"){
         document.all['decimalDigits'].disabled = "";
         document.all['decimalDigits'].value = "0";
         document.all['idismoney'].disabled = "";
       }else{
         document.all['decimalDigits'].disabled = "true";
         document.all['idismoney'].disabled = "true";
         document.all['idismoney'].selectedIndex=0;
       }
       
       if(obj.value=="1" || obj.value=="3" || obj.value=="2" || obj.value=="5"){
         document.all['selectTable'].style.display = "block";
         document.all['systemFieldDiv'].style.display = "none";
         document.all['checkDiv'].style.display = "none";
         
       }
       if(obj.value=="4" || obj.value=='6'){
          document.all['selectTable'].style.display = "block";
          document.all['checkDiv'].style.display = "block";
          document.all['systemFieldDiv'].style.display = "none";
       }
       if(obj.value=="7"){
         // document.all['systemFieldDiv'].style.display = "block";
         // document.all['checkDiv'].style.display = "none";
         //  document.all['selectTable'].style.display = "none";
         document.all['selectTable'].style.display = "none";
         document.all['checkDiv'].style.display = "none";
         document.all['systemFieldDiv'].style.display = "block";
       }
       //IT0958 begin
       if(obj.value=="15"){
       	 document.getElementById('reportTypeTr').style.display = "block";
       	 document.getElementById('reportIdTr').style.display = "block";
       	document.getElementById('reportSqlTr').style.display = "block";
       }else{
       	 document.getElementById('reportTypeTr').style.display = "none";
       	 document.getElementById('reportIdTr').style.display = "none";
       	document.getElementById('reportSqlTr').style.display = "none";
       }
       
       if(obj.value=="9"){
       	 document.all['systemFieldDiv'].style.display = "none";
       	 document.all['fieldcommenttr'].style.display = "block";
       	 document.all['selectTable'].style.display = "block";
       	 document.all['checkDiv'].style.display = "none";
       	 document.all['fieldLength'].value=500;
       	 document.all['fieldLength'].disabled = "true";
       	 document.getElementById('isRequired').disabled = "true";
       	 document.getElementById('isRequired').value = "false";
       }else{
       	 document.all['fieldcommenttr'].style.display = "none";
       	 document.getElementById('isRequired').disabled = "";
       	 document.all['fieldLength'].value=0;
       	 document.all['fieldLength'].disabled = "";
       }
       //IT0958 end
       
       if (obj.value=="12" || obj.value=="13") {
           document.all['selectTable'].style.display = "block";
           document.all['checkDiv'].style.display = "none";
           document.all['systemFieldDiv'].style.display = "none";
           document.all['fieldLength'].value = 1000;
           document.all['fieldLength'].disabled = "true";
       }
       
       document.all['controlType'].value = obj.value
    }
    
    function submitForm(){
      var sectionType = "<%=sectionType%>";
      var tableId = "<%=tableId%>";
      var rowId = "<%=rowId%>";
      var colId = "<%=colId%>";
      var sectionId = "<%=sectionId%>";
      var obj =  document.all['controlType'].value;
      var url = "";
      var fieldLabel;
      var fieldId;
      var length;
      var columnLabel;
      if(document.all['controlType'].value=='4' || document.all['controlType'].value=='6'){
         if(document.all['masterId'].value == ""){
           alert("Please select Options Data Source!");
           return;
         }
      }  
      document.all.submitBtn.disabled = "true";
      
      if(isRequired(document.forms['selectForm'].orderId,'<i18n:message key="form_design.order_id"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
      }
         
         if(isNumber(document.forms['selectForm'].orderId,'<i18n:message key="form_design.order_id"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
         }
      if(obj!="7"){ // 非系统定义字段，用户自定义字段
          url = "<%=request.getContextPath()%>/formManageAction.it?method=saveFormField&formSystemId=<%=formSystemId%>&sectionId=<%=sectionId%>";
         //data validate
         if(isRequired(document.forms['selectForm'].fieldId,'<i18n:message key="form_design.field_id"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
         }

         if(isRequired(document.forms['selectForm'].fieldLabel,'<i18n:message key="form_design.field_label"/>')==false){
          document.all.submitBtn.disabled = "";
          return;
         }

         if(isRequired(document.getElementById("fieldLength"),'<i18n:message key="form_design.field_length"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
         }
         
         if(isNumber(document.getElementById("fieldLength"),'<i18n:message key="form_design.field_length"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
         }

         /**
         //field_id
         var patrn=/^[A-Za-z]{1}+([A-Za-z]|[_]|[-]){0,19}$/; 
         if (!patrn.exec(document.forms['selectForm'].fieldId.value.Trim())){
            alert("Field Id is invalid! It can be only the composition of letters and '_'.");
            document.forms['selectForm'].fieldId.focus();
            return;
         } 
         **/
         
         var required = document.getElementById("isRequired").value;
         
         if(document.getElementById("fieldLength").value<1){
            alert("Field Length must be greater than 0!");
            document.getElementById("fieldLength").focus();
            document.all.submitBtn.disabled = "";
            return;
         }
         
         length = document.getElementById("fieldLength").value.Trim();
         if(length==""){
           length="0";
         }
         //var saveFieldLabel = formatStringAllChar(document.selectForm.fieldLabel.value.Trim());
         var saveFieldLabel = encodeURI(encodeURI(document.selectForm.fieldLabel.value.Trim()));
 
         url = url + "&fieldId=" + document.selectForm.fieldId.value+"&fieldLabel="+saveFieldLabel
                 +"&isRequired=" + document.selectForm.isRequired.value
                 +"&orderId="+ document.selectForm.orderId.value+"&formStatus="+formStatus+"&highLevel="+document.selectForm.highLevel.value
                 +"&isMoney="+ document.selectForm.isMoney.value+"&isSingleRow="+document.selectForm.isSingleRow.value
                 +"&defaultValue="+document.selectForm.defaultValue.value
                 +"&clickEvent="+document.selectForm.clickEvent.value
                 +"&dbclickEvent="+document.selectForm.dbclickEvent.value
                 +"&onfocusEvent="+document.selectForm.onfocusEvent.value
                 +"&lostfocusEvent="+document.selectForm.lostfocusEvent.value
                 +"&changeEvent="+document.selectForm.changeEvent.value
                 +"&isReadonly="+document.selectForm.isReadonly.value
                 +"&isDisabled="+document.selectForm.isDisabled.value;
         

         if(sectionType=="01"){
           fieldLabel = "";
           columnLabel = document.selectForm.fieldLabel.value;
           //columnLabel = document.all['label_'+document.selectForm.fieldLabel.value].value;
         }else{
           fieldLabel = document.selectForm.fieldLabel.value + ":";
         }
         fieldId = document.selectForm.fieldId.value;

         var  reg   =   /\s/g;   
         fieldId = fieldId.replace(reg, "");
        
         var fieldType,dataType;
         var cellInnerHtml = ""; 
         if(document.all['controlType'].value=='1'){
           cellInnerHtml = fieldLabel+"<input name='" + fieldId + "' size='"+length+"'>";
           fieldType=1;
           dataType=1;
         }else if(document.all['controlType'].value=='3'){
           cellInnerHtml = fieldLabel+"<input name='" + fieldId + "' style='width:100' onclick='setday(this)'>(MM/DD/YYYY)";
           fieldType=3;
           dataType=2;
         }else if(document.all['controlType'].value=='5'){
           if(document.getElementById('decimalDigits').value.Trim()==""){
             url = url + "&decimalDigits=0";
           }else{
             url = url + "&decimalDigits=" + document.getElementById('decimalDigits').value;
           }
           cellInnerHtml = fieldLabel+"<input name='" + fieldId + "' size='"+length+"'>";
           fieldType=5;
           dataType=3;
         }else if(document.all['controlType'].value=='4'){
           url = url + "&masterId=" + document.all['masterId'].value;
           cellInnerHtml = fieldLabel + ":<select name='tempSel'><option></option></select>";
           fieldType=4;
           dataType=1;
         }else if(document.all['controlType'].value=='6'){
           url = url + "&masterId=" + document.all['masterId'].value;
           cellInnerHtml = fieldLabel + ":<input type='checkbox' name='tempCheck'><input type='checkbox' name='tempCheck'>";
           fieldType=6;
           dataType=1;
         }else if(document.all['controlType'].value=='2'){
           cellInnerHtml = fieldLabel+"<textarea  name='" + fieldId + "' rows='2' cols='10'></textarea>";
           fieldType=2;
           dataType=1;
         }else if(document.all['controlType'].value=='9'){ //IT0958 Begin
        	fieldType = 9;
        	dataType = 1;
            //IT0958 end
         } else if (document.all['controlType'].value=='12') {
             fieldType = 12;
             dataType = 1;
         } else if (document.all['controlType'].value=='13') {
             fieldType = 13;
             dataType = 1;
         } else if (document.all['controlType'].value=='14') {
             fieldType = 14;
             dataType = 1;
         } else if (document.all['controlType'].value=='15') {
             fieldType = 15;
             dataType = 1;
         }
      

        url = url + "&fieldType="+fieldType+"&dataType="+dataType + "&saveType=<%=type%>";
        if(required=="true"){
          cellInnerHtml = cellInnerHtml + "(<font color='red'>*</font>)";
        }
      }else{ //systemfield
         var saveType = "<%=type%>";
         url = "<%=request.getContextPath()%>/formManageAction.it?method=saveSystemFormField&formSystemId=<%=formSystemId%>&sectionId=<%=sectionId%>";
         //var systemFieldId = getSelectValue("systemFieldId");
         var systemFieldLabel = (document.all['showSystemFieldLabel'].innerHTML).Trim();
         var systemFieldId = document.all['systemFieldId'].value;
         if(saveType=='new' && window.opener.document.all[systemFieldId]!=undefined){
           alert(system_field_only_one);
           document.all.submitBtn.disabled = "";
           return;
         }
         if(systemFieldLabel==""){
               alert(system_field_no_select);
               document.all.submitBtn.disabled = "";
               return;
         }
         if(sectionType=="01"){        
           fieldLabel = "";
           //columnLabel = document.all['label_'+systemFieldId].value;
           columnLabel = document.all['showSystemFieldLabel'].innerHTML;
         }else{
           //fieldLabel = document.all['label_'+systemFieldId].value + ":";
           fieldLabel = document.all['showSystemFieldLabel'].innerHTML + ":";
         }  
        url = url + "&systemFieldId=" + systemFieldId + "&saveType=<%=type%>&formStatus="+formStatus
                 +"&clickEvent="+document.selectForm.clickEvent.value
                 +"&dbclickEvent="+document.selectForm.dbclickEvent.value
                 +"&onfocusEvent="+document.selectForm.onfocusEvent.value
                 +"&lostfocusEvent="+document.selectForm.lostfocusEvent.value
                 +"&changeEvent="+document.selectForm.changeEvent.value
                 +"&orderId="+ document.selectForm.orderId.value+"&highLevel="+document.selectForm.highLevel.value
                 +"&defaultValue="+document.selectForm.defaultValue.value
                 +"&isSingleRow="+document.selectForm.isSingleRow.value
                 +"&isReadonly="+document.selectForm.isReadonly.value
                 +"&isDisabled="+document.selectForm.isDisabled.value
        		 +"&isRequired="+document.selectForm.isRequired.value;
        cellInnerHtml = fieldLabel + "<select><option value=''>&nbsp;&nbsp;</option><option value=''>&nbsp;&nbsp;</option></select>";
        fieldId = systemFieldId;
      }

      url = url +"&length="+length;
      
      //如果是 document.all['controlType'].value=='1'（单文本）document.all['controlType'].value=='2'（多文本） 
      //      document.all['controlType'].value=='5'（数字） document.all['controlType'].value=='3'（日期）2007-10-25
      //则可以设置控件的宽度
      if(document.all['controlType'].value=='1' || document.all['controlType'].value=='2'
           || document.all['controlType'].value=='3' || document.all['controlType'].value=='5'){
        url = url + "&controlsWidth=" + document.all['controlsWidth'].value; 
      }
      //如果是document.all['controlType'].value=='2'（多文本）则可以设置其高度
      if(document.all['controlType'].value=='2'){
        url = url + "&controlsHeight="+document.all['controlsHeight'].value;
      }
      //如果是非注释字段都可以设置注释
      if(document.all['controlType'].value!='9'){
        url = url + "&fieldComments=" + encodeURI(encodeURI(document.all['fieldComments'].value)); 
      }
      
      //IT0958 begin
      //如果是document.all['controlType'].value=='9'（Comments）
      if(document.all['controlType'].value=='9'){
      	url = url + "&commentContent=" + encodeURI(encodeURI(document.all['commentcont'].value));
      }
      //IT0958 end
      

      if(document.all['controlType'].value=='15'){
    	var temp = document.getElementsByName("reportType");  
    	var sourceSql = document.getElementById('sourceSql').value
    	var reportType = "";
    	for(var i=0;i<temp.length;i++)  {     
    		if(temp[i].checked)           
    		reportType = temp[i].value;  
    	}
    	if(reportType == ""){
            alert("Please select Report Type!");
            document.all.submitBtn.disabled = "";
            return;
         }
    	if(reportType=="O" && common.isEmpty(document.getElementById('reportSystemId').value)){
            alert("Please select Report Name!");
            document.all.submitBtn.disabled = "";
            return;
    	}
    	if(reportType=="O" && common.isEmpty(sourceSql)){
            alert("Please input the source sql!");
            document.all.submitBtn.disabled = "";
            return;
    	}
      	url = url + "&reportType=" + reportType
      			+ "&reportSystemId=" +document.getElementById('reportSystemId').value
      			+ "&sourceSql="+sourceSql;
      }
      
      //alert(url);      
      //return;
      
      if(!confirm(confirm_save)){
         document.all.submitBtn.disabled = "";
        return;
      }
      
      var  xmlhttp = createXMLHttpRequest();
      var result;
       if(xmlhttp){
           xmlhttp.open('GET',url,true);
           xmlhttp.onreadystatechange = function()
           {
             if(xmlhttp.readyState!=4){
             	window.document.getElementById("loading").style.display="block";
             }
             if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                 result = xmlhttp.responseText;
                 if(afterSubmitSucess(result,tableId,colId,columnLabel,cellInnerHtml)==false){
                   window.document.getElementById("loading").style.display="none";
                   return;
                 }else{
                   window.document.getElementById("loading").style.display="none";
                   alert(save_success);
                   window.opener.location.reload();
                   window.close();
                 }
             }
           }
           xmlhttp.setRequestHeader("If-Modified-Since","0");
           xmlhttp.send(null);
       } 
      
    }
    //end of submitForm()
    
    function afterSubmitSucess(result,tableId,colId,columnLabel,cellInnerHtml){
      if(result.Trim()=="success"){
         return true;
      }else{
         document.all.submitBtn.disabled = "";
         if(result.Trim()=="length"){
           alert(system_field_save_fail)
           document.getElementById('fieldLength').focus();
         }else{
           alert(result)
         }
         return false;
      }
      return true;
    }
    
    
    function addOption(){
      var objTable = document.getElementById('optionTable');
      var oTR = objTable.insertRow(objTable.rows.length);
      var oTD = oTR.insertCell();
      oTD.innerHTML = '<input type="checkbox" name="chkOption">';
      oTD = oTR.insertCell();
      oTD.innerHTML = 'option value:<input type="text" name="optionValue">&nbsp;&nbsp;option label:<input type="text" name="optionLabel">';
    }
    
    function removeOption(){
        delRow("optionTable","chkOption");
    }
    
  function delRow(tableId,checkBoxId) //删除指定行   
  {   
      var alltbDetailUsed = document.all(tableId).rows; 
      if(checkSelect(checkBoxId)<=0){
        alert("You have not select any records to delete!");
        return;
      }  
      if(confirm("Are you sure to delete the selected records")==false)   
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
  
  function selectMasterData(){
     openCenterWindow("<%=request.getContextPath()%>/baseDataManageAction.it?method=listMasterData&type=select",660,450);
  }
  
  function selectSystemField(){
     openCenterWindow("<%=request.getContextPath()%>/form/listSystemField.jsp",660,450);
  }
  
  function openEventWindow(){
    var retValue = showModalDialog('<%=request.getContextPath()%>/form/editFieldEvent.jsp?temp='+Math.random()*100000,window,'dialogWidth:650px; dialogHeight:450px;help:0;status:0;resizeable:1;')
  }

	function editFormScript() {
		var retValue = showModalDialog('<%=request.getContextPath()%>/formManageAction.it?method=editFormScript&formSystemId=<%=formSystemId%>', window, 'dialogWidth:900px; dialogHeight:600px; help:0; status:0; resizeable:1;')
	}

	function editGlobalScript() {
		var retValue = showModalDialog('<%=request.getContextPath()%>/formManageAction.it?method=editFormScript&formSystemId=-1', window, 'dialogWidth:900px; dialogHeight:600px; help:0; status:0; resizeable:1;')
	}
  
	function changeReportType(reprotType){
		if("C"==reprotType){
			document.getElementById('reportSystemId').disabled = "true";
			document.getElementById('sourceSql').disabled = "true";
		}else{
			document.getElementById('reportSystemId').disabled = "";
			document.getElementById('sourceSql').disabled = "";
		}
	}
	
  window.onload=function(){resize(500);}
 </script>
</head>
<body>
    <form name="selectForm" method='Post'>
     <input type='hidden' name='controlType' value='<%=(field!=null)?""+field.getFieldType():"1"%>'>
     <input type="hidden" name="oldFieldId" value='<%=((field!=null && field.getFieldId()!=null)?field.getFieldId():"")%>'>
     
   <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
      <td align='center'>
       <b><i18n:message key="form_design.field_type"/></b>
      </td>
    </tr>
    <tr>
      <td>
        <input <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> type=radio name='contronTypeName' value=1 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==1)?"checked":""%> <%=field==null?"checked":""%>><i18n:message key="form_design.single_text"/>
        &nbsp;&nbsp;<input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=2 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==2)?"checked":""%>><i18n:message key="form_design.multi_text"/>
        &nbsp;&nbsp;<input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=3 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==3)?"checked":""%>><i18n:message key="form_design.date"/>
        &nbsp;&nbsp;<input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=5 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==5)?"checked":""%>><i18n:message key="form_design.number"/>
        <br><br>
        <input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=6 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==6)?"checked":""%>><i18n:message key="form_design.checkbox"/>
        &nbsp;&nbsp;<input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=4 onclick="changeControlType(this)"  <%=(field!=null && field.getFieldType()==4)?"checked":""%>><i18n:message key="form_design.select"/>
        &nbsp;&nbsp;<input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=7 onclick="changeControlType(this)"  <%=(field!=null && field.getFieldType()==7)?"checked":""%>><i18n:message key="form_design.system_fields"/>
        &nbsp;&nbsp;<input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=9 onclick="changeControlType(this)"  <%=(field!=null && field.getFieldType()==9)?"checked":""%>><i18n:message key="form_design.comments"/>
        <br><br>
        <input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=12 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==12)?"checked":""%>><i18n:message key="form_design.refer_form"/>
        <%
        if (CompanyHelper.getInstance().getEFlowCompany().equals(
				CompanyHelper.EFlow_AIA_CHINA)) {
        %>
        <input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=13 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==13)?"checked":""%>><i18n:message key="form_design.refer_contract"/>
        <%
        }
        %>
        <input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=14 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==14)?"checked":""%>><i18n:message key="housekeeping_role.stafflist"/>
        
        <input type=radio name='contronTypeName' <%=(("0".equals(formStatus)&&"edit".equals(type))||(field!=null && field.getFieldType()==7)?"disabled":"")%> value=15 onclick="changeControlType(this)" <%=(field!=null && field.getFieldType()==15)?"checked":""%>><i18n:message key="form_design.report"/>
      </td>
     </tr>
     </table>
     <div id="loading" style="display:none;top:200px; left:150px;">
	   <div class="loading-indicator">
		 It is processing...
	   </div>
     </div>
   <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td align='center' colspan='2'>
          <b> <i18n:message key="form_design.field_detail"/></b>
          &nbsp;&nbsp;/&nbsp;&nbsp;<b><a href="javascript:editFormScript()"><i18n:message key="form_design.user_defined_function"/></a></b>
          &nbsp;&nbsp;/&nbsp;&nbsp;<b><a href="javascript:editGlobalScript()"><i18n:message key="form_design.user_defined_function_global"/></a></b>
       </td>
     </tr>
     </table>
    <div id="selectTable">
    <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.field_id"/> : 
       </td>
       <td style="WIDTH:70%">
       <input type='text' name='fieldId' readonly size='10' required='true' title='Field Id' length='10' value="<%=((field!=null && field.getFieldId()!=null)?field.getFieldId():"")%>">
         (<font color='red'>*</font>)
         (Remark:It can be only the composition of letters and '_')
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.field_label"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' name='fieldLabel' required='true' title='Field Label' size='40' length='40' value="<%=((field!=null && field.getFieldLabel()!=null)?StringUtil.htmlEncoder(field.getFieldLabel()):"")%>">
         (<font color='red'>*</font>)
       </td>
     </tr>
     <tr id='reportTypeTr' style="display :'none'">
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.report_type"/> : 
       </td>
       <td style="WIDTH:70%"> 
       	 <input type=radio name='reportType'  value='C' onclick="changeReportType('C')" <%=(field!=null && "C".equals(field.getReportType()))?"checked":""%>><i18n:message key="form_design.crystal_report"/>&nbsp;&nbsp;
       	 <input type=radio name='reportType'  value='O' onclick="changeReportType('O')" <%=(field!=null && "O".equals(field.getReportType()))?"checked":""%>><i18n:message key="form_design.other_report"/> 
        (<font color='red'>*</font>)
       </td>
     </tr>
     <tr id='reportIdTr' style="display :'none'">
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.report_name"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select id='reportSystemId' name='reportSystemId' disabled='true'>
         <option value="" ><i18n:message key="form_design.report_select"/></option>
         <%
         Iterator it = publishReports.iterator();
         while (it.hasNext()) {
             HashMap map = (HashMap) it.next();%>
             <option value="<%=map.get("REPORT_SYSTEM_ID") %>" <%=(field!=null && map.get("REPORT_SYSTEM_ID").equals(""+field.getReportSystemId()))?"selected":""%>><%=map.get("REPORT_NAME")%></option>
         <% }%>
         </select>
       </td>
     </tr>
     <tr id='reportSqlTr' style="display :'none'">
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.report_sql"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input id="sourceSql" name="sourceSql" type="text" value="<%=(field!=null&&field.getFieldDataSrSQL()!=null?""+field.getFieldDataSrSQL():"")%>" ></input>
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.field_length"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' required='true' name='fieldLength' title='Field Length' size='10' length='10' value="<%=(field!=null?""+field.getFieldLength():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">(<font color='red'>*</font>)
       </td>
     </tr>    
     <!-- IT0958 for comments Begin-->
     <tr id='fieldcommenttr' style="display:<%=(field.getFieldType()==9)?"block":"none"%>">
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.content"/> :
       </td>
       <td style="WIDTH:70%">
         <textarea rows=3 cols=40 name="commentcont"><%=((field!=null && field.getCommentContent()!=null)?""+field.getCommentContent().trim():"")%></textarea>
       </td>
     </tr>
     <!-- IT0958 for comments End-->
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.control_width"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' name='controlsWidth' title='Controls Width' <%=(field!=null && (field.getFieldType()==1 || field.getFieldType()==2 || field.getFieldType()==3 || field.getFieldType()==5))?"":"disabled"%> size='10' length='10' value="<%=(field!=null?""+field.getControlsWidth():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.control_height"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' name='controlsHeight' title='Controls Height' <%=(field!=null && field.getFieldType()==2)?"":"disabled"%> size='10' length='10' value="<%=(field!=null?""+field.getControlsHeight():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
       </td>
     </tr>
     
     <tr>
       <td style="WIDTH:30%" align='right'>
        <i18n:message key="form_design.scale"/>: 
        </td>
        <td style="WIDTH:70%">
        <input type='text' id='decimalDigits' <%=(field!=null && field.getFieldType()==5)?"":"disabled"%> size='10' length='10' value="<%=(field!=null?""+field.getDecimalDigits():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
       </td>
     </tr>
  
     <!-- IT0958 begin-->
     <tr>
       <td style="WIDTH:30%" align='right'>
        <i18n:message key="form_design.is_money"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select <%=((field.getDataType()==3)?"":"disabled")%> name="isMoney" id='idismoney'>
              <option value="-1" <%=(field!=null && "-1".equals(""+field.getIsMoney()))?"selected":""%>>No</option>
              <option value="1"  <%=(field!=null && "1".equals(""+field.getIsMoney()))?"selected":""%>>Yes</option>
         </select>         
       </td>
     </tr>
     <!-- IT0958 end-->
    
     <!-- IT1003 begin-->
     </table>
     </div>   
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.not_null"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select  id='isRequired'>
         	<option value="false" <%=(field!=null && !field.getIsRequired())?"selected":""%>>No</option>
         	<option value="true" <%=(field!=null && field.getIsRequired())?"selected":""%>>Yes</option>
         </select>

       </td>
     </tr> 
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.default_value"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' required='false' name='defaultValue' title='Default Value' size='10' length='10' value="<%=((field!=null && field.getDefaultValue()!=null)?StringUtil.htmlEncoder(field.getDefaultValue()):"")%>" style="ime-mode:disabled">
       </td>
     </tr> 
   <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.order_id"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' required='true' title='Order Id' name='orderId' size='10' length='10' value="<%=(field!=null?""+field.getOrderId():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
         (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.is_readonly"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="isReadonly">
              <option value="-1" <%=(field!=null && "-1".equals(""+field.getIsReadonly()))?"selected":""%>>No</option>
              <option value="1"  <%=(field!=null && "1".equals(""+field.getIsReadonly()))?"selected":""%>>Yes</option>
         </select>         
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.is_disabled"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="isDisabled">
              <option value="-1" <%=(field!=null && "-1".equals(""+field.getIsDisabled()))?"selected":""%>>No</option>
              <option value="1"  <%=(field!=null && "1".equals(""+field.getIsDisabled()))?"selected":""%>>Yes</option>
         </select>         
       </td>
     </tr>
 <!-- IT0958 begin-->
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.is_single_row"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="isSingleRow">
              <option value="-1" <%=(field!=null && "-1".equals(""+field.getIsSingleRow()))?"selected":""%>>No</option>
              <option value="1"  <%=(field!=null && "1".equals(""+field.getIsSingleRow()))?"selected":""%>>Yes</option>
         </select>         
       </td>
     </tr>
     <!-- IT0958 end-->
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.high_light"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="highLevel">
            <option value="-1" <%=(field!=null && "-1".equals(""+field.getHighLevel()))?"selected":""%>>No</option>
            <option value="1" <%=(field!=null && "1".equals(""+field.getHighLevel()))?"selected":""%>>Yes</option>
         </select>
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.field_comment"/> :
       </td>
       <td style="WIDTH:70%">
         <textarea rows=3 cols=40 name="fieldComments" title='Field Comments' ><%=((field!=null && field.getFieldComments()!=null)?StringUtil.htmlEncoder(field.getFieldComments().trim()):"")%></textarea>
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.click"/>: 
       </td>
       <td style="WIDTH:70%">  
        <input type='text' required='false' name='clickEvent' title='Click Event' size='20'  value="<%=((field!=null && field.getClickEvent()!=null)?field.getClickEvent():"")%>">
        (Event function 事件响应函数)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.dbclick"/>: 
       </td>
       <td style="WIDTH:70%">  
        <input type='text' required='false' name='dbclickEvent' title='Double Click Event' size='20'  value="<%=((field!=null && field.getDbclickEvent()!=null)?field.getDbclickEvent():"")%>">
        (Event function 事件响应函数)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.onfocus"/>: 
       </td>
       <td style="WIDTH:70%">  
        <input type='text' required='false' name='onfocusEvent' title='On Focus Event' size='20'  value="<%=((field!=null && field.getOnfocusEvent()!=null)?field.getOnfocusEvent():"")%>">
        (Event function 事件响应函数)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.lostfocus"/>: 
       </td>
       <td style="WIDTH:70%">  
        <input type='text' required='false' name='lostfocusEvent' title='Lost Focus Event' size='20'  value="<%=((field!=null && field.getLostfocusEvent()!=null)?field.getLostfocusEvent():"")%>">
        (Event function 事件响应函数)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="form_design.change"/>: 
       </td>
       <td style="WIDTH:70%">  
        <input type='text' required='false' name='changeEvent' title='Change Event' size='20'  value="<%=((field!=null && field.getChangeEvent()!=null)?field.getChangeEvent():"")%>">
       (Event function 事件响应函数)
       </td>
     </tr>
     <!-- IT1003 end-->
     </table>
       
     <div id="checkDiv" style='display:none'>
       <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
        <tr>
         <td colspan='2'>
           <i18n:message key="form_design.option_data_source"/>:
           <span id="showMasterName"><b><%=(masterVo==null?"":masterVo.getFieldName())%></b></span>
           <input type="button" name="selOptionsBtn" value='<i18n:message key="form_design.select_master_data"/>' onclick="selectMasterData()">
           <input type="hidden" name="masterId" value="<%=(masterVo==null?"":""+masterVo.getMasterId())%>">
         </td>
        </tr>         
       </table>
     </div>
     <div id="systemFieldDiv" style='display:none'>
       <input type="hidden" name="systemFieldId" value='<%=(field==null?"":field.getFieldId())%>'>
       <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
        <tr>
         <td colspan='2'>
           <i18n:message key="form_design.selected_system_field"/>:
           <b><span id="showSystemFieldLabel"><%=(field==null || field.getFieldLabel()==null?"":field.getFieldLabel())%></span></b>
           <input type="button" name="selOptionsBtn" value='<i18n:message key="form_design.select_sf_data"/>' onclick="selectSystemField()">
         </td>
        </tr>         
       </table>
     </div>
     
    <table width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >    
     <tr>
       <td align='center'>
         <input type='button' name='submitBtn' value='<i18n:message key="button.submit"/>' onclick='submitForm()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='reset' name='resetBtn' value='<i18n:message key="button.reset"/>' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
         <input type='button' name='close1' value='<i18n:message key="button.close"/>' onclick='window.close()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
     </tr>
   </table>
  </form>
</body>
</html>
 <script language="javascript">
    var fieldType = "<%=fieldType%>";
    if(fieldType=="6" || fieldType=="4"){
       //document.all['stringTable'].style.display = "none";
       document.all['selectTable'].style.display = "block";       
       document.all['checkDiv'].style.display = "block";
    }
    if(fieldType=="15" ){
       	 document.getElementById('reportTypeTr').style.display = "block";
       	 document.getElementById('reportIdTr').style.display = "block";
       	 document.getElementById('reportSqlTr').style.display = "block";
       	
       	changeReportType("<%=field.getReportType()%>");
     }
    if(fieldType=="7"){
      document.all['controlType'].value = fieldType;
      changeControlType(document.all['controlType']);
    }
    
</script>