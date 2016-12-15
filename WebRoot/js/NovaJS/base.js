//*************
//Brian Qiu
//set request.getContextPath()
var localObj = window.location;

var contextPath = localObj.pathname.split("/")[1];

var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;

var server_context=basePath;
  

//**************************

var currentRequests = {}, curX, curY, ra = 0, ac = 0;

jQuery(document).bind('mousemove.setcus', function (e) {
    curX = e.pageX;
    curY = e.pageY;
});

jQuery.ajaxPrefilter(function (options, originalOptions, jqXHR) {
    if (currentRequests[options.url]) {
        currentRequests[options.url].abort();
    }
    currentRequests[options.url] = jqXHR;
    ra += 1;
});

var _NOVA = {
	until : (function() {
		return {
			changeDateFormat : function(obj) {
				var inputDate = jQuery(obj).val();
				
				if(inputDate.indexOf("/") < 0 )
				{
					var mydate = '', YYYY = '', MM = '', DD = '';
					var str = inputDate;
					
					var m2 = str.match(/(\d{4})(\d{2})(\d{2})/);
					var m4 = str.match(/(\d{2})(\d{2})(\d{4})/);

					if (m2 != null && dateFormat=="yyyy/MM/dd") {
						YYYY = m2[1];
						MM = m2[2].length == 1 ? '0' + m2[2] : m2[2];
						DD = m2[3].length == 1 ? '0' + m2[3] : m2[3];
						if(DD != ''){
							mydate = YYYY + '/' + MM + '/' + DD ;
						}
					}

					if (m4 != null) {
						if(m4[3] != ''){						
							if(dateFormat=="MM/dd/yyyy"){
								YYYY = m4[3];
								MM = m4[1].length == 1 ? '0' + m4[1] : m4[1];
								DD = m4[2].length == 1 ? '0' + m4[2] : m4[2];
								
								mydate = MM + '/' + DD + '/' + YYYY;
							}else if(dateFormat=="dd/MM/yyyy"){
								YYYY = m4[3];
								DD = m4[1].length == 1 ? '0' + m4[1] : m4[1];
								MM = m4[2].length == 1 ? '0' + m4[2] : m4[2];
								mydate = DD + '/' + MM + '/' + YYYY;
							}
						}
					}
					if (mydate == '')						
						mydate = inputDate		
					
					jQuery(obj).val(mydate);
				}
				
				var validateStr = jQuery(obj).val();
				
				if(IsValidateDate(validateStr,dateFormat))
					jQuery(obj).css('color','black');
				else
					jQuery(obj).css('color','red');
		},
		html_encode : function(html) {
			html = html.replace(/&/g, '&amp;');
			html = html.replace(/</g, '&lt;');
			html = html.replace(/>/g, '&gt;');
			html = html.replace(/\"/g, '&#34;');
			html = html.replace(/'/g, '&#39;');
			return html;
		}
		}
	})()
};


function IsValidateDate(DateStr,dateformat){
	var returnVal = true;
	
	if(dateformat == 'yyyy-MM-dd' || dateformat == 'yyyy/MM/dd'||
			dateformat == 'yyyy-dd-MM' || dateformat == 'yyyy/dd/MM'){
		var IsoDateRe = RegExp("^([0-9]{4})[-\/]([0-9]{2})[-\/]([0-9]{2})$");
		var matches = IsoDateRe.exec(DateStr);
		if(!matches)
			return false;
	}
	
	if(dateformat == 'MM-dd-yyyy' || dateformat == 'MM/dd/yyyy'||
			dateformat == 'dd-MM-yyyy' || dateformat == 'dd/MM/yyyy'){
		var IsoDateRe = RegExp("^([0-9]{2})[-\/]([0-9]{2})[-\/]([0-9]{4})$");
		var matches = IsoDateRe.exec(DateStr);
		if(!matches)
			return false;
	}
	
	if(dateformat == 'yyyy-MM-dd' || dateformat == 'yyyy/MM/dd'){
		var composedDate = new Date(matches[1],(matches[2]-1),matches[3]);
		returnVal = ((composedDate.getMonth() == (matches[2]-1)) &&
				(composedDate.getDate() == matches[3]) &&
				(composedDate.getFullYear() == matches[1]));
	}
	
	if(dateformat == 'yyyy-dd-MM' || dateformat == 'yyyy/dd/MM' ){
		var composedDate = new Date(matches[1],(matches[3]-1),matches[2]);
		returnVal = ((composedDate.getMonth() == (matches[3]-1)) &&
				(composedDate.getDate() == matches[2]) &&
				(composedDate.getFullYear() == matches[1]));
	}
	
	if(dateformat == 'MM-dd-yyyy' || dateformat == 'MM/dd/yyyy' ){
		var composedDate = new Date(matches[3],(matches[1]-1),matches[2]);
		returnVal = ((composedDate.getMonth() == (matches[1]-1)) &&
				(composedDate.getDate() == matches[2]) &&
				(composedDate.getFullYear() == matches[3]));
	}
	
	if(dateformat == 'dd-MM-yyyy' || dateformat == 'dd/MM/yyyy' ){
		var composedDate = new Date(matches[3],(matches[2]-1),matches[1]);
		returnVal = ((composedDate.getMonth() == (matches[2]-1)) &&
				(composedDate.getDate() == matches[1]) &&
				(composedDate.getFullYear() == matches[3]));
	}
	
	return returnVal;
} 


//Global loading follow mouse
/*jQuery('<span/>')
	.addClass('t-loading')
    .bind("ajaxSend", function () {
        var self = jQuery(this);

        jQuery(this).hide();
        if (!self.is(':visible')) {
        	jQuery(this).show().css({
                position: 'absolute',
                top: curY + 10,
                left: curX + 15
            });

        	jQuery(document).bind('mousemove.menuLoading', function (e) {
                self.css({
                    left: e.pageX + 15,
                    top: e.pageY + 10
                });
            });
        }
    })
    .bind("ajaxComplete", function () {
        ac += 1;
        if (ac === ra) {
        	jQuery(this).hide();
        	jQuery(document).unbind('mousemove.menuLoading');
            ac = 0;
            ra = 0;
        }
    })
    .appendTo('body')
    .hide();*/




function AddMouseLoading(){
	var self;
	
	if(jQuery("#spanMouseLoading").length>0)
		self = jQuery('<span id="spanMouseLoading"/>').addClass('t-loading').appendTo('body');
	else
		self = jQuery("#spanMouseLoading");
	
	self.css({ 
		position: 'absolute',
        top: curY + 10,
        left: curX + 15
    });
	
	jQuery(document).bind('mousemove.menuLoading', function (e) {
	        self.css({
	            left: e.pageX + 15,
	            top: e.pageY + 10
	        });
    });
}

function RemoveMouseLoading(){
	jQuery("#spanMouseLoading").hide();
	jQuery(document).unbind('mousemove.menuLoading');
}