var CompassDB = "";
var CommonCompanyCode = "";
var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;
var server_context=basePath;
var GpolnoMem="";
var GdateMem= "";
var GpolnoPro="";
var GdatePro= "";
var arrayProduct = [];
var arrayPlan = [];
var formSystemId = "";

jQuery(document).ready(function() {
	formSystemId = jQuery("input[name='formSystemId'][type='hidden']").val();
	maxLengthControl();
	listenInputFocus();
	
	jQuery('input[isDate="true"]').each(function() { 
		jQuery(this).blur(function() { _NOVA.until.changeDateFormat(this); });	
	});
	
	hideTableButtonAndCheckBox();
	triggerBasicAction();
	triggerTableAction();
	initBtn();
	defaultDisplay();
	initalProductPlan();
	initalButtonClick();
});

function initalButtonClick(){
	jQuery("#div4 input[name='addRowBtn'][type='button']").click(function(){
		var memProductField = jQuery("#formTable9 tr:last").find('select[name="MemCHGProduct_id"]');
		var memPlanField = jQuery("#formTable9 tr:last").find('select[name="MemCHGPlan_id"]');
		
		memProductField.empty();
		if(arrayProduct.length>0)
		{
			jQuery.each(arrayProduct,function(i,item){
				memProductField.append(item);
			});
		}
		
		memProductField.trigger("change");
		
		memPlanField.empty();
		if(arrayPlan.length>0){
			jQuery.each(arrayPlan,function(i,item){
				memPlanField.append(item);
			});
		}
		
		memPlanField.trigger("change");
	});
}

function initalProductPlan(){
	arrayProduct = [];
	if(jQuery('#formTablePlanList tr').length>1){
		RetrivePolProductPlan()
	}
}

function hideTableButtonAndCheckBox(){
	jQuery("#div5").find("input[name='addRowBtn']").hide();
	jQuery("#div5").find("input[name='deleteRowBtn']").hide();
	jQuery("#div5").find("br").hide();
	
	jQuery("#div3").find("input[name='addRowBtn']").hide();
	jQuery("#div3").find("input[name='deleteRowBtn']").hide();
	
	jQuery("#div3").find("br").hide();
}

function triggerBasicAction(){
	
	jQuery('textarea[name="field_PolicyInfo_3"]').blur(function(){blurPolicy(this);}).focus(function(){HidePic(this);}); // Policy No
	jQuery('input[name="field_PolicyInfo_4"]').change(function(){blurDate(this);}).focus(function(){HidePic(this);}); // Effective Date
	
	jQuery('textarea[name="field_PolicyInfo_13"]').blur(function(){	RetriveClientNo(this);}).focus(function(){HidePic(this);});; //Client Code
	
	jQuery('textarea[name="field_PolicyInfo_2"]').blur(function(){RetriveClientName(this);}).focus(function(){HidePic(this);});; //Client Name
}

function blurPolicy(obj){
	jQuery(obj).parent().find('span.t-loading').remove();
	if(jQuery(obj).length>0)
		jQuery(obj).after(jQuery('<span/>').addClass('t-loading'));
	
	var outTimer2 = setTimeout(function(){
		jQuery(obj).parent().find('span.t-loading').remove();
		RetrivePolMem();
		RetrivePolProductPlan();
		RetrivePolicy(obj);
	}, 1000);
}

function blurDate(obj){
	jQuery(obj).parent().find('span.t-loading').remove();
	
	if(jQuery(obj).length>0)
		jQuery(obj).after(jQuery('<span/>').addClass('t-loading'));
	
	var outTimer2 = setTimeout(function(){
		jQuery(obj).parent().find('span.t-loading').remove();
		RetrivePolMem();
		RetrivePolProductPlan();
		RetriveValidate(obj);
	}, 1000);
}

function triggerTableAction(){
	bindTableNewRowAction("formTablePlanList", 1);
	bindTableNewRowAction("formTable9",1);
}

function bindTableNewRowAction(tableId, type){
	
	switch (tableId) {
		case "formTable9": {
			bindTableTRAction(tableId,'textarea[name="field_9_7"]','blur','searchMemExist(this);IdValidation(this,0);',type);
			bindTableTRAction(tableId,'textarea[name="field_9_2"]','blur','searchMemExist(this);',type);
			bindTableTRAction(tableId,'select[name="MemCHGProduct_id"]','change','changeMemProduct(this);',type);
			bindTableTRAction(tableId,'select[name="MemCHGPlan_id"]','change','changeMemPlan(this);',type);
			break;
		}
	}
	
	if(type==0)
	{
		jQuery("#"+tableId+" tr:last").find('textarea').each(function(){
	    	jQuery(this).focus(function(){ jQuery(this).css("background-color","#F8FEC3");});
	    	jQuery(this).blur(function(){ jQuery(this).css("background-color","#FFFFFF");});
	    });
	    
		jQuery("#"+tableId+" tr:last").find('input[type="text"]').each(function(){
	    	jQuery(this).focus(function(){jQuery(this).css("background-color","#F8FEC3");});
	    	jQuery(this).blur(function(){jQuery(this).css("background-color","#FFFFFF");});
	    });
	    
		jQuery("#"+tableId+" tr:last").find('input[type="checkbox"]').each(function(){
	    	jQuery(this).focus(function(){jQuery(this).parent().css("background-color","#F8FEC3");});
	    	jQuery(this).blur(function(){jQuery(this).parent().css("background-color","rgb(239, 239, 239)");});
	    });
	}
}

function bindTableTRAction(tableId, fieldId, action, func, type){
	if(type == 1)
	{
		if(jQuery("#"+tableId +" tr").length>1){
			jQuery("#"+tableId +" tr " + fieldId).each(function(){
				jQuery(this).bind(action,function(){eval(func);} );
			});
		}
	}
	else
		jQuery("#"+tableId+" tr:last " + fieldId).bind(action,function(){eval(func);} );
}

//20150416 
function defaultDisplay(){
	for(var num=5;num<11;num++){
		jQuery("#div"+num).stop().fadeIn();
		jQuery("#div"+num).fadeOut(0);
		document.getElementById("imgcon"+num).src = "images/Rplus.png";
	}
}

//20150415 Justin Bin 
function loadProductAndPlan(prodCode,benPlan){
	var sql="select top 1 NOVA_PRODCODE_Local,NOVA_BENPLNCD_Local from TNovaProductMapping where BENPLNCD ='"+benPlan+"' and PRODCODE='"+prodCode+"'";
	var data=selectFromSQLJson('',sql);
	
	if(data!="" && data!=null && data.length>0)
		return data;
	else
		return '';
}

function loadPlanLocalByPlan(benPlan){
	var sql="select top 1 option_label from teflow_base_data_detail where master_id = (select top 1 master_id from teflow_base_data_master where field_name = '产品计划' ) and option_value = '"+benPlan+"'";
	var data=selectFromSQLJson('',sql);
	
	if(data!="" && data!=null && data.length>0)
		return data;
	else
		return '';
}

function loadBUSNSRCE(strBUSNSRCE){
	var sql="select top 1 option_label from teflow_base_data_detail where master_id = (select master_id from teflow_base_data_master where field_name = '业务渠道') and option_value = '" + strBUSNSRCE + "'";
	var data=selectFromSQLJson('',sql);
	
	if(data!="" && data!=null && data.length>0)
		return data;
	else
		return '';
}

function RetrivePolMem(){
	var polno = jQuery('textarea[name="field_PolicyInfo_3"]').text();
	var date = jQuery('input[name="field_PolicyInfo_4"]').val();
	
	if(polno!="" && date!="" && (GpolnoMem!= polno || GdateMem != date))
	{
		var data = selectFromSPJson("compnwJNDI","dbo.UspGNOVAGetMemPolByPolAndEffDate", polno + "," + date );
		
		jQuery("#formTableMemberList tr:gt(0)").each(function(){jQuery(this).remove();});
		
		if(data!="" && data!=null && data.length>0)
		{
			var tr = "",strDOB,trLast,data3,strEMPDT;
			
			jQuery.each(data,function(i,item) {
				
				createTableSectionRow("formTableMemberList","MemberList","0");
				
				trLast = jQuery("#formTableMemberList").find('tr:visible:last');
				
				trLast.find('textarea[name="field_MemberList_1"]').text(item.SUBOFFCODE);
				trLast.find('textarea[name="field_MemberList_2"]').text(item.CERTNO);
				trLast.find('textarea[name="field_MemberList_3"]').text(item.MEMNAME);
				trLast.find('textarea[name="field_MemberList_4"]').text(item.DEPNAME);
				trLast.find('textarea[name="field_MemberList_16"]').text(item.DEPDES);
				trLast.find('textarea[name="field_MemberList_6"]').text(item.ADDR);
				trLast.find('textarea[name="field_MemberList_17"]').text(item.MEMNATION);
				trLast.find('textarea[name="field_MemberList_8"]').text(item.MEMIDNO);
				trLast.find('textarea[name="field_MemberList_18"]').text(item.SEX);
				strDOB = formatDate1(item.DOB,'yyyy-MM-dd',dateFormat);
				
				trLast.find('textarea[name="field_MemberList_19"]').text(strDOB);
				trLast.find('textarea[name="field_MemberList_15"]').text(item.OCCUPATION);
				
				data3=loadProductAndPlan(item.PRODCODE, item.BENPLNCD);
				
				if(data3.length>0){
					trLast.find('textarea[name="field_MemberList_22"]').text(data3[0].NOVA_PRODCODE_LOCAL);
					trLast.find('textarea[name="field_MemberList_14"]').text(data3[0].NOVA_BENPLNCD_LOCAL);
				}
				
				trLast.find('textarea[name="field_MemberList_13"]').text(item.SALARY);
				
				strEMPDT = formatDate1(item.EMPDT,"yyyy-MM-dd",dateFormat)
				
				trLast.find('textarea[name="field_MemberList_20"]').text(strEMPDT);
				trLast.find('textarea[name="field_MemberList_21"]').text(item.SHIP);
			});
		}
		GpolnoMem = polno;
		GdateMem = date;
	}
}

function RetrivePolProductPlan(){
	
	var polno=jQuery('textarea[name="field_PolicyInfo_3"]').text();
	var date= jQuery('input[name="field_PolicyInfo_4"]').val();
	
	if(polno!="" && date!="" && (GpolnoPro!= polno || GdatePro != date)){
		var data=selectFromSPJson("compnwJNDI","dbo.UspGNOVAGetProdPlanByPolNoAndEffDate",polno + "," + date);
		
		jQuery("#formTablePlanList tr:gt(0)").each(function(){jQuery(this).remove();});
		
		arrayProduct = [];
		arrayPlan = [];
		
		if(data!="" && data!=null && data.length>0){
			
			if(data[0].PRODTYPE == 'P')
			{
				jQuery.each(data,function(i,item){
					createTableSectionRow("formTablePlanList","PlanList","0");
					
					var trLast = jQuery("#formTablePlanList").find('tr:visible:last');
					
					var data3=loadProductAndPlan(item.PRODCODE, item.BENPLNCD);
					
					trLast.find('textarea[name="field_PlanList_1"]').after(jQuery('<input name="Product_id" type="hidden" required="false" value="'+item.PRODCODE+'"/>'));
					trLast.find('textarea[name="field_PlanList_2"]').after(jQuery('<input name="Plan_id" type="hidden" required="false" value="'+item.BENPLNCD+'"/>'));

					arrayProduct.push("<option value='"+item.PRODCODE+"'>"+data3[0].NOVA_PRODCODE_LOCAL+"</option>"); 
					arrayPlan.push("<option value='"+item.BENPLNCD+"'>"+data3[0].NOVA_BENPLNCD_LOCAL+"</option>"); 
					
					if(data3.length>0){
						trLast.find('textarea[name="field_PlanList_1"]').text(data3[0].NOVA_PRODCODE_LOCAL);
						trLast.find('textarea[name="field_PlanList_2"]').text(data3[0].NOVA_BENPLNCD_LOCAL);
					}
					trLast.find('textarea[name="field_PlanList_3"]').text(item.PLNDESC);
				});
			}
			else
			{
				var strBENPLNCD = "";
				
				jQuery.each(data,function(i,item){
					if(item.BENPLNCD!=strBENPLNCD)
					{
						createTableSectionRow("formTablePlanList","PlanList","0");
						
						var trLast = jQuery("#formTablePlanList").find('tr:visible:last');
						var productField = trLast.find('textarea[name="field_PlanList_1"]');
						var planField = trLast.find('textarea[name="field_PlanList_2"]');
						
						productField.text("定制产品");
						var data3 = loadPlanLocalByPlan(item.BENPLNCD)
						
						
						trLast.find('textarea[name="field_PlanList_1"]').after(jQuery('<input name="Product_id" type="hidden" required="false" value="TMPRD"/>'));
						trLast.find('textarea[name="field_PlanList_2"]').after(jQuery('<input name="Plan_id" type="hidden" required="false" value="'+item.BENPLNCD+'"/>'));
						
						arrayProduct.push("<option value='TMPRD'>定制产品</option>"); 
						arrayPlan.push("<option value='"+item.BENPLNCD+"'>"+data3[0].OPTION_LABEL+"</option>"); 
						
						planField.text(data3[0].OPTION_LABEL);
						
						destroyQtip("#imgShowProduct_"+item.BENPLNCD,"divProductList_" + item.BENPLNCD);
						
						var newImg = jQuery('<img id="imgShowProduct_'+item.BENPLNCD+'" width="16" height="16" style="width: 16px; height: 16px; border-width: 0px; cursor: pointer;" src="images/Checking/Search.png" />');
						planField.append(newImg);
						
						bindQtipProduct("#imgShowProduct_"+item.BENPLNCD,data,item.BENPLNCD);

						strBENPLNCD = item.BENPLNCD;
						
						trLast.find('textarea[name="field_PlanList_3"]').text(item.PLNDESC);
					}
				});
			}
		}
		GpolnoPro = polno;
		GdatePro = date;
		
		loadMemProductPlan();
	}
}

function searchMemExist(obj){
	var currentTR = jQuery(obj).closest('tr');
	
	var index = jQuery(obj).closest('tr').index();
	var newImg = jQuery('<img id="imgShowDup_'+index+'" width="16" height="16" style="width: 16px; height: 16px; border-width: 0px; cursor: pointer;" src="images/Checking/Plus2.png" />');
	
	var memIDNo = jQuery(obj).closest('tr').find('textarea[name="field_9_7"]').text();
	var clntcode = jQuery('textarea[name="field_PolicyInfo_13"]').text();
	var polno = jQuery('textarea[name="field_PolicyInfo_3"]').text();
	var date = jQuery('input[name="field_PolicyInfo_4"]').val();
	var name = jQuery(obj).closest('tr').find('textarea[name="field_9_2"]').text();
	var tr =jQuery(obj).closest('tr');
	
	destroyQtip("#imgShowDup_"+index,"divDupMember_"+index);
	
	var data = selectFromSPJson("compnwJNDI","dbo.UspGNOVAGetMemByUserNameMemIDNo", clntcode + "," + polno + "," + date + "," + memIDNo + " ," +name+" ");
	
	if(data!="" && data!=null && data.length>0)
	{
		newImg.appendTo(jQuery("td:eq(0)",currentTR));
		
		//if(jQuery("#imgShowDup_"+index).length==0)
		bindQtipClntPol1(jQuery("#imgShowDup_"+index), data, index);
	}
}

//Get verify the member ID whether suitable to generate DOB & Gender
//------------------------------------------------------------------
function IdValidation(OperField,Method){
	//1. Check 18 digit at first
	//2. Check 7 ~ 14 whether year
	//Return if client mode
	// member: 0  :go on
	// client: 1  :return
	//3. Check 17 whether male or female: Odd(1) male/ Even(2) Female
	//4. Check the black list (remove)
	  var IdString = OperField.innerText
	  var CurrentRow = OperField.parentNode.parentNode.rowIndex;
	
	//1. Check 18 digit
	  var IdLength = IdString.length;
	  
	//4. BlackList Checking
		//BlackListChecking(OperField,IdString);      
	    
		//6. check MemIDNo duplicate
		//CheckDuplicate(OperField);
		
	  if (IdLength !==18)
	 	 return false;
	
	//2. Check 7 ~ 14 whether year
	  var intYear   = IdString.substr(6,4);
	  var intMonth  = IdString.substr(10,2);
	  var intDay    = IdString.substr(12,2);
	  var boolLeapYear;
	
	  if(isNaN(intYear)||isNaN(intMonth)||isNaN(intDay)) 
	     return false;   	
	  if(intMonth>12 || intMonth<1)
	     return false;   	
	  if((intMonth==1||intMonth==3||intMonth==5||intMonth==7||intMonth==8||intMonth==10||intMonth==12) &&(intDay>31||intDay<1)) 
	     return false;
	  if((intMonth==4||intMonth==6||intMonth==9||intMonth==11)&&(intDay>30||intDay<1)) 
	     return false; 
	
	  if(intMonth==2)
	  {    
	      if(intDay < 1) { return False; }	
	      boolLeapYear   =   false;         
	      if((intYear%100) == 0){ 
	             if((intYear%400) == 0)
	             	boolLeapYear = true;
	             else{ 
	                if((intYear%4) == 0)                
	             	   boolLeapYear = true;          
	             }      
	             if(boolLeapYear)       
	                if(intDay > 29) { return false;}        
	             else          
	                if(intDay > 28)  {return false; }      
	       }
	  }	  
	  switch(Method){
	      case 0: // member: 0
	          //no need process anything
	     	 break;
	      case 1: // client: 1
	          return true; 
	      default: // for other
	     	//no need process anything
	     	 break;
	  }      	  
	  var DateString;	
	      DateString=  intMonth + "/" + intDay + "/" + intYear;	             
	      DateString = formatDate1(DateString,"MM/dd/yyyy",dateFormat);	      
	  // Get Same Row
	  var CurrentRow = OperField.parentNode.parentNode.rowIndex;	  
	  var dobField = jQuery(OperField).closest('tr').find('input[name="field_9_9"]');	  
	  dobField.val(DateString);
	  dobField.val(DateString).trigger("blur");	
	//3. Check 17 whether male or female: Odd(1) male/ Even(2) Female
	   var GenderId = IdString.substr(IdString.length-2,1);
	   if (( (parseInt(GenderId) + 2 )%2 )==0) 
	      document.getElementsByName("field_9_8")[CurrentRow-1].value = "F";
	   else 
	      document.getElementsByName("field_9_8")[CurrentRow-1].value = "M";    
   }

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

function changeMemProduct(selectedID){
	countPeopleByPlan();
}

function changeMemPlan(selectedID){
	countPeopleByPlan();
}

function countPeopleByPlan(){
	
	var tableA=jQuery('#formTablePlanList')
	var trList=tableA.find("tr");
	
	for(var i=1;i<trList.length;i++){
		
		var pkgCode=jQuery(trList[i]).find('input[name="Product_id"]').val();
		var pkgPlan=jQuery(trList[i]).find('input[name="Plan_id"]').val();
		
		var selectedTr=jQuery('select[name="MemCHGProduct_id"] option:selected[value="'+pkgCode+'"]').closest('tr').find('select[name="MemCHGPlan_id"] option:selected[value="'+pkgPlan+'"]').closest('tr');
		var count1=selectedTr.length;
		
		jQuery(trList[i]).find('textarea[name="field_PlanList_4"]').val(count1);
	}
}

function bindQtipClntPol1(obj, data, index){
	obj.css("cursor","pointer");
	
	obj.qtip({
			id:'divDupMember_' + index,
			content: {
			    text: '<table id="tbTitleDup_'+index+'"><tr><td></td><td>客户编号</td><td>分支编号</td><td>被保人编号</td><td>被保险人姓名</td><td>居住地址</td><td>身份证/护照号码</td></tr></table>',
			    title: { text: '<label>已存在投保人信息: </label>'}
		    },
		    position: {
		    	my: 'top left',
		    	at: 'bottom right',
		    	container: jQuery("#thedetailtableDIV")
		    },
		    show: {
		    	event: 'click',
		    	effect: function() {
		    		jQuery(obj).attr("src","images/Checking/Minus2.png");
	    			jQuery(this).fadeTo(500, 1);
	    			}
		    },
		    hide: {
		    	event: 'click',
		        effect: function() {
		    		jQuery(obj).attr("src","images/Checking/Plus2.png");
		    		jQuery(this).slideUp();
		    		}
		    },
		    style:{ 
		    	classes: 'qtip-tipped',
		    	width: '500px'
		    },
		    onHide: function() { jQuery(this).qtip('destroy'); }
	});
	
	var tr = "";

	obj.qtip('show');
	
	var memName = jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_2"]').text();
	var memID = jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_7"]').text();
	var certNo = jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_16"]').text();
	
	jQuery.each(data,function(i,item) {
		tr = '<tr><td><input type="radio" name="tr_'+index+'" onclick="setExitMemInfo(this,'+index+')"';
		
		if(item.MEMIDNO == memID && item.NAMELAST == memName && item.CERTNO == certNo)
			tr = tr + ' checked="true" ';
		
		tr = tr + '"/></td><td>'+item.CLNTCODE+'</td><td>'+item.SUBOFFCODE+'</td><td>'+item.CERTNO+'</td><td>'+item.NAMELAST+'</td><td>'+item.ADDR+'</td><td>'+item.MEMIDNO+'</td></tr>';
		jQuery("table#tbTitleDup_"+index).append(tr);
	});
	
	//obj.qtip('hide');
}

function setExitMemInfo(obj,index){
	jQuery("#imgShowDup_"+index).closest('tr').find('select[name="field_9_19"]').val('U');
	
	var subOffice = jQuery(obj).closest('tr').find('td').eq(2).text();
	jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_17"]').text(subOffice);
	
	var certNo = jQuery(obj).closest('tr').find('td').eq(3).text();
	jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_16"]').text(certNo);
	
	var memName = jQuery(obj).closest('tr').find('td').eq(4).text();
	jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_2"]').text(memName);
	
	var address = jQuery(obj).closest('tr').find('td').eq(5).text();
	jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_20"]').text(address);

	var memIDNo = jQuery(obj).closest('tr').find('td').eq(6).text();
	jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_7"]').text(memIDNo);
	
	jQuery("#imgShowDup_"+index).closest('tr').find('textarea[name="field_9_7"]').trigger('blur');
}

function RefreshPic(OperItem, Param, ShowText){
	
	 var ShowPic  = "";
	 ShowText = "  " + ShowText;
	 
	 var heightvar  = 16; 
	 var widthhtvar = 16;

	 var newImg = jQuery('<img width="'+widthhtvar+'" height="'+heightvar+'" id="'+OperItem.name
			 +'_ProcessLoading"  style="width: '+widthhtvar+'px; height: '
			 + heightvar+'px; border-width: 0px; cursor: pointer;" src="'
			 + ShowPic +'" />');
	 
	 var SpaceInput1 = jQuery('<span id="'+OperItem.name + '_ProcessSpace">&nbsp;&nbsp;</span>');
	 var SpaceInput2 = jQuery('<span id="'+OperItem.name + '_ProcessContent" style="color:red">&nbsp;'+ShowText+'</span>');

	 var CurrentRow = OperItem.parentNode.parentNode.rowIndex;

	 var ObjectCurrentRow = jQuery("#"+OperItem.name+"_ProcessSpace").closest('tr').index();
	 
	 if(jQuery("#"+OperItem.name+"_ProcessSpace").length>0 && (CurrentRow == ObjectCurrentRow))
		 jQuery("#"+OperItem.name+"_ProcessSpace").show();
	 else
		 jQuery(OperItem).parent().append(SpaceInput1);
	 
	 if(jQuery("#"+OperItem.name+"_ProcessLoading").length>0 && (CurrentRow == ObjectCurrentRow))
	 { 
		 jQuery("#"+OperItem.name+"_ProcessLoading").attr('src',ShowPic);
		 jQuery("#"+OperItem.name+"_ProcessLoading").show();
	 }
	 else
		 jQuery(OperItem).parent().append(newImg);

	 if(jQuery("#"+OperItem.name+"_ProcessContent").length>0 && (CurrentRow == ObjectCurrentRow))
	 {
		 jQuery("#"+OperItem.name+"_ProcessContent").text(ShowText);
		 jQuery("#"+OperItem.name+"_ProcessContent").show();
	 }
	 else
		 jQuery(OperItem).parent().append(SpaceInput2);
	 
	 jQuery(OperItem).parent().css("text-align","left");
	 jQuery(OperItem).parent().css("vertical-align","bottom");
}

function HidePic(OperItem){
	 if(jQuery("#"+OperItem.name+"_ProcessLoading").length>0)
	 {
		 destroyQtip("#"+OperItem.name+"_ProcessLoading","div" + OperItem.name);
		 
		 //jQuery("#"+OperItem.name+"_ProcessLoading").qtip('hide');
		 //jQuery("#"+OperItem.name+"_ProcessLoading").hide();
	 }
	 
	 if(jQuery("#"+OperItem.name+"_ProcessSpace").length>0 )
		 jQuery("#"+OperItem.name+"_ProcessSpace").remove();
	 
	 if(jQuery("#"+OperItem.name+"_ProcessContent").length>0 )
		 jQuery("#"+OperItem.name+"_ProcessContent").remove();
}

//20150401 Justin Bin Added to check the Client Code and Client Name
function RetrivePolicy(OperItem){
	if(jQuery(OperItem).length>0){
		var polNo=jQuery(OperItem).text();
		var data=selectFromSPJson("compnwJNDI","dbo.UspGNOVAGetClientByPolicyNo", polNo);
		var strBILLPRDTO, dataBUSNSRCE,strBUSNSRCE;
		
		if(data!="" && data!=null && data.length>0){
			jQuery('textarea[name="field_PolicyInfo_13"]').text(data[0].CLNTCODE==null?"":data[0].CLNTCODE);
			var clientNo=data[0].CLNTCODE;
			jQuery('textarea[name="field_PolicyInfo_2"]').text(data[0].CLNTNAME==null?"":data[0].CLNTNAME);	
			jQuery('textarea[name="field_PolicyInfo_6"]').text(data[0].POLDESC==null?"":data[0].POLDESC);
			
			strBILLPRDTO = formatDate1(data[0].BILLPRDTO,"yyyy-MM-dd",dateFormat);
			
			jQuery('textarea[name="field_PolicyInfo_16"]').text(strBILLPRDTO==null?"":strBILLPRDTO);
			jQuery('textarea[name="field_PolicyInfo_15"]').text(data[0].STATUS==null?"":data[0].STATUS);
			jQuery('textarea[name="field_PolicyInfo_10"]').text(data[0].NOTES==null?"":data[0].NOTES);
			
			strBUSNSRCE = "";
			
			if(data[0].BUSNSRCE!=null && data[0].BUSNSRCE!="")
			{
				dataBUSNSRCE = loadBUSNSRCE(data[0].BUSNSRCE);
				if(dataBUSNSRCE.length>0)
					strBUSNSRCE = dataBUSNSRCE[0].OPTION_LABEL;
			}
			
			jQuery('textarea[name="field_6_7"]').text(strBUSNSRCE);
			jQuery('textarea[name="field_6_9"]').text(data[0].COMMCODE==null?"":data[0].COMMCODE);
			
			jQuery('textarea[name="field_BranchInf_5"]').text(data[0].BRANCHCODE==null?"":data[0].BRANCHCODE);
			jQuery('textarea[name="field_BranchInf_6"]').text(data[0].COUNTRYCODE==null?"":data[0].COUNTRYCODE);
			
			jQuery('textarea[name="field_6_8"]').text(data[0].SRVAE==null?"":data[0].SRVAE);

			RetriveProducers(data[0].PRODUCERINFO==null ?"": data[0].PRODUCERINFO);		//ProducerInfo
		}
	}
}

function RetriveProducers(obj){
	//var jProducers=xmlToJson(stringToXml(obj));
	
	var jProducers = jQuery.parseJSON(obj);
	
	if(jProducers!="" && jProducers!=null && jProducers.length>1){
		//20150416 Justin Bin
		jQuery("#formTable7 tr:gt(0)").each(function(){jQuery(this).remove();});
		for(var i=1;i<jProducers.length;i++){
			createTableSectionRow("formTable7","7","0");
			var trLast = jQuery("#formTable7").find('tr:visible:last');
			jQuery.each(trLast.find('td'),function(i1,item1){
				var child = jQuery(item1).children().eq(0);
				if(child.attr("name")=="field_7_2")			// code
					child.val(jProducers[i].PRDUCODE);
				if(child.attr("name")=="field_7_1")			// name
					child.val(jProducers[i].PRDUNAME);
				if(child.attr("name")=="field_7_3")			//营销服务部
					child.val(jProducers[i].AGTOFFCODE);
				if(child.attr("name")=="field_7_6")			//组别
					child.val(jProducers[i].AGYCODE);
				if(child.attr("name")=="field_7_4")			//联系电话
					child.val(jProducers[i].TELNO);
				if(child.attr("name")=="field_7_5")			//佣金分配
					child.val(jProducers[i].PRDUSHRPC);
			});
		}
		
	}
}

//20150401 Justin Bin Added to check the client by client No
function RetriveClientNo(OperItem){
	 if(jQuery(OperItem).length>0){
		 var clientCode=jQuery(OperItem).text();
		 
		 var data=selectFromSPJson("compnwJNDI","dbo.USPGNOVAGetPolicyByClient",clientCode);
		 
		 if(data!=null && data!="" && data.length>0){
			 RefreshPic(OperItem, 4, "已存在（点击左侧按钮显示/隐藏）");
			 
			 bindQtipPolByClntCode(clientCode,data);
			 
			 if(data.length>1)
				 jQuery('textarea[name="field_PolicyInfo_17"]').text("是");
			 else
				 jQuery('textarea[name="field_PolicyInfo_17"]').text("否");
			 
		 }
	 }
}

function RetriveClientName(OperItem){
	if(jQuery(OperItem).length>0){
		 var clntName=jQuery(OperItem).text();
		 
		 var data=selectFromSPJson("compnwJNDI","dbo.USPGNOVAGetClientByClntName",clntName);
		 
		 if(data!=null && data!="" && data.length>0){
			 RefreshPic(OperItem, 4, "已存在（点击左侧按钮显示/隐藏）");
			 bindQtipClientByClntName(clntName,data);
		 }
	 }
}

//20150401	Justin Bin Added to check the validation
function RetriveValidate(OperItem){
	 if(jQuery(OperItem).length>0){
		 //TODO
		 var effDate=new Date(jQuery(OperItem).val()).getTime();
		 var paidToDate=new Date(jQuery('textarea[name="field_PolicyInfo_16"]').val().replace('-','\/')).getTime();
		 
		 if(paidToDate+2*60*60*24*1000<=effDate)
			 jQuery('textarea[name="field_PolicyInfo_14"]').text("通过");
		 else
			 jQuery('textarea[name="field_PolicyInfo_14"]').text("失败").css({'color':'red','font-weight':'bold'});
	 }
}

//20150420 Justin Bin Added
function bindQtipProduct(obj,data,plan){
	
	jQuery(obj).css("cursor","pointer");
		
	jQuery(obj).qtip({
			id: "divProductList_"+plan,
			content: {
			    text: '<table id="tbProduct_'+plan+'"><tr><td>产品代号</td><td>保险产品</td></tr></table>',
			    title: { text: '<label>定制计划产品 </label>'}
		    },
		    position: {
		    	my: 'top left',
		    	at: 'bottom right',
		    	container: jQuery("#thedetailtableDIV")
		    },
		    show: {
		    	event: 'click',
		    	effect: function() {
	    			jQuery(this).fadeTo(500, 1);
	    			}
		    },
		    hide: {
		    	event: 'click',
		        effect: function() {
		    		jQuery(this).slideUp();
		    		}
		    },
		    style:{ 
		    	classes: 'qtip-tipped',
		    	width: '400px'
		    },
		    onHide: function() { jQuery(this).qtip('destroy'); }
	});
	
	var tr = "";

	jQuery(obj).qtip('show');
	
	jQuery.each(data,function(i,item) {
		if(item.BENPLNCD == plan){
			var tr = '<tr><td>'+item.PRODCODE+'</td><td>'+item.PRODCDESC+'</td></tr>';
			jQuery("table#tbProduct_"+plan).append(tr);
		}
		else
			return true;
	});
	
	jQuery(obj).qtip('hide');
}

// 20150408 Justin Bin Added
function bindQtipPolByClntCode(clientCode,data){
	jQuery("#field_PolicyInfo_13_ProcessLoading").css("cursor","pointer");
	
	jQuery("#field_PolicyInfo_13_ProcessLoading").qtip({
		id: "divfield_PolicyInfo_13",
		content:{
			text:'<table id="tbPolicyList"><tr><td></td><td>Policy No</td><td>Policy Desc</td></tr></table>',
			title:{text:"<label>客户编号: " + clientCode+ " </label>&nbsp;&nbsp;"}
		},
		 position: {
		    	my: 'top left',
		    	at: 'bottom right',
		    	container: jQuery("#thedetailtableDIV")
		    },
		    show: {
		    	event: 'click',
		    	effect: function() {
		    		jQuery("#field_PolicyInfo_13_ProcessLoading").attr("src","images/Checking/Minus2.png");
	    			jQuery(this).fadeTo(500, 1);
	    			}
		    },
		    hide: {
		    	event: 'click',
		        effect: function() {
		    		jQuery("#field_PolicyInfo_13_ProcessLoading").attr("src","images/Checking/Plus2.png");
		    		jQuery(this).slideUp();
		    		}
		    },
		    style:{  
		    	classes: 'qtip-tipped',
		    	width: '500px'
		    },
		    onHide: function() { jQuery(this).qtip('destroy'); }
	});
	
	var tr = "";

	jQuery("#field_PolicyInfo_13_ProcessLoading").qtip('show');
	
	var polNo = jQuery('textarea[name="field_PolicyInfo_3"]').text();
	
	jQuery.each(data,function(i,item) {
		tr = '<tr><td><input type="radio" name="tr_'+clientCode+'" value="'+i+'" onclick="selectPol('+i+')"';
		if( polNo !="" && polNo == item.POLNO)
			tr = tr + ' checked="true" ';
				
		tr = tr + '/></td><td id="td_Pol'+i+'">'+item.POLNO+'</td><td id="td_PolDesc'+i+'">'+item.POLDESC+'</td></tr>';
		
		jQuery("table#tbPolicyList").append(tr);
	});
	
	jQuery("#field_PolicyInfo_13_ProcessLoading").qtip('hide');
}

function bindQtipClientByClntName(clntName,data){
	jQuery("#field_PolicyInfo_2_ProcessLoading").css("cursor","pointer");
	
	jQuery("#field_PolicyInfo_2_ProcessLoading").qtip({
		id: "divfield_PolicyInfo_2",
		content:{
			text:'<table id="tbClientList"><tr><td></td><td>Client Code</td><td>Client Name</td></tr></table>',
			title:{text:"<label>投保人名称: " + clntName+ " </label>&nbsp;&nbsp;"}
		},
		 position: {
		    	my: 'top left',
		    	at: 'bottom right',
		    	container: jQuery("#thedetailtableDIV")
		    },
		    show: {
		    	event: 'click',
		    	effect: function() {
		    		jQuery("#field_PolicyInfo_2_ProcessLoading").attr("src","images/Checking/Minus2.png");
	    			jQuery(this).fadeTo(500, 1);
	    			}
		    },
		    hide: {
		    	event: 'click',
		        effect: function() {
		    		jQuery("#field_PolicyInfo_2_ProcessLoading").attr("src","images/Checking/Plus2.png");
		    		jQuery(this).slideUp();
		    		}
		    },
		    style:{  
		    	classes: 'qtip-tipped',
		    	width: '500px'
		    },
		    onHide: function() { jQuery(this).qtip('destroy'); }
	});

	jQuery("#field_PolicyInfo_2_ProcessLoading").qtip('show');
	
	var tr = "";
	
	var clntCode = jQuery('textarea[name="field_PolicyInfo_13"]').text();
	
	jQuery.each(data,function(i,item) {
		tr = '<tr><td><input type="radio" name="tr_ClientName" value="'+i+'" onclick="selectClnt('+i+')" ';
		
		if(clntCode !="" && clntCode == item.CLNTCODE)
			tr = tr + 'checked="true"';
			
		tr = tr + '/></td><td id="td_ClntCode'+i+'">'+item.CLNTCODE+'</td><td id="td_ClntName'+i+'">'+item.CLNTNAME+'</td></tr>';
		
		jQuery("table#tbClientList").append(tr);
	});
	
	jQuery("#field_PolicyInfo_2_ProcessLoading").qtip('hide');
}

function selectClnt(index){
	jQuery('textarea[name="field_PolicyInfo_13"]').text(jQuery('#td_ClntCode'+index).text()).trigger('blur');
	jQuery("#field_PolicyInfo_2_ProcessLoading").qtip('hide');
}

function selectPol(index){
	jQuery('textarea[name="field_PolicyInfo_3"]').text(jQuery('#td_Pol'+index).text()).trigger('blur');
	jQuery("#field_PolicyInfo_13_ProcessLoading").qtip('hide');
}

function destroyQtip(strImg,strDiv){
	jQuery(strImg).qtip('hide');
	jQuery(strImg).qtip('destroy');
	jQuery(strImg).remove();
	
	jQuery('div[id^="qtip-'+ strDiv +'"]').each(function(){ //search for remaining objects

	    _qtip2 = jQuery(this).data("qtip"); 
	    if(_qtip2 != undefined){ 
	        _qtip2.destroy(true);
	    }
	});
}

function loadMemProductPlan(){
	if(jQuery("#formTable9 tr").length>0){
		var memProductField,memPlanField
		jQuery("#formTable9 tr").each(function(index){
			if(index>0)
			{
				memProductField = jQuery(this).find('select[name="MemCHGProduct_id"]');
				memPlanField = jQuery(this).find('select[name="MemCHGPlan_id"]');
				
				memProductField.empty();
				if(arrayProduct.length>0)
				{
					jQuery.each(arrayProduct,function(i,item){
						memProductField.append(item);
					});
				}
				
				memPlanField.empty();
				if(arrayPlan.length>0){
					jQuery.each(arrayPlan,function(i,item){
						memPlanField.append(item);
					});
				}
			}
		})
	}
}

function RemoveCheckingMsg(OperField){
	var MemIDNo = jQuery(OperField).val();
	
	jQuery(OperField).closest('tr').css('background-color','rgb(235, 235, 233)');
	if(jQuery('span[name="span'+MemIDNo+'"]').length>0)
	{
		jQuery('span[name="span'+MemIDNo+'"]').each(function(){
			jQuery(this).closest("div").remove();
		});
	}
	
	if(jQuery(".errorMsg").length==0)
		jQuery("div.wizard-steps-parent2").closest("tr").remove();
}

function initBtn() {
	var tid = "'formTable9'";
	
	var delBtn = jQuery('div#div4 input[name="deleteRowBtn"]');
	
	if (delBtn) {
		var file = "  <input type='file' id='xlsFile'/>";
		var button1 = '  <input type="button" id="copy_formTable9" value="Copy" onclick="memberCopy(\'formTable9\');" />';
		var button2 = '  <input type="button" id="impt_formTable9" value="导入Excel" onclick="validateXls(\'formTable9\');" />';
		var button3 = '  <input type="button" id="model_formTable9" value="Download" onclick="downloadXls(\'formTable9\');" />';
		delBtn.after(button1 + file + button2 + button3);

		jQuery('div#div4 input[name="deleteRowBtn"]').each(function(){
		    jQuery(this).data('onclick', this.onclick);
		    
		    var checkBox = jQuery('#formTable9 input[name="chkid_9"][type="checkbox"]');
		    	
		    this.onclick = function(event) {
		    	if(checkBox.length>0)
		    	{
		    		checkBox.each(function(){
			    		if(jQuery(this).is(":checked")){
			    			var memIDField = jQuery(this).closest('tr').find('textarea[name="field_9_7"]')
			    			RemoveCheckingMsg(memIDField);
			    		}
			    	});
		    	}
		    	jQuery(this).data('onclick').call(this, event || window.event);
		    	
		    	if(checkBox.length>0)
		    	{
		    		checkBox.each(function(){
			    		if(jQuery(this).is(":checked")){
			    			var memIDField = jQuery(this).closest('tr').find('textarea[name="field_9_7"]')
			    			CheckDuplicate(memIDField);
			    		}
			    	});
		    	}
		    };
		});
	}
}

function downloadXls(tblId){
	var path = "/download/template/MemberUpload_20264.xlsx";
	
	if(formSystemId!=""&&formSystemId!=null)
	{
		var sql = "select param_value as PATHName from teflow_param_config where param_code = 'MemberUpload_path' and description = '"+formSystemId+"'";
		
		var data = selectFromSQLJson('',sql);
		
		if (data.length>0)
			path = data[0].PATHNAME;
	}
	window.location.href = server_context + path;
}

function memberCopy(tblId){
	
	var m = tblId.match(/\d{1,100}/);
	var sessionID = m[0];
	var table = jQuery('#'+tblId);
	var rowNum = table.find('tr').length;
	
	createTableSectionRow(tblId, sessionID, "0");
	
	if(rowNum>1)
	{	
		var trLast = table.find('tr:visible:last');
		jQuery.each(trLast.find('td'),function(i1,item1){
			var child = jQuery(item1).children().eq(0);
			var preChild = trLast.prev().find('td').eq(i1).children().eq(0);
			var tagName = child.prop('tagName');
			
			switch (tagName) {
				case "TEXTAREA": {
					if(child.attr('name') =="field_9_7")
						child.trigger('blur');
					
					child.text(preChild.val());
					break;
				}
				case "SELECT": {
					if(child.attr('name') == "MemProduct_id" || child.attr('name') == "MemPlan_id" 
						|| child.attr('name') == "field_9_13"|| child.attr('name') == "field_9_20"
						 || child.attr('name') == "field_9_21")
						child.trigger('focus');
					
					child.val(preChild.val());
					
					if(child.attr('name') == "MemProduct_id" || child.attr('name') == "MemPlan_id" 
						|| child.attr('name') == "field_9_13"|| child.attr('name') == "field_9_20"
						 || child.attr('name') == "field_9_21")
						child.trigger('change');
					break;
				}
				case "INPUT": {
					if (child.attr('isdate') == 'true') {
						child.val(preChild.val());
					}
					break;
				}
				default:
					break;
			}
		});
	}
}

// 文件校验
function validateXls(tblId) {
	var excelPath = jQuery('#xlsFile').val();
	if (excelPath == null || excelPath == '') {
		alert("请选择要上传的Excel文件");
	} else {
		var fileExtend = excelPath.substring(excelPath.lastIndexOf('.'))
				.toLowerCase();
		if (fileExtend != '.xls' && fileExtend != '.xlsx') {
			alert("文件格式需为'.xls'或'xlsx'格式");

		} else {
			var m = tblId.match(/\d{1,100}/);
			var sessionID = m[0];
			var filePath = jQuery('#xlsFile').val();
			
			var outTimer1,outTimer2

			outTimer1 = setTimeout(function(){ShopConfirm("人员上传处理中。。。");
			clearTimeout(outTimer1);}, 50);
			
			var content
			
			outTimer2 = setTimeout(function(){
				content = readXls(filePath, tblId, sessionID);
				clearTimeout(outTimer2);
			}, 1000);
		}

		var file = jQuery('#xlsFile');
		file.after(file.clone().val(""));
		file.remove();
	}
	return;
}

// 读取xls文件
function readXls(filePath, tableName, sessionID) {
	var sheet_id = 1; // 读取第1个表
	var row_start = 2; // 从第2行开始读取
	var tempStr = '';

	var birthday = '';
	var effectDay = '';
	var oXL;
	var dateStr;
	try {
		oXL = new ActiveXObject("Excel.application");
	}
	// 创建Excel.Application对象
	catch (err) {
		alert(err.message);
	}
	
	var oWB = oXL.Workbooks.open(filePath);
    
	try {
		
		oWB.worksheets(sheet_id).select();
		var oSheet = oWB.ActiveSheet;
		
		//var rowNum = oXL.Worksheets(sheet_id).UsedRange.Cells.Rows.Count;
		var rowNum = oXL.Worksheets(sheet_id).Range("A65536").End(-4162).Row;
		//alert(rowNum2);
		var index, self, value, child, tagName, i2
		var tableRow = jQuery('#' + tableName).find('tr').length;
		for (var i1=row_start; i1<=rowNum; i1++) {
			
			i2 = tableRow;
			// 添加新行
			createTableSectionRow(tableName, sessionID, "0");
			tableRow++;
			
			jQuery('#' + tableName).find('tr').eq(i2).find('td').each(
				function(indexD) {
					if (indexD > 1) {
						self = jQuery(this);
						
						//indexD = indexD + 1;
						
						value = jQuery.trim(oSheet.Cells(i1, indexD).value);
						child = self.children().eq(0);
						tagName = child.prop('tagName');
						childName = child.attr('name'); 
							
						switch (tagName) {
							case "TEXTAREA": {
								child.text(value);
								
								if(child.attr('name') =="field_9_7")
									child.trigger('onblur');
								
								break;
							}
							case "SELECT": {
								child.find('option').filter(function() {
								    return jQuery(this).text() == value; 
								}).prop('selected', true);
								
								break;
							}
							case "INPUT": {
								if (child.attr('isdate') == 'true') {
									if(value!=""){
										d = new Date(value);
										if(!isNaN(d))
										{
											month = d.getMonth() + 1;
											month = month<10?('0'+month):month;
											date = d.getDate();
											date = date<10?('0'+date):date;
											
											dateStr = month + "/" + date + "/" + d.getFullYear();
											
											child.val(formatDate1(dateStr,"MM/dd/yyyy",dateFormat));
										}
									}
								}
								
								break;
							}
							default:
								break;
						}
					}
				});
		}
		
		oXL.DisplayAlerts = false;
		oWB.Close(savechanges=false);
		oWB = null;
		//oXL.DisplayAlerts = true;
		oXL.Quit();
		oXL.Application.Quit(); 
		oXL = null;
		window.setTimeout(CollectGarbage, 10);
		
	} catch (err) {
		alert(err.message);
	
		//oXL.DisplayAlerts = true;
		if(oXL)
		{
			oXL.DisplayAlerts = false;
			if(oWB)
			{
				oWB.Close(savechanges=false);
				oWB = null;
			}
			oXL.Quit();
			oXL.Application.Quit(); 
			oXL = null;
			window.setTimeout(CollectGarbage, 10);
		}
		closeWindow2();
	}
	
	closeWindow2();	
	jQuery('#loadingTR').remove();
	return tempStr; // 返回
}

