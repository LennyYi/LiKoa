var xmlHttp;
var completeDiv;
var inputField;
var summaryTable;
var summaryTableBody;

function createXMLHttpRequest(){
	if(window.ActiveXObject){
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	}else if(window.XMLHttpRequest){
		xmlHttp = new XMLHttpRequest();
	}		
	return xmlHttp;
}


 function handleStateChange(){
     if(xmlhttp.readyState == 4) {
        if(xmlhttp.status == 200) {
          updateList();
        }
     }
  }
  function updateList(){
    var select;
    select = document.getElementById("formSystemId");
    select.value = "";
    clearSelect(select);
    //alert(xmlhttp.responseXML.xml)
    var options = xmlhttp.responseXML.getElementsByTagName("option");
    if(options.length>0){
      select.appendChild(createEmptyElement(""));
    }
    for (var i = 0, n = options.length; i < n; i++) {
        select.appendChild(createElementWithValue(options[i],"id"));
    }
  }
  
  function createElementWithValue(text,name) {
      var element = document.createElement("option");
      element.setAttribute("value", text.getAttribute(name));
      var text = document.createTextNode(text.firstChild.nodeValue);
      element.appendChild(text);
      return element;
  }
  
  function createOptionEle(option_text, option_value, isChecked){
      var element = document.createElement("option");
      element.setAttribute("value", option_value);
      if(isChecked){
    	  element.selected = true;
      }      
      var text = document.createTextNode(option_text);
      element.appendChild(text);
      return element;  
  }
  
  function createElementWithValue(text,name, isChecked) {
      var element = document.createElement("option");
      element.setAttribute("value", text.getAttribute(name));
      if(isChecked){
    	  element.selected = true;
      }
      var text = document.createTextNode(text.firstChild.nodeValue);
      element.appendChild(text);
      return element;
  }
  
  function createEmptyElement(value){
      var element = document.createElement("option");
      element.setAttribute("value",value);
      var text = document.createTextNode("  ");
      element.appendChild(text);
      return element;
  }
  
function createOptionElement(item, valueTagName, labelTagName) {
	var valueNode = item.getElementsByTagName(valueTagName)[0].childNodes[0];
	var value = valueNode == null ? "" : valueNode.nodeValue;
	
	var labelNode = item.getElementsByTagName(labelTagName)[0].childNodes[0];
	var label = labelNode == null ? "" : labelNode.nodeValue;
	
	var element = document.createElement("option");
	element.setAttribute("value", value);
	element.appendChild(document.createTextNode(label));
	return element;
}
  
  function clearSelect(obj){
    var count = obj.options.length;
    for(var i=0;i<count;i++){
      obj.options.remove(0);
    }
    obj.style.width = "";
    obj.style.width = "auto";
  }
  
  
  function handleStateChangeStaff(){
     if(xmlhttp.readyState == 4) {
        if(xmlhttp.status == 200) {
          updateListStaff();
        }
     }
  }
  function updateListStaff() {
    var select;
    select = document.getElementById("request_staff_code");
    clearSelect(select);
    var options = xmlhttp.responseXML.getElementsByTagName("staff");
    for (var i = 0, n = options.length; i < n; i++) {
        select.appendChild(createElementWithValue(options[i],"code"));
    }
    select.fireEvent("onchange");
  }
  
  function updateListTeam(xmlData){
    var select;
    select = document.getElementById("team_code");
    clearSelect(select);
    //var options = xmlhttp.responseXML.getElementsByTagName("team");
    select.appendChild(createEmptyElement(""));
    var options = xmlData.getElementsByTagName("team");
    for (var i = 0, n = options.length; i < n; i++) {
        select.appendChild(createElementWithValue(options[i],"code"));
    }
  }
  
  function updateStaffList(xmlData){
    var select;
    select = document.getElementById("requestedBy");
    clearSelect(select);
    select.appendChild(createEmptyElement(""));
    var options = xmlData.getElementsByTagName("staff");
    for (var i = 0, n = options.length; i < n; i++) {
        select.appendChild(createElementWithValue(options[i],"code"));
    }
  }
  