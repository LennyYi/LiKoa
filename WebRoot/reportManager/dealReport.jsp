<%-- 
NO  Task_ID    Author     Modify_Date    Description
1.  IT0958     Robin.Hou   11/01/2007     Invite expert during processing report
2.  IT0958     Robin.Hou   11/01/2007     Expert can give the advice during processing report
3.  IT1002     Robin.Hou   04/16/2008    ���ֶ���ʾ�����У�������ֶ� ��������ĳ���ڵ��޸����ͱ����� �� �����޸ġ���
                                               �򲻹����ڿ�ʼ�ڵ㻹�Ǹ��м�ڵ㣬���ֶζ������޸ģ�������м�ڵ��б����óɡ���¼�롱�����ڵ�һ���ڵ㣨��ʼ�ڵ㣩���ֶ��ǲ���¼��ģ����м�ýڵ��ǿ���¼��ġ�
--%>
<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@ include file="/common/loading.jsp" %>
<%@page import="java.util.*,com.aiait.eflow.reportmanage.vo.*,com.aiait.eflow.common.helper.*,com.aiait.eflow.reportmanage.helper.*,com.aiait.eflow.util.FieldUtil,com.aiait.eflow.wkf.util.DataMapUtil" %>
<%@page import="com.aiait.framework.db.*,com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.*" %>
<%@page import="com.aiait.eflow.wkf.vo.WorkFlowProcessVO,com.aiait.eflow.util.StringUtil,com.aiait.eflow.reportmanage.util.DisplayReportPageUtil" %>
<%
  DisplayReportPageUtil pageUtil;
  if("1".equals(request.getSession().getAttribute("nonIE"))){
      pageUtil = new DisplayReportPageUtil(true);
  }else{
	  pageUtil = new DisplayReportPageUtil();
  }
  StaffVO currentStaff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
  FieldControlHelper.setServerUrl(request.getContextPath());
  FieldControlHelper.setCurrentStaffCode(currentStaff.getStaffCode());
  ReportFieldHelper.setServerUrl(request.getContextPath());

  ReportManageVO report = (ReportManageVO)request.getAttribute("listReport");
  String css = (String) request.getAttribute("css");
  HashMap sectionFieldMap = (HashMap)request.getAttribute("sectionFieldMap");
  HashMap onlyFillSectionFieldMap= (HashMap)request.getAttribute("onlyFillSectionFieldMap"); //�����ǰ�ڵ��ǡ�Begin���ڵ㣬����Ҫ���ÿ��section�Ƿ���������ת�п����޸ģ�������ڣ����ڡ�Begin���ڵ㲻���޸�
  String requestFormDate = "";
  
  String showCClist = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_CONFIG_SHOW_CC_LIST_SUBMIT);
  if(showCClist==null || "".equals(showCClist)){
	  showCClist = "0"; // 0---Not need to show the CC Staff selecting window; 1---Need to show the CC Staff selecting window.
  }
  
  //��ǰ�����޸ĵ�sectionFields,���sectionFieldId֮���Է��š�,�����,ÿ��field����ʽΪ��sectionId.fieldId��
  String updateSections = (String)request.getAttribute("updateSectionFields");
  if(updateSections!=null && !"".equals(updateSections)){
	  updateSections = ","+updateSections; //example: ",02.field_02_01,03.field_03_01"
  }else{
	  updateSections = "";
  }
  //��ǰ�ڵ�ſ��������sectionFields,���sectionFieldId֮���Է��š�,�����,ÿ��field����ʽΪ��sectionId.fieldId��
  String newSectionFields = (String)request.getAttribute("newSectionFields");
  if(newSectionFields!=null && !"".equals(newSectionFields)){
	  newSectionFields = ","+newSectionFields; //example: ",02.field_02_01,03.field_03_01"
  }else{
	  newSectionFields = "";
  }
  
  //SYSTEM FIELD: is_exceptional_case �Ƿ����ϵͳ�ֶΡ�is_exceptional_case���ı�־��Ĭ�ϲ�����;������ڣ������report����������isExceptionalCase��Ϊtrue,����Ϊfalse
  boolean existIsExceptionalCase = false;
  boolean isExceptionalCase = false;
  
  String operateType= (String)request.getParameter("operateType");
  if (operateType == null) {
      operateType = (String) request.getAttribute("operateType");
      if (operateType == null) {
          operateType = "view";
      }
  }
  
  String viewFlag= (String)request.getParameter("viewFlag"); //������������ǲ鿴���ϵ�ʱ��ֻ�ǲ鿴����Ҫ��ʾ�����İ�ť�������ֵ�Ļ���
  String pop = (String) request.getParameter("pop");
  //status: 00 ------ draft
  //        01 ------ submitted
  //        02 ------ in progress
  //        03 ------ rejected
  //        04 ------ completed
  String ccStaffView = (String)request.getAttribute("ccStaffView"); //CC����STAFF�򿪸�ҳ����в鿴
  if(ccStaffView!=null && "true".equals(ccStaffView)){
	  viewFlag = "false";
  }
    
  String status = (String)request.getParameter("status");
  String requestNo = (String)request.getParameter("reportNo");
  

  
  String inProcess = "";
  String lockStaffCode = null;//���inProcess=1�������������ڴ����������������ˣ�����Ϊnull
  String currentNodeId = "";
  String requestStaffCode = "";
  String lastNode = "true";//�Ƿ������һ�������ڵ�ı�־��false���ǣ�true�����һ�������ڵ�
 
  //���е�ǰ�����˵��������ı�־������ò������ڣ����ʾ�ǡ���Ҫ���е����Ĳ�����Ӧ����ʾ������ť��
  String adjustFlag = (String)request.getParameter("adjust");
  
  String printFlag = (String)request.getParameter("print");
  
 
 
  Collection sectionList = report.getSectionList();
  int sectionNum = sectionList.size();
  
  pageUtil.setCurrentNodeId(currentNodeId);
  pageUtil.setReport(report);
  pageUtil.setIsExceptionalCase(false);
  pageUtil.setNewSectionFields(newSectionFields);
  pageUtil.setOnlyFillSectionFieldMap(onlyFillSectionFieldMap);
  pageUtil.setPrintFlag(printFlag);
  pageUtil.setRequestNo(requestNo);
  pageUtil.setSectionFieldMap(sectionFieldMap);
  pageUtil.setStaff(currentStaff);
  pageUtil.setStatus(status);
  pageUtil.setUpdateSections(updateSections);
  pageUtil.setLockedStaffCode(lockStaffCode);
  
  boolean canReassign = false;
  AuthorityHelper authority = AuthorityHelper.getInstance();
  if (authority.checkAuthority(currentStaff.getCurrentRoleId(), ModuleOperateName.MODULE_FORM_INQUIRY, ModuleOperateName.OPER_FORM_INQUIRY_REASSIGN)) {
      canReassign = true;
  }
  
  long lonDateNow = new Date().getTime(); 
  String strDateNow = Long.toString(lonDateNow);
  
  String handleType = (String)request.getParameter("handleType");
  String reportSystemId = (String)request.getParameter("reportSystemId");
  String updateSectionFields = (String)request.getParameter("updateSectionFields");
  String contextPath = request.getContextPath();
  
  Collection processedNodeList = (ArrayList)request.getAttribute("processedNodeList");
  
  String reportReturn = request.getParameter("reportReturn");
  reportReturn = reportReturn == null ? "" : reportReturn;
%>

<html>
<head>
	<title>Report</title>
	<META HTTP-EQUIV="pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
	<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
	<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
	<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet"	href="<%=request.getContextPath()%>/css/smart_wizard.css" media="screen" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/form-field-tooltip.css" media="screen" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css" media="screen" type="text/css">	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/rounded-corners.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/form-field-tooltip.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/formdesign.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/table.js"></script>
	<script language="javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/windowPrompt.js"></script>
	<%if(CompanyHelper.EFlow_AIA_CHINA.equals(CompanyHelper.getInstance().getEFlowCompany())){%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/displayform_cho.js"></script>
	<%}else{%>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/displayform.js"></script>
	<%}%>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
	<link href="<%=request.getContextPath()%>/js/NovaJS/qtip/jquery.qtip.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/NovaJS/qtip/jquery.qtip.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/aiaitjsframe.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/dealform.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/selectmulti.js"></script>
	<script type="text/javascript" charset="gb2312" src="<%=request.getContextPath()%>/js/NovaJS/base.js"></script>

	<script type="text/javascript" src="<%=request.getContextPath()%>/reportManageAction.it?method=getReportScript&reportSystemId=-1"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/reportManageAction.it?method=getReportScript&reportSystemId=<%=report.getReportSystemId()%>&t=<%=strDateNow%>"></script> 

	<script language="javascript">

	  var _integrityType, _integritySQL, _integrityField;

	
	  var x = 0;

	  
	   	//window.onload=function(){resize(550);}
	   	document.oncontextmenu = function() { return false;}; 


	   	(function (){ 	   		
	   		if(!window.console) { //������console���󣬱���JS���� 
	   	 		window.console = (function(){
		   	        var instance = null;
		   	        function Constructor(){
		   	            this.div = document.createElement("console");
		   	            this.div.id = "console";
		   	            this.div.style.cssText = "filter:alpha(opacity=80);position:absolute;top:0px;left:0px;width:100%;border:1px solid #ccc;background:#eee;";
		   	            document.body.appendChild(this.div);
		   	        }
		   	        Constructor.prototype = {
		   	            log : function(str){
		   	                var p = document.createElement("p");
		   	                p.innerHTML = str;
		   	                this.div.appendChild(p);
		   	            }
		   	        }
		   	        function getInstance(){
		   	            if(instance == null){
		   	                instance =  new Constructor();
		   	            }
		   	            return instance;
		   	        }
		   	        return getInstance();
	   	   	 	})(); 
	   			window.console = {};
	   		}	   	   
	   		var console = window.console; 
	   		var funcs = ['assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 
	   	             'error', 'exception', 'group', 'groupCollapsed', 'groupEnd', 
	   	             'info', 'log', 'markTimeline', 'profile', 'profileEnd', 
	   	             'table', 'time', 'timeEnd', 'timeStamp', 'trace', 'warn']; 
		   	for(var i=0,l=funcs.length;i<l;i++) { 
		   	    var func = funcs[i]; 
		   	    if(!console[func]) 
		   	        console[func] = function(){}; 
		   	} 
		   	if(!console.memory) 
		   	    console.memory = {}; 
	   	})();
	   	function formatTime(formatStr) {   
	   		var myDate = new Date();
	   		myDate.getYear();        //��ȡ��ǰ���(2λ)
	   		myDate.getFullYear();    //��ȡ���������(4λ,1970-????)
	   		myDate.getMonth();       //��ȡ��ǰ�·�(0-11,0����1��)
	   		myDate.getDate();        //��ȡ��ǰ��(1-31)
	   		myDate.getDay();         //��ȡ��ǰ����X(0-6,0����������)
	   		myDate.getTime();        //��ȡ��ǰʱ��(��1970.1.1��ʼ�ĺ�����)
	   		myDate.getHours();       //��ȡ��ǰСʱ��(0-23)
	   		myDate.getMinutes();     //��ȡ��ǰ������(0-59)
	   		myDate.getSeconds();     //��ȡ��ǰ����(0-59)
	   		myDate.getMilliseconds();    //��ȡ��ǰ������(0-999)
	   		myDate.toLocaleDateString();     //��ȡ��ǰ����
	   		var mytime=myDate.toLocaleTimeString();     //��ȡ��ǰʱ��
	   		myDate.toLocaleString();        //��ȡ������ʱ��
	   	    var str = formatStr;   
	   	    var Week = ['��','һ','��','��','��','��','��'];  
	   	  
	   	    str=str.replace(/yyyy|YYYY/,myDate.getFullYear());   
	   	    str=str.replace(/yy|YY/,(myDate.getYear() % 100)>9?(myDate.getYear() % 100).toString():'0' + (myDate.getYear() % 100));   
	   	    str=str.replace(/MM/,myDate.getMonth()>9?myDate.getMonth().toString():'0' + myDate.getMonth());   
	   	    str=str.replace(/M/g,myDate.getMonth());   
	   	    str=str.replace(/w|W/g,Week[myDate.getDay()]);   
	   	    str=str.replace(/dd|DD/,myDate.getDate()>9?myDate.getDate().toString():'0' + myDate.getDate());   
	   	    str=str.replace(/d|D/g,myDate.getDate());   
	   	    str=str.replace(/hh|HH/,myDate.getHours()>9?myDate.getHours().toString():'0' + myDate.getHours());   
	   	    str=str.replace(/h|H/g,myDate.getHours());   
	   	    str=str.replace(/mm/,myDate.getMinutes()>9?myDate.getMinutes().toString():'0' + myDate.getMinutes());   
	   	    str=str.replace(/m/g,myDate.getMinutes());   
	   	    str=str.replace(/ss|SS/,myDate.getSeconds()>9?myDate.getSeconds().toString():'0' + myDate.getSeconds());   
	   	    str=str.replace(/s|S/g,myDate.getSeconds());   
	   	    return str;   
	   	}   
	   	//������Ҫ���ı�����fileType����̨��ȡ���ݲ���������ӵ���ҳ��
	   	function loadCSSJSContent(fileType){
	   		fileType = fileType || 'css';
	        var xmlhttp = createXMLHttpRequest();
	        var url = "<%=request.getContextPath()%>/reportManageAction.it?method=loadReportCSSJS&fileType="+fileType+"&reportSystemId=<%=report.getReportSystemId()%>";
	        var result;
	        if(xmlhttp){
	           	xmlhttp.open('POST',url,false);
	           	xmlhttp.onreadystatechange = function() {
	            	if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
	               		result = xmlhttp.responseText;
	            	}
	           	}
	           	xmlhttp.setRequestHeader("If-Modified-Since","0");
	           	xmlhttp.send(null);
	        } 
	        if(result.Trim()!=""){
	        	if(fileType=='js') {
	        		var script = document.createElement('script');
	        		script.setAttribute("type","text/javascript");
		        	try{  
		        		script.appendChild(document.createTextNode(result));  
		            } catch (ex){  
		            	script.innerText = result; //IE  
		            }  
		            document.getElementsByTagName("head")[0].appendChild(script);
	        	} else if(fileType=='css') {
	        		var style = document.createElement('style');
		        	style.setAttribute("type","text/css");
		        	try{  
		                style.appendChild(document.createTextNode(result));  
		            } catch (ex){  
		                style.styleSheet.cssText = result; //IE  
		            }  
		            document.getElementsByTagName("head")[0].appendChild(style);
	        	}
	        }
	    }
	   	
	   	function getClassesByTagName(root, tagName, className) { //��ñ�ǩ��ΪtagName,����className��Ԫ��
	   		root = root || document;
	   	    if(document.getElementsByClassName) { //֧���������
	   	          return root.getElementsByClassName(className);
	   	    } else {       
	   	    	var tags=root.getElementsByTagName(tagName);//��ȡ��ǩ
	   	        var tagArr=[];//���ڷ�������ΪclassName��Ԫ��
	   	        for(var i=0;i < tags.length; i++) {
	   			var node = tags[i];
	   	            if(node.getAttribute("className") == className || node.getAttribute("class") == className) {
	   	                tagArr[tagArr.length] = node;//��������������Ԫ��
	   	                node.parentNode.removeChild(node);
	   	                i--;
	   					node = '';
	   	            }
	   	        }
	   	        return tagArr;
	   	    }
	   	}
	   	function resetComtent(root, tagTo, classTo) { //��ñ�ǩ��ΪtagName,����className��Ԫ��
	   		var tagNodes = [];
	   		root = root || document;
	   	    if(document.getElementsByClassName) { //֧���������
	   	    	tagNodes = root.getElementsByClassName(classTo);
	   	    } else {       
	   	    	var elements=root.getElementsByTagName(tagTo);//��ȡ��ǩ
	   	    	//var reg = /^(\d+(,\d+)?)*$/;
	   	        for(var i=0;i < elements.length; i++) {
	   				var node = elements[i];
	   	            if(node.getAttribute("className") == classTo || node.getAttribute("class") == classTo) {
	   	                var content = null;
	   	             	content = node.innerHTML;
	   	             	node.innerHTML = '[' + classTo + '(' + content + ')' + classTo + ']';
	   	             	tagNodes[tagNodes.length] = node.innerHTML;//��������������Ԫ��
	   	            }
	   	         	
	   	        }
	   	    }
	   	 	return tagNodes;
	   	}
	   	function dispatch(root, tagName,className, tagFrom, classFrom, tagTo, classTo) {
	   		var result = new Array();
	   		if(document.getElementsByClassName) { //֧���������
	   	          return root.getElementsByClassName(className);
	   	    } else {       
	   	    	var tags=root.getElementsByTagName(tagName);//��ȡ��ǩ\
	   	        for(var i=0;i < tags.length; i++) {
	   				var node = tags[i];
	   				var tagArr=[];//���ڷ�������ΪclassName��Ԫ��
	   				var pageRemarks = new Array();
	   	            if(node.getAttribute("className") == className || node.getAttribute("class") == className) {
	   	               	var nodesFrom = node.getElementsByTagName(tagFrom);
	   	               	for(var j=0; j<nodesFrom.length; j++) {
	   	               		var nodeFrom = nodesFrom[j];
							if(nodeFrom.getAttribute("className") == classFrom || nodeFrom.getAttribute("class") == classFrom) {
								var content = nodeFrom.innerHTML;
								content = content.replace(/((<LI>)|(<li>))/g, '@@').replace(/<[^>]+>/g,"").replace(/<\/?.+?>/g,"")
												.replace(/(^\s*)|(\s*$)/g, "").replace(/(^\s+)|(\s+$)/g,"").replace(/&nbsp;/ig, "").replace('\r\n','');
								tagArr[tagArr.length] = content;
	   	            		}
	   	               	}
	   	               	var combineContent = '';
	   	               	for(var k=0; k<tagArr.length; k++) {
	   	               		combineContent = combineContent + ',' + tagArr[k];
	   	               	}
	   	               	var nodesTo = node.getElementsByTagName(tagTo);
	   	               	for(var l=0; l<nodesTo.length; l++) {
	   	               		var nodeTo = nodesTo[l];
							if(nodeTo.getAttribute("className") == classTo || nodeTo.getAttribute("class") == classTo) {
	   		   	             	if(nodeTo.tagName.toLowerCase()=='span') {
	   		   	             		var content = nodeTo.innerHTML;
	   		   	             		nodeTo.innerHTML = '[' + classTo + '(' + content + ')' + classTo + ']';
	   		   	             		var values = combineContent.split('@@');
	   		   	             		var keys = content.split('');
	   		   	             		var res = '';
	   		   	             		for(var idx=0; idx<keys.length; idx++){
	   		   	             			var value = values[keys[idx]];
	   		   	             			value = (value==null ? '' : value.trim());
	   		   	             			res += value + '@@';
	   		   	             		}
	   		   	             		var remark = values[0].replace(/[,:]/g, '');
	   		   	             		pageRemarks[pageRemarks.length] = i + ':' + nodeTo.innerHTML + ':' + remark + ':' + content + ':' + res;//��������������Ԫ��
	   		   	             		//if(typeof console!='undefined')
	   		   	             		console.log(nodeTo.className + ' --- ' + pageRemarks[pageRemarks.length-1]);
	   		   	            	} 
	   	            		}
	   	               	}
	   	               	if(pageRemarks.length>0) {
	   	               		pageRemarks.sort();
	   	               		result = result.concat(pageRemarks);
	   	               	}
	   	            }
	   	        }
	   	    }
	   		return result;
	   	}
	   	var Map = function(){ 
	   		this._entrys = new Array(); 
	   		this.put = function(key, value){ 
	   			if (key == null || key == undefined) { 
	   				return; 
	   			} 
	   			var index = this._getIndex(key); 
	   			if (index == -1) { 
		   			var entry = new Object(); 
		   			entry.key = key; 
		   			entry.value = value; 
		   			this._entrys[this._entrys.length] = entry; 
	   			}else{ 
	   				this._entrys[index].value = value; 
	   			} 
	   		}; 
	   		this.get = function(key){ 
	   			var index = this._getIndex(key); 
	   			return (index != -1) ? this._entrys[index].value : null; 
	   		}; 
	   		this.remove = function(key){ 
	   			var index = this._getIndex(key); 
	   			if (index != -1) { 
	   			this._entrys.splice(index, 1); 
	   			} 
	   		}; 
	   		this.clear = function(){ 
	   			this._entrys.length = 0;; 
	   		}; 
	   		this.contains = function(key){ 
	   			var index = this._getIndex(key); 
	   			return (index != -1) ? true : false; 
	   		}; 
	   		this.getCount = function(){ 
	   			return this._entrys.length; 
	   		}; 
	   		this.getEntrys = function(){ 
	   			return this._entrys; 
	   		}; 
	   		this._getIndex = function(key){ 
	   			if (key == null || key == undefined) { 
	   				return -1; 
	   			} 
	   			var _length = this._entrys.length; 
	   			for (var i = 0; i < _length; i++) { 
		   			var entry = this._entrys[i]; 
		   			if (entry == null || entry == undefined) { 
		   				continue; 
		   			} 
		   			if (entry.key === key) {//equal 
		   			return i; 
		   			} 
	   			} 
	   			return -1; 
	   		}; 
	   		this._toString = function(){ 
	   			var string = ""; 
	   			for (var i = 0; i < this.getEntrys().length; i++) { 
		   			string += this.getEntrys()[i].key+"::"+this.getEntrys()[i].value; 
		   			if(i!=this.getEntrys().length-1){ 
		   				string += ";"; 
		   			} 
	   			} 
	   			return string; 
	   		}; 
	   	}; 
	   	// ����ҳ�����ݵ�word ���� keyΪ��ҳ�ؼ��֣�wordNameΪ������ĵ���
	   	/* ���ҳ��ʶΪ^newPage ��ҳ���еķ�ҳ��ǩΪ
	   	<span class="newPage" lang=EN-US
			style="font-size: 10.5pt; mso-bidi-font-size: 12.0pt; font-family:"
			mce_style="font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:"
			Times New
			Roman';mso-fareast-font-family:����;mso-font-kerning:1.0pt;mso-ansi-languageEN-US;mso-fareast-language:ZH-CN;mso-bidi-language:AR-SA'><br
			clear=all style="mso-special-character:line-break;page-break-before: always"
			mce_style="page-break-before:always"></span>
	   	*/

	   	function showSplish(needShow) {
	   		var div = document.getElementById("loading");
		    var div1 = document.getElementById("mask");
			if(needShow==false){
		    	div.style.display="none";
				div1.style.display="none";
				//if(typeof console!='undefined')
				console.log('hide splish');
		    } else {
				div.style.display="inline";
			    div1.style.display="inline";
			  	//if(typeof console!='undefined')
			    console.log('show splish');
		    }
	   	}
	   	/* ShellSpecialFolderConstants
	   	DESKTOP = 0x00, Windows desktop��the virtual folder that is the root of the namespace.
	   	DESKTOPDIRECTORY = 0x10, File system directory used to physically store the file objects that are displayed on the desktop. It is not to be confused with the desktop folder itself, which is a virtual folder. A typical path is C:\Documents and Settings\username\Desktop.
	   	FAVORITES = 0x06,  File system directory that serves as a common repository for the user's favorite URLs. A typical path is C:\Documents and Settings\username\Favorites.
	   	HISTORY = 0x22, File system directory that serves as a common repository for Internet history items.
	   	PERSONAL = 0x05, File system directory that serves as a common repository for a user's documents. A typical path is C:\Users\username\Documents.
	   	RECENT = 0x08, File system directory that contains the user's most recently used documents. A typical path is C:\Users\username\AppData\Roaming\Microsoft\Windows\Recent.
	   	SENDTO = 0x09, File system directory that contains Send To menu items. A typical path is C:\Users\username\AppData\Roaming\Microsoft\Windows\SendTo.
	   	TEMPLATES = 0x15, File system directory that serves as a common repository for document templates.
	   	*/
	   	function chooseFolder(locate){
	   		var folder = '';	  
	   		var Shell = new ActiveXObject( "Shell.Application" );
	   		if(locate!=-1) {
	   	 		try{
	   	 			console.log('select a folder to save document');
	   	 			var Message = "Please choose a folder to save the document";  //ѡ�����ʾ��Ϣ
	   	  			//var fd = new ActiveXObject("MSComDlg.CommonDialog");
	        		//fd.Filter = "*.xml"; //�^�V�ļ���ͣ��F�ھ�ֻ�ܴ��.xml���ļ���
	        		//fd.FilterIndex = 2;
	        		//fd.MaxFileSize = 128;
	       			//fd.ShowSave();//�@���ǃ���Č�Ԓ���������Ҫ���_��Ԓ����Ҫ��fd.ShowOpen();
	        		//folder=fd.filename;//fd.filename���Ñ��x���·��
	        		folder = Shell.BrowseForFolder(0,Message,0x0040,locate);//��ʼĿ¼Ϊ���ҵĵ��� 0x11
	   	 	 		//var Folder = Shell.BrowseForFolder(0,Message,0); //��ʼĿ¼Ϊ������
	   	  			if(folder != null){
		   	  			folder = folder.items();  // ���� FolderItems ����
		   	  			folder = folder.item();  // ���� Folderitem ����
		   	 			folder = folder.Path;   // ����·��
		   	  		}	   	 		  	  		
		   	 	}catch(e){
		   	  		console.log('open filebrowser failed: ' + e.message +'\r\nnow return a default path!');
		   	 	}
	   		} else {
	   			try {
	   				folder = Shell.NameSpace(0x0005).Self.Path;//����ϵͳC:\\Users\\ASNPH4H\\Documents\\Ŀ¼Folder��Ŀ��
	   			} catch(e) {
	   				console.log('can not find the path by namespace way: '  + e.message + '\r\nnow try to find the folder on regedit');
	   				try {
	   				 	var shell = new ActiveXObject("WScript.Shell");
	   					folder = shell.RegRead("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\\Personal");//--��ȡע���
	   					//var  key  = shell.RegRead("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders\\Personal");
	   					//shell.RegDelete("HKEY_CURRENT_USER\\Software\\AC3Filter\\equalizer\\Linear scale\\eq_freq_0");//-- ɾ��ע���
	   					//shell.RegWrite("HKEY_CURRENT_USER\\Software\\AC3Filter\\equalizer\\Linear scale\\eq_freq_0", "10","REG_DWord");//-- д��ע���
	   				} catch(ex) {
	   					console.log('cannot find the folder path on regedit: ' + ex.message + '\r\nnow return a default path!');
	   				}
	   			}	   			
	   		}
	   		if(folder.charAt(folder.length-1) != "\\"){
	    		folder = folder + "\\";
	    	}
	   		console.log('the folder select is ' + folder);
	   	 	return folder;
	   	}
	   	
	   	function checkWordProgress(strProcess) {
	   		var t;
	   		var locator = new ActiveXObject ("WbemScripting.SWbemLocator");
	   		var service = locator.ConnectServer(".");
	   		var properties = service.ExecQuery("SELECT * FROM Win32_Process");
	   		var np = new Enumerator (properties);
	   		for (;!np.atEnd();np.moveNext())
	   		{
	   		    t=t + np.item().Name + "\n";
	   		 	var p = eProc.item().Name; 
	   	        if(p.toUpperCase() == strProcess.toUpperCase()) {
	   	            break;
	   	         (new ActiveXObject("WScript.Shell")).run("taskkill /f /t /im QQ.exe",0);
	   	        }
	   		}
	   		// ����Ҫ�жϵĽ���Ϊcalc.exe
	   		if(t.indexOf("calc.exe") > -1){
	   		    alert("calc is run");
	   		}else{
	   		    alert("calc is stop");
	   		}
	   	}
	    /* //�ַ�����ת
        function strturn(str) {
            if (str != "") {
            var str1 = "";
            for (var i = str.length - 1; i >= 0; i--) {
                str1 += str.charAt(i);
            }
            return (str1);
            }
        } */
	   	function generateFile(filePath, fileName, increment) {
        	fileName = fileName || formatTime('yyyyMMddHHmmss');
        	if(filePath=='\\') return fileName;
	   		var result = filePath + fileName;
	   		/* var extName = '.doc';
	   		var realName = fileName;
	   		var pos = fileName.lastIndexOf('.');
	   		if(pos!=-1) {
	   			var ext = fileName.toUpperCase().substring(pos,fileName.length);//��׺��
	   			if(ext=='.DOC'||ext=='.PDF') {
	   				extName = ext;
	   				realName = fileName.substring(0, pos);
	   			}
	   		}
	   		alert(realName.split('[(_V)(_v)]')[0]); */
	   		
	   		//var extName = "." + fileName.replace(/.+\./, "");
	   	 	//var posx = strturn("." + fileName.replace(/.+\./, ""));
         	//var file = strturn(fileName);
         	//var pos1 = strturn(file.replace(posx, ""));
         	//var names = pos1.split("\\");
         	//var realName = names[names.length - 1];
  
	   		if(increment==1) { // �Զ�����
	   			var fso = new ActiveXObject("Scripting.FileSystemObject");
	   			var path = filePath=='' ? fso.GetSpecialFolder(2) : filePath;
	   			console.log('folder: ' + path + '\tthe original path: ' + filePath);
	   			var folder = fso.GetFolder(path); 
	   			console.log("�ļ���:" + folder.Files.Count); 
	   			var filesEnum = new Enumerator(folder.Files);	
	   			var re = new RegExp("^(("+fileName+")(_[Vv])?(\\d+(\\.\\d+))?((\\.DOC)|(\\.doc)|(\\.PDF)|(\\.pdf))?)$","ig"); //�����������ʽ��ʽ. 
	   			var files = new Array();
	   			for (; !filesEnum.atEnd(); filesEnum.moveNext()) { 
	   			   	var tempName = filesEnum.item().Name.toUpperCase();
	   				console.log(tempName); 
	   			   	if(tempName.match(re)){
	   					files[files.length] = parseFloat('0'+RegExp.$4);
	   					console.log('find ' + RegExp.$1 + '|' + RegExp.$2 + '|' + RegExp.$3 + '|' + RegExp.$4 + '|' + RegExp.$5 + '|' + RegExp.$6 + '|' + RegExp.$7 + '|' + RegExp.$8 + '|' + RegExp.$9);
	   			   	}
	   			}
	   			files.sort();
	   			var index = 0.0;
	   			if(files.length==0) {
	   				index = 0.1;
	   				console.log('can\'t find ' + fileName + ' in folder of ' + path);
	   			} else {
	   				index = parseFloat(files[files.length-1]) + 0.1;
	   				index = index.toFixed(1);
	   				console.log('last version is ' + index);
	   			}	   			
	   			files[files.length] = index;	   			
	   			console.log('files: ' + files);
	   			result = result + '_V' + index;
	   		} else if(increment==0) {
	   			//result = filePath + fileName;
	   		} else if(increment==-1) {
	   			result = result + '_' + formatTime('yyyyMMddHHmmss');
	   		}
	   		console.log('generate the filename is ' + result);
	   		return result;
	   	}
	   	
	   	function ExportAllToWord(key, wordName, fileType, useFootnote){
	   		wordName = generateFile(chooseFolder(-1), wordName, 0);
	   		//return;
	   		showSplish();
	   		//document.body.style.overflow='hidden';
			loadCSSJSContent();
		   	useFootnote = useFootnote || false;
		   	key = key || '^newPage';
		   	fileType = fileType || '.doc';		   	
	   		var word = new ActiveXObject("Word.Application");
	   		word.Application.DefaultSaveFormat = "";
	   		//obj.run("Progra~1/xx/xx/xx.exe") 
			var clipboardData = null;
	   		try {
	   			clipboardData = window.clipboardData.getData("text"); //�����а����ݱ�������
	   			window.clipboardData.clearData('text'); //ʹ�ü��а�֮ǰ��ռ��а�����
		   	} catch (e) {
		        //IE��window.clipboardData.setData��getData�ᱨ
		        //"OpenClipboard Failed"���󣬵����ܸ��Ƴɹ���
		        //�����Ƕ�����ͬʱ����(��ȡ/����/���)����������
		        //alert(e.message);
		    } 
		   	var root = document.getElementById('thedetailtableDIV');
	   		var doc = word.Documents.Add("",0,1);
	   		doc.ShowGrammaticalErrors = false; //�����﷨���
	   	 	doc.ShowSpellingErrors = false;  //�����﷨���
	   	    doc.PageSetup.TopMargin = word.Application.InchesToPoints(0.5);
	   		doc.PageSetup.BottomMargin = word.Application.InchesToPoints(0.5);
	   		doc.PageSetup.LeftMargin = word.Application.InchesToPoints(0.5);
	   		doc.PageSetup.RightMargin = word.Application.InchesToPoints(0.5);
	   		//�����ӡ���ò���
			/* doc.PageSetup.LineNumbering.Active = false;
			doc.PageSetup.Orientation.wdOrientLandscape =  1; //����Ϊ�����ӡ
			doc.PageSetup.TopMargin =  word.Application.CentimetersToPoints(3.17);
			doc.PageSetup.BottomMargin =  word.Application.CentimetersToPoints(3.17);
			doc.PageSetup.LeftMargin = word.Application.CentimetersToPoints(2.54);
			doc.PageSetup.RightMargin = word.Application.CentimetersToPoints(2.54);
			doc.PageSetup.Gutter = word.Application.CentimetersToPoints(0);
			doc.PageSetup.HeaderDistance = word.Application.CentimetersToPoints(1.5);
			doc.PageSetup.FooterDistance = word.Application.CentimetersToPoints(1.75);
			doc.PageSetup.PageWidth = word.Application.CentimetersToPoints(29.7);
			doc.PageSetup.PageHeight = word.Application.CentimetersToPoints(21);
			doc.PageSetup.FirstPageTray =  0;
			doc.PageSetup.OtherPagesTray = 0;
			doc.PageSetup.SectionStart =  2;
			doc.PageSetup.OddAndEvenPagesHeaderFooter = false;
			doc.PageSetup.DifferentFirstPageHeaderFooter = false;
			doc.PageSetup.VerticalAlignment = 0;
			doc.PageSetup.SuppressEndnotes = false;
			doc.PageSetup.MirrorMargins = false;
			doc.PageSetup.TwoPagesOnOne = false;
			doc.PageSetup.BookFoldPrinting = false;
			doc.PageSetup.BookFoldRevPrinting = false;
			doc.PageSetup.BookFoldPrintingSheets = 1
			doc.PageSetup.GutterPos = 0;
			doc.PageSetup.LayoutMode = 2;  */
	   	 	
	   		var range = doc.Range(0,1);
	   		var tags = new Array('script','input','select','button','textarea','div','span','iframe','form','table','label','dd','a','img');//�˴�Ӧ��ע�⣬�����Ƴ���Ӧ������ǰ�ߣ�������ʾ�����ں���
	   		//var tags = new Array('a', 'acronym', 'address', 'applet', 'area', 'b', 'base', 'basefont', 'bdo', 'bgsound', 'big', 'blockquote', 'body', 'br', 'button', 'caption', 'center', 'cite', 'code', 'col', 'colgroup', 'custom', 'dd', 'del', 'dfn', 'dir', 'div', 'dl', 'document', 'dt', 'em', 'embed', 'fieldset', 'font', 'form', 'frame', 'frameset', 'head', 'hn', 'hr', 'html', 'i', 'iframe', 'img', 'ins', 'kbd', 'label', 'legend', 'li', 'link', 'listing', 'map', 'marquee', 'menu', 'ol', 'p', 'plaintext', 'pre', 'q', 's', 'samp', 'script', 'select', 'small', 'span', 'strike', 'strong', 'sub', 'sup', 'table', 'tbody', 'td', 'textarea', 'tfoot', 'th', 'thead', 'title', 'tr', 'tt', 'u', 'ul', 'var', 'xmp');
	   		var head = getClassesByTagName(root, 'div', 'HeaderPage');
	   		var foot = getClassesByTagName(root, 'div', 'FooterPage');
	   		var remarkTags = [];
	   		var remarkNo = [];
	   		var remarks = [];
	   		remarkNo = dispatch(root, 'div', 'Wpage', 'div', 'remarks', 'span', 'remarkNo');
	   		//remarkNo = resetComtent('span','remarkNo');
	   		if(useFootnote==true) {
	   			remarks = getClassesByTagName(root, 'div', 'remarks');
	   			remarks.length = 0;
	   		} else {
	   			remarks = resetComtent(root, 'div', 'lineShape');
	   		}
	   		//var newPages = getClassesByTagName('span','newPage');
	   		//for(var idx = 0; idx < newPages.length; idx++) 
	   		//	alert(newPages[idx].innerHTML);
	   		//alert(head[0].innerHTML);
	   		var sel = document.body.createTextRange();
	   		var copys = root.cloneNode(true);
	   		for(var i=0; i<tags.length; i++) {
	   			var nodes = root.getElementsByTagName(tags[i]);//����word��������tag��Χ���ݿ�����word���档		
	   			for(j=0;j<nodes.length;j++){
	   				var node = nodes[j];
	   				var tagName = node.tagName.toLowerCase();
	   				if(tagName=='script' || tagName=='input') {
	   					//if(typeof console!='undefined')
	   					console.log('the tagName of the node is ' + tagName + ', so skip it');
	   					node.parentNode.removeChild(node);
	   					//var oText=document.createTextNode("");
	   					//node.parentNode.replaceChild(oText,node);
	   					j--;
	   					node = '';
	   					continue;
	   				}/*  else if(node.type=='hidden' || node.type=='checkbox' || node.type=='button' || node.type=='none' || node.type=='password' || node.type=='radio' || node.type=='reset' || node.type=='submit' || node.type=='text'){
	   					//if(typeof console!='undefined')
	   					console.log('the type of the node is ' + node.type + ', so skip it');
	   					node.parentNode.removeChild(node);
	   					//var oText=document.createTextNode("");
	   					//node.parentNode.replaceChild(oText,node);
	   					j--;
	   					node = '';
	   					continue;
	   				} else if(node.style.display=='none' || node.style.display=='hidden' || node.className.display=='none' || node.className.display=='hidden') {
	   					//if(typeof console!='undefined')
	   					console.log('the style of the node is ' + node.style.display + ', so skip it');
	   					node.parentNode.removeChild(node);
	   					//var oText=document.createTextNode("");
	   					//node.parentNode.replaceChild(oText,node);
	   					j--;
	   					node = '';
	   					continue;
	   				} */
	   				//if(typeof console!='undefined')
	   				console.log(node.tagName + ' tag ' + j + ' of ' + nodes.length + ', the name is ' + node.name + ', the type is ' + node.type);
	   				//if(typeof console!='undefined')
	   				console.log('try to copy ' + node.tagName + ', the id is ' + node.id + ', the name is ' + node.name + ', the className is ' + node.className + ', the nodeType is ' + node.type);
	   				//if(node.id=='loading' || node.id=='mask')continue;
	   				try{
	   					if(tagName=='table') {
	   						var table_cells = node.rows[0].cells;
	   						for(var k=0;k<table_cells.length;k++){
	   						    range = doc.Range(range.End-1, range.End); //�趨λ�������������¡���������
	   						    sel.moveToElementText(table_cells[k]); //����Ԫ�����ݸ��Ƶ�word
	   						    sel.select();
	   						    sel.execCommand("Copy");
	   						    sel.moveEnd('character');  //������䵼�����ˣ��������Ϊcharacter������copy
	   						    range.Collapse();
	   						    range.Paste();
	   						    //range = doc.Range(range.End-1, range.End);
	   						}
	   					} else if(tagName=='img') { //�����ͼƬ����ֱ������
	   						range = doc.Range(range.End-1, range.End);
	   						word.Application.Selection.InlineShapes.AddPicture(node.src,false,true); 
	   						//j++;
	   					} else {
	   						range = doc.Range(range.End-1, range.End); //�趨λ�������������¡��������� --���Ʋ�ͬ�Ķ�������Ҫд������ д������仰����λ��
	   						sel.moveToElementText(node);
	   						sel.select();
	   						sel.execCommand('Copy');
	   						sel.moveEnd('character');  //������䵼�����ˣ��������Ϊcharacter������copy
	   						range.Collapse();
	   						range.Paste();
	   					}
	   				} catch(e) {
	   					//if(typeof console!='undefined')
	   					console.log('somthing goes wrong: ' + e.message + ' -- ' + e.description);
	   				}
	   				//if(typeof console!='undefined')
	   				console.log((node.id || node.name || node.className) + '\tCopied');
	   				node.parentNode.removeChild(node);
	   			}
	   		}
	   		
	   		var selection = word.Selection; //�õ��򿪺�word��selection���� 
	   		selection.Font.Size = 10; //���������С
	   		//selection.Font.Name = "Times New Roman";
	   		//selection.Font.AllCaps = true;
	   	 	selection.WholeStory(); //ctrl+A ȫѡ���� 
	   	 	selection.Find.ClearFormatting(); //�����ʽ  
	   	 	selection.Find.Text = key; //ָ�����ҹؼ��� --^mΪ�ֶ���ҳ����� 
	   	 	selection.Find.Forward = true; //���²���
	   	 	selection.Find.Wrap = 1; 
	   	 	selection.Find.MatchCase = false; //�����ִ�Сд  
	   	 	selection.Find.MatchWholeWord = false;  //��ƥ����������  
	   	 	for(var idx=1; idx<selection.Tables.Count; idx++) {
	   	 		var table = selection.Tables.Item(idx);
	   	 		table.Style.ParagraphFormat.LineUnitAfter = 0;
	   	 		//table.Style.ParagraphFormat.LineUnitBefore = 2;
	   	 		table.Style.ParagraphFormat.SpaceAfter = 0;
	   	 	} 
	   	 	//����ҵ�ָ���ַ���������,���򷵻�false 
	   	 	while (selection.Find.Execute()) {  
	   	  		selection.InsertBreak(7); //�����ҳ��,��ҳ������Ϊ7,����ɲ�word api 
	   	 	}  
	   	 	
	   	 	selection.Find.Replacement.Font.Superscript = true;
	   	 	//selection.Find.MatchPrefix = true;
	   	 	//selection.Find.MatchSuffix = true;
	   	 	//selection.Find.MatchWildcards = true;
	   	 	var count = 0;	   	 	
	   	 	for(var idx1=0; idx1<remarks.length; idx1++) {	
	   	 		/* selection.WholeStory(); //ctrl+A ȫѡ���� 
	   	 		selection.Find.ClearFormatting(); //�����ʽ  
	   	 		//selection.Find.Text = key; //ָ�����ҹؼ��� --^mΪ�ֶ���ҳ����� 
	   	 		selection.Find.Forward = true; //���²���
	   	 		selection.Find.Wrap = 1; 
	   	 		selection.Find.MatchCase = false; //�����ִ�Сд  
	   	 		selection.Find.MatchWholeWord = false;  //��ƥ����������   */
		   	 	var searchText = remarks[idx1]; //ָ�����ҹؼ��� --^mΪ�ֶ���ҳ�����
		   		//if(typeof console!='undefined')
		   		console.log('find ' + searchText);
		   		if(selection.Find.Execute(searchText, true, true, false, false, false, true, false, false, '', 1, false, false, false, false)) {//����ҵ�ָ���ַ���������,���򷵻�false 
		   			count++;
		   			selection.InsertBreak(6);
		   			selection.InlineShapes.AddHorizontalLineStandard(selection.Range);		   			
		   			var currentLine = selection.Range.Information(3);//�ҵ���ǰ������		   			
		   			//for(var i=37-currentLine; i>0; i--) {	//A4��ֽ�ţ�ÿҳ�����趨48��
		   			//	selection.InsertBreak(6);
		   			//}
		   			var lineCount = 0;
		   			/* while(1){
		   				selection.MoveDown(5, 1);
			   			selection.Select();
			   			var str = selection.Text.trim();
			   			console.log(str);
			   			if(str=='P')break;			   			
		   			} */
		   			//if(typeof console!='undefined')
		   			console.log('the cursor now is at line ' + currentLine + ', need to insert ' + (48-currentLine) + ' line breaks');
		   			//var content = remarkTags[idx1].replace(/<[^>]+>/g,"").replace(/<\/?.+?>/g,"").replace(/(^\s*)|(\s*$)/g, "").replace(/(^\s+)|(\s+$)/g,"").replace(/&nbsp;/ig, "");
		   			
		   		}	 
	   	 	}
	   		//if(typeof console!='undefined')
	   	 	console.log(count + ' remarks found!');  
	   	 	
	   	 	count = 0;
	   	 	selection.WholeStory(); //ctrl+A ȫѡ���� 
	   	 	selection.Find.ClearFormatting(); //�����ʽ  
	   	 	selection.Find.Forward = true; //���²���
	   	 	selection.Find.Wrap = 1; 
	   	 	selection.Find.MatchCase = false; //�����ִ�Сд  
	   	 	selection.Find.MatchWholeWord = false;  //��ƥ����������  
	   	 	//selection.Find.MatchPrefix = true;
	   	 	//selection.Find.MatchSuffix = true;
	   	 	//selection.Find.MatchWildcards = true;
	   	 	var pages = new Map();
	   	 	//remarkNo.sort();
	   		for(var idx=0; idx<remarkNo.length; idx++) {
	   			selection.WholeStory(); //ctrl+A ȫѡ���� 
	   	 		selection.Find.ClearFormatting(); //�����ʽ  
	   	 		//selection.Find.Font.Superscript = true;
	   	 		//selection.Find.Replacement.Font.Superscript = true;
	   	 		//selection.Font.Superscript = true;
	   	 		var contents = remarkNo[idx].split(':');
	   	 		var remark = contents[2] + ':';
		   	 	var list = contents[3].replace(/(^\s*)|(\s*$)/g, "").replace(/(^\s+)|(\s+$)/g,"").split('');
	   			var tips = contents[4].split('@@');
	   			var key = contents[0].split(',')[0];
	   			var value = '';
	   			if(pages.contains(key)) {
	   				value = pages.get(key);
	   			}
	   	 		var searchText = contents[1]; //ָ�����ҹؼ��� --^mΪ�ֶ���ҳ�����
	   	 		//if(typeof console!='undefined')
	   	 		console.log('find ' + contents);//�����ҳ��,��ҳ������Ϊ7,����ɲ�word api
	   	 		var replaceText = '';
	   	 		if(useFootnote==false) {
	   	 			replaceText = ' ' + contents[3];
	   	 		}
	   			if(selection.Find.Execute(searchText, true, true, false, false, false, true, false, false, replaceText, 1, false, false, false, false)) {//����ҵ�ָ���ַ���������,���򷵻�false 
	   				count++;
	   				selection.Font.Size = 10; //���������С
	   				selection.Font.Superscript = true;
	   				if(useFootnote==false)continue;
		   			for(var j=0; j<list.length; j++) {
		   				var index = list[j];
		   				if(index==',') {
		   					//selection.Font.Superscript = true;
		   					//selection.InsertAfter(index);
		   					//selection.InsertBefore(index);
		   				} else if(value.indexOf(index)==-1) {
		   					//if(typeof console!='undefined')
		   					console.log('add ' + key + ':' + index + '-------' + tips[j]);
		   					var footnote = selection.Footnotes.Add(selection.Range, index, tips[j]);
			   				value = value.concat(index);
		   				} else {
		   					//if(typeof console!='undefined')
		   					console.log('insert ' + '-------' + index);
		   					selection.InsertCrossReference(3, 16, index, false, false, true, ',');
		   				}
		   			}  
		   			pages.put(key, value);
	   			}	   			
	   		} 
	   		//if(typeof console!='undefined')
	   		console.log(count + ' remark found!');
	   	 	 
	   	 	if(head.length>0) {
	   	 		//range = selection.Sections(1).Headers(1).Range(0,1);
	   			var header = document.createElement();
	   			header.innerHTML = '<div style="width: 600px; text-align: left; overflow: hidden;"><div style="height: 1023px; overflow: hidden;"><div id="sectionTable1"><DIV class=HeaderPage>' + head[0].innerHTML + '</div></div></div>';
	   			root.appendChild(header);
	   			//cleanWhitespace( document.body );
	   	 		sel.moveToElementText(header);
	   	 		sel.select();
	   	 		sel.execCommand('Copy');
	   	 		sel.moveEnd('character');  //������䵼�����ˣ��������Ϊcharacter������copy
	   	 		root.removeChild(header);
	   	 		//Headers( 3 ) --- ����ż��ҳ�ϵ�����ҳü��ҳ��
	   	 		//Headers( 2 ) --- �����ĵ�����еĵ�һ��ҳü��ҳ��
	   	 		//Headers( 1 ) --- �����ĵ�����г���һҳ������ҳ�ϵ�ҳü��ҳ��
	   	 		word.ActiveWindow.ActivePane.View.SeekView = 10;  
	   	 	 	//selection.Fields.Add(selection.Range, 33);
	   	 	 	selection.Sections(1).Headers(1).Range.Font.Size = 10.5; 
	   	 		selection.Sections(1).Headers(1).Range.Collapse();
	   	 	 	selection.Sections(1).Headers(1).Range.Paste();
		   	 	var paragraphs = selection.Sections(1).Headers(1).Range.Paragraphs;
	   			paragraphs.Item(paragraphs.Count-1).SpaceBefore = 0;
	   			paragraphs.Item(paragraphs.Count-1).SpaceAfter = 0;
	   			paragraphs.Item(paragraphs.Count-1).LineUnitAfter = 0;
	   			paragraphs.Item(paragraphs.Count-1).KeepWithNext = true;
	   			paragraphs.Item(paragraphs.Count).PageBreakBefore = false;
	   			paragraphs.Item(paragraphs.Count).Range.Cut();
	   	 		//selection.Sections(1).Headers(1).Range.ParagraphFormat.SpaceAfter = 0;
	   	 		//selection.Sections(1).Headers(1).Range.ParagraphFormat.LineUnitAfter = 0;
	   	 	 	//selection.Sections(1).Headers(1).Range.Text = ReplaceTags(head); 
	   	 	 	//selection.ParagraphFormat.Alignment = 1; 
	   	 	 	//selection.Sections(1).Headers(1).Range.Font.Size = 10.5; 
	   	 	 	//selection.Sections(1).Headers(1).Range.InsertAfter(ReplaceTags(foot));
	   	 	 	//����ͼƬ��ҳü�е��������
	   	 	 	//selection.Sections(1).PageSetup.LeftMargin = 20;
	   	 	 	//selection.Sections(1).PageSetup.HeaderDistance = 20;
	   	 	 	//selection.Sections(1).Headers(1).Shapes.Addpicture();
	   			word.ActiveWindow.ActivePane.View.SeekView = 0;
	   	 	}
	   	 	
	   	 	if(foot.length>0) {
	   			var footer = document.createElement();
	   			footer.innerHTML = '<div style="width: 600px; text-align: left; overflow: hidden;"><div style="height: 1023px; overflow: hidden;"><span id="sectionTable1"><div class="FooterPage" style="bottom: 0px; position: absolute;">' + foot[0].innerHTML + '</div></span></div>';
	   			root.appendChild(footer);
	   			//cleanWhitespace( document.body );
	   	 		sel.moveToElementText(footer);
	   	 		sel.select();
	   	 		sel.execCommand('Copy');
	   	 		sel.moveEnd('character');  //������䵼�����ˣ��������Ϊcharacter������copy
	   	 		root.removeChild(footer);
	   	 		word.ActiveWindow.ActivePane.View.SeekView = 10;  
	   	 		//����ҳ�ţ�����ҳ �Ҷ��룬ż��ҳ ����룩 
	   	 	 	selection.Sections(1).Footers(1).Range.Font.Size = 10.5;
	   	 		selection.Sections(1).Footers(1).Range.Collapse();
	   			selection.Sections(1).Footers(1).Range.Paste();
	   			var paragraphs = selection.Sections(1).Footers(1).Range.Paragraphs;
	   			paragraphs.Item(paragraphs.Count-1).SpaceBefore = 0;
	   			paragraphs.Item(paragraphs.Count-1).SpaceAfter = 0;
	   			paragraphs.Item(paragraphs.Count-1).LineUnitAfter = 0;
	   			paragraphs.Item(paragraphs.Count-1).KeepWithNext = true;
	   			paragraphs.Item(paragraphs.Count-1).PageBreakBefore = false;
	   			paragraphs.Item(paragraphs.Count).Range.Cut();
	   			
	   			//selection.Sections(1).Footers(1).Range.ParagraphFormat.SpaceAfter = 0;
	   	 		//selection.Sections(1).Footers(1).Range.ParagraphFormat.LineUnitAfter = 0;
	   	 	 	//selection.ParagraphFormat.Alignment = 2; 
	   	 	 	//selection.HomeKey(5);  
	   	 	 	//selection.Sections(1).Footers(1).Range.InsertBefore("�� "); 
	   	 	 	//selection.EndKey(5);  
	   	 	 	//selection.Sections(1).Footers(1).Range.InsertAfter(" ҳ");  
	   	 	 	word.ActiveWindow.ActivePane.View.SeekView = 0;
	   	 	}
	   	 	try {
	   	 		window.clipboardData.clearData('text'); //word���������ռ��а�����
	   	 		clipboardData = clipboardData || '';
	   	 		window.clipboardData.setData("text",clipboardData); 
		   	} catch (e) {
		        //IE��window.clipboardData.setData��getData�ᱨ
		        //"OpenClipboard Failed"���󣬵����ܸ��Ƴɹ���
		        //�����Ƕ�����ͬʱ����(��ȡ/����/���)����������
		        //alert(e.message);
		    } 
	   	 	word.ActiveWindow.ActivePane.View.Type=3;//�������ģʽ:1--Ϊ��ͨ��ͼ��2---�����ͼ��3--ҳ����ͼ�� 4---web��ͼԤ��ģʽ��5---�����ͼ��6---web��ͼ��7---�Ķ���ʽ
	   	 	//document.appendChild(root);
	   	 	window.location.reload();
	   	 	//����Word�Ĵ�ӡ����
	   	 	//word.application.printout();  
	   	 	//�ر�word�ĵ�����,����0�����������ĵ�
	   		//word.application.activedocument.close(0); 
	   	 	showSplish(false);
	   	 	wordName = wordName + fileType
	   	 	if(fileType=='.doc') {//����word�ĵ�Ϊtest.doc 
		   	 	doc.SaveAs(wordName,0); //��ŵ�ָ����λ��ע��·��һ��Ҫ�ǡ�\\����Ȼ�ᱨ��
		   	 	//word.Application.Visible = true; //�����˼�����в�����Ϻ�����ʾ���������д�����棬�ᷢ��word�򿪺�ʲô��ǩ�������ݰ��͸����ֻ�һ����������
		   	 	//word.ActiveWindow.WindowState = true;
	   	 	} else if(fileType=='.pdf') {
	   	 		doc.SaveAs(wordName, 17); //��ŵ�ָ����λ��ע��·��һ��Ҫ�ǡ�\\����Ȼ�ᱨ��
	   	 		
	   	 	}
	   	 	for(i=word.Documents.Count;i>0;i--){ //�ر����д򿪵�WORD�ĵ�
	   			word.Documents(i).Close(0);
	   		}
	   		word.Application.quit(); //�˳�Word 
	   	 	//word.quit();
	   	 	word = null; 
	   	 	console.log('export success!!\tthe file is saved at ' + wordName);
	   	 	try{ 
         		var obj=new ActiveXObject("WScript.Shell"); 
         		if(obj){ 
             		obj.Run("\""+wordName+"\"", 1, false );
             		//obj.Run('"'+filename+'"'); 
             		obj=null; 
	            } 
	        }catch(e){ 
	        	console.log("cannot open the file, please check it!"); 
	        } 
	   	 	
	   	 	return wordName;
	   	}  

	 function showEditBox(id){
		 jQuery("#text_"+id).hide();
		// jQuery("#id_"+id).val(jQuery("#id_"+id).val().replace(/\,/g,''));
		 jQuery("#id_"+id).show().focus();	
		 } 	
	 
		 
	 function saveFileBox(id){
		 var val = jQuery("#id_"+id).val();
		 var text = jQuery("#text_"+id).html();
		 var formval ;
		 if(val == "" ){
			 jQuery("#text_"+id).html('-');
			 jQuery("#id_"+id).val('-');
		 }else{
		 if(!isNaN(val) && val!=null ){
			// alert("�Ǹ�����");
			 var num = val.split('.');
			 var numInteger = num[0];
			 var numDecimal = num.length > 1 ? '.' + num[1] : '';
			 formval = numInteger.split('').reverse().join('').replace(/(\d{3})/g,'$1,').replace(/\,$/,'').split('').reverse().join('');
			 formval += numDecimal;
			jQuery("#text_"+id).html(formval); 
		 }else{
			 // alert("������");
			 formval=jQuery("#id_"+id).val();
			 jQuery("#text_"+id).html(formval);
		 }; 
		 };
		 var str = id.split("_");
		 var sectionid = str[0];	 
		 var tid = str[1];
		 //val��text�����ж��ŵģ�ֻ�д�ҳ�������Ĳ���ִ��update 
		//	 alert(text);
		 if(jQuery("#id_"+id).val() != text){
			// alert("��Ȳ�");
			 var result1 = selectFromSQLJson("","select section_type from teflow_report_section where report_system_id="+<%=report.getReportSystemId()%>+" and section_id='"+sectionid+"'");
			 if(result1.length>0){
				 if(result1[0].SECTION_TYPE == '09' || result1[0].SECTION_TYPE == '0A' || result1[0].SECTION_TYPE == '0B'){
					 selectSimpleDataFromSQL("update teflow_report_<%=report.getReportSystemId()%>_"+sectionid+" set VarValue='"+jQuery("#id_"+id).val()+"' where id="+tid);
				 }else if(result1[0].SECTION_TYPE == '06'){
					 selectSimpleDataFromSQL("update teflow_report_<%=report.getReportSystemId()%>_"+sectionid+" set itemvalue='"+jQuery("#id_"+id).val()+"' where id="+tid);
				 }else if(result1[0].SECTION_TYPE == '02' || result1[0].SECTION_TYPE == '03'){
					 var str1 = id.split(",");
					 tid = str1[1];
					 selectSimpleDataFromSQL("update teflow_report_<%=report.getReportSystemId()%>_"+sectionid+" set "+tid+"='"+jQuery("#id_"+id).val()+"' where request_no='<%=requestNo%>'");
				 }
				jQuery("#text_"+id).html(formval);
			 }else{
               alert("error!!");
			}
		 };
		 			 	 		
		jQuery("#text_"+id).show();
		jQuery("#id_"+id).hide();
		
	 }  
	   	  
	</script>
	<style media=print>.Noprint{display:none;} 
	
	</style> 

<style>
.Wpage{
height:950px;overflow:hidden;
}

<%
out.println(css);
%>

</style>
</head>
<body onkeydown="checkesc(event)">

	 <input type="hidden" name="textareaLimitLength">
	 <%if(printFlag!=null && !"".equals(printFlag)){%>
		  <OBJECT id="WebBrowser" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" width="0"> 
		  </OBJECT> 
	 <%}%>
	 <input name="theHistoryRecord" type="hidden" value="">   
	 <form name="myReport" method="post" >
		  <input type='hidden' name='requestUrl' value='<%=request.getContextPath()%>'>
		  <input type='hidden' name='reportSystemId' value='<%=report.getReportSystemId()%>'>
		  <input type="hidden" name="curreportId" value="<%=report.getReportId()%>">
		  <input type="hidden" name="updateSections" value='<%=updateSections==null?"":updateSections%>'>
		  <input type="hidden" name="newSectionFields" value='<%=newSectionFields==null?"":newSectionFields%>'>
		  <input type="hidden" name="requestNo" value="<%=requestNo%>">
		  <input type="hidden" name="operateType" value="<%=operateType%>">
		  <input type="hidden" name="currentNodeId" value="<%=currentNodeId%>">
		  <input type="hidden" name="ccStaffCode">
		  <input type="hidden" name="showCCFlag" value="<%=showCClist%>">
		  <input type="hidden" name="requestStaffCode" value="<%=requestStaffCode%>">
		  <input type="hidden" name="updateFieldsVal" value="">
		  <input type="hidden" name="newFieldsVal" value="">
		  <input type="hidden" name="handleType" value="<%=handleType%>">
	  
	  <% 
	    if("00".equals(status) || "03".equals(status)){
	  %>
	    <input type="hidden" name="saveType" value="update">
	  <%
	   }
	  %>
	  
	 <div id="thedetailtableDIV" style='text-align:center;margin-top:10px;font:normal normal 12px Arial, Verdana, "����"'>
	  <!-- table width="100%"  border="0" cellpadding="3" cellspacing="0" bordercolor="#6595D6" style="border-collapse:collapse;" > -->
	  <table>
	  <tr>
	      <td colspan='6' align='center'>
	      
	      <input type="button" name="exportBtn" value='<i18n:message key="button.export_pdf"/>' onclick='javascript:ExportAllToWord("^newPage","RP02",".pdf")'' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
	onmouseout="this.className='btn3_mouseout'"
	onmousedown="this.className='btn3_mousedown'"
	onmouseup="this.className='btn3_mouseup'">
	
	
	<input type="button" name="exportBtn" value='ExportWord' onclick='javascript:ExportAllToWord("^newPage","RP02")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
	onmouseout="this.className='btn3_mouseout'"
	onmousedown="this.className='btn3_mousedown'"
	onmouseup="this.className='btn3_mouseup'">
	
	
	<input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick='javascript:window.close();' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
	        onmouseout="this.className='btn3_mouseout'"
	        onmousedown="this.className='btn3_mousedown'"
	        onmouseup="this.className='btn3_mouseup'">
	    
	
	
	      </td>
	  </tr>
	  <tr>
	    <td colspan='2'>&nbsp;&nbsp;</td>
	  </tr>
	  </table>
	  <div style="text-align:left;overflow:hidden;width:<%if("02".equals(report.getDisplayType())){ %>1023px<%}else{ %>700px<%} %>">
	  <%=pageUtil.displayReportWithContent(request)%> 
	  </div> 
	  <table>
	  <tr>
	    <td colspan='2'>&nbsp;&nbsp;</td>
	  </tr>
	  
  


	 <%if(pageUtil.getIsExceptionalCase()==true){%>
	  <tr>
	   <td colspan='6' align='center'>
	     <input type="hidden" name="isExceptionalCase" value="true">
	     <center class="Noprint" > 
	     <img src="<%=request.getContextPath()%>/images/reminder_03.gif">&nbsp;&nbsp;<font color='red'><b>Please note: This report has the exceptional item!</b></font>
	     </center>
	   </td>
	  </tr>
	  <%}else{%>
	   <input type="hidden" name="isExceptionalCase" value="false">
	  <%}%>
	  <tr>
	      <td colspan='6' align='center'>
	      
	      <input type="button" name="exportBtn" value='<i18n:message key="button.export_pdf"/>' onclick='javascript:ExportAllToWord("^newPage","RP02",".pdf")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
	onmouseout="this.className='btn3_mouseout'"
	onmousedown="this.className='btn3_mousedown'"
	onmouseup="this.className='btn3_mouseup'">
	
	
	<input type="button" name="exportBtn" value='ExportWord' onclick='javascript:ExportAllToWord("^newPage","RP02")' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
	onmouseout="this.className='btn3_mouseout'"
	onmousedown="this.className='btn3_mousedown'"
	onmouseup="this.className='btn3_mouseup'">
	
	
	<input type="button" name="closeBtn" value='<i18n:message key="button.close"/>' onclick='javascript:window.close();' class=btn3_mouseout onmouseover="this.className='btn3_mouseover'"
	        onmouseout="this.className='btn3_mouseout'"
	        onmousedown="this.className='btn3_mousedown'"
	        onmouseup="this.className='btn3_mouseup'">
	    
	
	
	      </td>
	  </tr>
	         <% if("1".equals(request.getSession().getAttribute("nonIE"))){ %>
	    <tr id="auditTR" >
	         <td width="96%"><IFRAME frameBorder="0" name="audit" id="audit" scrolling="yes" src=""
	            style="display:hidden  HEIGHT: 100px; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2"></IFRAME></td>
	    </tr>
	    <%} %>
	   </table>
	   
	    </div>
		
	</form>
	
</body>
</html>