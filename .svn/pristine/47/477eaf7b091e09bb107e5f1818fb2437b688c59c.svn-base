/*============================
	Author : fason 阿信
	Email		: fason_pfx@hotmail.com
============================*/

var Icon = {
	root :	"root.gif",
	folderopen :  "folderopen.gif",
	folderclosed :  "folderclosed.gif",
	Rminus:	  "Rminus.gif",
	Rplus:	 "Rplus.gif",
	minusbottom:	   "Lminus.gif",
	plusbottom:	  "Lplus.gif",
	minus:	 "minus.gif",
	plus:	 "plus.gif",
	join:	 "T.gif",
	joinbottom:	 "L.gif",
	blank:	 "blank.gif",
	line:	 "I.gif"
}

window.TV = [];
function TreeView()
{
	this.id = window.TV.length;
	window.TV[this.id] = this;
	this.target = "_self";
	this.checkbox = false;
	
	//修改人：王志雄,增加了两个参数。一个是checkbox的值，令一个是是否有checkbox显示
	this.Nodes ={ 0 : { ID : 0, ParentID : -1, Text : null, Href : null, Image : null, values : null, haveChecked :false, childNodes: new Array() } };
}
var tv = TreeView.prototype;
tv.setTarget = function(v) {
	this.target = v;
}
tv.setCheckbox = function(v) {
	this.checkbox = v;
}
tv.setName = function(v) {
	this.name = v;
}
tv.setImagePath = function(sPath) {
	for(o in Icon){
		tmp = sPath + Icon[o];
		Icon[o] = new Image();
		Icon[o].src = tmp;
	}
}

//修改人：王志雄,增加了两个参数。一个是checkbox的值，令一个是是否有checkbox显示
tv.add = function(iD,ParentiD,sText,sHref,sImage,sCheckboxname,sValue,bHaveChecked) {
	this.Nodes[iD] = { ID : iD, ParentID : ParentiD, Text : sText, Href : sHref, Image : sImage , childNodes: new Array() , open : false , checkboxname :sCheckboxname ,checked : false ,values : sValue, haveChecked : bHaveChecked};
	var ch = this.getNode(ParentiD).childNodes;
	ch[ch.length] = this.Nodes[iD];
};

tv.addcheck = function(iD,ParentiD,sText,sHref,sImage,sCheckboxname,sValue,bHaveChecked) {
	this.Nodes[iD] = { ID : iD, ParentID : ParentiD, Text : sText, Href : sHref, Image : sImage , childNodes: new Array() , open : false , checkboxname :sCheckboxname ,checked : true ,values : sValue, haveChecked : bHaveChecked};
	var ch = this.getNode(ParentiD).childNodes;
	ch[ch.length] = this.Nodes[iD];
};

tv.checkoriginal = function(ID){
	var node = this.getNode(ID);
	var ch = node.childNodes;

	if (ch.length > 0) {
		for (var i =0 ; i<ch.length;i++){
			this.checkoriginal(ch[i].ID);
		}
		
	} 	
		
	if (node.haveChecked && node.checked) {
		document.getElementById("checkbox" + ID).checked = true;
		this.oncheck(ID);
	}
	
	
	
};

tv.getNode = function(sKey) {
	if (typeof this.Nodes[sKey] != "undefined")
	{
		return this.Nodes[sKey];
	}
	return null;
};
tv.getParentNode = function(ID) {
	var key = this.getNode(ID).ParentID;
	if(this.getNode(key) == null) return null;
	return this.getNode(key);
}
tv.hasChildNodes = function(sKey) {
	return this.getNode(sKey).childNodes.length > 0;
};
tv.isLastNode = function(sKey) {
	var node = this.getNode(sKey);
	var par = this.getParentNode(sKey);
	if(par == null) node.isLast = true;
	else if (typeof node["isLast"] == "undefined") {
		for(var i = 0;i<par.childNodes.length;i++)
			if(par.childNodes[i].ID == sKey) break;
		node.isLast =  (i == par.childNodes.length-1)
	}
	return node.isLast;
};
tv.getRoot = function(ID) {
	var par = this.getParentNode(ID);
	if (this.getNode(ID).ParentID == 0)
	{
		return this.getNode(ID);
	}
	else
	{
		return this.getRoot(par.ID);
	}
}
tv.drawNode = function(ID) {
	var html = "";
	var node = this.getNode(ID);
	var rootid = this.getRoot(ID).ID;
	var hc = this.hasChildNodes(ID);
	
	
	html += '<div class="TreeNode" nowrap>'+this.drawIndent(ID)+
				'<a id="node'+ID+'" class="Anchor" href="'+node.Href+'" target="'+this.target+'" onclick ="window.TV['+this.id+'].openFolder('+ID+')" ondblclick="window.TV['+this.id+'].openHandler('+ID+')"><img id="folder'+ID+'" src="'+( node.Image ? node.Image : Icon.folderclosed.src)+'" align="absmiddle">'+
				(node.haveChecked ? ('<input type=checkbox class=checkbox id="checkbox'+ID+'" value="'+node.values+'" name="'+node.checkboxname+'" onclick="window.TV['+this.id+'].oncheck('+ID+')">') : '')+
				'<span>'+ node.Text +'</span></a></div>\n'
	if (hc) {
		var io = ID ==  rootid;
		node.open = io;
		html += ('<div id="container'+ID+'" style="display:'+(io ? '' : 'none')+'">\n');
		html += this.addNode(ID);
		html += '</div>\n';
	}
	return html;
}

tv.addNode = function(ID) {
	var node = this.getNode(ID);
	var html = "";
	for(var i = 0;i<node.childNodes.length;i++)
		html += this.drawNode(node.childNodes[i].ID);
	return html;
}

tv.drawIndent = function(ID) {
	var s = ''
	var ir = this.getRoot(ID).ID == ID;
	var hc = this.hasChildNodes(ID);
	if(this.getParentNode(ID) != null)
		s += ((hc ? '<a href="javascript:void window.TV['+this.id+'].openHandler('+ID+');" target="_self">':'')+'<img id="handler'+ID+'" src="'+ (this.hasChildNodes(ID) ? (ir ? Icon.Rminus.src : (this.isLastNode(ID) ? Icon.plusbottom.src : Icon.plus.src)) : (ir ? Icon.blank.src : (this.isLastNode(ID) ? Icon.joinbottom.src : Icon.join.src))) + '" align="absmiddle">'+(hc?'</a>':''));
	var p = this.getParentNode(ID);
	while(p != null)
	{
		if(this.getParentNode(p.ID) == null)break;
		s = ('<img src="'+(this.isLastNode(p.ID) ? Icon.blank.src : Icon.line.src) + '" align="absmiddle">')+s;
		p = this.getParentNode(p.ID);
	}
	return s;
}
tv.setSelected = function(ID) {
	if(this.selectedID) { document.getElementById("node" + this.selectedID).className = "Anchor";}
	this.selectedID = ID;
	//alert("selected" + ID);
	document.getElementById("node" + ID).className = "selected";
}
tv.oncheck = function(ID) {
	var o = this.getNode(ID);
	var v = o.checked;
	
	o.checked = document.getElementById("checkbox" + ID).checked;	
	//alert("oncheck" + ID + o.checked);
	this.checkChildren(ID,o.checked);		
	this.checkParent(ID);
};

tv.check = function(ID,v){
	this.getNode(ID).checked = v;
	//alert("check" + ID);
	document.getElementById("checkbox" + ID).checked = v;
}
tv.checkChildren = function(ID,v){
	var ch = this.getNode(ID).childNodes;
	for(var i = 0;i<ch.length;i++){
		this.check(ch[i].ID,v);
		this.checkChildren(ch[i].ID,v);
	}
}
tv.checkParent = function(ID) {
	var par = this.getParentNode(ID);
	
	if(ID != this.getRoot(ID).ID){
		
		for(var j = 0;j<par.childNodes.length;j++) {
			var node = par.childNodes[j];
			//alert(node.haveChecked);
			//check if the node has checkbox
			if (node.haveChecked) {
				if(!par.childNodes[j].checked) break;
			}
		}
		//check if the node has checkbox
		if (par.haveChecked) {
			this.check(par.ID,(j == par.childNodes.length));			
		}
		this.checkParent(par.ID);
	}
	
}

tv.closeAll = function(ID){
	var node = this.getNode(ID);
	var ch = node.childNodes;
	if (ch.length > 0) {		
		for (var i =0 ; i<ch.length;i++){
			this.collapse(ch[i].ID);
		}		
	} 	
};

tv.openHandler = function(ID) {
	
	if (this.hasChildNodes(ID)) {
		var node = this.getNode(ID);
		if (node.open) {
			this.collapse(ID);
		}
		else {
			if (node.ParentID == 0)
				this.closeAll(0);
			this.expand(ID);
		}
	}
}
tv.expand = function(ID) {
	var handler = document.getElementById("handler"+ID);
	var container = document.getElementById("container"+ID);
	handler.src = this.getRoot(ID).ID == ID ? Icon.Rminus.src : ( this.getNode(ID).isLast ? Icon.minusbottom.src : Icon.minus.src);
	container.style.display = '';
	this.getNode(ID).open = true;
}
tv.collapse = function(ID) {
	var handler = document.getElementById("handler"+ID);
	var container = document.getElementById("container"+ID);
	handler.src = this.getRoot(ID).ID == ID ? Icon.Rplus.src : ( this.getNode(ID).isLast ? Icon.plusbottom.src : Icon.plus.src);
	if (container != null) {
	    container.style.display = 'none';
	}
	this.getNode(ID).open = false;
}
tv.openFolder = function(ID)
{
	if(this.selectedID){ 
		if(this.getNode(this.selectedID).Image == null) {document.getElementById("folder"+this.selectedID).src = Icon.folderclosed.src;}
	}
	var folder = document.getElementById("folder" + ID);
	if(this.getNode(ID).Image == null) folder.src = Icon.folderopen.src;
	this.setSelected(ID);
	this.selectedID = ID;
	folder.parentNode.blur();
}
tv.toString = function() {
	var html = this.addNode(0);
	return html;
}