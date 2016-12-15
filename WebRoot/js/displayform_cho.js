//????????
function showFlow(preUrl,formSystemId,requestNo){
    var url = "/wkfDesignAction.it?method=showFormFlow&formSystemId="+formSystemId+"&requestNo="+requestNo;
    if(preUrl!=""){
      url = preUrl + url;
    }
    openCenterWindow(url,810,620);
}


  function getFormStr(fc) {
		var i,qs="",and="",ev="";
		for(i=0;i<fc.length;i++) {
			e=fc[i];
			if (e.disabled == true){
				continue;
			}
			if (e.name!='') {
				if (e.type=='select-one'&&e.selectedIndex>-1) ev=e.options[e.selectedIndex].value;
				else if (e.type=='checkbox' || e.type=='radio') {
					if (e.checked==false) continue;
					ev=e.value;
				}
				else 
					ev=e.value;
				//ev=encode(ev);
				ev = formatStringAllChar(ev);
				qs+=and+e.name+'='+ev;
				and="&";
			}
		}
		return qs;
	}  
  
 function afterAddRow(tableId, rowIndex, sectionId, formSystemId, nodeId){
	 //do nothing, to be override.
	 if(tableId=="formTable9"){	// Justin Added 20150306 count the member
		 if (typeof(countPeople) == "function") {
			 countPeople();
		 }		 
	 }
 }

/**
  *??????????????section table????????????
  **/
  function createFormSectionRow(preUrl,tableId,sectionId,formSystemId,nodeId){
	  
     var xmlhttp = createXMLHttpRequest();
     var url = "";
     var result = "";
     var oTable = document.getElementById(tableId);
     var index = oTable.rows.length - 1;
     url = "/formSectionAction.it?method=getTableSectionField&formSystemId="+formSystemId+"&sectionId="
           +sectionId+"&nodeId="+nodeId+"&index="+index;
     if(preUrl!=""){
       url = preUrl + url;
     }
     xmlhttp.open("POST", url, false);
     xmlhttp.onreadystatechange=function()
     {
       if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
          result = xmlhttp.responseText;
          if(result=="fail"){
            alert(add_row_fail);
            return;
          }else{ 
            var rowIndex = addRowForSectionTable(tableId,result);
            afterAddRow(tableId, rowIndex, sectionId, formSystemId, nodeId);
          }
       }
     }
     xmlhttp.setRequestHeader("If-Modified-Since","0");
     xmlhttp.send(null);	
     
     jQuery('input[isDate="true"]').blur(function(){_NOVA.until.changeDateFormat(this);jQuery(this).trigger('onchange')});
     
     jQuery(function() {  
 		jQuery("textarea[maxlength]").bind('input propertychange', function() {  
 	        var maxLength = jQuery(this).attr('maxlength');  
 	        if (jQuery(this).val().length > maxLength) {  
 	        	jQuery(this).val(jQuery(this).val().substring(0, maxLength));  
 	        }  
 	    })  
 	});
  }
  
  function addRowForSectionTable(tableId,result){
     var oTable = document.getElementById(tableId);
     var rowIndex = oTable.rows.length;
     var oTR = oTable.insertRow(rowIndex);  
     var tdLabel = result.split("<td>");
     
     for(i=0;i<oTable.rows[0].cells.length;i++){
         var oTD = oTR.insertCell(i);
         oTD.innerHTML = tdLabel[i];
         if(tdLabel[i].toLowerCase().indexOf("textarea")>0)
        	 oTD.style.wordBreak = "break-all";			
        	 //oTD.style.cssText=oTable.rows[0].cells[i].style.cssText;		//20150508 inherent the parents' style, Justin Bin Added,20150513 commented
         oTD.align = "left";
     }     

     jQuery("#" + tableId + " tr:last select.combox" ).combobox();
     
     return rowIndex;
  }
  
  /**
   *??????????????section table????????????
   **/
   function createFormSectionRow2(preUrl,tableId,sectionId,formSystemId,nodeId){
 	  
      var xmlhttp = createXMLHttpRequest();
      var url = "";
      var result = "";
      var oTable = document.getElementById(tableId);
      var index = oTable.rows.length - 1;
      url = "/formSectionAction.it?method=getTableSectionField&formSystemId="+formSystemId+"&sectionId="
            +sectionId+"&nodeId="+nodeId+"&index="+index;
      if(preUrl!=""){
        url = preUrl + url;
      }
      xmlhttp.open("POST", url, false);
      xmlhttp.onreadystatechange=function()
      {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
           result = xmlhttp.responseText;
           if(result=="fail"){
             alert(add_row_fail);
             return;
           }else{ 
             var rowIndex = addRowForSectionTable2(tableId,result);
             afterAddRow(tableId, rowIndex, sectionId, formSystemId, nodeId);
           }
        }
      }
      xmlhttp.setRequestHeader("If-Modified-Since","0");
      xmlhttp.send(null);	
      
      jQuery('input[isDate="true"]').blur(function(){_NOVA.until.changeDateFormat(this);jQuery(this).trigger('onchange')});
      
      jQuery(function() {  
  		jQuery("textarea[maxlength]").bind('input propertychange', function() {  
  	        var maxLength = jQuery(this).attr('maxlength');  
  	        if (jQuery(this).val().length > maxLength) {  
  	        	jQuery(this).val(jQuery(this).val().substring(0, maxLength));  
  	        }  
  	    })  
  	});
   }
  //20150514 Justin Bin Added
  function addRowForSectionTable2(tableId,result){
	     var oTable = document.getElementById(tableId);
	     var rowIndex = oTable.rows.length;
	     var oTR = oTable.insertRow(rowIndex);  
	     var tdLabel = result.split("<td>");
	     
	     for(i=0;i<oTable.rows[0].cells.length;i++){
	         var oTD = oTR.insertCell(i);
	         oTD.innerHTML = tdLabel[i];
	         if(tdLabel[i].toLowerCase().indexOf("textarea")>0)
	        	 oTD.style.wordBreak = "break-all";			
	         oTD.style.cssText=oTable.rows[0].cells[i].style.cssText;		//20150508 inherent the parents' style, Justin Bin Added,20150513 commented
	         oTD.align = "left";
	     }   
	     
	     jQuery("#" + tableId + " tr:last select.combox" ).combobox();	//20150526 Justin Bin Added
	     return rowIndex;
	  }
  
  
  function	openRefFormWindow(preUrl,targetUrl,formSystemId){
    returnTarget = showModalDialog(targetUrl,window,"dialogWidth:500px; dialogHeight:400px;help:0;status:0;resizeable:1;");
    //alert(returnTarget);
    if(returnTarget != undefined && returnTarget.length > 1) { 
    	//alter(1);   	
    	var arr;
    	var linkStr="";
		arr = returnTarget.split(",");
    	for(var i=0;i<arr.length;i++){
    		linkStr += "<a href=\"javascript:openWindow('"+preUrl+"/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&requestNo="+arr[i]+"&formSystemId="+formSystemId+"')\">"+arr[i]+"</a>,"
    	}
    	linkStr = linkStr.substring(0,linkStr.length-1);
    	linkStr+= "<input type=\"hidden\" name=\"reference_form\" value=\""+arr+" \">";
    	//alert(linkStr)
    	var obj = document.getElementById('divrefeformId');
		obj.innerHTML=linkStr;
	}else{
		//alert('1');
		var linkStr="";
		linkStr+= "<input type=\"hidden\" name=\"reference_form\" value='' >";
		var obj = document.getElementById('divrefeformId');
		obj.innerHTML=linkStr;
	}  	
  }
  
  function openRefFormWindow2(preUrl, targetUrl, fieldId, divId, tagId) {
      returnValue = showModalDialog(targetUrl, window, "dialogWidth:500px;dialogHeight:430px;help:0;status:0;resizeable:1;");
      while (true) {
          // alert("returnValue: " + returnValue);
          if (returnValue == undefined) {
              // Cancel
              return;
          }
          if (returnValue.indexOf("?method") == -1) {
              // Confirm
              break;
          }
          // Search
          returnValue = showModalDialog(returnValue, window, "dialogWidth:500px;dialogHeight:430px;help:0;status:0;resizeable:1;");
      }
      
      var linkStr = "";
      if (returnValue.length > 1) {
          var arr;
          arr = returnValue.split(",");
          var forduplicate = "";
          for (var i = 0; i < arr.length; i++) {
              if (forduplicate.indexOf(arr[i])  > 0)   {
                   continue;
              }
              forduplicate = forduplicate + " " + arr[i];
              
              var dot = arr[i].lastIndexOf(".");
              var requestNo = arr[i].substring(0, dot);
              var formSysId = arr[i].substring(dot + 1);
              linkStr += "<a href=\"javascript:openCenterWindow('" + preUrl
                  + "/formManageAction.it?method=displayFormContent&operateType=view&viewFlag=false&pop=true&requestNo="
                  + requestNo + "&formSystemId=" + formSysId + "',800,600)\">" + requestNo + "</a>, ";
          }
          if (arr.length > 0) {
              linkStr = linkStr.substring(0, linkStr.length - 2);
          }
      }
      // alert(linkStr);
      var obj = document.getElementById(divId);
      obj.innerHTML = linkStr;
      
      document.getElementById(tagId).value = returnValue;
      //document.getElementById(tagId).fireEvent("onchange");
      jQuery("#" + tagId).change();
  }
  
  function openRefContractWindow(preUrl, targetUrl, fieldId, divId, tagId) {
      returnValue = showModalDialog(targetUrl, window, "dialogWidth:500px;dialogHeight:430px;help:0;status:0;resizeable:1;");
      while (true) {
          // alert("returnValue: " + returnValue);
          if (returnValue == undefined) {
              // Cancel
              return;
          }
          if (returnValue.indexOf("?method") == -1) {
              // Confirm
              break;
          }
          // Search
          returnValue = showModalDialog(returnValue, window, "dialogWidth:500px;dialogHeight:430px;help:0;status:0;resizeable:1;");
      }
      
      var linkStr = "";
      if (returnValue.length > 1) {
          var arr;
          arr = returnValue.split(",");
          for (var i = 0; i < arr.length; i++) {
              linkStr += "<a href=\"javascript:openCenterWindow('" + preUrl
                  + "/contractAction.it?method=editCntr&editType=edit&pop=true&contractNo="
                  + arr[i] + "',800,600)\">" + arr[i] + "</a>, ";
          }
          if (arr.length > 0) {
              linkStr = linkStr.substring(0, linkStr.length - 2);
          }
      }
      // alert(linkStr);
      var obj = document.getElementById(divId);
      obj.innerHTML = linkStr;
      
      document.getElementById(tagId).value = returnValue;
      document.getElementById(tagId).fireEvent("onchange");
  }
  
 function validateForm(frm){
    for(i=0;i<frm.length;i++)
    {
      if(frm[i].disabled!="undefine" && frm[i].disabled==true){
        continue;
      }
      
      if ((frm[i].tagName.toUpperCase() == "INPUT" || frm[i].tagName.toUpperCase() == "TEXTAREA") && frm[i].value != "undefine") {
          frm[i].value = frm[i].value.Trim();
      }
      
      if(frm[i].type.toUpperCase()=="TEXTAREA" || frm[i].tagName.toUpperCase()=="SELECT"){
        if((frm[i].required=="true") && frm[i].value==""){
           str_warn1=please_input + frm[i].title;
           alert(str_warn1);
           frm[i].focus();
           return false;    
         }
      }
      if(frm[i].type.toUpperCase()=="TEXT" && frm[i].tagName.toUpperCase()=="TEXTAREA"){
        if((frm[i].required=="true") && frm[i].value==""){
           str_warn1=please_input + frm[i].title;
           alert(str_warn1);
           frm[i].focus();
           return false;    
         }
      }
      //checkbox
      if(frm[i].type.toUpperCase()=="CHECKBOX" && frm[i].required=="true")
      {
          if(GetValueChoose(frm[i])==""){
            alert(frm[i].title+must_select);
            if(document.all['addBtn']!=null){//20150228 check the object Justin Bin Added
            	document.all['addBtn'].disabled = "";	
            }
            if(document.all['initBtn']!=null){//20150228 check the object Justin Bin Added
            	document.all['initBtn'].disabled = "";	
            }
            return false;
          }
      }
      if(frm[i].tagName.toUpperCase()=="INPUT" && frm[i].type.toUpperCase()=="TEXT"){
         if((frm[i].required=="true") && frm[i].value==""){
           str_warn1=please_input + frm[i].title;
           alert(str_warn1);
           frm[i].focus();
           return false;    
         }
        if (frm[i].isNumber == 'true') {
            //alert(frm[i].value.replace(/,/g, ""));
            if (isNaN(frm[i].value.replace(/,/g, ""))) {
                str_warn1=frm[i].title + be_number;
                alert(str_warn1);
                frm[i].focus();
                return false;
            }
        }
         if(frm[i].isDate=='true' && frm[i].value!=""){
            if(isValidDate(frm[i].value)==false){
                str_warn1=frm[i].title+be_date;
                alert(str_warn1);
                frm[i].focus();
                return false;              
            }
         }
         if(frm[i].tagName.toUpperCase()=="TEXTAREA" && frm[i].maxLength!=""){
              var count = getStrLength(frm[i].value);
              if(count>frm[i].maxLength){
                 str_warn1=field_length_long+" "+frm[i].title;
                 alert(str_warn1);
                 frm[i].focus();
                 return false;   
              }
         }
         if(frm[i].tagName.toUpperCase()=="INPUT" && frm[i].type.toUpperCase()=="TEXT"){
            var count = getStrLength(frm[i].value);
            if(frm[i].isNumber=="true")
            	count = getStrLength(frm[i].value.replace(/,/g,""));
            if(frm[i].maxLength!=""){
               if(count>frm[i].maxLength){
                 str_warn1=field_length_long+" "+frm[i].title;
                 alert(str_warn1);
                 frm[i].focus();
                 return false;   
               }
            }
         }
      }  
    }
    return true;
  }
  
  function saveForm(url,submitType,actionType,actionMessage,validateUrl){
	//////////////////////////////////////////////
    var temp = url.split("/");
     var prefixUrl = "/";
    if(temp[0]!=""){
       prefixUrl = prefixUrl + temp[0];
    }else{
       prefixUrl = prefixUrl + temp[1];
    }

    var str = "";
    var frm = document.forms[0];
    if (submitType != "00") {
    	if (typeof(beforeFormValid) == "function") {
            if (beforeFormValid() == false) {
                return;
            }
        }
    	if (validateForm(frm) == false) {
      		return;
    	}
    	if (typeof(validationSubmit) == "function") {
            if (validationSubmit() == false) {
                return;
            }
        }
    } else {
        if (typeof(validationSave) == "function") {
            if (validationSave() == false) {
                return;
            }
        }
    }
    //check the length of every fields
    for(var i=0;i<frm.length;i++)
    {
      if((frm[i].tagName.toUpperCase()=="INPUT" &&frm[i].type.toUpperCase()=="TEXT") || frm[i].tagName.toUpperCase()=="TEXTAREA"){
       //var value = frm[i].value.Trim();
       var value = "";
       if(frm[i].isNumber=="true")
    	   value = frm[i].value.Trim().replace(/,/g,"");
       else
    	   value = frm[i].value.Trim();
       var length = value.len();
       if(length>frm[i].maxLength){
          alert(field_length_long+" "+frm[i].title)
          frm[i].focus();
          return;
        }
      }
    }
    var checkResult = "";
    var temp = "";
    if(validateUrl!=""){
       //validateUrl = prefixUrl +"/"+ validateUrl + "&" + getFormStr(frm)+"&tempData="+new Date();
       validateUrl = prefixUrl +"/"+ validateUrl +"&tempData="+new Date();
       var args = getFormStr(frm);
       var myAjax = new Ajax.Request(
         validateUrl,
        {
            method:"post",       //
            parameters:args,   //
            asynchronous:false,
            setRequestHeader:{"If-Modified-Since":"0"},     //
            onComplete:function(x){    //
               var result = x.responseText;
               result = result.Trim();
               if(result!="success"){
                   temp = result.substring(0,1);
                   result = result.substring(1);
                   checkResult = result;
               }
            },
            onError:function(x){          //
                alert('Fail to validate form');
            } 
        } 
       );  
        
    }
    
     if(checkResult!=""){
         if(temp=="2"){
               if(confirm(checkResult+confirm_continue_1)==false){ return;   }
          }else{ alert(checkResult);  return; }
    }
    //return;
    if(submitType=="00"){
      str = confirm_save_form;
    }else{
      str = confirm_submit_form;
    }
    if(submitType=="01"){
      var showFlag = document.all['showCCFlag'].value;
      if(showFlag=="1" && confirm(confirm_cc)){
        var arr = showModalDialog(prefixUrl+'/userManageAction.it?method=enterSelectStaff&pageTitle=SelectCCToStaff',window,'dialogWidth:500px; dialogHeight:450px;help:0;status:0;resizeable:1;')
        if(arr!=undefined){
          document.all['ccStaffCode'].value = arr;
        }
      }
    }
    
    if((submitType=="00" && actionType=="01") || (submitType=="01" && actionType=="02")){
      if(actionMessage!=""){
        //boxs(1,actionMessage);
        //Alert(actionMessage,null,null,'Prompt Message',function(){ConfirmInfo(str,null,null,null,function(){submitAppliedForm(url);},null)},null)
        Alert(actionMessage,null,null,'Prompt Message',function(){submitAppliedForm(str,url,submitType)},null);
      }
      else
      {
    	  submitAppliedForm(str,url,submitType);
      }
    }
    else
    {
    	submitAppliedForm(str,url,submitType);
    }
  }
  
  function submitAppliedForm(str,url,submitType){    
	var temp = url.split("/");
	var prefixUrl = "/";
	var frm = document.forms[0];
	
	if(temp[0]!=""){
	   prefixUrl = prefixUrl + temp[0];
	}else{
	   prefixUrl = prefixUrl + temp[1];
	}
    if(confirm(str)){

       if(checkSubmitFlg==true){
         return;
       }
       
	   	var outTimer1
	
	   	outTimer1 = setTimeout(function(){ShopConfirm(operating_tip);
	   	clearTimeout(outTimer1);}, 50);

      if(str == confirm_submit_form){
    	  
    	  updateUrl = prefixUrl+"/formManageAction.it?method=saveFormFill&checkOptionalNode=1";
    	  var newRequestNo = frm.request_no.value;
          var args = getFormStr(frm);
          args = encodeURI(encodeURI(args)); //Very Important!
          var myAjax = new Ajax.Request(
        	updateUrl,
           {
               method:"post",       //
               parameters:args,   //
               asynchronous:false,
               setRequestHeader:{"If-Modified-Since":"0"},     //
               onComplete:function(x){
            	   newRequestNo = x.responseText;
               },
               onError:function(x){          //
            	   closeWindow2();
                   alert('Fail to submit form');
                   checkSubmitFlg = false;
                   return;
               } 
           } 
          );  
          url += "&saveType=update";    //Very Important!
	      var inquiryUrl = prefixUrl+"/formManageAction.it?method=isNextProcessorOptional";
	      var result="";
	      if(frm.pending_request_no){
		      if(newRequestNo.substring(0,10)==frm.pending_request_no.value.substring(0,10)){
		    	  frm.request_no.value = newRequestNo;
		      }else{	      	
		    	  closeWindow2();
		    	  alert(network_busy);
		    	  checkSubmitFlg = false;
		    	  return;
		      }
	      }
          var args = getFormStr(frm);//����request_no
          args = encodeURI(encodeURI(args)); //Very Important!
          
	      myAjax = new Ajax.Request(
	    		inquiryUrl,
	          {
	              method:"post",       //
	              parameters:args,   //
	              asynchronous:false,  //ͬ��
	              setRequestHeader:{"If-Modified-Since":"0"},     //
	              onComplete:function(x){    //
	                 var result = x.responseText;
	                 result = result.Trim();
	                 if(result=="1"||result=="2"){
	                     var arr = "";
	                     
	                     while(arr==""||arr==undefined) {	                    	
	                    	arr = showModalDialog(prefixUrl+'/userManageAction.it?method=enterSelectStaff&pageTitle=Select Next Approver&showTips=form.select_submitto_'+result,
		                    		 window,'dialogWidth:500px; dialogHeight:550px;help:0;status:0;resizeable:1;');
	                    	if(result=="2" && (arr==""||arr==undefined)){
	                    		alert("Please select next approver");
	                    	} else break;	                    	
	                     }		 
	                     if(arr!=""&&arr!=undefined){
		                   	url = url + "&nextApproverStaffCode=" + arr;
		                 } 
	                 }
	              },
	              onError:function(x){          //
	            	      closeWindow2();
	                      alert(x.responseText);
	                      checkSubmitFlg = false;
	                      return;
	              } 
	          } 
	         ); 
      }
      checkSubmitFlg = true;
      
      if(submitType!="00")
      {
	      if (typeof(validationBeforeSubmit) == "function") {
	          if (validationBeforeSubmit() == false) {
	        	  closeWindow2();
	        	  clearTimeout(outTimer1);
	        	  checkSubmitFlg = false;
	              return;
	          }
	      }
      }
      
      document.forms[0].action= url;
      document.forms[0].submit();
      
      closeWindow2();
    }
  }
  
    function showCheckInout(url){
     var staffCode = document.all['request_staff_code'].value;
     url = url + "&staffCode="+staffCode;
     openCenterWindow(url,500,500)
  }

    function convertMoney(number) {
        if (number == null) {
            return "";
        }
        return formatNumber(number, "#,###.00");
    }
    
    function convertMoneyObj(obj) {
        if (obj == null) {
            return;
        }
        obj.value = formatNumber(obj.value, "#,###.00");
    }
    
    function maxLengthControl(){
    	jQuery(function() {  
    		jQuery("textarea[maxlength]").bind('input propertychange', function() {  
    	        var maxLength = jQuery(this).attr('maxlength');  
    	        if (jQuery(this).val().length > maxLength) {  
    	        	jQuery(this).val(jQuery(this).val().substring(0, maxLength));  
    	        }  
    	    })  
    	});
    }
    
    function listenInputFocus(){ 
    	jQuery('textarea').each(function(){
        	jQuery(this).focus(function(){ jQuery(this).css("background-color","#F8FEC3");});
        	jQuery(this).blur(function(){ jQuery(this).css("background-color","#FFFFFF");});
        });
        
        jQuery('input[type="text"]').each(function(){
        	jQuery(this).focus(function(){jQuery(this).css("background-color","#F8FEC3");});
        	jQuery(this).blur(function(){jQuery(this).css("background-color","#FFFFFF");});
        });
        
        jQuery('input[type="checkbox"]').each(function(){
        	jQuery(this).focus(function(){jQuery(this).parent().css("background-color","#F8FEC3");});
        	jQuery(this).blur(function(){jQuery(this).parent().css("background-color","rgb(239, 239, 239)");});
        });
    } 
    