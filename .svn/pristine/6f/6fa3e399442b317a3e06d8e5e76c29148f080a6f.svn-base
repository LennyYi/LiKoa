
function setCatalog(frameEleName){
	var Acoll;
	var sequenceNo, catalogText, catalogStr;
	
	document.getElementById("sideCatalog-catalog").innerHTML = "";
    
	if (frameEleName==""){
		Acoll=jQuery("a.anchorSection");
	}
	else{
		Acoll=jQuery(window.frames[frameEleName].document).find("a.anchorSection");
	}
		
	Acoll.each(function(){
  		  sequenceNo = jQuery(this).attr("name").replace("anchor","");
		  catalogText = jQuery(this).attr("title");
		  
		  catalogStr = "";
		  catalogStr += '<dd class="sideCatalog-item1" >';
		  catalogStr +='<span class="sideCatalog-index1">'+sequenceNo+'</span>';
		  catalogStr += '<a title="'+catalogText+'" onclick="anchor('+frameEleName+','+sequenceNo+')" href="#3">'+catalogText+'</a>';
		  catalogStr +='</dd>';
		  jQuery(catalogStr).appendTo(jQuery("#sideCatalog-catalog"));
	  }); 
	
	  jQuery(".sideCatalogBtn").unbind('click');
	
	  jQuery(".sideCatalogBtn").click(function(){
		 jQuery(this).toggleClass("activeBar"); 
		 
		 if(jQuery(this).hasClass("activeBar"))
			 jQuery("div.sideCatalogBg").hide();
		 else
			 jQuery("div.sideCatalogBg").show();
	  });
	  
}


function anchor(frameEleName,Aindex){
	if (frameEleName==""){
		document.location.hash = "anchor"+index;
	}
	else{
		frameEleName.document.location.hash = "anchor"+Aindex;
	}
}

jQuery(window).scroll(function() {
	var scrolltop =$(document).scrollTop();

	var bottom = 50 - scrolltop;
	jQuery('#divTop').css('bottom',bottom);
	jQuery('#divTop').fadeIn();
});