

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
		
		
		if(fsPkgcode!=""&&fsPlancode!="")
		{ 
			nPremium = getXTAPremium(fsPlancode);
		}
		
		var ageField = jQuery(nodeId).closest('tr').find('input[name="field_9_18"]');
		ageField.val(fnage);
		var premiumField = jQuery(nodeId).closest('tr').find('input[name="field_9_16"]');
		premiumField.val(parseFloat(nPremium).toFixed(2));
		
		
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
function getXTAPremium(planKey){
	var table = jQuery('#formTable12');
	var premium = 0;
	var plan = "";
	
	if(planKey=="001"){
		plan = "计划一";
	}
	else if(planKey=="002"){
		plan = "计划二";
	}
	else if(planKey=="003"){
		plan = "计划三";
	}
	else if(planKey=="004"){
		plan = "计划四";
	}
	else if(planKey=="005"){
		plan = "计划五";
	}
	
	for(var i=1; i < 8; i=i+2){
		var tr1 = table.find('tr:eq('+i+')');
		var tr2 = table.find('tr:eq('+(i+1)+')');
		var sumPremium = removeCommas(tr1.find('textarea[title="'+plan+'"]').val()) * tr2.find('textarea[title="'+plan+'"]').val() / 1000;
		premium = premium + sumPremium ;
	}
	for(var i=9; i < 14; i=i+2){
		var tr1 = table.find('tr:eq('+(i+1)+')');
		if (common.isEmpty(tr1.find('textarea[title="'+plan+'"]').val())){
			continue;
		}
		var sumPremium = parseFloat(tr1.find('textarea[title="'+plan+'"]').val());
		premium = premium + sumPremium ;
	}
		
	return premium;
}
/*function getXTAPremium(productKey,planKey){
	var table = jQuery('#formTable4');
	for(var i=0; i<table.find('tr').length; i++){
		var tr = table.find('tr:eq('+i+')');
		if(productKey == tr.find('select[name="Product_id"]').val() && planKey == tr.find('select[name="Plan_id"]').val()){
			var premium1 = removeCommas(tr.find('textarea[name="field_4_8"]').val()) * tr.find('textarea[name="field_4_9"]').val().replace('%','');
			var premium2 = removeCommas(tr.find('textarea[name="field_4_10"]').val()) * tr.find('textarea[name="field_4_11"]').val().replace('%','');
			var premium3 = removeCommas(tr.find('textarea[name="field_4_12"]').val()) * tr.find('textarea[name="field_4_13"]').val().replace('%','');
			var premium4 = removeCommas(tr.find('textarea[name="field_4_14"]').val()) * tr.find('textarea[name="field_4_15"]').val().replace('%','');
			var premium5 = 1*tr.find('textarea[name="field_4_17"]').val();
			var premium6 = 1*tr.find('textarea[name="field_4_19"]').val();
			var premium7 = 1*tr.find('textarea[name="field_4_21"]').val();
			var premium = (premium1+premium2+premium3+premium4)/1000+(premium5+premium6+premium7)/1;
			return premium;
		}
		
	}
}*/
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


