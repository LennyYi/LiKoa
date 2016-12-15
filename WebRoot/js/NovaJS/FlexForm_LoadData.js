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
	{
		var planCode = jQuery("select[name='Plan_id']").length; 
		
		if(planCode>0)
		{
			setArray();
			setCheckbox();
		}
	}
	
	tableLength = jQuery("#formTable9 tr").length;
	if(tableLength>1)
	{
		var selectedMemPlan;
		jQuery("#formTable9 tr select[name='MemPlan_id']").each(function(){
			changeMemProduct(this);
		})
	}
	
    jQuery("#div5").find('input[name="addRowBtn"]').click(function(){
    	var lastRowNo =  tableLength - 1;

    	if(array_total[0].length==0)
	        setArray();
    	setCheckbox();	//20150402 Justin Bin Added NOVA-137   :Refresh the plan
	    removeCheckbox();
    });
});

//NOVA-66 Hinson BEGIN: trigger this function when package product changed
function pkgProductChange(selectedID){
	var planCode = jQuery(selectedID).closest('tr').find('select[name="Plan_id"]');
	jQuery(planCode).find("option:first-child").attr("selected",true);
	jQuery(planCode).change();
	
	jQuery('select[name="field_9_20"]').each(function(){	//load member pkg products
		loadMemPkgPdt(this);
//		changeMemProduct(this);
	});
}

function loadMemPkgPdt(selectedID){
	var selected = jQuery("select[name='field_4_19']");
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

//NOVA-66 Hinson END

function flexPlanChange(selectedID){
	
    var selectedValue = jQuery(selectedID).val();
    var rowIndex = jQuery("#formTable4 tr").index(jQuery(selectedID).closest("tr"));
	var rowTDs = jQuery(selectedID).closest("tr").find("td");
	
    var checkboxNew
    
    jQuery("#formTable4 tr:eq("+rowIndex+") td").slice(4, 14).each(function(i){
    	var td = jQuery(this);
    	var name = "";
    	var hidden;
    	jQuery.each(array_total[i],function(i2,item){
            if(selectedValue == item.val.substr(5,3))
            {
            	td.contents().remove();
                checkboxNew = item.checkbox.clone();
                td.append(checkboxNew.after(item.label.text()));
                name = item.checkbox.attr("name");
                td.append(jQuery('<input name="'+name+'" type="hidden" required="false" value=""/>'));
                return false;
            }
            else if (selectedValue == ""){
            	//NOVA-66-20150309 hinson begin            	
//            	removeCheckbox();
            	td.contents().remove();
            	name = item.checkbox.attr("name");
            	td.append(jQuery('<input name="'+name+'" type="hidden" required="false" value=""/>'));
            	return false;
            	//NOVA-66-20150309 hinson end
            }
            
            
        });
    	
    	td.find('input[type="checkbox"]').live("click",function(){
    		name = jQuery(this).attr("name");
    		hidden = td.find('input[name="'+name+'"][type="hidden"]');
	    	if(jQuery(this).is(":checked")) 
	    	{
	    		if(hidden.length>0)
	    			hidden.remove();
	    	}
	    	else
	    	{
	    		if(hidden.length==0)
	    			td.append(jQuery('<input name="'+name+'" type="hidden" required="false" value=""/>'));
	    	}
	    });
    })
    //NOVA-66 Hinson begin: refresh member plan
    refreshPlan();
  //NOVA-66 Hinson end
    
    countPeopleByPlan();	//20150323 Justin Bin Added
}

function changeMemProduct(selectedID){
	var selectedVal = jQuery(selectedID).closest('tr').find('select[name="field_9_20"]').val();
	var options = [];
	
	var curSelectPlan = jQuery(selectedID);
	var curSelectPlanVal = curSelectPlan.val();
	
	curSelectPlan.empty();
	//NOVA-66-20150309 Hinson begin
//	if(selectedVal!='null')
	if(selectedVal!='')
	//NOVA-66-20150309 Hinson end
	{
		var selectedProduct = jQuery("#formTable4 tr td select[name='field_4_19']");
		
		
		if(selectedProduct.length>0)
		{
			//NOVA-68 hinson begin: default value should be blank instead of 'null'
//			jQuery(curSelectPlan).append("<option value='null'></option>"); 
			jQuery(curSelectPlan).append("<option value=''></option>"); 
			//NOVA-68 hinson end
			selectedProduct.each(function(i){
				selectedPlanVal = selectedProduct.eq(i).find("option:selected").val();
				
				if(selectedPlanVal == selectedVal)
				{
					selectedPlan = selectedProduct.eq(i).closest('tr').find("select[name='Plan_id']");
					
					value = selectedPlan.find("option:selected").val();
					text = selectedPlan.find("option:selected").text();
					
					//NOVA-66-20150309 hinson begin: if plan is blank, not add to the member plan
//					options.push("<option value='"+value+"'>"+text+"</option>");
					if(value!==''){
						options.push("<option value='"+value+"'>"+text+"</option>");
					}
					//NOVA-66-20150309 hinson end
				}
			});
			
			options = uniQueue(options);
			
			curSelectPlan.append(options);
			
			curSelectPlan.combobox("rebindData");
			//jQuery(curSelectPlan).find("option:first-child").attr("selected", true);
		}
		
		//curSelectPlan.val(curSelectPlanVal);
		curSelectPlan.combobox("setValue",curSelectPlanVal)
		
		changeMemPlan(curSelectPlan);
	}
	
	//selectSHIP = jQuery(selectedID).closest('tr').find('select[name="field_9_13"]');
	
	//jQuery(selectSHIP).find("option:first-child").attr("selected", true);
	
	//selectSHIP.removeAttr('disabled');

	//calculatePremium(selectedID);
}

function changeMemPlan(selectedID){
	
	//calculatePremium(selectedID);
	var dobField = jQuery(selectedID).closest('tr').find('input[name="field_9_9"]');
	
	calculatePremium(dobField,true);
	
	var shipField = jQuery(selectedID).closest('tr').find('select[name="field_9_13"]');

	shipField.trigger('change');
	//dobField.trigger('onblur');
	countPeopleByPlan();		//20150323 Justin Bin Added
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
	
	jQuery("#formTable4 tr:eq(1) td").slice(4, 14).each(function(i){	//20150428 Justin Bin
		var td = jQuery(this).contents();
		
		for(var i2=0; i2 < td.length-1; i2++ ){
    		if(i2%3==0)
    		{
    			array_total[i].push({val: td.eq(i2).val(), 
                      checkbox: td.eq(i2).clone().attr('checked', false),
                      label: td.eq(i2+1)});
    			i2 = i2 + 1;
    		}
    	}
	});
}

function removeCheckbox(){
	jQuery("#formTable4 tr:last td").slice(4, 14).each(function(i){
		jQuery(this).contents().remove();
	});
}

function setCheckbox(){
	jQuery.each(jQuery("#formTable4 tr:gt(0)"),function(i,item){
		var value = jQuery(item).find("select[name='Plan_id']").val();
		jQuery(item).find("td").slice(4, 14).each(function(i2){
			var checkbox;
			var td = jQuery(this).contents();
			var tdself = jQuery(this);
			var name = "";
	    	var hidden;
	    	
			for(var i3=0; i3 < td.length-1; i3++ ){
	    		if(i3%3==0)
	    		{
	    			if(value == td.eq(i3).val().substr(5,3))
					{
						checkbox = td.eq(i3).clone();
						checkbox = checkbox.after(td.eq(i3+1).text());
						
						if(!td.eq(i3).is(":checked")) 
						{
							name = td.eq(i3).attr("name");
							checkbox = checkbox.after(jQuery('<input name="'+name+'" type="hidden" required="false" value=""/>'));
						}
						break;
					}
	    			i3 = i3 + 1;
	    		}
	    	}
			
			td.remove();
			tdself.append(checkbox);
			
			tdself.find('input[type="checkbox"]').live("click",function(){
	    		name = jQuery(this).attr("name");
	    		hidden = tdself.find('input[name="'+name+'"][type="hidden"]');
		    	if(jQuery(this).is(":checked")) 
		    	{
		    		if(hidden.length>0)
		    			hidden.remove();
		    	}
		    	else
		    	{
		    		if(hidden.length==0)
		    			tdself.append(jQuery('<input name="'+name+'" type="hidden" required="false" value=""/>'));
		    	}
		    	refreshPlan();
		    });
		});
	});
}
