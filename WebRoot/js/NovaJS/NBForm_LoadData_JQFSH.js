
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

/*jQuery(document).ready(function() {  //move to the end of this file by Colin;
	SpecialHandle();
});*/

function productChange(selectedID,isLoad){
	var tr = jQuery(selectedID).closest('tr');
	var index = jQuery("#formTable4 tr").index(jQuery(selectedID).closest('tr'));	

	//jQuery(selectedID).parent().find("input[type='hidden'][name='Product_id']").val(jQuery(selectedID).val());

	bindCompassPlanProduct(selectedID, false,isLoad);
	
	if(jQuery("#formTable4 tr").length>2 && index==1)
	{
		var productItem = jQuery("#formTable4 select[name='Product_id']");
		productItem.each(function(i){
			if(i>0)
			{
				loadNProduct(this,true);
				if(jQuery(this).val()=="")
					//jQuery(this).val(jQuery(selectedID).val());
					jQuery(this).combobox("setValue",jQuery(selectedID).val());
				bindCompassPlanProduct(this ,true,isLoad);
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

function bindCompassPlanProduct(selectedID, bool, isLoad){
	
	var tr = jQuery(selectedID).closest('tr');
	var selectedVal = jQuery(selectedID).val();
	
	if(selectedVal!="")
	{
		var sql = "select distinct PRODCODE as PRODCODE from TNOVAProductMapping where NOVA_PRODCODE = '" +selectedVal+ "'";
		var reSelected, reSelected2;
		
		var data = selectFromSQLJson('',sql);
		
		var prodCode = '';
		if(data.length>0)
		{	
			prodCode = data[0].PRODCODE;
			reSelected = tr.find('select[name="field_4_6"]');
			reSelected2 = tr.find('select[name="Plan_id"]');
			var value1 = reSelected2.val();
			//alert(1);
			
			loadPlan(reSelected2,true)
			
			reSelected2.val(value1);
			
			if(reSelected.length>0)
				reSelected.val(prodCode);
		}
		
		if(bool)
		{
			var memProduct = jQuery("select[name='MemProduct_id']");
	
			if(memProduct.length>0){
				jQuery.each(memProduct,function(i,item) {
					loadMemProduct(item,true,false,isLoad);
				});
			}
		}
	}
}

function planChange(selectedID){
	var tr = jQuery(selectedID).closest('tr');
	
	tr.find('select[name="field_4_7"]').val("");
	
	var selectedVal = jQuery(selectedID).val();
	var productVal = tr.find('select[name="Product_id"]').val();
	
	
	var memProduct2 = jQuery("select[name='MemProduct_id']");
	
	if(memProduct2.length>0){
		jQuery.each(memProduct2,function(i,item) {
			loadMemProduct(item,true,true,true);
		});
	}
	
	if(selectedVal!="")
	{
		var sql = "select distinct BENPLNCD as BENPLNCD from TNOVAProductMapping where NOVA_PRODCODE = '" +productVal+ "'"+" AND NOVA_BENPLNCD = '" +selectedVal+ "'";
		var reSelected, reSelected2;
		var data = selectFromSQLJson('',sql);
		var BenplnCD = '';

		if(data.length>0)
		{	
			BenplnCD = data[0].BENPLNCD;
			reSelected = tr.find('select[name="field_4_7"]');
			
			if(reSelected)
			{
				reSelected.trigger("focus");
				reSelected.val(BenplnCD);
			}
		}
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
			//NOVA-66-20150309 Hinson BEGIN
//			jQuery(selectedID).append("<option value='null'></option>");
			jQuery(selectedID).append("<option value=''></option>");
			//NOVA-66-20150309 Hinson END
			
			jQuery.each(data,function(i,item) {
				value = item.NOVA_BENPLNCD;
				text = item.NOVA_BENPLNCD_LOCAL;
				jQuery(selectedID).append("<option value='"+value+"'>"+text+"</option>");  
			});
			
			jQuery(selectedID).combobox("rebindData");
		}
		if(selectedVal!='')
			//jQuery(selectedID).val(selectedVal);
			jQuery(selectedID).combobox("setValue",selectedVal);
		else
			jQuery(selectedID).find("option:first-child").attr("selected", true);
		
		//jQuery(selectedID).trigger("onchange");
	}
}

function loadCPlan(selectedID){
	
	var selectedVal = jQuery(selectedID).val();
	
	var productVal = jQuery(selectedID).closest('tr').find('select[name="field_4_6"]').val();
	
	var sql = "select distinct BENPLNCD,BENPLNCD_Local from TNOVAProductMapping where NOVA_PRODCODE='"+ productVal +"'";
	
	var xmlObj = selectFromSQL(sql,2);

	if(selectedVal=='')
	{
		var prodCode = '';
		if(xmlObj){
			var item = jQuery(xmlObj).find("item");
			if(item.length>0)
			{	
				jQuery(selectedID).empty();
				//NOVA-66-20150309 Hinson BEGIN
//				jQuery(selectedID).append("<option value='null'></option>"); 
				jQuery(selectedID).append("<option value=''></option>");
				//NOVA-66-20150309 Hinson END
				
				item.each(function(i){
					value = item.eq(i).find('BENPLNCD').text();
					text = item.eq(i).find('BENPLNCD_LOCAL').text();
					jQuery(selectedID).append("<option value='"+value+"'>"+text+"</option>");   
				})
			}
		}
		jQuery(selectedID).find("option:first-child").attr("selected", true);
		jQuery(selectedID).combobox("rebindData");
	}
	else
		//jQuery(selectedID).val(selectedVal);
		jQuery(selectedID).combobox("setValue",selectedVal);
}

function loadCProduct(selectedID){

	var selectedVal = jQuery(selectedID).val();
	
	var productVal = jQuery(selectedID).closest('tr').find('select[name="field_4_7"]');
	
	var sql = "select distinct PRODCODE,PRODCODE_Local from TNovaProductMapping where NOVA_PRODCODE='"+ productVal +"'";
	
	var xmlObj = selectFromSQL(sql,2);

	if(selectedVal=='')
	{
		var prodCode = '';
		if(xmlObj){
			var item = jQuery(xmlObj).find("item");
			if(item.length>0)
			{	
				jQuery(selectedID).empty();
				//NOVA-66-20150309 Hinson BEGIN
//				jQuery(selectedID).append("<option value='null'></option>");
				jQuery(selectedID).append("<option value=''></option>");
				//NOVA-66-20150309 Hinson END
				item.each(function(i){
					value = item.eq(i).find('NOVA_BENPLNCD').text();
					text = item.eq(i).find('NOVA_BENPLNCD_LOCAL').text();
					jQuery(selectedID).append("<option value='"+value+"'>"+text+"</option>");   
				})
				
				jQuery(selectedID).combobox("rebindData");
			}
		}
		
		jQuery(selectedID).find("option:first-child").attr("selected", true);
	}
	else
		//jQuery(selectedID).val(selectedVal);
		jQuery(selectedID).combobox("setValue",selectedVal);
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
			//NOVA-66-20150309 Hinson BEGIN
//				jQuery(selectedID).append("<option value='null'></option>");
			jQuery(selectedID).append("<option value=''></option>");
			//NOVA-66-20150309 Hinson END
			jQuery.each(jsonObj,function(i,item){
				jQuery(selectedID).append("<option value='"+item.NOVA_PRODCODE+"'>"+item.NOVA_PRODCODE_LOCAL+"</option>");   
			});
			
			jQuery(selectedID).combobox("rebindData");
		}
		
		if(selectedVal!='')
			//jQuery(selectedID).val(selectedVal);
			jQuery(selectedID).combobox("setValue",selectedVal);
		else
			jQuery(selectedID).find("option:first-child").attr("selected", true);

	}
}

function loadMemProduct(selectedID,srcChange,strShow,isLoad){
	var selected = jQuery("#formTable4 tr td select[name='Product_id']");
	var selectedVal = jQuery(selectedID).val();
	var options = [];
	
	if(selectedVal==''||srcChange)
	{
		jQuery(selectedID).empty();
		//NOVA-66-20150309 Hinson BEGIN
//				jQuery(selectedID).append("<option value='null'></option>"); 
		jQuery(selectedID).append("<option value=''></option>");
		//NOVA-66-20150309 Hinson END
		
		jQuery.each(selected,function(i,item){
			value = jQuery(item).find("option:selected").val();
			text = jQuery(item).find("option:selected").text();
			
			options.push("<option value='"+value+"'>"+text+"</option>");
		});
		
		options = uniQueue(options);
		
		jQuery(selectedID).append(options);
		
		jQuery(selectedID).combobox("rebindData");
		
		if(selectedVal!='')
			//jQuery(selectedID).val(selectedVal);
			jQuery(selectedID).combobox("setValue",selectedVal);
		else
			jQuery(selectedID).find("option:first-child").attr("selected", true);
		
		//jQuery(selectedID).trigger("onchange");
		changeMemProduct(selectedID,strShow,isLoad);
	}
}

function changeMemProduct(selectedID,srcChange,isLoad){

	var selectedVal = jQuery(selectedID).val();
	
	var curSelectPlan = jQuery(selectedID).closest('tr').find('select[name="MemPlan_id"]');
	var selectedPlanVal2 = curSelectPlan.val();
	
	loadMemPlan(selectedVal,curSelectPlan);
	
	//jQuery(selectedID).val(selectedVal);
	jQuery(selectedID).combobox("setValue",selectedVal);
	
	//NOVA-66-20150309 Hinson BEGIN
//		if(selectedPlanVal2!='null')
	if(selectedPlanVal2!='')
	//NOVA-66-20150309 Hinson END
		//curSelectPlan.val(selectedPlanVal2);
		curSelectPlan.combobox("setValue",selectedPlanVal2);
	
	selectSHIP = jQuery(selectedID).closest('tr').find('select[name="field_9_13"]');
	
	selectSHIP.removeAttr('disabled');
	
	if(srcChange)
	{
		changeMemPlan(curSelectPlan[0],isLoad);
		
		var memIDField = jQuery(selectedID).closest('tr').find("textarea[name='field_9_7']");
		
		if(memIDField.length>0)
		{
			//alert(1);
			RemoveCheckingMsg(memIDField[0]);
			CheckDuplicate(memIDField[0]);
		}
	}
}

function loadMemPlan(selectedVal,curSelectPlan){
	
	var selectedProduct = jQuery("#formTable4 select[name='Product_id']");
	var options = [];
	curSelectPlan.empty();
	
	//NOVA-68 hinson begin: default value should be blank instead of 'null'
//		curSelectPlan.append("<option value='null'></option>"); 
	curSelectPlan.append("<option value=''></option>"); 
	//NOVA-68 hinson end
	var selectedPlanVal,selectedPlan;
	
	jQuery.each(selectedProduct,function(i,item){
		selectedPlanVal = jQuery(item).find("option:selected").val();
		
		if(selectedPlanVal == selectedVal)
		{
			selectedPlan = jQuery(item).closest('tr').find("select[name='Plan_id']");
			
			value = selectedPlan.val();
			text = selectedPlan.find("option:selected").text();
			
			options.push("<option value='"+value+"'>"+text+"</option>");
		}
	});
	
	options = uniQueue(options);
	curSelectPlan.append(options);
	
	curSelectPlan.combobox("rebindData");
}

function changeMemPlan(selectedID,isLoad){
	
	var dobField = jQuery(selectedID).closest('tr').find('input[name="field_9_9"]');
	if(dobField.length>0)
		calculatePremium(dobField[0],isLoad);
	
	var shipField = jQuery(selectedID).closest('tr').find('select[name="field_9_13"]');

	shipField.trigger('change');
	
	countPeopleByPlan(); //20150323 NOVA 105 Justin Bin Added
}

function changeMemSHIP(selectedID){

	//NOVA-214
	var formSystemId2 = jQuery("input[name='formSystemId'][type='hidden']").val();
	
	if(formSystemId2 == "30322"){
	    return;
	}

	var productVal = jQuery(selectedID).closest('tr').find('select[name="MemProduct_id"]').val();
	var planVal = jQuery(selectedID).closest('tr').find('select[name="MemPlan_id"]').val();
	var sql = "select SHIP from TNovaProductMapping where NOVA_PRODCODE = '"+ productVal +"' and NOVA_BENPLNCD = '" + planVal + "'";

	var data = selectFromSQLJson('',sql);
	
	var selectedTR = jQuery(selectedID).closest('tr');
	var selectedTD = jQuery(selectedID).closest('td');
	
	var statusField = jQuery(selectedID).closest('tr').find('textarea[name="field_9_19"]');
	
	if(statusField.val() == "")
	{
		selectedTR.css('background-color','rgb(235, 235, 233)');
		selectedTD.css('background-color','rgb(235, 235, 233)');
	}
	else
	{
		selectedTR.css('background-color','#F8FEC3');
		selectedTD.css('background-color','#F8FEC3');
	}
	
	if(data.length>0){
		var value = data[0].SHIP;
		
		var selectSHIP = jQuery(selectedID).closest('tr').find('select[name="field_9_13"]').val();
		
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

function addProductPlanControl(){
	var table = jQuery('#formTable4');
	var formSystemId2 = jQuery("input[name='formSystemId'][type='hidden']").val();
	
	if(formSystemId2 == "20264" ||formSystemId2 == "30286" || formSystemId2 == "30288" || formSystemId2 == "20261")
	{
		jQuery("td:eq(3)",jQuery("#formTable4 tr")).hide();   
		jQuery("td:eq(4)",jQuery("#formTable4 tr")).hide(); 
		
		jQuery("#div5 input[name='addRowBtn'][type='button']").click(function(){
			
			jQuery("td:eq(3)",jQuery("#formTable4 tr")).hide();   
			jQuery("td:eq(4)",jQuery("#formTable4 tr")).hide();   
			  
			if(table.find('tr').length>2){
		        var tr1 = table.find('tr:eq(1)');
		        var sel1 = tr1.find('select:first').find('option:selected');
		        
		        var selObj = table.find('tr:last').find('select:first');
		        selObj.trigger("focus");
		        //selObj.val(sel1.val());
		        selObj.combobox("setValue",sel1.val());
		        selObj.trigger("change");
		        
		        var selObj2 = table.find('tr:last').find('select:eq(2)');
		        var selObj3 = table.find('tr:last').find('select:eq(3)');
		    }
		});
		
		var saveType = jQuery('input[type="hidden"][name="saveType"]');
		
		
		if(saveType != "new")
		{
			if(table.find('tr').length>2)
			{
				table.find('tr:gt(0)').each(function(i){
					
					var select = jQuery(this).find("select:eq(0)");
					loadNProduct(select[0],true);
					
					productChange(select[0],false);
					
					select = jQuery(this).find("select:eq(1)");
					loadPlan(select[0],true);
					
					select = jQuery(this).find("select:eq(2)");
					select.trigger("focus");
					select.trigger("change");
					
					select = jQuery(this).find("select:eq(3)");
					select.trigger("focus");
					select.trigger("change");
				});
			}
		}
	}
}

addProductPlanControl();

function SpecialHandle(){
	var operateType = jQuery('input[name="operateType"][type="hidden"]');
	var saveType = jQuery('input[name="saveType"][type="hidden"]');
	if(operateType.length>0)
	{
		if(operateType.val()=="deal" || (operateType.val()=="view" && saveType.length==0))
		{
			var productID,planID,sql,data,parentTD
			
			jQuery('#formTable4 tr').each(function(i){
				if(i>0)
				{
					productID = jQuery(this).find('input[name="Product_id"][type="hidden"]').val();
					planID = jQuery(this).find('input[name="Plan_id"][type="hidden"]').val();
					
					sql = "select NOVA_BENPLNCD_Local as TEXT,NOVA_PRODCODE_Local as TEXTPro from TNovaProductMapping where NOVA_BENPLNCD = '"
						+ planID + "' and NOVA_PRODCODE = '"
						+ productID + "'";
					
					data = selectFromSQLJson('',sql);
					
					if(data.length>0)
					{
						parentTD = jQuery(this).find('input[name="Plan_id"][type="hidden"]').parent();
						parentTD.text(data[0].TEXT);
						
						//20150408 Justin Bin added
						parentTD = jQuery(this).find('input[name="Product_id"][type="hidden"]').parent()
						parentTD.text(data[0].TEXTPRO)
						//20150408 Justin Bin Added end
					}
				}
			});
			
			jQuery('#formTable9 tr').each(function(i){
				if(i>0)
				{
					productID = jQuery(this).find('input[name="MemProduct_id"][type="hidden"]').val();
					planID = jQuery(this).find('input[name="MemPlan_id"][type="hidden"]').val();
					
					sql = "select NOVA_BENPLNCD_Local as TEXT,NOVA_PRODCODE_Local as TEXTPro from TNovaProductMapping where NOVA_BENPLNCD = '"
						+ planID + "' and NOVA_PRODCODE = '"
						+ productID + "'";
					
					data = selectFromSQLJson('',sql);
					
					if(data.length>0)
					{
						parentTD = jQuery(this).find('input[name="MemPlan_id"][type="hidden"]').parent();
						parentTD.text(data[0].TEXT);
						
						//20150408 Justin Bin added
						parentTD = jQuery(this).find('input[name="MemProduct_id"][type="hidden"]').parent()
						parentTD.text(data[0].TEXTPRO)
						//20150408 Justin Bin added end
					}
				}
			});
		}
		else
			InitialProductPlan();
	}
}

function InitialProductPlan(){
	if(jQuery('#formTable4 tr').length>1){
		jQuery('#formTable4 tr').each(function(i){
			if(i>0){
				jQuery(this).find('select[name="Product_id"]').trigger("focus");
				productChange(jQuery(this).find('select[name="Product_id"]'),false);
			}
		});
		
		var memProduct = jQuery("select[name='MemProduct_id']");
		
		if(memProduct.length>0){
			jQuery.each(memProduct,function(i,item) {
				loadMemProduct(item,true,true,true);
			});
		}
	}
}

function cleanMenPaln(){
	var table = jQuery('#formTable9');
	for(var i=0; i<table.find('tr').length; i++){
		var tr = table.find('tr:eq('+i+')');
		
		tr.find('select[name="MemPlan_id"]').combobox("setValue",'')
		
		tr.find('select[name="MemPlan_id"]').trigger('change');
	}	
}

function triggerPlanColumn(){

	//jQuery('#formTable12 tr').find('td:contains("�ƻ�")').hide();
	//jQuery('#formTable12 tr').find('td:eq(0)').hide();
	jQuery('#formTable12 tr').find('td:eq(0)').hide();
	jQuery('#formTable12 tr').find('td:eq(2)').hide();
	jQuery('#formTable12 tr').find('td:eq(4)').hide();
	
	jQuery('#formTable12 tr').find('td:eq(5)').hide();
	jQuery('#formTable12 tr').find('td:eq(6)').hide();
	jQuery('#formTable12 tr').find('td:eq(7)').hide();
	jQuery('#formTable12 tr').find('td:eq(8)').hide();
	jQuery('#formTable12 tr').find('td:eq(9)').hide();
	if(jQuery("#formTable4 tr").length >= 2){
		var planItem = jQuery("#formTable4 select[name='Plan_id']");	
//		planItem.each(function(i){
			if(jQuery("#formTable4 tr").length == 2){
				jQuery('#formTable12 tr').find('td:eq(5)').show();
			}
			
			if(jQuery("#formTable4 tr").length == 3){
				jQuery('#formTable12 tr').find('td:eq(5)').show();
				jQuery('#formTable12 tr').find('td:eq(6)').show();				
			}			

			if(jQuery("#formTable4 tr").length == 4){
				jQuery('#formTable12 tr').find('td:eq(5)').show();
				jQuery('#formTable12 tr').find('td:eq(6)').show();
				jQuery('#formTable12 tr').find('td:eq(7)').show();				
			}
			
			if(jQuery("#formTable4 tr").length == 5){
				jQuery('#formTable12 tr').find('td:eq(5)').show();
				jQuery('#formTable12 tr').find('td:eq(6)').show();
				jQuery('#formTable12 tr').find('td:eq(7)').show();
				jQuery('#formTable12 tr').find('td:eq(8)').show();				
			}			
			
			if(jQuery("#formTable4 tr").length == 6){
				jQuery('#formTable12 tr').find('td:eq(5)').show();			
				jQuery('#formTable12 tr').find('td:eq(6)').show();
				jQuery('#formTable12 tr').find('td:eq(7)').show();
				jQuery('#formTable12 tr').find('td:eq(8)').show();
				jQuery('#formTable12 tr').find('td:eq(9)').show();				
			}			
			
//				if(jQuery(this).val()=="001"){
//					jQuery('#formTable12 tr').find('td:eq(5)').show();
//				}
//				else if(jQuery(this).val()=="002"){
//					jQuery('#formTable12 tr').find('td:eq(6)').show();
//				}
//				else if(jQuery(this).val()=="003"){
//					jQuery('#formTable12 tr').find('td:eq(7)').show();
//					}
//				else if(jQuery(this).val()=="004"){
//					jQuery('#formTable12 tr').find('td:eq(8)').show();
//					}
//				else if(jQuery(this).val()=="005"){
//					jQuery('#formTable12 tr').find('td:eq(9)').show();
//					}
				
//    });	
		
	}	
	
}

function fillPlanColumn(selectedID){

	//get the current row index to determine which column to apply
	var tr = jQuery(selectedID).closest('tr');
	var index = jQuery("#formTable4 tr").index(jQuery(selectedID).closest('tr'));

    //get the current value of product id & plan id	
	var productVal = tr.find('select[name="Product_id"]').val();
	var planVal    = tr.find('select[name="Plan_id"]').val();	
	
	//get the default benefit for JQFSH package 01 / 02
	//skip if it is not JQFS1 / JQFS2
	if(productVal =='JQFS1'){		
		aResultA = getPkgBenefitJQFSH_01(productVal, planVal);
	}else if(productVal =='JQFS2'){
		aResultA = getPkgBenefitJQFSH_02(productVal, planVal);
	}else{
		return;
	}
	
	var aResult;

	var iAmt201C1 = 0;
	var iAmt211D1 = 0;
	var iAmt211S5 = 0;	
	var iAmt211D2 = 0;
	var iAmt311S3 = 0;
	var iAmt214S1 = 0;
	var iCnt301C2_HS    = 0;
	var iCnt301C2_RB    = 0;
	var iCnt301C2_OHS   = 0;
	var iCnt301C2_DRUGS = 0;
	var iCnt301S1 = 0;	
		
	iAmt201C1 = parseInt(aResultA[0]);
	iAmt211D1 = parseInt(aResultA[1]);
	iAmt211S5 = parseInt(aResultA[2]);
	iAmt211D2 = parseInt(aResultA[3]);
	iAmt311S3 = parseInt(aResultA[4]);
	iAmt214S1 = parseInt(aResultA[5]);
	iCnt301C2_HS    = parseInt(aResultA[6]);
	iCnt301C2_RB    = parseInt(aResultA[7]);
	iCnt301C2_OHS   = parseInt(aResultA[8]);
	iCnt301C2_DRUGS = parseInt(aResultA[9]);
	iCnt301S1 = parseInt(aResultA[10]);	
	
	//make the return value into field_12_1~5
	fillPkgBenefitJQFSH(index, productVal, iAmt201C1, iAmt211D1, iAmt211S5, iAmt211D2, iAmt311S3, iAmt214S1, iCnt301C2_HS , iCnt301C2_RB , iCnt301C2_OHS, iCnt301C2_DRUGS, iCnt301S1 );	        	
		
}

function getPkgBenefitJQFSH_01(prod_name, plan_name){
	//this paragraph is to get the benefit amount based on input product & plan name (JQFS1)
	var o1, o2, o3, o4, o5, o6, o7_1, o7_2, o7_3, o7_4, o8;
	o1 = o2 = o3 = o4 = o5 = o6 = o7_1 = o7_2 = o7_3 = o7_4 = o8 = 0;
	
	if (prod_name = 'JQFS1'){
		
		switch(plan_name) {
		    case '001': 
		    {
		    	o2 = 100000;
		    	o3 = 10000;
		    	o5 = 40;
		        break;
		    }
		    case '002': 
		    {
		    	o2 = 200000;
		    	o3 = 20000;
		    	o5 = 40;
		        break;
		    }
		    case '003': 
		    {
		    	o2 = 300000;
		    	o3 = 20000;
		    	o5 = 60;
		        break;
		    }
		    case '004': 
		    {
		    	o2 = 400000;
		    	o3 = 30000;
		    	o5 = 60;
		        break;
		    }
		    case '005': 
		    {
		    	o2 = 500000;
		    	o3 = 30000;
		    	o5 = 60;
		        break;
		    }
		    case '006': 
		    {
		    	o2 = 600000;
		    	o3 = 30000;
		    	o5 = 60;
		        break;
		    }
		    case '007': 
		    {
		    	o2 = 100000
		    	o3 = 10000;
		    	o5 = 40;
		        break;
		    }
		    case '008': 
		    {
		    	o2 = 200000;
		    	o3 = 20000;
		    	o5 = 40;
		        break;
		    }
		    case '009': 
		    {
		    	o2 = 300000;
		    	o3 = 20000;
		    	o5 = 60;
		        break;
		    }
		    case '010': 
		    {
		    	o2 = 400000;
		    	o3 = 20000;
		    	o5 = 60;
		        break;
		    }	    
		    default:
		    {
		    	o1 = 0;
		    	o2 = 0;
		    	o3 = 0;
		    	o4 = 0;
		    	o5 = 0;
		    	o6 = 0;
		    	o7 = 0;
		    	o8 = 0;
		        break;
		    }
		}				
	} 	
	return [o1, o2, o3, o4, o5, o6, o7_1, o7_2, o7_3, o7_4, o8];
}

function getPkgBenefitJQFSH_02(prod_name, plan_name){
	//this paragraph is to get the benefit amount based on input product & plan name (JQFS2)	
	var o1, o2, o3, o4, o5, o6, o7_1, o7_2, o7_3, o7_4, o8;
	o1 = o2 = o3 = o4 = o5 = o6 = o7_1 = o7_2 = o7_3 = o7_4 = o8 = 0;
	
	if (prod_name = 'JQFS2'){
		
		switch(plan_name) {
		    case '001': 
		    {
		    	o2   = 100000;
		    	o3   = 10000;
		    	o5   = 40;
		    	o7_1 = 30;
		    	o7_2 = 1500;
		    	o7_3 = 1000;
		    	o7_4 = 1000;
		        break;
		    }
		    case '002': 
		    {
		    	o2   = 200000;
		    	o3   = 20000;
		    	o5   = 40;
		    	o7_1 = 60;
		    	o7_2 = 3000;
		    	o7_3 = 2000;
		    	o7_4 = 2000;
		        break;
		    }
		    case '003': 
		    {
		    	o2   = 300000;
		    	o3   = 20000;
		    	o5   = 40;
		    	o7_1 = 90;
		    	o7_2 = 4500;
		    	o7_3 = 3000;
		    	o7_4 = 3000;
		        break;
		    }
		    case '004': 
		    {
		    	o2   = 400000;
		    	o3   = 30000;
		    	o5   = 60;
		    	o7_1 = 120;
		    	o7_2 = 6000;
		    	o7_3 = 4000;
		    	o7_4 = 4000;
		        break;
		    }
		    case '005': 
		    {
		    	o2   = 500000;
		    	o3   = 30000;
		    	o5   = 60;
		    	o7_1 = 150;
		    	o7_2 = 7500;
		    	o7_3 = 5000;
		    	o7_4 = 5000;
		        break;
		    }
		    case '006': 
		    {
		    	o2   = 600000;
		    	o3   = 30000;
		    	o5   = 60;
		    	o7_1 = 180;
		    	o7_2 = 9000;
		    	o7_3 = 6000;
		    	o7_4 = 6000;
		        break;
		    }
		    case '007': 
		    {
		    	o2   = 100000
		    	o3   = 10000;
		    	o5   = 40;
		    	o7_1 = 30;
		    	o7_2 = 1500;
		    	o7_3 = 1000;
		    	o7_4 = 1000;		    	
		        break;
		    }
		    case '008': 
		    {
		    	o2   = 200000;
		    	o3   = 20000;
		    	o5   = 40;
		    	o7_1 = 60;
		    	o7_2 = 3000;
		    	o7_3 = 2000;
		    	o7_4 = 2000;
		        break;
		    }
		    case '009': 
		    {
		    	o2   = 300000;
		    	o3   = 20000;
		    	o5   = 60;
		    	o7_1 = 90;
		    	o7_2 = 4500;
		    	o7_3 = 3000;
		    	o7_4 = 3000;
		        break;
		    }
		    case '010': 
		    {
		    	o2   = 400000;
		    	o3   = 20000;
		    	o5   = 60;
		    	o7_1 = 120;
		    	o7_2 = 6000;
		    	o7_3 = 4000;
		    	o7_4 = 4000;
		        break;
		    }	    
		    default:
		    {		    	
		        break;
		    }
		}				
	} 	
	return [o1, o2, o3, o4, o5, o6, o7_1, o7_2, o7_3, o7_4, o8];
}

function fillPkgBenefitJQFSH(idx, prod_name, o1, o2, o3, o4, o5, o6, o7_1, o7_2, o7_3, o7_4, o8){
	
	if(prod_name != 'JQFS1' && prod_name != 'JQFS2'){
		return;
	}
	
	if (o1.toString() == "0"){
		o1 = "";
	}
	if (o2.toString() == "0"){
		o2 = "";
	}
	if (o3.toString() == "0"){
		o3 = "";
	}
	if (o4.toString() == "0"){
		o4 = "";
	}
	if (o5.toString() == "0"){
		o5 = "";
	}
	if (o6.toString() == "0"){
		o6 = "";
	}
	if (o7_1.toString() == "0"){
		o7_1 = "";
	}
	if (o7_2.toString() == "0"){
		o7_2 = "";
	}
	if (o7_3.toString() == "0"){
		o7_3 = "";
	}
	if (o7_4.toString() == "0"){
		o7_4 = "";
	}
	if (o8.toString() == "0"){
		o8 = "";
	}
	
	switch(idx.toString()) {
	    case '1': 
	    {
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[0].value = o1;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[1].value = o2;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[2].value = o3;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[3].value = o4;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[4].value = o5;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[5].value = o6;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[6].value = o7_1;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[7].value = o7_2;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[8].value = o7_3;        
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[9].value = o7_4;
	    	jQuery('#formTable12 tr').find('td:eq(5) textarea')[10].value = o8;
	    	
	        break;
	    }
	    case '2': 
	    {
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[0].value = o1;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[1].value = o2;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[2].value = o3;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[3].value = o4;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[4].value = o5;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[5].value = o6;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[6].value = o7_1;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[7].value = o7_2;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[8].value = o7_3;        
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[9].value = o7_4;
	    	jQuery('#formTable12 tr').find('td:eq(6) textarea')[10].value = o8;
	    	
	        break;
	    }
	    case '3': 
	    {
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[0].value = o1;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[1].value = o2;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[2].value = o3;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[3].value = o4;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[4].value = o5;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[5].value = o6;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[6].value = o7_1;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[7].value = o7_2;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[8].value = o7_3;        
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[9].value = o7_4;
	    	jQuery('#formTable12 tr').find('td:eq(7) textarea')[10].value = o8;
	    	
	        break;
	    }
	    case '4': 
	    {	    	
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[0].value = o1;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[1].value = o2;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[2].value = o3;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[3].value = o4;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[4].value = o5;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[5].value = o6;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[6].value = o7_1;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[7].value = o7_2;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[8].value = o7_3;        
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[9].value = o7_4;
	    	jQuery('#formTable12 tr').find('td:eq(8) textarea')[10].value = o8;
	    	
	        break;
	    }
	    case '5': 
	    {
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[0].value = o1;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[1].value = o2;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[2].value = o3;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[3].value = o4;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[4].value = o5;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[5].value = o6;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[6].value = o7_1;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[7].value = o7_2;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[8].value = o7_3;        
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[9].value = o7_4;
	    	jQuery('#formTable12 tr').find('td:eq(9) textarea')[10].value = o8;
	    	
	        break;
	    }	    
	}	
	
}

function initProductPlan(){

	if(jQuery('#formTable12 tr').length <= 1){
			
		createTableSectionRow("formTable12","12","0");
		jQuery('#formTable12 tr:eq(1)').find('select[name="PRODUCT_CODE"] option:eq(0)').attr('selected','selected');
		jQuery('#formTable12 tr:eq(1)').find('select[name="LIMIT_CODE"] option:eq(0)').attr('selected','selected');
	
		jQuery('#formTable12 tr:eq(1)').find('td:eq(1) textarea')[0].value = jQuery('#formTable12 tr:eq(1)').find('select[name="PRODUCT_CODE"] option:eq(0)').text();
		jQuery('#formTable12 tr:eq(1)').find('td:eq(3) textarea')[0].value = jQuery('#formTable12 tr:eq(1)').find('select[name="LIMIT_CODE"] option:eq(0)').text();
		
		jQuery('#formTable12 tr:eq(1)').find('select[name="PRODUCT_CODE"]').attr("readonly","readonly");
		jQuery('#formTable12 tr:eq(1)').find('select[name="LIMIT_CODE"]').attr("readonly","readonly");
		//jQuery('#formTable12 tr:eq(1)').find('textarea').change(cleanMenPaln);
		
		/*if("SA" == jQuery('#formTable12 tr:eq(1)').find('select[name="LIMIT_CODE"] option:eq(0)').val()){
			jQuery('#formTable12 tr:eq(1)').find('textarea').each(
					function(){
						jQuery(this).bind("change",jQuery(this).val(),changeCommasValue)
					});
		}*/
		
		var productOptions = jQuery('#formTable12 tr:eq(1)').find('select[name="PRODUCT_CODE"] option');
		for(var i=1;i<productOptions.length-1;i++){
			//jQuery("#div6 input[name='addRowBtn'][type='button']").trigger('click');
			createTableSectionRow("formTable12","12","0");
			var j=i+1;
			jQuery('#formTable12 tr:eq('+j+')').find('select[name="PRODUCT_CODE"] option:eq('+i+')').attr('selected','selected');
			jQuery('#formTable12 tr:eq('+j+')').find('select[name="LIMIT_CODE"] option:eq('+i+')').attr('selected','selected');
	
			jQuery('#formTable12 tr:eq('+j+')').find('td:eq(1) textarea')[0].value = jQuery('#formTable12 tr:eq('+j+')').find('select[name="PRODUCT_CODE"] option:eq('+i+')').text();
			jQuery('#formTable12 tr:eq('+j+')').find('td:eq(3) textarea')[0].value = jQuery('#formTable12 tr:eq('+j+')').find('select[name="LIMIT_CODE"] option:eq('+i+')').text();
			
			jQuery('#formTable12 tr:eq('+j+')').find('select[name="PRODUCT_CODE"]').attr("readonly","readonly");
			jQuery('#formTable12 tr:eq('+j+')').find('select[name="LIMIT_CODE"]').attr("readonly","readonly");
			//jQuery('#formTable12 tr:eq('+j+') textarea').change(cleanMenPaln);
		}
	
	}	
	
	jQuery("#div6 input[name='addRowBtn'][type='button']").hide();
	jQuery("#div6 input[name='deleteRowBtn'][type='button']").hide();
	jQuery('#formTable12 tr').find('textarea').change(cleanMenPaln);
}

jQuery(document).ready(
	function(){

		var formSystemId2 = jQuery("input[name='formSystemId'][type='hidden']").val();		
		if(formSystemId2 == "30322"){
			jQuery("#div5 input[name='deleteRowBtn'][type='button']").click(triggerPlanColumn);
			initProductPlan();
			triggerPlanColumn();
			SpecialHandle();
		}
			
	}		
)




