(function() {
  if (!window.xyTree)
    window.xyTree = {};
})();

xyTree.TreeConfigNormal = {
  rootIcon        : ' js/xtree/images/foldericon.gif',
  openRootIcon    : ' js/xtree/images/openfoldericon.gif',
  folderIcon      : ' js/xtree/images/foldericon.gif',
  openFolderIcon  : ' js/xtree/images/openfoldericon.gif',
  fileIcon        : ' js/xtree/images/file.gif',
  iIcon           : ' js/xtree/images/I.gif',
  lIcon           : ' js/xtree/images/L.gif',
  lMinusIcon      : ' js/xtree/images/Lminus.gif',
  lPlusIcon       : ' js/xtree/images/Lplus.gif',
  tIcon           : ' js/xtree/images/T.gif',
  tMinusIcon      : ' js/xtree/images/Tminus.gif',
  tPlusIcon       : ' js/xtree/images/Tplus.gif',
  blankIcon       : ' js/xtree/images/blank.gif',
  defaultText     : 'Tree Item',
  defaultAction   : 'javascript:void(0);',
  defaultBehavior : 'classic',
  usePersistence	: true,
  jianju1         : '4px'
};
//????????????????2007/06/15
xyTree.defaultNodeClickAction ="xyTree.defaultNodeClickAction";//????????



//div????
//=====================================================================
xyTree.DivTreeNormal = function(name,img) {
  this.img;
  if(img) 
    this.img = img;
  
  var objectname = this.getName();
  this.tree = new xyTree.TreeNormal(name, objectname);
  this.tree.divtree = this;
  this.div = this.creatediv();
}

xyTree.DivTreeNormal.count = 0;

xyTree.DivTreeNormal.prototype.getName = function() {
  var s = "xytreenormalid" ;
  s += (window.xyTree.DivTreeNormal.count++);
  return s;
}


xyTree.DivTreeNormal.prototype.add = function(node) {
  this.tree.add(node);	
}

xyTree.DivTreeNormal.prototype.init = function(funClickNode, funClickRootNode) {
  var div = this.div.lastChild;
  //??????????????????????
  //??????????????????????
  var root = this.tree.root;
  var arr = root.child;
  for(var i = 0; i < arr.length; i++ )
    div.appendChild(arr[i].innerhtml());
  
  this.clickNode = funClickNode ? funClickNode : this.defaultClickNode;
  this.clickRootNode = funClickRootNode ? funClickRootNode : this.defaultClickRootNode;
}

xyTree.DivTreeNormal.prototype.creatediv = function (){
  var divtree = this;
  var div = document.createElement('div');
  div.className = 'treeyangshi';
  
  var divhead = document.createElement('div');
  var img = document.createElement('img');
  
  if (!this.img) {
    img.src = xyTree.TreeConfigNormal.openRootIcon;
    img.onclick = function() {
      var divbody = this.parentNode.parentNode.lastChild;
      if (divbody.style.display == 'block') {
        divbody.style.display = 'none';
        img.src = xyTree.TreeConfigNormal.rootIcon;
      } else {
        divbody.style.display = 'block';
        img.src = xyTree.TreeConfigNormal.openRootIcon;
      }
    }
  } else {
    img.src = this.img;
    img.className = 'treeyangshiImg';
    img.onclick = function() {
      var divbody = this.parentNode.parentNode.lastChild;
      if (divbody.style.display == 'block') {
        divbody.style.display = 'none';
        //img.src = xyTree.TreeConfigNormal.rootIcon;
      } else {
        divbody.style.display = 'block';
        //img.src = xyTree.TreeConfigNormal.openRootIcon;
      }
    }
  }
  
  img.align = "absbottom";
  divhead.appendChild(img);

  var qj = this.tree.objectname; //??????????????????
  var a = document.createElement('a');
  a.href = 'javascript:void(0);';
  a.onclick = function() {
    divtree.clickRootNode();
  };
  a.onfocus = function() {this.blur();}
  a.appendChild(document.createTextNode(this.tree.treename));
  a.style.marginLeft = xyTree.TreeConfigNormal.jianju1;
  
  divhead.appendChild(a);
  div.appendChild(divhead);

  var divbody = document.createElement('div');
  divbody.style.display = 'block';
  div.appendChild(divbody);
  
  return div;
}



xyTree.DivTreeNormal.prototype.hideTreeBody = function() {
  this.div.lastChild.style.display = 'none';
  if (!this.img) {
    this.div.firstChild.firstChild.src = xyTree.TreeConfigNormal.rootIcon;
  }	  
}

xyTree.DivTreeNormal.prototype.showTreeBody = function() {
  this.div.lastChild.style.display = 'block';
  if (!this.img) {
    this.div.firstChild.firstChild.src = xyTree.TreeConfigNormal.openRootIcon;
  }	  
}


/**
 * ??????????????????????????????????????
 * @param {xyTree.Node} node ????????????
 */
xyTree.DivTreeNormal.prototype.defaultClickNode = function(node) {
  node.getHtmlElementFoldImg().onclick();
}

/**
 * ??????????????????????????????????????????????
 */
xyTree.DivTreeNormal.prototype.defaultClickRootNode = function() {
  this.div.firstChild.firstChild.onclick();
}
