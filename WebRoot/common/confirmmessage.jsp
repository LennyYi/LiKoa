
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
  <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<style>

#bg{background:#000000;opacity: 0.5;-moz-opacity:0.5; filter:alpha(opacity=50); width:100%; height:100%;position:absolute; top:0; left:0}
#info{height:0px; width:0px;top:50%; left:50%;position:absolute;  line-height:1.7}
#center{background:#fff;border:1px solid #217AC1; width:300px; height:100px; position:absolute; margin:-50px -150px;}
#center strong{ display:block; padding:2px 5px; background:#EBF4FC; color:#519FEE;}
#center p{padding:10px; text-align:center; color:#1C6FB8;}
</style>

<script>
 function boxs(v,message){ 
   window.scrollTo(0,0);
   var bo = document.getElementsByTagName('body')[0];
   var ht = document.getElementsByTagName('html')[0];
   var boht = document.getElementById('boxs');    
   boht.innerHTML = '';
   bo.style.height='auto';
   bo.style.overflow='auto';
   ht.style.height='auto'; 
   if(v == 1){   
     bo.style.height='100%';
     bo.style.overflow='hidden';
     ht.style.height='100%';  
     boht.innerHTML = '<div id="bg"></div><div id="info"><div id="center"><strong>Tip:</strong><p><a href="javascript:boxs(0,null);">'+message+'</a></p></div></div>';   
     //return 0;
   }else{
     //return 1;
     doAfterMessage();
   }
} 
</script>
<span id="boxs"></span>