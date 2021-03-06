<%--
    Task_ID    Author     Modify_Date    Description
1.  ePayment   Mario.Cao   05/27/2009     initial
--%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/taglibs-i18n.tld" prefix="i18n" %>
<%@page
	import="java.util.*,java.text.*,com.aiait.eflow.util.StringUtil,com.aiait.eflow.report.vo.*,java.io.*,org.apache.poi.hssf.usermodel.*"%>
<%@page
	import="com.aiait.eflow.housekeeping.vo.FormTypeVO,com.aiait.eflow.housekeeping.vo.StaffVO,com.aiait.eflow.common.helper.ParamConfigHelper,com.aiait.eflow.util.ExcelFileUtil"%>	
<script type=text/javascript src="<%=request.getContextPath()%>/common/message.jsp"></script>
<%
	ExcelFileUtil excelOper = new ExcelFileUtil();
	SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMddHHmmdd");
	//File f = new File("C:/EPAY_RPT");
	//f.mkdir();
	String pathandname = ParamConfigHelper.getInstance().getParamValue("upload_file_dir")+
		"/eBankVender_"+bartDateFormat.format(new Date())+".xls";
	excelOper.setSheet(1);
	excelOper.appendRow(1);
	int r = 0;
	int c = 1;
	Collection formList = (ArrayList) request
					.getAttribute("resultList"); // search result list

	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"表单流水号");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"付款帐号开户行");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"付款帐号");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"付款帐号名称");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"收款帐号开户行");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"省份名");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"地市名");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"收款帐号");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"收款帐号名称");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"金额（分）");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"汇款用途");
	excelOper.writecell(1,c++,HSSFCell.CELL_TYPE_STRING,"备注");
	if (formList != null) {
		DecimalFormat df2 = new DecimalFormat("#.00");
		Iterator formIt = formList.iterator();
		r = 2;
		while (formIt.hasNext()) {
			c = 1;
			excelOper.appendRow(1);
			PaymentVO vo = (PaymentVO) formIt.next();
			String[] tmp = {""};
			String prov = "";
			String city = "";
			if(vo.getPayeeProvince()!=null){
				tmp=vo.getPayeeProvince().split("_");
				if(tmp.length > 1){
					prov = tmp[0];
					city = tmp[tmp.length-1];
				}else {
					prov=vo.getPayeeProvince();
					city=vo.getPayeeCity();
				}
			}
			
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getRequestNo());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayBank());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayAccount());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayName());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayeeBank());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, prov);
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, city);
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayeeAccount());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPayeeName());
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, df2.format(vo.getAmount()));
			excelOper.writecell(r,c++,HSSFCell.CELL_TYPE_STRING, vo.getPurpose());
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
	}	%>