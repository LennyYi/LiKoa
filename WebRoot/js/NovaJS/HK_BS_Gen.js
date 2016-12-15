
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
	loadPreview();

});

function loadPreview(){
	var reportNo = jQuery('input[name="Prop_version"]').val();
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