
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
		

/*		jQuery('#formTable15').closest('div').closest('tr').hide();
		jQuery('#formTable15').closest('div').closest('tr').prev().hide();
		jQuery('#formTable15').closest('div').closest('tr').prev().prev().hide();

		jQuery('#formTable16').closest('div').closest('tr').hide();
		jQuery('#formTable16').closest('div').closest('tr').prev().hide();
		jQuery('#formTable16').closest('div').closest('tr').prev().prev().hide();*/
		
		
		//Change button name
		jQuery('input[name="saveBtn"]').val("Save");
		jQuery('input[name="subBtn"]').val("Edit Proposal");
		jQuery('input[name="exportBtn"]').hide();
		
		jQuery('textarea[name="field_2_2"]').change(function(){  
			var compcode = jQuery(this).val();
			loadCompcodeRefInfo1(compcode);
			});

		jQuery('select[name="company_name"]').change(function(){  
			var compcode = jQuery(this).val();
			jQuery('textarea[name="field_2_2"]').val(compcode);
			jQuery('textarea[name="field_2_2"]').change();
			});		
		
		jQuery('select[name="Prop_version"]').change(function(){  
			loadPreview();
			var psVersion = jQuery(this).val();
			var sql = "SELECT replace(itemvalue,'Plan ','') as PLAN_NO FROM teflow_report_2_4 WHERE class = 'h0_P10' AND request_no = '" + psVersion + "'";
			var objDataset = selectFromSQLJson('',sql);	
			addPlan(objDataset,SECTION_LIFCOV);	
			addPlan(objDataset,SECTION_MEDCOV);
			});
		
		jQuery('input[name="field_2_8"]').click(function(){  
			clearBtn(this);
		});
		
		loadPrVersion();
	}
	else {
		jQuery('input[name="updateBtn"]').hide();
	}
	
//	if(currentNodeId=="1"){//Edit Prop.
//		var psVersion = jQuery('input[name="Prop_version"]').val();
//		var sql = "SELECT replace(itemvalue,'Plan ','') as PLAN_NO FROM teflow_report_2_4 WHERE class = 'h0_P10' AND request_no = '" + psVersion + "'";
//		var objDataset = selectFromSQLJson('',sql);	
//		addPlan(objDataset,SECTION_LIFCOV);	
//		addPlan(objDataset,SECTION_MEDCOV);
//	}

	if(currentNodeId=="2"){	
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
			jQuery('input[name="field_8_1_checkbox"]').attr('checked','checked'); 
		}		
		
	}else{
		jQuery('input[name="field_8_1_checkbox"]').removeAttr('checked'); 
		jQuery('input[name="field_8_1_checkbox"]').attr("disabled","disabled");
	}
	jQuery('input[name="field_PropDoc_2_checkbox"]').val("");
	jQuery('input[name="field_PropDoc_2_checkbox"]').attr("disabled","disabled");
	
});

/*
function loadCompcodeRefInfo(compcode){	
//	alert("ddd");
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
}
*/
		
function addPlan(objDataset,sectionID){
	jQuery('#formTable' + sectionID + ' tr:gt(0)').remove();
	var oprDIV = jQuery('#formTable' + sectionID).closest('div');	
	
	if (objDataset != undefined && objDataset.length>0) {
		jQuery.each(objDataset,function(i,item) {	
			value = item.PLAN_NO;
			
			createTableSectionRow("formTable"+sectionID,sectionID,"0");
			jQuery(oprDIV).find('textarea[name="field_' + sectionID + '_1"]:last').text(value);
		});
	}	
}

function loadCompcodeRefInfo1(OperItem)
{
  try
  {   
	  RefreshPic(OperItem,99,"");
	  RefreshPic(OperItem,0,"");
	  
	  var compcode = getCompcode();
	  
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
//	  var sql = "SELECT source,agy_code,ab_name,ae_code,CONVERT(CHAR(10),lann_date,101) AS lann_date FROM t_wp_comp where comp_code = '" + compcode + "'";
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
				
//				alert(item.LANN_DATE);
				jQuery('input[name="field_2_5"]').val(item.LANN_DATE);   //Ref. Date
				loadPrVersion();
				
				  RefreshPic(OperItem,2,"");  
				  return;
			});
		}
		  else
		  {
			  RefreshPic(OperItem,1,"The company code is not exist");  
				jQuery('textarea[name="Pr_AE"]').val("");   //AE code
				jQuery('textarea[name="Pr_AB"]').val("");   //Agent/Broker
				jQuery('input[name="field_2_5"]').val("")
			  return;
		  }
  }
  catch(e)
  {
		RefreshPic (OperItem,99,"");  
//		alert ("MailFormatChecking: " + e.message);  
  }
}

function loadPreview(){
	var reportNo = jQuery('select[name="Prop_version"]').val();
	var form_request_no = "";
	var sql = "select form_request_no from teflow_report_instance where report_no = '" + reportNo + "'"; 
	var objDataset = selectFromSQLJson('',sql);
	if (objDataset != undefined && objDataset.length>0) {
		jQuery.each(objDataset,function(i,item) {	
			form_request_no = item.FORM_REQUEST_NO;					
		});
	}
	jQuery('input[name="field_PropDoc_2_button"]').removeAttr('onclick'); 
	jQuery('input[name="field_PropDoc_2_button"]').click(function(){  
//		alert(form_request_no);
		generateReport1(form_request_no,"field_PropDoc_2","2",reportNo,"");
		});	
}

function loadPrVersion(){
	var compcode = getCompcode();
	var sql = "SELECT d.report_no as option_value, d.report_no as option_label FROM teflow_30_2 b, teflow_wkf_process c, teflow_report_instance d where b.field_2_2 = '" + compcode + "' and c.request_no = b.request_no and c.node_id = -1 and status = '04' and d.form_request_no = b.request_no order by right(b.request_no,11) desc ,repost_vsersion desc";
//	alert(sql);
	var data = selectFromSQLJson('',sql);
	var oPrVersion = jQuery('select[name="Prop_version"]');
	loadSelect(data,oPrVersion,'first');
	oPrVersion.change();
	loadPreview();
}

function loadSelect(objDataset,objSelect,defSeleted){
	objSelectVal = jQuery(objSelect).find("option:selected").val();
//	alert(objSelectVal);
	jQuery(objSelect).empty();
	if(defSeleted=='blank'){
		jQuery(objSelect).append("<option value=''></option>");
	}
//	alert(objDataset.length);
	if (objDataset != undefined && objDataset.length>0) {
//		alert("aa");
		jQuery.each(objDataset,function(i,item) {	
			value = item.OPTION_VALUE;
			text = item.OPTION_LABEL;
//			alert(value);
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