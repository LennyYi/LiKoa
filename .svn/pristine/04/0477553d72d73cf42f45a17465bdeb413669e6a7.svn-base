/*2014-12-22 begin*/
function getSubProducts(fsPkgcode,fsPlancode){
	var selectedTr = jQuery('select[name="field_4_19"] option:selected[value="'+fsPkgcode+'"]').closest('tr').find('select[name="Plan_id"] option:selected[value="'+fsPlancode+'"]').closest('tr');
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
	//alert(selectSubProd);
	return selectSubProd;
}

function refreshPlan(){
	jQuery('select[name="MemPlan_id"]').each(function(){
		//jQuery(this).find("option:first-child").attr("selected",true);
		jQuery(this).combobox("setValue","");
		jQuery(this).closest('tr').find('input[name="field_9_16"]').val(parseFloat(0).toFixed(2));
	});
	
	showPlan();
	summaryPremiumAndCommission();
	//summaryPremium();
}
/*2014-12-22 end*/

function calculatePremium(nodeId,isSum){
	
	if(jQuery(nodeId).val()!=""&&jQuery(nodeId).val()!=null)
	{	
		_NOVA.until.changeDateFormat(jQuery(nodeId));
		
		var dateVal = jQuery(nodeId).val().toString();
		/*if(jQuery(nodeId).val())
			dateVal = jQuery(nodeId).val().toString();
		else
			dateVal = "";*/
		
		var fnCurrentRow = jQuery(nodeId).closest('tr').index();
		
		var fdtPolEffDt = jQuery('input[name="field_02_13"]').val().toString();
		
		var fdtDOB = dateVal;
		
		var fnage =	calculateAge(fdtDOB,fdtPolEffDt);
		
		var fsPkgcode = jQuery(nodeId).closest('tr').find('select[name="MemProduct_id"]').val();	//2014-12-22 hinson
		var fsPlancode = jQuery(nodeId).closest('tr').find('select[name="MemPlan_id"]').val();	//2014-12-22 hinson
		
		var nPremium = 0;
		var ageField = jQuery(nodeId).closest('tr').find('input[name="field_9_18"]');
		ageField.val(fnage);
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
		
		ChangeFont(statusField[0]);
		
		if(isSum)
			calSumFun()
	}
}

//20150508 Justin Bin Added
function getPremium1(pkgCode,plan){
	var table = jQuery('#formTable4');
	for(var i=1; i<table.find('tr').length; i++){
		var tr = table.find('tr:eq('+i+')');
		if(pkgCode == tr.find('select[name="Product_id"]').val() && plan == tr.find('select[name="Plan_id"]').val()){
			var premium1 = removeCommas(tr.find('textarea[name="field_4_5"]').val()) * tr.find('textarea[name="field_4_6"]').val();			//GTL
			var premium2 = removeCommas(tr.find('textarea[name="field_4_7"]').val()) * tr.find('textarea[name="field_4_8"]').val();			//GTLPD
			var premium3 = removeCommas(tr.find('textarea[name="field_4_9"]').val()) * tr.find('textarea[name="field_4_10"]').val();		//GADB
			var premium4 = removeCommas(tr.find('textarea[name="field_4_11"]').val()) * tr.find('textarea[name="field_4_12"]').val();		//GADD
			var premium5 = removeCommas(tr.find('textarea[name="field_4_13"]').val()) * tr.find('textarea[name="field_4_14"]').val();		//GCPA2014
			var premium6 = removeCommas(tr.find('textarea[name="field_4_15"]').val()) * tr.find('textarea[name="field_4_16"]').val();		//GAMR(A3)
			var premium7 = removeCommas(tr.find('textarea[name="field_4_17"]').val()) * tr.find('textarea[name="field_4_18"]').val();		//GADR2014
			var premium8 = removeCommas(tr.find('textarea[name="field_4_19"]').val()) * tr.find('textarea[name="field_4_20"]').val();		//GCIG25
			var premium9 = removeCommas(tr.find('textarea[name="field_4_21"]').val()) * tr.find('textarea[name="field_4_22"]').val();		//GCIG34
			var premium10 = removeCommas(tr.find('textarea[name="field_4_23"]').val()) * tr.find('textarea[name="field_4_24"]').val();		//GAHIR(B)
			var premium11 = removeCommas(tr.find('textarea[name="field_4_26"]').val()) * tr.find('textarea[name="field_4_27"]').val();		//GHIR
			
			var premium = (premium1+premium2+premium3+premium4+premium5+premium6+premium7+premium8+premium9+premium10+premium11)/1000;
			return premium;
		}
		
	}
}

//20150508 Justin Bin Added
/**
 * @author ASNPGYE
 * @Date	20150515
 * @param PackageCode , char
 * @param Plan, plannumber
 * @returns total premium for one people
 * **/
function getPremiumAndCommission(pkgCode,plan){
	var tableProd=jQuery('#formTableProduct');
	var tableDetail = jQuery('#formTableplanDetail');
	var arrProd=new Array();
	var arryReturn=new Array();
	var Premium=0;
	var Commission=0;
	for(var i=1;i<tableProd.find('tr').length;i++){
		var tr = tableProd.find('tr:eq('+i+')');
		var prodCode=tr.find('select[name="field_Product_1"]').val();
		if(prodCode!=null && prodCode!="" && jQuery.inArray(prodCode,arrProd)<0){
			arrProd.push(prodCode);
			//if(plan=="001"){		//plan 1
			var fieldSeq= 2 + parseInt(plan);
			
			var sa=jQuery('#formTableplanDetail').find('select[name="PRODUCT_CODE"] option:selected[value="'+prodCode+'"]').closest('tr')
														.find('select[name="LIMIT_CODE"] option:selected[value="SA"]').closest('tr')
														.find('input[name="field_planDetail_'+fieldSeq+'"]').val();
			
			var rate=jQuery('#formTableplanDetail').find('select[name="PRODUCT_CODE"] option:selected[value="'+prodCode+'"]').closest('tr')
														.find('select[name="LIMIT_CODE"] option:selected[value="RATE"]').closest('tr')
														.find('input[name="field_planDetail_'+fieldSeq+'"]').val();
			
			if(prodCode!="211S5" && prodCode!="311S1" && prodCode!="311S3")
				Premium=parseFloat(Premium)+parseFloat(sa*rate*0.001);
			else
				Premium=parseFloat(Premium)+parseFloat(rate);
				
			var commissionCode=tr.find('select[name="field_Product_2"]').val();
			
			var sqlStr="select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+commissionCode+"'";
			
			var dataCommissionRate=selectFromSQLJson("compnwJNDI",sqlStr);
			
			if(dataCommissionRate!=null && dataCommissionRate.length>0){
				if(prodCode!="211S5" && prodCode!="311S1" && prodCode!="311S3"){
					Commission=parseFloat(Commission)+parseFloat(sa*rate*0.001*dataCommissionRate[0].COMMPERC);
				}else{
					Commission=parseFloat(Commission)+parseFloat(rate*dataCommissionRate[0].COMMPERC);
				}
			}	
		}
	}
	arryReturn.push(Premium);
	arryReturn.push(Commission);
	return arryReturn;
}

// return dataset about commission rate 20150508
function getCommisionRate1(){
	var rateCode1=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="201C1"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode2=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="201C2"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode3=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="201C3"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode4=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="211D1"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode5=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="211D2"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode6=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="211S5"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode7=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="211T1"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode8=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="214S1"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode9=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="214S2"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode10=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="311S1"]').closest('tr').find('select[name="field_Product_2"]').val()
	var rateCode11=jQuery('#formTableProduct').find('select[name="field_Product_1"] option:selected[value="311S3"]').closest('tr').find('select[name="field_Product_2"]').val()
	
	var sqlStr=" select 'RATE1'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode1+"'),0) "+
				" ,'RATE2'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode2+"'),0) "+
				" ,'RATE3'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode3+"'),0) "+
				" ,'RATE4'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode4+"'),0) "+
				" ,'RATE5'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode5+"'),0) "+
				" ,'RATE6'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode6+"'),0) "+
				" ,'RATE7'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode7+"'),0) "+
				" ,'RATE8'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode8+"'),0) "+
				" ,'RATE9'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode9+"'),0) "+
				" ,'RATE10'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode10+"'),0) "+
				" ,'RATE11'=isnull(( select top 1 COMMPERC from TCOMSCALE where COMMCODE='"+rateCode11+"'),0) ";		
	var data = selectFromSQLJson("compnwJNDI",sqlStr);
	
	return data;
}

//20150508	get the commission
function getCommission(pkgCode,Plan,rates){
	var tableProd = jQuery('#formTableProduct');
	var tableDetail=jQuery('#formTableplanDetail')
	var arrProd=new Array();
	var commission=0;
	for(var i=1; i<tableProd.find('tr').length; i++){
		var tr = table.find('tr:eq('+i+')');
		if(pkgCode == tr.find('select[name="Product_id"]').val() && Plan == tr.find('select[name="Plan_id"]').val()){
			var commission1 = removeCommas(tr.find('textarea[name="field_4_5"]').val()) * tr.find('textarea[name="field_4_6"]').val() * rates[0].RATE1;			//GTL
			var commission2 = removeCommas(tr.find('textarea[name="field_4_7"]').val()) * tr.find('textarea[name="field_4_8"]').val() * rates[0].RATE2;			//GTLPD
			var commission3 = removeCommas(tr.find('textarea[name="field_4_9"]').val()) * tr.find('textarea[name="field_4_10"]').val()* rates[0].RATE3;			//GADB
			var commission4 = removeCommas(tr.find('textarea[name="field_4_11"]').val()) * tr.find('textarea[name="field_4_12"]').val()* rates[0].RATE4;		//GADD
			var commission5 = removeCommas(tr.find('textarea[name="field_4_13"]').val()) * tr.find('textarea[name="field_4_14"]').val()* rates[0].RATE5;		//GCPA2014
			var commission6 = removeCommas(tr.find('textarea[name="field_4_15"]').val()) * tr.find('textarea[name="field_4_16"]').val()* rates[0].RATE6;		//GAMR(A3)
			var commission7 = removeCommas(tr.find('textarea[name="field_4_17"]').val()) * tr.find('textarea[name="field_4_18"]').val()* rates[0].RATE7;		//GADR2014
			var commission8 = removeCommas(tr.find('textarea[name="field_4_19"]').val()) * tr.find('textarea[name="field_4_20"]').val()* rates[0].RATE8;		//GCIG25
			var commission9 = removeCommas(tr.find('textarea[name="field_4_21"]').val()) * tr.find('textarea[name="field_4_22"]').val()* rates[0].RATE9;		//GCIG34
			var commission10 = removeCommas(tr.find('textarea[name="field_4_23"]').val()) * tr.find('textarea[name="field_4_24"]').val()* rates[0].RATE10;		//GAHIR(B)
			var commission11 = removeCommas(tr.find('textarea[name="field_4_26"]').val()) * tr.find('textarea[name="field_4_27"]').val()* rates[0].RATE11;		//GHIR
			
			var commission = (commission1+commission2+commission3+commission4+commission5+commission6+commission7+commission8+commission9+commission10+commission11)/1000;
			return commission;
		}
		
	}
}

function calSumFun(){
	/*summaryPremium();
	summaryCommission();*/
	summaryPremiumAndCommission();
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
	    
		if(parseInt(fnDOBMM+fnDOBDD,10)>parseInt(farrayMM+farrayDD,10))	//// modified to parseInt(xx,10),20150312, Justin Bin
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


function summaryPremiumAndCommission(){
	
	var nSumPremium = parseFloat(0).toFixed(2);
	var commission=0;
	
	var memberList=jQuery('#formTable9')
	for(var i=1;i<memberList.find('tr').length;i++){
		var tr = memberList.find('tr:eq('+i+')');
		var pkgCode=tr.find('select[name="MemProduct_id"]').val();
		var planCode=tr.find('select[name="MemPlan_id"]').val();
		
		var PremiumAndCommission=getPremiumAndCommission(pkgCode,planCode);
		
		tr.find('input[name="field_9_16"]').val(parseFloat(PremiumAndCommission[0]).toFixed(2)=="NaN"?parseFloat(0).toFixed(2):parseFloat(PremiumAndCommission[0]).toFixed(2))
		nSumPremium = parseFloat(nSumPremium) + parseFloat(PremiumAndCommission[0]);
		commission = parseFloat(commission) + parseFloat(PremiumAndCommission[1]);
	}
	
	var SumPremium = 0;  
	SumPremium = parseFloat(nSumPremium).toFixed(2);
	jQuery('input[name="field_10_1"]').val(SumPremium);
	if(isNaN(commission))
		commission=parseFloat(0).toFixed(2);
	commission=parseFloat(commission).toFixed(2);
	jQuery('input[name="field_10_6"]').val(commission);
	var RiskClass = jQuery('select[name="field_02_30"]');
		
	if(SumPremium < 200000 &&  RiskClass.length>0)
		RiskClass.combobox("setValue","L");//低风险
	else
		RiskClass.combobox("setValue","H");//高风险
}

function summaryPremium(){
	
	var nSumPremium = parseFloat(0).toFixed(2);
	
	var memberList=jQuery('#formTable9')
	for(var i=1;i<memberList.find('tr').length;i++){
		var tr = memberList.find('tr:eq('+i+')');
		var pkgCode=tr.find('select[name="MemProduct_id"]').val();
		var planCode=tr.find('select[name="MemPlan_id"]').val();
		nSumPremium = parseFloat(nSumPremium) + parseFloat(getPremium(pkgCode,planCode));
	}
	
	var SumPremium = 0;  
	SumPremium = parseFloat(nSumPremium).toFixed(2);
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
	var commission=0;
	var SumPremium = jQuery('input[name="field_10_1"]').val();
	

	var data = getCommisionRate();
	if (data.length>0)
		var memberList=jQuery('#formTable9')
		for(var i=1;i<memberList.find('tr').length;i++){
			var tr = memberList.find('tr:eq('+i+')');
			var pkgCode=tr.find('select[name="MemProduct_id"]').val();
			var planCode=tr.find('select[name="MemPlan_id"]').val();
			commission = parseFloat(commission) + parseFloat(getCommission(pkgCode,planCode,data));
		}
	if(isNaN(commission))
		commission=parseFloat(0).toFixed(2);
	jQuery('input[name="field_10_6"]').val(parseFloat(commission).toFixed(2));
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
		/*summaryPremium();
		summaryCommission();*/
		countPeople();	//20150306 Justin Bin Added for count the enrolled member;
		countPeopleByPlan();	// 20150323 Justin Bin Added
		

	}else if(tableId=="formTable4" && checkBoxId=="chkid_4"){		//20150402 Justin Bin Added Changes for NOVA-137:Refresh the plan
		refreshPlan();
	}else if(tableId="formTableProduct"){		//20150508
		//showProduct(tableId);
		showProductPlan();
	}
	calSumFun();		//20150515 Justin Bin Added
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
		
		var pkgCode=jQuery(trList[i]).find('select[name="Product_id"]').val();
		var pkgPlan=jQuery(trList[i]).find('select[name="Plan_id"]').val();
		
		var selectedTr=jQuery('select[name="MemProduct_id"] option:selected[value="'+pkgCode+'"]').closest('tr').find('select[name="MemPlan_id"] option:selected[value="'+pkgPlan+'"]').closest('tr');
		var count1=selectedTr.length;
		//jQuery('input[name="field_4_9"]').val();
		jQuery(trList[i]).find('textarea[name="field_4_3"]').val(count1);
	}
	//jQuery('input[name="field_10_7"]').val(countMember);
}

