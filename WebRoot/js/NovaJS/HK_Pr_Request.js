
var requestNo = jQuery('input[name="request_no"]').val();
var requestUrl = jQuery('input[name="requestUrl"]').val();
var formSystemId = jQuery('input[name="formSystemId"]').val();
var requestFormDate = jQuery('input[name="request_date"]').val();
var currentNodeId = "";
var operateType = "";
var MAX_PLAN = 14;
var SECTION_IH = "9";
var SECTION_OP = "11";
var SECTION_MAT = "12";
var SECTION_SMM = "10";
var SECTION_DEN = "13";
var SECTION_LIFCOV = "15";
var SECTION_MEDCOV = "16";

jQuery().ready(function(){
	currentNodeId=jQuery("[name='currentNodeId']").val();
	operateType = jQuery("[name='operateType']").val();
//	currentNodeId="2";	//debug
//	alert(currentNodeId);
	if(currentNodeId=="0" || currentNodeId==undefined){	//Request Proposal
		//Change button name
		jQuery('input[name="saveBtn"]').val("Save");
		jQuery('input[name="subBtn"]').val("Generate Proposal");
		jQuery('input[name="exportBtn"]').hide();
		
		//hide proposal section
//		jQuery('#div18').closest('tr').hide();
//		jQuery('#div18').closest('tr').prev().hide();
//		jQuery('#div18').closest('tr').prev().prev().hide();
		
		jQuery('#sectionTable8').closest('div').closest('tr').hide();
		jQuery('#sectionTable8').closest('div').closest('tr').prev().hide();
		jQuery('#sectionTable8').closest('div').closest('tr').prev().prev().hide();
		
		
		jQuery('textarea[name="field_2_2"]').change(function(){  
			var compcode = jQuery(this).val();
			loadCompcodeRefInfo(compcode);
			});

		jQuery('select[name="company_name"]').change(function(){  
			var compcode = jQuery(this).val();
			jQuery('textarea[name="field_2_2"]').val(compcode);
			jQuery('textarea[name="field_2_2"]').change();
			});		
		
		//Life Product Options:
		jQuery('select[name="field_20_1"]').change(function(){  
			refreshLifePrintOpt();
			loadLifeUWVer();
			loadLifeCurrency();
			});
		jQuery('select[name="field_20_3"]').change(function(){  
			loadLifeCurrency();
			});
		
		//GHS Product Options:
		jQuery('select[name="field_21_1"]').change(function(){  
			loadGHSHCVer();
			loadGHSUWVer();
			loadMedCurrency();
			//20150522 begin
//			loadAddBenPlan(true);
			//20150522 end
			addPlan(SECTION_IH);//Medical - In Hospital
			addPlan(SECTION_OP);
			addPlan(SECTION_SMM);
			addPlan(SECTION_DEN);
//			addPlan(SECTION_LIFCOV);
//			addPlan(SECTION_MEDCOV);			
			});
		jQuery('select[name="field_21_2"]').change(function(){  
			loadGHSUWVer();
			loadMedCurrency();
			});
		jQuery('select[name="field_21_3"]').change(function(){  
			loadMedCurrency();
			});

		//MC Product Options:
		jQuery('select[name="field_22_1"]').change(function(){  
			loadMedCurrency();
			//20150522 begin
//			loadAddBenPlan(true);
			//20150522 end
			addPlan(SECTION_IH);//Medical - In Hospital
			addPlan(SECTION_OP);
			addPlan(SECTION_SMM);
			addPlan(SECTION_DEN);
//			addPlan(SECTION_LIFCOV);
//			addPlan(SECTION_MEDCOV);
			});

		jQuery('input[name="field_2_8"]').click(function(){  
			clearBtn(this);
		});

		jQuery('input[name="field_20_4"]').click(function(){  
			var curCheckBox = jQuery(this);
			if(curCheckBox.attr('checked')=="checked" && (curCheckBox.val()=="2" || curCheckBox.val()=="3")){
//				  jQuery(':checkbox[name="'+this.name+'"][value="2"]').removeAttr('checked');
//				  jQuery(':checkbox[name="'+this.name+'"][value="3"]').removeAttr('checked');
				  jQuery('input:checkbox[name="field_20_4"][value="2"]').removeAttr('checked');
				  jQuery('input:checkbox[name="field_20_4"][value="3"]').removeAttr('checked');
				  curCheckBox.attr('checked','checked'); 
			}
			chkLifePrintOpt(this);
			enableVer();
		});
		
		jQuery('input:checkbox[name="field_21_4"]').click(function(){			
			if(jQuery(this).attr('checked')=="checked"){
				  jQuery('input:checkbox[name="field_22_4"]').removeAttr('checked'); 
			}
			enableVer();
			
			jQuery('select[name="field_21_1"]').change();
			
		});

		jQuery('input:checkbox[name="field_22_4"]').click(function(){	
			//20150515 begin: Disable the print MC option (Item-18)
//			if(jQuery(this).attr('checked')=="checked"){
//				  jQuery('input:checkbox[name="field_21_4"]').removeAttr('checked'); 
//			}
//			enableVer();
//
//			jQuery('select[name="field_22_1"]').change();
			
			jQuery(this).removeAttr('checked'); 
			//20150515 end
		});
		
		//Addtional Benefits: hide the unavailable fields and clear their values
		jQuery('#formTable17').closest('div').find('input[name="addRowBtn"]').attr("onclick","");
		jQuery('#formTable17').closest('div').find('input[name="addRowBtn"]').click(function(){
			createTableSectionRow("formTable17","17","0");
			hideAddBenDtl();
		});	
		
		//Plan level item: hide the [Add] and [Delete]
		jQuery('#formTable' + SECTION_IH).closest('div').find('input[type="button"]').remove();
		jQuery('#formTable' + SECTION_OP).closest('div').find('input[type="button"]').remove();
		jQuery('#formTable' + SECTION_SMM).closest('div').find('input[type="button"]').remove();
		jQuery('#formTable' + SECTION_DEN).closest('div').find('input[type="button"]').remove();
//		jQuery('#formTable' + SECTION_LIFCOV).closest('div').find('input[type="button"]').remove();
//		jQuery('#formTable' + SECTION_MEDCOV).closest('div').find('input[type="button"]').remove();
		
		enableVer();
		loadVersion();
		loadCurrency();
		jQuery('select[name="field_20_5"]').hide();
		jQuery('select[name="field_22_2"]').hide();
		jQuery('select[name="field_22_3"]').hide();
		//20150522 begin: not need to load the benefit limit
//		loadAddBenPlan(false);
		//20150522 end
	}
//	else if(currentNodeId=="2"){	//Prepare Proposal
//		jQuery("input[name='selectRefBtn']").eq(0).attr("value","Proposal");
//		jQuery("input[name='selectRefBtn']").eq(0).attr("onclick","");
//	}
	else {
		//20150522 begin: not need to load the benefit limit
//		loadAddBenPlan(false);
		//20150522 end
		//hide save button
		jQuery('input[name="updateBtn"]').hide();
	}

	if(currentNodeId=="2"){	//Preview Prop.
		var request_no = jQuery('input[name="request_no"]').val();
		var sql = "SELECT isnull(field_8_1,'') as report_no FROM teflow_30_8 where request_no = '" + request_no + "'";
		var objDataset = selectFromSQLJson('',sql);
		var report_no = "";

		if (objDataset != undefined && objDataset.length>0) {
			jQuery.each(objDataset,function(i,item) {	
				report_no = item.REPORT_NO;
			});
		}
		
		if(report_no==""){
			jQuery('input[name="field_8_1_checkbox"]').	attr('checked','checked'); 
		}		
		
	}else{
		jQuery('input[name="field_8_1_checkbox"]').removeAttr('checked'); 
		jQuery('input[name="field_8_1_checkbox"]').attr("disabled","disabled");
	}

});

function refreshLifePrintOpt(){
	jQuery('input[name="field_20_4"]:checked').each(function(i){
		chkLifePrintOpt(this);
	});
}

function chkLifePrintOpt(selectedObj){
	var selectedObj = jQuery(selectedObj);
	var sProdName = getLifeProdName(selectedObj.val());
	var slifeCvgVer = jQuery('select[name="field_20_1"]').val();
	var compcode = getCompcode();
	var sql = "";
	
	if(selectedObj.attr('checked')=="checked"){
		selectedObj.removeAttr('checked');
		
		if(sProdName=="LTD"){
			sql = "select 'LTD' as prod_name from t_wp_ltd1 where comp_code = '" + compcode + "' and ver_no = '" + slifeCvgVer + "'";			
		}else{
			sql = "select distinct prod_name from t_wp_life1 where comp_code = '" + compcode + "' and ver_no = '" + slifeCvgVer + "' and prod_name = '" + sProdName + "'";
		}
		var objDataset = selectFromSQLJson('gmpsJNDI',sql);
		
		if (objDataset != undefined && objDataset.length>0) {
			selectedObj.attr('checked','checked'); 
		}
	}
}

function getLifeProdName(lifeProdIdx){
	var lifeProdName = "";
	if(lifeProdIdx==1){
		lifeProdName = "LIFE";
	}else if(lifeProdIdx==3){
		lifeProdName = "ADD(L)";
	}else if(lifeProdIdx==4){
		lifeProdName = "TPDI";
	}else if(lifeProdIdx==5){
		lifeProdName = "CI";
	}else if(lifeProdIdx==6){
		lifeProdName = "LTD";
	}else{
		lifeProdName = "";
	}
	return lifeProdName;
}

function addPlan(sectionID){
	jQuery('#formTable' + sectionID + ' tr:gt(0)').remove();
	var oprDIV = jQuery('#formTable' + sectionID).closest('div');
	
	var objDataset = getMedPlans();
	if (objDataset != undefined && objDataset.length>0) {
		jQuery.each(objDataset,function(i,item) {	
			value = item.PLAN_NO;
			createTableSectionRow("formTable"+sectionID,sectionID,"0");
			jQuery(oprDIV).find('textarea[name="field_' + sectionID + '_1"]:last').text(value);
		});
	}	
}

function hideAddBenDtl(){
	var tdBenLimitHdr = jQuery('#formTable17 tr.tr2 td:visible:gt(2)');
	jQuery('#formTable17 tr:gt(0) td').show();
	var nPlanIdx = tdBenLimitHdr.length;
	var colidx = 0;
	while(nPlanIdx<MAX_PLAN){
		colidx = nPlanIdx + 4;
		if(currentNodeId=="0" || currentNodeId == undefined){
			jQuery('#formTable17 textarea[name="field_17_' + colidx + '"]').text("");
			jQuery('#formTable17 textarea[name="field_17_' + colidx + '"]').closest('td').hide();
		}else{
			jQuery('#formTable17 input[name="field_17_' + colidx + '"]').closest('td').hide();
		}
		nPlanIdx=nPlanIdx+1;
	}
}

function getMedPlans(){
	var compcode = getCompcode();
	var sGHSCvgVer = "";
	var sMCCvgVer = "";
	if(currentNodeId=="0" || currentNodeId == undefined){
		var sGHSCvgVer = jQuery('select[name="field_21_1"]').val();
		var sMCCvgVer = jQuery('select[name="field_22_1"]').val();
		var bPrintGHS = jQuery('input:checkbox[name="field_21_4"]:checked').val();
		var bPrintMC = jQuery('input:checkbox[name="field_22_4"]:checked').val();
	}else{
		var sGHSCvgVer = jQuery('input[name="field_21_1"]').val();
		var sMCCvgVer = jQuery('input[name="field_22_1"]').val();	
		var bPrintGHS = jQuery('input[name="field_21_4"]').val();
		var bPrintMC = jQuery('input[name="field_22_4"]').val();	
	}
	var sql="";
	if(bPrintGHS=="1"){
		sql = "select distinct plan_no from t_wp_med1 where comp_code = '" + compcode + "' and ver_no = '" + sGHSCvgVer + "'";
	}else if(bPrintMC=="1"){
		sql = "select distinct plan_no from t_wp_mc1 where comp_code = '" + compcode + "' and ver_no = '" + sMCCvgVer + "'";
	}
	
	var objDataset = selectFromSQLJson('gmpsJNDI',sql);
	return objDataset;
}

function loadAddBenPlan(bRefresh){	
	var compcode = getCompcode();
	var objDataset = getMedPlans();
	var tdBenLimitHdr = jQuery('#formTable17 tr.tr2 td:gt(2)');
	var colidx = 4;
	//Clear header and detail value
	tdBenLimitHdr.text("Benefit Limit");
	tdBenLimitHdr.show();
	if(bRefresh){
		while(colidx < MAX_PLAN + 4){
			jQuery('#formTable17 textarea[name="field_17_' + colidx + '"]').text("");
			colidx = colidx + 1;
		}
	}
	
	var nPlanIdx = 0;
	if (objDataset != undefined && objDataset.length>0) {
		jQuery.each(objDataset,function(i,item) {	
			value = item.PLAN_NO;
			nPlanIdx = i;
			tdBenLimitHdr.eq(i).text("Benefit Limit - Plan " + value);
		});
	}
	//Hide header
	var nPlanIdx = nPlanIdx + 1;
	
	while(nPlanIdx<MAX_PLAN){		
		tdBenLimitHdr.eq(nPlanIdx).hide();
		nPlanIdx=nPlanIdx+1;
	}
	//Hide detail
	hideAddBenDtl();
}

function loadLifePlan(){
	var i = 0;
}

function loadMedPlan(){
	var i = 0;
}

function enableVer(){
	jQuery('select[name="field_20_1"]').attr("disabled","disabled");
	jQuery('select[name="field_20_5"]').attr("disabled","disabled");
	jQuery('select[name="field_20_3"]').attr("disabled","disabled");
	jQuery('select[name="field_21_1"]').attr("disabled","disabled");
	jQuery('select[name="field_21_2"]').attr("disabled","disabled");
	jQuery('select[name="field_21_3"]').attr("disabled","disabled");
	jQuery('select[name="field_22_1"]').attr("disabled","disabled");
	jQuery('select[name="field_22_2"]').attr("disabled","disabled");
	jQuery('select[name="field_22_3"]').attr("disabled","disabled");	
	
	jQuery('select[name="field_20_1"]')[0].setAttribute('required','false');
	jQuery('select[name="field_20_5"]')[0].setAttribute('required','false');
	jQuery('select[name="field_20_3"]')[0].setAttribute('required','false');
	jQuery('select[name="field_21_1"]')[0].setAttribute('required','false');
	jQuery('select[name="field_21_2"]')[0].setAttribute('required','false');
	jQuery('select[name="field_21_3"]')[0].setAttribute('required','false');
	jQuery('select[name="field_22_1"]')[0].setAttribute('required','false');
	jQuery('select[name="field_22_2"]')[0].setAttribute('required','false');
	jQuery('select[name="field_22_3"]')[0].setAttribute('required','false');	
	
	
	var bPrintLife = false;
	var bPrintGHS = false;
	var bPrintMC = false;
	
	jQuery('input[name="field_20_4"]:checked').each(function(i){
		bPrintLife = true;
	});
	jQuery('input[name="field_21_4"]:checked').each(function(i){
		bPrintGHS = true;
	});
	jQuery('input[name="field_22_4"]:checked').each(function(i){
		bPrintMC = true;
	});
	
	if(bPrintLife){
		jQuery('select[name="field_20_1"]').removeAttr("disabled");
		jQuery('select[name="field_20_3"]').removeAttr("disabled");
		jQuery('select[name="field_20_1"]')[0].setAttribute('required','true');
		jQuery('select[name="field_20_3"]')[0].setAttribute('required','true');
	}

	if(bPrintGHS){
		jQuery('select[name="field_21_1"]').removeAttr("disabled");
		jQuery('select[name="field_21_2"]').removeAttr("disabled");
		jQuery('select[name="field_21_3"]').removeAttr("disabled");
		jQuery('select[name="field_21_1"]')[0].setAttribute('required','true');
		jQuery('select[name="field_21_2"]')[0].setAttribute('required','true');
		jQuery('select[name="field_21_3"]')[0].setAttribute('required','true');
	}

	if(bPrintMC){
		jQuery('select[name="field_22_1"]').removeAttr("disabled");
		jQuery('select[name="field_22_1"]')[0].setAttribute('required','true');
	}	
}

function loadCompcodeRefInfo(compcode){	
	//Clear data
	jQuery('select[name="company_name"]').val("");
	jQuery('select[name="Pr_AE"]').val("");
	jQuery('select[name="Pr_AB"]').val("");
	jQuery('input[name="field_2_8"]').each(function(){		//SOB
		jQuery(this).attr('checked',false);	//Clear first
		});
	jQuery('input[name="field_2_5"]').val("");
	//Refresh data
	jQuery('select[name="company_name"]').val(compcode);
	var sql = "SELECT source,ab_code,ab_name,ae_code,CONVERT(CHAR(10),lann_date,101) AS lann_date FROM t_wp_comp where comp_code = '" + compcode + "'";
	var data = selectFromSQLJson('gmpsJNDI',sql);
	if(data.length>0){
		jQuery.each(data,function(i,item){
			jQuery('select[name="Pr_AE"]').val(jQuery.trim(item.AE_CODE));   //AE code
			jQuery('select[name="Pr_AB"]').val(jQuery.trim(item.AB_CODE));   //Agent/Broker

			jQuery('input[name="field_2_8"]').each(function(){		//SOB
				jQuery(this).attr('checked',false);	//Clear first
				if(jQuery(this).val()==item.SOURCE){
					jQuery(this).attr('checked',true);
				}
			});
			
			jQuery('input[name="field_2_5"]').val(item.LANN_DATE);   //Ref. Date
		});
	}
	loadVersion();
	loadCurrency();
}

function loadCurrency(){
	loadLifeCurrency();
	loadMedCurrency();
}

function loadLifeCurrency(){
	var compcode  =  getCompcode();
	var slifeVer = jQuery('select[name="field_20_1"]').val();
	var oLifeCurrency = jQuery('select[name="field_5_1"]');
	var sql = "select distinct currency as option_value, currency as option_label from t_wp_life1 where comp_code = '" + compcode + "' and ver_no = '" + slifeVer + "' order by currency";
//	alert(sql);
	var data = selectFromSQLJson('gmpsJNDI',sql);
	loadSelect(data,oLifeCurrency,'first');
}

function loadMedCurrency(){
	var compcode  =  getCompcode();
	var sGHSVer = jQuery('select[name="field_21_1"]').val();
	var sMCVer = jQuery('select[name="field_22_1"]').val();
	var oMedCurrency = jQuery('select[name="field_6_1"]');
	var sql = "select distinct currency as option_value, currency as option_label from t_med_versetting where comp_code = '" + compcode + "' and ver_no = '" + sGHSVer + "'";
	sql = sql + " UNION";
	sql = sql + " select distinct currency as option_value, currency as option_label from t_mc_versetting where comp_code = '" + compcode + "' and ver_no = '" + sMCVer + "'";
	sql = sql + " order by currency";
//	alert(sql);
	var data = selectFromSQLJson('gmpsJNDI',sql);
	loadSelect(data,oMedCurrency,'first');
}

function loadVersion(){
	loadLifeVersion();
	loadLifeUWVer();
	loadGHSVersion();
	loadGHSHCVer();
	loadGHSUWVer();
	loadMCVersion();
}

function loadLifeVersion(){
	var compcode  =  getCompcode();
	var version = jQuery('select[name="field_20_1"]');
	var sql = "select distinct ver_no as option_value, ver_no as option_label from t_wp_life1 where comp_code = '" + compcode + "' order by ver_no";
//	alert(sql);
	var data = selectFromSQLJson('gmpsJNDI',sql);
	loadSelect(data,version,'last');
}

function loadLifeUWVer(){
	var compcode  =  getCompcode();
	var lifeCvgVerVal = jQuery('select[name="field_20_1"]').val();
	var lifeUWVer = jQuery('select[name="field_20_3"]');
	var sql = "select distinct uwver_no as option_value, uwver_no as option_label from t_wp_life_underwriting where comp_code = '" + compcode + "' and ver_no = '" + lifeCvgVerVal + "' order by uwver_no"
//	alert(sql);
	var data = selectFromSQLJson('gmpsJNDI',sql);
	loadSelect(data,lifeUWVer,'last');
}

function loadGHSVersion(){
	var compcode  =  getCompcode();
	var version = jQuery('select[name="field_21_1"]');
	var sql = "select distinct ver_no as option_value, ver_no as option_label from t_wp_med1 where comp_code = '" + compcode + "' order by ver_no";
	var data = selectFromSQLJson('gmpsJNDI',sql);
	loadSelect(data,version,'last');
}

function loadGHSHCVer(){
	var compcode  =  getCompcode();
	var sCvgVer = jQuery('select[name="field_21_1"]').val();	
	var oHCVer = jQuery('select[name="field_21_2"]');
	var sql = "select distinct hcver_no as option_value, hcver_no as option_label from t_wp_med_underwriting where hcver_no <> '' and ver_no <> '' and comp_code = '" + compcode + "' and cvgver_no = '" + sCvgVer + "' order by hcver_no"
	var data = selectFromSQLJson('gmpsJNDI',sql);
	loadSelect(data,oHCVer,'last');
}

function loadGHSUWVer(){
	var compcode  =  getCompcode();
	var sCvgVer = jQuery('select[name="field_21_1"]').val();	
	var sHCVer = jQuery('select[name="field_21_2"]').val();
	var oUWVer = jQuery('select[name="field_21_3"]');
	var sql = "select distinct ver_no as option_value, ver_no as option_label from t_wp_med_underwriting where hcver_no <> '' and ver_no <> '' and comp_code = '" + compcode + "' and cvgver_no = '" + sCvgVer + "' and hcver_no = '" + sHCVer + "' order by ver_no"
	var data = selectFromSQLJson('gmpsJNDI',sql);
	loadSelect(data,oUWVer,'last');
}

function loadMCVersion(){
	var compcode  =  getCompcode();
	var version = jQuery('select[name="field_22_1"]');
	var sql = "select distinct ver_no as option_value, ver_no as option_label from t_wp_mc1 where comp_code = '" + compcode + "' order by ver_no";
	var data = selectFromSQLJson('gmpsJNDI',sql);
	loadSelect(data,version,'last');
}

function loadSelect(objDataset,objSelect,defSeleted){
	objSelectVal = jQuery(objSelect).find("option:selected").val();
//	alert(objSelectVal);
	jQuery(objSelect).empty();
	if(defSeleted=='blank'){
		jQuery(objSelect).append("<option value=''></option>");
	}
	if (objDataset != undefined && objDataset.length>0) {
		jQuery.each(objDataset,function(i,item) {	
			value = item.OPTION_VALUE;
			text = item.OPTION_LABEL;
			jQuery(objSelect).append("<option value='" + value + "'>" + text + "</option>");						
		});
	}
	if(objSelectVal!='' && objSelectVal!=null){
		jQuery(objSelect).val(objSelectVal);
	}else{
		if(defSeleted=='last'){		
			jQuery(objSelect).find("option:last-child").attr("selected", true);
		}
		else{
			jQuery(objSelect).find("option:first-child").attr("selected", true);
		}
	}
}

function getCompcode(){	
	if(currentNodeId=="0" || currentNodeId == undefined){
		return jQuery('textarea[name="field_2_2"]').val();
	}else{
		return jQuery('input[name="field_2_2"]').val();
	}
}

function loadCompcodeRefInfo1(OperItem)
{
  try
  {   
	  RefreshPic(OperItem,99,"");
	  RefreshPic(OperItem,0,"");
	  
	  var compname = jQuery("#sectionTable2 select[name='company_name']")
	  var compcode  =  OperItem.innerText.trim(); 
	  
	  

	  var sql = "SELECT source,agy_code,ab_name,ae_code,CONVERT(CHAR(10),lann_date,101) AS lann_date FROM t_wp_comp where comp_code = '" + compcode + "'";
		var data = selectFromSQLJson('gmpsJNDI',sql);
		if(data.length>0){
			jQuery.each(data,function(i,item){
				jQuery('textarea[name="field_2_3"]').val(item.AE_CODE);   //AE code
				jQuery('textarea[name="field_2_4"]').val(item.AGY_CODE);   //Agent/Broker
				//jQuery('textarea[name="company_name"]').val(compcode);   
//				compname.combobox("setValue", compcode);

				jQuery('input[name="field_2_8"]').each(function(){		//SOB
					jQuery(this).attr('checked',false);	//Clear first
					if(jQuery(this).val()==item.SOURCE){
						jQuery(this).attr('checked',true);
					}
				});
				
//				alert(item.LANN_DATE);
				jQuery('input[name="field_2_5"]').val(item.LANN_DATE);   //Ref. Date
				  RefreshPic(OperItem,2,"");  
				  return;
			});
		}
		  else
		  {
			  RefreshPic(OperItem,1,"The company code is not exist");  
				jQuery('textarea[name="field_2_3"]').val("");   //AE code
				jQuery('textarea[name="field_2_4"]').val("");   //Agent/Broker
				jQuery('input[name="field_2_5"]').val("")
			  return;
		  }
  }
  catch(e)
  {
		RefreshPic (OperItem,99,"");  
		alert ("MailFormatChecking: " + e.message);  
  }
}


function RefreshPic(OperItem, Param, ShowText){
	
	 var ShowPic  = "";
	 ShowText = "  " + ShowText;
	 deletePic(OperItem,0);
     
	 switch(Param){
	       case 0: //0 - Loading
	       {
	         ShowPic = "images/Checking/Loading.gif"; 
	         break;
	       }
	       case 1: //1 - Existed
	       {
	         ShowPic = "images/Checking/Invalid.png"; 
	         break;
	       }
	       case 2: //2 - New
	       {
	         ShowPic = "images/Checking/Valid.png"; 
	         break;
	       }
	       case 3: //3 - for ID validation
	       {
	         ShowPic= "images/Checking/Warning.png"; 
	         break;
	       }
	       case 4: //4 - Show Client checking
	       {
	         ShowPic= "images/Checking/Search.png"; 
	         break;
	       }
	       case 10: //10 - delete all client PIC
	       {
	    	 deletePic(OperItem,1);
	    	 return true;
	       }
	       case 11: //11 - delete 2 client name only
	       {
	    	 deletePic(OperItem,2);
	    	 return true;
	       }
	       default: //- other         
	       {	//no need process anything
	         deletePic(OperItem,0);
	      	 return true;
	       }
	 }  
	 
	 var heightvar  = 16; 
	 var widthhtvar = 16;
	 
	 
	 var newImg = jQuery('<img width="'+widthhtvar+'" height="'+heightvar+'" id="'+OperItem.name
			 +'_ProcessLoading"  style="width: '+widthhtvar+'px; height: '
			 + heightvar+'px; border-width: 0px; cursor: pointer;" src="'
			 + ShowPic +'" />');
	 
	 var SpaceInput1 = jQuery('<span id="'+OperItem.name + '_ProcessSpace">&nbsp;&nbsp;</span>');
	
	 var SpaceInput2 = jQuery('<span id="'+OperItem.name + '_ProcessContent" style="color:red">&nbsp;'+ShowText+'</span>');
	 //var SpaceInput2 = jQuery('<span id="'+OperItem.name + '_ProcessContent" style="color:red">&nbsp;'+1111111+'</span>');
	 

	 
	 var CurrentRow = OperItem.parentNode.parentNode.rowIndex;
	 
	 
	 //alert (CurrentRow + "   "+ ObjectCurrentRow);
	 
	 //if(jQuery("#"+OperItem.name+"_ProcessSpace").length>0 || (CurrentRow == ObjectCurrentRow))
	 var ObjectCurrentRow = jQuery("#"+OperItem.name+"_ProcessSpace").closest('tr').index();
	 
	 if(jQuery("#"+OperItem.name+"_ProcessSpace").length>0 && (CurrentRow == ObjectCurrentRow))
	 {
		 jQuery("#"+OperItem.name+"_ProcessSpace").show();
	 }
	 else
	 {
		 jQuery(OperItem).parent().append(SpaceInput1);
	 }
	 
	 if(jQuery("#"+OperItem.name+"_ProcessLoading").length>0 && (CurrentRow == ObjectCurrentRow))
	 { 
		 jQuery("#"+OperItem.name+"_ProcessLoading").attr('src',ShowPic);
		 jQuery("#"+OperItem.name+"_ProcessLoading").show();
	 }
	 else
	 {
		 jQuery(OperItem).parent().append(newImg);
	 }

	 if(jQuery("#"+OperItem.name+"_ProcessContent").length>0 && (CurrentRow == ObjectCurrentRow))
	 {
		 jQuery("#"+OperItem.name+"_ProcessContent").text(ShowText);
		 jQuery("#"+OperItem.name+"_ProcessContent").show();
	 }
	 else
	 {
		 jQuery(OperItem).parent().append(SpaceInput2);
	 }
	 
	 jQuery(OperItem).parent().css("text-align","left");
	 jQuery(OperItem).parent().css("vertical-align","bottom");
}

function deletePic(OperItem ,Param){
	switch(Param){
	       case 0: //0 - clear code only
	       {
	    		try
	    		{
	    			 //var CurrentRow = OperItem.parentNode.parentNode.rowIndex;
	    			 //var ObjectCurrentRow = jQuery("#"+OperItem.name+"_ProcessSpace").index();
	    			 //if(CurrentRow == ObjectCurrentRow)
	    			// {
	    				 jQuery("img[id^="+OperItem.name+"]").hide();
	    				 jQuery("span[id^="+OperItem.name+"]").hide();
	    			// }
	    		}
	    		catch(e)
	    		{
	    			alert("deletePic:0 " + e.message);
	    			return false;
	    		}
	            break;
	       }
	       case 1: //1 - clear all client
	       {
	    	    var ClintCode = "field_02_16";
	    		var ClintName = "field_02_17";
	    		var ClintGenName = "field_02_26";
	    		
	    		try
	    		{
		    		jQuery("img[id^="+ClintCode+"]").hide();
		    		jQuery("span[id^="+ClintCode+"]").hide();
		    		jQuery("img[id^="+ClintName+"]").hide();
		    		jQuery("span[id^="+ClintName+"]").hide();
		    		jQuery("img[id^="+ClintGenName+"]").hide();
		    		jQuery("span[id^="+ClintGenName+"]").hide();
	    		}
	    		catch(e)
	    		{
	    			alert("deletePic:1 " + e.message);
	    			return false;
	    		}	
	            break;
	       }
	       case 2: //2 - clear 2 name only 
	       {
	    	    //var ClintCode = "field_02_16";
	    		var ClintName = "field_02_17";
	    		var ClintGenName = "field_02_26";
	    		
	    		try
	    		{
		    		//jQuery("img[id^="+ClintCode+"]").remove();
		    		//jQuery("span[id^="+ClintCode+"]").remove();
		    		jQuery("img[id^="+ClintName+"]").hide();
		    		jQuery("span[id^="+ClintName+"]").hide();
		    		jQuery("img[id^="+ClintGenName+"]").hide();
		    		jQuery("span[id^="+ClintGenName+"]").hide();
	    		}
	    		catch(e)
	    		{
	    			alert("deletePic:2 " +e.message);
	    			return false;
	    		}	
	            break;
	       }
	       default: //- other         
	       //no need process anything
	      	 return true;
	}  
}

function clearBtn(box){
	if(jQuery(box).attr('checked')=="checked"){
	  jQuery('input:checkbox[name="'+box.name+'"]').removeAttr('checked'); 
	  jQuery(box).attr('checked','checked'); 
	}
}