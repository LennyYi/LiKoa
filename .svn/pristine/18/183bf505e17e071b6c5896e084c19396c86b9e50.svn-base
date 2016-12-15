
var requestNo = jQuery('input[name="request_no"]').val();
var requestUrl = jQuery('input[name="requestUrl"]').val();
var formSystemId = jQuery('input[name="formSystemId"]').val();
var requestFormDate = jQuery('input[name="request_date"]').val();
//var currentNodeId;
//var operateType;

jQuery().ready(function(){
	var currentNodeId=jQuery("[name='currentNodeId']").val();
	var operateType = jQuery("[name='operateType']").val();
//	currentNodeId="2";	//debug
	//alert(currentNodeId);
	if(currentNodeId=="0" || currentNodeId=="1" || currentNodeId=="2"){	//Request Proposal
		jQuery('#div3').closest('tr').hide();
		jQuery('#div3').closest('tr').prev().hide();
		jQuery('#div3').closest('tr').prev().prev().hide();
	}else if(currentNodeId=="3"){	//Prepare Proposal
		jQuery("input[name='selectRefBtn']").eq(0).attr("value","Mem Benefit Summary");
		jQuery("input[name='selectRefBtn']").eq(0).attr("onclick","");
	
		
		jQuery("input[name='selectRefBtn']").eq(2).hide();	//Suppress Contract
		jQuery("input[name='selectRefBtn']").eq(2).closest('td').hide();
		jQuery("input[name='selectRefBtn']").eq(2).closest('td').prev().hide();
	}
	else {
		jQuery('#div3').closest('tr').hide();
		jQuery('#div3').closest('tr').prev().hide();
		jQuery('#div3').closest('tr').prev().prev().hide();

	}
		
	jQuery('select[name="company_name"]').change(function(){  
		var compcode = jQuery(this).val();
		loadCompcodeRefInfo(compcode);
		});
	jQuery("input[name='selectRefBtn']").eq(0).click(function(){  
//		alert("aaa");
//		window.open ('Demo/ProposalIndemnity_2.htm', 'newwindow', 'height=1169, width=827, top=0,left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=no,location=no, status=no');
		window.open ('Demo/HKDemo.html', 'newwindow', 'height=1000, width=900, top=0,left=400, toolbar=no, menubar=no, resizable=no, scrollbars=yes');
		});
	jQuery("input[name='selectRefBtn']").eq(1).click(function(){  
//		window.open ('Demo/HKDemo.html', 'newwindow', 'height=1000, width=900, top=0,left=400, toolbar=no, menubar=no, resizable=no, scrollbars=yes');
		window.open ('Demo/HKDemo.html', 'newwindow', 'height=1000, width=900, top=0,left=400, toolbar=no, menubar=no, resizable=no, scrollbars=yes');
		});
	jQuery("input[name='selectRefBtn']").eq(2).click(function(){  
//		window.open ('Demo/HKDemo.html', 'newwindow', 'height=1000, width=900, top=0,left=400, toolbar=no, menubar=no, resizable=no, scrollbars=yes');
		window.open ('Demo/HKContract.html', 'newwindow', 'height=1000, width=900, top=0,left=400, toolbar=no, menubar=no, resizable=no, scrollbars=yes');
		});
});

function loadCompcodeRefInfo(compcode){	
	jQuery('textarea[name="field_2_2"]').val(compcode);	
	//AE name
//	var sql = "select ab_name from GMPSUPTC..t_ae_ab where ae_code in (select ae_code from GMPSUPTC..t_wp_comp where comp_code = '" + compcode + "') ";
	var sql = "SELECT source,AGY_CODE,ab_name,ae_code,CONVERT(CHAR(10),lann_date,101) AS lann_date FROM GMPSUPTC..t_wp_comp where comp_code = '" + compcode + "'";
	var data = selectFromSQLJson('',sql);
	if(data.length>0){
		jQuery.each(data,function(i,item){
			jQuery('textarea[name="field_2_3"]').val(item.AE_CODE);   //AE code
			jQuery('textarea[name="field_2_4"]').val(item.AGY_CODE);   //Agent/Broker

			jQuery('input[name="field_2_8"]').each(function(){		//SOB
				jQuery(this).attr('checked',false);	//Clear first
				if(jQuery(this).val()==item.SOURCE){
					jQuery(this).attr('checked',true);
				}
			});
			
//			alert(item.LANN_DATE);
			jQuery('input[name="field_2_5"]').val(item.LANN_DATE);   //Ref. Date
		});
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

	  var sql = "SELECT source,agy_code,ab_name,ae_code,CONVERT(CHAR(10),lann_date,101) AS lann_date FROM GMPSUPTC..t_wp_comp where comp_code = '" + compcode + "'";
		var data = selectFromSQLJson('',sql);
		if(data.length>0){
			jQuery.each(data,function(i,item){
				jQuery('textarea[name="field_2_3"]').val(item.AE_CODE);   //AE code
				jQuery('textarea[name="field_2_4"]').val(item.AGY_CODE);   //Agent/Broker
				//jQuery('textarea[name="company_name"]').val(compcode);   
				compname.val(compcode);

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
			  RefreshPic(OperItem,1,"The Compcode is not exist");  
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
