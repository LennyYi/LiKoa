  window.onload   =   historyOncemore;   
  window.onbeforeunload   =   fixHistory;
  function fixHistory() //��ס��ʷ   
  {   
     document.all("theHistoryRecord").value=document.all("thedetailtableDIV").innerHTML.replace(/\n/g,"");   
  }   
    
  function historyOncemore() //�ָ���ʷ   
  {   
    if(document.all("theHistoryRecord").value!="")   
    {
      document.all("thedetailtableDIV").innerHTML=document.all("theHistoryRecord").value;   
    }   
  }  
  
  function addRowByRowIndex(afterRowIndex,sourceRowIndex,tableId) //��������,���ݸ��Ƹ���sourceRowIndex
  {   
    var newRow = document.all("tbDetailPrepare").rows[sourceRowIndex].cloneNode(true);   
    //var desRow = alltbDetailUsed[theFirstSelectedDetail+1];   
    var desRow = document.all(tableId).rows[document.all(tableId).rows.length-1]; 
    desRow.parentElement.insertBefore(newRow,desRow);   
  } 
  
  function addRow(afterRowIndex,tableId) //��������   
  {   
    var newRow = document.all("tbDetailPrepare").rows[0].cloneNode(true);   
    //var desRow = alltbDetailUsed[theFirstSelectedDetail+1];   
    var desRow = document.all(tableId).rows[document.all(tableId).rows.length-1]; 
    desRow.parentElement.insertBefore(newRow,desRow);   
  } 
  
    function addSectionRow(afterRowIndex,tableId,sectionId) //��������   
  {   
    var newRow = document.all("tbDetailPrepare"+sectionId).rows[0].cloneNode(true);   
    //var desRow = alltbDetailUsed[theFirstSelectedDetail+1];   
    var desRow = document.all(tableId).rows[document.all(tableId).rows.length-1]; 
    desRow.parentElement.insertBefore(newRow,desRow);   
  } 
  
  function addSectionRow1(afterRowIndex,tableId,sectionId) //��������   
  {   
    var oldRow = document.all("tbDetailPrepare"+sectionId).rows[0];   
    var newRowTr = document.all("tbDetailPrepare"+sectionId).insertRow();
    var rowIndex = newRowTr.rowIndex;
    for(i=0;i<oldRow.cells.length;i++){
       var td = newRowTr.insertCell();
       var temp = oldRow.cells[i].innerHTML;
       if(temp.indexOf("type=checkbox")>-1 && temp.indexOf("name=chkid")==-1){
         rowIndex = rowIndex - 1;
         var name = temp.substring(temp.indexOf("name="),temp.indexOf(">"));
         temp = temp.replace(new RegExp(name, 'g'),name+"_"+rowIndex)
       }
       td.innerHTML = temp;
    }
  } 
  
  function delRow(tableId,checkBoxId) //ɾ��ָ����   
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
           if(i==1){
             alert(record_can_not_del)
             continue;
           }
           document.all(tableId).deleteRow(i);   
           i=i-1;   
         }   
      }   
  } 
  
  function delAllRow(tableId,checkBoxId) //ɾ��ָ����   
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
      if (typeof(postDeleteRows) == "function") {
          postDeleteRows(tableId, checkBoxId);
      }
      if (typeof(afterDel) == "function") {
          afterDel(tableId,checkBoxId); //Ԥ����ڣ����������ϼ��ֶεĸ��µ�
      }
  } 
  
  
	function delAllRow2(tableId,checkBoxId)
	{
		var alltbDetailUsed = document.all(tableId).rows; 
	     
	    for(var i=0;i<alltbDetailUsed.length;i++)   
	    {   
	       if(alltbDetailUsed[i].all(checkBoxId)!=null && alltbDetailUsed[i].all(checkBoxId).checked==true)   
	       {   
	         document.all(tableId).deleteRow(i);   
	         i=i-1;   
	       }   
	    }
	}
	
	/**
	 * 20150513 Justin Bin Added
	 * */
	function delAllRow3(tableId)
	{
		var alltbDetailUsed = document.all(tableId).rows; 
	     
	    for(var i=1;i<alltbDetailUsed.length;i++)   
	    {   
	         document.all(tableId).deleteRow(i);   
	         i=i-1;   
 
	    }
	}