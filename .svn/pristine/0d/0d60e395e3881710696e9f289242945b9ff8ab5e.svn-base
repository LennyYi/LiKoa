<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<html>
<style>
.drag{position:relative;cursor:hand}
FIELDSET{
   PADDING-RIGHT: 3px; PADDING-LEFT: 3px; PADDING-BOTTOM: 55%; PADDING-TOP: 3px
}
</style>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<script src="<%=request.getContextPath()%>/js/webtools.js" language="JavaScript"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script language="javascript">
  function showTool(){
    document.all['creatControlTool'].style.display = 'block';
  }
  
  function createFormSection(){
     var returnObj = window.showModalDialog("editFormSection.jsp","Create","dialogWidth:385px;status:no;dialogHeight:300px"); 
     if(returnObj==null) return;
     var oTable = document.all.formSpace;
     
     var oTR = oTable.insertRow(oTable.rows.length);
     var oTD = oTR.insertCell(0);
     oTD.innerHTML = "&nbsp;&nbsp;";
     oTR = oTable.insertRow(oTable.rows.length);
     oTD = oTR.insertCell(0);      
     if(returnObj.sectionType=="02"){
       oTD.innerHTML = "<b>"+returnObj.sectionRemark+":</b><input type='button' name='addBtn"+returnObj.sectionId+"' value='Add Row' onclick='addRow("+ "\"" + "sectionTable"+returnObj.sectionId +"\""+")'>";
       oTD.innerHTML = oTD.innerHTML + "&nbsp;<input type='button' name='delBtn"+returnObj.sectionId+"' value='Delete Row' onclick='deleteRow("+ "\"" + "sectionTable"+returnObj.sectionId +"\""+")'>";
       oTD.innerHTML = oTD.innerHTML + "&nbsp;<input type='button' name='addField1' value='Add Field' onclick='addField("+ "\"" + "sectionTable"+returnObj.sectionId +"\""+")'>";
       oTR = oTable.insertRow(oTable.rows.length);
       oTD = oTR.insertCell(0);
       oTD.innerHTML = "<table id='sectionTable"+returnObj.sectionId+"' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>"
        +"<tr><td onclick='SelectRow(this)'>&nbsp;&nbsp;</td><td onclick='SelectRow(this)'>&nbsp;&nbsp;</td></tr></table>";
    }else if (returnObj.sectionType=="01"){
    	 oTD.innerHTML = "<b>"+returnObj.sectionRemark+":</b><input type='button' name='addBtn"+returnObj.sectionId+"' value='Add Field' onclick='addCol("+ "\"" + "sectionTable"+returnObj.sectionId +"\""+")'>";
    	 oTD.innerHTML = oTD.innerHTML + "&nbsp;<input type='button' name='addColField1' value='Add Field' onclick='addColField("+ "\"" + "sectionTable"+returnObj.sectionId +"\""+")'>";
    	 oTR = oTable.insertRow(oTable.rows.length);
       oTD = oTR.insertCell(0);
       oTD.innerHTML = "<table id='sectionTable"+returnObj.sectionId+"' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>"
        +"<tr class='tr1'><td onclick='SelectRow(this)'>&nbsp;&nbsp;</td></tr><tr><td onclick='SelectRow(this)'>&nbsp;&nbsp;</td></tr></table>";
    }
      
     //document.all['myadd'].innerHTML = document.all['myadd'].innerHTML +
     // "<tr><td colspan='2'>&nbsp;&nbsp;</td></tr><tr><td colspan='2' align='left'><b>"+returnObj.sectionRemark+":</td></tr>"
     // +"<tr><td colspan='2'><table id='sectionForm' width='100%'  border='1' cellpadding='3' cellspacing='0' bordercolor='#6595D6' style='border-collapse:collapse;'>"
     // +"<tr><td colspan='2'>&nbsp;&nbsp;</td></tr></table>";
  }
  
  function addCol(tableId){
    var oTable = document.all[tableId];
    for(i=0;i<oTable.rows.length;i++){
     	var oTR = oTable.rows[i];
      var oTD = oTR.insertCell(0);
      oTD.onclick = new Function("SelectCol(this)");
      
    }
  }
  
  function addColField(tableId){
    var objTable = document.all[tableId];
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
    var rObj = window.showModalDialog("editFormField.jsp","","dialogWidth:460px;status:no;dialogHeight:400px"); 
    if(rObj==null) return;
    
    var objLabelCell = objTable.rows[0].cells[colId];
    objLabelCell.innerText = rObj.fieldLabel;
    
    var objCell = objTable.rows[rowId].cells[colId];
    if(rObj.controlType=="string"){
      objCell.innerHTML = "<input type='text' name='"+rObj.fieldId+"' isRequried='"+rObj.isRequired+"' length='"+ rObj.length + "'>";
    }else if(rObj.controlType=="date"){
        objCell.innerHTML = "<input type='text' name='"+rObj.fieldId+"' isRequried='"+rObj.isRequired+"' onclick='setday(this)'>(MM/DD/YYYY)";
    }else if(rObj.controlType=="number"){
       objCell.innerHTML = "<input type='text' name='"+rObj.fieldId+"' isRequried='"+rObj.isRequired+"' length='"+ rObj.length + "'>";
    }else if(rObj.controlType=="select"){
      objCell.innerHTML = "<select name='"+rObj.fieldId+"'><option>&nbsp;&nbsp;</option></select>";
    }
   
  }
  
  function createFormClick(){
    var obj = new Object();
    obj.type = "create";
    if(openForm(obj)==false) return;
  }
  
  function editForm(){
    if(document.forms['createForm'].formId.value==""){
      alert("Form have't be created!");
      return;
    }
    var obj = new Object();
    obj.type = "edit";
    obj.formId = document.forms['createForm'].formId.value;
    obj.formName = document.forms['createForm'].formName.value;
    obj.description = document.forms['createForm'].description.value;
    if(openForm(obj)==false) return;
  }
  
  function openForm(obj1){
     var returnObj = window.showModalDialog("editForm.jsp",obj1,"dialogWidth:385px;status:no;dialogHeight:300px"); 
     if(returnObj==null) return false;
     var targetTable = document.all.formSpace;
     targetTable.rows[0].cells[0].innerHTML = "Welcome to <b>" + returnObj.formName+"</b> display";
     document.forms['createForm'].formId.value = returnObj.formId;
     document.forms['createForm'].formName.value = returnObj.formName;
     document.forms['createForm'].description.value = returnObj.description;
     return true;
  }
  
  function addRow(tableId){
     var oTable = document.all[tableId];
     var oTR = oTable.insertRow(oTable.rows.length);
     for(i=0;i<2;i++){
       var oTD = oTR.insertCell(i);
       //oTD.innerHTML  = "<input type='text' name='name'>";
       oTD.innerHTML  = "&nbsp;&nbsp;";
       oTD.ondblclick =new Function("test(this)");
       oTD.onclick = new Function("SelectRow(this)");
     }
  }
  function deleteRow(tableId){
   if(confirm("Are you sure to delete the selected row?")){
       var objTable = document.all[tableId];
       alert(objTable.rows.length)
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
  
  function addField(tableId){
    var objTable = document.all[tableId];
    //alert(tableId)
    //alert(document.all['selectRowId'].value)
    //alert(document.all['selectCellId'].value)
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
    var rObj = window.showModalDialog("editFormField.jsp","","dialogWidth:460px;status:no;dialogHeight:500px"); 
    if(rObj==null) return;
    var objCell = objTable.rows[rowId].cells[colId];
    if(rObj.controlType=="string"){
      objCell.innerHTML = rObj.fieldLabel+":<input type='text' name='"+rObj.fieldId+"' isRequried='"+rObj.isRequired+"' length='"+ rObj.length + "'>";
    }else if(rObj.controlType=="date"){
        objCell.innerHTML = rObj.fieldLabel+":<input type='text' name='"+rObj.fieldId+"' isRequried='"+rObj.isRequired+"' onclick='setday(this)'>(MM/DD/YYYY)";
    }else if(rObj.controlType=="select"){
      objCell.innerHTML = rObj.fieldLabel+":<select name='"+rObj.fieldId+"'><option>&nbsp;&nbsp;</option></select>";
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
 
 document.all['tableId'].value = objTable.id;
 document.all['selectRowId'].value = currRowIndex;
 document.all['selectCellId'].value = objTD.cellIndex;
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
 
 document.all['tableId'].value = objTable.id;
 document.all['selectRowId'].value = currRowIndex;
 document.all['selectCellId'].value = objTD.cellIndex;
//} 
}
 
  function test(obj){
    alert(obj.innerText)
  }
</script>
<body>

<div id="sectionTool">
 <table border='0'>
  <tr>
    <td>
    
     <input type='button' value='Show tool' onclick='showTool()'>
      <input type='button' value='Create Form' onclick='createFormClick()'>
      <input type='button' value='Edit Form Property' onclick='editForm()'>
      <input type='button' value='Create Form Section' onclick='createFormSection()'>
      <input type='button' value='AddField' onclick='addRow()'>
      <input type='button' value='Show' onclick='showTool()'>
    </td>
  </tr>
 </table>
</div>

<div id='creatControlTool' style='display:none'>
<table border='0'>
<tr>
<td>
<input type=radio name=mychoice value=radio>radio
<input type=radio name=mychoice value=checkbox>checkbox
<input type=radio name=mychoice value=text>text
<input type=radio name=mychoice value=button>button
<input type=radio name=mychoice value=label>label
</td>
<td>
<input type=button value=create onclick=create()>
<input type=button value=remove onclick=remove()>
<input type=button value=getinfo onclick=getinfo()>
</td>
<tr>
</table>
</div>
<form name='createForm'>
<div id=myadd>

 <input type="hidden" name="formId">
 <input type="hidden" name="formName">
 <input type="hidden" name="description">
 <table id='formSpace' width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" >
     <tr>
       <td colspan='2' align='center'></td>
     </tr>
</table>

</div>

</form>


<table>
</table>
  <input type='hidden' name='tableId'>
  <input type='hidden' name='selectRowId'>
  <input type='hidden' name='selectCellId'>
  
</body>
</html>