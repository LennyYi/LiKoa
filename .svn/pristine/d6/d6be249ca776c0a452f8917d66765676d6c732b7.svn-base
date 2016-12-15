<%@page import="com.aiait.eflow.util.EncryptUtil,com.aiait.eflow.housekeeping.vo.*,com.aiait.eflow.common.*,com.aiait.eflow.common.helper.*" %>
<%@page import="com.aiait.framework.db.*" %>
<%
	String type = (String)request.getParameter("type");  
   	String server = (String)request.getParameter("server");
	String db = (String)request.getParameter("db");
	String login = (String)request.getParameter("login");
	String pwd = (String)request.getParameter("pwd");
	
	StaffVO currentStaff = (StaffVO) request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
	String validStaff = (ParamConfigHelper.getInstance().getParamValue("hrdb_maintain","").split(";"))[0];   
	if(!currentStaff.getStaffCode().equals(validStaff)){
		throw new Exception("You are not allowed to access this page.");
	}
   if(type==null || "".equals(type)){
	   type = "init"; 
   }
   EncryptUtil encrypt = new EncryptUtil();
   try{
     String ncrServer = "";
     String ncrDb = "";
     String ncrLogin = "";
     String ncrPwd = "";
     
     if(!"init".equals(type)){
    	ncrServer = encrypt.EncryptDES(server);
    	ncrDb = encrypt.EncryptDES(db);
    	ncrLogin = encrypt.EncryptDES(login);
    	ncrPwd = encrypt.EncryptDES(pwd);
    	String SQL = "UPDATE teflow_HRDB_setting SET server=?, db=?, login=?, password=?, updatetime=getdate() where 1=1"; 
    	String resultMsg = "";
    	
 		IDBManager dbManager = null;
 		try{
			dbManager =  DBManagerFactory.getDBManager();
			dbManager.executeUpdate(SQL, 
					new Object[]{ncrServer, ncrDb, ncrLogin, ncrPwd},
					new int[]{1,1,1,1});
            dbManager.commit();
            resultMsg = "Success";
 		}catch(Exception ex){
            dbManager.rollback();
            ex.printStackTrace();
            out.print(ex.getMessage());
            resultMsg = ex.getMessage();
 		}finally{
 			out.print(resultMsg);
            if (dbManager != null)
                dbManager.freeConnection();
 		} 		
     }else{
%>
<html>
<title>AIAIT E-Flow</title>
<body>
  <form name="myForm" method='post' action="<%=request.getContextPath()%>/encryptHR.jsp?type=save">
  	SERVER: <input type="text" name="server" value=""><br/>
  	DB: <input type="text" name="db" value=""><br/>
  	LOGIN: <input type="text" name="login" value=""><br/>
  	PASSWORD: <input type="password" name="pwd" value=""><br/>
  	  	
    <input type="submit" name="searchBtn" value="Encrypt and Save" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">&nbsp;&nbsp;
	<input type="button" name="closeBtn" value="Close" onclick="window.close()" class=btn3_mouseout onmouseover="this.className='btn3_mouseover'" onmouseout="this.className='btn3_mouseout'"
           onmousedown="this.className='btn3_mousedown'" onmouseup="this.className='btn3_mouseup'">

  </form>
</body>  
</html>
<%	 
     }
   }catch(Exception e){
	   e.printStackTrace();
	   out.print("fail");
   }
%>