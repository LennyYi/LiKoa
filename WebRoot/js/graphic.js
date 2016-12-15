document.write("<html xmlns:v><style>v\\:*{behavior:url(#default#VML)}</style>")
var ct1=new ct()
function ct(){
 this.ac="green,yellow,red,blue,gray".split(",")
 this.getCss=function(css,k,df){
  if(css==null)
   return def==null ? "" : df
  var r=new RegExp("(^|)"+k+":([^\;]*)(\;|$)","gi")
  var a=r.exec(css.replace(/=/g,":").replace(/ /g,"").toLowerCase())
  return a==null ? (df==null ? "" : df) : (isNaN(a[2])||a[2]=="" ? a[2] : parseInt(a[2]))
 }
 this.bar=function(vList,css){
  var l="",a,n,s,hsz,max=0,cx=20,ch
  var pw=this.getCss(css,"width",500),ph=this.getCss(css,"height",300)
  if(ph<150||pw<150){
   alert("????????????")
   return
  }
  a=vList.split(";")
  for(var i in a){
   a[i]=a[i].split(",")
   for(var j in a[i]){
    if(i>0&&j>0)
     if(parseInt(a[i][j])>max)
      max=parseInt(a[i][j])
   }
  }
  if(max==0)
   return
  hsz=(ph-100)/max
  n=(vList.length-vList.replace(/;/g,"").replace(/,/g,"").length)*20+20
  if(pw<n)
   pw=n
  l+="<v:roundrect fillcolor='"+this.getCss(css,"background","white")+"' style='position:absolute;left:0;top:0;width:"+pw+";height:"+ph+"'><v:shadow on=t type=emboss opacity=80% offset='3px,3px' offset2='5px,5px' /></v:roundrect>"
  l+="<v:line from="+30+","+(ph-30)+" to="+30+","+10+"><v:stroke startarrow=none endarrow=classic /></v:line>"
  l+="<v:line from="+30+","+(ph-30)+" to="+(pw-10)+","+(ph-30)+"><v:stroke startarrow=none endarrow=classic /></v:line>"
  l+="<span style='position:absolute;z-index:3;font:12;left:220;top:10'>"+this.getCss(css,"title")+"</span>"
  l+="<span style='position:absolute;z-index:3;font:12;left:"+(pw-50)+";top:"+(ph-20)+"'>"+a[0][0]+"</span>"
  l+="<span style='position:absolute;z-index:3;font:12;left:10;top:10;width:5;word-break:break-all'>"+this.getCss(css,"vname")+"</span>"
  for(i=0;i<5;i++){
   l+="<v:line from="+30+","+(70+i*(ph-100)/5)+" to="+(pw-10)+","+(70+i*(ph-100)/5)+" strokecolor=#c0c0c0><v:stroke dashstyle=dash /></v:line>"
   l+="<span style='position:absolute;z-index:3;font:12;left:"+10+";top:"+(65+i*(ph-100)/5)+"'>"+(parseInt(max)*(5-i)/5)+"</span>"
  }
  
  for(i in a){
   for(j in a[i]){
    if(i==0){
     if(j>0){
      l+="<v:roundrect fillcolor='"+this.ac[j-1]+"' style='position:absolute;left:"+(j*80-20)+";top:30;width:20;height:20' />"
      l+="<span style='position:absolute;z-index:3;font:12;left:"+(j*80+5)+";top:35'>"+a[i][j]+"</span>"
     }
    }
    else{
     if(j==0)
      l+="<span style='position:absolute;z-index:3;font:12;left:"+cx+";top:"+(ph-25)+"'>"+a[i][j]+"</span>"
     else{
      ch=a[i][j]*hsz
      l+="<v:roundrect title='"+a[i][j]+"' fillcolor='"+this.ac[j-1]+"' style='position:absolute;left:"+cx+";top:"+(ph-30-ch)+";width:20;height:"+ch+"' />"
      if(this.getCss(css,"showVal")=="t")
       l+="<span style='position:absolute;z-index:3;font:12;left:"+(cx+3)+";top:"+(ph-42-ch)+"'>"+a[i][j]+"</span>"
      cx+=20
     }
    }
   }
   cx+=20
  }
  return l
 }
 this.lines=function(vList,css){
  var l="",a,n,s,hsz,max=0,ch
  var pw=this.getCss(css,"width",500),ph=this.getCss(css,"height",300)
  if(ph<150||pw<150){
   alert("????????????")
   return
  }
  a=vList.split(";")
  for(var i in a){
   a[i]=a[i].split(",")
   for(var j in a[i]){
    if(i>0&&j>0)
     if(parseInt(a[i][j])>max)
      max=parseInt(a[i][j])
   }
  }
  if(max==0)
   return
  hsz=(ph-100)/max
  n=a.length*40+40
  if(pw<n)
   pw=n
  l+="<v:roundrect fillcolor='"+this.getCss(css,"background","white")+"' style='position:absolute;left:0;top:0;width:"+pw+";height:"+ph+"'><v:shadow on=t type=emboss opacity=80% offset='3px,3px' offset2='5px,5px' /></v:roundrect>"
  l+="<v:line from="+30+","+(ph-30)+" to="+30+","+10+"><v:stroke startarrow=none endarrow=classic /></v:line>"
  l+="<v:line from="+30+","+(ph-30)+" to="+(pw-10)+","+(ph-30)+"><v:stroke startarrow=none endarrow=classic /></v:line>"
  l+="<span style='position:absolute;z-index:3;font:12;left:"+(pw-50)+";top:"+(ph-20)+"'>"+this.getCss(css,"xname")+"</span>"
  l+="<span style='position:absolute;z-index:3;font:12;left:10;top:10;width:5;word-break:break-all'>"+a[0][0]+"</span>"
  l+="<span style='position:absolute;z-index:3;font:12;left:220;top:10'>"+this.getCss(css,"title")+"</span>"
  for(i=0;i<5;i++){
   l+="<v:line from="+30+","+(70+i*(ph-100)/5)+" to="+(pw-10)+","+(70+i*(ph-100)/5)+" strokecolor=#c0c0c0><v:stroke dashstyle=dash /></v:line>"
   l+="<span style='position:absolute;z-index:3;font:12;left:"+10+";top:"+(65+i*(ph-100)/5)+"'>"+(parseInt(max)*(5-i)/5)+"</span>"
  }
  
  for(i in a){
   for(j in a[i]){
    if(i==0){
     if(j>0){
      l+="<v:roundrect fillcolor='"+this.ac[j-1]+"' style='position:absolute;left:"+(j*80-20)+";top:30;width:20;height:20' />"
      l+="<span style='position:absolute;z-index:3;font:12;left:"+(j*80+5)+";top:35'>"+a[i][j]+"</span>"
     }
    }
    else{
     if(j==0)
      l+="<span style='position:absolute;z-index:3;font:12;left:"+(i*40)+";top:"+(ph-25)+"'>"+a[i][j]+"</span>"
     else{
      ch=a[i][j]*hsz
      if(i>1){
       oh=parseInt(a[i-1][j]*hsz)
       l+="<v:line from="+((i-1)*40)+","+(ph-oh-30)+" to="+(i*40)+","+(ph-ch-30)+" strokecolor='"+this.ac[j-1]+"' />"
      }
      l+="<v:roundrect title='"+a[i][j]+"' fillcolor='"+this.ac[j-1]+"' style='z-index:3;position:absolute;left:"+(i*40-1)+";top:"+(ph-ch-31)+";width:3;height:"+3+"' />"
      if(this.getCss(css,"showVal")=="t")
       l+="<span style='position:absolute;z-index:3;font:12;left:"+(i*40-5)+";top:"+(ph-ch-42)+"'>"+a[i][j]+"</span>"
     }
    }
   }
  }
  return l
 
 }
 this.pie=function(vList,css){
  var l="",a,i,j,n,s,amt,dx=0
  var pw=this.getCss(css,"width",500),ph=this.getCss(css,"height",300)
  if(ph<150||pw<150){
   alert("????????????")
   return
  }
  a=vList.split(";")
  n=a.length*170-130
  if(pw<n)
   pw=n
  l+="<v:roundrect fillcolor='"+this.getCss(css,"background","white")+"' style='position:absolute;left:0;top:0;width:"+pw+";height:"+ph+"'><v:shadow on=t type=emboss opacity=80% offset='3px,3px' offset2='5px,5px' /></v:roundrect>"
  l+="<span style='position:absolute;z-index:3;font:12;left:220;top:10'>"+this.getCss(css,"title")+"</span>"
  if(a.length==2)
   dx=pw/2-100
  if(a.length==3)
   dx=pw/2-180
  for(i in a){
   a[i]=a[i].split(",")
   amt=0
   for(j=1;j<a[i].length;j++){
    amt+=parseFloat(a[i][j])
   }
   sa=0
   for(j in a[i]){
    if(i==0){
     if(j>0){
      l+="<v:roundrect fillcolor='"+this.ac[j-1]+"' style='position:absolute;left:"+(j*80-20)+";top:30;width:20;height:20' />"
      l+="<span style='position:absolute;z-index:3;font:12;left:"+(j*80+5)+";top:35'>"+a[i][j]+"</span>"
     }
    }
    else{
     if(j==0){
      l+="<span style='position:absolute;z-index:3;font:12;left:"+(i*170-90+dx)+";top:"+(ph-50)+"'>"+a[i][j]+"("+amt+")</span>"
     }
     else{
      ea=a[i][j]*360/amt+sa
      if(j==a[i].length-1)
       ea=0
      if(a[i][j]>0)
       l+=this.getPie(60,sa,ea,"title:"+a[i][j]+";val:"+(parseInt((10000*a[i][j]/amt))/100)+"%;x:"+(i*170-70+dx)+";y:"+(ph/2+10)+";background:"+this.ac[j-1])
      sa=ea
     }
    }
   }
  }
  return l
 }
 this.getPie=function(r,sa,ea,css){
  var sf,ef,sx,sy,ex,ey
  var title=this.getCss(css,"title"),val=this.getCss(css,"val")
  var x=parseInt(this.getCss(css,"x",0)),y=parseInt(this.getCss(css,"y",0))
  sf=Math.PI*(sa/180)
  ef=Math.PI*(ea/180)
  sy=parseInt(r*Math.sin(sf))
  sx=parseInt(r*Math.cos(sf))
  ey=parseInt(r*Math.sin(ef))
  ex=parseInt(r*Math.cos(ef))
  
  s="m0,0l"+sx+","+sy+"ar-"+r+",-"+r+","+r+","+r+","+ex+","+ey+","+sx+","+sy+",l0,0xe"
  l="<v:shape path='"+s+"' title='"+title+"' coordsize=1,1 style='position:absolute;width:1;height:1;left:"+this.getCss(css,"x","0")+";top:"+this.getCss(css,"y","0")+"' fillcolor='"+this.getCss(css,"background","white")+"' />"
  if(ef==0)
   ef=270
  var cx=(r+10)*Math.cos((sf+ef)/2),cy=(r+10)*Math.sin((sf+ef)/2)
  l+="<span style='position:absolute;z-index:3;font:12;left:"+(cx+x-10)+";top:"+(cy+y-5)+"'>"+val+"</span>"
  return l
 }
 
 this.draw=function(vList,css)
 {
  var l,type=this.getCss(css,"type")
  if(type=="pie")
   l=this.pie(vList,css)
  else if(type=="lines")
   l=this.lines(vList,css)
  else
   l=this.bar(vList,css)
  return l
 }
}