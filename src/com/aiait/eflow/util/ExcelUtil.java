package com.aiait.eflow.util;

import java.io.*;
import java.math.BigDecimal;
import java.text.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellValue;

public class ExcelUtil {
    private POIFSFileSystem fileSystem;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFRow row;
    private HSSFCell cell;

    private final DecimalFormat df = new DecimalFormat("00");

    private final char[] columnid = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 
     * @param xlsPath
     */
    public ExcelUtil(String xlsPath) throws Exception {
        this.fileSystem = new POIFSFileSystem(new FileInputStream(xlsPath));
        this.workbook = new HSSFWorkbook(fileSystem);
    }

    /**
     * @since CHO epayment
     */
    public ExcelUtil() throws Exception {
        this.workbook = new HSSFWorkbook();
        this.workbook.createSheet();
        appendRow(1);
    }

    /**
     * 增加一行，有255个cell
     * 
     * @param sheet
     * @return
     */
    public void appendRow(int sheetnum) {
        int maxRow = this.workbook.getSheetAt(sheetnum - 1).getPhysicalNumberOfRows();
        this.workbook.getSheetAt(sheetnum - 1).createRow(maxRow);
        for (short i = 0; i < 255; i++)
            this.workbook.getSheetAt(sheetnum - 1).getRow(maxRow).createCell(i);
        return;
    }

    /**
     * 获取单元格数据,以String格式返回
     * 
     * @param sheet
     * @param row
     * @param cell
     * @return
     */
    public String getCellValue(int sheet, int row, int cell) {
        setSheet(sheet);
        setRow(row);
        setCell(cell);
        if (this.getCell() == null)
            return "";
        if (this.getCell().getCellType() == HSSFCell.CELL_TYPE_STRING) {
            return this.getCell().getStringCellValue();
        } else if (this.getCell().getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            if (new BigDecimal(getCell().getNumericCellValue()).divideToIntegralValue(BigDecimal.ONE).equals(
                    new BigDecimal(getCell().getNumericCellValue()))) {
                return df.format(this.getCell().getNumericCellValue());
            } else {
                return "" + this.getCell().getNumericCellValue();
            }
        } else if (this.getCell().getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(this.getSheet(), this.workbook);
            evaluator.setCurrentRow(this.getRow());
            CellValue cellValue = evaluator.evaluate(this.getCell());   //change by Colin Wang for import the poi-3.9.jar to replace the poi-3.1.jar, by this change have not been tested         	
            	//return cellValue.getStringValue();
            
                int cellType = cellValue.getCellType();
                switch (cellType){
                
                case HSSFCell.CELL_TYPE_STRING:
                	return cellValue.getStringValue();
                	
                case HSSFCell.CELL_TYPE_BOOLEAN:
                	return Boolean.toString(cellValue.getBooleanValue());    
                	
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if (new BigDecimal(cellValue.getNumberValue()).divideToIntegralValue(BigDecimal.ONE).equals(
                            new BigDecimal(cellValue.getNumberValue()))) {
                        return df.format(cellValue.getNumberValue());
                    } else {
                        return "" + cellValue.getNumberValue();
                    }
                    
                case HSSFCell.CELL_TYPE_BLANK:
                case HSSFCell.CELL_TYPE_ERROR:
                	return cellValue.getStringValue();
                }                
                return cellValue.getStringValue();                
        } else {
            return this.getCell().getStringCellValue();
        }
    }

    /**
     * 获取单元格数据,以Date String格式返回
     * 
     * @param sheet
     * @param row
     * @param cell
     * @return
     */
    private String getCellDateValue(int sheet, int row, int cell) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        setSheet(sheet);
        setRow(row);
        setCell(cell);
        if (this.getCell() == null)
            return "";
        return dateFormat.format(this.getCell().getDateCellValue());

    }

    // 导出文件
    public void Export(String xlsPath, int sheet, int row, int cell, String value) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(xlsPath);

        this.setSheet(sheet);
        this.setRow(row);
        this.setCell(cell);
        this.getCell().setCellValue(value);

        this.workbook.write(fileOut);

        fileOut.close();

    }

    /**
     * 导出文件
     * 
     * @param xlsPath
     * @throws Exception
     */
    public void export(String xlsPath) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(xlsPath);

        this.workbook.write(fileOut);

        fileOut.close();
    }

    /**
     * 写入一格
     * 
     * @param xlsPath
     * @throws Exception
     */
    public void writecell(int row, int cell, int type, String value) throws Exception {
        this.setRow(row);
        this.setCell(cell);
        this.getCell().setCellType(type);
        this.getCell().setCellValue(value == null ? "" : value);
    }

    public HSSFCell getCell() {
        return cell;
    }

    private void setCell(int cell) {
        this.cell = this.row.getCell((short) (cell - 1));
    }

    public HSSFRow getRow() {
        return row;
    }

    private void setRow(int row) {
        this.row = this.sheet.getRow(row - 1);
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    // private void setSheet(int sheet) {
    public void setSheet(int sheet) {
        // 默认是从0开始的,这里利用n-1转换下,用1开始,调用好理解点
        this.sheet = this.workbook.getSheetAt(sheet - 1);
    }

    public static void main(String[] args) {
        try {
            ExcelUtil excelOper = new ExcelUtil("D:/ts_eform/SH/data/Budget/Budget_uploading_list_year_CHO.xls");
            String tmp = excelOper.getCellValue(1, 3, 1);
            System.out.println(excelOper.getCellValue(1, 3, 1));
            System.out.println(excelOper.getCellValue(1, 3, 2));
            System.out.println(excelOper.getCellValue(1, 3, 3));
            System.out.println(excelOper.getCellValue(1, 3, 4));
            System.out.println(excelOper.getCellValue(1, 3, 5));
            System.out.println(excelOper.getCellValue(1, 3, 6));
            System.out.println(excelOper.getCellValue(1, 3, 7));
            // System.out.println(excelOper.getCellValue(1, 3, 8));
            // System.out.println(excelOper.getCellValue(1, 3, 9));
            // System.out.println(excelOper.getCellValue(1, 3, 10));
            // System.out.println(excelOper.getCellValue(1, 3, 11));
            System.out.println(excelOper.getCellValue(1, 3, 12));
            // excelOper.Export("产品项目导入模板_导出.xls",1,1,1,"ABCDE");

        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * String s = "123.0";
         * 
         * //Integer i = new Integer(s); //double d = Double.parseDouble(s); Double d = new Double(s);
         * 
         * System.out.print(d.intValue());
         **/
    }

}
