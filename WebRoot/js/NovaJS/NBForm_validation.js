
var PreviousMinProducerCode = "";

// Get Domain ID
function domainidrequest(){
	var CheckObject = document.getElementsByName("request_staff_code")[0];
	
	if((CheckObject.selectedIndex+"") == "undefined"){return true;}
	
	var SplitBegin, SplitEnd;
	    SplitBegin = "(";
	    SplitEnd = ")";

	var substr1 , substr2 , substr3 ;

    substr1 = CheckObject.options[CheckObject.selectedIndex].text;
    substr2 = substr1.substr( substr1.lastIndexOf(SplitBegin)+1, substr1.length);
    substr3 = substr2.substr( 0, substr2.indexOf(SplitEnd)-substr2.indexOf("(")-1);

    document.getElementsByName("field_02_28")[0].value = substr3;
}

function OtherChecking(OperField)
{
	GenCERTNOByID(OperField);	
}

// Get verify the member ID whether suitable to generate DOB & Gender
// ------------------------------------------------------------------
function IdValidation(OperField,Method) 
{
	
// 1. Check 18 digit at first
// 2. Check 7 ~ 14 whether year
// Return if client mode
   // member: 0  :go on
   // client: 1  :return
// 3. Check 17 whether male or female: Odd(1) male/ Even(2) Female
// 4. Check the black list (remove)
     var IdString = OperField.innerText
     var CurrentRow = OperField.parentNode.parentNode.rowIndex;

// 1. Check 18 digit
     var IdLength = IdString.length;
     
   //4. BlackList Checking
   	BlackListChecking(OperField,IdString);      
       
 	//6. check MemIDNo duplicate
 	CheckDuplicate(OperField);
 	
     if (IdLength !==18)
    	 return false;

// 2. Check 7 ~ 14 whether year
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
         if((intYear%100) == 0)
         { 
                if((intYear%400) == 0)
                	boolLeapYear = true;
                else
                { 
                   if((intYear%4) == 0)                
                	   boolLeapYear = true;          
                }        

                if(boolLeapYear)       
                   if(intDay > 29) { return false;}        
                else          
                   if(intDay > 28)  {return false; }      
          }
      }
     
     switch(Method)
     {
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

// 3. Check 17 whether male or female: Odd(1) male/ Even(2) Female
      var GenderId = IdString.substr(IdString.length-2,1);
  
      if (( (parseInt(GenderId) + 2 )%2 )==0) 
    	  jQuery(OperField).closest('tr').find('select[name="field_9_8"]').combobox("setValue","F");
         //document.getElementsByName("field_9_8")[CurrentRow-1].value = "F";
      else 
    	  jQuery(OperField).closest('tr').find('select[name="field_9_8"]').combobox("setValue","M");
         //document.getElementsByName("field_9_8")[CurrentRow-1].value = "M";    
      
}

function CheckDuplicate(OperField){

	var MemIDNoField = jQuery(OperField);
	var NameField = jQuery(OperField).closest('tr').find('textarea[name="field_9_2"]');
	var ClntCodeField = jQuery('textarea[name="field_02_16"]');
	var statusField = jQuery(OperField).closest('tr').find('textarea[name="field_9_19"]');
	var prodCodeField = jQuery(OperField).closest('tr').find('select[name="MemProduct_id"]');
	if(statusField.length>0)
	{
		var smemstatus = statusField.val();
		statusField.val(smemstatus.replace("q",""));
	}
	
	var MemIDNo = MemIDNoField.val();

	if(MemIDNo!="" && prodCodeField.val() !="")
	{
		var data = selectFromSQLJson('',"select distinct PRODCODE from TNovaProductMapping where NOVA_PRODCODE = '" + prodCodeField.val() + "'") 
		if (data.length > 0) 
		{
			data = selectFromSPJson("compnwJNDI","dbo.USPGNOVAValidation", MemIDNo + "," + 
					NameField.text() + "," + 
					ClntCodeField.text() + "," + data[0].PRODCODE );
			
	        if (data.length > 0) 
	        {
	    		statusField.val(statusField.val()+"q");	
	        	//alert(data[0].MSG);
	        	
	        	var requestUrl = jQuery('input[type="hidden"][name="requestUrl"]').val();
	        	var warnMsg = '';
	        	
	        	jQuery.each(data,function(i,item){
		        	if(jQuery('div.errorDiv').length>0)
		        	{
		        		/*if(jQuery("span#span"+MemIDNo).length==0)
		        		{
			        		warnMsg = '<div class="errorDiv"><img src="'+requestUrl+'/images/Error.png"  class="errorImg"/><span class="errorMsg" id="span'+MemIDNo+'">'+item.MSG+'</span></div>';
			        		jQuery('div.errorDiv:last').after(warnMsg);
		        		}*/
		        		warnMsg = '<div class="errorDiv"><img src="'+requestUrl+'/images/Error.png"  class="errorImg"/><span class="errorMsg" name="span'+MemIDNo+'">'+item.MSG+'</span></div>';
		        		jQuery('div.errorDiv:last').after(warnMsg);
		        	}
		        	else
		        	{ 
		        		warnMsg = '<tr><td><div class="wizard-steps-parent2"><div align="center" class="wizard-steps2">';
			        	warnMsg += '<div class="errorDiv"><img src="'+requestUrl+'/images/Error.png"  class="errorImg"/><span class="errorMsg" name="span'+MemIDNo+'">'+item.MSG+'</span></div>';
			        	warnMsg += '</div></div></td></tr>';
			        	jQuery('table:eq(0)').find('tr:eq(0)').after(warnMsg);
		        	}
	        	});
	        }
		}
    }
	if(statusField.length>0)
		ChangeFont(statusField[0]);
}

function BlackListChecking(OperField,IdString){
	var sql = "SELECT option_value FROM teflow_base_data_detail where master_id='10137' AND option_value = '" + IdString + "'";
    
	var data = selectFromSQLJson('',sql);
	var statusField = jQuery(OperField).closest('tr').find('textarea[name="field_9_19"]');
	
	if(statusField.length>0)
	{
		var smemstatus = statusField.val();
		
		if(data.length>0){
			if (smemstatus.indexOf("r",smemstatus)<0)
				statusField.val(smemstatus+"r");	
		}     
		else
			statusField.val(smemstatus.replace("r",""));
	
		ChangeFont(statusField[0]);
	}
}

// Add % for Commission
// --------------------
function AddPrecentageSign(OperField){
    var NodeCount = parseInt(OperField.parentNode.childNodes.length) ;
    var AddInd = true;
    if (NodeCount == 1 )
    { AddInd  = true; }
    else
    {
        if (NodeCount == 2 && OperField.parentNode.childNodes[1].innerText !=="%")
        { AddInd  = true; }
        else
        { return; }
    }   

    if (AddInd)
    {        	
        var newInput = document.createElement('text');    
        newInput.innerHTML = "%"; 
        OperField.parentNode.appendChild(newInput);
    }
}

// check sales Channel and commission
// ------------------------------------
function ChnlAndComChecking(){
   var ChannelObiectName = "field_6_1";
   var n = document.getElementsByName(ChannelObiectName).length;
   var k = 0;
   var BussChannelObj = document.getElementsByName(ChannelObiectName);
   for (var i=0; i<n; i++)
   {
       if(BussChannelObj[i].checked)
       {
          k = 1;

          if (BussChannelObj[i].value == "A" || BussChannelObj[i].value == "B")
          {
              if (!PercentageChecking())
                  return false;
          }  
          else
              return true;
       }
    }
   if(k==0)
   {
       alert("请选业务渠道");
       document.getElementsByName(ChannelObiectName)[0].focus();
       return false;
   }
   
   return true;
}

// check commission percentage
// ------------------------------------
function PercentageChecking(){
    var ItemCount =  document.getElementById("formTable7").rows.length;
    var RowCount = ItemCount;
    var totalP = 0;

    for(var i=0; i < RowCount -1 ; i++)
    { 
      if (document.getElementsByName("field_7_1")[i].value.trim() == "" 
          || document.getElementsByName("field_7_2")[i].value.trim() == ""
          || document.getElementsByName("field_7_3")[i].value.trim() == "" 
          || document.getElementsByName("field_7_4")[i].value.trim() == ""
          || document.getElementsByName("field_7_5")[i].value.trim() == ""
          )
      {
          alert ("请填写完整的佣金信息");
          document.getElementsByName("field_7_2")[0].focus();
          return false;
      }
      totalP = (parseInt(document.getElementsByName("field_7_5")[i].value) )+ totalP  ;
    }

    if ( totalP !== 100 ){
	     alert("请确保佣金分配比例为100%  现在是" + totalP + "%" );
	     
	     if (document.getElementsByName("field_7_5").length == "0"){return false;}
	     document.getElementsByName("field_7_5")[0].focus();
	     return false;
    }
    else
    {
    	return true;
    }

}

function PaymethodChecking(){
	try{
	
		var CheckBoxObj = jQuery('input[name="field_2_5"]:checked');
		var PaymethodValue = jQuery('select[name="field_2_9"]');
		
		if((CheckBoxObj.val()== 3) && (jQuery.trim(PaymethodValue.val())== ""))
		{
		  alert("请填写其他付款方式明细");
		  PaymethodValue.focus();
		  return false;
		}
	    else
		{
		  return true;
		}
	}
	catch(e){alert("PaymethodChecking:  " + e.message);}
}

function GetLeastInsuranceCharge(){
	var sql = "select param_value as LeastInsuranceCharge from teflow_param_config where param_code = 'least_insurance_charge'";
	
	var data = selectFromSQLJson("",sql);
	
	if(data.length>0)
		return parseInt(data[0].LEASTINSURANCECHARGE);
	else
		return 0;
}

//check total insurance charge,should be >=1500
function TotalInsuranceChargeChecking(){
	try{
		var LeastInsuranceCharge = GetLeastInsuranceCharge();
		var TotalInsuranceCharge = jQuery('input[name="field_10_1"]');

		if(TotalInsuranceCharge.val()<LeastInsuranceCharge)
		{
		  //alert("保险费总计不能小于" + LeastInsuranceCharge+"！");
		  TotalInsuranceCharge.focus();
		  return false;
		}
	    else
		{
		  return true;
		}
	}
	catch(e){alert("TotalInsuranceChargeChecking:  " + e.message);}
}

// Get Commission Count
// --------------------
function GetCommCount(){
 	return document.getElementById("formTable7").childNodes.item(0).childNodes.length;
}

function GetLeastMembers(){
	var LeastMembersSwtich;
	var LeastMembers;
	var LeastMembersSwtich_SQL="SELECT RTRIM(PARMVALUE) as LeastMembersSwtich FROM TSYSPARMH WHERE PARMDESC = 'ChkNoOfMem' AND PARMTYPE = 'BILL'";
	var LeastMembers_SQL="SELECT RTRIM(PARMVALUE) as LeastMembers FROM TSYSPARMH WHERE PARMDESC = 'MinNoOfMem' AND PARMTYPE = 'BILL'";
	var data = selectFromSQLJson("compnwJNDI",LeastMembersSwtich_SQL);
	if(data.length>0){
		LeastMembersSwtich=data[0].LEASTMEMBERSSWTICH;
		if (LeastMembersSwtich!='Y')
		{
			return 0;
		}
	}
	else
	{
		return 0;
	}
	var data = selectFromSQLJson("compnwJNDI",LeastMembers_SQL);
	if(data.length>0){
		LeastMembers=data[0].LEASTMEMBERS;
		return LeastMembers;
	}
	else
	{
		return 0;
	}
}

//check total policy members count,should be >=5
function TotalPolicyMembersChecking(){
	var nMembersCount=0;
	var LeastMembers=GetLeastMembers();
	var AryChkPlancode =  jQuery('input[name="field_9_16"]');

	if(AryChkPlancode.length>0){
		AryChkPlancode.each(function(){
			if (jQuery(this).val()!="")
				nMembersCount = nMembersCount + 1;
		});
	}

	if(nMembersCount<LeastMembers)
	{
	  //alert("参保人数不能小于"+LeastMembers+"!");
	  jQuery('table[id="formTable9"]').focus();
	  return false;
	}
    else
	  return true;
}


function getSystemId4Pending(){
	var SYSTEMIDSArr=[];
	var GetSystemId4Pending_SQL = "select option_value as SYSTEMIDS from teflow_base_data_detail where master_id=(select top 1 master_id  from teflow_base_data_master where field_name='PendingFormList' );";
	var data = selectFromSQLJson('',GetSystemId4Pending_SQL);
	for(var i=0;i<data.length;i++){
		SYSTEMIDSArr.push(data[i].SYSTEMIDS);
	}
	return SYSTEMIDSArr;
   
}
// Call detail Submit function
// ------------------------------
function SubmitValidation(){
	//backup original SubmitValidation() at 20140408
	//获取SYSTEMID集合stringObj.match(rgExp) 
	if(jQuery("textarea[name='field_02_6']").length>0)
	{
		var SYSTEMIDS=getSystemId4Pending().toString();
		
		var plno_field = jQuery("textarea[name='field_02_6']")[0];
		jQuery('div.wizard-steps-parent2').each(function (){
			jQuery(this).parent().parent().remove();
		});
		
		if (!DupChecking_Pol(plno_field)){return false;}
		
	    if (!PaymethodChecking()){return false;}
	
	    if (!ChnlAndComChecking()){return false;}
	         
	    if(MemberRowValidation())
		{	
			//nova-87 -- only applied to GD at this stage
	    	if(!SYSTEMIDS.match(formSystemId))
			{
				var errormessage = GetMessage("E000");
				alert(errormessage);
				jQuery('#formTable9').focus();	    	
				return false;	
			}
		}
	   
	    //NOVA-87
		if (!TotalPolicyMembersChecking()){
			//Z07
			if(!SYSTEMIDS.match(formSystemId)){		
				return false;
			}else{
				AddPendingDataIndexOf("Z07");
			}
		
		}
	    //NOVA-87
		if (!TotalInsuranceChargeChecking()){
			//PENDING ITEM: N27
			if(!SYSTEMIDS.match(formSystemId)){
				return false;			
			}else{
				AddPendingDataIndexOf("N27");			
			}
		}
		
		//if (CheckMemberNovaDuplicate()) {return false;}
		
		//NOVA-78 20150310 Hinson begin
		if (!valMemProdPlanExists()){return false;}
		//NOVA-78 20150310 Hinson end
	}
	
	return true;
	
    // if (~PercentageChecking()){return};
    // PercentageChecking();
}
//NOVA-78 20150310 Hinson begin
function getFormType(){
	var arrayformType = jQuery("input[name='request_no']").val().split('_');
	var formType = '';
	for(var i=0; i < arrayformType.length-2; i++ ){
		if(i==0){
			formType=arrayformType[i];
		}else{
			formType=formType + '_' + arrayformType[i];
		}
	}
	return formType;
}

function ifMemProdPlanExists(psMemProd,psMemPlan){
	var returnvalue = false;
	var sPolPdt_Field = '';
	var sPolPdtPln_Field = '';
	if( formSystemId == '20264' || formSystemId == '20261'){ //GD, JS
		sPolPdt_Field = 'Product_id';
		sPolPdtPln_Field = 'Plan_id';
	}else if( formSystemId == '20267'){ // GCPA
		sPolPdt_Field = 'field_4_1';
		sPolPdtPln_Field = 'field_4_3';
	}else if( formSystemId == '20259'){ // SZ
		sPolPdt_Field = 'field_4_19';
		sPolPdtPln_Field = 'Plan_id';
	}	
	
	var objsProduct = jQuery("select[name='" + sPolPdt_Field + "']");
	if(objsProduct.length>0){
		jQuery.each(objsProduct,function(i,item){
			fsPolPrd = jQuery(item).find("option:selected").val();
			fsPolPlan = jQuery(item).closest('tr').find("select[name='" + sPolPdtPln_Field + "']").find("option:selected").val();
			if(fsPolPrd==psMemProd && fsPolPlan==psMemPlan){
				returnvalue = true;
				return false;
			}
		});
	}
	else{
		returnvalue = false;
	}
	return returnvalue;
}

function valMemProdPlanExists(){	
	var invalidMemProdPlan = false;
	var sMemPdt_Field = '';
	var sMemPdtPln_Field = '';
	if(formSystemId == '20264' || formSystemId == '20261'){ //GD, JS
		sMemPdt_Field='MemProduct_id';
		sMemPdtPln_Field='MemPlan_id';
	}else if(formSystemId == '20267'){ // GCPA
		sMemPdt_Field='field_9_20';
		sMemPdtPln_Field='field_9_21';		
	}else if(formSystemId == '20259'){ // SZ
		sMemPdt_Field='field_9_20';
		sMemPdtPln_Field='MemPlan_id';		
	}	
	
	var objsMemProd = jQuery("select[name='" + sMemPdt_Field + "']");
//	alert(objsMemProd.length);
	if(objsMemProd.length>0){
		jQuery.each(objsMemProd,function(){
			fsMemProd = jQuery(this).find("option:selected").val();
			fsMemPlan = jQuery(this).closest('tr').find("select[name='" + sMemPdtPln_Field + "']").find("option:selected").val();
//			alert(fsMemProd + fsMemPlan);
			if(fsMemProd=='' || fsMemPlan==''){
				invalidMemProdPlan = true;
				return false;
			}
			if(!ifMemProdPlanExists(fsMemProd,fsMemPlan)){
				invalidMemProdPlan = true;	
				return false;				
			}
		});
	}
	if(invalidMemProdPlan){
		alert("人员产品计划与保单产品计划不一致！");
		return false;
	}else{
		return true;
	}	
}
//NOVA-78 20150310 Hinson end

function beforeFormValid(){
	if(GenCLNTCODE())
	{
		if(!GenCERTNO())
			return false;
	}
	else
		return false;
}

function validationSubmit(){
    if (SubmitValidation()) 
          return true;
    else
          return false;
}

function validationUpdate(currentNodeId){
	return pendCheching(currentNodeId);
}

function pendCheching(currentNodeId){
	//NOVA-87
    //等待照会
    var bPendExit = true;
    
    if (getCurrentNodeName(currentNodeId) == "等待照会"){
		var AryChkPend =  jQuery('input[name="field_Pend_5"]');

		if(AryChkPend.length>0){
			AryChkPend.each(function(){
				if(jQuery(this).prop('checked') == false){
					bPendExit = false;
				}											
			});
		}		
    }
    
    //NOVA-87: add condition for Pending Note checking 
    if(bPendExit==false){
			alert("当前仍有未完成照会事项，请检查");
			jQuery('#formTablePend').focus();
			bPendExit = false;
    }
    
    return bPendExit;
}

function getCurrentNodeName(currentNodeId){
	var data = selectFromSQLJson('',"select top 1 t2.node_name from teflow_wkf_define t1 " +
			" inner join teflow_wkf_detail t2 on t1.flow_id = t2.flow_id and t1.form_system_id = " + formSystemId + " and t2.node_id = " + currentNodeId) 
	var nodeName = "";
	
	if (data.length > 0) 
		node_name = data[0].NODE_NAME;
	
	return node_name;
}

function validationBeforeSubmit(){

	if (CheckMemberNovaDuplicate()) 
	{		
		if(jQuery('div.errorDiv').length>0)
			return false;
		else
		{
			if(confirm('是否跳过NOVA中重复用户检测'))
				return true;
			else
				return false;
		}
	}
	else
		return true;
}

function GenCLNTCODE() {
	var ClntCodeField = jQuery("textarea[name='field_02_16']");
	var ClntCodeFieldhidden = jQuery("input[type='hidden'][name='field_02_16']");
	
	var ClntCode = jQuery.trim(ClntCodeField.val());

	if (ClntCode != '')
		return true;
	
	var data = selectFromSPJson("compnwJNDI","dbo.UspGNOVACompassClntCode","");
    if (data.length > 0) 
    {
        ClntCodeField.val(data[0].SYSGENNUM);
        ClntCodeFieldhidden.val(data[0].SYSGENNUM);
    }
    else
    {
      RefreshPic(ClntCodeField,1);
      return false;
    }
    return true;
}

function GenCERTNO(){
	var ClntCode = jQuery.trim(jQuery("textarea[name='field_02_16']").val());
	var memberTableTr = jQuery("#formTable9 tr");
	if (ClntCode != '' && memberTableTr.length>0)
	{
		var data, certNOField, userNameField, occupationField, empdtField,effDate;
		
		jQuery.each(memberTableTr,function(i,item){
			
			certNOField = jQuery(item).find("textarea[name='field_9_1']");
			
			if(i==0){return;}
			var Certno = certNOField.text();
			var memIDNo = jQuery(item).find("textarea[name='field_9_7']").text();
			
			if (Certno != ''){ 
				if(validateCertNO(ClntCode,Certno, memIDNo))
					return;
				else
					certNOField.text('');
			}
			
			
			userID = jQuery(item).find("textarea[name='field_9_7']").val();
			
			effDate = jQuery("input[name='field_02_13']").val();
			
			var GetCertType = "S"
			if(effDate!="")
			{
				data = selectFromSPJson("compnwJNDI","dbo.UspGNOVACompassGetCertNo",ClntCode+","+userID+","+GetCertType+','+effDate);
			
				//var temp =  ClntCode+","+userID+","+"'S'";
				
				userNameField = jQuery(item).find("textarea[name='field_9_2']");
				occupationField = jQuery(item).find("textarea[name='field_9_10']");
				empdtField = jQuery(item).find("input[name='field_9_12']");
				
				if(data!=null && data.length>0)
				{
					if(data[0].CERTNO!='')
						certNOField.val(data[0].CERTNO);
					if(data[0].NAMELAST!='')
						userNameField.val(data[0].NAMELAST);
					if(data[0].EMPDT!='')
						empdtField.val(formatDate1(data[0].EMPDT,"MM/dd/yyyy",dateFormat));
				}
			}
		});
	}
	return true;
}

function validateCertNO(ClntCode,Certno,memIDNo){
	var data = selectFromSQLJson("compnwJNDI","SELECT COUNT(1) AS COUNTN FROM tmember where clntcode = '"+ClntCode+"' " +
			" and ((certno = '" + Certno + "' and MEMIDNO <> '"+memIDNo+"') " +
			" or (certno <> '" + Certno + "' and MEMIDNO = '"+memIDNo+"'))");
	
	if(data.length>0)
	{
		if(data[0].COUNTN>0)
			return false;
		else
			return true;
	}
	else
		return false;
}

function GenCERTNOByID(OperField){
	var userID = jQuery(OperField).val();
	var ClntCode = jQuery.trim(jQuery("textarea[name='field_02_16']").val());
	
	if (ClntCode != '' && userID != '')
	{
		var GetCertType = "N"
		var effDate = jQuery("input[name='field_02_13']").val();
		
		if(effDate!="")
		{
			var data = selectFromSPJson("compnwJNDI","dbo.UspGNOVACompassGetCertNo",ClntCode+','+userID+','+GetCertType+','+effDate);
			if(data!=null && data.length>0)
			{
				var certNOField = jQuery(OperField).closest('tr').find("textarea[name='field_9_1']");
				var userNameField = jQuery(OperField).closest('tr').find("textarea[name='field_9_2']");
				var occupationField = jQuery(OperField).closest('tr').find("textarea[name='field_9_10']");
				var empdtField = jQuery(OperField).closest('tr').find("input[name='field_9_12']");
				
				if(data[0].CERTNO!='')
					certNOField.val(data[0].CERTNO);
				if(data[0].NAMELAST!='')
					userNameField.val(data[0].NAMELAST);
				/*if(data[0].OCCUPATION!='')
					occupationField.val(data[0].OCCUPATION);*/
				if(data[0].EMPDT!='')
					empdtField.val(formatDate1(data[0].EMPDT,"MM/dd/yyyy",dateFormat));
			}
		}
	}
}

// 通用单选方法
function clearBtn(box){
	if(jQuery(box).attr('checked')=="checked"){
	  jQuery(':checkbox[name="'+box.name+'"]').removeAttr('checked'); 
	  jQuery(box).attr('checked','checked'); 
	}
}

// 校验银行信息入口
function bankValidate(){
	return valiBankMsg('field_2_3','field_2_6','field_2_7','field_2_8') &&
	     valiBankMsg('field_11_9','field_11_10','field_11_11','field_11_12');
}

// 银行信息校验方法
function valiBankMsg(f1,f2,f3,f4){
	var bank = jQuery('select[name="'+f1+'"]').find('option:selected').val();
	var branchName= jQuery('textarea[name="'+f2+'"]').val();
	var account=jQuery('textarea[name="'+f3+'"]').val();
	var province=jQuery('select[name="'+f4+'"]').find('option:selected').val();
	
	if(bank=='' || branchName=='' || account=='' || province==''){
		alert("银行信息填写不完整,请补充完整后重新提交！");
		return false;
	}
	else
		return true;
}

function MemberRowValidation(){
	//NOVA-87
	var bMemberDup= false;
	var memstatus = "",memIDNoField;
	jQuery('#formTable9 textarea[name="field_9_19"]').each(function (i) 
	{
		if(memstatus == "")
			memstatus=jQuery(this).text()+memstatus;
		
		if(jQuery(this).text()!="")
		{
			memIDNoField = jQuery(this).closest("tr").find('textarea[name="field_9_7"]');
			memIDNoField.trigger("onfocus");
			memIDNoField.trigger("onblur");
			
			//NOVA-87
			//alert("i is: " + i + "this.text is: " + jQuery(this).text());
			if(jQuery(this).text().toString().indexOf("q")>= 0 ){
				bMemberDup = true;
			}
			
			AddPendingData(jQuery(this).text());
		}
	});

	if (memstatus!=="")
		return true;
	else
		return false;	
}

//NOVA-87
function AddPendingData(InputId){
	var SYSTEMIDS=getSystemId4Pending().toString();
	//only applied at GD NBForm at this stage
    if(!SYSTEMIDS.match(formSystemId))
		return;	

	//this function will use charAt to loop and identify whether keyword character exists in InputId, in this to retrieve pending description & remark
    
	var isExist = "N";
	var sPendingCode = "";
	var sPendingDesc = "";
	var sPendingRemark = "";
	
	for(var i = 0; i < InputId.trim().length; i++ ){
		isExist = "N";
		sPendingCode = "";
		sPendingDesc = "";
		sPendingRemark = "";
		
		switch (InputId.trim().charAt(i))
		{
			case "r":
				sPendingCode = "Z01";
				break;
		
			case "y":
				//NOVA-236				
				//sPendingCode = "Z02";
				sPendingCode = "P30";
				break;
		
			case "q":
				//NOVA-236				
				//sPendingCode = "Z03";
				sPendingCode = "P31";
				break;
				
			case "w":
				sPendingCode = "Z04";
				break;							
			 	
			default:
				sPendingCode = "Z99";
				sPendingDesc = "其他事项";
				sPendingRemark  = InputId.trim();
				break;			
		}
		
        //check pending code
		var aPendResult = checkPendingCode(sPendingCode);
		isExist         = aPendResult[0];
		sPendingCode    = aPendResult[1];
		sPendingDesc    = aPendResult[2];
		sPendingRemark  = aPendResult[3];
				
	}
}

function AddPendingDataIndexOf(InputId){
     var SYSTEMIDS = getSystemId4Pending().toString();
	//only applied at GD NBForm at this stage
    if(!SYSTEMIDS.match(formSystemId))
		return;	

	//this function will use IndexOf to identify whether keyword exists in InputId, in this to retrieve pending description & remark  
	
	var isExist = "N";
	var sPendingCode = "";
	var sPendingDesc = "";
	var sPendingRemark = "";

	//default pending code & description
	sPendingCode = "Z99";	
    
    if (InputId.trim().toString().indexOf("Z05")>= 0){
		sPendingCode = "Z05";    	
    }
    
    if (InputId.trim().toString().indexOf("Z06")>= 0){
		sPendingCode = "Z06";    	
    }    
    
    if (InputId.trim().toString().indexOf("Z07")>= 0){
		sPendingCode = "Z07";    	
    }

    if (InputId.trim().toString().indexOf("N27")>= 0){
		sPendingCode = "N27";    	
    }

	var aPendResult = checkPendingCode(sPendingCode);
	isExist        = aPendResult[0];
	sPendingCode   = aPendResult[1];
	sPendingDesc   = aPendResult[2];
	sPendingRemark = aPendResult[3];    
	
}

function checkPendingCode(PendingCode){

	var sPendingExists = "N";
	var sPendingCode = "";
	var sPendingDesc = "";
	var sPendingRemark = "";
	
	sPendingCode = PendingCode.trim();
	
	var sql = "SELECT top 1 PendCode as PENDCODE, PendScope as PENDSCOPE, PendIssue AS PENDISSUE, PendDesc AS PENDDESC, PendRemark AS PENDREMARK from TNOVAPENDINGLIST WHERE PendCode = '" +sPendingCode+ "'";
	var data = selectFromSQLJson('',sql);
	
	if(data!="" && data != null){	
		sPendingDesc   = data[0].PENDDESC;
		sPendingRemark = data[0].PENDREMARK;				
	}
	
	jQuery("select[name='PendingList']").each(function(){
		if(jQuery(this).val().trim() == sPendingCode){
			sPendingExists = "Y";
			return false;
		}
	});
	
	if (sPendingExists == "N"){
		createTableSectionRow("formTablePend","Pend","0");
		jQuery("#formTablePend tr:last select[name='PendingList']").combobox("setValue",sPendingCode);
		jQuery("#formTablePend tr:last textarea[name='field_Pend_3']").val(sPendingDesc);
	}
	
	return [sPendingExists, sPendingCode, sPendingDesc, sPendingRemark];
	
}

//nova-87
function changePendingData(selectedID,srcChange){
	//change pending content & remark when pending list changed	
	
	var strPendDesc   = jQuery(selectedID).closest('tr').find('textarea[name="field_Pend_3"]');	
	
	//retrieve the pending list key for reference
	var selectedVal = jQuery(selectedID).val();
	
	//No handle for "Z00"
	if (selectedVal == "Z00"){
		return true;
	}		
		
	var sql = "SELECT top 1 PendCode as PENDCODE, PendScope as PENDSCOPE, PendIssue AS PENDISSUE, PendDesc AS PENDDESC, PendRemark AS PENDREMARK from TNOVAPENDINGLIST WHERE PendCode = '" +selectedVal+ "'";
	var data = selectFromSQLJson('',sql);
	
	if(data!="" && data != null)
		strPendDesc.val(data[0].PENDDESC);
	else	
		return false;		
}
				
function GetMessage(MessageCode){
	 var Message = "";
	 var sql = "SELECT option_label FROM teflow_base_data_detail where master_id='10138' AND option_value = '" + MessageCode + "'";
     var xmlObj = selectFromSQL(sql,2);
     
     if(xmlObj){
		var item = jQuery(xmlObj).find("item");
		if(item.length>0){ Message = item.find('OPTION_LABEL').text(); }
     }
     return Message;
}

function ShowSearch(OperItem){
	if(jQuery(OperItem).length>0)
		jQuery(OperItem).after(jQuery('<span/>').addClass('t-loading'));
	
	var outTimer1 = setTimeout(function(){
		if(ClientNameSearch(OperItem,0))
			jQuery('textarea[name="field_02_26"]').focus();
		
		if(jQuery(OperItem).siblings('span.t-loading').length>0)
			jQuery(OperItem).siblings('span.t-loading').remove();
		
		clearTimeout(outTimer1);
	},  100);
}

function DupChecking_Pol(OperItem){
	
	  RefreshPic(OperItem,99,"");

      var CheckValue =  OperItem.innerText.trim();
      if(CheckValue ==''){
	      //OperItem.focus();
	      return false;
	  }
      else{
	      CheckValue = ImproveLength(CheckValue,10,"0");
	      OperItem.innerText = CheckValue ;
      }

	  RefreshPic(OperItem,0,"");

	 if (CheckValue.substr(0,1) != "G")
	 {
		RefreshPic(OperItem,1,"保单必须以G开头，请核查！");
		OperItem.focus();
		return false;
	 }

	var strSql = "SELECT POLNO FROM TPOLICY(NOLOCK) WHERE POLNO = '"+ CheckValue +"'";
	var data = selectFromSQLJson('compnwJNDI',strSql);

	if (data.length>0)
	{
		RefreshPic(OperItem,1,"保单已存在！");
		OperItem.focus();
		return false;
	}
	else
	{
		RefreshPic(OperItem,2,"");
		return true;
	}
}

function DupChecking_Client(OperItem){
	  if(jQuery(OperItem).length>0)
			jQuery(OperItem).after(jQuery('<span/>').addClass('t-loading'));
	  
	  RefreshPic(OperItem,99,"");
	  
	  var outTimer1 = setTimeout(function(){
		  var CheckValue =  OperItem.innerText.trim();
	      if(CheckValue ==''){
	    	  jQuery('textarea[name="field_02_17"]').val("");
	          jQuery('textarea[name="field_02_26"]').val("");
	          if(jQuery(OperItem).siblings('span.t-loading').length>0)
					jQuery(OperItem).siblings('span.t-loading').remove();
	    	  return false;
	      }
	      else{
	    	CheckValue = ImproveLength(CheckValue,5,"0");
	    	OperItem.innerText = CheckValue ;
	      }
	
	      RefreshPic(OperItem,0,"");
	      
	      // get clntcode 
	      var strSql = "SELECT CLNTCODE, NOVA.funReplaceSpecN(CLNTNAME,0) as CLNTNAME, NOVA.funReplaceSpecN(CLNTNAMEGEN,0) as CLNTNAMEGEN FROM TCLIENT WHERE CLNTCODE = '"
	    	  + CheckValue +"' AND RCDSTS<>'D'";
	      var PolExistNo= "";
	      var data = selectFromSQLJson('compnwJNDI',strSql);
	      
	      if (data.length>0)
	      {
	          RefreshPic(OperItem,10,"");	
	          RefreshPic(OperItem,3,"投保人编号已存在");
	          
	          jQuery('textarea[name="field_02_17"]').val(data[0].CLNTNAME);
	          jQuery('textarea[name="field_02_26"]').val(data[0].CLNTNAMEGEN);
	          jQuery('textarea[name="field_02_17"]').trigger('blur');
	          //jQuery('input[name="field_02_13"]').focus();
	      }
	      else
	      {
	    	  RefreshPic(OperItem,2,"");
	    	  jQuery('textarea[name="field_02_17"]').val("");
	    	  jQuery('textarea[name="field_02_17"]').focus();
	          jQuery('textarea[name="field_02_26"]').val("");
	          //jQuery('textarea[name="field_02_17"]').focus();
	      }
	      
	      if(jQuery(OperItem).siblings('span.t-loading').length>0)
				jQuery(OperItem).siblings('span.t-loading').remove();
	      
	      clearTimeout(outTimer1);
		},  100);
}

function IdValidationClntPross(OperItem)
{
  if (OperItem.innerText.trim()==""){return;}
  
  if (!IdValidation(OperItem,1))
	  RefreshPic(OperItem,3,"输入的证件号码并非身份证件号");
  else
	  RefreshPic(OperItem,2,"");
}

function MailFormatChecking(OperItem)
{
  try
  {   
	  RefreshPic(OperItem,99,"");
	  RefreshPic(OperItem,0,"");
	  
	  var EmailAddress  =  OperItem.innerText.trim(); 

	  if (EmailAddress.trim() !== "")
	  {
		  var reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
		  if(!reg.test(EmailAddress))
		  {
			  RefreshPic (OperItem,1,"请检查您的电子邮箱地址");
			  OperItem.focus();
		  }
		  else
			  RefreshPic(OperItem,2,"");
	  }
	  else
	  {
		  RefreshPic(OperItem,99,"");  
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


/*
function makeFixed()
{   
	var div  =   document.getElementById('webBgLayer');      
	var clientWidth    =   (document.documentElement || document.body).clientWidth;      
	var clientHeight   =   (document.documentElement || document.body).clientHeight;
	var top            =   (document.documentElement || document.body).scrollTop + (div.offsetHeight>clientHeight ? 0 : (clientHeight-div.offsetHeight)/2);       
    var div.style.left      =   (clientWidth-div.offsetWidth)/2 +'px';       
    var div.style.top       =   top + 'px';    
}
*/

function CheckAgent(){
	var CheckBoxObj = jQuery('input[name="field_6_1"]:checked');
   var ChannelObiectName = "field_6_1";
   var n = document.getElementsByName(ChannelObiectName).length;
   var k = 0;
   var BussChannelObj = document.getElementsByName(ChannelObiectName);
   var ItemCount =  document.getElementById("formTable7").rows.length;

   for (var i=0; i<n; i++)
   {
       if(BussChannelObj[i].checked)
       {
          if ((BussChannelObj[i].value != "D")&& ItemCount == 1) 
          {
        	  createTableSectionRow("formTable7","7","0");
        	  document.getElementsByName("field_7_5")[0].value = "100";
        	  document.getElementsByName("field_7_5")[0].click();
        	  
        	  //20150304 Justin Bin, validate the commission rate
        	  if(jQuery('select[name="field_6_6"]').val()=="" && CheckBoxObj.val()!="D" && formSystemId!="30328"){
        		  alert("请选择佣金率！");
        		  return false;        		  
        	  }
        	 /* if(document.getElementById("combox_transform_field_6_6").value==""){
        		  document.getElementById("combox_transform_field_6_6").value="PF200 FLAT 20%"  
        		  //jQuery('select[name="field_6_6"]').val("PF200");
        	  }
        	  document.getElementById("combox_transform_field_6_6").disabled="none";*/
        	  return true;
          }  
          else
          {   
        	  if(BussChannelObj[i].value == 'D')
        	  {
        		  if(jQuery("#formTable7 tr").length>1)
        		  {
	        		  for (var j=0;j<ItemCount-1; j++)
	        		  {
	        			  if (!document.getElementsByName("chkid_7")[j].checked)
	        				  	document.getElementsByName("chkid_7")[j].click();
	        		  }	  
	        		   delAllRow("formTable7","chkid_7");
        		  }
        		//20150304 Justin Bin
        		  /*document.getElementById("combox_transform_field_6_6").disabled="disabled";*/
        		  //document.getElementById("combox_transform_field_6_6").value="";
        		  
        		  //jQuery('select[name="field_6_6"]').val("0");
        		  return true;
        	  }
        	  /*document.getElementById("combox_transform_field_6_6").disabled="none";*/
        	  return false;
          }
       }
    }
   
	return true;
}

function checkAgentManual()
{
	  var ItemCount =  document.getElementById("formTable7").rows.length;
	  if (ItemCount == 2 && document.getElementsByName("field_7_5")[0].value.trim() == "")
	  {
	    document.getElementsByName("field_7_5")[0].value = "100";
	    document.getElementsByName("field_7_5")[0].click();
	    return true;
	  }
	  else
		return false;  
}

function NoofMemValidation(){ 
	  if(jQuery('input[name="field_5_1"]').val()!==""&&jQuery('input[name="field_5_2"]').val()!=="" )
      {
		  var reason = document.getElementsByName("field_5_3")[0];
		  var NoofMem= document.getElementsByName("field_5_2")[0];
		  
          var DetailReason = jQuery('textarea[name="field_5_7"]');
          RefreshPic(reason,99,"");
          RefreshPic(NoofMem,99,"");
          
	        if(jQuery('input[name="field_5_1"]').val()> jQuery('input[name="field_5_2"]').val())
	        {
	              RefreshPic(reason,4,"请选择未能全员参保原因!");
	        }
	        
	        if(jQuery('input[name="field_5_1"]').val()== jQuery('input[name="field_5_2"]').val())
	        {     
	              //reason.empty();
	              jQuery('textarea[name="field_5_7"]').text("");
	              jQuery('select[name="field_5_3"]').combobox("setValue","");
	        }
	
	        if(jQuery('input[name="field_5_1"]').val()< jQuery('input[name="field_5_2"]').val())
	        {     
	              RefreshPic(NoofMem,1,"检查是否录入错误，若无误需发照会跟客户确认");
	        }
      }
}

function SetReason(){
      var DetailReason = jQuery('textarea[name="field_5_7"]');
          DetailReason.val(jQuery('select[name="field_5_3"]').find('option:selected').text())
}

function effectDayVali(effectDay){
	var waitDay = jQuery('textarea[name="field_02_24"]').val();
	var contract = new Date(jQuery('input[name="field_02_13"]').val()).getTime();
	var oldDay = new Date(effectDay.getAttribute("value")).getTime();
	if(contract < oldDay + waitDay*60*60*24*1000){
		alert("劳动合同生效日期+等候期 应该<=保险合同生效日期");
	}
}


function ResetProducer(OperItem) {
	var strPrduCode = "";
	var nLength = 0;
	var CurrentRow = OperItem.parentNode.parentNode.rowIndex;
	
	document.getElementsByName("field_7_1")[CurrentRow - 1].value = "";
	document.getElementsByName("field_7_3")[CurrentRow - 1].value = "";
	document.getElementsByName("field_7_4")[CurrentRow - 1].value = "";
}

function ProducerReplaceCheck()
{
	if(document.getElementById("formTable7").rows.length == null) {return false; }
	 
	var RowCount =  document.getElementById("formTable7").rows.length;

	for(var i=0; i < RowCount -1 ; i++)
	{ 
	  var CurrentItem = document.getElementsByName("field_7_2")[i].value.trim();
	  //alert ("ProducerReplaceCheck- Previous:" + PreviousMinProducerCode +  "   current:" + CurrentItem);
	  if(PreviousMinProducerCode == CurrentItem)
	  {
		  return true;
	  }
	  else
	  {
		  continue;
	  }
	  
	}
	 return false;
}

function CheckMinProducer(CheckCode)
{
	var ProducerCodeMin = "",BlankVlau = "";
	if(document.getElementById("formTable7").rows.length == null) {return false; }
	 
	var RowCount =  document.getElementById("formTable7").rows.length;

	for(var i=0; i < RowCount -1 ; i++)
	{ 
	  var CurrentItem = document.getElementsByName("field_7_2")[i].value.trim();
	  //if(CurrentItem == "" || CurrentItem < ProducerCodeMin)
	  if(CheckCode > CurrentItem)
	  {
		  return false;
	  }
	  else
	  {
		  continue;
	  }
	  
	}
	 return true;
}

function BranchInfRetrieve1(OfficeCode,strPrduCode){
	//alert (OfficeCode);
	
	if(OfficeCode == "")
	{
	  return false;	
	}
	
	var sql = "SELECT COMPCODE,BRANCHCODE,COUNTYCODE,OFFCOD,OFFNAME2,OFFNAME1 FROM TNOVABRANCHPRODUCERMAPPING WHERE OFFCOD = '" + OfficeCode + "'";
	var xmlObj = selectFromSQL(sql,2);

	
	//alert (sql);	
	
    if(xmlObj)
    {
		var item = jQuery(xmlObj).find("item");
		if(item.length>0)
		{ 
			//alert ( item.find('BRANCHCODE').text());
			//document.getElementsByName("BranCod_id")[0].value = item.find('BRANCHCODE').text(); 
			
			var BranchSelect = document.getElementsByName("BranCod_id")[0];
			var BranchCodeItem = "";
			//alert (CompanySelect.options.length);
			
			

			var CompanyCode = item.find('COMPCODE').text();
	
			//alert (CommonCompanyCode + "  " + CompanyCode);
			if (CommonCompanyCode != CompanyCode)
			{
				alert ("输入的Producer不包含该公司的Branch code,请检查并重新输入。");
				return false;
			}
	
			
			if(BranchSelect != null)
			{
			
				for (var i = 0; i < BranchSelect.options.length; i++) 
				{ 
					
					//alert (BranchSelect.options[i].value + "  " + item.find('BRANCHCODE').text());
					var TMPCode = CompanyCode + item.find('BRANCHCODE').text();
					//alert (TMPCode);
					if (BranchSelect.options[i].value == TMPCode) 
					{ 
						BranchCodeItem = BranchSelect.options[i].text; 
						
						PreviousMinProducerCode = strPrduCode;
						
						break; 
					} 

				} 
			
				var ComboBoxItem = document.getElementById("combox_transform_select_BranCod_id");
				if(ComboBoxItem == null)
				{
					return false;
				}
				
				ComboBoxItem.value = BranchCodeItem;
				//CompanySelect.click();
			}
			//alert ("BranchCode" + BranchCodeItem);
			//document.getElementsByName("field_BranchInf_1")[0].value = item.find('COUNTYCODE').text(); 
			document.getElementsByName("field_BranchInf_2")[0].value = item.find('OFFCOD').text(); 
			document.getElementsByName("field_BranchInf_3")[0].value = item.find('OFFNAME2').text(); 
			document.getElementsByName("field_BranchInf_4")[0].value = item.find('OFFNAME1').text();
			
			return true;
		}
		else
		{
			return false;
		}
	
    }
    else
    {
    	return false;
    }
}

function BranchInfRetrieve(OfficeCode,strPrduCode){
	
	if(OfficeCode == "")
	{
	  return false;	
	}
	
	var sql = "SELECT COMPCODE,BRANCHCODE,COUNTYCODE,OFFCOD,OFFNAME2,OFFNAME1 FROM TNOVABRANCHPRODUCERMAPPING WHERE OFFCOD = '" + OfficeCode + "'";
	var xmlObj = selectFromSQL(sql,2);
	
    if(xmlObj)
    {
		var item = jQuery(xmlObj).find("item");
		if(item.length>0)
		{ 
			var BranchSelect = document.getElementsByName("BranCod_id")[0];
			var BranchCodeItem = "";
			
			var CompanyCode = item.find('COMPCODE').text();
	
			if (CommonCompanyCode != CompanyCode)
			{
				alert ("输入的Producer不包含该公司的Branch code,请检查并重新输入。");
				return false;
			}
			
			var TMPCode = CompanyCode + item.find('BRANCHCODE').text();
		    //document.getElementsByName("BranCod_id")[0].value = TMPCode;
		    jQuery('select[name="BranCod_id"]').combobox("setValue",TMPCode);
		    //changeBranchCode();
		    changeCityCode();
		    //alert (item.find('COUNTYCODE').text().substr(0,4));
			//document.getElementsByName("SecCity")[0].value = item.find('COUNTYCODE').text().substr(0,4);
		    jQuery('select[name="SecCity"]').combobox("setValue",item.find('COUNTYCODE').text().substr(0,4));
			
            //alert (item.find('OFFCOD').text());
			document.getElementsByName("field_BranchInf_2")[0].value = item.find('OFFCOD').text(); 
			document.getElementsByName("field_BranchInf_3")[0].value = item.find('OFFNAME2').text(); 
			document.getElementsByName("field_BranchInf_4")[0].value = item.find('OFFNAME1').text();
			
			return true;
		}
		else
		{
			//alert ("1111");
			//var CityCode = jQuery('select[name="SecCity"]');
			//CityCode.empty();
			return false;
		}
	
    }
    else
    {
		//alert ("2222");
		//var CityCode = jQuery('select[name="SecCity"]');
		//CityCode.empty();
    	return false;
    }
}

function ClearBranchInfo(){
	//document.getElementById("combox_transform_select_BranCod_id").value = "";
	//document.getElementById("combox_transform_select_SecCity").value = "";
	////document.getElementsByName("BranCod_id")[0].value = "";
	////document.getElementsByName("SecCity")[0].value = "";
	
    //var TMPCode = CompanyCode + item.find('BRANCHCODE').text();
    jQuery('select[name="BranCod_id"]').combobox("setValue","");
    jQuery('select[name="SecCity"]').combobox("setValue","");
	
	document.getElementsByName("field_BranchInf_2")[0].value = ""; 
	document.getElementsByName("field_BranchInf_3")[0].value = "";
	document.getElementsByName("field_BranchInf_4")[0].value = "";
}

function loadPic(loadField){
	
	if (jQuery("#field_02_17_ProcessLoading").data("qtip")) 
    {
		jQuery("#field_02_17_ProcessLoading").qtip('hide');
		//jQuery("#field_02_17_ProcessLoading").qtip('destroy');
    }
	//jQuery("div[id^=qtip]").remove();
	RefreshPic(loadField,99,"");
	
	/*if(loadField>1)
		jQuery(loadField).after(jQuery('<span/>').addClass('t-loading'));*/
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

var IsTerminalClient = true;

function ClientNameSearch(OperItem,NameType)
{	
	var ClientName, ClientCodeObj, ClientState;
	var ClientCodeObj = document.getElementsByName("field_02_16")[0];
	var ClientNameObj = jQuery('textarea[name="field_02_17"]');
	
	if(ClientNameObj.length==0){return false;}
	
	var ClientName = ClientNameObj.text().trim();
		
	if (ClientName==null||ClientName==""){return false;}
	
	var data = selectFromSPJson("compnwJNDI","dbo.UspGNOVAGetClntInfo", ","+ClientName);

	RefreshPic(OperItem, 11, "");
	
	if (data.length>0)
	{ 	
		var ClntCode = "";
		
		if(ClientCodeObj.value!="")
			ClntCode = ClientCodeObj.value
		
		var ClntCodeTmp="", ClntNameTmp="", ClntNameGen = "";
		  
		RefreshPic(OperItem, 4, "已存在（点击左侧按钮显示/隐藏）");
		  
		bindQtipClntPol();
		jQuery("#field_02_17_ProcessLoading").qtip('show');
		
		jQuery("select#selSClntCode").empty();
		
		jQuery.each(data, function(i,item){
			jQuery("select#selSClntCode").append("<option value='"+item.CLNTCODE+"'>"
					+item.CLNTCODE+"</option>");  
		});
		
		if(ClntCode!="")
			jQuery("select#selSClntCode").val(ClntCode);
		
		jQuery("select#selSClntCode").trigger("onchange");
		
        return true;
     }
     else {
		return false;
     }
}

function setClntFieldInfo(data){
	if(data!=""){
		if(data.STATUS=="T")
			IsTerminalClient = true;
		else
			IsTerminalClient = false;
	
		jQuery('textarea[name="field_02_16"]').text(data.CLNTCODE);
		
		jQuery('input[type="hidden"][name="field_02_16"]').val(data.CLNTCODE);
		
		jQuery('textarea[name="field_02_17"]').text(data.CLNTNAME); 
		jQuery('textarea[name="field_02_26"]').text(data.CLNTNAMEGEN);
		var ClientCodeObj = document.getElementsByName("field_02_16")[0];
		RefreshPic(ClientCodeObj, 3, "投保人编号已存在！");
		
		if(IsTerminalClient==false)
		{
			jQuery('textarea[name="field_02_9"]').text(data.ADDRESS);
			jQuery('textarea[name="field_02_20"]').text(data.TELNO);
			jQuery('textarea[name="field_02_18"]').text(data.MAILCODE);
			jQuery('textarea[name="field_02_21"]').text(data.EMAILADDR); 
			jQuery('textarea[name="field_02_19"]').text(data.CONTPERSON); 
			jQuery('select[name="field_02_8"]').combobox("setValue",data.SICCODE);
			jQuery('textarea[name="field_02_23"]').text(data.TAXID); 
			jQuery('textarea[name="field_02_25"]').text(data.USERDEFINED1); 
			
			jQuery('textarea[name="field_2_7"]').text(data.BANKAC); 
			jQuery('select[name="field_2_3"]').combobox("setValue",data.BANKCODE);
			jQuery('textarea[name="field_2_6"]').text(data.BANKBRNAME==null?"":data.BANKBRNAME);	//20150316 Justin Bin fixed Bug NOVA-80 
			jQuery('textarea[name="field_2_10"]').text(data.PAYEENAME); 
			jQuery('select[name="field_2_8"]').combobox("setValue",data.CITYCODE);
		}
		else
		{
			jQuery('textarea[name="field_02_9"]').text("");
			jQuery('textarea[name="field_02_20"]').text("");
			jQuery('textarea[name="field_02_18"]').text("");
			jQuery('textarea[name="field_02_21"]').text(""); 
			jQuery('textarea[name="field_02_19"]').text(""); 
			jQuery('select[name="field_02_8"]').combobox("setValue","");
	
			jQuery('textarea[name="field_02_23"]').text(""); 
			jQuery('textarea[name="field_02_25"]').text(""); 
			
			jQuery('textarea[name="field_2_7"]').text(""); 
			jQuery('select[name="field_2_3"]').combobox("setValue","");
			jQuery('textarea[name="field_2_6"]').text(""); 
			jQuery('textarea[name="field_2_10"]').text(""); 
			jQuery('select[name="field_2_8"]').combobox("setValue","");
		}
		
		jQuery("span#spanSClntName").text(data.CLNTNAME);
		jQuery("span#spanSClntNameGen").text(data.CLNTNAMEGEN);
		jQuery("span#spanSClntStatus").text(data.STATUS);
		jQuery("span#spanSClntDate").text(data.STATEFFDT2);
	}
	else{
		jQuery('textarea[name="field_02_9"]').text("");
		jQuery('textarea[name="field_02_20"]').text("");
		jQuery('textarea[name="field_02_18"]').text("");
		jQuery('textarea[name="field_02_21"]').text(""); 
		jQuery('textarea[name="field_02_19"]').text(""); 
		jQuery('select[name="field_02_8"]').combobox("setValue","");
	
		jQuery('textarea[name="field_02_23"]').text(""); 
		jQuery('textarea[name="field_02_25"]').text(""); 
		jQuery('textarea[name="field_02_26"]').text("");		//20150505	Justin Bin
		
		jQuery('textarea[name="field_2_7"]').text(""); 
		jQuery('select[name="field_2_3"]').combobox("setValue","");
		jQuery('textarea[name="field_2_6"]').text(""); 
		jQuery('textarea[name="field_2_10"]').text(""); 
		jQuery('select[name="field_2_8"]').combobox("setValue","");
		
		jQuery("span#spanSClntName").text("");
		jQuery("span#spanSClntNameGen").text("");
		jQuery("span#spanSClntStatus").text("");
		jQuery("span#spanSClntDate").text("");
	}
}

function bindClntSubInfo(clntcode){
	
	if(jQuery('#formTable11 tr').length>2)
	{
		jQuery('#formTable11 tr').each(function(index){
			if(index>1)
				jQuery(this).find('input[name="chkid_11"]').attr("checked",true);
		});
		delAllRow2("formTable11","chkid_11");
	}
	
	if(clntcode!=""&&IsTerminalClient==false){
		var data = selectFromSPJson("compnwJNDI","dbo.UspGNOVAGetClntSubInfo", clntcode);
		
		if(data.length>0)
		{
			jQuery.each(data,function(i,item){
				createTableSectionRow("formTable11", "11", "0");	//20150313 Justin Bin Modified: add null check
				jQuery('#formTable11 tr:last textarea[name="field_11_1"]').text(item.SUBOFFCODE==null?"":item.SUBOFFCODE);
				jQuery('#formTable11 tr:last textarea[name="field_11_2"]').text(item.SUBOFFNAME==null?"":item.SUBOFFNAME);
				jQuery('#formTable11 tr:last textarea[name="field_11_8"]').text(item.SUBOFFNAMEGEN==null?"":item.SUBOFFNAMEGEN);
				jQuery('#formTable11 tr:last textarea[name="field_11_3"]').text(ToDBC(item.ADDRESS==null?"":item.ADDRESS));
				jQuery('#formTable11 tr:last textarea[name="field_11_13"]').text(item.MAILCODE==null?"":item.MAILCODE);
				jQuery('#formTable11 tr:last textarea[name="field_11_6"]').text(item.CONTPERSON==null?"":item.CONTPERSON);
				jQuery('#formTable11 tr:last textarea[name="field_11_5"]').text(item.USERDEFINED1==null?"":item.USERDEFINED1);
				jQuery('#formTable11 tr:last textarea[name="field_11_4"]').text(item.TELNO==null?"":item.TELNO);
				jQuery('#formTable11 tr:last textarea[name="field_11_7"]').text(item.EMAILADDR==null?"":item.EMAILADDR);
				jQuery('#formTable11 tr:last select[name="field_11_9"]').val(item.BANKCODE==null?"":item.BANKCODE);	
				jQuery('#formTable11 tr:last textarea[name="field_11_10"]').text(item.BANKBRNAME==null?"":item.BANKBRNAME);
				jQuery('#formTable11 tr:last textarea[name="field_11_11"]').text(item.BANKAC==null?"":item.BANKAC);
				jQuery('#formTable11 tr:last select[name="field_11_12"]').val(item.CITYCODE==null?"":item.CITYCODE);
			});
		}
	}
}

function clntCodeChange(selectID){
	var clntCode = jQuery(selectID).val();
	jQuery("select#selSPolNo").empty();
	
	if(clntCode!="")
	{
		var data2 = selectFromSPJson("compnwJNDI","dbo.UspGNOVAGetClntInfo",clntCode+', ');
		
		if(data2.length>0){
			
			jQuery("span#spanSClntName").text(data2[0].CLNTNAME);
			jQuery("span#spanSClntNameGen").text(data2[0].CLNTNAMEGEN);
			jQuery("span#spanSClntStatus").text(data2[0].STATUS);
			jQuery("span#spanSClntDate").text(data2[0].STATEFFDT2);
    		
    		var polListSQL = "select T2.POLNO,POLSTATUS = T2.[STATUS], POLSTATEFFDT = convert(varchar, T2.STATEFFDT, 23)  from TCLIENT T1 " +
    				"INNER JOIN TPOLICY T2 ON T1.CLNTCODE = T2.CLNTCODE AND T1.CLNTCODE = '"+clntCode+"' AND T2.RCDSTS <> 'D'";
			
			var data3 = selectFromSQLJson('compnwJNDI',polListSQL);
    		
			if(data3.length>0){
				jQuery.each(data3,function(i3,item3){
					jQuery("select#selSPolNo").append("<option value='" +item3.POLNO+ "'>" +item3.POLNO+ "</option>");  
				});
				
				jQuery("select#selSPolNo").trigger("onchange");
			}
		}
	}
}

function polNoChange(selectID){
	var polNo = jQuery(selectID).val();
	
	if(polNo!="")
	{
		var polListSQL = "select POLNO, POLSTATUS = [STATUS] , POLSTATEFFDT = convert(varchar, STATEFFDT, 23) from TPOLICY WHERE POLNO = '"+polNo+"' AND RCDSTS <> 'D'";
		
		var data2 = selectFromSQLJson('compnwJNDI',polListSQL);
		
		if(data2.length>0){
			jQuery("span#spanSPolStatus").text(data2[0].POLSTATUS);
			jQuery("span#spanSPolDate").text(data2[0].POLSTATEFFDT);
		}
	}
}

function bindQtipClntPol(){
    jQuery("#field_02_17_ProcessLoading").css("cursor","pointer");
    
    jQuery("#field_02_17_ProcessLoading").qtip({
			content: {
			    text: '<label>投保人名称: </label><span id="spanSClntName"></span>'
			    +'<br/><label>投保人别名: </label><span id="spanSClntNameGen"></span>'
			    +'<br/><label>投保人状态: </label><span id="spanSClntStatus"></span>'
			    +'<br/><label>投保人更新日期: </label><span id="spanSClntDate"></span>'
			    +'<br/><label>保险合同编号: </label><select id="selSPolNo" onchange="polNoChange(this)"></select>'
			    +'<br/><label>保险合同状态: </label><span id="spanSPolStatus"></span>'
			    +'<br/><label>保险合同更新日期: </label><span id="spanSPolDate"></span>',
			    
			    title: { text: '<label>投保人编号: </label><select id="selSClntCode" onchange="clntCodeChange(this)"></select>&nbsp;&nbsp;'
			    	+ '<input name="replaceBtn" type="button" value="替换" onclick="ReplaceValue(1);"/>&nbsp;&nbsp;'
			    	+ '<input name="earBtn" type="button" value="重置" onclick="ReplaceValue(0);"/>'}
		    },
		    position: {
		    	my: 'top center',
		    	at: 'bottom center',
		    	container: jQuery("#thedetailtableDIV")
		    },
		    show: {
		    	event: 'click',
		    	effect: function() {
	    			jQuery(this).fadeTo(500, 1);
//	    			if(jQuery("tr#trClientList").length>0)
//	    				jQuery("tr#trClientList").fadeTo(500, 1);
//	    			else
//	    				jQuery("#field_02_17_ProcessLoading").closest('tr').append("<tr id='trClientList'><td colspan='6'><table><tr><td col>test</td></tr></table></td></tr>");
		        }
		    },
		    hide: {
		    	event: 'click',
		        effect: function() {
		    		jQuery(this).slideUp();
//		    		if(jQuery("tr#trClientList").length>0)
//		    			jQuery("tr#trClientList").slideUp();
		        }
		    },
		    style:{ 
		    	classes: 'qtip-tipped',
		    	width: '300px'
		    }
	});
}

//flag=1: replace flag=0: reset
function ReplaceValue(flag){
	
	if(flag=="1"){
		var clntCode = jQuery("select#selSClntCode").val();
			
		var data = selectFromSPJson("compnwJNDI", "dbo.UspGNOVAGetClntInfo",clntCode+', ');
		
		if(data.length>0){
			setClntFieldInfo(data[0]);
			addLabelRow();
			bindClntSubInfo(clntCode);
		}
		
		jQuery('textarea[name="field_02_26"]').trigger('blur');
		jQuery('textarea[name="field_02_9"]').trigger('blur');
		
		changeOriPolicyCode();
		
	}else{
		//jQuery('textarea[name="field_02_26"]').trigger('blur');
		//jQuery('textarea[name="field_02_9"]').trigger('blur');
		setClntFieldInfo("");
		jQuery('textarea[name="field_02_16"]').text("");
		jQuery('textarea[name="field_02_17"]').text("");
		jQuery('textarea[name="field_02_16"]').trigger('blur');
		jQuery('textarea[name="field_02_17"]').trigger('blur');
		
		//jQuery('textarea[name="field_02_16"]').siblings('span.t-loading').remove()
		bindClntSubInfo("");		//20150505 
	}
	
}

function GetProducerInfo(OperItem) {
	
	//alert ("1 " + ProducerReplaceCheck);
	if(!ProducerReplaceCheck())
	{
		ClearBranchInfo();
	}
	//alert ("2 " + ProducerReplaceCheck);
	
	ResetProducer(OperItem);
	var strPrduCode = "";
	var nLength = 0;
	var PreviousProducerCode = "";

	strPrduCode = OperItem.value;
	// alert (strPrduCode);
	nLength = strPrduCode.length;

	if (nLength == 0)
	{	
	  return;
	}
	else
	{
		OperItem.value = ImproveLength(OperItem.value,9, "0");
		
		var CompanyName = "";
		if (document.getElementsByName("field_02_27")[0] != null)
		{
		  //CompanyName = document.getElementsByName("field_02_27")[0].text;
		  var SelectItem = document.getElementsByName("field_02_27")[0];
		  CompanyName =  SelectItem.options[SelectItem.selectedIndex].text;
		}
		else
		  return false; 	
		
		//alert (CompanyName);
		//debug1
		//1 上海，2 广东，3 佛山，4 深圳，5 北京，6 江苏，7 东莞，8 江门
		var InitialW = "0";
		switch(CompanyName) {
	         case 'REGION HO': 
	         {
	        	InitialW = "0";
	            break;
	         }
	         case 'AIASH': 
	         {
				InitialW = "1";		
	            break;
	         }
	         case 'AIAGD': 
	         {
  	        	InitialW = "2";
	            break;
	         }
	         case 'AIAFS': 
	         {
	  	        InitialW = "3";
	            break;
	         }
	         case 'AIASZ': 
	         {
        	    InitialW = "4";
	            break;
	         }
	         case 'AIABJ': 
	         {
        	    InitialW = "5";
	            break;
	         }
	         case 'AIAJS': 
	         {
        	    InitialW = "6";
	            break;
	         }
	         case 'AIADG': 
	         {
			  	InitialW = "7";				        	 
	            break;
	         }
	         case 'AIAJM': 
	         {
		  	    InitialW = "8";		        	 
	            break;
	         }
	         default: //- other         
	         {
				InitialW = "0";			        	 
	            break;
	         }
	    }

		//alert (OperItem.value + "  " + InitialW);
		OperItem.value = ImproveLength(OperItem.value,10,InitialW);
		strPrduCode = OperItem.value;
	}
		
	var strExist = "";
	var strSql = "SELECT AGTOFFCODE,PRDUCODE,PRDUNAME,AGYCODE,TELNO FROM tproducer WHERE prducode = '"
			+ strPrduCode + "' and RcdSts = 'A' and status = 'A' AND CERTSTS<>'0'";

	var data = selectFromSQLJson('compnwJNDI', strSql);
	var CurrentRow = OperItem.parentNode.parentNode.rowIndex;
	
	// alert (strSql);
	if (data.length>0) 
	{
		PreviousProducerCode = document.getElementsByName("field_7_2")[CurrentRow - 1].value;
		//alert (PreviousProducerCode);
		
		//document.getElementsByName("field_7_2")[CurrentRow - 1].value = jQuery.trim(data[0].PRDUCODE);
		document.getElementsByName("field_7_1")[CurrentRow - 1].value = jQuery.trim(data[0].PRDUNAME);
		document.getElementsByName("field_7_3")[CurrentRow - 1].value = jQuery.trim(data[0].AGTOFFCODE);
		document.getElementsByName("field_7_6")[CurrentRow - 1].value = jQuery.trim(data[0].AGYCODE);
		document.getElementsByName("field_7_4")[CurrentRow - 1].value = jQuery.trim(data[0].TELNO);
		
		//****Check the same name and tel --Justin Bin 20150210 Added
		if (!SameNameTelChecking(CurrentRow)){
			alert(checkSameTelandName);
			document.getElementsByName("field_7_2")[CurrentRow - 1].focus();
			return false;
		}
		
		document.getElementsByName("field_7_5")[CurrentRow - 1].focus();
		
		var OfficeCode = "";
		var BranchCodeExist = "";
		BranchCodeExist = document.getElementsByName("BranCod_id")[0].value.trim();
		
		if (CheckMinProducer(strPrduCode) || BranchCodeExist == "" )
		{
			OfficeCode = jQuery.trim(data[0].AGTOFFCODE);
			BranchInfRetrieve(OfficeCode,strPrduCode);
		}
		else
			return false;
		
		return true;
	} 
	else 
	{
		alert("The producer doesn't exist in system.");
		document.getElementsByName("field_7_2")[CurrentRow - 1].focus();
		return;
	}	
}

function ResetProducer(OperItem) {
	var strPrduCode = "";
	var nLength = 0;
	var CurrentRow = OperItem.parentNode.parentNode.rowIndex;
	
	document.getElementsByName("field_7_1")[CurrentRow - 1].value = "";
	document.getElementsByName("field_7_3")[CurrentRow - 1].value = "";
	document.getElementsByName("field_7_4")[CurrentRow - 1].value = "";
}

//check if contact name and tel is same as the producer  -- 20150209 Justin Added
function SameNameTelChecking(CurrentRow){
	var producerTel="";
	var producerName="";
	var contactTel="";
	var contactName="";
	//var CurrentRow = OperItem.parentNode.parentNode.rowIndex;
	
	producerTel=document.getElementsByName("field_7_4")[CurrentRow-1].value;
	producerName=document.getElementsByName("field_7_1")[CurrentRow-1].value;
	contactTel=document.getElementsByName("field_02_20")[0].value;
	contactName=document.getElementsByName("field_02_19")[0].value;
	if(producerTel==contactTel && producerName==contactName && document.activeElement.name!="field_02_19" && document.activeElement.name!="field_02_20"){
		//alert(checkSameTelandName);
		//document.getElementsByName("field_7_2")[CurrentRow - 1].focus();
		return false
	}else{
		//document.getElementsByName("field_7_5")[CurrentRow - 1].focus();
		return true;
	}
	
}

//check if the contact name and tel is same as the producer ,applied in field_02_19 and field_02_20 --20150209 Justin Bin Added
function CheckSameNameandTel(OperItem){
	var producerTel="";
	var producerName="";
	var contactTel=document.getElementsByName("field_02_20")[0].value;
	var contactName=document.getElementsByName("field_02_19")[0].value;
	for(var i=0;i<document.getElementsByName("field_7_4").length;i++){
		producerTel=document.getElementsByName("field_7_4")[i].value;
		producerName=document.getElementsByName("field_7_1")[i].value;
		if(producerTel==contactTel && producerName==contactName && document.activeElement.name!="field_02_19" && document.activeElement.name!="field_02_20" && document.activeElement.name!="field_7_2"){
			alert(checkSameTelandName);
			OperItem.focus();
			return;
		}
	}
}

function CheckMemberNovaDuplicate(){
	var requestUrl = jQuery('input[type="hidden"][name="requestUrl"]').val();
	var reqNo = jQuery('input[name="requestNo"]').val();
	var formId = jQuery('input[name="formSystemId"]').val();
	
	if(reqNo==""||reqNo==undefined||reqNo==null){
		reqNo = jQuery('input[name="request_no"]').parent().text();
	}
	
	var warningData = selectFromSPJson("","NovaUspCheckingMemDup",formId+','+ reqNo);
	if(warningData!=null)
	{
		if(warningData.length>0)
		{
			if(warningData[0].MSGNO>0)
			{
				jQuery('div.wizard-steps-parent2').each(function (){
					jQuery(this).parent().parent().remove();
					});
				var warnMsg = GetWarningMsg(requestUrl,reqNo,formId)
				jQuery('table:eq(0)').find('tr:eq(0)').after(warnMsg);
				
				jQuery('html,body').animate({scrollTop:0},'fast');
				return true;
			}
			else
				return false;
		}
	}
	
	return true;
}


