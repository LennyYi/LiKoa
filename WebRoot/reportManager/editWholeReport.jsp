
<%@ include file="/common/head.jsp" %>
<%@ include file="/common/loading.jsp" %>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@page import="java.util.*,com.aiait.eflow.reportmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.common.*,java.sql.Connection,java.sql.PreparedStatement,java.sql.ResultSet" %>
<%@page import="com.aiait.framework.db.*,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.reportmanage.helper.*,com.aiait.framework.db.IDBManager" %>
<% 
    ReportManageVO report = (ReportManageVO)request.getAttribute("wholeReport");
    
    String reportStatus = report.getStatus();
    boolean isPublished = false;
    if("0".equals(reportStatus)){
    	isPublished = true;
    }
    
    
    
%>
<html>
<head>
<title>Edit Report</title>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
   <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
<script language="javascript">
   function createReportSection(){
      var url = "<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportSection&type=new&tableId=reportSpace&reportSystemId="
                 +document.forms[0].reportSystemId.value;
      openCenterWindow(url,460,200);
   }
  
   function addCol(tableId,sectionId){
    var oTable = document.getElementById(tableId);
    var rowId = 1,colId = -1;
    
    for(var j=0;j<oTable.rows[0].cells.length;j++) 
    {
        if(oTable.rows[0].cells[j].bgColor=="#e7eaf5"){
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

   function addCol_table(tableId,sectionId){
	    var oTable = document.getElementById(tableId);
	    var rowId = 1,colId = -1;
	    
	    for(var j=0;j<oTable.rows[0].cells.length;j++) 
	    {
	        if(oTable.rows[0].cells[j].bgColor=="#e7eaf5"){
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
    
    var url = "<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportField&type=new&rowId="+rowId+"&colId="
               +colId+"&reportSystemId="+document.forms[0].reportSystemId.value+"&sectionId="+sectionId+"&tableId="+tableId+"&sectionType=01"; 
    openCenterWindow(url,500,550);
  }
  
   function addRow(tableId,sectionId){
     var oTable = document.getElementById(tableId);
     var oTR = oTable.insertRow(oTable.rows.length);
     var cel = oTable.rows[0].cells.length;
     for(i=0;i<cel;i++){
       var oTD = oTR.insertCell(i);
       //oTD.innerHTML  = "<input type='text' name='name'>";
       oTD.innerHTML  = "&nbsp;&nbsp;";
       //oTD.ondblclick =new Function("test(this)");
       oTD.onclick = new Function("SelectRow(this)");
       oTD.ondblclick =new Function('editField(this,"sectionTable'+sectionId+'","'+sectionId+'","-999","02")');
     }
  }

   function addRow_08(tableId,sectionId){
	     var oTable = document.getElementById(tableId);
	     var oTR = oTable.insertRow(oTable.rows.length);
	     var cel = 2;
	     for(i=0;i<cel;i++){
	       var oTD = oTR.insertCell(i);
	       //oTD.innerHTML  = "<input type='text' name='name'>";
	       oTD.innerHTML  = "&nbsp;&nbsp;";
	       //oTD.ondblclick =new Function("test(this)");
	       oTD.onclick = new Function("SelectRow(this)");
	       oTD.ondblclick =new Function('editField(this,"sectionTable'+sectionId+'","'+sectionId+'","-999","02")');
	     }
	  }
   function addRow_table(tableId,sectionId){
	     var oTable = document.getElementById(tableId);
	     var oTR = oTable.insertRow(oTable.rows.length);
	     var cel = oTable.rows[0].cells.length;
	     for(i=0;i<cel;i++){
	       var oTD = oTR.insertCell(i);
	       oTD.innerHTML  = "&nbsp;&nbsp;";
	       oTD.onclick = new Function("SelectRow(this)");
	      
	     }
	  }
	  
   function addHeadRow(tableId,sectionId){
	     var oTable = document.getElementById(tableId);
	     var oTR = oTable.insertRow(oTable.rows.length);
	     oTR.className="tr2";
	     var cel = oTable.rows[0].cells.length;
	     for(i=0;i<cel;i++){
	       var oTD = oTR.insertCell(i);
	       oTD.innerHTML  = "&nbsp;&nbsp;";
	       oTD.onclick = new Function("SelectRow(this)");
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
     var url = "<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportField&type=new&rowId="+rowId+"&colId="
               +colId+"&reportSystemId="+document.forms[0].reportSystemId.value+"&sectionId="+sectionId+"&tableId="+tableId+"&sectionType="+sectionType
               +"&reportStatus="+document.forms[0].status.value;

     openCenterWindow(url,500,550);
  }
  
 function deleteCol(tableId){
   if(confirm(confirm_delete_column)){
       var objTable = document.getElementById(tableId);
       var rowId = 1,colId = -1;
       for(var j=0;j<objTable.rows[0].cells.length;j++) 
       {
           if(objTable.rows[0].cells[j].bgColor=="#e7eaf5"){
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
   
   var status = "<%=reportStatus%>";
   if(status=="0"){
     if(confirm(confirm_leave_report)==false){
       return;
     }
   }
   
   var url = "<%=request.getContextPath()%>/reportManageAction.it?method=deleteReportField&reportSystemId="+document.forms[0].reportSystemId.value
             +"&sectionId="+sectionId+"&fieldId="+objTable.rows[rowId].cells[colId].getAttribute("id")
             +"&reportStatus="+status;

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
   
   var reportSystemId = document.forms[0].reportSystemId.value;
   
    var url = "<%=request.getContextPath()%>/reportManageAction.it?method=deleteReportSection&reportSystemId="+reportSystemId
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
  for(var j=0;j<objTable.rows[0].cells.length;j++) 
  {
    objTable.rows[0].cells[j].bgColor = "#ffffff";
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

     var url = "<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportField&type="+type+"&rowId="+objTR.rowIndex+"&colId="
               +objTD.cellIndex+"&reportSystemId="+document.forms[0].reportSystemId.value+"&sectionId="+sectionId+"&tableId="+tableId
               +"&fieldId="+fieldId+"&sectionType="+sectionType+"&reportStatus="+document.forms[0].status.value;

     openCenterWindow(url,500,550);
  }
  
  function editSection(url){
    //var newWindow = window.open("","test","height=1, width=1, top=0, left=0,toolbar =no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
    url = url + "&type=edit&tableId=reportSpace";
    openCenterWindow(url,460,150);
  }
 
  function test(obj){
    alert(obj.innerText)
  }
  function previewReport(reportSystemId){
    var url = "<%=request.getContextPath()%>/reportManageAction.it?method=displayReportFill&type=preview&reportSystemId="+reportSystemId;
    openCenterWindow(url,800,600);
  }
  
  function adjustSection(reportSystemId){
    var url = "<%=request.getContextPath()%>/reportManageAction.it?method=preAdjustSectionOrder&reportSystemId="+reportSystemId;
    openCenterWindow(url,600,250);
  }
  
  function goBackReportList(){
    window.location = "<%=request.getContextPath()%>/reportManageAction.it?method=manageReport";
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
		  			CurrentRow.cells(0).colSpan = 2;
  				}else if (i!= RowCount-1){
  					CurrentRow.cells(0).colSpan = 2;
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
  *To trigger the System Field [Selecting Reference Report Window]
  **/
  function	showRefReportWindow(){ 
	var url = "<%=request.getContextPath()%>/reportManageAction.it?method=enterRefReportWindow&sfilId=reference_report&reportId="+document.forms[0].reportId.value; 		
  	returnTarget = showModalDialog(url,window,"dialogWidth:500px; dialogHeight:400px;help:0;status:1;resizeable:1;");    
    if(returnTarget != undefined && returnTarget.length > 1) {
    	var obj = document.getElementById('divrefereportId');
		obj.innerHTML=returnTarget;
	}	
  }
  
  function adjustFieldOrder(sectionId,sectionType){
      var url = "<%=request.getContextPath()%>/reportManageAction.it?method=preAdjustFieldOrder&reportSystemId=<%=report.getReportSystemId()%>"
                +"&sectionId="+sectionId+"&sectionType="+sectionType;
      openCenterWindow(url,600,250);
  }
  
  function adjustColumnWidth(sectionId,sectionType){
      var url = "<%=request.getContextPath()%>/reportManageAction.it?method=preAdjustColumnWidth&reportSystemId=<%=report.getReportSystemId()%>"
                +"&sectionId="+sectionId+"&sectionType="+sectionType;
      openCenterWindow(url,600,250);
  }
  function editReportHtmlCode(sectionId) {
		var retValue = showModalDialog('<%=request.getContextPath()%>/reportManageAction.it?method=editReportHtml&reportSystemId=<%=report.getReportSystemId()%>&sectionId='+sectionId, window, 'dialogWidth:900px; dialogHeight:600px; help:0; status:0; resizeable:1;')
  }

  function editReportStyle() {
		var retValue = showModalDialog('<%=request.getContextPath()%>/reportManageAction.it?method=editReportStyle&reportSystemId=<%=report.getReportSystemId()%>', window, 'dialogWidth:900px; dialogHeight:600px; help:0; status:0; resizeable:1;')
  }

  function editReportScript() {
		var retValue = showModalDialog('<%=request.getContextPath()%>/reportManageAction.it?method=editReportScript&reportSystemId=<%=report.getReportSystemId()%>', window, 'dialogWidth:900px; dialogHeight:600px; help:0; status:0; resizeable:1;')
	}
  
</script>

</head>

<body>

  <form name='createReport'>
   <div id=myadd>
    <input type="hidden" name="reportSystemId" value="<%=report.getReportSystemId()%>">
    <input type="hidden" name="reportId" value="<%=report.getReportId()%>">
    <input type="hidden" name="reportName" value="<%=report.getReportName()%>">
    <input type="hidden" name="status" value="<%=report.getStatus()%>">
    <input type="hidden" name="reportType" value="<%=report.getReportType()%>">
    <input type="hidden" name="reportDescription" value="<%=report.getDescription()%>">
    <table id='reportSpace' width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     
     <tr>
       <td colspan='2' align='center'>
        <input type='button' value='<i18n:message key="report_design.create_section"/>' onclick='createReportSection()' <%=(isPublished==true?"":"")%> class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        &nbsp;&nbsp;<input type='button' value='<i18n:message key="report_design.adjust_order"/>' onclick='adjustSection("<%=report.getReportSystemId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        &nbsp;&nbsp;<input type='button' value='<i18n:message key="report_design.edit_style"/>' onclick='editReportStyle()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"> 
                
        &nbsp;&nbsp;<input type='button' value='<i18n:message key="report_design.user_defined_function"/>' onclick='editReportScript()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'"> 
           
        &nbsp;&nbsp;<input type='button' value='<i18n:message key="report_design.preview"/>' onclick='previewReport("<%=report.getReportSystemId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
        &nbsp;&nbsp;<input type='button' value='<i18n:message key="button.back"/>' onclick='goBackReportList()' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
       </td>
      </tr>
       <% 
         Collection sectionList = report.getSectionList();
         Iterator sectionIt = sectionList.iterator();
         //Added to supporting the input parameter for the createControl() method
         StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
         while(sectionIt.hasNext()){
        	 out.println("<tr><td>&nbsp;&nbsp;</td></tr>");
        	 ReportSectionVO section = (ReportSectionVO)sectionIt.next();
        	 if("title".equals(section.getSectionId())){
        		 List<ReportSectionFieldVO> list = (List<ReportSectionFieldVO>) section.getFieldList();
        		 String alignStr = "";
        		 if(list.get(0).getAligned()==1){
        			 alignStr = "left";
        		 }else if(list.get(0).getAligned()==2){
        			 alignStr="center";
        		 }else if(list.get(0).getAligned()==3){
        			 alignStr="right";
        		 }
        		 String[] tmp = list.get(0).getCssStr().split(";");
                 String[] textTmp = tmp[0].split(",");
                 String[] valueTmp = tmp[1].split(",");
                 String textCssStyle="left:"+textTmp[0]+"px;top:"+textTmp[1]+"px;font:"+textTmp[3]+" "+textTmp[4]+" "+textTmp[2]+"px "+textTmp[5]+";";
                 String valueCssStyle="left:"+valueTmp[0]+"px;top:"+valueTmp[1]+"px;font:"+valueTmp[3]+" "+valueTmp[4]+" "+valueTmp[2]+"px "+valueTmp[5]+";";
                 
        	%>
        	 
        		 <tr>
        	       <td colspan='2' align='<%=alignStr%>' ondblclick='editField(this,"","title","field_title","02")'>
        	       <div style='position:relative;<%=valueCssStyle%>'><%=list.get(0).getDefaultValue()%></div>
        	       </td>
        	     </tr>
        	<%
        	 }else{
        		 
        	 
        	 if("01".equals(section.getSectionType())){
       %>
       <tr>
         <td>
         <div id="div<%=section.getSectionId()%>">
           <b><a href="javascript:editSection('<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportSection&reportSystemId=<%=report.getReportSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
              <input type='button' name='addBtn<%=section.getSectionId()%>' value='<i18n:message key="report_design.add_column"/>' onclick='addCol("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
               &nbsp;<input type='button' name='delBtn<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_column"/>' onclick='deleteCol("sectionTable<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='addColField<%=section.getSectionId()%>' value='<i18n:message key="report_design.add_field"/>' onclick='addColField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='delField<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_field"/>' onclick='deleteField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","01")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' <%=isPublished?"disabled":""%> name='delSection<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_section"/>' onclick='deleteSection("sectionTable<%=section.getSectionId()%>","div<%=section.getSectionId()%>","<%=section.getSectionType()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           &nbsp;<input type='button' name='adjustField<%=section.getSectionId()%>' value='<i18n:message key="report_design.adjust_field_order"/>' onclick='adjustFieldOrder("<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           &nbsp;<input type='button' name='adjustWidth<%=section.getSectionId()%>' value='<i18n:message key="report_design.adjust_col_width"/>' onclick='adjustColumnWidth("<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
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
                	   ReportSectionFieldVO field = (ReportSectionFieldVO)fieldIt.next();
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
             	     ReportSectionFieldVO field = (ReportSectionFieldVO)fieldIt.next();
              	     if(field.getFieldType()==CommonName.FIELD_TYPE_IDENTITY){
              		  continue;
              	    }
                  %>
                    <td onclick='SelectRow(this)' width='<%=field.getControlsWidth()==0?defaultColumnWidth:""+field.getControlsWidth()%>%' id='<%=field.getFieldId()%>' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","<%=field.getFieldId()%>","01")'>
                      <% 
                        //out.println(FieldControlHelper.createControl(field,"01",-1));
                        out.println(ReportFieldHelper.showEditField(field,report,staff,null,null));
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
        }else if("02".equals(section.getSectionType()) || "03".equals(section.getSectionType())){
       %>
         <tr>
          <td>
          <div id="div<%=section.getSectionId()%>">
           <b><a href="javascript:editSection('<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportSection&reportSystemId=<%=report.getReportSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
              <input type='button' name='addBtn<%=section.getSectionId()%>' value='<i18n:message key="report_design.add_row"/>' onclick='addRow("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='delBtn<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_row"/>' onclick='deleteRow("sectionTable<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='addField<%=section.getSectionId()%>' value='<i18n:message key="report_design.add_field"/>' onclick='addField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='delField<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_field"/>' onclick='deleteField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","02")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' <%=isPublished?"disabled":""%> name='delSection<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_section"/>' onclick='deleteSection("sectionTable<%=section.getSectionId()%>","div<%=section.getSectionId()%>","<%=section.getSectionType()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           &nbsp;<input type='button' name='adjustField<%=section.getSectionId()%>' value='<i18n:message key="report_design.adjust_field_order"/>' onclick='adjustFieldOrder("<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
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
                 int line = 0;
                 if("02".equals(section.getSectionType())){
                	 line = 2;
                 }else{
                	 line = 3;
                 }

                 if(fieldList==null || fieldList.size()==0)
                 {
               %>
                 <tr><td onclick='SelectRow(this)' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","-999","02")'>&nbsp;&nbsp;</td><td onclick='SelectRow(this)' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","-999","02")'>&nbsp;&nbsp;</td></tr>
               <%}else{
            	   Iterator fieldIt = fieldList.iterator();
            	   ReportSectionFieldVO fieldPrv=null;
                   int prvIsSingleRow=0;
              	   while(fieldIt.hasNext()){
              		 if (fieldPrv!=null){
             	    	prvIsSingleRow = fieldPrv.getIsSingleRow();
             	     }                	   
               	     ReportSectionFieldVO field = (ReportSectionFieldVO)fieldIt.next();
               	     fieldPrv = field;
               	     if("1".equals(""+field.getIsSingleRow())){
               	  		out.println("<tr>");
       	        		if (count>1){
       	        			count = count-1;	
       	        		}
       	        	 }else if(count%line==1){		
       	        		out.println("<tr>");
       	        	 }else if(prvIsSingleRow==1){
       	 	        	out.println("<tr>");
       	 	        	if (count>1){
	        				count = count-1;	
	        			}
       	 	         }   
               	  String[] tmp = field.getCssStr().split(";");
                  String[] textTmp = tmp[0].split(",");
                  String[] valueTmp = tmp[1].split(",");
                  String textCssStyle="left:"+textTmp[0]+"px;top:"+textTmp[1]+"px;font:"+textTmp[3]+" "+textTmp[4]+" "+textTmp[2]+"px "+textTmp[5]+";";
                  String valueCssStyle="left:"+valueTmp[0]+"px;top:"+valueTmp[1]+"px;font:"+valueTmp[3]+" "+valueTmp[4]+" "+valueTmp[2]+"px "+valueTmp[5]+";";
                  
               	 	 if("1".equals(""+field.getIsSingleRow())){
               	 		if("2".equals(""+field.getFieldType())){
               	 			field.setFieldComments(field.getFieldComments().replace("@path",request.getContextPath()));
               	 			
               	 			 if("0".equals(""+field.getControlsHeight())){
               	 				out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'><div style='position:relative;"+textCssStyle+"'>"+field.getFieldComments()+"</div>"); 
               	 			 }else{
               	 				out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'><div style='position:relative;"+textCssStyle+"height:"+field.getControlsHeight()+"'>"+field.getFieldComments()+"</div>"); 
               	 			 }
               	 			
               	 		 }else{
               	 			 
               	 		 
             			if("02".equals(section.getSectionType())){
           	    	  		out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel()+" : ");
		         	    	//out.println(FieldControlHelper.createControl(field,"02",-1));
		         	    	out.println(ReportFieldHelper.showEditField(field,report,staff,null,null));
           	        	}else if("03".equals(section.getSectionType())){
           	    	  	    //Basic inreportation field can't be updated
           	    	  	    if(field.getFieldType()==CommonName.FIELD_TYPE_BASIC){
           	    	     		out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"'>"+field.getFieldLabel()+" : ");  
                      		}else{
                    	  		out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel()+" : "); 
                      		}
           	    	  	    //out.println(FieldControlHelper.createBaseInforControl(false,field,report,request));
           	    	  	    out.println(ReportFieldHelper.showEditField(field,report,staff,null,null));
           	         	}}
             	 	 }else{
             	 		if("2".equals(""+field.getFieldType())){
               	 			field.setFieldComments(field.getFieldComments().replace("@path",request.getContextPath()));
               	 			
               	 			 if("0".equals(""+field.getControlsHeight())){
               	 				out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'><div style='position:relative;"+textCssStyle+"'>"+field.getFieldComments()+"</div>"); 
               	 			 }else{
               	 				out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'><div style='position:relative;"+textCssStyle+"height:"+field.getControlsHeight()+"'>"+field.getFieldComments()+"</div>"); 
               	 			 }
               	 			
               	 		 }else{
             		   if("02".equals(section.getSectionType())){
             	    	  out.println("<td id='"+field.getFieldId()+"' isSingle='No' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel()+" : ");
             	    	  //out.println(FieldControlHelper.createControl(field,"02",-1));
             	    	 out.println(ReportFieldHelper.showEditField(field,report,staff,null,null));
             	       }else if("03".equals(section.getSectionType())){                      
             	    	  if(field.getFieldType()==CommonName.FIELD_TYPE_BASIC){
             	    	     out.println("<td isSingle='No' id='"+field.getFieldId()+"'>"+field.getFieldLabel()+" : ");  
                          }else{
                      	  	 out.println("<td isSingle='No' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel()+" : "); 
                          }
             	    	  //out.println(FieldControlHelper.createBaseInforControl(false,field,report,request));
             	    	 out.println(ReportFieldHelper.showEditField(field,report,staff,null,null));
             	       }
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
                	 if(("1".equals(""+field.getIsSingleRow()))||count%line==0){
                	 	 out.println("</tr>");
               	     }
                	 if(!"1".equals(""+field.getIsSingleRow())){
	                     count++;		
                	 }
                	//Added by young end
                   }//end of while(fieldIt.hasNext())
              	   if(count%line==0){
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
        }else if("06".equals(section.getSectionType())){ 	
      %>
       <tr>
         <td>
         <div id="div<%=section.getSectionId()%>">
           <b><a href="javascript:editSection('<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportSection&reportSystemId=<%=report.getReportSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
                        
           </div> 
         </td>
       </tr>
       <tr class='tr2'>
       <td height='100'>
       </td>
       </tr>
       
       
      <% 
        }else if("08".equals(section.getSectionType())){ 
        	%>
            <tr>
              <td>
              <div id="div<%=section.getSectionId()%>">
           <b><a href="javascript:editSection('<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportSection&reportSystemId=<%=report.getReportSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
              <input type='button' name='addBtn<%=section.getSectionId()%>' value='<i18n:message key="report_design.add_row"/>' onclick='addRow_08("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='delBtn<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_row"/>' onclick='deleteRow("sectionTable<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='addField<%=section.getSectionId()%>' value='<i18n:message key="report_design.add_field"/>' onclick='addField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' name='delField<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_field"/>' onclick='deleteField("sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","02")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
              &nbsp;<input type='button' <%=isPublished?"disabled":""%> name='delSection<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_section"/>' onclick='deleteSection("sectionTable<%=section.getSectionId()%>","div<%=section.getSectionId()%>","<%=section.getSectionType()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
           &nbsp;<input type='button' name='adjustField<%=section.getSectionId()%>' value='<i18n:message key="report_design.adjust_field_order"/>' onclick='adjustFieldOrder("<%=section.getSectionId()%>","<%=section.getSectionType()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">
            </div> 
              </td>
            </tr>
            <tr>
              <td>
                <div style="height:1023px;width:625px;border:1px #D4D4D5 solid">
                 <table id='sectionTable<%=section.getSectionId()%>' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>
                <%
                 Collection fieldList = section.getFieldList();
                 int count = 1;
                 int line = 2;

                 if(fieldList==null || fieldList.size()==0)
                 {
               %>
                 <tr><td onclick='SelectRow(this)' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","-999","02")'>&nbsp;&nbsp;</td><td onclick='SelectRow(this)' ondblclick='editField(this,"sectionTable<%=section.getSectionId()%>","<%=section.getSectionId()%>","-999","02")'>&nbsp;&nbsp;</td></tr>
               <%}else{
            	   Iterator fieldIt = fieldList.iterator();
            	   ReportSectionFieldVO fieldPrv=null;
                   int prvIsSingleRow=0;
              	   while(fieldIt.hasNext()){
              		 if (fieldPrv!=null){
             	    	prvIsSingleRow = fieldPrv.getIsSingleRow();
             	     }                	   
               	     ReportSectionFieldVO field = (ReportSectionFieldVO)fieldIt.next();
               	     fieldPrv = field;
               	     if("1".equals(""+field.getIsSingleRow())){
               	  		out.println("<tr>");
       	        		if (count>1){
       	        			count = count-1;	
       	        		}
       	        	 }else if(count%line==1){		
       	        		out.println("<tr>");
       	        	 }else if(prvIsSingleRow==1){
       	 	        	out.println("<tr>");
       	 	        	if (count>1){
	        				count = count-1;	
	        			}
       	 	         }  
               	  String[] tmp = field.getCssStr().split(";");
                  String[] textTmp = tmp[0].split(",");
                  String[] valueTmp = tmp[1].split(",");
                  String textCssStyle="left:"+textTmp[0]+"px;top:"+textTmp[1]+"px;font:"+textTmp[3]+" "+textTmp[4]+" "+textTmp[2]+"px "+textTmp[5]+";";
                  String valueCssStyle="left:"+valueTmp[0]+"px;top:"+valueTmp[1]+"px;font:"+valueTmp[3]+" "+valueTmp[4]+" "+valueTmp[2]+"px "+valueTmp[5]+";";
                  
               	 	 if("1".equals(""+field.getIsSingleRow())){
               	 		 if("2".equals(""+field.getFieldType())){
               	 			field.setFieldComments(field.getFieldComments().replace("@path",request.getContextPath()));
               	 			
               	 			 if("0".equals(""+field.getControlsHeight())){
               	 				out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'><div style='position:relative;"+textCssStyle+"'>"+field.getFieldComments()+"</div>"); 
               	 			 }else{
               	 				out.println("<td colspan=2 isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'><div style='position:relative;"+textCssStyle+"height:"+field.getControlsHeight()+"'>"+field.getFieldComments()+"</div>"); 
               	 			 }
               	 			
               	 		 }else{ 
               	 			 
               	 			 String align="left";
             			  if("2".equals(""+field.getAligned())){
             				 align="center";
             			  }else if("3".equals(""+field.getAligned())){
             				 align="right";
             			  }

           	    	  		out.println("<td colspan=2 align='"+align+"' isSingle='Yes' id='"+field.getFieldId()+"' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'> <div style='position:relative;"+textCssStyle+"'>"+field.getFieldLabel());
		         	    	//out.println(FieldControlHelper.createControl(field,"02",-1));
		         	    	out.println(ReportFieldHelper.showEditField(field,report,staff,null,null)+"</div>");
           	                
               	 		 }
             	 	 }else{
             		   
             	    	  out.println("<td id='"+field.getFieldId()+"' isSingle='No' ondblclick='editField(this,\"sectionTable"+section.getSectionId()+"\",\""+section.getSectionId()+"\",\""+field.getFieldId()+"\",\"02\")' onclick='SelectRow(this)'>"+field.getFieldLabel());
             	    	  //out.println(FieldControlHelper.createControl(field,"02",-1));
             	    	 out.println(ReportFieldHelper.showEditField(field,report,staff,null,null)+"</div>");
             	       
             	     }               	    
              	     out.println("</td>");
              	  	
                	 if(("1".equals(""+field.getIsSingleRow()))||count%line==0){
                	 	 out.println("</tr>");
               	     }
                	 if(!"1".equals(""+field.getIsSingleRow())){
	                     count++;		
                	 }
                	//Added by young end
                   }//end of while(fieldIt.hasNext())
              	   if(count%line==0){
              	   		out.println("<td id='' ondblclick='editField(this,\"sectionTable"+
              	   		section.getSectionId()+"\",\""+section.getSectionId()+"\",\"-999\",\"02\")' onclick='SelectRow(this)'>"+"&nbsp;&nbsp;</td></tr>");
                   }
                 }//end of (fieldList==null || fieldList.size()==0)   
               %>
            </table>
                  
                </div>
              </td>
            </tr>
           <% 
        	
        }else if("09".equals(section.getSectionType())){ 
        	%>
            <tr>
              <td>
              <div id="div<%=section.getSectionId()%>">
                <b><a href="javascript:editSection('<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportSection&reportSystemId=<%=report.getReportSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
                                           
                &nbsp;<input type='button'  name='delSection<%=section.getSectionId()%>' value='<i18n:message key="report_design.edit_html"/>' onclick='editReportHtmlCode("<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">

                   &nbsp;<input type='button'  name='delSection<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_section"/>' onclick='deleteSection("sectionTable<%=section.getSectionId()%>","div<%=section.getSectionId()%>","<%=section.getSectionType()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">

         
                </div> 
              </td>
            </tr>
            <tr>
              <td>
                <div id='sectionTable<%=section.getSectionId()%>' ondblclick='editReportHtmlCode("<%=section.getSectionId()%>")' style="height:1023px;width:625px;border:1px #D4D4D5 solid;position:relative">
                 <%
                    if(section.getHtmlCode()!=null&& !"".equals(section.getHtmlCode())){
                       out.println(section.getHtmlCode().replace("@path",request.getContextPath()));
                    }
                 %>
                  
                </div>
              </td>
            </tr>
           <% 
        	
        }else if("0A".equals(section.getSectionType()) || "0B".equals(section.getSectionType()) || "04".equals(section.getSectionType())){ 
        	%>
            <tr>
              <td>
              <div id="div<%=section.getSectionId()%>">
                <b><a href="javascript:editSection('<%=request.getContextPath()%>/reportManageAction.it?method=enterEditReportSection&reportSystemId=<%=report.getReportSystemId()%>&sectionId=<%=section.getSectionId()%>')"><%=section.getSectionRemark()%></a>:</b>
                                           
                &nbsp;<input type='button' name='delSection<%=section.getSectionId()%>' value='<i18n:message key="report_design.edit_html"/>' onclick='editReportHtmlCode("<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">

                   &nbsp;<input type='button'  name='delSection<%=section.getSectionId()%>' value='<i18n:message key="report_design.delete_section"/>' onclick='deleteSection("sectionTable<%=section.getSectionId()%>","div<%=section.getSectionId()%>","<%=section.getSectionType()%>","<%=section.getSectionId()%>")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
                onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">

         
                </div> 
              </td>
            </tr>
            <tr>
              <td>
                <div id='sectionTable<%=section.getSectionId()%>' ondblclick='editReportHtmlCode("<%=section.getSectionId()%>")' style="height:100px;border:1px #D4D4D5 solid">
                 <%
                    if(section.getHtmlCode()!=null&& !"".equals(section.getHtmlCode())){
                       out.println(section.getHtmlCode().replace("@path",request.getContextPath()));
                    }
                 %>
                  
                </div>
              </td>
            </tr>
           <% 
        	
        }
        }
       }// while(sectionIt.hasNext()){
       %>
   </table>
   </div>
  </form>
  
</body>

</html>