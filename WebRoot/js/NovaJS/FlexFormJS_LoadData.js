function uniQueue(array) {
	var arr = [];
	var m;
	while (array.length > 0) {
		m = array[0];
		arr.push(m);
		array = jQuery.grep(array, function(n, i) {
			return n == m;
		}, true);
	}
	return arr;
}

var array_total = new Array(new Array(), new Array(), new Array(), new Array(), new Array(), new Array(), new Array(), new Array(), new Array(),new Array());

jQuery(document).ready(function() {
	
	var tableLength = jQuery("#formTable4 tr").length;
	if(tableLength>1)
		var planCode = jQuery("select[name='Plan_id']").length; 
	
	tableLength = jQuery("#formTable9 tr").length;
	if(tableLength>1)
	{
		var selectedMemPlan;
		jQuery("#formTable9 tr select[name='MemPlan_id']").each(function(){
			changeMemProduct(this,false,false);
		})
	}
	
    jQuery("#div5").find('input[name="addRowBtn"]').click(function(){
    	var lastRowNo =  tableLength - 1;

    	if(array_total[0].length==0)
	        setArray();
    	setCheckbox();	//20150402 Justin Bin Added NOVA-137   :Refresh the plan
	    removeCheckbox();
    });
    
    initControl();
    
	showProductPlan();
	addProductPlanControl();
	showPlan();
});

//20150514 Justin Bin Added.
function initControl(){
	jQuery("#formTableplanDetail").parent().find('input[name="addRowBtn"]').hide();
	jQuery("#formTableplanDetail").parent().find('input[name="deleteRowBtn"]').hide();
	jQuery('select[name="field_6_6"]').parent().hide();
	jQuery('select[name="field_6_6"]').parent().prev().hide();
}
//NOVA-66 Hinson BEGIN: trigger this function when package product changed
function pkgProductChange(selectedID){
	var planCode = jQuery(selectedID).closest('tr').find('select[name="Plan_id"]');
	jQuery(planCode).find("option:first-child").attr("selected",true);
	jQuery(planCode).change();
	
	jQuery('select[name="MemProduct_id"]').each(function(){	//load member pkg products
		loadMemPkgPdt(this);
//		changeMemProduct(this);
	});
}

function loadMemPkgPdt(selectedID){
	var selected = jQuery("select[name='Product_id']");
	var selectedVal = jQuery(selectedID).val();
	var options = [];

	jQuery(selectedID).empty();			
	jQuery(selectedID).append("<option value=''></option>");
	
	if(selected.length>0)
	{			
		jQuery.each(selected,function(i,item){
			value = jQuery(item).find("option:selected").val();
			text = jQuery(item).find("option:selected").text();	
			if(value!=''){
				options.push("<option value='"+value+"'>"+text+"</option>");
			}
		});
		
		options = uniQueue(options);			
		jQuery(selectedID).append(options);
	}
	
	jQuery(selectedID).combobox("rebindData");
	
	if(selectedVal!='')
		//jQuery(selectedID).val(selectedVal);
		jQuery(selectedID).combobox("setValue",selectedVal);
	else
		jQuery(selectedID).find("option:first-child").attr("selected", true);	
}

function loadNProduct(selectedID,srcChange){
	var selectedVal = jQuery(selectedID).val();
	
	var tr = jQuery(selectedID).closest('tr');
	
	var index = jQuery("#formTable4 tr").index(jQuery(selectedID).closest('tr'));
	
	var formSystemId2 = jQuery("input[name='formSystemId'][type='hidden']").val();
	
	if(selectedVal==''||srcChange)
	{
		var sql = "";
		if(index>1)
		{
			var productCode = jQuery("#formTable4 select[name='Product_id']").eq(0).val(); 
			sql = "select distinct NOVA_PRODCODE,NOVA_PRODCODE_Local from TNovaProductMapping where form_System_id="
				+ formSystemId2 + " and PRODCODE = (select distinct PRODCODE from TNovaProductMapping where NOVA_PRODCODE = '"+productCode+"')";
		}
		else
			sql = "select distinct NOVA_PRODCODE,NOVA_PRODCODE_Local from TNovaProductMapping where form_System_id="+ formSystemId2;
		
		var jsonObj = selectFromSQLJson('',sql);
		
		var prodCode = '';
		if(jsonObj.length>0 ){
			jQuery(selectedID).empty();
			jQuery(selectedID).append("<option value=''></option>");
			
			jQuery.each(jsonObj,function(i,item){
				jQuery(selectedID).append("<option value='"+item.NOVA_PRODCODE+"'>"+item.NOVA_PRODCODE_LOCAL+"</option>");   
			});
		}
		
		jQuery(selectedID).combobox("rebindData");
		
		if(selectedVal!='')
			//jQuery(selectedID).val(selectedVal);
			jQuery(selectedID).combobox("setValue",selectedVal);
		else
			jQuery(selectedID).find("option:first-child").attr("selected", true);

	}
	

}

function loadPlan(selectedID,srcChange){
	
	var selectedVal = jQuery(selectedID).val();
	var tr = jQuery(selectedID).closest('tr');
	var productVal = tr.find('select[name="Product_id"]').val();
	
	if(selectedVal==''||srcChange)
	{
		var prodCode = '';
		
		var sql = "select distinct NOVA_BENPLNCD,NOVA_BENPLNCD_Local from TNOVAProductMapping where NOVA_PRODCODE='"+ productVal +"'";
		
		var data = selectFromSQLJson('',sql);
		jQuery(selectedID).empty();
		
		if(data.length>0)
		{	
			jQuery(selectedID).append("<option value=''></option>");
			
			jQuery.each(data,function(i,item) {
				value = item.NOVA_BENPLNCD;
				text = item.NOVA_BENPLNCD_LOCAL;
				jQuery(selectedID).append("<option value='"+value+"'>"+text+"</option>");  
			});
		}
		
		jQuery(selectedID).combobox("rebindData");
		
		if(selectedVal!='')
			//jQuery(selectedID).val(selectedVal);
			jQuery(selectedID).combobox("setValue",selectedVal);
		else
			jQuery(selectedID).find("option:first-child").attr("selected", true);
	}
}

function planChange(selectedID){
	var tr = jQuery(selectedID).closest('tr');	
	var selectedVal = jQuery(selectedID).val();
	var productVal = tr.find('select[name="Product_id"]').val();
	var memProduct2 = jQuery("select[name='MemProduct_id']");
	if(memProduct2.length>0){
		jQuery.each(memProduct2,function(i,item) {
			loadMemProduct(item,true,true,true);
		});
	}
	showPlan();		//20150514 Justin Bin Added
}

function productChange(selectedID,isLoad){
	var tr = jQuery(selectedID).closest('tr');
	var index = jQuery("#formTable4 tr").index(jQuery(selectedID).closest('tr'));

	if(jQuery("#formTable4 tr").length>1 && index==1)
	{
		var productItem = jQuery("#formTable4 select[name='Product_id']");
		productItem.each(function(i){
			if(i>0)
			{
				loadNProduct(this,true);
				if(jQuery(this).val()=="")
					//jQuery(this).val(jQuery(selectedID).val());
					jQuery(this).combobox("setValue",jQuery(selectedID).val());
				//bindCompassPlanProduct(this ,true,isLoad);
			}
		});		
	}
	if(isLoad) {
		//NOVA-70-20150306 begin: refresh member product and plan
		var productPlan = tr.find('select[name="Plan_id"]');
		planChange(productPlan);
		//NOVA-70-20150306 end
	}	
	calSumFun();
}


function flexPlanChange(selectedID){
	
    var selectedValue = jQuery(selectedID).val();
    var rowIndex = jQuery("#formTable4 tr").index(jQuery(selectedID).closest("tr"));
	var rowTDs = jQuery(selectedID).closest("tr").find("td");
    refreshPlan();
    countPeopleByPlan();	//20150323 Justin Bin Added
}

function loadMemProduct(selectedID,srcChange,strShow,isLoad){
	var selected = jQuery("#formTable4 tr td select[name='Product_id']");
	var selectedVal = jQuery(selectedID).val();
	var options = [];	
	if(selectedVal==''||srcChange || selectedVal==null)
	{
		jQuery(selectedID).empty();
		jQuery(selectedID).append("<option value=''></option>");
		jQuery.each(selected,function(i,item){
			value = jQuery(item).find("option:selected").val();
			text = jQuery(item).find("option:selected").text();
			options.push("<option value='"+value+"'>"+text+"</option>");
		});
		options = uniQueue(options);		
		jQuery(selectedID).append(options);		
		
		jQuery(selectedID).combobox("rebindData");
		
		if(selectedVal!='' && selectedVal!=null )
			//jQuery(selectedID).val(selectedVal);
			jQuery(selectedID).combobox("setValue",selectedVal);
		else
			jQuery(selectedID).find("option:first-child").attr("selected", true);
		changeMemProduct(selectedID,strShow,isLoad);
	}
}

function changeMemProduct(selectedID,srcChange,isLoad){
	var selectedVal = jQuery(selectedID).closest('tr').find('select[name="MemProduct_id"]').val();
	var options = [];
	
	var curSelectPlan = jQuery(selectedID).closest('tr').find('select[name="MemPlan_id"]');
	var curSelectPlanVal = curSelectPlan.val();
	
	curSelectPlan.empty();
	
	if(selectedVal!='')
	{
		var selectedProduct = jQuery("#formTable4 select[name='Product_id']");
		
		if(selectedProduct.length>0)
		{
			jQuery(curSelectPlan).append("<option value=''></option>"); 
			
			selectedProduct.each(function(i){
				selectedPlanVal = selectedProduct.eq(i).find("option:selected").val();
				
				if(selectedPlanVal == selectedVal)
				{
					selectedPlan = selectedProduct.eq(i).closest('tr').find("select[name='Plan_id']");
					
					value = selectedPlan.find("option:selected").val();
					text = selectedPlan.find("option:selected").text();
					
					if(value!=='')
						options.push("<option value='"+value+"'>"+text+"</option>");
					
				}
			});
			
			options = uniQueue(options);
			curSelectPlan.append(options);
			
			curSelectPlan.combobox("rebindData");
		}
		
		if(curSelectPlanVal!=""&&curSelectPlanVal!=null)
			//curSelectPlan.val(curSelectPlanVal);
			curSelectPlan.combobox("setValue",curSelectPlanVal);
		
		if(srcChange)
			changeMemPlan(curSelectPlan[0],isLoad);
	}
}

function changeMemPlan(selectedID,isLoad){
	
	var dobField = jQuery(selectedID).closest('tr').find('input[name="field_9_9"]');
	
	var shipField = jQuery(selectedID).closest('tr').find('select[name="field_9_13"]');

	shipField.trigger('change');
	
	countPeopleByPlan();
	
	if(dobField.length>0)
		calculatePremium(dobField[0],isLoad);
}

function changeMemSHIP(selectedID){
	
	var productVal = jQuery(selectedID).closest('tr').find('select[name="field_9_20"]').val();
	var planVal = jQuery(selectedID).closest('tr').find('select[name="MemPlan_id"]').val();
	var sql = "select SHIP from TNovaProductMapping where NOVA_PRODCODE = '"+ productVal +"' and NOVA_BENPLNCD = '" + planVal + "'";
	
	var data = selectFromSQLJson('',sql);
	
	var selectedTR = jQuery(selectedID).closest('tr');
	var selectedTD = jQuery(selectedID).closest('td');
	
	selectedTR.css('background-color','rgb(235, 235, 233)');
	selectedTD.css('background-color','rgb(235, 235, 233)');
	
	if(data.length>0){	
		var value = data[0].SHIP;
		
		var selectSHIP = jQuery(selectedID).closest('tr').find('select[name="field_9_13"]').val();
		var statusField = jQuery(selectedID).closest('tr').find('textarea[name="field_9_19"]');
		
		var smemstatus = statusField.val();
		
		statusField.val(smemstatus.replace("w",""));
		
		if(selectSHIP!=""){
			if (value!=selectSHIP)
			{
				selectedTR.css('background-color','#F8FEC3');
				selectedTD.css('background-color','#FFB3B3');
				statusField.val(statusField.val()+"w");
			}
		}
		else if(statusField.val() != "")
		{
			selectedTR.css('background-color','#F8FEC3');
			selectedTD.css('background-color','#F8FEC3');
		}
	}
}

function setArray(){
	
}

function removeCheckbox(){
	
}

function setCheckbox(){
	
}

//20150507 Justin Bin
function showProduct(obj){
	//var deObj=jQuery('#formTable4')
	//break;
	initProducts();
	var tableA=jQuery('#formTableProduct')
	var trList=tableA.find("tr");
	for(var i=0;i<trList.length;i++){
		var prod=jQuery(trList[i]).find('select[name="field_Product_1"]').val();
		switch(prod){
		case "201C1":
			jQuery("td:eq(4)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(5)",jQuery("#formTable4 tr")).show();
			break;
		case "201C2":
			jQuery("td:eq(6)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(7)",jQuery("#formTable4 tr")).show();
			break;
		case "211C3":
			jQuery("td:eq(8)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(9)",jQuery("#formTable4 tr")).show();
			break;
		case "211D1":
			jQuery("td:eq(10)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(11)",jQuery("#formTable4 tr")).show();
			break;
		case "211D2":
			jQuery("td:eq(12)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(13)",jQuery("#formTable4 tr")).show();
			break;
		case "211S5":
			jQuery("td:eq(14)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(15)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(16)",jQuery("#formTable4 tr")).show();
			break;
		case "211T1":
			jQuery("td:eq(17)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(18)",jQuery("#formTable4 tr")).show();
			break;
		case "214S1":
			jQuery("td:eq(19)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(20)",jQuery("#formTable4 tr")).show();
			break;
		case "214S2":
			jQuery("td:eq(21)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(22)",jQuery("#formTable4 tr")).show();
			break;
		case "311S1":
			jQuery("td:eq(23)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(24)",jQuery("#formTable4 tr")).show();
			break;
		case "311S3":
			jQuery("td:eq(25)",jQuery("#formTable4 tr")).show();
			jQuery("td:eq(26)",jQuery("#formTable4 tr")).show();
			break;
			
		}
		
	}
}

//20150512 Justin Bin 
function showProductPlan(){
		var tableA=jQuery('#formTableProduct')
		var trList=tableA.find("tr");
		var prodCodes="";
		for(var i=1;i<trList.length;i++){
			var prod=jQuery(trList[i]).find('[name="field_Product_1"]').val();

			prodCodes+=",'"+prod+"'";
		}
		prodCodes=prodCodes.substring(1);
		jQuery("td:eq(2)",jQuery("#formTableplanDetail tr")).hide();
		jQuery("td:eq(4)",jQuery("#formTableplanDetail tr")).hide();
		
		
		var operateType = jQuery('input[name="operateType"][type="hidden"]').val();
		//var saveType = jQuery('input[name="saveType"][type="hidden"]');
		//if(prodCodes!="" && (operateType=="00" || operateType=="view"))
		if(operateType=="00" || operateType=="view")
			addProductPlans(prodCodes);
}

function initProducts(){
		jQuery("td:eq(4)",jQuery("#formTable4 tr")).hide();			//GTL
		jQuery("td:eq(5)",jQuery("#formTable4 tr")).hide();			//GTL
		jQuery("td:eq(6)",jQuery("#formTable4 tr")).hide();			//GTLPD
		jQuery("td:eq(7)",jQuery("#formTable4 tr")).hide();			//GTLPD
		jQuery("td:eq(8)",jQuery("#formTable4 tr")).hide();			//GABD
		jQuery("td:eq(9)",jQuery("#formTable4 tr")).hide();			//GABD
		jQuery("td:eq(10)",jQuery("#formTable4 tr")).hide(); 		//GADD2014
		jQuery("td:eq(11)",jQuery("#formTable4 tr")).hide();		//GADD2014
		jQuery("td:eq(12)",jQuery("#formTable4 tr")).hide();		//GPCA2014
		jQuery("td:eq(13)",jQuery("#formTable4 tr")).hide();
		jQuery("td:eq(14)",jQuery("#formTable4 tr")).hide();		//GAMR(A3)
		jQuery("td:eq(15)",jQuery("#formTable4 tr")).hide();
		jQuery("td:eq(16)",jQuery("#formTable4 tr")).hide();		//GADR(2014)
		jQuery("td:eq(17)",jQuery("#formTable4 tr")).hide();
		jQuery("td:eq(18)",jQuery("#formTable4 tr")).hide(); 		//GCIR25
		jQuery("td:eq(19)",jQuery("#formTable4 tr")).hide();
		jQuery("td:eq(20)",jQuery("#formTable4 tr")).hide();		//GCIR34
		jQuery("td:eq(21)",jQuery("#formTable4 tr")).hide();
		jQuery("td:eq(22)",jQuery("#formTable4 tr")).hide();		//GAHIR(B)
		jQuery("td:eq(23)",jQuery("#formTable4 tr")).hide();
		jQuery("td:eq(24)",jQuery("#formTable4 tr")).hide();
		jQuery("td:eq(25)",jQuery("#formTable4 tr")).hide();		//GHIR
		jQuery("td:eq(26)",jQuery("#formTable4 tr")).hide();
	
}

function addProductPlanControl(){
	var table = jQuery('#formTable4');
	var formSystemId2 = jQuery("input[name='formSystemId'][type='hidden']").val();
	
	if(formSystemId2 == "30321" )
	{
		
		jQuery("#div6 input[name='addRowBtn'][type='button']").click(function(){
			  
		});
		
		var saveType = jQuery('input[type="hidden"][name="saveType"]');
		
	}
}

//20150514 Justin Bin
function showPlan(){
	var table=jQuery('#formTable4');
	var trList=table.find("tr");
	jQuery("td:eq(5)",jQuery("#formTableplanDetail tr")).hide();
	//jQuery("td:eq(5)",jQuery('#formTableplanDetail input[name="field_planDetail_3"]')).val(" ");
	//jQuery('#formTableplanDetail').find('input[name="field_planDetail_3"]').val("");
	jQuery("td:eq(6)",jQuery("#formTableplanDetail tr")).hide();
	//jQuery("td:eq(5)",jQuery('#formTableplanDetail input[name="field_planDetail_4"]')).val(" ");
	table.find('input[name="field_planDetail_4"]').val(" ");
	jQuery("td:eq(7)",jQuery("#formTableplanDetail tr")).hide();
	table.find('input[name="field_planDetail_5"]').val(" ");
	jQuery("td:eq(8)",jQuery("#formTableplanDetail tr")).hide();
	table.find('input[name="field_planDetail_6"]').val(" ");
	jQuery("td:eq(9)",jQuery("#formTableplanDetail tr")).hide();
	table.find('input[name="field_planDetail_7"]').val(" ");
	for(var i=0;i<trList.length;i++){
		var plan='';
		plan=jQuery(trList[i]).find('select[name="Plan_id"]').val();
		if(plan==null || plan==""){
			plan=jQuery(trList[i]).find('input[name="Plan_id"]').val();
		}
		var j=parseInt(plan)+4
		jQuery("td:eq("+j+")",jQuery("#formTableplanDetail tr")).show();
		/*if(plan=="001"){
			jQuery("td:eq(5)",jQuery("#formTableplanDetail tr")).show();
		}else if(plan=="002"){
			jQuery("td:eq(6)",jQuery("#formTableplanDetail tr")).show();
		}*/
	}
	
	if(jQuery("td:eq(5)",jQuery("#formTableplanDetail tr"))[0].style.display== "none"){
		jQuery('#formTableplanDetail').find('input[name="field_planDetail_3"]').val("");
	}
	if(jQuery("td:eq(6)",jQuery("#formTableplanDetail tr"))[0].style.display== "none"){
		jQuery('#formTableplanDetail').find('input[name="field_planDetail_4"]').val("");
	}
	if(jQuery("td:eq(7)",jQuery("#formTableplanDetail tr"))[0].style.display== "none"){
		jQuery('#formTableplanDetail').find('input[name="field_planDetail_5"]').val("");
	}
	if(jQuery("td:eq(8)",jQuery("#formTableplanDetail tr"))[0].style.display== "none"){
		jQuery('#formTableplanDetail').find('input[name="field_planDetail_6"]').val("");
	}
	if(jQuery("td:eq(9)",jQuery("#formTableplanDetail tr"))[0].style.display== "none"){
		jQuery('#formTableplanDetail').find('input[name="field_planDetail_7"]').val("");
	}
}

function addProductPlans(prodCodes){
	var request_no=jQuery('input[name="request_no"]').val();
	delAllRow3("formTablePlanDetail");
	
	var formSystemId3=jQuery("input[name='formSystemId'][type='hidden']").val();
	//jQuery("td:eq(4)",jQuery("#formTablePlanDetail tr")).hide();
	var sqlStr="select a.*,b.*  from TNOVAPRODUCTLIMIT a "+
				"left join teflow_"+formSystemId3+"_planDetail b on a.PRODCODE=b.PRODUCT_CODE and a.LIMITCODE=b.LIMIT_CODE  and b.request_no='"+request_no+"'"+
				"where a.FORM_SYSTEM_ID='"+formSystemId3+"' and a.PRODCODE in ("+prodCodes+")";	
	var data = selectFromSQLJson('',sqlStr);
	if(data!=null){
		for(var i=0;i<data.length;i++){
			createTableSectionRow2("formTableplanDetail","planDetail","0");
			var trItem=jQuery("#formTableplanDetail").find("tr:visible:last");
			trItem.find('textArea[name="field_planDetail_1"]').val(data[i].PRODDESC);
			trItem.find('textArea[name="field_planDetail_2"]').val(data[i].LIMITDESC);
			trItem.find('input[name="field_planDetail_3"]').val(data[i].FIELD_PLANDETAIL_3);
			trItem.find('input[name="field_planDetail_4"]').val(data[i].FIELD_PLANDETAIL_4);
			trItem.find('input[name="field_planDetail_5"]').val(data[i].FIELD_PLANDETAIL_4);
			trItem.find('input[name="field_planDetail_6"]').val(data[i].FIELD_PLANDETAIL_4);
			trItem.find('input[name="field_planDetail_7"]').val(data[i].FIELD_PLANDETAIL_4);
			/*************************/
			//trItem.find('select[name="PRODUCT_CODE"]').val(data[i].PRODCODE);
			trItem.find('select[name="PRODUCT_CODE"]').combobox("setValue",data[i].PRODCODE);
			//trItem.find('select[name="LIMIT_CODE"]').val(data[i].LIMITCODE);
			trItem.find('select[name="LIMIT_CODE"]').combobox("setValue",data[i].LIMITCODE);
		}
	}
}

function removeCommas(nStr){     
	nStr += '';   
	var re = /,/g; 
	nStr = nStr.replace(re,'');
	return nStr;
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

function formatPercent(item){
	//if(20)
}

