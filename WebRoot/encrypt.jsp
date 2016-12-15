<%@page import="com.aiait.eflow.util.EncryptUtil" %>

<%
   String origPassword = (String)request.getParameter("oldPassword");
   String type = (String)request.getParameter("type");  //01:encrypt; 02:decrypt
   if(type==null || "".equals(type)){
	   type = "01";   //encrypt
   }
   EncryptUtil encrypt = new EncryptUtil();
   try{
     String newPWD = "";
     if("01".equals(type)){
       newPWD = encrypt.EncryptDES(origPassword);
     }else{
    	 newPWD = encrypt.UnryptDES(origPassword);	 
     }
     out.print(newPWD);
   }catch(Exception e){
	   e.printStackTrace();
	   out.print("fail");
   }
%>