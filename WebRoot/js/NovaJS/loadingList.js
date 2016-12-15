var requestUrl="";
var currentStaff="";

jQuery(document).ready(function() {
	
	var url;// = "/wkfProcessAction.it?method=getPersonalApplyFormList";
	
	if (jQuery("#sort").val()=="approval"){
		url = "/wkfProcessAction.it?method=getPersonalApprovalFormList";
	}
	else{
		url = "/wkfProcessAction.it?method=getPersonalApplyFormList";
	}
	
    if(document.all['requestUrl']){
    	requestUrl = jQuery('input[name="requestUrl"]').val();
    	url = requestUrl + url;
    }
    
    if(document.all['currentStaffCode'])
    	currentStaff = jQuery('input[name="currentStaffCode"]').val();
	
	jQuery("#list451").jqGrid({
	   	url:url,
		datatype: 'json',
		//Request No, Request Task, Filter field1, Filter field2, Filter field3, Status, NodeStatus, Submitted Date, Requested by, Processing by, Operation
	   	colNames:[parent.common_requestNo,parent.common_requestTask,parent.common_policyNo,parent.common_status,parent.common_history_submitDate,parent.common_requestedBy,parent.common_history_processedBy,parent.common_operation,
	   	          'URGENTLEVEL','OPENFLAG','INPROCESS','ISDEPUTY','ADVISEEXPERT','EXPERTREPLIED',
	   	          'INVITEDEXPERT','SYSFORMID','TEMPSTATUS','VIEWFLAG','CANDELETE'],
	   	colModel:[
	   		{name:'REQUESTNO',resizable:true,index:'REQUESTNO',formatter:currentFormat, width:300, sorttype:'string', searchoptions:{sopt:['eq','bw','bn','cn','nc','ew','en']}},
	   		{name:'FORMNAME',index:'FORMNAME', width:400, sorttype:'string', searchoptions:{sopt:['eq','bw','bn','cn','nc','ew','en']}},
	   		{name:'CONTENT',index:'CONTENT', width:220, sorttype:'string', searchoptions:{sopt:['eq','bw','bn','cn','nc','ew','en']}},
	   		{name:'STATUS',index:'STATUS', width:120, sorttype:'string', searchoptions:{sopt:['eq','bw','bn','cn','nc','ew','en']}},
	   		{name:'SUBMISSIONDATE',index:'SUBMISSIONDATE', width:200, sorttype:'string', searchoptions:{sopt:['eq','bw','bn','cn','nc','ew','en']}},
	   		{name:'SUBMITTEDBY',index:'SUBMITTEDBY', width:160, sorttype:'string', searchoptions:{sopt:['eq','bw','bn','cn','nc','ew','en']}},
	   		{name:'PREVIOUSPROCESSOR',index:'PREVIOUSPROCESSOR', width:200, sorttype:'string', searchoptions:{sopt:['eq','bw','bn','cn','nc','ew','en']}},
	   		{name:'OPERATION',index:'OPERATION', formatter:currentFormat2, width:150, sorttype:'string'},
	   		{name:'URGENTLEVEL',index:'URGENTLEVEL',sorttype:'string',hidden:true},
	   		{name:'OPENFLAG',index:'OPENFLAG',sorttype:'string',hidden:true},
	   		{name:'INPROCESS',index:'INPROCESS',sorttype:'string',hidden:true},
	   		{name:'ISDEPUTY',index:'ISDEPUTY',sorttype:'string',hidden:true},
	   		{name:'ADVISEEXPERT',index:'ADVISEEXPERT',sorttype:'string',hidden:true},
	   		{name:'EXPERTREPLIED',index:'EXPERTREPLIED',sorttype:'string',hidden:true},
	   		{name:'INVITEDEXPERT',index:'INVITEDEXPERT',sorttype:'string',hidden:true},
	   		{name:'SYSFORMID',index:'SYSFORMID',sorttype:'string',hidden:true},
	   		{name:'TEMPSTATUS',index:'TEMPSTATUS',sorttype:'string',hidden:true},
	   		{name:'VIEWFLAG',index:'VIEWFLAG',sorttype:'string',hidden:true},
	   		{name:'CANDELETE',index:'CANDELETE',sorttype:'string',hidden:true}
	   	],
	   	rowNum: 20,
		rowList : [20,30,50],
	   	mtype: "GET",
	   	loadonce:true,
	   	rownumbers: true,
		gridview: true,
	   	pager: '#pager451',
	    viewrecords: true,
	    autowidth: true,
	    height: 300,
	    ignoreCase: true,
		caption: ""
	});
	jQuery("#list451").jqGrid('filterToolbar',{searchOperators : true});
});

jQuery(window).resize(function(){
	jQuery("#list451").setGridWidth(jQuery(window).width()-5);
});

function currentFormat(cellvalue, options, rowObject){
	var images = "";
	var link = "<a href='#'>"+cellvalue+"</a>";

	if(rowObject["OPERATION"]=='D')
	{
		var  viewFlag = "";
		if(rowObject["VIEWFLAG"] == '1')
			viewFlag = "&viewFlag=false";
		
		link = '<a href="javascript:openFormWithLayer(\''+requestUrl+'/formManageAction.it?method=displayFormContent'+viewFlag+'&operateType=view&status='+rowObject["TEMPSTATUS"]+'&requestNo='+rowObject["REQUESTNO"]+'&formSystemId='+ rowObject["SYSFORMID"] +'\')">'+cellvalue+'</a>';
	}
	else
	{
		if(rowObject["URGENTLEVEL"]=='2')
			images = images + '<image src="'+requestUrl+'/images/urgent.gif" alt="Urgent"></image>';
		
		if(rowObject["OPENFLAG"]=='0')
			images = images + '<image src="'+requestUrl+'/images/new1.gif" alt="New"></image>';
		
		if(rowObject["INPROCESS"]=='1')
			images = images + '<image src="'+requestUrl+'/images/lock.gif" alt="Lock"></image>';
		
		if(rowObject["ISDEPUTY"] == '1')
			images = images + '<image src="'+requestUrl+'/images/deputy.gif" alt="Deputy"></image>';
		
		if(rowObject["ADVISEEXPERT"] == '1')
		{
			images = images + '<image src="'+requestUrl+'/images/expert_advise.gif" alt="Advise"></image>';
			link = '<a href="javascript:openFormWithLayer(\''+requestUrl+'/formManageAction.it?method=displayFormContent&operateType=advise&requestNo='+rowObject["REQUESTNO"]+'&formSystemId='+ rowObject["SYSFORMID"] +'\')">'+cellvalue+'</a>';
		}
		else{
			if(rowObject["EXPERTREPLIED"] == '1')
				images = images + '<image src="'+requestUrl+'/images/expert_replied.gif" alt="Advise"></image>';
			else if(rowObject["INVITEDEXPERT"] == '1')
				images = images + '<image src="'+requestUrl+'/images/invite_expert.gif" alt="Invited"></image>';
			
			link = '<a href="javascript:openFormWithLayer(\''+requestUrl+'/formManageAction.it?method=displayFormContent&operateType=deal&requestNo='+rowObject["REQUESTNO"]+'&formSystemId='+ rowObject["SYSFORMID"] +'\')">'+cellvalue+'</a>';
		}
	}
	
	return images + link + '&nbsp;&nbsp;';
}

function currentFormat2(cellvalue, options, rowObject){
	if(cellvalue == 'D')
	{
		var strDelete = "<span>"+parent.common_del+"</span>";
		if(rowObject["CANDELETE"] == '1'){
			strDelete = '<a href="javascript:deleteForm(\''+rowObject["REQUESTNO"]+'\');">'+parent.common_del+'</a>';
		}
		var returnVal = strDelete + '&nbsp;&nbsp;<a href="javascript:copyRequestedForm(\''+rowObject["REQUESTNO"]+'\');">'+parent.common_copy+'</a>';
		return returnVal;
	}
	else
		return "<span></span>";
}

var xmlhttp = createXMLHttpRequest();
function getOptionList(formType){
	    type = formType;
	    var url = requestUrl + "/formManageAction.it?method=getFormList&formType="+formType;
	    xmlhttp.open("GET", url, true);
	    var objId = "formSystemId";
	    xmlhttp.onreadystatechange=handleStateChange;
	    xmlhttp.setRequestHeader("If-Modified-Since","0");
	    xmlhttp.send(null);	
}

function advanceSearchForm(){
  window.location =  requestUrl + "/wkfProcessAction.it?method=enterAdvanceQuery&queryType=01";
}

function searchForm(isExport){
	      if(document.forms[0].beginSubmissionDate.value.Trim()!=""){
	        if(isDate(document.forms[0].beginSubmissionDate,"Submitted From")==false){
	          return;
	        }
	      }
	      if(document.forms[0].endSubmissionDate.value.Trim()!=""){
	        if(isDate(document.forms[0].endSubmissionDate,"Submitted To")==false){
	          return;
	        }
	      }
	      if(document.forms[0].beginSubmissionDate.value.Trim()!="" && document.forms[0].endSubmissionDate.value.Trim()!=""){
	         if(compareDate(document.forms[0].beginSubmissionDate.value.Trim(),document.forms[0].endSubmissionDate.value.Trim())==false){
	            alert(from_to_date_compare);
	            document.forms[0].beginSubmissionDate.focus();
	            return;
	         }
	      }
	      var url =  requestUrl + "/wkfProcessAction.it?method=listPersonalApplyForm&isExport="+isExport;
	      if(isExport=="1")document.forms[0].target="_blank"
	      document.forms[0].action = url;
	      document.forms[0].submit();
	   }
   
function deleteForm(requestNo){
   var url =  requestUrl + "/formManageAction.it?method=deleteRequestedForm&requestNo="+requestNo;
   if(confirm(confirm_delete_form + requestNo)){
     var result="";
     if(xmlhttp){
       xmlhttp.open("POST", url, true);
       xmlhttp.onreadystatechange=function()
       {
          if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
             result = xmlhttp.responseText;
             if(result.Trim()=="success"){
                //alert(form_delete_success);
                //searchForm();
                jQuery("#list451").jqGrid('setGridParam',{datatype:'json'}).trigger("reloadGrid", [{current:true}]);
                jQuery('div.div-ShowSpanUp',window.parent.document).trigger("click");
             }else{
                alert(result.Trim());
             }
          }
       }
       xmlhttp.setRequestHeader("If-Modified-Since","0");
       xmlhttp.send(null);	
     }
   }
}
function copyRequestedForm(requestNo){
  var url =  requestUrl + "/formManageAction.it?method=copyRequetedForm&requestNo="+requestNo;
   if(confirm(confirm_copy_form + requestNo)){
     var result="";
     if(xmlhttp){
       xmlhttp.open("POST", url, true);
       xmlhttp.onreadystatechange=function()
       {
          if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
             result = xmlhttp.responseText;
             if(result.Trim()=="success"){
                //alert(form_copy_success);
                //document.all['requestNo'].value = "";
                //searchForm();
                jQuery("#list451").jqGrid('setGridParam',{datatype:'json'}).trigger("reloadGrid", [{current:true}]);
                
                jQuery('div.div-ShowSpanUp',window.parent.document).trigger("click");
                //parent.refreshSpanList();
             }else{
                alert(result.Trim());
             }
          }
       }
       xmlhttp.setRequestHeader("If-Modified-Since","0");
       xmlhttp.send(null);	
     }
   }
}