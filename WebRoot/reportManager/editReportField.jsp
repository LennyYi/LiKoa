<!-- Task_ID	Author	Modify_Date	Description-->
<!-- IT0958		Young	 10/23/2007 Add a Comments Type of Field-->
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page import="java.util.*,com.aiait.eflow.reportmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.basedata.vo.*,com.aiait.eflow.housekeeping.vo.*" %>
<%@page import="com.aiait.eflow.basedata.vo.BaseDataVO,com.aiait.eflow.util.StringUtil" %>
<% 
  String reportSystemId = (String)request.getParameter("reportSystemId");
  String sectionId = (String)request.getParameter("sectionId");
  String tableId = (String)request.getParameter("tableId");
  String rowId = (String)request.getParameter("rowId");
  String colId = (String)request.getParameter("colId");
  String type = (String)request.getParameter("type");
  String sectionType = (String)request.getParameter("sectionType");
  String reportStatus = (String)request.getParameter("reportStatus");
  ReportSectionFieldVO field = (ReportSectionFieldVO)request.getAttribute("field");
  BaseDataVO masterVo = (BaseDataVO)request.getAttribute("masterVo");
  int fieldType = 0;
  //String cssStr= "";
  String[] textCss=null;
  String[] valueCss=null;
  if(field!=null){
	  fieldType = field.getFieldType();
	  if(field.getCssStr()!=null || "".equals(field.getCssStr())){
		  String cssStr[] = field.getCssStr().split(";");
		  textCss = cssStr[0].split(",");
		  valueCss = cssStr[1].split(",");
	  }
	  
  }
  String initialFieldId = "";
  if(!"edit".equals(type)){
	  initialFieldId = (String)request.getAttribute("fieldId");
  }
%>
<html>
<head>
 <title><i18n:message key="report_design.field_edit_title"/></title>
 <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/loading.css" />
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
 <script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
 <script language="javascript">
 
    var reportStatus = window.opener.document.all['status'].value;
 
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
    
    function submitReport(){
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
      
      document.all.submitBtn.disabled = "true";
      
      if(isRequired(document.forms['selectReport'].orderId,'<i18n:message key="report_design.order_id"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
      }
         
         if(isNumber(document.forms['selectReport'].orderId,'<i18n:message key="report_design.order_id"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
         }
      if(obj!="7"){ // 非系统定义字段，用户自定义字段
          url = "<%=request.getContextPath()%>/reportManageAction.it?method=saveReportField&reportSystemId=<%=reportSystemId%>&sectionId=<%=sectionId%>";
         //data validate
         if(isRequired(document.forms['selectReport'].fieldId,'<i18n:message key="report_design.field_id"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
         }


         if(isRequired(document.getElementById("fieldLength"),'<i18n:message key="report_design.field_length"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
         }
         
         if(isNumber(document.getElementById("fieldLength"),'<i18n:message key="report_design.field_length"/>')==false){
           document.all.submitBtn.disabled = "";
           return;
         }

        
         
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
         //var saveFieldLabel = formatStringAllChar(document.selectReport.fieldLabel.value.Trim());
         var saveFieldLabel = encodeURI(encodeURI(document.selectReport.fieldLabel.value.Trim()));
         var cssStr="";
         if(document.selectReport.text_x.value==""){
        	 cssStr = cssStr+"0,";
         }else{
        	 cssStr = cssStr+document.selectReport.text_x.value+",";
         }
         if(document.selectReport.text_y.value==""){
        	 cssStr = cssStr+"0,";
         }else{
        	 cssStr = cssStr+document.selectReport.text_y.value+",";
         }
         if(document.selectReport.text_size.value==""){
        	 cssStr = cssStr+"12,";
         }else{
        	 cssStr = cssStr+document.selectReport.text_size.value+",";
         }
         
         cssStr = cssStr+document.selectReport.text_style.value+","+document.selectReport.text_weight.value+","+document.selectReport.text_family.value+";";

         if(document.selectReport.value_x.value==""){
        	 cssStr = cssStr+"0,";
         }else{
        	 cssStr = cssStr+document.selectReport.value_x.value+",";
         }
         if(document.selectReport.value_y.value==""){
        	 cssStr = cssStr+"0,";
         }else{
        	 cssStr = cssStr+document.selectReport.value_y.value+",";
         }
         if(document.selectReport.value_size.value==""){
        	 cssStr = cssStr+"12,";
         }else{
        	 cssStr = cssStr+document.selectReport.value_size.value+",";
         }
         cssStr = cssStr+document.selectReport.value_style.value+","+document.selectReport.value_weight.value+","+document.selectReport.text_family.value;
         cssStr = encodeURI(encodeURI(cssStr));
         url = url + "&fieldId=" + document.selectReport.fieldId.value+"&fieldLabel="+saveFieldLabel
                 +"&isRequired=" + document.selectReport.isRequired.value
                 +"&orderId="+ document.selectReport.orderId.value+"&reportStatus="+reportStatus+"&highLevel="+document.selectReport.highLevel.value
                 +"&isSingleRow="+document.selectReport.isSingleRow.value
                 +"&defaultValue="+encodeURI(encodeURI(document.selectReport.defaultValue.value))
                 +"&isReadonly="+document.selectReport.isReadonly.value
                 +"&isSingleLabel="+document.selectReport.isSingleLabel.value
                 +"&cssStr="+cssStr
                 +"&aligned="+document.selectReport.aligned.value
                 +"&border="+document.selectReport.border.value;

         if(sectionType=="01"){
           fieldLabel = "";
           columnLabel = document.selectReport.fieldLabel.value;
           //columnLabel = document.all['label_'+document.selectReport.fieldLabel.value].value;
         }else{
           fieldLabel = document.selectReport.fieldLabel.value + ":";
         }
         fieldId = document.selectReport.fieldId.value;

         var  reg   =   /\s/g;   
         fieldId = fieldId.replace(reg, "");
        
         var fieldType,dataType;
         var cellInnerHtml = ""; 
         if(document.all['controlType'].value=='1'){
           cellInnerHtml = fieldLabel+"<input type='hidden' name='" + fieldId + "'>";
           fieldType=1;
           dataType=1;
         }else{
        	 fieldType=2;
             dataType=2; 
         }
      

        url = url + "&fieldType="+fieldType+"&dataType="+dataType + "&saveType=<%=type%>";
        if(required=="true"){
          cellInnerHtml = cellInnerHtml + "(<font color='red'>*</font>)";
        }
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
      //if(document.all['controlType'].value=='2'){
        url = url + "&controlsHeight="+document.all['controlsHeight'].value;
      //}
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
    //end of submitReport()
    
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
     openCenterWindow("<%=request.getContextPath()%>/report/listSystemField.jsp",660,450);
  }
  
  function openEventWindow(){
    var retValue = showModalDialog('<%=request.getContextPath()%>/report/editFieldEvent.jsp?temp='+Math.random()*100000,window,'dialogWidth:650px; dialogHeight:450px;help:0;status:0;resizeable:1;')
  }

	function editReportScript() {
		var retValue = showModalDialog('<%=request.getContextPath()%>/reportManageAction.it?method=editReportScript&reportSystemId=<%=reportSystemId%>', window, 'dialogWidth:900px; dialogHeight:600px; help:0; status:0; resizeable:1;')
	}

	function editGlobalScript() {
		var retValue = showModalDialog('<%=request.getContextPath()%>/reportManageAction.it?method=editReportScript&reportSystemId=-1', window, 'dialogWidth:900px; dialogHeight:600px; help:0; status:0; resizeable:1;')
	}
  
  window.onload=function(){resize(500);}
 </script>
</head>
<body>
    <form name="selectReport" method='Post'>
     <input type="hidden" name="oldFieldId" value='<%=((field!=null && field.getFieldId()!=null)?field.getFieldId():"")%>'>
     <input type="hidden" name="isReadonly" value='1'>
     <input type="hidden" name="isRequired" value='false'>
  
     <div id="loading" style="display:none;top:200px; left:150px;">
	   <div class="loading-indicator">
		 It is processing...
	   </div>
     </div>
   <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td align='center' colspan='2'>
          <b> <i18n:message key="report_design.field_detail"/></b>
          &nbsp;&nbsp;/&nbsp;&nbsp;<b><a href="javascript:editReportScript()"><i18n:message key="report_design.user_defined_function"/></a></b>
          &nbsp;&nbsp;/&nbsp;&nbsp;<b><a href="javascript:editGlobalScript()"><i18n:message key="report_design.user_defined_function_global"/></a></b>
       </td>
     </tr>
     </table>
    <div id="selectTable">
    <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.field_type"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="controlType">
              <option value="1" <%=(field!=null && "1".equals(""+field.getFieldType()))?"selected":""%>>Text</option>
              <option value="2" <%=(field!=null && "2".equals(""+field.getFieldType()))?"selected":""%>>Html</option>
         </select>         
       </td>
     </tr>
      
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.field_id"/> : 
       </td>
       <td style="WIDTH:70%">
       <input type='text' name='fieldId' readonly size='10' required='true' title='Field Id' length='10' value="<%=((field!=null && field.getFieldId()!=null)?field.getFieldId():"")%>">
         (<font color='red'>*</font>)
         (Remark:It can be only the composition of letters and '_')
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.field_label"/> : 
       </td>
       <td style="WIDTH:70%">  
         
         <input type='text' name='fieldLabel' required='false' title='Field Label' size='40' length='40' value="<%=((field!=null && field.getFieldLabel()!=null)?StringUtil.htmlEncoder(field.getFieldLabel()):"")%>">
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.field_length"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' required='true' name='fieldLength' title='Field Length' size='10' length='10' value="<%=(field!=null?""+field.getFieldLength():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">(<font color='red'>*</font>)
       </td>
     </tr>    
     <!-- IT0958 for comments Begin-->
     <tr id='fieldcommenttr' style="display:<%=(field.getFieldType()==9)?"block":"none"%>">
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.content"/> :
       </td>
       <td style="WIDTH:70%">
         <textarea rows=3 cols=40 name="commentcont"><%=((field!=null && field.getCommentContent()!=null)?""+field.getCommentContent().trim():"")%></textarea>
       </td>
     </tr>
     <!-- IT0958 for comments End-->
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.control_width"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' name='controlsWidth' title='Controls Width' <%=(field!=null && (field.getFieldType()==1 || field.getFieldType()==2 || field.getFieldType()==3 || field.getFieldType()==5))?"":"disabled"%> size='10' length='10' value="<%=(field!=null?""+field.getControlsWidth():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.control_height"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' name='controlsHeight' title='Controls Height' <%=(field!=null && field.getFieldType()==2)?"":"disabled"%> size='10' length='10' value="<%=(field!=null?""+field.getControlsHeight():"")%>"  onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
       </td>
     </tr>
     
     <tr>
       <td style="WIDTH:30%" align='right'>
        <i18n:message key="report_design.scale"/>: 
        </td>
        <td style="WIDTH:70%">
        <input type='text' id='decimalDigits' <%=(field!=null && field.getFieldType()==5)?"":"disabled"%> size='10' length='10' value="<%=(field!=null?""+field.getDecimalDigits():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
       </td>
     </tr>
    
     <!-- IT1003 begin-->
     </table>
     </div>   
  <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.default_value"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' required='false' name='defaultValue' title='Default Value' size='10' length='10' value="<%=((field!=null && field.getDefaultValue()!=null)?StringUtil.htmlEncoder(field.getDefaultValue()):"")%>" style="ime-mode:disabled">
       </td>
     </tr> 
   <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.order_id"/> : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' required='true' title='Order Id' name='orderId' size='10' length='10' value="<%=(field!=null?""+field.getOrderId():"")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
         (<font color='red'>*</font>)
       </td>
     </tr>
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.is_single"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="isSingleLabel">
              <option value="-1" <%=(field!=null && "-1".equals(""+field.getIsSingleLabel()))?"selected":""%>>No</option>
              <option value="0" <%=(field!=null && "0".equals(""+field.getIsSingleLabel()))?"selected":""%>>Single Text</option>
              <option value="1"  <%=(field!=null && "1".equals(""+field.getIsSingleLabel()))?"selected":""%>>Single Value</option>
         </select>         
       </td>
     </tr>
   <!-- IT0958 begin-->
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.is_single_row"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="isSingleRow">
              <option value="-1" <%=(field!=null && "-1".equals(""+field.getIsSingleRow()))?"selected":""%>>No</option>
              <option value="1"  <%=(field!=null && "1".equals(""+field.getIsSingleRow()))?"selected":""%>>Yes</option>
         </select>         
       </td>
     </tr>
     
     <tr>
       <td style="WIDTH:30%" align='right'>
         Class Name : 
       </td>
       <td style="WIDTH:70%">  
         <input type='text' name='className' size='10' length='10' value="">
       </td>
     </tr>
     
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.format_text"/> : 
       </td>
       <td style="WIDTH:70%">       
         X:<input type='text'  name='text_x' title='Field Length' size='3' length='10' value="<%=(textCss!=null?textCss[0]:"0")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
         Y:<input type='text'  name='text_y' title='Field Length' size='3' length='10' value="<%=(textCss!=null?textCss[1]:"0")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
         Size:
         <input type='text'  name='text_size' title='Field Length' size='3' length='10' value="<%=(textCss!=null?textCss[2]:"12")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">              
         Style:
         <select name ='text_style'>
         <option value="normal" <%=(textCss!=null && "normal".equals(textCss[3]))?"selected":""%> >normal</option><option value="italic" <%=(textCss!=null && "italic".equals(textCss[3]))?"selected":""%>>italic</option>
         </select>
         weight:
         <select name = 'text_weight'>
         <option value="400" <%=(textCss!=null && "400".equals(textCss[4]))?"selected":""%>>400</option>
         <option value="100" <%=(textCss!=null && "100".equals(textCss[4]))?"selected":""%>>100</option>
         <option value="200" <%=(textCss!=null && "200".equals(textCss[4]))?"selected":""%>>200</option>
         <option value="300" <%=(textCss!=null && "300".equals(textCss[4]))?"selected":""%>>300</option>
         
         <option value="500" <%=(textCss!=null && "500".equals(textCss[4]))?"selected":""%>>500</option>
         <option value="600" <%=(textCss!=null && "600".equals(textCss[4]))?"selected":""%>>600</option>
         <option value="700" <%=(textCss!=null && "700".equals(textCss[4]))?"selected":""%>>700</option>
         <option value="800" <%=(textCss!=null && "800".equals(textCss[4]))?"selected":""%>>800</option>
         <option value="900" <%=(textCss!=null && "900".equals(textCss[4]))?"selected":""%>>900</option>
         </select>
         Family:
         <select name='text_family'>
           <option value="Arial, Verdana, '黑体'">Arial, Verdana, "黑体"</option>
         </select>
       </td>
     </tr>
     
     
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.format_value"/> : 
       </td>
       <td style="WIDTH:70%">       
          X:<input type='text'  name='value_x' title='Field Length' size='3' length='10' value="<%=(valueCss!=null?valueCss[0]:"0")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
          Y:<input type='text'  name='value_y' title='Field Length' size='3' length='10' value="<%=(valueCss!=null?valueCss[1]:"0")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
         Size:
         <input type='text'  name='value_size' title='Field Length' size='3' length='10' value="<%=(valueCss!=null?valueCss[2]:"12")%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">              
         Style:
         <select name='value_style'>
         <option value="normal" <%=(valueCss!=null && "normal".equals(valueCss[3]))?"selected":""%>>normal</option>
         <option value="italic" <%=(valueCss!=null && "italic".equals(valueCss[3]))?"selected":""%>>italic</option>
         </select>
         weight:
         <select name='value_weight'>
         <option value="400" <%=(valueCss!=null && "400".equals(valueCss[4]))?"selected":""%>>400</option>
         <option value="100" <%=(valueCss!=null && "100".equals(valueCss[4]))?"selected":""%>>100</option>
         <option value="200" <%=(valueCss!=null && "200".equals(valueCss[4]))?"selected":""%>>200</option>
         <option value="300" <%=(valueCss!=null && "300".equals(valueCss[4]))?"selected":""%>>300</option>
         
         <option value="500" <%=(valueCss!=null && "500".equals(valueCss[4]))?"selected":""%>>500</option>
         <option value="600" <%=(valueCss!=null && "600".equals(valueCss[4]))?"selected":""%>>600</option>
         <option value="700" <%=(valueCss!=null && "700".equals(valueCss[4]))?"selected":""%>>700</option>
         <option value="800" <%=(valueCss!=null && "800".equals(valueCss[4]))?"selected":""%>>800</option>
         <option value="900" <%=(valueCss!=null && "900".equals(valueCss[4]))?"selected":""%>>900</option>
         </select>
         Family:
         <select name='value_family'>
           <option value="Arial, Verdana, '黑体'">Arial, Verdana, "黑体"</option>
         </select>
       </td>
     </tr>
     
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.alignedPosition"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="aligned">
              <option value="1" <%=(field!=null && "1".equals(""+field.getAligned()))?"selected":""%>>Left</option>
              <option value="2" <%=(field!=null && "2".equals(""+field.getAligned()))?"selected":""%>>Center</option>
              <option value="3" <%=(field!=null && "3".equals(""+field.getAligned()))?"selected":""%>>Right</option>
         </select>         
       </td>
     </tr>
     
   
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.border"/> : 
       </td>
       <td style="WIDTH:70%">  
         <select name="border">
              <option value="0" <%=(field!=null && "0".equals(""+field.getBorder()))?"selected":""%>>None</option>
              <option value="1" <%=(field!=null && "1".equals(""+field.getBorder()))?"selected":""%>>Top</option>
              <option value="2" <%=(field!=null && "2".equals(""+field.getBorder()))?"selected":""%>>Bottom</option>
              <option value="3" <%=(field!=null && "3".equals(""+field.getBorder()))?"selected":""%>>Left</option>
              <option value="4" <%=(field!=null && "4".equals(""+field.getBorder()))?"selected":""%>>Right</option>
              <option value="5" <%=(field!=null && "5".equals(""+field.getBorder()))?"selected":""%>>Top/Bottom</option>
              <option value="6" <%=(field!=null && "6".equals(""+field.getBorder()))?"selected":""%>>Left/Right</option>
              <option value="7" <%=(field!=null && "7".equals(""+field.getBorder()))?"selected":""%>>Top/Left</option>
              <option value="8" <%=(field!=null && "8".equals(""+field.getBorder()))?"selected":""%>>Right/Bottom</option>
              <option value="9" <%=(field!=null && "9".equals(""+field.getBorder()))?"selected":""%>>All</option>
         </select>         
       </td>
     </tr>
     
     <!-- IT0958 end-->
     <tr>
       <td style="WIDTH:30%" align='right'>
         <i18n:message key="report_design.high_light"/> : 
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
         <i18n:message key="report_design.field_comment"/> :
       </td>
       <td style="WIDTH:70%">
         <textarea rows=3 cols=40 name="fieldComments" title='Field Comments' ><%=((field!=null && field.getFieldComments()!=null)?StringUtil.htmlEncoder(field.getFieldComments().trim()):"")%></textarea>
       </td>
     </tr>
    
     </table>
       
     <div id="checkDiv" style='display:none'>
       <table width="100%"  border="1" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
        <tr>
         <td colspan='2'>
           <i18n:message key="report_design.option_data_source"/>:
           <span id="showMasterName"><b><%=(masterVo==null?"":masterVo.getFieldName())%></b></span>
           <input type="button" name="selOptionsBtn" value='<i18n:message key="report_design.select_master_data"/>' onclick="selectMasterData()">
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
           <i18n:message key="report_design.selected_system_field"/>:
           <b><span id="showSystemFieldLabel"><%=(field==null || field.getFieldLabel()==null?"":field.getFieldLabel())%></span></b>
           <input type="button" name="selOptionsBtn" value='<i18n:message key="report_design.select_sf_data"/>' onclick="selectSystemField()">
         </td>
        </tr>         
       </table>
     </div>
     
    <table width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >    
     <tr>
       <td align='center'>
         <input type='button' name='submitBtn' value='<i18n:message key="button.submit"/>' onclick='submitReport()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
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
    if(fieldType=="7"){
      document.all['controlType'].value = fieldType;
      changeControlType(document.all['controlType']);
    }
    
</script>