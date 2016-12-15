function StringBuffer(){
     if(arguments[0]) this.data = [arguments[0]];
     else
        this.data = [];
}

StringBuffer.prototype.append = function(){this.data.push(arguments[0]);return this};
StringBuffer.prototype.toString = function(){return this.data.join(arguments[0]||"")};
StringBuffer.prototype.length = function(){return this.data.length};
StringBuffer.prototype.clear = function(){this.data.length=0; return this;};


//String
String.prototype.trim = function(){
  return this.replace(/(^\s*)|(\s*$)/g, ""); 
}

String.prototype.ltrim = function(){
  
}

String.prototype.hasSubStrInArr = function(){
			for(var i=0; i<arguments[0].length; i++){
				if(this.hasSubString(arguments[0][i])){return true;}
			}
			return false;
		};

String.prototype.hasSubString = function(s,f){
		if(!f) f="";
			return (f+this+f).indexOf(f+s+f)==-1?false:true;
};

String.prototype.cint = function(){ //????????????????????????
		    return this.replace(/\D/g,"")*1;
};



/** 
 * ��������� Ruby �ˡ���Ҫ����������
 * 1.  ����� document.getElementById(id) ����򻯵��á� 
 * ���磺$("aaa") �������� aaa ���� 
 * 2.  �õ��������� 
 * ����: $("aaa","bbb") ����һ������idΪ"aaa"��"bbb"����input�ؼ���������顣 
 */ 
function $() { 
  var elements = new Array(); 
  if(!arguments[0]){return null;}
  for (var i = 0; i < arguments.length; i++) { 
    var element = arguments[i]; 
    if (typeof element == 'string') 
      element = document.getElementById(element); 
      $._Method.Element.apply(element);
      
    if (arguments.length == 1) 
      return element; 
      
    elements.push(element); 
  } 
  return elements; 
} 

$.Form = function(n){
	var f = typeof n =="string" ? document.forms[n] : n;
	$.Form._Method.apply(f);
	return f;
}

$.Form._Method = function(){
	this.serialize = function(obj){
			var arr = this.elements;
			var elem = {};
			for(var i=0,j; j=arr[i]; i++){
				if(j.disabled || !j.name){continue;}
				if(j.type && j.type.toLowerCase().hasSubStrInArr(["radio","checkbox"]) && !j.checked){continue;}
				var na = j.name.toLowerCase();
				//var na = j.name;
				if(typeof elem[na] == "undefined"){
					elem[na] = [];
				}
				elem[na].push($E(j.value));
				//elem[na].push(j.value);
			}
			return $.Form.serialize(obj,elem);
		};
}

$.Form.serialize = function(obj){
	var elem = arguments[1] || {};
	for(var key in obj){
		var na = key.toLowerCase();
		//var na = key;
		if(typeof elem[na] == "undefined"){
			elem[na] = [];
		}
		elem[na].push($E(obj[key]));
		//elem[na].push(obj[key]);
	}

	var para = new StringBuffer("");
	for(var name in elem){
		for(var i=0; i<elem[name].length; i++){
			para.append(name+"="+elem[name][i]);
		}
	}
	return para.toString("&");
};

$._Method = {
	Element	: function(){
		this.hide = function(){this.style.display="none"; return this};
		this.show = function(){this.style.display=""; return this};
	},
	Function : function(){
		this.bind = function() {
  			var __method = this, args = $A(arguments), object = args.shift();
  			return function() {
    			return __method.apply(object, args.concat($A(arguments)));
  			}
		}
     }
}

function $A(list){
	var arr = [];
	for (var i=0,len=list.length; i<len; i++){
		arr[i] = list[i];
	}
	return arr;
};
function $D(str){return decodeURIComponent(str);};
function $E(str){return encodeURIComponent(str);};
function $V(id){return $(id)?($(id).tagName.hasSubStrInArr(["INPUT","TEXTAREA","SELECT","BUTTON"])?$(id).value : $(id).innerHTML):"";};

$._Method.Function.apply(Function.prototype);


var Ajax={
	xmlhttp:function (){
		var obj = null;	
		try{
			obj = new ActiveXObject('Msxml2.XMLHTTP');
		}catch(e){
			try{
				obj = new ActiveXObject('Microsoft.XMLHTTP');
			}catch(e){
				obj = new XMLHttpRequest();
			}
		}
		return Ajax.xmlObjCache = obj;
	},xmlObjCache:null
};		

/**
����ֵ����ǰʹ�õ� xmlhttp ����
Ajax.Request(
url,  //��ѡ����ݷ��͵�Ŀ���ַ�� 
{
method:method,      //��ѡ������ύ�ķ�ʽ��Ĭ��ֵΪget�����õĻ���post
parameters:para,    //�� method Ϊ get ʱ�ǿ�ѡ�Ϊ post ʱ�Ǳ�ѡ����͵����ݣ�����ʽΪ�� name1=valeu1& name2=value2&name3=value3...... 
postBody:xmlString, //��ѡ��ͻ��˷��͵� xml ��ʽ�ַ������������ postBody����ô parameters �������ԡ�
asynchronous:true,  //��ѡ�ָ�������Ƿ��첽��Ĭ��Ϊtrue���첽���� 
setRequestHeader:Object, //ָ�������ͷ���ִ�����ֵ����Ϊ������ֵ�ԡ���ʽ�Ķ��󣬱��磺{"If-Modified-Since":"0", "SOAPAction":"http://tempuri.org/SBS_WebService", ... ... } 
onComplete:completeFun,  //��ѡ�����ɹ�ʱִ�еĻص��������ú���Ĭ�ϰѵ�ǰʹ�� xmlhttp ������Ϊ��һ�������� 
onError:errorFun         //��ѡ������쳣ʱִ�еĻص��������ú���Ĭ�ϰѵ�ǰʹ�� xmlhttp ������Ϊ��һ�������� 
}
)
**/
Ajax.Request=function (){
	if(arguments.length<2)return ; //�����������Ϊ2��
	var para = {asynchronous:true,method:"GET",parameters:""};
	for (var key in arguments[1]){
		para[key] = arguments[1][key];
	}
	var _x= Ajax.xmlhttp(); //Ajax.xmlObjCache || 
	var _url=arguments[0];
	if(para["parameters"].length>0) para["parameters"]+='&_=';
	if(para["method"].toUpperCase()=="GET") _url+=(_url.match(/\?/)?'&':'?')+para["parameters"];
	_x.open(para["method"].toUpperCase(),_url,para["asynchronous"]);
	_x.onreadystatechange=Ajax.onStateChange.bind(_x,para);
	if(para["method"].toUpperCase()=="POST")_x.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	for (var ReqHeader in para["setRequestHeader"]){
		_x.setRequestHeader(ReqHeader,para["setRequestHeader"][ReqHeader]);
	}
	_x.send(para["method"].toUpperCase()=="POST"?(para["postBody"]?para["postBody"]:para["parameters"]):null);
	return _x;
};

Ajax.onStateChange = function(para){
	if(this.readyState==4){
		if(this.status==200)
			para["onComplete"]?para["onComplete"](this):"";
		else{
			para["onError"]?para["onError"](this):"";
		}
	}
};

//������Ajax.Request�Ŀ�ݵ��÷�ʽ
$.Ajax = {
	Request : function(url,_method,para,complete,error){return Ajax.Request(url,{method:_method||"get",parameters:para||"",onComplete:complete,onError:error});},
	get	: function(url,complete,error){ return $.Ajax.Request(url+(url.indexOf("?")==-1?"?":"&")+Math.random(),"get","",complete,error); },
	post : function(url,para,complete,error){ return $.Ajax.Request(url,"post",para,complete,error);},
	update : function(url,id){ return $.Ajax.Request(url,(arguments[2]?"post":"get"),(arguments[2]?arguments[2]:Math.random()),function(x){if("INPUT,SELECT,BUTTON,TEXTAREA".hasSubString($(id).tagName,",")){$(id).value=x.responseText;}else{$(id).innerHTML=x.responseText;}});}
}