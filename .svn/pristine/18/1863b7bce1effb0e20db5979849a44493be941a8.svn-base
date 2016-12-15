var tooltipObj = new DHTMLgoodies_formTooltip();
	tooltipObj.setTooltipPosition('right');
	tooltipObj.setPageBgColor('#FBFBFC');
	tooltipObj.setTooltipCornerSize(15);
	tooltipObj.initFormFieldTooltip();

document.all.updateFieldsVal.value = getFieldVals(document, document.all.updateSections);
document.all.newFieldsVal.value = getFieldVals(document, document.all.newSectionFields);

jQuery(document).ready(function(){
	  var requestUrl = jQuery('input[type="hidden"][name="requestUrl"]').val();
	  var reqNo = jQuery('input[name="requestNo"]').val();
	  var formId = jQuery('input[name="formSystemId"]').val();
	  
	  //wizard bar
	  var wizardStr = GetWorkFlowStatus(requestUrl,reqNo,formId);
   	  
   	  //warning msg
   	  var warnMsg = GetWarningMsg(requestUrl,reqNo,formId);
   	
      jQuery('table:eq(0)').find('tr:eq(0)').after(wizardStr+warnMsg);

      setCatalog();
});

//centralize warning bar
jQuery(window).resize(function(){
	var width1 = jQuery('div.wizard-steps-parent').outerWidth();
	var width2 = jQuery('div.wizard-steps').outerWidth();
	var width = (width1 - width2)/2;
    jQuery('div.wizard-steps').css("left",  width + "px");

    width1 = jQuery('div.wizard-steps-parent2').outerWidth();
	width2 = jQuery('div.wizard-steps2').outerWidth();
	width = (width1 - width2)/2;
    jQuery('div.wizard-steps2').css("left",  width + "px");
});

function GetWorkFlowStatus(requestUrl,reqNo,formId){
	  var wizardData = selectFromSPJson("","poef_wkf_getFlowStatus",reqNo+','+formId); //带参数逗号隔开;
	  var wizardStr = '<tr><td><div class="wizard-steps-parent"><div class="wizard-steps">';
	  if(wizardData)
	  {
	      if(wizardData.length>0){
		     jQuery.each(wizardData, function(i,item){ 
		        var curNode = item.CURNODE;
		        var stepClass = 'active-step';
		        if(item.CURNODE=='1') {stepClass = 'completed-step'; }
		        wizardStr += '<div class="'+stepClass+'"><a href="#">'+item.NDNAME+'</a></div>'
		     });
	      }
	  }
  	  wizardStr += '</div></div></td></tr>';
  	  
  	  return wizardStr;
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

function textComment(imgId,reqNo,processId){
	var tdObj=jQuery('#'+imgId).parent().parent();
	//td中的文本内容：
    var text = tdObj.html();
    var beginComment = text.substring(0,text.indexOf('\&nbsp'));
    var endComment = text.substring(text.indexOf('\&nbsp'));
    var saveFlag=false;
    if(text == "N/A")
    {
        return false;
    }
    //清空td中的内容
    tdObj.html('');
    var inputObj = jQuery('<input type=text />')
    .css('border-width', '1')
    .css('font-size',tdObj.css('font-size'))
    .width(tdObj.width())
    .css('background-color', tdObj.css('background-color'))
    .val(beginComment)
    .appendTo(tdObj);
	
	inputObj.trigger('focus').trigger('select');
	
	inputObj.click(
	    function()
	    {
	        return false;
	    }
	);
/*	inputObj.keyup(
	    function(event)
	    {
	        //获得键值
	        var keyCode = event.which;
	        //处理回车
	        if(13 == keyCode)
	        {
	            //获取当前文本框中的内容
	            var inputText =  jQuery(this).val();
	            if(inputText!=beginComment){
	             	tdObj.html(inputText+endComment);
	             	submitComment(inputText,reqNo,processId);//提交修改的comment
	             	saveFlag=true;
	             }else
	            	 tdObj.html(text);
	        }
	        //处理esc的情况
	        if(27 == keyCode)
	        {
	            //将td中的内容还原
	            tdObj.html(text);
	        }
	    }
	);*/
	//处理文本框失去焦点
	inputObj.blur(
	    function()
	    {
	        //获取当前文本框中的内容
//	        var inputText = jQuery(this).val();
//	        if(saveFlag){
//	        	tdObj.html(inputText);
//	        }else
//	       	 tdObj.html(text);
	        var inputText = jQuery(this).val();
	        if(inputText!=beginComment){
             	tdObj.html(inputText+endComment);
             	submitComment(inputText,reqNo,processId);//提交修改的comment
             }else
            	 tdObj.html(text);
	    }
	);
	//清除td下的文本框
	tdObj.remove('input');
}

function submitComment(comment,reqNo,processId){
	var requestUrl = jQuery('input[type="hidden"][name="requestUrl"]').val();
	var urlstr = requestUrl+"/formManageAction.it?method=editComment";
	jQuery.ajax({
		type: "POST", 
        url: urlstr, 
        data: "comment="+comment+"&requestNo="+reqNo+"&processId="+processId,
        success: function (data) { 
			alert("update success!");
		}
	});
}
