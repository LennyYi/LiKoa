<script language="javascript">
    function saveItem() {
        // Year
        var year = document.getElementById("year");
        if ((year.value = year.value.Trim()) == "") {
            alert("Please input the 'Year'");
            year.focus();
            return;
        }
        var yearNum = parseInt(year.value);
        if ((yearNum < 2000) || (yearNum > 2999)) {
            alert("Please input valid 'Year' (2000 ~ 2999)");
            year.value = "";
            year.focus();
            return;
        }
        // Entitle
        var statutoryEntitle = document.getElementById("statutory_entitle"); 
        if ((statutoryEntitle.value = statutoryEntitle.value.Trim()) == "") {
            alert("Please input the 'Annual Statutory Entitlement Days'");
            statutoryEntitle.focus();
            return;
        }

        var companyEntitle = document.getElementById("company_entitle");
        if ((companyEntitle.value = companyEntitle.value.Trim()) == "") {
            alert("Please input the 'Annual Company Entitlement Days'");
            companyEntitle.focus();
            return;
        }
        
        var entitle = document.getElementById("entitle");
        if ((entitle.value = entitle.value.Trim()) == "") {
            alert("Please input the 'Annual Total Entitled Days'");
            entitle.focus();
            return;
        }

       if (new Number(entitle.value) != new Number(statutoryEntitle.value)+ new Number(companyEntitle.value)){
    	   alert("The 'Annual Total' is not equal to the sum of 'Statutory entitle' and 'Company entitle', please check!");
    	   entitle.focus();
    	   return;
       }
        
        // Balance
        var balance = document.getElementById("balance").value;
        balance = new Number(balance);
        if (balance < 0.0) {
            alert("'Annual Balance' can not be less than 0.0");
            return;
        }
        
        document.forms[0].action = "<%=request.getContextPath()%>/leaveBalanceAction.it?method=saveLeaveBalance";
        document.forms[0].submit();
    }

    function saveCommonInformation(currentStaffCode) {
        // Onboard date
		if(document.forms[0].onboard_date.value.Trim()!=""){			
    		if(isDate(document.forms[0].onboard_date,"Onboard date")==false){
    			document.forms[0].onboard_date.focus();
      			return;
    		}			
		}else{
			alert("Please input the valid Date!");
			document.forms[0].onboard_date.focus();
			return
		}
		
		//Grade Effective Date
		if(document.forms[0].effective_date.value.Trim()!=""){			
    		if(isDate(document.forms[0].effective_date,"Grade Effective Date")==false){
    			document.forms[0].effective_date.focus();
      			return;
    		}			
		}else{
			alert("Please input the valid Date!");
			document.forms[0].effective_date.focus();
			return
		}


        //Previous Working Experience
        var entitle = document.getElementById("pre_working_experience");
        if ((entitle.value = entitle.value.Trim()) == "") {
            alert("Please input the Previous Working Experience");
            entitle.focus();
            return;
        }
        
        //Grade
        var grade = document.getElementById("grade");
        if ((grade.value = grade.value.Trim()) == "") {
            alert("Please input the Grade");
            grade.focus();
            return;
        }

        //Old Grade
        var old_grade = document.getElementById("old_grade");
        if ((old_grade.value = old_grade.value.Trim()) == "") {
        	old_grade.value = "-1";
           // alert("Please input the Previous Grade");
          //  old_grade.focus();
          //  return;
        }

        //medicalException
        var medicalException = document.getElementById("medicalException");
        if ((medicalException.value = medicalException.value.Trim()) == "") {
            alert("Please select whether the staff is medical exception");
            medicalException.focus();
            return;
        }

        //updatedLeave
        var updatedLeave = document.getElementById("updatedLeave");
        if ((updatedLeave.value = updatedLeave.value.Trim()) == "") {
            alert("Please select whether this grade has already updated leave balance");
            updatedLeave.focus();
            return;
        } 

        //updatedMedical
        var updatedMedical = document.getElementById("updatedMedical");
        if ((updatedMedical.value = updatedMedical.value.Trim()) == "") {
            alert("Please select whether this grade has already updated medical balance");
            updatedMedical.focus();
            return;
        }               
        
        document.forms[0].action = "<%=request.getContextPath()%>/leaveBalanceAction.it?method=saveLeaveBalanceCommonInfor&currentStaffCode="+currentStaffCode
        						    +"&updateEntitlement="+document.forms[0].updateEntitlement.checked
        							+"&updateMedicalEntitlement="+document.forms[0].updateMedicalEntitlement.checked;
        document.forms[0].submit();
    }

    function deleteItem() {
        if (checkSelect('itemId') <= 0) {
            alert("Please selecte the record(s) to delete");
            return;
        }
        if (confirm("Are you sure to delete the selected record(s)?")) {
            document.forms[0].action = "<%=request.getContextPath()%>/leaveBalanceAction.it?method=deleteLeaveBalance";
            document.forms[0].submit();
        }
    }

    function deleteGradeItem() {
        if (checkSelect('gradeItemId') <= 0) {
            alert("Please selecte the record(s) to delete");
            return;
        }
        if (confirm("Are you sure to delete the selected record(s)?")) {
            document.forms[0].action = "<%=request.getContextPath()%>/leaveBalanceAction.it?method=deleteGradeRecord";
            document.forms[0].submit();
        }
    }

    function closeForm() {
        window.close();
    }

    function checkKey() {
        if (event.keyCode != 46 && event.keyCode != 45 && (event.keyCode < 48 || event.keyCode > 57)) {
            event.returnValue=false;
        }
    }

    function checkYear(){
    	checkInt();
    	getRowValue();
    }

    function checkInt() {
        if ((event.keyCode < 48 || event.keyCode > 57)) {
            event.returnValue=false;
        }
    }

    function getRowValue(){

    	var inputYear = document.getElementById("year").value;    	
    	if(inputYear.length < 4){
        	return;
    	}
    	
    	var yearList = document.getElementsByName("itemId");   	
    	if(yearList.length<1){
        	return;
        }
       
    	for (tableIndex = 0; tableIndex < yearList.length; tableIndex++) {   
        	 		
    		if(yearList[tableIndex].value ==inputYear){
        		
    			var currentRow = document.all("mytable").rows[tableIndex+1]; 
    			
    			var carryForward_td = document.getElementById("carry_forward");
    			var carryForward = currentRow.cells[2].innerHTML;
    			carryForward = new Number(carryForward.replace("&nbsp;", "")).toFixed(1);
    			carryForward_td.value = carryForward;

    			var statutory_entitle_td = document.getElementById("statutory_entitle");
    			var statutory_entitle = currentRow.cells[3].innerHTML;
    			statutory_entitle = new Number(statutory_entitle.replace("&nbsp;", "")).toFixed(1);
    			statutory_entitle_td.value = statutory_entitle;    

    			var company_entitle_td = document.getElementById("company_entitle");
    			var company_entitle = currentRow.cells[4].innerHTML;
    			company_entitle = new Number(company_entitle.replace("&nbsp;", "")).toFixed(1);
    			company_entitle_td.value = company_entitle; 

    			var entitle_td = document.getElementById("entitle");
    			var entitle = currentRow.cells[5].innerHTML;
    			entitle = new Number(entitle.replace("&nbsp;", "")).toFixed(1);
    			entitle_td.value = entitle;

    			var applied_td = document.getElementById("applied");
    			var applied = currentRow.cells[6].innerHTML;
    			applied = new Number(applied.replace("&nbsp;", "")).toFixed(1);
    			applied_td.value = applied;

    			var forfeit_td = document.getElementById("forfeit");
    			var forfeit = currentRow.cells[7].innerHTML;
    			forfeit = new Number(forfeit.replace("&nbsp;", "")).toFixed(1);
    			forfeit_td.value = forfeit;

    			var balance_td = document.getElementById("balance_td");
    			var balance = currentRow.cells[8].innerHTML;
    			balance = new Number(balance.replace("&nbsp;", "")).toFixed(1);
    			balance_td.innerHTML = balance;

    			var sick_total_entitle_td = document.getElementById("sick_total_entitle");
    			var sick_total_entitle = currentRow.cells[9].innerHTML;
    			sick_total_entitle = new Number(sick_total_entitle.replace("&nbsp;", "")).toFixed(1);
    			sick_total_entitle_td.value = sick_total_entitle;

    			var sick_applied_td = document.getElementById("sick_applied");
    			var sick_applied = currentRow.cells[10].innerHTML;
    			sick_applied = new Number(sick_applied.replace("&nbsp;", "")).toFixed(1);
    			sick_applied_td.value = sick_applied;

    			var sick_balance_td = document.getElementById("sick_balance_td");
    			var sick_balance = currentRow.cells[11].innerHTML;
    			sick_balance = new Number(sick_balance.replace("&nbsp;", "")).toFixed(1);
    			sick_balance_td.innerHTML = sick_balance;

    			return;
    		}
    	}   
    }

    function removeBalance(){
    	var balance_td = document.getElementById("balance_td");
    	balance_td.innerHTML ="";

    	var sick_balance_td = document.getElementById("sick_balance_td");
    	sick_balance_td.innerHTML="";    	
    }

    function validateDays(obj,type) {
        var re = /^[0-9]+\.?[0-9]?$/;

        var days = obj.value;
        if (days != "" && !re.test(days)) {
            obj.value = days.substring(0, days.length - 1);
            return;
        }

        if(type == "annual"){
            getBalance();
        }else if (type == "sick"){
        	getSickBalance();
        }
    }

    function validateMedAmt(obj,entl,apl,bal) {
    	validateDouble(obj);
    	
    	var i=obj.rowIndex;	 

        var entitlement = document.getElementById(entl).value;
        entitlement=isNaN(new Number(entitlement)) ? 0.0 : new Number(entitlement);

        var applied = document.getElementById(apl).value;
        applied=isNaN(new Number(applied)) ? 0.0 : new Number(applied);
        
        var balance = document.getElementById(bal);
        var balance_td = document.getElementById(bal+"_td");

        balance.value = (entitlement - applied).toFixed(2);
        balance_td.innerHTML = balance.value;
    }

    function getBalance() {
        var carryForward = document.getElementById("carry_forward").value;
        carryForward = isNaN(new Number(carryForward)) ? 0.0 : new Number(carryForward);
        
        var statutoryEntitle = document.getElementById("statutory_entitle").value;
        statutoryEntitle = isNaN(new Number(statutoryEntitle)) ? 0.0 : new Number(statutoryEntitle);

        var companyEntitle = document.getElementById("company_entitle").value;
        companyEntitle = isNaN(new Number(companyEntitle)) ? 0.0 : new Number(companyEntitle);
                       
        var entitle = document.getElementById("entitle").value;
        entitle = isNaN(new Number(entitle)) ? 0.0 : new Number(entitle);
        
        var applied = document.getElementById("applied").value;
        applied = isNaN(new Number(applied)) ? 0.0 : new Number(applied);

        var forfeit = document.getElementById("forfeit").value;
        forfeit = isNaN(new Number(forfeit)) ? 0.0 : new Number(forfeit);
        
        var balance = document.getElementById("balance");
        var balance_td = document.getElementById("balance_td");
        balance.value = (entitle + carryForward - applied - forfeit).toFixed(1);
        balance_td.innerHTML = balance.value;
    }

    function getSickBalance() {

        var entitle = document.getElementById("sick_total_entitle").value;
        entitle = isNaN(new Number(entitle)) ? 0.0 : new Number(entitle);

        var applied = document.getElementById("sick_applied").value;
        applied = isNaN(new Number(applied)) ? 0.0 : new Number(applied);

        var balance = document.getElementById("sick_balance");
        var balance_td = document.getElementById("sick_balance_td");

        balance.value = (entitle - applied).toFixed(1);
        balance_td.innerHTML = balance.value;
    }

    function validateDouble(obj) {
        var re = /^[0-9]+\.?[0-9]?[0-9]?$/;
        
        var doubleVal = obj.value;
        if (doubleVal != "" && !re.test(doubleVal)) {
            obj.value = doubleVal.substring(0, doubleVal.length - 1);
            return;
        }
    }

    function manualYearlyJob() {                  
        if(confirm(confirm_manual_year_job)){
            var url ="<%=request.getContextPath()%>/leaveBalanceAction.it?method=manualComingYearBalance";   
            var xmlhttp = createXMLHttpRequest();
            var result;
            if(xmlhttp){
                xmlhttp.open('POST',url,false);
                xmlhttp.onreadystatechange = function()
                {
                  if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                      result = xmlhttp.responseText;
                      alert(result)
                      document.forms[0].action = "<%=request.getContextPath()%>/leaveBalanceAction.it?method=leaveBalanceSetting";  
                      document.forms[0].submit(); 
                  }
                }
                xmlhttp.setRequestHeader("If-Modified-Since","0");
                xmlhttp.send(null);
            }        
          }       
    }

    function manualUpdateStaffEntitlement(obj) {
        if (obj.checked) {
            alert(confirm_manual_update_staff_entitlement);
        }
    }

    function manualUpdateStaffMedicalEntitlement(obj) {
        if (obj.checked) {
            alert(confirm_manual_update_staff_medical_entitlement);
        }
    }

   function  disp2(num, imaChange) {
   	 if (document.getElementById("div"+num).style.display == "none") {
   	   	 
   	  	document.getElementById("div"+num).style.display = "block";
   	 	if(imaChange){
   	    	document.getElementById("imgcon"+num).src = "images/Rminus.png";
   	  	}
   	 } 

   	 else {
   	 	document.getElementById("div"+num).style.display = "none";
	   	if(imaChange){
	   		document.getElementById("imgcon"+num).src = "images/Rplus.png";
	   	}
   	 }
   	}

   function getRowValueMed(){

   	var inputYear = document.getElementById("yearMed").value;    	
   	if(inputYear.length < 4){
       	return;
   	}
   	
   	var yearList = document.getElementsByName("medItemId");   	
   	if(yearList.length<1){
       	return;
       }
      
   	for (tableIndex = 0; tableIndex < yearList.length; tableIndex++) {   
       	 		
   		if(yearList[tableIndex].value ==inputYear){
       		
   			var currentRow = document.all("medtable").rows[tableIndex+1]; 
	
   			var staffCEntitlement_td = document.getElementById("staffCEntitlement");
   			var staffCEntitlement = currentRow.cells[2].innerHTML;
   			staffCEntitlement = new Number(staffCEntitlement.replace("&nbsp;", "")).toFixed(2);
   			staffCEntitlement_td.value = staffCEntitlement;
   			
   			var staffCApplied_td = document.getElementById("staffCApplied");
   			var staffCApplied = currentRow.cells[3].innerHTML;
   			staffCApplied = new Number(staffCApplied.replace("&nbsp;", "")).toFixed(2);
   			staffCApplied_td.value = staffCApplied;

			var staffCBalance_td = document.getElementById("staffCBalance_td");
			var staffCBalance = currentRow.cells[4].innerHTML;
			staffCBalance = new Number(staffCBalance.replace("&nbsp;", "")).toFixed(2);
			staffCBalance_td.innerHTML = staffCBalance;

   			var staffHEntitlement_td = document.getElementById("staffHEntitlement");
   			var staffHEntitlement = currentRow.cells[5].innerHTML;
   			staffHEntitlement = new Number(staffHEntitlement.replace("&nbsp;", "")).toFixed(2);
   			staffHEntitlement_td.value = staffHEntitlement;

   			var staffHApplied_td = document.getElementById("staffHApplied");
   			var staffHApplied = currentRow.cells[6].innerHTML;
   			staffHApplied = new Number(staffHApplied.replace("&nbsp;", "")).toFixed(2);
   			staffHApplied_td.value = staffHApplied;

			var staffHBalance_td = document.getElementById("staffHBalance_td");
			var staffHBalance = currentRow.cells[7].innerHTML;
			staffHBalance = new Number(staffHBalance.replace("&nbsp;", "")).toFixed(2);
			staffHBalance_td.innerHTML = staffHBalance;
			
   			var connubialName_td = document.getElementById("connubialName");
   			var connubialName = new String(currentRow.cells[8].innerHTML.replace("&nbsp;", ""));
   			connubialName_td.value = connubialName;
   			
   			var connubialCEntitlement_td = document.getElementById("connubialCEntitlement");
   			var connubialCEntitlement = currentRow.cells[9].innerHTML;
   			connubialCEntitlement = new Number(connubialCEntitlement.replace("&nbsp;", "")).toFixed(2);
   			connubialCEntitlement_td.value = connubialCEntitlement;

   			var connubialCApplied_td = document.getElementById("connubialCApplied");
   			var connubialCApplied = currentRow.cells[10].innerHTML;
   			connubialCApplied = new Number(connubialCApplied.replace("&nbsp;", "")).toFixed(2);
   			connubialCApplied_td.value = connubialCApplied;

			var connubialCBalance_td = document.getElementById("connubialCBalance_td");
			var connubialCBalance = currentRow.cells[11].innerHTML;
			connubialCBalance = new Number(connubialCBalance.replace("&nbsp;", "")).toFixed(2);
			connubialCBalance_td.innerHTML = connubialCBalance;

   			var connubialHEntitlement_td = document.getElementById("connubialHEntitlement");
   			var connubialHEntitlement = currentRow.cells[12].innerHTML;
   			connubialHEntitlement = new Number(connubialHEntitlement.replace("&nbsp;", "")).toFixed(2);
   			connubialHEntitlement_td.value = connubialHEntitlement;

   			var connubialHApplied_td = document.getElementById("connubialHApplied");
   			var connubialHApplied = currentRow.cells[13].innerHTML;
   			connubialHApplied = new Number(connubialHApplied.replace("&nbsp;", "")).toFixed(2);
   			connubialHApplied_td.value = connubialHApplied;

			var connubialHBalance_td = document.getElementById("connubialHBalance_td");
			var connubialHBalance = currentRow.cells[14].innerHTML;
			connubialHBalance = new Number(connubialHBalance.replace("&nbsp;", "")).toFixed(2);
			connubialHBalance_td.innerHTML = connubialHBalance;

   			var childName_td = document.getElementById("childName");
   			var childName = new String(currentRow.cells[15].innerHTML.replace("&nbsp;", ""));
   			childName_td.value = childName;
   			
   			var childCEntitlement_td = document.getElementById("childCEntitlement");
   			var childCEntitlement = currentRow.cells[16].innerHTML;
   			childCEntitlement = new Number(childCEntitlement.replace("&nbsp;", "")).toFixed(2);
   			childCEntitlement_td.value = childCEntitlement;

   			var childCApplied_td = document.getElementById("childCApplied");
   			var childCApplied = currentRow.cells[17].innerHTML;
   			childCApplied = new Number(childCApplied.replace("&nbsp;", "")).toFixed(2);
   			childCApplied_td.value = childCApplied;

			var childCBalance_td = document.getElementById("childCBalance_td");
			var childCBalance = currentRow.cells[18].innerHTML;
			childCBalance = new Number(childCBalance.replace("&nbsp;", "")).toFixed(2);
			childCBalance_td.innerHTML = childCBalance;

   			var childHEntitlement_td = document.getElementById("childHEntitlement");
   			var childHEntitlement = currentRow.cells[19].innerHTML;
   			childHEntitlement = new Number(childHEntitlement.replace("&nbsp;", "")).toFixed(2);
   			childHEntitlement_td.value = childHEntitlement;

   			var childHApplied_td = document.getElementById("childHApplied");
   			var childHApplied = currentRow.cells[20].innerHTML;
   			childHApplied = new Number(childHApplied.replace("&nbsp;", "")).toFixed(2);
   			childHApplied_td.value = childHApplied;

			var childHBalance_td = document.getElementById("childHBalance_td");
			var childHBalance = currentRow.cells[21].innerHTML;
			childHBalance = new Number(childHBalance.replace("&nbsp;", "")).toFixed(2);
			childHBalance_td.innerHTML = childHBalance;
   			return;
   		}
   	}   
   }	

   function saveMedItem() {
       // Year
       var yearMed = document.getElementById("yearMed");
       if ((yearMed.value = yearMed.value.Trim()) == "") {
           alert("Please input the 'Year'");
           yearMed.focus();
           return;
       }
       var yearNum = parseInt(yearMed.value);
       if ((yearNum < 2000) || (yearNum > 2999)) {
           alert("Please input valid 'Year' (2000 ~ 2999)");
           yearMed.value = "";
           yearMed.focus();
           return;
       }
       // Entitle
       var staffCEntitlement = document.getElementById("staffCEntitlement"); 
       if ((staffCEntitlement.value = staffCEntitlement.value.Trim()) == "") {
           alert("Please input the Entitlement");
           staffCEntitlement.focus();
           return;
       }

       var staffHEntitlement = document.getElementById("staffHEntitlement");
       if ((staffHEntitlement.value = staffHEntitlement.value.Trim()) == "") {
    	   alert("Please input the Entitlement");
           staffHEntitlement.focus();
           return;
       }
       
       var connubialCEntitlement = document.getElementById("connubialCEntitlement");
       if ((connubialCEntitlement.value = connubialCEntitlement.value.Trim()) == "") {
    	   alert("Please input the Entitlement");
           connubialCEntitlement.focus();
           return;
       }

       var connubialHEntitlement = document.getElementById("connubialHEntitlement");
       if ((connubialHEntitlement.value = connubialHEntitlement.value.Trim()) == "") {
    	   alert("Please input the Entitlement");
    	   connubialHEntitlement.focus();
           return;
       }

       var childCEntitlement = document.getElementById("childCEntitlement");
       if ((childCEntitlement.value = childCEntitlement.value.Trim()) == "") {
    	   alert("Please input the Entitlement");
    	   childCEntitlement.focus();
           return;
       }

       var childHEntitlement = document.getElementById("childHEntitlement");
       if ((childHEntitlement.value = childHEntitlement.value.Trim()) == "") {
    	   alert("Please input the Entitlement");
    	   childHEntitlement.focus();
           return;
       }             
   
       document.forms[0].action = "<%=request.getContextPath()%>/medicalBalanceAction.it?method=saveMedicalBalance";
       document.forms[0].submit();
   }

   function deleteMedItem() {
       if (checkSelect('medItemId') <= 0) {
           alert("Please selecte the record(s) to delete");
           return;
       }
       if (confirm("Are you sure to delete the selected record(s)?")) {
           document.forms[0].action = "<%=request.getContextPath()%>/medicalBalanceAction.it?method=deleteMedicalBalance";
           document.forms[0].submit();
       }
   }  

   function manualYearlyJobMed(currentStaffCode) {                  
       if(confirm(confirm_manual_year_job_med)){
           
           var url ="<%=request.getContextPath()%>/medicalBalanceAction.it?method=manualComingYearBalance";   
           var xmlhttp = createXMLHttpRequest();
           var result;
           if(xmlhttp){
               xmlhttp.open('POST',url,false);
               xmlhttp.onreadystatechange = function()
               {
                 if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                     result = xmlhttp.responseText;
                     alert(result);
                     document.forms[0].action = "<%=request.getContextPath()%>/leaveBalanceAction.it?method=leaveBalanceSetting";  
                     document.forms[0].submit(); 
                 }
               }
               xmlhttp.setRequestHeader("If-Modified-Since","0");
               xmlhttp.send(null);
           } 
       }  
   }

</script>