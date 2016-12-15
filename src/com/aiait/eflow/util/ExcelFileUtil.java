package com.aiait.eflow.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellValue;

import com.aiait.eflow.common.helper.CompanyHelper;
import com.aiait.eflow.housekeeping.vo.BankVO;
import com.aiait.eflow.housekeeping.vo.BuildingVO;
import com.aiait.eflow.housekeeping.vo.ContractVO;
import com.aiait.eflow.housekeeping.vo.FinanceBudgetVO;
import com.aiait.eflow.housekeeping.vo.FinanceCodeVO;
import com.aiait.eflow.housekeeping.vo.SupplierVO;

public class ExcelFileUtil {
	private POIFSFileSystem fileSystem;

	private HSSFWorkbook workbook;

	private HSSFSheet sheet;

	private HSSFRow row;

	private HSSFCell cell;

    private final DecimalFormat df = new DecimalFormat("00");

    private final char[] columnid="ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
    public static String CONST_SUNAC = "SUNAC";
    public static String CONST_T2 = "T2";
    public static String CONST_T6 = "T6";
    
	/**
	 * ��������Ԥ����ÿ�¸��µ�excel����
	 * excel���ݸ�ʽ
	 * �����кţ�     1             2               3                4             5           6             7            8          9         10    11       12               13                    14
	 * ������   company_code   company_name  department_code  department_name  account_dc category_id  category_name sub_cat_id sub_cat_name  year  month  ytd_actual  adjust_full_year_budget  ytd_budget
	 * @return
	 * @throws IOException
	 */
	public Collection parseMonthlyBudget()throws Exception{
		Collection list = new ArrayList();
		this.setSheet(1);
		int rows = this.getSheet().getPhysicalNumberOfRows();
		//�ӵ�2�п�ʼ
		if (rows >= 2) {
			//��������Ƿ���ȷ
		    HSSFRow rowline = this.getSheet().getRow(1);
		    if(rowline.getLastCellNum()<12){
		    	throw new Exception("Excel �ļ������ݸ�ʽ����ȷ���������ԣ�");
		    }
			for (int r = 2; r <= rows; r++) {
				if(this.getCellValue(1, r, 1)==null || "".equals(this.getCellValue(1, r, 1))){
					continue;
				}
				FinanceBudgetVO vo = new FinanceBudgetVO();
				
				if (this.getCellValue(1, r, 1) != null && !"".equals(this.getCellValue(1, r, 1))) {
                    vo.setOrgId(this.getCellValue(1, r, 1).trim());
                } else {
                    throw new Exception("Company Code ����Ϊ��");
                }
                if (this.getCellValue(1, r, 3) != null && !"".equals(this.getCellValue(1, r, 3))) {
                    vo.setDepartmentId("" + new Double(this.getCellValue(1, r, 3)).intValue());
                } else {
                    throw new Exception("Dept Code ����Ϊ��");
                }
                if (this.getCellValue(1, r, 5) != null) {
                    vo.setAccountDC(this.getCellValue(1, r, 5).trim());
                } else {
                    //throw new Exception("Account DC ����Ϊ��");
                }
                if (this.getCellValue(1, r, 6) != null) {
                    vo.setCategoryId(this.getCellValue(1, r, 6).trim());
                } else {
                    throw new Exception("Category Code ����Ϊ��");
                }
                if (this.getCellValue(1, r, 7) != null) {
                    vo.setCategoryName(this.getCellValue(1, r, 7).trim());
                } else {
                    //throw new Exception("Category ����Ϊ��");
                }
                if (this.getCellValue(1, r, 8) != null) {
                    vo.setSubCategoryId(this.getCellValue(1, r, 8).trim());
                } else {
                    throw new Exception("Detail Category Code ����Ϊ��");
                }
                if (this.getCellValue(1, r, 9) != null) {
                    vo.setSubCategoryName(this.getCellValue(1, r, 9).trim());
                } else {
                    //throw new Exception("Detail Category ����Ϊ��");
                }
                
                if (this.getCellValue(1, r, 10) != null && !"".equals(this.getCellValue(1, r, 10))
                        && !"N/A".equals(this.getCellValue(1, r, 10).trim())
                        && !"-".equals(this.getCellValue(1, r, 10).trim())) {
                    vo.setBudgetYear("" + (int) Double.parseDouble(this.getCellValue(1, r, 10)));
                } else {
                    vo.setBudgetYear("");
                }
                if (this.getCellValue(1, r, 11) != null && !"".equals(this.getCellValue(1, r, 11))
                        && !"N/A".equals(this.getCellValue(1, r, 11).trim())
                        && !"-".equals(this.getCellValue(1, r, 11).trim())) {
                    vo.setCurrentMonth("" + (int) Double.parseDouble(this.getCellValue(1, r, 11)));
                } else {
                    vo.setCurrentMonth("");
                }
				
				if ("".equals(this.getCellValue(1, r, 12)) || this.getCellValue(1, r, 12) == null
                        || "N/A".equals(this.getCellValue(1, r, 12).trim())
                        || "-".equals(this.getCellValue(1, r, 12).trim())) {
                    vo.setYtnActualExpense(0);
                } else {
                    vo.setYtnActualExpense(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(1, r, 12))));
                }
				
				// adjust_full_year_budget
                if ("".equals(this.getCellValue(1, r, 13)) || this.getCellValue(1, r, 13) == null
                        || "N/A".equals(this.getCellValue(1, r, 13).trim())
                        || "-".equals(this.getCellValue(1, r, 13).trim())) {
                    vo.setAdjustFullYearBudget(0);
                } else {
                    vo.setAdjustFullYearBudget(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(1, r, 13))));
                }

                // ytd_budget
                if ("".equals(this.getCellValue(1, r, 14)) || this.getCellValue(1, r, 14) == null
                        || "N/A".equals(this.getCellValue(1, r, 14).trim())
                        || "-".equals(this.getCellValue(1, r, 14).trim())) {
                    vo.setYtnBudget(0);
                } else {
                    vo.setYtnBudget(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(1, r, 14))));
                }

                list.add(vo);
			}
		}
		return list;
	}

	/**
	 * ��������Ԥ������ȵļƻ�excel���� excel�����ݸ�ʽ�ǣ�
	 * 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 
	 * Company_Code Company Department_Code Department Account_DC Category_Code Category Detail_Category_Code Detail_Category 
	 * Year Original_Full_year_budget Dec Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov
	 * 
	 * @param filePathAndName
	 * @return
	 */
	public Collection parseAnnuallyBudget() throws Exception {
		Collection list = new ArrayList();
		this.setSheet(1);
		int rows = this.getSheet().getPhysicalNumberOfRows();
		if (rows >= 3) {
			//��������Ƿ���ȷ
		    HSSFRow rowline = this.getSheet().getRow(2);
		    if(rowline.getLastCellNum()<11){
		    	throw new Exception("Excel �ļ������ݸ�ʽ����ȷ���������ԣ�");
		    }
			// �ӵ����п�ʼ����Ч����,row��������0��ʼ
			for (int r = 3; r <= rows; r++) {
				if(this.getCellValue(1, r, 1)==null || "".equals(this.getCellValue(1, r, 1))){
					continue;
				}
				
				FinanceBudgetVO vo = new FinanceBudgetVO();
				if(this.getCellValue(1, r, 1)!=null && !"".equals(this.getCellValue(1, r, 1))){
				  vo.setOrgId(this.getCellValue(1, r, 1).trim());
				}else{
					throw new Exception("Company Code ����Ϊ��");
				}
				if(this.getCellValue(1, r, 3)!=null && !"".equals(this.getCellValue(1, r, 3))){
				   vo.setDepartmentId(""+new Double(this.getCellValue(1, r, 3)).intValue());
				}else{
					throw new Exception("Dept Code ����Ϊ��");
				}
				if(this.getCellValue(1,r,5)!=null){
				  vo.setAccountDC(this.getCellValue(1,r,5).trim());
				}else{
					throw new Exception("Account DC ����Ϊ��");
				}
				if(this.getCellValue(1, r, 6)!=null){
				  vo.setCategoryId(this.getCellValue(1, r, 6).trim());
				}else{
					throw new Exception("Category Code ����Ϊ��");
				}
				if (this.getCellValue(1, r, 7) != null) {
                    vo.setCategoryName(this.getCellValue(1, r, 7).trim());
                } else {
                    throw new Exception("Category ����Ϊ��");
                }
				if (this.getCellValue(1, r, 8) != null) {
                    vo.setSubCategoryId(this.getCellValue(1, r, 8).trim());
                } else {
                    throw new Exception("Detail Category Code ����Ϊ��");
                }
				if (this.getCellValue(1, r, 9) != null) {
                    vo.setSubCategoryName(this.getCellValue(1, r, 9).trim());
                } else {
                    throw new Exception("Detail Category ����Ϊ��");
                }
				
				if(this.getCellValue(1, r, 10)!=null && !"".equals(this.getCellValue(1, r, 10)) && !"N/A".equals(this.getCellValue(1, r, 10).trim())
						&& !"-".equals(this.getCellValue(1, r, 10).trim())){
				   vo.setBudgetYear(""+(int)Double.parseDouble(this.getCellValue(1, r, 10)));
				}else{
				   vo.setBudgetYear("");
				}
				//System.out.println(this.getCellValue(1, r, 11));
				if (this.getCellValue(1, r, 11) == null || "".equals(this.getCellValue(1, r, 11).trim())
						 || "N/A".equals(this.getCellValue(1, r, 11).trim())
						|| "-".equals(this.getCellValue(1, r, 11).trim())) {
					vo.setFullYearBudget(0);
				} else {
					vo.setFullYearBudget(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 11))));
				}
				if ("".equals(this.getCellValue(1, r, 12).trim())
						|| this.getCellValue(1, r, 12) == null || "N/A".equals(this.getCellValue(1, r, 12).trim())
						|| "-".equals(this.getCellValue(1, r, 12).trim())) {
					vo.setOriginalBudget12(0);
				} else {
					vo.setOriginalBudget12(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 12))));
				}
				if ("".equals(this.getCellValue(1, r, 13).trim())
						|| this.getCellValue(1, r, 13) == null || "N/A".equals(this.getCellValue(1, r, 13).trim())
						|| "-".equals(this.getCellValue(1, r, 13).trim())) {
					vo.setOriginalBudget1(0);
				} else {
					vo.setOriginalBudget1(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 13))));
				}				
				if ("".equals(this.getCellValue(1, r, 14).trim())
						|| this.getCellValue(1, r, 14) == null || "N/A".equals(this.getCellValue(1, r, 14).trim())
						|| "-".equals(this.getCellValue(1, r, 14).trim())) {
					vo.setOriginalBudget2(0);
				} else {
					vo.setOriginalBudget2(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 14))));
				}
				if ("".equals(this.getCellValue(1, r, 15).trim())
						|| this.getCellValue(1, r, 15) == null || "N/A".equals(this.getCellValue(1, r, 15).trim())
						|| "-".equals(this.getCellValue(1, r, 15).trim())) {
					vo.setOriginalBudget3(0);
				} else {
					vo.setOriginalBudget3(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 15))));
				}				
				if ("".equals(this.getCellValue(1, r, 16).trim())
						|| this.getCellValue(1, r, 16) == null || "N/A".equals(this.getCellValue(1, r, 16).trim())
						|| "-".equals(this.getCellValue(1, r, 16).trim())) {
					vo.setOriginalBudget4(0);
				} else {
					vo.setOriginalBudget4(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 16))));
				}
				if ("".equals(this.getCellValue(1, r, 17).trim())
						|| this.getCellValue(1, r, 17) == null || "N/A".equals(this.getCellValue(1, r, 17).trim())
						|| "-".equals(this.getCellValue(1, r, 17).trim())) {
					vo.setOriginalBudget5(0);
				} else {
					vo.setOriginalBudget5(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 17))));
				}				
				if ("".equals(this.getCellValue(1, r, 18).trim())
						|| this.getCellValue(1, r, 18) == null || "N/A".equals(this.getCellValue(1, r, 18).trim())
						|| "-".equals(this.getCellValue(1, r, 18).trim())) {
					vo.setOriginalBudget6(0);
				} else {
					vo.setOriginalBudget6(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 18))));
				}		
				if ("".equals(this.getCellValue(1, r, 19).trim())
						|| this.getCellValue(1, r, 19) == null || "N/A".equals(this.getCellValue(1, r, 19).trim())
						|| "-".equals(this.getCellValue(1, r, 19).trim())) {
					vo.setOriginalBudget7(0);
				} else {
					vo.setOriginalBudget7(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 19))));
				}
				if ("".equals(this.getCellValue(1, r, 20).trim())
						|| this.getCellValue(1, r, 20) == null || "N/A".equals(this.getCellValue(1, r, 20).trim())
						|| "-".equals(this.getCellValue(1, r, 20).trim())) {
					vo.setOriginalBudget8(0);
				} else {
					vo.setOriginalBudget8(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 20))));
				}
				if ("".equals(this.getCellValue(1, r, 21).trim())
						|| this.getCellValue(1, r, 21) == null || "N/A".equals(this.getCellValue(1, r, 21).trim())
						|| "-".equals(this.getCellValue(1, r, 21).trim())) {
					vo.setOriginalBudget9(0);
				} else {
					vo.setOriginalBudget9(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 21))));
				}				
				if ("".equals(this.getCellValue(1, r, 22).trim())
						|| this.getCellValue(1, r, 22) == null || "N/A".equals(this.getCellValue(1, r, 22).trim())
						|| "-".equals(this.getCellValue(1, r, 22).trim())) {
					vo.setOriginalBudget10(0);
				} else {
					vo.setOriginalBudget10(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 22))));
				}				
				if ("".equals(this.getCellValue(1, r, 23).trim())
						|| this.getCellValue(1, r, 23) == null || "N/A".equals(this.getCellValue(1, r, 23).trim())
						|| "-".equals(this.getCellValue(1, r, 23).trim())) {
					vo.setOriginalBudget11(0);
				} else {
					vo.setOriginalBudget11(StringUtil.getLongValue(Double.parseDouble(this.getCellValue(
							1, r, 23))));
				}				
				list.add(vo);
			}
		}

		return list;
	}

	/**
	 * �������ֲ�������excel����
	 * excel���ݸ�ʽ
	 * �����кţ�     1             2
	 * ������       code          name
	 * @param  ������˾����
	 * @return
	 * @throws IOException
	 */
	public Collection parseFinanceCode(String orgId, String flg)throws Exception{
		Collection list = new ArrayList();
		this.setSheet(1);
		int rows = this.getSheet().getPhysicalNumberOfRows();
		//�ӵ�2�п�ʼ
		if (rows >= 2) {
			//��������Ƿ���ȷ
		    HSSFRow rowline = this.getSheet().getRow(1);
		    if(rowline.getLastCellNum()<2){
		    	throw new Exception("Excel �ļ������ݸ�ʽ����ȷ���������ԣ�");
		    }
			for (int r = 2; r <= rows; r++) {
				if(this.getCellValue(1, r, 1)==null || "".equals(this.getCellValue(1, r, 1))){
					continue;
				}
				FinanceCodeVO vo = new FinanceCodeVO();
				
				vo.setOrgId(orgId);
				if (this.getCellValue(1, r, 1) != null && !"".equals(this.getCellValue(1, r, 1))) {
                    vo.setCode(this.getCellValue(1, r, 1).trim());
                } else {
                    throw new Exception("Code ����Ϊ��");
                }
                if (this.getCellValue(1, r, 2) != null && !"".equals(this.getCellValue(1, r, 2))) {
                    vo.setName(this.getCellValue(1, r, 2).trim());
                } else {
                    throw new Exception("Name ����Ϊ��");
                }
                if (flg.equalsIgnoreCase(CONST_SUNAC)) {
                    vo.setT0(Integer.parseInt(this.getCellValue(1, r, 3)));
                    vo.setT1(Integer.parseInt(this.getCellValue(1, r, 4)));
                    vo.setT2(Integer.parseInt(this.getCellValue(1, r, 5)));
                    vo.setT3(Integer.parseInt(this.getCellValue(1, r, 6)));
                    vo.setT4(Integer.parseInt(this.getCellValue(1, r, 7)));
                    vo.setT5(Integer.parseInt(this.getCellValue(1, r, 8)));
                    vo.setT6(Integer.parseInt(this.getCellValue(1, r, 9)));
                }
                list.add(vo);
			}
		}
		return list;
	}

	/**
	 * �����ϴ�Building�����excel����
	 * excel���ݸ�ʽ
	 * �����кţ�     1			2			3			4			5			����
	 * ������       �ֹ�˾	��ҵ����	ʡ��	������	�տȫ��	�ʻ���Ϣ(�ʻ�����)	�ʻ���Ϣ(������)	�ʻ���Ϣ(�ʺ�)	
	 * 
	 * ��ͬ����	��ͬ����(From-To)	�������	������(����)	�������ڼ�(From-To)	�����	����	�¹����	����	Ѻ��(����)	Ѻ��(���)	Ѻ��(��ҵ)	��ͬ���	��ͬ�ܶ�
	 * @param  ������˾����
	 * @return
	 * @throws IOException
	 */
	public Collection parseBank(String org_id)throws Exception{
		Collection list = new ArrayList();
		this.setSheet(1);
		int rows = this.getSheet().getPhysicalNumberOfRows();
		//�ӵ�2�п�ʼ
		int r=0;
		int c=0;
		try {
			if (rows >= 2) {
				//��������Ƿ���ȷ
			    HSSFRow rowline = this.getSheet().getRow(1);
			    if(rowline.getLastCellNum()<7){
			    	throw new Exception("Excel �ļ������ݸ�ʽ����ȷ���������ԣ�");
			    }
				for (r = 2; r <= rows; r++) {
					if(this.getCellValue(1, r, 1)==null || "".equals(this.getCellValue(1, r, 1))){
						continue;
					}
					BankVO vo = new BankVO();
					c = 1;
					
					if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setBankCode(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("BankCode ����Ϊ��");
			        }

			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setCity(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("City ����Ϊ��");
			        }
					
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setBankName(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("BankName ����Ϊ��");
			        }

			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setAccountName(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("AccountName ����Ϊ��");
			        }

			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setAccountCode(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("AccountCode ����Ϊ��");
			        }
			        vo.setSunCode((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setType((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setOrgId(org_id);

			        list.add(vo);
				}
			}
		} catch (Exception e) {
			if(e instanceof NumberFormatException){
				throw new Exception("��"+r+"��"+columnid[c-2]+"�и�ʽ����");
			} else {
				throw e;
			}
		}
		return list;
	}
	/**
	 * �����ϴ�Building�����excel����
	 * excel���ݸ�ʽ
	 * �����кţ�     1			2			3			4			5			����
	 * ������       �ֹ�˾	��ҵ����	ʡ��	������	�տȫ��	�ʻ���Ϣ(�ʻ�����)	�ʻ���Ϣ(������)	�ʻ���Ϣ(�ʺ�)	
	 * 
	 * ��ͬ����	��ͬ����(From-To)	�������	������(����)	�������ڼ�(From-To)	�����	����	�¹����	����	Ѻ��(����)	Ѻ��(���)	Ѻ��(��ҵ)	��ͬ���	��ͬ�ܶ�
	 * @param  ������˾����
	 * @return
	 * @throws IOException
	 */
	public Collection parseBuilding(String org_id)throws Exception{
		Collection list = new ArrayList();
		this.setSheet(1);
		int rows = this.getSheet().getPhysicalNumberOfRows();
		//�ӵ�2�п�ʼ
		int r=0;
		int c=0;
		try {
			if (rows >= 2) {
				//��������Ƿ���ȷ
			    HSSFRow rowline = this.getSheet().getRow(1);
			    if(rowline.getLastCellNum()<18){
			    	throw new Exception("Excel �ļ������ݸ�ʽ����ȷ���������ԣ�");
			    }
				for (r = 2; r <= rows; r++) {
					if(this.getCellValue(1, r, 1)==null || "".equals(this.getCellValue(1, r, 1))){
						continue;
					}
					BuildingVO vo = new BuildingVO();
					c = 1;
					
					if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setCode(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("Code ����Ϊ��");
			        }
					
					c++;//�ڶ��С��ֹ�˾����Ҫ
					
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setName(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("Name ����Ϊ��");
			        }
			        
			        vo.setProvince((" "+this.getCellValue(1, r, c++)).trim());
			        
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setCity(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("City ����Ϊ��");
			        }
					if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setRenter(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("Renter ����Ϊ��");
			        }
			        vo.setAcc_name((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setAcc_bank((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setAcc_no((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setDuration((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setPeriod((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setArea(Double.parseDouble(this.getCellValue(1, r, c++)));
			        vo.setFree_month((int)Double.parseDouble(this.getCellValue(1, r, c++)));
			        vo.setFree_period((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setMonth_rent_fee(Double.parseDouble(this.getCellValue(1, r, c++)));
			        vo.setMonth_rent_curr((" "+this.getCellValue(1, r, c++)).trim().substring(0,3));
			        vo.setMonth_mang_fee(Double.parseDouble(this.getCellValue(1, r, c++)));
			        vo.setMonth_mang_curr((" "+this.getCellValue(1, r, c++)).trim().substring(0,3));
			        vo.setDepo_month((int)Double.parseDouble(this.getCellValue(1, r, c++)));
			        vo.setDepo_fee_rent(Double.parseDouble(this.getCellValue(1, r, c++)));
			        vo.setDepo_fee_prop(Double.parseDouble(this.getCellValue(1, r, c++)));
			        vo.setContract_no((" "+this.getCellValue(1, r, c++)).trim());
			        vo.setTot_amount(Double.parseDouble(this.getCellValue(1, r, c++)));
			        vo.setOrg_id(org_id);

			        list.add(vo);
				}
			}
		} catch (Exception e) {
			if(e instanceof NumberFormatException){
				throw new Exception("��"+r+"��"+columnid[c-2]+"�и�ʽ����");
			} else {
				throw e;
			}
		}
		return list;
	}

	/**
	 * �����ϴ��ĺ�ͬ����excel����
	 * excel���ݸ�ʽ
	 * �����кţ�     1            2          3            4           5         6       7        8       9   
	 * ������   contractNo    respStaff   respDept  contactTel contractName  sign1  sign2  sign3  content  
	 *  			����    
	 * 			amount  signDate  effPeriod    issueDate  signDoc  orgName  remark
	 * @return
	 * @throws Exception
	 */
	public Collection parseContract(String orgId)throws Exception{
		Collection list = new ArrayList();
		this.setSheet(1);
		int rows = this.getSheet().getPhysicalNumberOfRows();
		//�ӵ�2�п�ʼ
		int r=0;
		int c=0;
		try {
			if (rows >= 2) {
				//��������Ƿ���ȷ
			    HSSFRow rowline = this.getSheet().getRow(1);
			    if(rowline.getLastCellNum()<11){
			    	throw new Exception("Excel �ļ������ݸ�ʽ����ȷ���������ԣ�");
			    }
				for (r = 2; r <= rows; r++) {
					if(this.getCellValue(1, r, 1)==null || "".equals(this.getCellValue(1, r, 1))){
						continue;
					}
					ContractVO vo = new ContractVO();
					c = 1;
					
					if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setContractNo(new Double(this.getCellValue(1, r, c++)).intValue());
			        } else {
			            throw new Exception("Contract No. ����Ϊ��");
			        }
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setCity(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("City ����Ϊ��");
			        }
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setRespDept(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("Responsible Department ����Ϊ��");
			        }
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setRespStaff(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("Responsible Staff ����Ϊ��");
			        }
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setContractName(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("Contract Name ����Ϊ��");
			        }
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setSign2(this.getCellValue(1, r, c++).trim());
			        } else {
			            throw new Exception("Sign2 ����Ϊ��");
			        }
			        if (this.getCellValue(1, r, c) != null) {
			            vo.setSign3(this.getCellValue(1, r, c++).trim());
			        }                
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setContent(this.getCellValue(1, r, c++).trim());
			        }
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {	        	
			            vo.setAmount(Double.parseDouble(this.getCellValue(1, r, c++)));
			        } else {
			            throw new Exception("Amount ����Ϊ��");
			        }               
			        if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
			            vo.setEffPeriod(this.getCellValue(1, r, c++).trim()+" To "+this.getCellValue(1, r, c++).trim());
			        }
			        vo.setOrgName(CompanyHelper.getInstance().getOrgName(orgId));            

			        list.add(vo);
				}
			}
		} catch (Exception e) {
			if(e instanceof NumberFormatException){
				throw new Exception("��"+r+"��"+columnid[c-2]+"�и�ʽ����");				
			} else {
				throw e;
			}
		}
		return list;
	}
	
	/**
	 * �����ϴ��Ĺ�Ӧ��excel����
	 * excel���ݸ�ʽ
	 * �����кţ�     1       2          3            4         5        6          7        8       	9      		10   
	 * ������   	  
	 * @return
	 * @throws Exception
	 */
	public Collection parseSupplier(String orgId)throws Exception{
		Collection list = new ArrayList();
		for(int i=0;i<this.workbook.getNumberOfSheets();i++){
			this.setSheet(i+1);
			int rows = this.getSheet().getPhysicalNumberOfRows();
			//�ӵ�2�п�ʼ
			int r=0;
			int c=0;
			try {
				if (rows >= 2) {
					//��������Ƿ���ȷ
				    HSSFRow rowline = this.getSheet().getRow(1);
				    if(rowline.getLastCellNum()<15){
				    	throw new Exception("Excel �ļ������ݸ�ʽ����ȷ���������ԣ�");
				    }
					for (r = 2; r <= rows; r++) {
						if(this.getCellValue(1, r, 1)==null || "".equals(this.getCellValue(1, r, 1))){
							continue;
						}
						SupplierVO vo = new SupplierVO();
						c = 1;
						
						if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c)) 
								&& (Short.parseShort(this.getCellValue(1, r, c)))>0 && (Short.parseShort(this.getCellValue(1, r, c)))<=9) {
		                    vo.setCertClass(this.getCellValue(1, r, c++).trim());
		                } else {
		                    throw new Exception("��֤���� ����Ϊ�գ���ֻ����1~9��line#"+r+" : "+this.getCellValue(1, r, 1));
		                }
						if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
		                    vo.setCode(this.getCellValue(1, r, c++).trim());
		                } else {
		                    throw new Exception("Code ����Ϊ��");
		                }
						c++;//��3�С��ֹ�˾����Ҫ
						
		                if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
		                    vo.setType(this.getCellValue(1, r, c++).trim());
		                } else {
		                    throw new Exception("Type ����Ϊ��");
		                }
		                if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
		                    vo.setNameC(this.getCellValue(1, r, c++).trim());
		                } else {
		                    throw new Exception("Name ����Ϊ��");
		                }
	
	                    vo.setNameE(this.getCellValue(1, r, c++).trim());
	                    
		                if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
		                    vo.setProduct(this.getCellValue(1, r, c++).trim());
		                } else {
		                    throw new Exception("Product ����Ϊ��");
		                }
		                if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
		                	vo.setEffDate(this.getCellDateValue(1, r, c++));
		                }
	                	vo.setContacter((" "+this.getCellValue(1, r, c++)).trim());
	                    vo.setTel((" "+this.getCellValue(1, r, c++)).trim());
	                    vo.setAddressC((" "+this.getCellValue(1, r, c++)).trim());
	                    vo.setAddressE((" "+this.getCellValue(1, r, c++)).trim());
	                    vo.setProvince((" "+this.getCellValue(1, r, c++)).trim());
	                    vo.setCity((" "+this.getCellValue(1, r, c++)).trim());
	                    vo.setBank((" "+this.getCellValue(1, r, c++)).trim());
	                    vo.setBankAccount((" "+this.getCellValue(1, r, c++)).trim());
	
		                if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
		                	vo.setTeamName((" "+this.getCellValue(1, r, c++)).trim());
		                }
		                if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
		                	vo.setTeamContacter((" "+this.getCellValue(1, r, c++)).trim());
		                }
		                if (this.getCellValue(1, r, c) != null && !"".equals(this.getCellValue(1, r, c))) {
		                	vo.setStatus((" "+this.getCellValue(1, r, c++)).trim());
		                } else {
		                	vo.setStatus("A");
		                }
				        //vo.setOrgId(orgId);
		                vo.setOrgId("Z07002");//AIA SH
		
		                list.add(vo);
					}
				}
			} catch (Exception e) {
				if(e instanceof NumberFormatException){
					throw new Exception("��"+r+"��"+columnid[c-2]+"�и�ʽ����");
				} else {
					throw e;
				}
			}
		}
		
		return list;
	}
	/**
	 * 
	 * @param xlsPath
	 */
	public ExcelFileUtil(String xlsPath) throws Exception {
		this.fileSystem = new POIFSFileSystem(new FileInputStream(xlsPath));
		this.workbook = new HSSFWorkbook(fileSystem);
	}
	/**
	 * @since CHO epayment 
	 */
	public ExcelFileUtil() throws Exception {
		this.workbook = new HSSFWorkbook();
		this.workbook.createSheet();
		appendRow(1);
	}
	
	/**
	 * ����һ�У���255��cell
	 * 
	 * @param sheet
	 * @return
	 */
	public void appendRow(int sheetnum){
		int maxRow = this.workbook.getSheetAt(sheetnum-1).getPhysicalNumberOfRows();
		this.workbook.getSheetAt(sheetnum-1).createRow(maxRow);
		for(short i=0;i<255;i++)
			this.workbook.getSheetAt(sheetnum-1).getRow(maxRow).createCell(i);
		return;
	}


	/**
	 * ��ȡ��Ԫ������,��String��ʽ����
	 * 
	 * @param sheet
	 * @param row
	 * @param cell
	 * @return
	 */
	private String getCellValue(int sheet, int row, int cell) {
		setSheet(sheet);
		setRow(row);
		setCell(cell);
		if(this.getCell()==null) return "";
		if (this.getCell().getCellType() == HSSFCell.CELL_TYPE_STRING) {
			return this.getCell().getStringCellValue();
		} else if (this.getCell().getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			if(new BigDecimal(getCell().getNumericCellValue()).divideToIntegralValue(BigDecimal.ONE)
					.equals(new BigDecimal(getCell().getNumericCellValue()))){
				return df.format(this.getCell().getNumericCellValue());
			} else {
				return ""+this.getCell().getNumericCellValue();
			}
		} else if (this.getCell().getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
			HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(this.getSheet(), this.workbook);
			evaluator.setCurrentRow(this.getRow());
			CellValue cellValue = evaluator.evaluate(this.getCell());//change by Colin Wang for import the poi-3.9.jar to replace the poi-3.1.jar, by this change have not been tested 
			return cellValue.getStringValue();
		}
		else {
			return  this.getCell().getStringCellValue();
		}
	}

	/**
	 * ��ȡ��Ԫ������,��Date String��ʽ����
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
		if(this.getCell()==null) return "";
		return dateFormat.format(this.getCell().getDateCellValue());
		
	}
	
	// �����ļ�
	public void Export(String xlsPath, int sheet, int row, int cell,
			String value) throws Exception {
		FileOutputStream fileOut = new FileOutputStream(xlsPath);

		this.setSheet(sheet);
		this.setRow(row);
		this.setCell(cell);
		this.getCell().setCellValue(value);

		this.workbook.write(fileOut);

		fileOut.close();

	}
	/** �����ļ�
	 * 
	 * @param xlsPath
	 * @throws Exception
	 */
	public void export(String xlsPath) throws Exception {
		FileOutputStream fileOut = new FileOutputStream(xlsPath);

		this.workbook.write(fileOut);

		fileOut.close();
	}

	/** д��һ��
	 * 
	 * @param xlsPath
	 * @throws Exception
	 */
	public void writecell(int row, int cell, int type, String value) throws Exception {
		this.setRow(row);
		this.setCell(cell);
		this.getCell().setCellType(type);
		this.getCell().setCellValue(value==null?"":value);
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

	//private void setSheet(int sheet) {
	public void setSheet(int sheet) {
		// Ĭ���Ǵ�0��ʼ��,��������n-1ת����,��1��ʼ,���ú�����
		this.sheet = this.workbook.getSheetAt(sheet - 1);
	}

	public static void main(String[] args) {
		try {		
			ExcelFileUtil excelOper = new ExcelFileUtil(
					"D:/ts_eform/SH/data/Budget/Budget_uploading_list_year_CHO.xls");
			String tmp = excelOper.getCellValue(1, 3, 1);
			System.out.println(excelOper.getCellValue(1, 3, 1));
			System.out.println(excelOper.getCellValue(1, 3, 2));
			System.out.println(excelOper.getCellValue(1, 3, 3));
			System.out.println(excelOper.getCellValue(1, 3, 4));
			System.out.println(excelOper.getCellValue(1, 3, 5));
			System.out.println(excelOper.getCellValue(1, 3, 6));
			System.out.println(excelOper.getCellValue(1, 3, 7));
			//System.out.println(excelOper.getCellValue(1, 3, 8));
			//System.out.println(excelOper.getCellValue(1, 3, 9));
			//System.out.println(excelOper.getCellValue(1, 3, 10));
			//System.out.println(excelOper.getCellValue(1, 3, 11));
			System.out.println(excelOper.getCellValue(1, 3, 12));
			// excelOper.Export("��Ʒ��Ŀ����ģ��_����.xls",1,1,1,"ABCDE");

		} catch (Exception e) {
			e.printStackTrace();
		}
      
		/**
		String s = "123.0";
		
		//Integer i = new Integer(s);
		//double d = Double.parseDouble(s);
		Double d = new Double(s);
		
		System.out.print(d.intValue());
		**/
	}

}
