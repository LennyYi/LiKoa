<%@page import="com.aiait.framework.mvc.util.Global"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n"%>
<%@page import="java.io.*,com.aiait.eflow.util.*,com.aiait.eflow.common.helper.ParamConfigHelper"%>
<%@page import="com.aiait.framework.util.*,com.aiait.eflow.common.*"%>
<i18n:jsmessage jsRelativePath="js" prefixName="message" />
<%
    String fileName = (String) request.getParameter("fileName");
    fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
	String lowFileName = fileName.toLowerCase();
	String strMIME = "";
	if(lowFileName.endsWith("pdf")){
		strMIME = "application/pdf";
	} else if(lowFileName.endsWith("doc")||lowFileName.endsWith("docx")){
		strMIME = "application/msword";
	} else if(lowFileName.endsWith("xls")||lowFileName.endsWith("xlsx")){
		strMIME = "application/vnd.ms-excel";
	} else if(lowFileName.endsWith("ppt")||lowFileName.endsWith("pptx")){
		strMIME = "application/vnd.ms-powerpoint";
	} else if(lowFileName.endsWith("jpg")||lowFileName.endsWith("jpeg")||lowFileName.endsWith("img")){
		strMIME = "image/jpeg";	
	} else if(lowFileName.endsWith("gif")){
		strMIME = "image/gif";
	} else if(lowFileName.endsWith("msg")){
		strMIME = "message/external-body";
	} else {
		strMIME = "text/html";
	}
    String type = (String) request.getParameter("type");
    if (type == null) {
        type = "request";
    }
    if ("request".equals(type)) {
        fileName = "/upload/requestform/" + fileName;
    } else {
        String requestNo = (String) request.getParameter("requestNo");
        fileName = "/upload/processform/" + requestNo + "/" + fileName;
    }
    File f = FileUtil.getUploadFile(fileName);
    if (!f.exists()) {
        out.print("File Not Found");
        return;
    }
    OutputStream outPut = null;
    BufferedInputStream br = null;
    try {
        br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // It's important
        response.setContentType(strMIME);
        response.setHeader("Content-Disposition", "inline; filename="
                + java.net.URLEncoder.encode(f.getName(), "UTF-8").replaceAll("\\+", "%20"));
        outPut = response.getOutputStream();
        while ((len = br.read(buf)) > 0) {
            outPut.write(buf, 0, len);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        try {
            if (outPut != null) {
                outPut.close();
            }
            if (br != null) {
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.clear();
        out = pageContext.pushBody();
    }
%>