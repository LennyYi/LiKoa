<%--
    Task_ID    Author     Modify_Date    Description
1.  ePayment   Mario.Cao   07/21/2009     initial
--%>
<%@page
	import="java.util.*,java.io.*,java.text.DecimalFormat,java.text.SimpleDateFormat,com.aiait.eflow.util.StringUtil,com.aiait.eflow.report.vo.*,com.aiait.eflow.common.helper.ParamConfigHelper"%>
<%@page
	import="com.aiait.eflow.wkf.vo.AceVO,org.apache.poi.hssf.usermodel.*,org.apache.poi.poifs.filesystem.POIFSFileSystem,com.aiait.eflow.util.ExcelFileUtil"%>

<html>
<head>
<title>Ace Interface</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
</head>

<body style="font-family: Arial, Helvetica, sans-serif;">
<%

	ExcelFileUtil excelOper = new ExcelFileUtil();
	SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmdd");
	//File f = new File("C:/EPAY_RPT");
	//f.mkdir();
	String pathandname = ParamConfigHelper.getInstance().getParamValue("upload_file_dir")+
		"/AceInterface_"+bartDateFormat.format(new Date())+".xls";
	excelOper.setSheet(1);
	excelOper.appendRow(1);
	Collection formList = (ArrayList) request.getAttribute("exportAceList"); // search result list
	int r = 0;
	int c = 1;
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"COMPANY_CODE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"CITY_CODE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ACCNT_CODE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"PERIOD");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"TRANS_DATE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"AMOUNT");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"D_C");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ALLOCATION");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"BATCH_NO");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"JRNAL_TYPE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"JRNAL_SRCE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"TREFERENCE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"DESCRIPTION");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"CONV_CODE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"CONV_RATE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"OTH_AMT");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ASSET_CODE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ASSET_SUB");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ASSET_IND");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"DUE_DATE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T0");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T1");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T2");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T3");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T4");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T5");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T6");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T7");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T8");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"ANAL_T9");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"WB_FLAG");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"WB_ERR_DESC");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"WB_JRNAL_NO");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"WB_TREFERENCE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"DB_CODE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"POLICY_CODE");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"BANK_NO");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"epay_id");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"dc_id");
	if (formList != null) {
		DecimalFormat df3 = new DecimalFormat("#.000");
		DecimalFormat df9 = new DecimalFormat("#.000000000");
		Iterator formIt = formList.iterator();
		r = 2;
		while (formIt.hasNext()) {
			AceVO vo = (AceVO) formIt.next();
			Date cDate = null;
			c = 1;
			excelOper.appendRow(1);
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getCOMPANY_CODE ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getCITY_CODE    ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getACCNT_CODE   ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getPERIOD       ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getTRANS_DATE   ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,df3.format(vo.getAMOUNT()));
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getD_C          ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getALLOCATION   ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getBATCH_NO     ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getJRNAL_TYPE   ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getJRNAL_SRCE   ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getTREFERENCE   ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getDESCRIPTION  ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getCONV_CODE    ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,df9.format(vo.getCONV_RATE()));
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,df3.format(vo.getOTH_AMT()));
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getASSET_CODE   ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getASSET_SUB    ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getASSET_IND    ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getDUE_DATE     ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T0      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T1      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T2      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T3      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T4      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T5      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T6      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T7      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T8      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getANAL_T9      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getWB_FLAG      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getWB_ERR_DESC  ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getWB_JRNAL_NO  ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getWB_TREFERENCE());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getDB_CODE      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getPOLICY_CODE  ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getBANK_NO      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getEpay_id      ());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,vo.getDc_id        ());
			r++;
		}
		excelOper.export(pathandname);
	}
	File f = new File(pathandname);
	OutputStream outPut = null;
	BufferedInputStream br = null;
	try {
		if (!f.exists()) {
			out.print("File Not Found");
		}
		br = new BufferedInputStream(
				new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;
		response.reset(); //It is important
		response.setContentType("application/x-msdownload"); 
		response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(f.getName(), "UTF-8"));
	    outPut = response.getOutputStream(); 
		while((len = br.read(buf))>0) {
		    outPut.write(buf,0,len); 
		}
		out.clear();
		out = pageContext.pushBody();
	} catch (Exception ex) {
	    //ex.printStackTrace();
	} finally {
	    if(outPut!=null)
	    	outPut.close(); 
	    if (br != null) {
	        br.close();
	    }
	}	
	%>