//NOVA-214
function getEachProuctShipOcc(nodeId){
	//this paragraph is to get the product setting of  ship & occupation class by the plan selection.

	// get the member plan value in section '人员信息'
	var fsPlancode = jQuery(nodeId).closest('tr').find('select[name="MemPlan_id"]').val();

	// get the table in section '计划选择'
	var tableA=jQuery('#formTable4')
	var trList=tableA.find("tr");	
	
	//start from index 1
	for(var i = 1; i<trList.length; i++){

		var pkgCode=jQuery(trList[i]).find('select[name="Product_id"]').val();
		var pkgPlan=jQuery(trList[i]).find('select[name="Plan_id"]').val();
		
		if (fsPlancode.toString() == pkgPlan.toString()){
			//SELECT: field_4_14--职业等级, SELECT: field_4_15--是否拥有当地医保 		
			var o1 = jQuery(trList[i]).find('select[name="field_4_14"]').val();
			var o2 = jQuery(trList[i]).find('select[name="field_4_15"]').val();
			
			return [true, o1, o2];
		}
	}		
	return [false, '', ''];	
}

function getEachProductAmount(nodeId){
	//this paragraph is to get the product benefit amount by the plan selection.
	
	// get the member plan value in section '人员信息'
	var fsPlancode = jQuery(nodeId).closest('tr').find('select[name="MemPlan_id"]').val();
	
	// get the table in section '产品计划组合详情'
	var tblData=jQuery('#formTable12')
	var trList =tblData.find("tr");

    var clmList = "";

	switch(fsPlancode) {
	    case '001': 
	    {
	    	clmList = "field_12_1";
	        break;
	    }
	    case '002': 
	    {
	    	clmList = "field_12_2";
	        break;
	    }
	    case '003': 
	    {
	    	clmList = "field_12_3";
	        break;
	    }
	    case '004': 
	    {
	    	clmList = "field_12_4";
	        break;
	    }
	    case '005': 
	    {
	    	clmList = "field_12_5";
	        break;
	    }
	    default:
	    {
	    	clmList = "field_12_1";			        	 
	        break;
	    }
	}	
	
	for(var i = 1; i<trList.length; i++){			 
		
		if (i == 1){
			var o1 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
			if (isNaN(o1) || o1 == ""){
				o1 = "0";
			}
		}
		
		if (i == 2){
			var o2 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
			if (isNaN(o2) || o2 == ""){
				o2 = "0";
			}		
		}		
		
		if (i == 3){
			var o3 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
			if (isNaN(o3) || o3 == ""){
				o3 = "0";
			}			
		}
		
		if (i == 4){
			var o4 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
			if (isNaN(o4) || o4 == ""){
				o4 = "0";
			}			
		}
		
		if (i == 5){
			var o5 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
			if (isNaN(o5) || o5 == ""){
				o5 = "0";
			}			
		}
		
		if (i == 6){
			var o6 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
			if (isNaN(o6) || o6 == ""){
				o6 = "0";
			}		
		}
		
		if (i == 7){
			var o7_1 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
		}
		if (i == 8){
			var o7_2 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
		}
		if (i == 9){
			var o7_3 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
		}
		if (i == 10){
			var o7_4 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
		}					
		
		if (i == 11){
			var o7 = "0";
			if((parseInt(o7_1)/30 == parseInt(o7_2)/1500) && (parseInt(o7_2)/1500 == parseInt(o7_3)/1000)  && (parseInt(o7_3)/1000 == parseInt(o7_4)/1000) ){
				o7 = parseInt(o7_1)/30; 
			}			
			var o8 = jQuery(trList[i]).find("textarea[name='" +clmList + "']").val();
		
			if (isNaN(o8) || o8 == ""){
				o8 = "0";
			}
		}			
		//alert ("i: " + i + ", trList.length: " + trList.length + ", o1: " + o1 + " , o2: " + o2 + " , o3: " + o3 );
	}
	//alert ("i: " + i + ", trList.length: " + trList.length + ", o1: " + o1 + " , o2: " + o2 + " , o3: " + o3 );
	return [true, o1, o2, o3, o4, o5, o6, o7, o8];
}

function getEachProductAmount_00(nodeId){
	
	var tableA=jQuery('#formTable4')
	var trList=tableA.find("tr");

	var fsPlancode = jQuery(nodeId).closest('tr').find('select[name="MemPlan_id"]').val();
	//fsPlancode = '001' as tnovaproductmapping
	
	//start from index 1
	for(var i = 1; i<trList.length; i++){
		
		var pkgCode=jQuery(trList[i]).find('select[name="Product_id"]').val();
		var pkgPlan=jQuery(trList[i]).find('select[name="Plan_id"]').val();
		
		//alert("i: " + i + ", pkgCode: " + pkgCode + ", pkgPlan: " + pkgPlan + ", fsPlancode" + fsPlancode)
		
		if (fsPlancode.toString() == pkgPlan.toString()){

			var o1 = jQuery(trList[i]).find('input[name="field_4_4"]').val(); 
			var o2 = jQuery(trList[i]).find('input[name="field_4_5"]').val();
			var o3 = jQuery(trList[i]).find('input[name="field_4_6"]').val();
			var o4 = jQuery(trList[i]).find('input[name="field_4_16"]').val();			
			var o5 = jQuery(trList[i]).find('input[name="field_4_7"]').val();
			var o6 = jQuery(trList[i]).find('input[name="field_4_8"]').val();

			var o7 = 0;
			var o7_1 = jQuery(trList[i]).find('input[name="field_4_9"]').val();			
			var o7_2 = jQuery(trList[i]).find('input[name="field_4_10"]').val();
			var o7_3 = jQuery(trList[i]).find('input[name="field_4_11"]').val();
			var o7_4 = jQuery(trList[i]).find('input[name="field_4_12"]').val();
			if((parseInt(o7_1)/30 == parseInt(o7_2)/1500) && (parseInt(o7_2)/1500 == parseInt(o7_3)/1000)  && (parseInt(o7_3)/1000 == parseInt(o7_4)/1000) ){
				o7 = parseInt(o7_1)/30; 
			}										
			var o8 = 0;
			o8 = parseInt(jQuery(trList[i]).find('input[name="field_4_13"]').val())/1000;
			if (isNaN(o8)){
				o8 = 0;
			}
			
			var o9 = jQuery(trList[i]).find('select[name="field_4_14"]').val();
			var o10 = jQuery(trList[i]).find('select[name="field_4_15"]').val();
			         
			//alert("matched: " + i + ", o1: " + o1+ ", o2: " + o2+ ", o3: " + o3+ ", o4: " + o4+ ", o5: " + o5+ ", o6: " + o6+ ", o7: " + o7+ ", o8: " + o8+ ", o9: " + o9+ ", o10: " + o10);
			if (isNaN(o1) || o1 == ""){
				o1 = "0";
			}
			if (isNaN(o2) || o2 == ""){
				o2 = "0";
			}		
			if (isNaN(o3) || o3 == ""){
				o3 = "0";
			}			
			if (isNaN(o4) || o4 == ""){
				o4 = "0";
			}			
			if (isNaN(o5) || o5 == ""){
				o5 = "0";
			}			
			if (isNaN(o6) || o6 == ""){
				o6 = "0";
			}		
			if (isNaN(o7) || o7 == ""){
				o7 = "0";
			}		
			if (isNaN(o8) || o8 == ""){
				o8 = "0";
			}			
			
			return [true, o1, o2, o3, o4, o5, o6, o7, o8, o9, o10];
		}		
	}
	return [false, 0, 0, 0, 0, 0, 0, 0, 0, '', ''];
}

//NOVA-214
function calculatePremiumJQF(nodeId,isSum){

	if(jQuery(nodeId).val() == "" || jQuery(nodeId).val()==null)
	{
		return; 
	}
	
	var systemFormID = jQuery('input[name="formSystemId"]').val();
	if (systemFormID != '30322'){
		return;				
	} 
	
	var bMatch = false;
	var iAmt201C1 = 0;
	var iAmt211D1 = 0;
	var iAmt211S5 = 0;	
	var iAmt211D2 = 0;
	var iAmt311S3 = 0;
	var iAmt214S1 = 0;
	var iCnt301C2 = 0;
	var iCnt301S1 = 0;	
	var sOccClass = "";
	var sShip = "";
	

	var aResultA = getEachProductAmount(nodeId);
	bMatchA   = aResultA[0];
	iAmt201C1 = parseInt(aResultA[1]);
	iAmt211D1 = parseInt(aResultA[2]);
	iAmt211S5 = parseInt(aResultA[3]);
	iAmt211D2 = parseInt(aResultA[4]);
	iAmt311S3 = parseInt(aResultA[5]);
	iAmt214S1 = parseInt(aResultA[6]);
	iCnt301C2 = parseInt(aResultA[7]);
	iCnt301S1 = parseInt(aResultA[8]);
	
	var aResultB = getEachProuctShipOcc(nodeId);
	bMatchB   = aResultB[0];
	sOccClass = aResultB[1];
	sShip     = aResultB[2];

	//alert("1: " + bMatch + ", 2: " + iAmt201C1  + ", 3: " + iAmt211D1+ ", 4: " + iAmt211D2+ ", 5: " + iAmt211S5+ ", 6: " + iAmt311S3 + ", 7: " + iAmt214S1 + ", 8: " + iCnt301C2 + ", 9: " + iCnt301S1 + ", 10: " + sOccClass + ", 11" + sShip  );
	if (!bMatchA || !bMatchB){
		return;
	}
	
//	var fnage =	calculateAge(fdtDOB,fdtPolEffDt);
//	var ageField = jQuery(nodeId).closest('tr').find('input[name="field_9_18"]');
//	ageField.val(fnage);	
	
	var iMemAge = 0;
	var sMemOccClass = "";
	var sMemShip = "";
	 
	iMemAge = parseInt(jQuery(nodeId).closest('tr').find('input[name="field_9_18"]').val());
	sMemOccClass = jQuery(nodeId).closest('tr').find('select[name="field_9_15"]').val();
	sMemShip = jQuery(nodeId).closest('tr').find('select[name="field_9_13"]').val();
	
	//use OccClass & Ship in Product Plan as calculation base.	
	sMemOccClass = sOccClass;
	sMemShip     = sShip;
	
	//Skip if member age is blank
	if (isNaN(iMemAge)){
		return;
	}
	
	var fPremTotal = 0;

	var sql = "select Prodcode as PRODCODE, PlanCode as PLANCODE, UNTPVALUE, PRMRTMAL FROM TNovaProdPrem where pkgcode = 'JQFSH'";
	sql += " and ProdCode = '201C1'";
	sql += " and AgeMin  <= " + iMemAge;
	sql += " and AgeMax  >= " + iMemAge;
	sql += " and OccClass = '"+ sMemOccClass + "'";
	sql += " union all";
	sql += " select Prodcode as PRODCODE, PlanCode as PLANCODE, UNTPVALUE, PRMRTMAL FROM TNovaProdPrem where pkgcode = 'JQFSH'";
	sql += " and ProdCode = '211D1'";
	sql += " and AgeMin  <= " + iMemAge;
	sql += " and AgeMax  >= " + iMemAge;
	sql += " and OccClass = '"+ sMemOccClass + "'";
	sql += " union all";
	sql += " select Prodcode as PRODCODE, PlanCode as PLANCODE, UNTPVALUE, PRMRTMAL FROM TNovaProdPrem where pkgcode = 'JQFSH'";
	sql += " and ProdCode = '211D2'";
	sql += " and AgeMin  <= " + iMemAge;
	sql += " and AgeMax  >= " + iMemAge;
	sql += " and OccClass = '"+ sMemOccClass + "'";
	sql += " union all";
	sql += " select Prodcode as PRODCODE, PlanCode as PLANCODE, UNTPVALUE, PRMRTMAL FROM TNovaProdPrem where pkgcode = 'JQFSH'";
	sql += " and ProdCode = '211S5'";
	sql += " and AgeMin  <= " + iMemAge;
	sql += " and AgeMax  >= " + iMemAge;
	sql += " and OccClass = '"+ sMemOccClass + "'";
	sql += " union all";
	sql += " select Prodcode as PRODCODE, PlanCode as PLANCODE, UNTPVALUE, PRMRTMAL FROM TNovaProdPrem where pkgcode = 'JQFSH'";
	sql += " and ProdCode = '311S3'";
	sql += " and AgeMin  <= " + iMemAge;
	sql += " and AgeMax  >= " + iMemAge;
	sql += " and OccClass = '"+ sMemOccClass + "'";
	sql += " union all";
	sql += " select Prodcode as PRODCODE, PlanCode as PLANCODE, UNTPVALUE, PRMRTMAL FROM TNovaProdPrem where pkgcode = 'JQFSH'";
	sql += " and ProdCode = '214S1'";
	sql += " and AgeMin  <= " + iMemAge;
	sql += " and AgeMax  >= " + iMemAge;
	sql += " and OccClass = '"+ sMemOccClass + "'";
	sql += " union all";
	sql += " select Prodcode as PRODCODE, PlanCode as PLANCODE, UNTPVALUE, PRMRTMAL FROM TNovaProdPrem where pkgcode = 'JQFSH'";
	sql += " and ProdCode = '301C2'";
	sql += " and AgeMin <= " + iMemAge;
	sql += " and AgeMax >= " + iMemAge;
	sql += " and Ship   = '" + sMemShip + "'";
	sql += " union all";
	sql += " select Prodcode as PRODCODE, PlanCode as PLANCODE, UNTPVALUE, PRMRTMAL FROM TNovaProdPrem where pkgcode = 'JQFSH'";
	sql += " and ProdCode = '301S1'";
	sql += " and AgeMin <= " + iMemAge;
	sql += " and AgeMax >= " + iMemAge;
	sql += " and Ship   = '" + sMemShip + "'";				


	var data = selectFromSQLJson('',sql);

	if(data.length>0){
		fPremTotal = 0;
		for (var i = 0; i < data.length; i++) {
  
			if (data[i].PRODCODE == "201C1"){
				fPremTotal = fPremTotal + iAmt201C1 / parseInt(data[i].UNTPVALUE) * parseFloat(data[i].PRMRTMAL)  
			}

			if (data[i].PRODCODE == "211D1"){
				fPremTotal = fPremTotal + iAmt211D1 / parseInt(data[i].UNTPVALUE) * parseFloat(data[i].PRMRTMAL)  
			}			

			if (data[i].PRODCODE == "211D2"){
				fPremTotal = fPremTotal + iAmt211D2 / parseInt(data[i].UNTPVALUE) * parseFloat(data[i].PRMRTMAL)  
			}

			if (data[i].PRODCODE == "211S5"){
								
				if (iAmt211S5 >= parseInt(data[i].UNTPVALUE)){					
					//calculation for first premium about AMR(A3)
					if( data[i].PLANCODE == "000"){					
						fPremTotal = fPremTotal + parseFloat(data[i].PRMRTMAL);						
						iAmt211S5 = iAmt211S5 - parseInt(data[i].UNTPVALUE);
					}
				
					//calculation for remaining premium about AMR(A3)
					if( data[i].PLANCODE == "001"){
						fPremTotal = fPremTotal + iAmt211S5 / parseInt(data[i].UNTPVALUE) * parseFloat(data[i].PRMRTMAL)						
					}
				}				
			}

			if (data[i].PRODCODE == "311S3"){
				fPremTotal = fPremTotal + iAmt311S3 / parseInt(data[i].UNTPVALUE) * parseFloat(data[i].PRMRTMAL)  
			}
		
			if (data[i].PRODCODE == "214S1"){
				fPremTotal = fPremTotal + iAmt214S1 / parseInt(data[i].UNTPVALUE) * parseFloat(data[i].PRMRTMAL)  
			}
		
			if (data[i].PRODCODE == "301C2"){
				fPremTotal = fPremTotal + iCnt301C2 / parseInt(data[i].UNTPVALUE) * parseFloat(data[i].PRMRTMAL)  
			}
		
			if (data[i].PRODCODE == "301S1"){
				fPremTotal = fPremTotal + iCnt301S1 / parseInt(data[i].UNTPVALUE) * parseFloat(data[i].PRMRTMAL)  
			}												
		}
	}

	var premiumField = jQuery(nodeId).closest('tr').find('input[name="field_9_16"]');
	premiumField.val(parseFloat(fPremTotal).toFixed(2));

}



function calculatePremium(nodeId,isSum){
	
	if(jQuery(nodeId).val()!=""&&jQuery(nodeId).val()!=null)
	{	
		_NOVA.until.changeDateFormat(jQuery(nodeId));
			
		var dateVal = jQuery(nodeId).val().toString();
		
		var systemFormID = jQuery('input[name="formSystemId"]').val();					
		
		var fnCurrentRow = jQuery(nodeId).closest('tr').index();

		var fdtPolEffDt = jQuery('input[name="field_02_13"]').val().toString();
				
		var fdtDOB = dateVal;
		
		var fnage =	calculateAge(fdtDOB,fdtPolEffDt);
		
		var fsPkgcode = jQuery(nodeId).closest('tr').find('select[name="MemProduct_id"]').val();
		var fsPlancode = jQuery(nodeId).closest('tr').find('select[name="MemPlan_id"]').val();
		
		var nPremium = 0;
				
		var ageField = jQuery(nodeId).closest('tr').find('input[name="field_9_18"]');
		ageField.val(fnage);
		
		//NOVA-214, 30322 for JQFSH		
		if (systemFormID == '30322'){
			
			calculatePremiumJQF(nodeId,isSum);
			
		}else{		
		
			if(fsPkgcode!=""&&fsPlancode!="")
			{
				var sql = "SELECT SUM(PREMIUM) AS PREMIUM FROM TNovaProductMapping A, TNovaProdPrem B WHERE A.NOVA_PRODCODE = '" + fsPkgcode + "' AND A.NOVA_BENPLNCD = '" + fsPlancode + "' AND B.pkgcode = A.PRODCODE AND B.plancode = A.BENPLNCD AND "+ fnage +">=AGEMIN AND "+ fnage +"<=AgeMax and A.form_system_id = "+systemFormID+" GROUP BY B.PKGCODE, B.PLANCODE";
		
				var data = selectFromSQLJson('',sql);
				
				if(data.length>0)
					nPremium = data[0].PREMIUM;
			}
			
			//var ageField = jQuery(nodeId).closest('tr').find('input[name="field_9_18"]');
			//ageField.val(fnage);
			var premiumField = jQuery(nodeId).closest('tr').find('input[name="field_9_16"]');
			premiumField.val(parseFloat(nPremium).toFixed(2));

		}
		
		var soverage = "";
		soverage = OverAgeChecking(fnage);	
		
		var statusField = jQuery(nodeId).closest('tr').find('textarea[name="field_9_19"]');
		
		if(statusField.length>0){
			var smemstatus = statusField.val();
			
			if (soverage=="y"){
				if (smemstatus.indexOf("y",smemstatus)<0)
					statusField.val(smemstatus+soverage);
			}else{
				statusField.val(smemstatus.replace("y",soverage));
			}
			
			ChangeFont(statusField[0]);
		}
		
		if(isSum)
			calSumFun()
	}
}

function calSumFun(){
	summaryPremium();
	summaryCommission();
	setBillDate();
}

// Calculate age (Age calc. basic: Last DOB; Age Reference: policy anniversary)
// Date format should be MM/DD/YYYY
function calculateAge(fsDOB,fsAnnvDate){
	var fnAge = 0;	

	if(fsDOB!=""&&fsAnnvDate!=""){
		
		//******************
		//Brian Qiu  for diferent date format
		var dateStr;
		if(dateFormat=='yyyyMMdd')
			dateStr = ["yyyy","MM","dd"];
		else
			dateStr = dateFormat.replace(/-/, "/").split("/");
		
		var fsAnnvDateStr;
		
		if(dateFormat == "yyyyMMdd"){	
			fsAnnvDateStr = fsAnnvDate.match(/(\d{4})(\d{2})(\d{2})/);
	    	fsAnnvDateStr = [fsAnnvDateStr[1],fsAnnvDateStr[2],fsAnnvDateStr[3]];
	    }
		else
			fsAnnvDateStr = fsAnnvDate.replace(/-/, "/").split("/")
		
		var fsDOBStr;
		
		if(dateFormat == "yyyyMMdd"){	
			fsDOBStr = fsDOB.match(/(\d{4})(\d{2})(\d{2})/);
			fsDOBStr = [fsDOBStr[1],fsDOBStr[2],fsDOBStr[3]];
	    }
		else
			fsDOBStr = fsDOB.replace(/-/, "/").split("/");
		
		var l = dateStr.length;
		

		var fnAnnvYear = '';
		var farrayDD = '';
		var farrayMM = '';
		
		var fnDOBYear = '';
		var fnDOBDD = '';
		var fnDOBMM = '';
		
	    for(var i=0;i<l;i++){
			if(dateStr[i]=="yyyy"){
				fnAnnvYear = fsAnnvDateStr[i];
				fnDOBYear = fsDOBStr[i];
			}
			if(dateStr[i]=="MM"){
				farrayMM = fsAnnvDateStr[i];
				fnDOBMM = fsDOBStr[i];
			}
			if(dateStr[i]=="dd"){
				farrayDD = fsAnnvDateStr[i];
				fnDOBDD = fsDOBStr[i];
			}
		}
	    
		if(parseInt(fnDOBMM+fnDOBDD,10)>parseInt(farrayMM+farrayDD,10))		//when the data start with 0, will cause error, 20150312 Justin Bin modified
			fnAge = fnAnnvYear - fnDOBYear - 1;
		else
			fnAge = fnAnnvYear - fnDOBYear;
	}
	else
		fnAge = 0;
	
	if(isNaN(fnAge))
		fnAge = 0;
	
	return fnAge;
}

function summaryPremium(){
	
	var AryChkPlancode =  jQuery('input[name="field_9_16"]');
	
	var nSumPremium = parseFloat(0).toFixed(2);
	
	if(AryChkPlancode.length>0){
		AryChkPlancode.each(function(){
			if (jQuery(this).val()!=""){
				nSumPremium = parseFloat(nSumPremium) + parseFloat(jQuery(this).val());
			}
		});
	}
	
	//jQuery('input[name="field_10_1"]').val(parseFloat(nSumPremium).toFixed(2));
	var SumPremium = 0;  
	SumPremium = parseFloat(nSumPremium).toFixed(2);
	
	if(isNaN(SumPremium))
		SumPremium = parseFloat(0).toFixed(2);
	
	jQuery('input[name="field_10_1"]').val(SumPremium);
	
	var RiskClass = jQuery('select[name="field_02_30"]');
	
	if(SumPremium < 200000 &&  RiskClass.length>0)
		RiskClass.combobox("setValue","L");//低风险
	else
		RiskClass.combobox("setValue","H");//高风险
}

///20150212 Justin Bin Added , to add commission calculattion;
function summaryCommission(){
	///20150305
	var commissionObj = jQuery('input[type="checkbox"][name="field_6_1"]:checked')
	if(commissionObj.length>0){
		if(commissionObj.val()=="D"){
			jQuery('input[name="field_10_6"]').val("0");
			return;
		}
	}
	var commissionRate=0;
	var commissionCode=jQuery('select[name="field_6_6"]').val();
	var commission=0;
	var SumPremium = jQuery('input[name="field_10_1"]').val();
	
	
	var sql="select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+commissionCode+"' order by YRFR  desc"
	var data = selectFromSQLJson("compnwJNDI",sql);
	if (data.length>0)
		commissionRate=data[0].COMMPERC
		commission=parseFloat(commissionRate*SumPremium).toFixed(2);
	if(isNaN(commission))
		commission=parseFloat(0).toFixed(2);
	jQuery('input[name="field_10_6"]').val(commission);
}

function setBillDate(){
	var startDate = jQuery("input[name='field_02_13']").val();
	var requestDate = jQuery("input[name='request_date']").val();
	var billMode =jQuery("input[name='field_2_1']:checked").val();

	requestDate = requestDate.split(' ')[0];
	var d = new Date(formatDate1(startDate,dateFormat,"MM/dd/yyyy"));
	var d2 = new Date(formatDate1(requestDate,dateFormat,"MM/dd/yyyy"));
	
	if(d!='NaN')
	{
		startDate = d.getFullYear() + '/' + (""+(d.getMonth()+101)).substr(1) + '/' + (""+(d.getDate()+100)).substr(1) ;
		
		startDate = formatDate1(startDate,"yyyy/MM/dd",dateFormat);
		
		switch(billMode)
		{
			case 'A':
				d.setFullYear(d.getFullYear()+1);
				break;
			case 'M':
				d.setMonth(d.getMonth()+1); 
				break;
			case 'Q':
				d.setMonth(d.getMonth()+3); 
				break;
			case 'S':
				d.setMonth(d.getMonth()+6);  
				break;
		}
		
		d.setDate(d.getDate()-1);
		
		endDate = d.getFullYear() + '/' + (""+(d.getMonth()+101)).substr(1) + '/' + (""+(d.getDate()+100)).substr(1) ;
	}
	else
		endDate = startDate;
	
	endDate = formatDate1(endDate,"yyyy/MM/dd",dateFormat);
	
	requestDate = d2.getFullYear() + '/' + (""+(d2.getMonth()+101)).substr(1) + '/' + (""+(d2.getDate()+100)).substr(1);
	
	requestDate = formatDate1(requestDate,"yyyy/MM/dd",dateFormat);
	
	jQuery("textarea[name='field_10_2']").val(startDate);
	jQuery("textarea[name='field_10_3']").val(requestDate);
	jQuery("textarea[name='field_10_4']").val(endDate);
}

function OverAgeChecking(fnage){
	var CheckingValue = "";
	
	if(nMaxAge==0||nMaxAge==0)
	{
		var sql = "SELECT MIN(param_value) as minAge,MAX(param_value) as maxAge FROM teflow_param_config where param_code in ('MaxAge','MinAge')"	
	
		var data = selectFromSQLJson('',sql);
		
		if(data.length>0)
		{
			nMaxAge = data[0].MAXAGE;
			nMinAge = data[0].MINAGE;
		}
	}
	
	if((fnage > nMaxAge) || (fnage < nMinAge))
		CheckingValue="y";
	
	return CheckingValue;	
	
}


//20150305 Justin Bin  Added for recalculate the premium
function afterDel(tableId,checkBoxId){
	if(tableId=="formTable9" && checkBoxId=="chkid_9"){
		summaryPremium();
		summaryCommission();
		countPeople();	//20150306
		countPeopleByPlan();	//20150323
	}
}

//20150306 Justin Bin Added for count the enroll member
function countPeople(){
	var countMember=jQuery('input[name="field_9_16"]').length;
	
	jQuery('input[name="field_10_7"]').val(countMember);
}

//20150320 Justin Bin Added for count the enroll member
function countPeopleByPlan(){
	//var countMember=jQuery('input[name="field_9_16"]').length;
	var tableA=jQuery('#formTable4');
	var trList=tableA.find("tr");
	
	for(var i=1;i<trList.length;i++){
		
		var pkgCode=jQuery(trList[i]).find('select[name="Product_id"]').val();
		var pkgPlan=jQuery(trList[i]).find('select[name="Plan_id"]').val();
		
		var selectedTr=jQuery('select[name="MemProduct_id"] option:selected[value="'+pkgCode+'"]').closest('tr').find('select[name="MemPlan_id"] option:selected[value="'+pkgPlan+'"]').closest('tr');
		var count1=selectedTr.length;
		//jQuery('input[name="field_4_9"]').val();
		jQuery(trList[i]).find('textarea[name="field_4_3"]').val(count1);
	}
	//jQuery('input[name="field_10_7"]').val(countMember);
}



//NOVA-214, copied from NBForm_LoadData_BJXTA
//function cleanMenPaln(){
//	var table = jQuery('#formTable9');
//	for(var i=0; i<table.find('tr').length; i++){
//		var tr = table.find('tr:eq('+i+')');
//		tr.find('select[name="MemPlan_id"]').val('').trigger('change');
//	}
//}

