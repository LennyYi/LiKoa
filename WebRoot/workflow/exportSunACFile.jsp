<%-- 
New Page: ePayment    Mario Cao    11/05/2009    Form can export to SunAC upload file 
--%>
<%@ page contentType="application/x-msdownload;charset=UTF-8" %>  
<%@ page import="java.io.*,java.util.*,com.aiait.eflow.util.*,com.aiait.eflow.formmanage.vo.*,com.aiait.eflow.common.helper.*"%>
<%@ page import="java.math.*" %>
<%
	Collection resultList = (ArrayList)request.getAttribute("resultList");
	FormManageVO form = (FormManageVO)request.getAttribute("form");
	String[] fieldArray = (String[])request.getAttribute("fieldArray");
	HashMap fieldMap = (HashMap)request.getAttribute("formFieldMap");
 	final char[] SPACE20 = "                    ".toCharArray();
 	final char[] ZERO20  = "00000000000000000000".toCharArray();
	
	FileWriter fw = null;
	BufferedWriter bw=null;
	
	File f = new File(ParamConfigHelper.getInstance().getParamValue("upload_file_dir") + "/sunac.txt");
	OutputStream outPut = null;
	BufferedInputStream br = null;
	try {
	 	byte[] buf = new byte[1024];
	 	int len = 0;
	 	response.reset(); //It is important
	 	response.setContentType("application/x-msdownload"); 
	 	response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(f.getName(), "UTF-8"));
		outPut = response.getOutputStream(); 

		fw=new FileWriter(f); 
		bw=new BufferedWriter(fw); 
        if(resultList!=null && resultList.size()>0){
        	Iterator it = resultList.iterator();
        	while(it.hasNext()){
				HashMap map = (HashMap)it.next();
				StringBuffer str = new StringBuffer();
				str.append(((String)map.get("FIELD_06_11")).substring(0,10));
				str.append(SPACE20,0,5);
				str.append(map.get("FIELD_06_10"));
				str.append(StringUtil.getCurrentDateStr("yyyyMMdd"));
				str.append(SPACE20,0,2);
				str.append("M");
				str.append(SPACE20,0,14);
				str.append(ZERO20,0,18);
				str.append(("1").equals(map.get("FIELD_06_1"))?"D":"C");
				str.append(SPACE20,0,1);
				str.append("EPAY ");
				str.append("EPAY ");
				str.append("EPAY "+StringUtil.getCurrentDateStr("MMdd")+"      ");
				str.append(((String)map.get("REQUEST_NO") 
						+ (String)map.get("FIELD_06_7") 
						+ new String(SPACE20)).substring(0,25));
				for(int i=0;i< 69 ;i++){
					str.append(' ');
        		}
				str.append(map.get("FIELD_06_6")==null?"NUL":((String)map.get("FIELD_06_6")).substring(0,3));
				str.append(SPACE20,0,2);
				str.append(ZERO20,0,18);
				String tmpStr = (String)map.get("FIELD_06_8");
				BigDecimal decVal = new BigDecimal(Double.parseDouble(tmpStr));
				decVal = decVal.multiply(new BigDecimal(1000));
				tmpStr = "" + decVal.longValue();//重要，去掉小数部分（.00）
				str.append(new String(ZERO20,0,18-tmpStr.length()) + tmpStr);//9(15)V999

				for(int i=0;i< 28 ;i++){
					str.append(' ');
				}
				for(int i=0;i< 143 ;i++){
					str.append(' ');
        		}

				bw.write(str.toString());
				if(it.hasNext())bw.newLine();
        	}
	 	}
		bw.flush();//将数据更新至文件
	 	br = new BufferedInputStream(new FileInputStream(f));
		
		while((len = br.read(buf))>0) {
		    outPut.write(buf,0,len); 
		}
	 	out.clear();
	 	out = pageContext.pushBody();
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {
		if (bw!=null) bw.close();
		if (fw!=null) fw.close();//关闭文件流
		if(outPut!=null) outPut.close(); 
		if (br != null) br.close();
    	FileUtil.deleteFile(f.getPath());
	}
%>