<%--
    Task_ID    Author     Modify_Date    Description
1.  ePayment   Mario.Cao   05/27/2009     initial
2.  ePayment   Mario.Cao   06/24/2010     解决存在离职员工单导致查询报表出错的问题
--%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.io.*,java.util.*,java.text.*,org.apache.poi.hssf.util.*,org.apache.poi.hssf.usermodel.*,com.aiait.eflow.common.helper.*"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.formmanage.vo.FormManageVO,com.aiait.eflow.util.*,com.aiait.eflow.report.vo.*"%>
<%@ page contentType="text/html; charset=GBK" %>  	
<%
ExcelFileUtil excelOper = new ExcelFileUtil();
SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmdd");
//File f = new File("C:/EPAY_RPT");
//f.mkdir();
String pathandname = ParamConfigHelper.getInstance().getParamValue("upload_file_dir")+
	"/invoice_"+bartDateFormat.format(new Date())+".xls";
excelOper.setSheet(1);
excelOper.appendRow(1);
int r = 0;
int c = 1;
Collection formList = (ArrayList) request
					.getAttribute("resultList"); // search result list

	excelOper.writecell(1,1,HSSFCell.CELL_TYPE_STRING,"未递交发票清单报告");
	excelOper.getCell().getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
	excelOper.getSheet().addMergedRegion(new Region(0,(short)0,0,(short)6));
	excelOper.appendRow(1);
	excelOper.writecell(2,1,HSSFCell.CELL_TYPE_STRING,"Transaction between "+
			(String)request.getAttribute("beginDate")+" and "+
			(String)request.getAttribute("endDate"));
	excelOper.getCell().getCellStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
	excelOper.getSheet().addMergedRegion(new Region(1,(short)0,1,(short)6));
	excelOper.appendRow(1);
	excelOper.writecell(3,c++,HSSFCell.CELL_TYPE_STRING,"支付日期");
	excelOper.writecell(3,c++,HSSFCell.CELL_TYPE_STRING,"表单流水号");	
	excelOper.writecell(3,c++,HSSFCell.CELL_TYPE_STRING,"申请人");	
	excelOper.writecell(3,c++,HSSFCell.CELL_TYPE_STRING,"所属部门");	
	excelOper.writecell(3,c++,HSSFCell.CELL_TYPE_STRING,"备注");	
	excelOper.writecell(3,c++,HSSFCell.CELL_TYPE_STRING,"收款人");	
	excelOper.writecell(3,c++,HSSFCell.CELL_TYPE_STRING,"金额");		
	if (formList != null) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat df2 = new DecimalFormat("#.00");
		Iterator formIt = formList.iterator();
		r = 4;
		while (formIt.hasNext()) {
			c = 1;
			excelOper.appendRow(1);
			InvoiceInfoVO vo = (InvoiceInfoVO) formIt.next();
			Date cDate = null;
			if (vo.getPayDateStr() != null
					&& !"".equals(vo.getPayDateStr())) {
				cDate = df.parse(vo.getPayDateStr());
			}
			StaffVO staff = StaffTeamHelper.getInstance().getStaffByCode(vo.getRequestStaffCode());
	
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING,(
					vo.getPayDateStr() != null && !"".equals(vo.getPayDateStr())) ? 
					StringUtil.getDateStr(cDate, "yyyy-MM-dd")
					: "");
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getRequestNo());
			try{
				excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, staff.getStaffName());
			}catch(Exception e){
				excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getRequestStaffCode());
			}			
			try{
				excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, StaffTeamHelper.getInstance().getTeamNameByCode(staff.getTeamCode()));
			}catch(Exception e){
				excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, "-");
			}	
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getRemark());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayee());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, df2.format(vo.getAmount()));
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
	}	%>