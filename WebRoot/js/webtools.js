var dragapproved=false
var eventsource,x,y
choiceObj=null;
function move()
 {
  if (event.button==1&&dragapproved) //??????????????
   {
    eventsource.style.pixelLeft=temp1+event.clientX-x
    eventsource.style.pixelTop=temp2+event.clientY-y
    return false
   }
 }
function drags()
 {
  if (!document.all)
  return
  if (event.srcElement.className=="drag") //???????????????
   {
    dragapproved=true
    eventsource=event.srcElement
    temp1=eventsource.style.pixelLeft
    temp2=eventsource.style.pixelTop
    x=event.clientX
    y=event.clientY
    document.onmousemove=move
   }
 }
document.onmousedown=drags   //???????,????
document.onmouseup=new Function("dragapproved=false")//???????,????

function create()
{
  for(i=0;i<document.all.mychoice.length;i++)
    if(document.all.mychoice[i].checked)
    {
      mytype=document.all.mychoice[i].value;
      var rObj = createControlInfo(mytype);
      if(rObj==null) return;
      alert(rObj.fieldId + ":" + rObj.required)
      if(mytype=="label"){
         document.all.myadd.innerHTML+="<label class=drag onselectstart='choiceObj=this'>label</label>";
      }else{
         var innerStr = "<label class=drag onselectstart='choiceObj=this'>"+rObj.fieldId+":</label><input type="+mytype+" class=drag onfocus='choiceObj=this' name='"+rObj.fieldId+"' isRequired='"+rObj.required+"'";
         if(mytype=="button"){
           innerStr = innerStr + " value='"+rObj.labelValue+"'";
         }
         innerStr = innerStr + ">";
         document.all.myadd.innerHTML+= innerStr;
      }
      break;
    }
}

function createControlInfo(controlType){
    var obj = new Object();
    obj.type = controlType;
    var returnObj = window.showModalDialog("editControlInfo.jsp?type="+controlType,obj,"dialogWidth:385px;status:no;dialogHeight:300px"); 
    return returnObj;
}

function remove()
{
newinnerHTML="";
for(i=0;i<document.all.myadd.childNodes.length;i++)
{
if(document.all.myadd.childNodes(i)!=choiceObj)
newinnerHTML+=document.all.myadd.childNodes(i).outerHTML;
}
document.all.myadd.innerHTML=newinnerHTML;
}
function getinfo()
{
realwidth=0;
for(i=0;i<document.all.myadd.childNodes.length;i++)
{
  tt=document.all.myadd.childNodes(i)
  var ttop  = tt.offsetTop;     //TT???????
  var thei  = tt.clientHeight;  //TT??????
  var tleft = tt.offsetLeft;    //TT???????
  var ttyp  = tt.type;          //TT?????
  while (tt = tt.offsetParent){ttop+=tt.offsetTop; tleft+=tt.offsetLeft;}
alert("x="+tleft+";y="+ttop);
}
}
