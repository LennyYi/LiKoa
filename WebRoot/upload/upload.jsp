<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
 <script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<%@page
	import="com.aiait.eflow.util.FileUtil,com.aiait.eflow.util.MultipartRequest,java.io.File,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.CommonName"%>


<%
   StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
   String userId = staff.getLogonId();
   try {
	   if (request.getMethod().equals("POST") && MultipartRequest.isMultipart(request)) {
		   request = new MultipartRequest(request,userId);//userId??????
		  
	         for(int i=0;i<2;i++){
	        	//System.out.println("----"+i);
			   //??????
	           File upFile = ((MultipartRequest) request).getFile("path"+i);
               //???????
	           String fileName = ((MultipartRequest) request).getFileName(upFile);
               //????http??
	           request.getParameter("res_name");
               //????????c:/temp?????123.jpg???
	           FileUtil.saveAs(upFile, "c:/temp/"+userId+"/"+fileName);
	         }
             out.print("Upload successfully!");
		   
	   }
	   }finally{
	     if (request instanceof MultipartRequest) {
	      //?????????
	      ((MultipartRequest) request).deleteTemporaryFile();
	     }
	   }


%>
