package com.aiait.eflow.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aiait.eflow.housekeeping.dao.ParamConfigDAO;
import com.aiait.eflow.util.RandomUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.db.DBManagerFactory;
import com.aiait.framework.db.IDBManager;

public class ExcelWriterUtil   {
	private String fileName;
	private static String path ;
	private static String excelType;
	private File excelFile;
	private Workbook workBook;

	public static final String SUFFIX_2003 = ".xls";
	public static final String SUFFIX_2007 = ".xlsx";
	
	public ExcelWriterUtil(String fileName){
		if(StringUtil.isEmptyString(path) || StringUtil.isEmptyString(excelType)){
			IDBManager dbManager;
			try {
				dbManager = DBManagerFactory.getDBManager();
				ParamConfigDAO paramConfigDao = new ParamConfigDAO(dbManager);
				excelType = paramConfigDao.getParamByCode("excel_type").getParamValue();
				path = paramConfigDao.getParamByCode("excel_temp_path").getParamValue();
			} catch (Exception e) {
				e.printStackTrace();
				path = "D:/excel/";
				excelType = "Excel2007";
			}
		}
		
		String appendString = "_"+System.currentTimeMillis()+"_"+RandomUtil.createRandomStr(5, null);
		String suffix;
		if("Excel2003" == excelType){
			suffix = SUFFIX_2003;
		}else{
			suffix = SUFFIX_2007;
		}
		this.fileName = fileName+appendString+suffix;
		
	}

	


	public File setExcelData(Map<String, String> data) {
		excelFile = createDestFile();
		getWorkBook();
		this.writeJsonToExcel(excelFile, data);
		return excelFile;
	}
	
	private File createDestFile() {
		String result = "";
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String parentFilePath = path +"/"+date;
		File parentFile = new File(parentFilePath);
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		String destFileName = parentFilePath +"/" + fileName;
		File resultFile = new File(destFileName);
		if(resultFile == null || !resultFile.exists()){
			try {
				resultFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultFile;
	}
	
	public Workbook getWorkBook() {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(excelFile);
			if("Excel2003" == excelType){
				workBook = new HSSFWorkbook();
			}else{
				workBook = new XSSFWorkbook();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return workBook;
	}
	
	private void writeJsonToExcel(File destFile,Map<String,String> data) {
		String dataString = data.get("rows");
		String columnIds = data.get("colModels");
		String columnNames = data.get("colNames");
		writeJsonToExcel(destFile, dataString, columnIds, columnNames, null);
	}
	
	
	private void writeJsonToExcel(File destFile,String dataString,String columnIds,String columnNames,ExcelBean excelBean) {
		int sheetIndex = 0;
		int startRow = 0;
		int endRow = -1;
		int startCell = 0;
		int endCell = -1;
		boolean isErrorData = false;
		if(excelBean != null){
			sheetIndex = excelBean.getSheetIndex();
			startRow = excelBean.getStartRow();
			endRow = excelBean.getEndRow();
			startCell = excelBean.getStartCell();
			endCell = excelBean.getEndCell();
			isErrorData = excelBean.isErrorData();
		}
		Sheet sheet = this.getSheet();
		if(sheet == null) {
			sheet = this.workBook.createSheet();
		}
		
		Object[] dataArray =  JSONArray.fromObject(dataString).toArray();
		Object[] columnIdArray =  JSONArray.fromObject(columnIds).toArray();
		Object[] columnNameArray =  JSONArray.fromObject(columnNames).toArray();
		
		if(dataArray.length > 0){
			Row titlerow = this.getRow(sheet, startRow);
			for(int i=0;i<columnNameArray.length;i++){
				Cell cell = this.getCell(titlerow, startCell+i);
				String columnName = (String)columnNameArray[i];
				columnName = columnName == null ||"null".equals(columnName) ? "" : columnName;
				cell.setCellValue(columnName);
				if(isErrorData){
					cell.setCellStyle(setErrorStyle(workBook));
				}
			}
			for(int i=0;i<dataArray.length; i++){
				Row row = this.getRow(sheet, startRow+i+1);
				JSONObject rowData = (JSONObject)dataArray[i];
				for(int j=0;j<columnIdArray.length;j++){
					Cell cell = this.getCell(row, startCell+j);
					String result = rowData.getString((String)columnIdArray[j]);
					result = result == null ||"null".equals(result) ? "" : result;
					cell.setCellValue(result);
					if(isErrorData){
						cell.setCellStyle(setErrorStyle(workBook));
					}
				}
			}
		}
		try {
			workBook.write(new FileOutputStream(destFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void writeExcelData(File destFile,
			List<Map<String, Object>> listData,ExcelBean excelBean) {
		int sheetIndex = 0;
		int startRow = 0;
		int endRow = -1;
		int startCell = 0;
		int endCell = -1;
		boolean isErrorData = false;
		if(excelBean != null){
			sheetIndex = excelBean.getSheetIndex();
			startRow = excelBean.getStartRow();
			endRow = excelBean.getEndRow();
			startCell = excelBean.getStartCell();
			endCell = excelBean.getEndCell();
			isErrorData = excelBean.isErrorData();
		}
		Sheet sheet = this.getSheet();
		if(sheet == null) {
			sheet = this.workBook.createSheet();
		}
		if(listData != null && !listData.isEmpty()){
			int records = listData.size();
			endRow = (endRow == -1) ? records : endRow;
			for (int i = startRow;i < startRow + endRow ; i++) {
				Row row = this.getRow(sheet, i);
				if(row == null) {
					row = sheet.createRow(i);
				}
				Map<String, Object> map = listData.get(i-startRow);
				int oneRowEndCell = (endCell == -1) ? map.keySet().size() : endCell;
				int start = 1;
				for (int j = startCell; j < startCell + oneRowEndCell; j++) {
					Object value = "";
					
					if(map != null && !map.isEmpty()){
						value = map.get(start+"");
						start ++;
						//value = map.values().iterator().next();
					}
					Cell cell = this.getCell(row, j);
					if(cell == null){
						cell = row.createCell(j);
					}
					String result = value == null ? "" : value.toString();
					cell.setCellValue(result);
					if(isErrorData){
						cell.setCellStyle(setErrorStyle(workBook));
					}
				}
			}
		}
		try {
			workBook.write(new FileOutputStream(destFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static CellStyle setErrorStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		Font font = wb.createFont();
		font.setColor(IndexedColors.RED.getIndex());
		style.setFont(font);
		return style;
	}
	
	public Sheet getSheet(int sheetIndex) {
		if(workBook == null) {
			return null;
		}
		Sheet sheet = workBook.getSheetAt(sheetIndex);
		return sheet;
	}
	
	public Sheet getSheet( ) {
		if(workBook == null) {
			return null;
		}
		Sheet sheet = workBook.createSheet();
		 return sheet;
	}

	public Row getRow(Sheet sheet, int rowIndex) {
		if(workBook == null) {
			return null;
		}
		Row row = sheet.getRow(rowIndex);
		if(row == null) {
			row = sheet.createRow(rowIndex);
		}
		return row;
	}

	public Cell getCell(Row row,int cellIndex){
		Cell cell = row.getCell(cellIndex);
		if(cell == null){
			cell = row.createCell(cellIndex);
		}
		return cell;
	}

}
