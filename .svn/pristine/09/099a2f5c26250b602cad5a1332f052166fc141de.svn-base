var pageNumber = 1;
var runTime = '';
var sGStatus = ' ';

jQuery(window).resize(function(){
	var frame = document;
	//var mainloc = window.frames["main"].location.toString().match(/[^\/]*$/)[0];
	
	var width = Math.max(0, ((jQuery(frame).width() - jQuery(frame).find('#layout-middle').outerWidth()) / 2) + 
    		jQuery(frame).scrollLeft())+"px";
	
	jQuery(frame).find('#layout-middle').css({'position':'absolute','left':width});
});

jQuery().ready(function () {
	
	var list = jQuery("#channel-list");
	
	jQuery(".next").click(function(){
		
		var varLeft = jQuery('#layout-middle').css("left");
		
		var left2 = list.offset().left - 1200 - parseInt(varLeft) ;
		
		list.animate({"left":left2},500);
		
    });
	
	jQuery(".pre").click(function(){
		
		var varLeft = jQuery('#layout-middle').css("left");
		
		var left1 = list.offset().left + 1200 - parseInt(varLeft);
		
		list.animate({"left":left1},500);
		
    });
	
	loadSpanList();
	loadShortCut();
	loadTdHover();
	loadListStatusNum();
	loadListStatusHover();
});

function loadSpanList(){
	
	jQuery("div.div-ShowSpan").contents().remove();
	
	var outTimer1 = setTimeout(function(){
		var html = '<div id="divSpanLoading" style="width:100%;margin-top:60px;"><div class="t-loading"></div></div>';
		jQuery(html).appendTo("div.div-ShowSpan");
		clearTimeout(outTimer1);
	}, 50);
	
	var outTimer2 = setTimeout(function(){
		runTime = GetDateT();
		bindScrollBar();
		genDivSpanList(1,runTime,sGStatus,false);
		jQuery("#divSpanLoading").remove();
		showSpanList();
		clearTimeout(outTimer2);
	}, 300);
}

function loadShortCut(){

	var requestUrl = jQuery('input[name="requestUrl"][type="hidden"]').val();
	var hidCurrentStaffCode = jQuery('input#hidCurrentStaffCode').val();

	var sql = "";

	var data = selectFromSPJson("","NovaUspGetFavorite", hidCurrentStaffCode);
	
	jQuery('td[id^=tdShortCut]').each(function(){jQuery(this).contents().remove()});
	
	if(data.length>0){
		var elementP = '', elementI = '', indexNum = 0; 
		jQuery.each(data,function(i,item2){
			elementP = '<p style="display: none;"><a class="top-link"'+ 
			'href="'+requestUrl+'/formManageAction.it?method=displayFormFill&formSystemId='+item2.FORM_SYSTEM_ID+'">'+item2.FORM_NAME+'</a></p>';
			elementI = '<img style="display: inline; filter: alpha(opacity=80); zoom: 1;" src="'+requestUrl+'/images/'+ item2.FORM_IMAGE +'" />';
			indexNum = i+1;
			jQuery('td#tdShortCut' + indexNum).append(jQuery(elementP)).append(jQuery(elementI));
		});
	}
}

function loadTdHover(){
	jQuery('td[id^=tdShortCut]').each(function(){
		jQuery(this).unbind('mouseenter').unbind('mouseleave');
		jQuery(this).hover(
		    function() {  
		    	jQuery(this).find('img').hide(); 
		    	jQuery(this).find('p').stop().animate({'opacity':'show'}, 800); 
		    },
		    function() { 
	    		jQuery(this).find('p').hide(); 
	    		jQuery(this).find('img').stop().animate({'opacity':'show'}, 800); 
		    });
	});
}

function loadListStatusHover(){
	jQuery('td.divTdWelcome3').find('td').each(function(){
		jQuery(this).unbind('mouseenter').unbind('mouseleave');
		jQuery(this).hover(
		    function() {  
		    	jQuery(this).find('span').hide(); 
		    	jQuery(this).find('p').stop().animate({'opacity':'show'}, 800); 
		    },
		    function() { 
	    		jQuery(this).find('p').hide(); 
	    		jQuery(this).find('span').stop().animate({'opacity':'show'}, 800); 
		    });
	});
}

function loadListStatusNum(){
	
	if(jQuery('span.spanStatusNum').length>0)
		jQuery('span.spanStatusNum').remove();
	
	var currentStaffCode = jQuery("#hidCurrentStaffCode").val();
	
	var data = selectFromSPJson("","poef_wkf_getHomeSpanNumByStatus",currentStaffCode+',' + runTime + ',00');
	
	if(data.length>0){
		if(data[0].NUM!=0)
			jQuery('span.spanImgDraftStatus').append(jQuery('<span class="spanStatusNum">'+data[0].NUM+'</span>'))
	}
	
	data = selectFromSPJson("","poef_wkf_getHomeSpanNumByStatus",currentStaffCode+',' + runTime + ',02');
	
	if(data.length>0){
		if(data[0].NUM!=0)
			jQuery('span.spanImgInprogressStatus').append(jQuery('<span class="spanStatusNum">'+data[0].NUM+'</span>'))
	}
	
	data = selectFromSPJson("","poef_wkf_getHomeSpanNumByStatus",currentStaffCode+',' + runTime + ',03');
	
	if(data.length>0){
		if(data[0].NUM!=0)
			jQuery('span.spanImgRejectStatus').append(jQuery('<span class="spanStatusNum">'+data[0].NUM+'</span>'))
	}
	
	data = selectFromSPJson("","poef_wkf_getHomeSpanNumByStatus",currentStaffCode+',' + runTime + ',04');
	
	if(data.length>0){
		if(data[0].NUM!=0)
			jQuery('span.spanImgCompleteStatus').append(jQuery('<span class="spanStatusNum">'+data[0].NUM+'</span>'))
	}
}

function clickStatus(sStatus){
	sGStatus = sStatus;
	runTime = GetDateT();
	pageNumber = 1;
	jQuery('#divShowHomeSpan').contents().remove();
	LoadMoreSpan(pageNumber,runTime,true);
	jQuery('#divShowHomeSpan').slimscroll({scrollTo:0});
	if(jQuery('div.div-ShowSpanDown').is(":hidden"))
		jQuery('div.div-ShowSpanDown').show();
	
}

function genDivSpanList(pageNum,runningTime,status,style){
	var currentStaffCode = jQuery("#hidCurrentStaffCode").val();
	
	var dataList = selectFromSPJson("","poef_wkf_getHomeSpanList",currentStaffCode+',' + pageNum + ',' + runningTime + ',' + status);
	
	var requestUrl = document.all['requestUrl'].value;
	
	if(dataList.length>0)
	{
		var sHTML = "", link = "";
		jQuery.each(dataList, function(i,item){ 
			link = "";
			sHTML = "";
			if(item.OPERATION=='P')
			{
				if(item.ADVISEEXPERT == '1')
				{
					link = 'javascript:openFormWithLayer(\''+requestUrl+'/formManageAction.it?method=displayFormContent&operateType=advise&requestNo='
					+item.REQUESTNO+'&formSystemId='
					+ item.SYSFORMID +'\')';
				}
				else{
					link = 'javascript:openFormWithLayer(\''+requestUrl+'/formManageAction.it?method=displayFormContent&operateType=deal&requestNo='
					+ item.REQUESTNO +'&formSystemId='
					+ item.SYSFORMID +'\')';
				}
				
				if(style)
					sHTML = sHTML + '<div class="div-receiveSpan" style="overflow: hidden; margin-left: -70px; display: block;">';
				else
					sHTML = sHTML + '<div class="div-receiveSpan">';
				
				if(item.TEMPSTATUS=='03'||item.TEMPSTATUS=='04')
				{
					sHTML = sHTML + '<img src="'+ requestUrl + '/images/' + item.FORMIMG + '">'+
								'<span class="spanReceive">'+
									'<div class="div17Font"><a href="'+link+'">'+item.FORMNAME+'</a></div>'+
									'<div class="div13Font"><span class="spanTitle">'+parent.common_requestNo+'：</span><span>'+item.REQUESTNO+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_policyNo+'：</span><span>'+item.CONTENT+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_requestedBy+'：</span><span>'+item.SUBMITTEDBY+'</span></div>'+
									'<div class="div13Font"><span class="spanTitle" style="color:red;font-weight:bold;">'+parent.common_status+'：</span><span style="color:red;font-weight:bold;">'+item.STATUS+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_submitDate+'：</span><span>'+item.SUBMISSIONDATE+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_processedBy+'：</span><span>'+item.CURRENTPROCESSOR+'</span></div>'+
								'</span>';
				}
				else
				{
					sHTML = sHTML + '<img src="'+ requestUrl + '/images/' + item.FORMIMG + '">'+
					'<span class="spanReceive">'+
						'<div class="div17Font"><a href="'+link+'">'+item.FORMNAME+'</a></div>'+
						'<div class="div13Font"><span class="spanTitle">'+parent.common_requestNo+'：</span><span>'+item.REQUESTNO+'</span>&nbsp;&nbsp;'+
							'<span class="spanTitle">'+parent.common_policyNo+'：</span><span>'+item.CONTENT+'</span>&nbsp;&nbsp;'+
							'<span class="spanTitle">'+parent.common_requestedBy+'：</span><span>'+item.SUBMITTEDBY+'</span></div>'+
						'<div class="div13Font"><span class="spanTitle">'+parent.common_status+'：</span><span>'+item.STATUS+'</span>&nbsp;&nbsp;'+
							'<span class="spanTitle">'+parent.common_submitDate+'：</span><span>'+item.SUBMISSIONDATE+'</span>&nbsp;&nbsp;'+
							'<span class="spanTitle">'+parent.common_processedBy+'：</span><span>'+item.CURRENTPROCESSOR+'</span></div>'+
					'</span>';
				}
				
				if(item.OPENFLAG=='0')
					sHTML = sHTML + '<span class="spanUnRead"></span></div>';
				else
					sHTML = sHTML + '</div>';
			}
			else if(item.OPERATION=='D')
			{
				var  viewFlag = "";
				if(item.VIEWFLAG == '1')
					viewFlag = "&viewFlag=false";
				
				link = 'javascript:openFormWithLayer(\''+requestUrl+'/formManageAction.it?method=displayFormContent'
					+viewFlag+'&operateType=view&status='
					+item.TEMPSTATUS+'&requestNo='
					+item.REQUESTNO+'&formSystemId='
					+ item.SYSFORMID +'\')';
				
				if(style)
					sHTML = sHTML + '<div class="div-sendSpan" style="overflow: hidden; margin-left: 0px; display: block;">';
				else
					sHTML = sHTML + '<div class="div-sendSpan">';
				
				if(item.OPENFLAG=='0')
					sHTML = sHTML + '<span class="spanUnRead"></span>';
				
				if(item.TEMPSTATUS=='03'||item.TEMPSTATUS=='04')
				{
					sHTML = sHTML + '<span class="spanSend">'+
									'<div class="div17Font"><a href="'+ link +'">'+item.FORMNAME+'</a></div>'+
									'<div class="div13Font"><span class="spanTitle">'+parent.common_requestNo+'：</span><span>'+item.REQUESTNO+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_policyNo+'：</span><span>'+item.CONTENT+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_requestedBy+'：</span><span>'+item.SUBMITTEDBY+'</span></div>'+
									'<div class="div13Font"><span class="spanTitle" style="color:red;font-weight:bold;">'+parent.common_status+'：</span><span style="color:red;font-weight:bold;">'+item.STATUS+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_submitDate+'：</span><span>'+item.SUBMISSIONDATE+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_processedBy+'：</span><span>'+item.CURRENTPROCESSOR+'</span></div>'+
								'</span>'+
								'<img src="'+ requestUrl + '/images/' + item.FORMIMG + '">'+
							'</div>';
				}
				else
				{
					sHTML = sHTML + '<span class="spanSend">'+
									'<div class="div17Font"><a href="'+ link +'">'+item.FORMNAME+'</a></div>'+
									'<div class="div13Font"><span class="spanTitle">'+parent.common_requestNo+'：</span><span>'+item.REQUESTNO+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_policyNo+'：</span><span>'+item.CONTENT+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_requestedBy+'：</span><span>'+item.SUBMITTEDBY+'</span></div>'+
									'<div class="div13Font"><span class="spanTitle">'+parent.common_status+'：</span><span>'+item.STATUS+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_submitDate+'：</span><span>'+item.SUBMISSIONDATE+'</span>&nbsp;&nbsp;'+
										'<span class="spanTitle">'+parent.common_processedBy+'：</span><span>'+item.CURRENTPROCESSOR+'</span></div>'+
								'</span>'+
								'<img src="'+ requestUrl + '/images/' + item.FORMIMG + '">'+
							'</div>';
				}
			}
			
			jQuery(sHTML).appendTo("div.div-ShowSpan");
		});
		
		pageNumber = pageNumber + 1;
	}
	else
	{
		jQuery('div.div-ShowSpanDown').hide();
	}
}

function showSpanList(){
	jQuery("div.div-receiveSpan").each(function() {
				jQuery(this).animate({
			     height:'toggle',width:'toggle',
			     marginLeft: parseInt(jQuery(this).css('marginLeft'),10) == 0 ?
			    		  jQuery(this).outerWidth() : 0
			    });
		});
	jQuery("div.div-sendSpan").each(function(){
				jQuery(this).animate({
				     height:'toggle',width:'toggle',
				      marginLeft: parseInt(jQuery(this).css('marginRight'),10) == 0 ?
				    		  jQuery(this).outerWidth() : 0
				    });
		});
}

function bindScrollBar(){
	
	var divPreLoad = '<div class="Maintop"></div><div class="div-ShowSpanUp">'+
		'<span class="div-ShowSpanUpImg"></span>&nbsp;&nbsp;<span class="div-ShowSpanUpSpan">REFRESH</span>&nbsp;&nbsp;'+
		'</div>';
	var divMorLoad = '<div class="div-ShowSpanDown"><span class="div-ShowSpanDownImg"></span>&nbsp;&nbsp;<span class="div-ShowSpanDownSpan">MORE</span></div>';
	
	if(jQuery("div.div-ShowSpanUp").length==0)
		jQuery("div.div-ShowSpan").before(divPreLoad);
	
	if(jQuery("div.div-ShowSpanDown").length==0)
		jQuery("div.div-ShowSpan").after(divMorLoad);
	
	jQuery('#divShowHomeSpan').slimscroll({
		  height: '450px',
		  size: '5px',
		  railOpacity: 0.3,
		  wheelStep: 10,
		  color: '#333',
		  railColor: '#FFFFFF',
		  railVisible: true,
		  allowPageScroll: false,
		  position: 'right'});
	
	jQuery('div.div-ShowSpanDown').click(function(){
		jQuery('div.div-ShowSpanDown').attr('disabled', 'disabled');
		LoadMoreSpan(pageNumber,runTime,true);
		jQuery('div.div-ShowSpanDown').removeAttr('disabled');
	});
	
	jQuery('div.div-ShowSpanUp').click(function(){
		refreshSpanList()
	});
	
}

function refreshSpanList(){
	runTime = GetDateT();
	pageNumber = 1;
	sGStatus = ' '
	jQuery('#divShowHomeSpan').contents().remove();
	LoadMoreSpan(pageNumber,runTime,true);
	jQuery('#divShowHomeSpan').slimscroll({scrollTo:0});
	if(jQuery('div.div-ShowSpanDown').is(":hidden"))
		jQuery('div.div-ShowSpanDown').show();
	
	loadListStatusNum();
}

function LoadMoreSpan(pageNumberP,runTimeP,type){
	
	var outTimer1 = setTimeout(function(){
		var html = '<div id="divSpanLoading" style="width:100%;margin-top:5px;margin-bottom:5px;"><div class="t-loading"></div></div>';
		jQuery(html).appendTo("div.div-ShowSpan");
		
		var scrollTo_val = jQuery('#divShowHomeSpan').prop('scrollHeight') + 'px';
		jQuery('#divShowHomeSpan').slimscroll({scrollTo:scrollTo_val});
		
	}, 50);
	
	var outTimer2 = setTimeout(function(){
		if(jQuery("#divSpanLoading").length>0)
			jQuery("#divSpanLoading").remove();
		genDivSpanList(pageNumberP,runTimeP,sGStatus,type);
		
		var scrollTo_val = jQuery('#divShowHomeSpan').prop('scrollHeight') + 'px';
		jQuery('#divShowHomeSpan').slimscroll({scrollTo:scrollTo_val});
	}, 300);
}

function GetDateT()
{
	 var d,s,t_s;
	 d = new Date();
	 
	 t_s = d.getTime();
	 d.setTime(t_s+2000);
	 s = d.getYear() + "-";             //取年份
	 s = s + ("" + (d.getMonth() + 101)).substr(1) + "-";//取月份
	 s += ("" + (d.getDate() + 100)).substr(1) + " ";         //取日期
	 s += ("" + (d.getHours() + 100)).substr(1) + ":";       //取小时
	 s += ("" + (d.getMinutes() + 100)).substr(1) + ":";    //取分
	 s += ("" + (d.getSeconds() + 100)).substr(1);         //取秒
	 s += "." + d.getMilliseconds();
	  
	 return s;  
} 
