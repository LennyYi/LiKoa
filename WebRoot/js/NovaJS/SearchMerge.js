
function SelTitleItem(obj){
	jQuery(".titleitem").each(function(){
		this.style.background="none";
		$(this).css("border-left","1px solid rgb(150,150,150)");
	});	
	obj.style.background="#D31145";
	$(obj).css("border-left","1px solid #FF6D9F");
}

function ShowFrame(FrameID){
	jQuery(".SearchFrame").each(function(){
		this.style.display="none";
	});	
	jQuery("#div4iframe").css("display","none");
	if (FrameID!=""){
		jQuery("#div4iframe").css("display","");
		jQuery("#"+FrameID).css("display","");
	}
}

$("#divborder").one("click", function(){
});

document.write("<div style='position:absolute;left:2px;top:2px;border:0px;z-index:200;background:rgb(118,118,118);height:43px;font-size:15px;width:100%;line-height:43px;'>");//width:1019px;
document.write("<span class=titleitem style='float:left;height:42px;text-align:center;display:inline-block;width:230px;padding-left:15px;padding-right:15px;color:white;cursor:pointer;background:#D31145;border-right:1px solid #555;border-left:1px solid #FF6D9F;' onclick='SelTitleItem(this);ShowFrame(\"\");'>"+document.all("requestedForm").value+"</span>");
document.write("<span class=titleitem style='float:left;height:42px;text-align:center;display:inline-block;width:200px;padding-left:15px;padding-right:15px;color:white;cursor:pointer;border-right:1px solid #555;border-left:1px solid rgb(150,150,150);' onclick='SelTitleItem(this);ShowFrame(\"f1\");'>"+document.all("form_inquiry").value+"</span>");
document.write("<span class=titleitem style='float:left;height:42px;text-align:center;display:inline-block;width:200px;padding-left:15px;padding-right:15px;color:white;cursor:pointer;border-right:1px solid #555;border-left:1px solid rgb(150,150,150);' onclick='SelTitleItem(this);ShowFrame(\"f2\");'>"+document.all("advancy_query").value+"</span>");
document.write("<span class=titleitem style='float:left;height:42px;text-align:center;display:inline-block;width:0px;padding-left:15px;padding-right:15px;color:white;cursor:none;border-right:0px solid #555;border-left:1px solid rgb(150,150,150);' onclick=''>"+""+"</span>");
document.write("</div>");
document.write("<div ID=div4iframe style='display:none;position:absolute;left:2px;top:2px;border:1px solid #CCCCCC;z-index:101;width:1036px;height:720px;padding:0px;margin:0px;border-bottom:26px solid #767676;'>");//</div>");
document.write("<iframe class=SearchFrame frameborder='no' id=f1 style='display:none;position:absolute;left:0px;top:0px;z-index:100;width:1036px;height:720px;' src='"+document.all("requestUrl").value+"/wkfProcessAction.it?method=listInquiryForm&needquery=false'></iframe>");
document.write("<iframe class=SearchFrame frameborder='no' id=f2 style='display:none;position:absolute;left:0px;top:0px;z-index:100;width:1036px;height:720px;' src='"+document.all("requestUrl").value+"/workflow/commonQueryForm.jsp'></iframe>");
document.write("</div>");

jQuery(function(){
	var htmlCode;
	htmlCode="<div style='height:22px;margin-bottom:6px;background-color:#fff;font-size:12px;'>";
	htmlCode=htmlCode + "<a class=titleitem2 href='"+document.all("requestUrl").value+"/workflow/listPersonalForm.jsp?sort=submit&combin=true' style='overflow:hidden;float:left;margin:0px 0px 0px 0px;display:block;background:#fff;color:#666;height:21px;line-height:21px;width:180px;text-align:center;border-right:1px solid #E4E4E4;"+document.all("css_submit_bold").value+"'>"+menu_workspace_requested_form_subitem1+"</a>";
	htmlCode=htmlCode + "<a class=titleitem2 href='"+document.all("requestUrl").value+"/workflow/listPersonalForm.jsp?sort=approval&combin=true' style='overflow:hidden;float:left;margin:0px 0px 0px 1px;display:block;background:#fff;color:#666;height:21px;line-height:21px;width:180px;text-align:center;border-right:1px solid #E4E4E4;"+document.all("css_approval_bold").value+"'>"+menu_workspace_requested_form_subitem2+"</a>";
	htmlCode=htmlCode + "</div>";
	jQuery("#gview_list451 div:first").after(htmlCode);
});