<%--
    Task_ID    Author     Modify_Date    Description
1.  ePayment   Mario.Cao   05/27/2009     initial
2.  ePayment   Mario.Cao   06/24/2010     解决存在离职员工单导致查询报表出错的问题
--%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.util.*,java.text.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.report.vo.*,java.io.*,org.apache.poi.hssf.usermodel.*"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.*,com.aiait.eflow.util.ExcelFileUtil"%>
<html>
<head>
<title>Personal Applied Form List</title>
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
		"/eBankStaff_"+bartDateFormat.format(new Date())+".xls";
	excelOper.setSheet(1);
	excelOper.appendRow(1);
	int r = 0;
	int c = 1;
	Collection formList = (ArrayList) request
						.getAttribute("resultList"); // search result list

	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"所属公司");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"表单流水号");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"客户名称");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"开户银行");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"银行账号");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"金额");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"备注");
	if (formList != null) {
		DecimalFormat df2 = new DecimalFormat("#.00");
		Iterator formIt = formList.iterator();
		r = 2;
		while (formIt.hasNext()) {
			c = 1;
			excelOper.appendRow(1);
			StaffEbankVO vo = (StaffEbankVO) formIt.next();
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getOrgName());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getRequestNo());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getRequestStaffCNName());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayeeBank());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayeeAccount());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, df2.format(vo.getAmount()));
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getRemark());
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
