
jQuery(document).ready(function(){
	  var formID = jQuery('input[name="formSystemId"]').val();
	  var requestUrl = jQuery('input[type="hidden"][na="requestUrl"]').val();

	  var resultData = selectFromSPJson("","poef_wkf_getFlowName",formID);

	  var wizardStr = '<tr><td><div class="wizard-steps-parent"><div class="wizard-steps">';
      if(resultData.length>0){
	     jQuery.each(resultData, function(i,item){ 
	        var ndName = item.NDNAME;
	        wizardStr += '<div class="active-step"><a href="#">'+item.NDNAME+'</a></div>'
	     });
      }
   	  wizardStr += '</div></div></td></tr>';
      jQuery('table:eq(0)').find('tr:eq(0)').after(wizardStr);
      
      setAutoSave()
      setCatalog();
});

function setAutoSave(){
	var time = GetAutoSaveTime();
	if(time>0)
		setInterval(autoSave,time*1000);
}

function GetAutoSaveTime(){
	var sql = "select param_value as SaveTime from teflow_param_config where param_code = 'AutoSaveTime'";
	
	var data = selectFromSQLJson('',sql);
	
	if(data.length>0)
		return parseInt(data[0].SAVETIME);
	else
		return 0;
}

function setCatalog(){
	var sequenceNo, catalogText, catalogStr;
    
	  jQuery("span.spSectionName").each(function(){
		  sequenceNo = jQuery(this).parent().contents().eq(0).text();
		  catalogText = jQuery(this).parent().contents().eq(1).text().slice(0,-1);
		  
		  jQuery(this).parent().append(jQuery('<a name="anchor'+sequenceNo+'"></a>'));
		  
		  catalogStr = "";
		  catalogStr += '<dd class="sideCatalog-item1" >';
		  catalogStr +='<span class="sideCatalog-index1">'+sequenceNo+'</span>';
		  catalogStr += '<a title="'+catalogText+'" onclick="anchor('+sequenceNo+')" href="#3">'+catalogText+'</a>';
		  catalogStr +='</dd>';
		  jQuery(catalogStr).appendTo(jQuery("#sideCatalog-catalog"));
	  });
	  
	  jQuery(".sideCatalogBtn").click(function(){
		 jQuery(this).toggleClass("activeBar"); 
		 
		 if(jQuery(this).hasClass("activeBar"))
			 jQuery("div.sideCatalogBg").hide();
		 else
			 jQuery("div.sideCatalogBg").show();
	  });
	  
	  jQuery("#likDown.sideToolbar-down").click(function(e){
		  e.preventDefault();           
		  jQuery('body').animate({scrollTop: jQuery(document).height()},200);
	  });
	  
	  /*jQuery("#likTop.sideToolbar-up").click(function(){
		  jQuery('body').animate({scrollTop: 0},"slow");
	  });*/
}

function anchor(index){
	location.hash = "anchor"+ index;
	
	jQuery('td.tdSectionTitle').css("background-color","rgb(211, 211, 212)");
	
	jQuery('a[name="anchor'+index+'"]').parent().parent().css("background-color","#F8FEC3");
}

jQuery(window).scroll(function() {
	var scrolltop = document.body.scrollTop;

	var bottom = 50 - scrolltop;
	jQuery('#divTop').css('bottom',bottom);
	jQuery('#divTop').fadeIn();
	
	
});

function autoSave(){
	if(jQuery('textarea[name="field_02_6"]').length > 0 && jQuery('textarea[name="field_02_6"]').val()!="")
	{
		var requestUrl = jQuery('input[type="hidden"][name="requestUrl"]').val();
		var url = requestUrl+ "/formManageAction.it?method=saveFormFillAuto&submitType=00"
		var frm = document.forms[0];
		var args = getFormStr(frm);
		args = encodeURI(encodeURI(args));
		
		jQuery.ajax({
	    	  type: "POST", 
	          url: url, 
	          data: args,
	          async: true,
	          success: function (data) { 
					if(data.requestNo)
					{
						jQuery("input[name='saveType'][type='hidden']").val("update");
						jQuery("input[name='request_no'][type='hidden']").val(data.requestNo);
						 var successStr = '<tr id="trSuccessInfo"><td><div class="notificationbar_green"><a href="#">';
						  successStr += '<img src="'+requestUrl+'/images/btn-notification-close.png" alt="close" /></a>';
						  successStr += '<p>自动保存投保单成功</p></div></td></tr>';
							  
						  jQuery('table:eq(0)').find('tr:eq(0)').after(successStr);
						  
						  jQuery("div.notificationbar_green a").click(function () {
						        jQuery("tr#trSuccessInfo").fadeOut();
						        return false;
						  });
						  setTimeout(function () { jQuery('tr#trSuccessInfo').fadeOut(); }, (4*1000));
					}
			  }, 
	          error: function (XMLHttpRequest, textStatus, errorThrown) { 
	          		alert("Error when save the form"); 
	          } 
	    });
	}
} 



function GetWarningMsg(requestUrl,reqNo,formId){
	 var warnMsg = '<tr><td><div class="wizard-steps-parent2"><div align="center" class="wizard-steps2">';
  	  var warningData = selectFromSPJson("","poef_wkf_getFlowMsg",reqNo);
  	  if(warningData)
	  {
	   	  if(warningData.length>0){
		     jQuery.each(warningData, function(i,item){
		    	 if(item.MSGCODE==null)
		    		 item.MSGCODE = "";
		    	 if(item.MSGTEXT==null)
		    		 item.MSGTEXT = "";
		    	 
				if(item.MSGINDICATOR=='W')
					 warnMsg += '<div class="warningDiv"><img src="'+requestUrl+'/images/Warning.png" class="warningImg"/><span class="warningMsg">'+item.MSGCODE+'-'+item.MSGTEXT+'</span></div>';
				 
			    if(item.MSGINDICATOR=='E')
			    	 warnMsg += '<div class="errorDiv"><img src="'+requestUrl+'/images/Error.png"  class="errorImg"/><span class="errorMsg">'+item.MSGCODE+'-'+item.MSGTEXT+'</span></div>';
	
			    if(item.MSGINDICATOR=='I')
			    	 warnMsg += '<div class="informDiv"><img src="'+requestUrl+'/images/Information.png"  class="informImg"/><span class="informMsg">'+item.MSGCODE+'-'+item.MSGTEXT+'</span></div>';
			     
			    if(item.MSGINDICATOR=='S')
			    	 warnMsg += '<div class="successDiv"><img src="'+requestUrl+'/images/Success.png"  class="successImg"/><span class="successMsg">'+item.MSGCODE+'-'+item.MSGTEXT+'</span></div>';
			 });
	   	  }
	  }
  	  
  	  warnMsg += '</div></div></td></tr>';
  	  
  	  return warnMsg
}

jQuery(window).resize(function(){
	var width1 = jQuery('div.wizard-steps-parent').outerWidth();
	var width2 = jQuery('div.wizard-steps').outerWidth();
	var width = (width1 - width2)/2;
    jQuery('div.wizard-steps').css("left",  width + "px");
});


