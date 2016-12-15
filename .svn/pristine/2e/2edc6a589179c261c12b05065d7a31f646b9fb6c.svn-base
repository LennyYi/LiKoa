package com.aiait.eflow.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;;

public class AutomobileFormUtil {
   
	public static PdfPTable generateCell(HashMap sectionContentMap)throws Exception{
        //中文字体   
        BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED); 

		 Font fontChineseSection = new Font(bfChinese, 10, Font.HELVETICA, new Color(0, 0, 0));
		 
		double B14 = 0 , F14 = 0, B15 = 0, F15=0 , B16=0, F16=0, F17 = 0,C20=0, C21=0, C22=0;
		
		Collection fieldContentList = (ArrayList)sectionContentMap.get("04");

		HashMap fieldContentMap = null;
        if(fieldContentList==null || fieldContentList.size()==0){
      	  fieldContentMap = new HashMap();
        }else{
      	  fieldContentMap = (HashMap)fieldContentList.iterator().next();
        }
        //System.out.println(fieldContentMap.get("FIELD_04_3"));
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_LEASING_FEE).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_LEASING_FEE).toUpperCase()))){
			B14 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_LEASING_FEE).toUpperCase()));
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_COST).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_COST).toUpperCase()))){
			F14 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_COST).toUpperCase()));
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_RENTAL).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_RENTAL).toUpperCase()))){
			B15 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_RENTAL).toUpperCase()));
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_PURCHASE).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_PURCHASE).toUpperCase()))){
			F15 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_EXPENSES_PURCHASE).toUpperCase()));
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_RENTAL).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_RENTAL).toUpperCase()))){
			B16 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_RENTAL).toUpperCase()));
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_INSURANCE).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_INSURANCE).toUpperCase()))){
			F16 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_INSURANCE).toUpperCase()));
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_PURCHASE).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_PURCHASE).toUpperCase()))){
			F17 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_SALARY_PURCHASE).toUpperCase()));
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C20).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C20).toUpperCase()))){
			C20 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C20).toUpperCase()))/100;
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C21).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C21).toUpperCase()))){
			C21 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C21).toUpperCase()))/100;
		}
		if(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C22).toUpperCase())!=null && !"".equals(fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C22).toUpperCase()))){
			C22 = Double.parseDouble((String)fieldContentMap.get(ParamConfigHelper.getInstance().getParamValue(CommonName.AUTOMOBILE_C22).toUpperCase()))/100;
		}
		
		PdfPTable datatable = new PdfPTable(8);
		datatable.setWidthPercentage(100); // 表格的宽度百分比 
		datatable.setSpacingBefore(5);
		//datatable.set
		//datatable.getDefaultCell().setPaddingTop(1);
        datatable.getDefaultCell().setPadding(2);   
        datatable.getDefaultCell().setBorderWidth(1); 
        datatable.getDefaultCell().setBackgroundColor(new Color(239,239,239));
        datatable.getDefaultCell().setHorizontalAlignment(   
                Element.ALIGN_CENTER);   
        datatable.addCell(new Paragraph("Year",fontChineseSection));
        datatable.addCell(new Paragraph("Yr0",fontChineseSection));
        datatable.addCell(new Paragraph("Yr1",fontChineseSection));
        datatable.addCell(new Paragraph("Yr2",fontChineseSection));
        datatable.addCell(new Paragraph("Yr3",fontChineseSection));
        datatable.addCell(new Paragraph("Yr4",fontChineseSection));
        datatable.addCell(new Paragraph("Yr5",fontChineseSection));
        datatable.addCell(new Paragraph("Total",fontChineseSection));
        
        String[][] temp = new String[6][8];
        temp[0][0]="(A) Leasehold/ rental:";
        double c12=0,c13=0,c14=0,c15=0,c16=0,c17=0;
        c12 = B14*12;
        c13 = B14*12;
        c14 = B14*12*(1+C20);
        c15 = B14*12*(1+C20);
        c16 = B14*12*(1+C20)*(1+C20);
        c17 = c12 + c13 + c14 + c15 + c16;
        temp[1][0] = "- Rental";
        temp[1][1] = "-";
        temp[1][2] = ""+Math.rint(c12);
        temp[1][3] = ""+Math.rint(c13);
        temp[1][4] = ""+Math.rint(c14);
        temp[1][5] = ""+Math.rint(c15);
        temp[1][6] = ""+Math.rint(c16);
        temp[1][7] = ""+Math.rint(c17);
        double c22=0,c23=0,c24=0,c25=0,c26=0,c27=0;
        c22 = B15*12;
        c23 = B15*12;
        c24 = B15*12*(1+C20);
        c25 = B15*12*(1+C20);
        c26 = B15*12*(1+C20)*(1+C20);
        c27 = c22 + c23 + c24 + c25 + c26;
        temp[2][0] = "- Automobile Expenses";
        temp[2][1] = "-";
        temp[2][2] = ""+Math.rint(c22);
        temp[2][3] = ""+Math.rint(c23);
        temp[2][4] = ""+Math.rint(c24);
        temp[2][5] = ""+Math.rint(c25);
        temp[2][6] = ""+Math.rint(c26);
        temp[2][7] = ""+Math.rint(c27);
        double c32=0,c33=0,c34=0,c35=0,c36=0,c37=0;
        c32 = Math.rint(B16*12);
        c33 = Math.rint(B16*12*(1+C21));
        c34 = Math.rint(B16*12*(1+C21)*(1+C21));
        c35 = Math.rint(B16*12*(1+C21)*(1+C21)*(1+C21));
        c36 = Math.rint(B16*12*(1+C21)*(1+C21)*(1+C21)*(1+C21));
        c37 = Math.rint(c32 + c33 + c34 + c35 + c36);
        temp[3][0] = "- Salary for driver";
        temp[3][1] = "-";
        temp[3][2] = ""+Math.rint(c32);
        temp[3][3] = ""+Math.rint(c33);
        temp[3][4] = ""+Math.rint(c34);
        temp[3][5] = ""+Math.rint(c35);
        temp[3][6] = ""+Math.rint(c36);
        temp[3][7] = ""+Math.rint(c37);
        double c42=0,c43=0,c44=0,c45=0,c46=0,c47=0;
        c42 = Math.rint(c12+c22+c32);
        c43 = Math.rint(c13+c23+c33);
        c44 = Math.rint(c14+c24+c34);
        c45 = Math.rint(c15+c25+c35);
        c46 = Math.rint(c16+c26+c36);
        c47 = Math.rint(c17 + c27 + c37);
        temp[4][0] = "Total Cash Outlay (Nominal)";
        temp[4][1] = "-";
        temp[4][2] = ""+Math.rint(c42);
        temp[4][3] = ""+Math.rint(c43);
        temp[4][4] = ""+Math.rint(c44);
        temp[4][5] = ""+Math.rint(c45);
        temp[4][6] = ""+Math.rint(c46);
        temp[4][7] = ""+Math.rint(c47);
        double c52=0,c53=0,c54=0,c55=0,c56=0,c57=0;
        c52 = Math.rint(c42/(1+C20));
        c53 = Math.rint(c43/((1+C20)*(1+C20)));
        c54 = Math.rint(c44/((1+C20)*(1+C20)*(1+C20)));
        c55 = Math.rint(c45/((1+C20)*(1+C20)*(1+C20)*(1+C20)));
        c56 = Math.rint(c46/((1+C20)*(1+C20)*(1+C20)*(1+C20)*(1+C20)));
        c57 = c52+c53+c54+c55+c56;
        temp[5][0] = "Total Cash Outlay (NPV)";
        temp[5][1] = "-";
        temp[5][2] = ""+Math.rint(c52);
        temp[5][3] = ""+Math.rint(c53);
        temp[5][4] = ""+Math.rint(c54);
        temp[5][5] = ""+Math.rint(c55);
        temp[5][6] = ""+Math.rint(c56);
        temp[5][7] = ""+Math.rint(c57);

        PdfPCell cell = new PdfPCell(new Paragraph(temp[0][0]));
        cell.setColspan(8);
        cell.setBackgroundColor(new Color(255,255,255));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        datatable.addCell(cell);
        
        for(int row=1;row<6;row++){
        	for(int col=0;col<8;col++){
        		cell = new PdfPCell(new Paragraph(temp[row][col]));
        		if(!"-".equals(temp[row][col])){
        			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        		}else{
                  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        		}
        		if(col==0){
        		  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        		  cell.setBackgroundColor(new Color(239,239,239));
        		}else{
        		  cell.setBackgroundColor(new Color(255,255,255));
        		}
        		datatable.addCell(cell);
        	}
        }
        
        double d12=0,d17=0,d22=0,d23=0,d24=0,d25=0,d26=0,d27=0;
        d12 = F14;
  	    d17 = F14;
  	    d22 = F15 * 12;
  	    d23 = d22;
  	    d24 = F15*12*(1+C20);
  	    d25 = d24;
  	    d26 = F15*12*(1+C20)*(1+C20);
  	    d27 = d22 + d23 + d24 + d25 + d26;
        String[][] data = new String[8][8];
        data[0][0] = "(B) Purchase:";
        data[1][0] = "- Automobile Cost";
        data[1][1] = data[1][7] = ""+d12;
        data[1][2] = data[1][3] = data[1][4]=data[1][5] = data[1][6] = "-";
        data[2][0] = "- Automobile Expenses";
        data[2][1] = "-";
        data[2][2] = ""+Math.rint(d22);
        data[2][3] = ""+Math.rint(d23);
        data[2][4] = ""+Math.rint(d24);
        data[2][5] = ""+Math.rint(d25);
        data[2][6] = ""+Math.rint(d26);
        data[2][7] = ""+Math.rint(d27);
        double d32=0,d33=0,d34=0,d35=0,d36=0,d37=0;
        d32 = F14 * (0.01+C22*1);
        d33 = F14 * (0.01+C22*2);
        d34 = F14 * (0.01+C22*3);
        d35 = F14 * (0.01+C22*4);
        d36 = F14 * (0.01+C22*5);
        d37 = d32 + d33 + d34 + d35 + d36;
        data[3][0] = "- Automobile Maintenance";
        data[3][1] = "-";
        data[3][2] = ""+Math.rint(d32);
        data[3][3] = ""+Math.rint(d33);
        data[3][4] = ""+Math.rint(d34);
        data[3][5] = ""+Math.rint(d35);
        data[3][6] = ""+Math.rint(d36);
        data[3][7] = ""+Math.rint(d37);
        double d42=0,d43=0,d44=0,d45=0,d46=0,d47=0;
        d42 = d43 = d44 = d45 = d46 = F16;
        d47 = d42 + d43 + d44 + d45 + d46;
        data[4][0] = "- Insurance";
        data[4][1] = "-";
        data[4][2] = ""+Math.rint(d42);
        data[4][3] = ""+Math.rint(d43);
        data[4][4] = ""+Math.rint(d44);
        data[4][5] = ""+Math.rint(d45);
        data[4][6] = ""+Math.rint(d46);
        data[4][7] = ""+Math.rint(d47);        
        double d52=0,d53=0,d54=0,d55=0,d56=0,d57=0;
        d52 = F17 * 12;
        d53 = d52 * (1+C21);
        d54 = d53 * (1+C21);
        d55 = d54 * (1+C21);
        d56 = d55 * (1+C21);
        d57 = d52 + d53 + d54 + d55 + d56;
        data[5][0] = "- Salary for driver";
        data[5][1] = "-";
        data[5][2] = ""+Math.rint(d52);
        data[5][3] = ""+Math.rint(d53);
        data[5][4] = ""+Math.rint(d54);
        data[5][5] = ""+Math.rint(d55);
        data[5][6] = ""+Math.rint(d56);
        data[5][7] = ""+Math.rint(d57);
        double d61=0,d62=0,d63=0,d64=0,d65=0,d66=0,d67=0;
        d61 = d12;
        d62 = d22 + d32 + d42 + d52;
        d63 = d23 + d33 + d43 + d53;
        d64 = d24 + d34 + d44 + d54;
        d65 = d25 + d35 + d45 + d55;
        d66 = d26 + d36 + d46 + d56;
        d67 = d17 + d27 + d37 + d47 + d57;
        data[6][0] = "Total Cash Outlay (Nominal)";
        data[6][1] = ""+d61;
        data[6][2] = ""+Math.rint(d62);
        data[6][3] = ""+Math.rint(d63);
        data[6][4] = ""+Math.rint(d64);
        data[6][5] = ""+Math.rint(d65);
        data[6][6] = ""+Math.rint(d66);
        data[6][7] = ""+Math.rint(d67);  
        double d71=0,d72=0,d73=0,d74=0,d75=0,d76=0,d77=0;
        d71 = d61;
        d72 = Math.rint(d62/(1+C20));
        d73 = Math.rint(d63/((1+C20)*(1+C20)));
        d74 = Math.rint(d64/((1+C20)*(1+C20)*(1+C20)));
        d75 = Math.rint(d65/((1+C20)*(1+C20)*(1+C20)*(1+C20)));
        d76 = Math.rint(d66/((1+C20)*(1+C20)*(1+C20)*(1+C20)*(1+C20)));
        d77 = d71 + d72 + d73 + d74 + d75+d76;
        data[7][0] = "Total Cash Outlay (NPV)";
        data[7][1] = ""+d71;
        data[7][2] = ""+d72;
        data[7][3] = ""+d73;
        data[7][4] = ""+d74;
        data[7][5] = ""+d75;
        data[7][6] = ""+d76;
        data[7][7] = ""+d77;        
        
        cell = new PdfPCell(new Paragraph(data[0][0]));
        cell.setColspan(8);
        cell.setBackgroundColor(new Color(255,255,255));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        datatable.addCell(cell);
        for(int row=1;row<8;row++){
        	for(int col=0;col<8;col++){
        		cell = new PdfPCell(new Paragraph(data[row][col]));
        		if(!"-".equals(data[row][col])){
        			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        		}else{
                  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        		}
        		if(col==0){
        		  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        		  cell.setBackgroundColor(new Color(239,239,239));
        		}else{
        		  cell.setBackgroundColor(new Color(255,255,255));
        		}
        		datatable.addCell(cell);
        	}
        }
        cell = new PdfPCell(new Paragraph("Comparison of rental vs purchase:"));
        cell.setColspan(8);
        cell.setBackgroundColor(new Color(255,255,255));
        datatable.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("Net Cash Flow (Nominal)       " + Math.rint((c47- d67))));
        cell.setColspan(8);
        cell.setBackgroundColor(new Color(255,255,255));
        datatable.addCell(cell);
        
        String result = "Net Cash Flow (NPV)           " + Math.rint((c57- d77));
        if((c57- d77)>0){
        	result = result + "    Analysis indicates car purchase generates lesser cash outlay";
        }else{
        	result = result + "    Analysis indicates leasehold/rental generates lesser cash outlay";
        }
        
        cell = new PdfPCell(new Paragraph(result));
        cell.setColspan(8);
        cell.setBackgroundColor(new Color(255,255,255));
        datatable.addCell(cell);
        
        return datatable;
	}
	
}
