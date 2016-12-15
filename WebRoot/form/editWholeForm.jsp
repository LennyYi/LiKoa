<!-- 
  表单设计的核心jsp部分
 -->
<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@page import="java.util.*,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.framework.db.*,com.aiait.eflow.housekeeping.vo.StaffVO" %>
  <% 
    FormManageVO form = (FormManageVO)request.getAttribute("wholeForm");
    String formStatus = form.getStatus();
    boolean isPublished = false;
    if("0".equals(formStatus)){
    	isPublished = true;
    }
    
    int formColumns=3;
    String tempColumnStr=ParamConfigHelper.getInstance().getParamValue(CommonName.PAGE_ROW);
    if(tempColumnStr.matches("[0-9]") && !tempColumnStr.equals("")){
    	if(Integer.parseInt(tempColumnStr)>0){
    		formColumns=Integer.parseInt(tempColumnStr);
    	}
    }
   
    
  %>
<html>
<head>
<title>Edit Form</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">
   function createFormSection(){
      var url = "<%=request.getContextPath()%>/formManageAction.it?method=enterEditFormSection&type=new&tableId=formSpace&formSystemId="
                 +document.forms[0].formSystemId.value;
      openCenterWindow(url,460,200);
   }
  
   function addCol(tableId,sectionId){
    var oTable = document.getElementById(tableId);
    var rowId = 1,colId = -1;
    
    for(var j=0;j<oTable.rows[1].cells.length;j++) 
    {
        if(oTable.rows[1].cells[j].bgColor=="#e7eaf5"){
          rowId = 1;
          colId = j;
          break;
        }
    }
    var oTD;
    
    for(i=0;i<oTable.rows.length;i++){
      var oTR = oTable.rows[i];
      if(colId!=-1){
        oTD = oTR.insertCell(colId+1);
      }else{
        oTD = oTR.insertCell(oTR.cells.length);
      }
      oTD.onclick = new Function("SelectCol(this)");
      oTD.ondblclick =new Function('editField(this,"sectionTable'+sectionId+'","'+sectionId+'","-999","01")');     
    }
  }
  
  function addColField(tableId,sectionId){
    var objTable = document.getElementById(tableId);
    var rowId,colId
    for(var i=0;i<objTable.rows.length;i++)
      for(var j=0;j<objTable.rows[i].cells.length;j++) 
      {
        if(objTable.rows[i].cells[j].bgColor=="#e7eaf5"){
          rowId = i;
          colId = j;
          break;
        }
      }
    
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=enterEditFormField&type=new&rowId="+rowId+"&colId="
               +colId+"&formSystemId="+document.forms[0].formSystemId.value+"&sectionId="+sectionId+"&tableId="+tableId+"&sectionType=01"; 
    openCenterWindow(url,500,550);
  }
  
   function addRow(tableId,sectionId){
     var oTable = document.getElementById(tableId);
     var oTR = oTable.insertRow(oTable.rows.length);
     for(i=0;i<2;i++){
       var oTD = oTR.insertCell(i);
       //oTD.innerHTML  = "<input type='text' name='name'>";
       oTD.innerHTML  = "&nbsp;&nbsp;";
       //oTD.ondblclick =new Function("test(this)");
       oTD.onclick = new Function("SelectRow(this)");
       oTD.ondblclick =new Function('editField(this,"sectionTable'+sectionId+'","'+sectionId+'","-999","02")');
     }
  }
  function deleteRow(tableId){
   if(confirm(confirm_delete_row)){
       var objTable = document.getElementById(tableId);
       var rowId = -1,colId
       for(var i=0;i<objTable.rows.length;i++){
         for(var j=0;j<objTable.rows[i].cells.length;j++) 
         {
           if(objTable.rows[i].cells[j].bgColor=="#e7eaf5"){
             rowId = i;
             colId = j;
             break;
            }
         }
        }
       if(rowId!=-1){
        objTable.deleteRow(rowId);
       }
      }
  }
  
  function addField(tableId,sectionId,sectionType){
    var objTable = document.getElementById(tableId);
    var rowId=-1,colId=-1
    for(var i=0;i<objTable.rows.length;i++)
      for(var j=0;j<objTable.rows[i].cells.length;j++) 
      {
        if(objTable.rows[i].cells[j].bgColor=="#e7eaf5"){
          rowId = i;
          colId = j;
          break;
        }
      }
     if(rowId==-1 || colId==-1){
       alert(have_no_select_cell)
       return;
     }
     var url = "<%=request.getContextPath()%>/formManageAction.it?method=enterEditFormField&type=new&rowId="+rowId+"&colId="
               +colId+"&formSystemId="+document.forms[0].formSystemId.value+"&sectionId="+sectionId+"&tableId="+tableId+"&sectionType="+sectionType
               +"&formStatus="+document.forms[0].status.value;

     openCenterWindow(url,500,550);
  }
  
 function deleteCol(tableId){
   if(confirm(confirm_delete_column)){
       var objTable = document.getElementById(tableId);
       var rowId = 1,colId = -1;
       for(var j=0;j<objTable.rows[1].cells.length;j++) 
       {
           if(objTable.rows[1].cells[j].bgColor=="#e7eaf5"){
             rowId = 1;
             colId = j;
             break;
            }
       }
       if(colId!=-1){
        for(var j=0;j<objTable.rows.length;j++){
          objTable.rows[j].deleteCell(colId);
        }
       }else{
        alert(have_no_select_col)
        return;
       }
    }
 }
 
 function deleteField(tableId,sectionId,sectionType){
    var objTable = document.getElementById(tableId);
    var rowId=-1,colId=-1
    for(var i=0;i<objTable.rows.length;i++){
      for(var j=0;j<objTable.rows[i].cells.length;j++) 
      {
        if(objTable.rows[i].cells[j].bgColor=="#e7eaf5"){
          rowId = i;
          colId = j;
          break;
        }
      }
     }
     if(rowId==-1 || colId==-1){
       alert(have_no_select_field)
       return;
     } 
   if(objTable.rows[rowId].cells[colId].innerHTML=="" || objTable.rows[rowId].cells[colId].innerHTML=="&nbsp;&nbsp;"){
      alert(no_field_delete);
      return;
   }
   if(objTable.rows[rowId].cells[colId].getAttribute("id")==""){
     alert(field_can_not_get);
     return;
   }
   if(confirm(confirm_delete_field)==false){
     return;
   }
   
   var status = "<%=formStatus%>";
   if(status=="0"){
     if(confirm(confirm_leave_form)==false){
       return;
     }
   }
   
   var url = "<%=request.getContextPath()%>/formManageAction.it?method=deleteFormField&formSystemId="+document.forms[0].formSystemId.value
             +"&sectionId="+sectionId+"&fieldId="+objTable.rows[rowId].cells[colId].getAttribute("id")
             +"&formStatus="+status;

   var  xmlhttp = createXMLHttpRequest();
   var result = "";
   if(xmlhttp){
      xmlhttp.open('POST',url,false);
      xmlhttp.onreadystatechange = function()
      {
             if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
                 result = xmlhttp.responseText;
             }
       }
       xmlhttp.setRequestHeader("If-Modified-Since","0");
       xmlhttp.send(null);
   }
   if(result.Trim()=="success"){
     //alert("Be successful to delete the field !");
     objTable.rows[rowId].cells[colId].setAttribute("id","-999");
     if(sectionType=="02" || sectionType=="03"){
       objTable.rows[rowId].cells[colId].innerHTML = "&nbsp;&nbsp;";
     }else{
       objTable.rows[0].cells[colId].innerHTML = "&nbsp;&nbsp;";
       objTable.rows[1].cells[colId].innerHTML = "&nbsp;&nbsp;";
     }
     window.document.location.reload();
   }else{
     alert(operate_fail);
   }    
 }
 
 function deleteSection(tableId,divId,sectionType,sectionId){
   if(sectionType=="03"){
     alert(can_not_del_basic);
     return;
   } 
   if(confirm(confirm_delete_section)==false){
     return;
   }
   
   var formSystemId = document.forms[0].formSystemId.value;
   
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=deleteFormSection&formSystemId="+formSystemId
             +"&sectionId="+sectionId;

   var  xmlhttp = createXMLHttpRequest();
   var result = "";
   if(xmlhttp){
      xmlhttp.open('POST',url,false);
      xmlhttp.onreadystatechange = function()
      {
             if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
                 result = xmlhttp.responseText;
             }
       }
       xmlhttp.setRequestHeader("If-Modified-Since","0");
       xmlhttp.send(null);
   }
   if(result.Trim()=="success"){
     //document.getElementById(tableId).style.display = "none";
     //document.getElementById(divId).style.display = "none";
     window.location.reload(); 
     alert(operate_success)
   }else{
     alert(result);
   }
 }
 
 function SelectRow(objTD)
 {
 var objTR =objTD.parentElement;
 var objTable = objTR.parentElement;
 for(var i=0;i<objTable.rows.length;i++)
  for(var j=0;j<objTable.rows[i].cells.length;j++) 
  {
    objTable.rows[i].cells[j].bgColor = "#ffffff";
  }
  
 objTD.bgColor="#E7EAF5";
 currRowIndex = objTR.rowIndex;
 
} 

 function SelectCol(objTD)
 {
 var objTR =objTD.parentElement;
 var objTable = objTR.parentElement;
 //for(var i=0;i<objTable.rows.length;i++)
  for(var j=0;j<objTable.rows[1].cells.length;j++) 
  {
    objTable.rows[1].cells[j].bgColor = "#ffffff";
  }
  
 objTD.bgColor="#E7EAF5";
 currRowIndex = objTR.rowIndex;
 
//} 
}
 
  function editField(objTD,tableId,sectionId,fieldId,sectionType){
     var objTR =objTD.parentElement;
     var objTable = objTR.parentElement;
     var type = "edit";
     if(fieldId=="-999"){
        type = "new";
        if(objTD.getAttribute("id")!="" && objTD.getAttribute("id")!="-999"){
          fieldId = objTD.getAttribute("id");
          type = "edit";
        }
     }

     var url = "<%=request.getContextPath()%>/formManageAction.it?method=enterEditFormField&type="+type+"&rowId="+objTR.rowIndex+"&colId="
               +objTD.cellIndex+"&formSystemId="+document.forms[0].formSystemId.value+"&sectionId="+sectionId+"&tableId="+tableId
               +"&fieldId="+fieldId+"&sectionType="+sectionType+"&formStatus="+document.forms[0].status.value;

     openCenterWindow(url,500,550);
  }
  
  function editSection(url){
    //var newWindow = window.open("","test","height=1, width=1, top=0, left=0,toolbar =no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
    url = url + "&type=edit&tableId=formSpace";
    openCenterWindow(url,460,150);
  }
 
  function test(obj){
    alert(obj.innerText)
  }
  function previewForm(formSystemId){
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=displayFormFill&type=preview&formSystemId="+formSystemId;
    openCenterWindow(url,800,600);
  }
  
  function adjustSection(formSystemId){
    var url = "<%=request.getContextPath()%>/formManageAction.it?method=preAdjustSectionOrder&formSystemId="+formSystemId;
    openCenterWindow(url,600,250);
  }
  
  function goBackFormList(){
    window.location = "<%=request.getContextPath()%>/formManageAction.it?method=manageForm";
  }
  
 function mergeColumn(tblId,sectionId){
   var fieldtable = document.getElementById(tblId);          
   if (fieldtable!=null){
       	var RowCount = fieldtable.rows.length;       	
		var CurrentRow = null;
  		for(var i=0;i<RowCount;i++){
  			CurrentRow = fieldtable.rows(i);
  			if (CurrentRow.cells(1)==null){
	  			if (CurrentRow.cells(0).isSingle=='Yes') {    
		  			CurrentRow.cells(0).colSpan = 3;
  				}else if (i!= RowCount-1){
  					CurrentRow.cells(0).colSpan = 3;			//20150317 Justin Bin Founded
  				}else{
  				    var oTR = fieldtable.rows(i);
  				    if (oTR!=null){
  				    	var oTD = oTR.insertCell(1);
	  				    oTD.innerHTML  = "&nbsp;&nbsp;";
				        oTD.onclick = new Function("SelectRow(this)");
				        oTD.ondblclick =new Function('editField(this,"sectionTable'+sectionId+'","'+sectionId+'","-999","02")');
  					}
  				}
  			}
  		}
  	}
  }
  
  /**
  *To trigger the System Field [Selecting Reference Form Window]
  **/
  function	showRefFormWindow(){ 
	var url = "<%=request.getContextPath()%>/formManageAction.it?method=enterRefFormWindow&sfilId=reference_form&formId="+document.forms[0].formId.value; 		
  	returnTarget = showModalDialog(url,window,"dialogWidth:500px; dialogHeight:400px;help:0;status:1;resizeable:1;");    
    if(returnTarget != undefined && returnTarget.length > 1) {
    	var obj = document.getElementById('divrefeformId');
		obj.innerHTML=returnTarget;
	}	
  }
  
  function adjustFieldOrder(sectionId,sectionType){
      var url = "<%=request.getContextPath()%>/formManageAction.it?method=preAdjustFieldOrder&formSystemId=<%=form.getFormSystemId()%>"
                +"&sectionId="+sectionId+"&sectionType="+sectionType;
      openCenterWindow(url,600,250);
  }
  
  function adjustColumnWidth(sectionId,sectionType){
      var url = "<%=request.getContextPath()%>/formManageAction.it?method=preAdjustColumnWidth&formSystemId=<%=form.getFormSystemId()%>"
                +"&sectionId="+sectionId+"&sectionType="+sectionType;
      openCenterWindow(url,600,250);
  }
  
</script>

</head>

<body>

  <form name='createForm'>
   <div id=myadd>
    <input type="hidden" name="formSystemId" value="<%=form.getFormSystemId()%>">
    <input type="hidden" name="formId" value="<%=form.getFormId()%>">
    <input type="hidden" name="formName" value="<%=form.getFormName()%>">
    <input type="hidden" name="status" value="<%=form.getStatus()%>">
    <input type="hidden" name="formType" value="<%=form.getFormType()%>">
    <input type="hidden" name="formDescription" value="<%=form.getDescription()%>">
    <table id='formSpace' width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td colspan='3' align='center'><%=form.getFormName()%></td>
     </tr>
     <tr>
       <td colspan='3' align='center'>
        <input type='button' value='<i18n:message key="form_design.create_section"/>' onclick='createFormSection()' <%=(isPublished==true?"":"")%> class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        &nbsp;&nbsp;<input type='button' value='<i18n:message key="form_design.adjust_order"/>' onclick='adjustSection("<%=form.getFormSystemId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        &nbsp;&nbsp;<input type='button' value='<i18n:message key="form_design.preview"/>' onclick='previewForm("<%=form.getFormSystemId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        &nbsp;&nbsp;<input type='button' value='<i18n:message key="button.back"/>' onclick='goBackFormList()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
       <% 
         Collection sectionList = form.getSectionList();
         Iterator sectionIt = sectionList.iterator();
         //Added to supporting the input parameter for the createControl() method
         StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
         while(sectionIt.hasNext()){
        	 out.println("<tr><td>&nbsp;&nbsp;</td></tr>");
        	 FormSectionVO section = (FormSectionVO)sectionIt.next();
        	 if("01".equals(section.getSectionType())){
       %>
       <tr>
         <td>
         <div id="div<%=section.getSectionId()%>">
           <b><a href="javascript:editSection('<%=request.getContextPath()%>/formManageAction.it?method=enterEditFormSection&formSystemId=<%=form.getFormSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
              <input type='button' name='addBtn<%=section.getSectionId()%>' value='<i18n:message key="form_design.add_column"/>' onclick='addCol("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
               &nbsp;<input type='button' name='delBtn<%=section.getSectionId()%>' value='<i18n:message key="form_design.delete_column"/>' onclick='deleteCol("sectionTable<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='addColField<%=section.getSectionId()%>' value='<i18n:message key="form_design.add_field"/>' onclick='addColField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='delField<%=section.getSectionId()%>' value='<i18n:message key="form_design.delete_field"/>' onclick='deleteField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","01")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' <%=isPublished?"disabled":""%> name='delSection<%=section.getSectionId()%>' value='<i18n:message key="form_design.delete_section"/>' onclick='deleteSection("sectionTable<%=section.getSectionId()%>","div<%=section.getSectionId()%>","<%=section.getSectionType()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           &nbsp;<input type='button' name='adjustField<%=section.getSectionId()%>' value='<i18n:message key="form_design.adjust_field_order"/>' onclick='adjustFieldOrder("<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           &nbsp;<input type='button' name='adjustWidth<%=section.getSectionId()%>' value='<i18n:message key="form_design.adjust_col_width"/>' onclick='adjustColumnWidth("<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">           
           </div> 
         </td>
       </tr>
       <tr>
         <td>
           <table id='sectionTable<%=section.getSectionId()%>' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>
             	
               <%
                 Collection fieldList = section.getFieldList();
                 
                 if(fieldList!=null && fieldList.size()>0){
                	Iterator fieldIt = fieldList.iterator();
                	String defaultColumnWidth = "10";
                %>
                <tr class='tr4'>
                  <%
                	while(fieldIt.hasNext()){
                	   FormSectionFieldVO field = (FormSectionFieldVO)fieldIt.next();
                	   if(field.getFieldType()==CommonName.FIELD_TYPE_IDENTITY){
                 		  continue;
                 	  }
                 %>
                    <td align='center'><%=field.getFieldLabel()%></td>
                 <%}%>
                 </tr> 
                <tr>
                  <% 
                   fieldIt = fieldList.iterator();
              	   while(fieldIt.hasNext()){
             	     FormSectionFieldVO field = (FormSectionFieldVO)fieldIt.next();
              	     if(field.getFieldType()==CommonName.FIELD_TYPE_IDENTITY){
              		  continue;
              	    }
                  %>
                    <td onclick='SelectRow(this)' width='<%=field.getControlsWidth()==0?defaultColumnWidth:""+field.getControlsWidth()%>%' id='<%=field.getFieldId()%>' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","<%=field.getFieldId()%>","01")'>
                      <% 
                        //out.println(FieldControlHelper.createControl(field,"01",-1));
                        out.println(FormFieldHelper.showEditField(field,form,staff,null,null,form.formSystemId,false));
                      %>
                    </td>
                    
                 <%}%> 
                                  
                </tr>
               <%}else{%>
                 <tr class='tr4'><td onclick='SelectRow(this)'>&nbsp;&nbsp;</td></tr> 
                 <tr><td onclick='SelectRow(this)' id='-999' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","-999","01")' >&nbsp;&nbsp;</td></tr>
               <%}%>             
           </table>
         </td>
       </tr>
       <%
        }else if("02".equals(section.getSectionType()) || "03".equals(section.getSectionType())){//if("01".equals(section.getSectionType())){
       %>
         <tr>
          <td>
          <div id="div<%=section.getSectionId()%>">
           <b><a href="javascript:editSection('<%=request.getContextPath()%>/formManageAction.it?method=enterEditFormSection&formSystemId=<%=form.getFormSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
              <input type='button' name='addBtn<%=section.getSectionId()%>' value='<i18n:message key="form_design.add_row"/>' onclick='addRow("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='delBtn<%=section.getSectionId()%>' value='<i18n:message key="form_design.delete_row"/>' onclick='deleteRow("sectionTable<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='addField<%=section.getSectionId()%>' value='<i18n:message key="form_design.add_field"/>' onclick='addField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='delField<%=section.getSectionId()%>' value='<i18n:message key="form_design.delete_field"/>' onclick='deleteField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","02")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' <%=isPublished?"disabled":""%> name='delSection<%=section.getSectionId()%>' value='<i18n:message key="form_design.delete_section"/>' onclick='deleteSection("sectionTable<%=section.getSectionId()%>","div<%=section.getSectionId()%>","<%=section.getSectionType()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           &nbsp;<input type='button' name='adjustField<%=section.getSectionId()%>' value='<i18n:message key="form_design.adjust_field_order"/>' onclick='adjustFieldOrder("<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
            </div>
           </td>
         </tr>
         <tr>
          <td>
            <table id='sectionTable<%=section.getSectionId()%>' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>
                <%
                 Collection fieldList = section.getFieldList();
                 int count = 1;
                 if(fieldList==null || fieldList.size()==0)
                 {
               %>
                 <tr><td onclick='SelectRow(this)' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","-999","02")'>&nbsp;&nbsp;</td><td onclick='SelectRow(this)' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","-999","02")'>&nbsp;&nbsp;</td></tr>
               <%}else{
            	   Iterator fieldIt = fieldList.iterator();
            	   FormSectionFieldVO fieldPrv=null;
                   int prvIsSingleRow=0;
              	   while(fieldIt.hasNext()){
              		 if (fieldPrv!=null){
             	    	prvIsSingleRow = fieldPrv.getIsSingleRow();
             	     }                	   
               	     FormSectionFieldVO field = (FormSectionFieldVO)fieldIt.next();
               	     fieldPrv = field;
               	     if("1".equals(""+field.getIsSingleRow())){
               	  		out.println("<tr>");
       	        		if (count>1){
       	        			count = count-1;	
       	        		}
       	        	 }else if(count%formColumns==1){			//20150318 Justin Bin
       	        		out.println("<tr>");
       	        	 }else if(prvIsSingleRow==1){
       	 	        	out.println("<tr>");
       	 	        	if (count>1){
	        				count = count-1;	
	        			}
       	 	         }               	     
               	 	 if("1".equals(""+field.getIsSingleRow())){
             			if("02".equals(section.getSectionType())){
           	    	  		out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel()+" : ");
		         	    	//out.println(FieldControlHelper.createControl(field,"02",-1));
		         	    	out.println(FormFieldHelper.showEditField(field,form,staff,null,null,form.formSystemId,false));
           	        	}else if("03".equals(section.getSectionType())){
           	    	  	    //Basic information field can't be updated
           	    	  	    if(field.getFieldType()==CommonName.FIELD_TYPE_BASIC){
           	    	     		out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"'>"+field.getFieldLabel()+" : ");  
                      		}else{
                    	  		out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel()+" : "); 
                      		}
           	    	  	    //out.println(FieldControlHelper.createBaseInforControl(false,field,form,request));
           	    	  	    out.println(FormFieldHelper.showEditField(field,form,staff,null,null,form.formSystemId,false));
           	         	}
             	 	 }else{
             		   if("02".equals(section.getSectionType())){
             	    	  out.println("<td id='"+field.getFieldId()+"' isSingle='No' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel()+" : ");
             	    	  //out.println(FieldControlHelper.createControl(field,"02",-1));
             	    	 out.println(FormFieldHelper.showEditField(field,form,staff,null,null,form.formSystemId,false));
             	       }else if("03".equals(section.getSectionType())){                      
             	    	  if(field.getFieldType()==CommonName.FIELD_TYPE_BASIC){
             	    	     out.println("<td isSingle='No' id='"+field.getFieldId()+"'>"+field.getFieldLabel()+" : ");  
                          }else{
                      	  	 out.println("<td isSingle='No' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel()+" : "); 
                          }
             	    	  //out.println(FieldControlHelper.createBaseInforControl(false,field,form,request));
             	    	 out.println(FormFieldHelper.showEditField(field,form,staff,null,null,form.formSystemId,false));
             	       }
             	     }               	    
              	     out.println("</td>");
              	  	 //comment by young begin
              	     //if(count%2==0){
                	 //	out.println("</tr>");
                	 //  }  
              	     //count++;
	              	 //comment by young end
              	  	 //Added by young begin
                	 if(("1".equals(""+field.getIsSingleRow()))||count%formColumns==0){		//20150318 Justin Bin Added
                	 	 out.println("</tr>");
               	     }
                	 if(!"1".equals(""+field.getIsSingleRow())){
	                     count++;		
                	 }
                	//Added by young end
                   }//end of while(fieldIt.hasNext())
              	   if(count%2==0){
              	   		out.println("<td id='' ondblclick='editField(this,\"sectionTable"+
              	   		section.getSectionId()+"\",\""+section.getSectionId()+"\",\"-999\",\"02\")' onclick='SelectRow(this)'>"+"&nbsp;&nbsp;</td></tr>");
                   }
                 }//end of (fieldList==null || fieldList.size()==0)   
               %>
            </table>
            <script language="javascript">
				mergeColumn("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>");
		    </script>
          </td>
         </tr>
         
       <%    	
        }else if("00".equals(section.getSectionType())){ // attached file	
      %>
        <tr>
          <td>
          <div id="div<%=section.getSectionId()%>">
           <b><a href="javascript:editSection('<%=request.getContextPath()%>/formManageAction.it?method=enterEditFormSection&formSystemId=<%=form.getFormSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
              &nbsp;<input type='button' <%=isPublished?"disabled":""%> name='delSection<%=section.getSectionId()%>' value='<i18n:message key="form_design.delete_section"/>' onclick='deleteSection("sectionTable<%=section.getSectionId()%>","div<%=section.getSectionId()%>","<%=section.getSectionType()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
            </div>
           </td>
         </tr>
         <tr>
           <td>
            <table id='sectionTable<%=section.getSectionId()%>' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>
             <tr>
               <td align='right'>
                 <i18n:message key="form_design.select_file"/> :
               </td>
               <td align='left'>
                 <input type="file" name="path" size="38" />&nbsp;&nbsp;<input type="button"  name="submitBtn" value='<i18n:message key="button.upload"/>'>
               </td>
            </tr>
            <tr>
              <td align='right'>
                <i18n:message key="common.file_description"/>:
              </td>
              <td align='left'>
                   <input type="text" name="fileDescription" size="38"/>
              </td>
           </tr>
           </table>
           </td>
         </tr>
      <%    
        }
       }// while(sectionIt.hasNext()){
       %>
   </table>
   </div>
  </form>
  
</body>

</html>