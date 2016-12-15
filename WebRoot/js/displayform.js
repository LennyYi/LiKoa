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
				else ev=e.value;
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
            if(tableId=='formTable4'&&index>0){
                var tr1 = jQuery('#'+tableId).find('tr:eq(1)');
                var sel1 = tr1.find('select:first').find('option:selected');
                var row = index+1;
                
                var selObj = jQuery('#'+tableId).find('tr:eq(' + row + ')').find('select:first');
                selObj.trigger("onfocus");
                selObj.val(sel1.val());
            }

          }
       }
     }
     xmlhttp.setRequestHeader("If-Modified-Since","0");
     xmlhttp.send(null);
     
     jQuery('input[isDate="true"]').blur(function(){_NOVA.until.changeDateFormat(this);});
     
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
     var tdLabel = result.split("<td>")
     for(i=0;i<oTable.rows[0].cells.length;i++){
         var oTD = oTR.insertCell(i);
         oTD.innerHTML = "<td align='left' style='word-break : break-all; '>"+tdLabel[i]+"</td>";
     }
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
            document.all['addBtn'].disabled = "";
            document.all['initBtn'].disabled = "";
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
            	alert("!!!");
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
               if(confirm(checkResult+confirm_continue_1)==false){
                                 return;
                           }
          }else{
                         alert(checkResult);
                         return;
          }
    }
    //return;
    if(submitType=="00"){
      str = confirm_save_form;
    }else{
      str = confirm_submit_form
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
        Alert(actionMessage,null,null,'Prompt Message',function(){submitAppliedForm(str,url)},null)
      }else{
        //ConfirmInfo(str,null,null,null,function(){submitAppliedForm(url);},null);
        submitAppliedForm(str,url)
      }
    }else{
      //ConfirmInfo(str,null,null,null,function(){submitAppliedForm(url);},null);
      submitAppliedForm(str,url)
    }
  }
  
  function submitAppliedForm(str,url){
    if(confirm(str)){
       if(checkSubmitFlg==true){
         return;
       }
      checkSubmitFlg = true;
      document.forms[0].action= url;
      document.forms[0].submit();
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
    