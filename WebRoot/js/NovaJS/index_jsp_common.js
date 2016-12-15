jQuery().ready(function () {
	setTimeout('show_time()', 60);

	jQuery('.top-bg-right-search').hover(
			function () {
			    jQuery(this).addClass("activeBar1");
			  },
			  function () {
				  jQuery(this).removeClass("activeBar1");
			  });
	
	jQuery('.top-bg-right-home').hover(
			function () {
			    jQuery(this).addClass("activeBar2");
			  },
			  function () {
				  jQuery(this).removeClass("activeBar2");
			  });
	
	jQuery('.top-bg-right-new').hover(
			function () {
				jQuery(this).addClass("activeBar3");
			  },
			  function () {
				  jQuery(this).removeClass("activeBar3");
			  });

	jQuery('.top-bg-right-list').hover(
			function () {
				jQuery(this).addClass("activeBar4");
			  },
			  function () {
				  jQuery(this).removeClass("activeBar4");
			  });

	jQuery('#switchPoint').hover(function () 
			{  if(jQuery('#switchPoint').text()==4)
				{
					jQuery('#frmTitle').fadeIn(500);
					jQuery('#switchPoint').text(3);
					jQuery(window.parent.document).find("#switchPoint").attr('title','collapse');
				}
			
				else if(jQuery('#switchPoint').text()==3){
					jQuery('#frmTitle').fadeOut(500);
					jQuery('#switchPoint').text(4);
					jQuery(window.parent.document).find("#switchPoint").attr('title','expand');
				}
			
			}, function() {  });
	
	callToDo();
	
	//setInterval(callToDo,1000*20);//二十秒钟检查一遍
});


function callToDo(){
	
	var cscode = jQuery("input[name='currentStaffCode'][type='hidden']");

	var data = selectFromSPJson("","poef_wkf_getPersonalFlag", cscode.val() );
	
	if(jQuery("span.top-bg-right-home2").length>0)
		jQuery("span.top-bg-right-home2").remove();
	
	if(data.length>0){
		if(data[0].COUNTNO>0)
			jQuery('span.top-bg-right-home').append(jQuery('<span class="top-bg-right-home2">'+data[0].COUNTNO+'</span>'));
	}
	
	var interval = 30*1000; 
    
    setTimeout(arguments.callee, interval);
}

function show_time(){ 
	var time_now = new Date(); 
	
	var yyyy,MM,dd,hh,mm;
	yyyy = time_now.getFullYear();
	MM = time_now.getMonth()+ 1;
	dd = time_now.getDate();
	hh = time_now.getHours();
	mm = time_now.getMinutes();
	if(dd<10)
		dd = "0" +dd;
	if(hh<10)
		hh="0" + hh;
	if(mm<10)
		mm="0" + mm;

	  
	var today = new Array('星期日','星期一','星期二','星期三','星期四','星期五','星期六');  
	var week = today[time_now.getDay()];  
			
	
	$("#spTimeH").text(hh + ":" + mm ); 
	$("#spTimeD").text(yyyy + "-" + MM + "-" + dd ); 
	$("#spTimeW").text(week); 
	setTimeout('show_time()',60)
}

function fixPNG(myImage) {
	var arVersion = navigator.appVersion.split("MSIE");     
	var version = parseFloat(arVersion[1]);
	
	if ((version >= 5.5) && (version < 7) && (document.body.filters)){	
		var imgID = (myImage.id) ? "id='" + myImage.id + "' " : "";         
		var imgClass = (myImage.className) ? "class='" + myImage.className + "' " : "";         
		var imgTitle = (myImage.title) ? "title='" + myImage.title  + "' " : "title='" + myImage.alt + "' ";         
		var imgStyle = "display:inline-block;" + myImage.style.cssText;         
		var strNewHTML = "<span " + imgID + imgClass + imgTitle 
			+ " style=\"" + "width:" + myImage.width
			+ "px; height:" + myImage.height
			+ "px;" + imgStyle + ";"
			+ "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
			+ "(src=\'" + myImage.src + "\', sizingMethod='scale');\"></span>";
		myImage.outerHTML = strNewHTML;     
	} 
} 

function gotoSearch(){
	document.getElementById("searchForm").style.display='';
	document.getElementById("searchKey").select();
	document.getElementById("searchKey").focus();
}

function gotoHomePage(){
	//var listOffset = list.offset().left;
	//var left2 = listOffset;
	var mainloc = window.frames["main"].location.toString().match(/[^\/]*$/)[0];
	var list;
	
	if(mainloc == 'main.jsp')
	{
		list = jQuery(window.frames["main"].document).find("#channel-list") ;
    	list.animate({"left":0},500);
	}
	else
	{
		window.frames["main"].location.href='main.jsp'
		
		var iframe = document.getElementById("main");  
		
		if (iframe.attachEvent) {  
			jQuery('#main').bind('load', function() {
				list = jQuery(window.frames["main"].document).find("#channel-list") ;
		    	list.animate({"left":0},500);
		    	jQuery('#main').unbind('load');
			});
		} else {  
		    iframe.onload = function() {  
		    	list = jQuery(window.frames["main"].document).find("#channel-list") ;
		    	list.animate({"left":0},500);
		    	jQuery('#main').unbind('load');
		    };  
		}  
	}
	jQuery('div.div-ShowSpanUp',window.frames["main"].document).trigger("click");
}

function gotoNewPage(){
	//var listOffset = list.offset().left;
	//var left2 = listOffset;
	
	var mainloc = window.frames["main"].location.toString().match(/[^\/]*$/)[0];
	var list;
	
	if(mainloc == 'main.jsp')
	{
		list = jQuery(window.frames["main"].document).find("#channel-list") ;
    	list.animate({"left":-1200},500);
	}
	else
	{
		window.frames["main"].location.href='main.jsp'
		
		var iframe = document.getElementById("main");  
		
		if (iframe.attachEvent) {  
			jQuery('#main').bind('load', function() {
				list = jQuery(window.frames["main"].document).find("#channel-list") ;
		    	list.animate({"left":-1200},500);
		    	jQuery('#main').unbind('load');
			});
		} else {  
		    iframe.onload = function() {  
		    	list = jQuery(window.frames["main"].document).find("#channel-list") ;
		    	list.animate({"left":-1200},500);
		    	jQuery('#main').unbind('load');
		    };  
		}  
	}
}

function gotoListPage(){
	//var listOffset = list.offset().left;
	//var left2 = listOffset;
	
	var mainloc = window.frames["main"].location.toString().match(/[^\/]*$/)[0];
	var list;
	
	if(mainloc == 'main.jsp')
	{
		list = jQuery(window.frames["main"].document).find("#channel-list") ;
    	list.animate({"left":-2400},500);
	}
	else
	{
		window.frames["main"].location.href='main.jsp'
		
		var iframe = document.getElementById("main");  
		
		if (iframe.attachEvent) {  
			jQuery('#main').bind('load', function() {
				list = jQuery(window.frames["main"].document).find("#channel-list") ;
		    	list.animate({"left":-2400},500);
		    	jQuery('#main').unbind('load');
			});
		} else {  
		    iframe.onload = function() {  
		    	list = jQuery(window.frames["main"].document).find("#channel-list") ;
		    	list.animate({"left":-2400},500);
		    	jQuery('#main').unbind('load');
		    };  
		}  
	}
}	


