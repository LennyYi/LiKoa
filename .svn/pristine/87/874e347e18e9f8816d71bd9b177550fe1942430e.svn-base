/*2014-12-22 begin*/
function getSubProducts(fsPkgcode,fsPlancode){
	var selectedTr = jQuery('select[name="field_4_1"] option:selected[value="'+fsPkgcode+'"]').closest('tr').find('select[name="field_4_3"] option:selected[value="'+fsPlancode+'"]').closest('tr');
	//alert(fsPkgcode+fsPlancode);
	//alert(selectedTr.html());
	//alert(selectedTr.find(":checkbox:checked").length);
	var selectSubProd = "('";
	selectedTr.find(":checkbox:checked").each(function(i){
		if(selectSubProd!="('"){
			selectSubProd = selectSubProd + "','" + jQuery(this).val().substring(0,5);		
		}else{
			selectSubProd = selectSubProd + jQuery(this).val().substring(0,5);
		}
	});
	selectSubProd = selectSubProd + "')";
	//countPeople();
	//alert(selectSubProd);
	return selectSubProd;
}

/*2014-12-22 end*/

function calculatePremium(nodeId,isSum){
	
	if(jQuery(nodeId).val()!=""&&jQuery(nodeId).val()!=null)
	{	
		_NOVA.until.changeDateFormat(jQuery(nodeId));
		
		var dateVal = jQuery(nodeId).val().toString();
		
		var fnCurrentRow = jQuery(nodeId).closest('tr').index();
		
		var fdtPolEffDt = jQuery('input[name="field_02_13"]').val().toString();
		
		var fdtDOB = dateVal;
		
		var fnage =	calculateAge(fdtDOB,fdtPolEffDt);
		
		var fsPkgcode = jQuery(nodeId).closest('tr').find('select[name="field_9_20"]').val();	//2014-12-22 hinson
		var fsPlancode = jQuery(nodeId).closest('tr').find('select[name="field_9_21"]').val();	//2014-12-22 hinson

		//2014-12-22 begin
		var fsSubProduct = getSubProducts(fsPkgcode,fsPlancode);
		//2014-12-22 end

		var nPremium = 0;
		
		if(fsPkgcode!=""&&fsPlancode!="")
		{
			var sql = "SELECT SUM(PREMIUM) AS PREMIUM FROM TNovaProductMapping A, TNovaProdPrem B WHERE A.NOVA_PRODCODE = '" + fsPkgcode + "' AND A.NOVA_BENPLNCD = '" + fsPlancode + "' AND B.pkgcode = A.PRODCODE AND B.PRODCODE IN " + fsSubProduct + " AND B.plancode = A.BENPLNCD AND "+ fnage +">=AGEMIN AND "+ fnage +"<=AgeMax GROUP BY B.PKGCODE, B.PLANCODE";
			
			var data = selectFromSQLJson('',sql);
			
			if(data.length>0)
				nPremium = data[0].PREMIUM;
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
		}
		if(statusField.length>0)
			ChangeFont(statusField[0]);
		
		if(isSum)
			calSumFun();
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
	    
		if(parseInt(fnDOBMM+fnDOBDD,10)>parseInt(farrayMM+farrayDD,10))		// modified to parseInt(xx,10),20150312, Justin Bin
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
	jQuery('input[name="field_10_1"]').val(SumPremium);
	
	var RiskClass = jQuery('select[name="field_02_30"]');
	
	if(SumPremium < 200000 &&  RiskClass.length>0)
		RiskClass.combobox("setValue","L");//µÍ·çÏÕ
	else
		RiskClass.combobox("setValue","H");//¸ß·çÏÕ
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
	
	var nMaxAge = 0;
	var nMinAge = 0;
	var CheckingValue = "";
	var sql = "SELECT MIN(param_value) as minAge,MAX(param_value) as maxAge FROM teflow_param_config where param_code in ('MaxAge','MinAge')"	

	var data = selectFromSQLJson('',sql);
	
	if(data.length>0)
	{
		nMaxAge = data[0].MAXAGE;
		nMinAge = data[0].MINAGE;
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
		//20150320
		countPeopleByPlan()
	}else if(tableId=="formTable4" && checkBoxId=="chkid_4"){		//20150402 Justin Bin Added NOVA-137:Refresh the plan
		refreshPlan();
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
	var tableA=jQuery('#formTable4')
	var trList=tableA.find("tr");
	
	for(var i=1;i<trList.length;i++){
		
		var pkgCode=jQuery(trList[i]).find('select[name="field_4_1"]').val();
		var pkgPlan=jQuery(trList[i]).find('select[name="field_4_3"]').val();
		
		var selectedTr=jQuery('select[name="field_9_20"] option:selected[value="'+pkgCode+'"]').closest('tr').find('select[name="field_9_21"] option:selected[value="'+pkgPlan+'"]').closest('tr');
		var count1=selectedTr.length;
		//jQuery('input[name="field_4_9"]').val();
		jQuery(trList[i]).find('textarea[name="field_4_9"]').val(count1);
	}
	//jQuery('input[name="field_10_7"]').val(countMember);
}

