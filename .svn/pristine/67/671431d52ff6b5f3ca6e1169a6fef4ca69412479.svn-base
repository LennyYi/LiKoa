package com.aiait.eflow.report.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.*;

import com.aiait.eflow.report.vo.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class EPaymentReportPDFUtil {

    public static ByteArrayOutputStream exportStaffPayment(Rectangle pageSize, Collection list, double totalAmount)
            throws DocumentException {
        if (pageSize == null)
            pageSize = PageSize.A4;
        Document doc = new Document(pageSize, 10, 10, 10, 10);
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfWriter docWriter = null;

        try {
            // 中文字体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            docWriter = PdfWriter.getInstance(doc, baosPDF);
            doc.addCreationDate();
            doc.addTitle("Staff Payment Report");

            doc.open();
            Font fontChineseSection = new Font(bfChinese, 10, Font.HELVETICA, new Color(0, 0, 0));

            if (list == null || list.size() == 0) {
                Paragraph title11 = new Paragraph("The form has not any field !");
                title11.setFont(fontChineseSection);
                doc.add(title11);
                return baosPDF;
            }

            PdfPTable datatable = new PdfPTable(7);
            datatable.setWidthPercentage(100); // 表格的宽度百分比
            datatable.setSpacingBefore(6);

            datatable.getDefaultCell().setPadding(4);
            datatable.getDefaultCell().setBorderWidth(1);
            datatable.getDefaultCell().setBackgroundColor(new Color(225, 225, 225));
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            // Column label
            datatable.addCell(new Paragraph("所属公司", fontChineseSection));
            datatable.addCell(new Paragraph("表单流水号", fontChineseSection));
            datatable.addCell(new Paragraph("客户名称", fontChineseSection));
            datatable.addCell(new Paragraph("开户银行", fontChineseSection));
            datatable.addCell(new Paragraph("银行账号", fontChineseSection));
            datatable.addCell(new Paragraph("金额", fontChineseSection));
            datatable.addCell(new Paragraph("备注", fontChineseSection));

            datatable.getDefaultCell().setPadding(2);
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            datatable.getDefaultCell().setBackgroundColor(new Color(255, 255, 255));

            // List data
            DecimalFormat df = new DecimalFormat("#.00");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                StaffEbankVO staffEbank = (StaffEbankVO) it.next();
                datatable.addCell(new Paragraph(staffEbank.getOrgName(), fontChineseSection));
                datatable.addCell(new Paragraph(staffEbank.getRequestNo(), fontChineseSection));
                datatable.addCell(new Paragraph(staffEbank.getRequestStaffCNName(), fontChineseSection));
                datatable.addCell(new Paragraph(staffEbank.getPayeeBank(), fontChineseSection));
                datatable.addCell(new Paragraph(staffEbank.getPayeeAccount(), fontChineseSection));
                Paragraph cellAmount = new Paragraph(df.format(staffEbank.getAmount()), fontChineseSection);
                datatable.addCell(cellAmount);
                datatable.addCell(new Paragraph(staffEbank.getRemark(), fontChineseSection));
            }
            doc.add(datatable);
            Paragraph total = new Paragraph("总计 " + list.size() + " 条记录, 金额: " + df.format(totalAmount),
                    fontChineseSection);
            doc.add(total);
        } catch (DocumentException dex) {
            baosPDF.reset();
            throw dex;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    public static ByteArrayOutputStream exportPayment(Rectangle pageSize, Collection list) throws DocumentException {
        if (pageSize == null)
            pageSize = PageSize.A4;
        Document doc = new Document(pageSize, 10, 10, 10, 10);
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfWriter docWriter = null;

        try {
            // 中文字体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            docWriter = PdfWriter.getInstance(doc, baosPDF);
            doc.addCreationDate();
            doc.addTitle("Staff Payment Report");

            doc.open();
            Font fontChineseSection = new Font(bfChinese, 10, Font.HELVETICA, new Color(0, 0, 0));

            if (list == null || list.size() == 0) {
                Paragraph title11 = new Paragraph("The form has not any field !");
                title11.setFont(fontChineseSection);
                doc.add(title11);
                return baosPDF;
            }

            PdfPTable datatable = new PdfPTable(12);
            datatable.setWidthPercentage(100); // 表格的宽度百分比
            datatable.setSpacingBefore(5);

            datatable.getDefaultCell().setPadding(4);
            datatable.getDefaultCell().setBorderWidth(1);
            datatable.getDefaultCell().setBackgroundColor(new Color(225, 225, 225));
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

            // Column label
            datatable.addCell(new Paragraph("表单流水号", fontChineseSection));
            datatable.addCell(new Paragraph("付款帐号开户行", fontChineseSection));
            datatable.addCell(new Paragraph("付款帐号", fontChineseSection));
            datatable.addCell(new Paragraph("付款帐号名称", fontChineseSection));
            datatable.addCell(new Paragraph("收款帐号开户行", fontChineseSection));
            datatable.addCell(new Paragraph("省份名", fontChineseSection));
            datatable.addCell(new Paragraph("地市名", fontChineseSection));
            datatable.addCell(new Paragraph("收款帐号", fontChineseSection));
            datatable.addCell(new Paragraph("收款帐号名称", fontChineseSection));
            datatable.addCell(new Paragraph("金额（分）", fontChineseSection));
            datatable.addCell(new Paragraph("汇款用途", fontChineseSection));
            datatable.addCell(new Paragraph("备注", fontChineseSection));

            datatable.getDefaultCell().setPadding(2);
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            datatable.getDefaultCell().setBackgroundColor(new Color(255, 255, 255));

            // List data
            DecimalFormat df = new DecimalFormat("#.00");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                PaymentVO payment = (PaymentVO) it.next();
                String prov = "";
                String city = "";
                if (payment.getPayeeProvince() != null) {
                    String[] tmp = payment.getPayeeProvince().split("_");
                    if (tmp.length > 1) {
                        prov = tmp[0];
                        city = tmp[tmp.length - 1];
                    } else {
                        prov = payment.getPayeeProvince();
                        city = payment.getPayeeCity();
                    }
                }
                datatable.addCell(new Paragraph(payment.getRequestNo(), fontChineseSection));
                datatable.addCell(new Paragraph(payment.getPayBank(), fontChineseSection));
                datatable.addCell(new Paragraph(payment.getPayAccount(), fontChineseSection));
                datatable.addCell(new Paragraph(payment.getPayName(), fontChineseSection));
                datatable.addCell(new Paragraph(payment.getPayeeBank(), fontChineseSection));
                datatable.addCell(new Paragraph(prov, fontChineseSection));
                datatable.addCell(new Paragraph(city, fontChineseSection));
                datatable.addCell(new Paragraph(payment.getPayeeAccount(), fontChineseSection));
                datatable.addCell(new Paragraph(payment.getPayeeName(), fontChineseSection));
                Paragraph cellAmount = new Paragraph(df.format(payment.getAmount()), fontChineseSection);
                datatable.addCell(cellAmount);
                datatable.addCell(new Paragraph(payment.getPurpose(), fontChineseSection));
                datatable.addCell(new Paragraph(payment.getRemark(), fontChineseSection));
            }
            doc.add(datatable);
            Paragraph total = new Paragraph("总计 " + list.size() + " 条记录", fontChineseSection);
            doc.add(total);
        } catch (DocumentException dex) {
            baosPDF.reset();
            throw dex;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

}
