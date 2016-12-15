package com.aiait.eflow.reportmanage.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.reportmanage.helper.ReportFieldHelper;
import com.aiait.eflow.reportmanage.vo.ReportManageVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionFieldVO;
import com.aiait.eflow.reportmanage.vo.ReportSectionVO;
import com.aiait.eflow.util.DataConvertUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.vo.WorkFlowProcessTraceVO;
import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ExportFileUtil {

    

    /**
     * 单独在report中将某张report导出pdf文件
     * 
     * @param report
     * @param sectionContentMap
     * @param traceList
     * @return
     * @throws DocumentException
     */
    public static ByteArrayOutputStream exportPDF(ReportManageVO report, HashMap sectionContentMap, Collection traceList)
            throws DocumentException {
        Document doc = new Document(PageSize.A4, 10, 10, 10, 10);
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfWriter docWriter = null;
        //PdfReportField reportField = null;

        try {
            // 中文字体
            BaseFont bfChinese = ExportFileUtil.getBaseFont();
 
            Font fontChinese = new Font(bfChinese, 12, Font.HELVETICA, new Color(0, 0, 0));

            docWriter = PdfWriter.getInstance(doc, baosPDF);
            doc.addCreationDate();
            doc.addTitle(DataConvertUtil.convertISOToGBK(report.getReportId() + " (" + report.getReportName() + ")"));

            doc.open();
            
            Paragraph title1 = new Paragraph(DataConvertUtil.convertISOToGBK(report.getReportId() + " ("
                    + report.getReportName() + ")"), fontChinese);
            title1.setAlignment(1); // 1----居中；2---居左；3----居右
            Chapter chapter1 = new Chapter(title1, 1);
            chapter1.setNumberDepth(0);

            Font fontChineseSection = new Font(bfChinese, 10, Font.HELVETICA, new Color(0, 0, 0));
            Collection sectionList = report.getSectionList();
            int sectionNum = sectionList.size();
            if (sectionNum == 0) {
                Paragraph title11 = new Paragraph("The report has not any field !");
                title11.setFont(fontChineseSection);
                doc.add(chapter1);
                doc.add(title11);
            } else {
                Iterator sectionIt = sectionList.iterator();
                int sectionId = 1;
                while (sectionIt.hasNext()) {
                    ReportSectionVO section = (ReportSectionVO) sectionIt.next();
                    Collection fieldList = section.getFieldList();
                    Iterator it = fieldList.iterator();
                    int count = 1;
                    String sectionType = section.getSectionType();
                    Paragraph sectionTitle = new Paragraph(section.getSectionRemark(), fontChineseSection);
                    Section sectionPDF = chapter1.addSection(sectionTitle);
                    if ("01".equals(sectionType) || "04".equals(sectionType) || "05".equals(sectionType)) { // 01 -- 表格形式
                        PdfPTable datatable = new PdfPTable(section.getFieldList().size() - 1);
                        datatable.setWidthPercentage(100); // 表格的宽度百分比
                        datatable.setSpacingBefore(5);
                        // datatable.set
                        // datatable.getDefaultCell().setPaddingTop(1);
                        datatable.getDefaultCell().setPadding(2);
                        datatable.getDefaultCell().setBorderWidth(1);
                        datatable.getDefaultCell().setBackgroundColor(new Color(239, 239, 239));
                        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

                        while (it.hasNext()) {
                            ReportSectionFieldVO field = (ReportSectionFieldVO) it.next();
                            if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY) {
                                continue;
                            }

                            // 添加表头元素
                            datatable.addCell(new Paragraph(field.getFieldLabel(), fontChineseSection));
                        }

                        // 获取该表格形式section的结果记录集
                        Collection fieldContentList = (ArrayList) sectionContentMap.get(section.getSectionId());
                        if (fieldContentList != null && fieldContentList.size() > 0) {
                            Iterator fieldContentIt = fieldContentList.iterator();
                            while (fieldContentIt.hasNext()) {
                                HashMap fieldContent = (HashMap) fieldContentIt.next();
                                Object[] fieldArray = fieldList.toArray();
                                for (int i = 0; i < fieldArray.length; i++) {
                                    ReportSectionFieldVO field = (ReportSectionFieldVO) fieldArray[i];
                                    if (field.getFieldType() == CommonName.FIELD_TYPE_IDENTITY) {
                                        continue;
                                    }

                                    datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                                    datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

                                    datatable.getDefaultCell().setBackgroundColor(new Color(255, 255, 255));
   
                                    datatable.addCell(new Paragraph(stripHtml(ReportFieldHelper.showLabelField(field,
                                            report, fieldContent, false, -1, "text",null)), fontChineseSection));
                                }
                            }
                        }

                        sectionPDF.add(datatable);
                    } else if ("04".equals(sectionType)) {// 04---特定的形式，是一些动态生成的数据
                        /*if (section.getTableName().trim().equals("report/cashflowAnalysisList.jsp")) { // automobileReport
                            PdfPTable datatable = AutomobileReportUtil.generateCell(sectionContentMap);
                            sectionPDF.add(datatable);
                        }*/
                    } else {
                        // 定义表格的单元格宽度
                        int WidthE[] = {3, 6, 3, 6};
                        PdfPTable datatable = new PdfPTable(4);
                        datatable.setWidths(WidthE);
                        datatable.setWidthPercentage(100); // 表格的宽度百分比
                        datatable.setSpacingBefore(5);
                        // datatable.getDefaultCell().setPaddingTop(1);
                        datatable.getDefaultCell().setPadding(2);
                        datatable.getDefaultCell().setBorderWidth(1);
                        datatable.getDefaultCell().setBackgroundColor(new Color(239, 239, 239));
                        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

                        /**
                         * Table table = new Table(4); // 设置表格的格式 table.setWidth(100); table.setWidths(WidthE);
                         * table.setBorder(1); table.setPadding(1); table.setTop(20); table.setConvert2pdfptable(true);
                         * table.setTop(10); //table.set table.setSpacing(0); //table.setDefaultHorizontalAlignment(1);
                         **/

                        count = 0;
                        Collection fieldContentList = (ArrayList) sectionContentMap.get(section.getSectionId());
                        HashMap fieldContentMap = null;
                        if (fieldContentList == null || fieldContentList.size() == 0) {
                            fieldContentMap = new HashMap();
                        } else {
                            fieldContentMap = (HashMap) fieldContentList.iterator().next();
                        }
                        Cell cell = null;
                        while (it.hasNext()) {                        	
                            ReportSectionFieldVO field = (ReportSectionFieldVO) it.next();
                            //20150303 begin: hide the hidden fields
                        	if(field.getFieldComments().equalsIgnoreCase("hidden")){
                        		continue;
                        	}
                        	//20150303 end
                            ReportSectionFieldVO nextField = it.hasNext() && (field.getIsSingleRow() != 1) ? (ReportSectionFieldVO) it
                                    .next()
                                    : null;

                            // Cell cell1 = new Cell(new Chunk(field.getFieldLabel(),
                            // new com.lowagie.text.Font(bfChinese, 10,
                            // com.lowagie.text.Font.BOLD)));
                            // cell1.setBackgroundColor(new Color(239,239,239));
                            // cell1.setHorizontalAlignment(2);
                            // table.addCell(cell1);
                            datatable.getDefaultCell().setBackgroundColor(new Color(239, 239, 239));
                            datatable.getDefaultCell().setHorizontalAlignment(2);
                            datatable.addCell(new Paragraph(field.getFieldLabel(), fontChineseSection));

                            PdfPCell cell1 = new PdfPCell(new Paragraph(stripHtml(ReportFieldHelper.showLabelField(field,
                                    report, fieldContentMap, false, -1, "text",null)), fontChineseSection));
                            if (field.getIsSingleRow() == 1 || nextField != null && nextField.getIsSingleRow() == 1) {
                                cell1.setColspan(3);
                                count++;
                            }
                            // Cell cell2 = new Cell(new
                            // Chunk(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,"02","01"),
                            // new com.lowagie.text.Font(bfChinese, 10,
                            // com.lowagie.text.Font.BOLD)));
                            // cell2.setHorizontalAlignment(3);
                            // table.addCell(cell2);
                            datatable.getDefaultCell().setHorizontalAlignment(3);
                            datatable.getDefaultCell().setBackgroundColor(new Color(255, 255, 255));
                            // datatable.addCell(new
                            // Paragraph(FieldControlHelper.displayFieldInfo(section.getSectionType(),field,fieldContentMap,"02","01"),fontChineseSection));
                            // datatable.addCell(new
                            // Paragraph(ReportFieldHelper.showDisplayField(section.getSectionType(),field,fieldContentMap),fontChineseSection));
                            datatable.addCell(cell1);
                            count++;

                            if (nextField != null) {
                                datatable.getDefaultCell().setBackgroundColor(new Color(239, 239, 239));
                                datatable.getDefaultCell().setHorizontalAlignment(2);
                                datatable.addCell(new Paragraph(nextField.getFieldLabel(), fontChineseSection));

                                PdfPCell cell2 = new PdfPCell(new Paragraph(stripHtml(ReportFieldHelper.showLabelField(
                                        nextField, report, fieldContentMap, false, -1, "text",null)), fontChineseSection));
                                if (nextField != null && nextField.getIsSingleRow() == 1) {
                                    cell2.setColspan(3);
                                    count++;
                                }
                                datatable.getDefaultCell().setHorizontalAlignment(3);
                                datatable.getDefaultCell().setBackgroundColor(new Color(255, 255, 255));
                                datatable.addCell(cell2);
                                count++;
                            }
                        }
                        if (count % 2 == 1) {
                            datatable.getDefaultCell().setBackgroundColor(new Color(239, 239, 239));
                            datatable.getDefaultCell().setHorizontalAlignment(2);
                            datatable.addCell(new Paragraph("", fontChineseSection));

                            datatable.getDefaultCell().setHorizontalAlignment(3);
                            datatable.getDefaultCell().setBackgroundColor(new Color(255, 255, 255));
                            datatable.addCell(new Paragraph(""));
                        }

                        sectionPDF.add(datatable);
                    }
                }

                doc.add(chapter1);

                // add process trace
                Paragraph sectionTitle = new Paragraph("Processing Trail:");
                doc.add(sectionTitle);

                PdfPTable datatable = new PdfPTable(6);
                datatable.setWidthPercentage(100); // 表格的宽度百分比
                datatable.setSpacingBefore(5);
                // datatable.getDefaultCell().setPaddingTop(1);
                datatable.getDefaultCell().setPadding(2);
                datatable.getDefaultCell().setBorderWidth(1);
                datatable.getDefaultCell().setBackgroundColor(new Color(239, 239, 239));
                datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                String[] heading = {"Processed Node", "Processed By", "Processed Date", "Processed Type",
                        "Processed Remark", "Attachment"};
                for (int i = 0; i < 6; i++) {
                    // 添加表头元素
                    datatable.addCell(new Paragraph(heading[i], fontChineseSection));
                }
                if (traceList != null && traceList.size() > 0) {
                    Iterator traceIt = traceList.iterator();
                    while (traceIt.hasNext()) {
                        WorkFlowProcessTraceVO traceVo = (WorkFlowProcessTraceVO) traceIt.next();
                        datatable.getDefaultCell().setBackgroundColor(new Color(255, 255, 255));
                        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                        datatable.addCell(new Paragraph(traceVo.getNodeName(), fontChineseSection));
                        datatable.addCell(new Paragraph(StaffTeamHelper.getInstance().getStaffNameByCode(
                                traceVo.getHandleStaffCode()), fontChineseSection));
                        if (traceVo.getHandleDateStr() != null && !"".equals(traceVo.getHandleDateStr())) {
                            datatable.addCell(new Paragraph(StringUtil.getDateStr(StringUtil.stringToDate(traceVo
                                    .getHandleDateStr(), "yyyy-MM-dd HH:mm:ss"), "MM/dd/yyyy HH:mm:ss"),
                                    fontChineseSection));
                        } else {
                            datatable.addCell(new Paragraph("", fontChineseSection));
                        }
                        if ( "05".equals(traceVo.getHandleType())) {
                        	datatable.addCell(new Paragraph("Complete", fontChineseSection));
                        } else {
                        	datatable.addCell(new Paragraph(traceVo.getHandleTypeName(), fontChineseSection));
                        }
                        datatable.addCell(new Paragraph(traceVo.getHandleComments(), fontChineseSection));
                        if (traceVo.getFilePathName() != null && !"".equals(traceVo.getFilePathName())) {
                            String fileName = traceVo.getFilePathName().substring(
                                    traceVo.getFilePathName().lastIndexOf("/") + 1);
                            datatable.addCell(new Paragraph(fileName, fontChineseSection));
                        } else {
                            datatable.addCell(new Paragraph("", fontChineseSection));
                        }
                    }
                }
                doc.add(datatable);
            }
        } catch (DocumentException dex) {
            baosPDF.reset();
            throw dex;
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            if (doc != null) {
                doc.close();
            }
            if (docWriter != null) {
                docWriter.close();
            }
        }

        if (baosPDF.size() < 1) {
            throw new DocumentException("document has " + baosPDF.size() + " bytes");
        }
        return baosPDF;
    }
    
    
    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.+?>", "");
        // 还原HTML
        content = StringUtil.unFormatHtml(content);
        return content;
    }
    
    
    private static BaseFont getBaseFont() throws DocumentException, Exception {
        String defaultFontEncoding = "STSong-Light;UniGB-UCS2-H";
        String fontEncoding = ParamConfigHelper.getInstance().getParamValue("pdf_export_font");
        if (fontEncoding == null || ("").equals(fontEncoding)) {
            fontEncoding = defaultFontEncoding;
        }
        String[] fontEncodingSplit = fontEncoding.split(";");
        try {
            BaseFont bfChinese = BaseFont.createFont(fontEncodingSplit[0], fontEncodingSplit[1], BaseFont.NOT_EMBEDDED);
            return bfChinese;
        } catch (DocumentException dex) {
            throw dex;
        } catch (Exception e) {
            throw e;
        }
    }

}
