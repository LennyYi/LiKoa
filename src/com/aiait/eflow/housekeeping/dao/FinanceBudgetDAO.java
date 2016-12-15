package com.aiait.eflow.housekeeping.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.aiait.eflow.housekeeping.vo.FinanceBudgetVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.vo.BaseVO;

/**
 * 每个公司每个部门的每个account部门的每个类别都只会存在一条纪录
 * org_id department_id acccount_dc category_id
 * @author asnpgj3
 *
 */
public class FinanceBudgetDAO extends BaseDAOImpl {
	
	public FinanceBudgetDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	private FinanceBudgetVO convertMap(HashMap map){
		if(map==null) return null;
		FinanceBudgetVO vo = new FinanceBudgetVO();
		vo.setOrgId(FieldUtil.convertSafeString(map,"ORG_ID"));
		vo.setDepartmentId(FieldUtil.convertSafeString(map,"DEPARTMENT_ID"));
		vo.setCategoryId(FieldUtil.convertSafeString(map,"CATEGORY_ID"));
		vo.setSubCategoryId(FieldUtil.convertSafeString(map,"SUB_CAT_ID"));
		vo.setCategoryName(FieldUtil.convertSafeString(map,"CATEGORY_NAME"));
		vo.setSubCategoryName(FieldUtil.convertSafeString(map,"SUB_CAT_NAME"));
		vo.setBudgetYear(FieldUtil.convertSafeString(map,"BUDGET_YEAR"));
		vo.setCurrentMonth(FieldUtil.convertSafeString(map,"CURRENT_MONTH"));
		vo.setAccountDC(FieldUtil.convertSafeString(map,"ACCOUNT_DC"));
		vo.setYtnActualExpense(FieldUtil.convertSafeDouble(map,"YTD_ACTUAL_EXPENSE",0));
		vo.setFullYearBudget(FieldUtil.convertSafeDouble(map,"FULL_YEAR_BUDGET",0));
		vo.setAdjustFullYearBudget(FieldUtil.convertSafeDouble(map, "ADJUST_FULL_YEAR_BUDGET", 0));
		vo.setYtnBudget(FieldUtil.convertSafeDouble(map,"YTD_BUDGET",0));
		vo.setBanlance(FieldUtil.convertSafeDouble(map,"BALANCE",0));
		vo.setOriginalBudget1(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_1",0));
		vo.setOriginalBudget2(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_2",0));
		vo.setOriginalBudget3(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_3",0));
		vo.setOriginalBudget4(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_4",0));
		vo.setOriginalBudget5(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_5",0));
		vo.setOriginalBudget6(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_6",0));
		vo.setOriginalBudget7(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_7",0));
		vo.setOriginalBudget8(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_8",0));
		vo.setOriginalBudget9(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_9",0));
		vo.setOriginalBudget10(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_10",0));
		vo.setOriginalBudget11(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_11",0));
		vo.setOriginalBudget12(FieldUtil.convertSafeDouble(map,"ORIGINAL_BUDGET_12",0));
		//vo.setBanlance(FieldUtil.convertSafeFloat(map,"BALANCE",0));
		return vo;
	}
	
	/**
	 * 检查指定的纪录是否已经存在
	 * @param conditionVo
	 * @return false --- 不存在； true ---- 已经存在
	 * @throws DAOException
	 */
	public boolean checkRecordExist(FinanceBudgetVO conditionVo) throws DAOException {
        StringBuffer sql = new StringBuffer("select * from teflow_finance_budget where 1=1 ");
        sql.append(" and org_id='").append(conditionVo.getOrgId()).append("' and department_id='").append(
                conditionVo.getDepartmentId()).append("' and category_id='").append(conditionVo.getCategoryId())
                .append("' and sub_cat_id='").append(conditionVo.getSubCategoryId()).append("' ");
        Collection list = dbManager.query(sql.toString());
        if (list == null || list.size() == 0)
            return false;
        return true;
    }
	
	public FinanceBudgetVO getVO(FinanceBudgetVO conditionVo)throws DAOException {
		StringBuffer sql = new StringBuffer("select * from teflow_finance_budget where 1=1 ");
		sql.append(" and org_id='").append(conditionVo.getOrgId()).append("' and department_id='")
		   .append(conditionVo.getDepartmentId()).append("' and category_id='")
		   .append(conditionVo.getCategoryId()).append("' and sub_cat_id='").append(conditionVo.getSubCategoryId())
		   .append("' ");
		//System.out.println(sql.toString());
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) return null;
		HashMap map = (HashMap)list.iterator().next();
		return convertMap(map);
	}
	
	public int getTotalRecordNum(PageVO page)throws DAOException{
		String sql  = "select count(*) from teflow_finance_budget where 1=1 ";
		sql = sql + makeQueryCondition(page.getParamMap());
		int result = dbManager.getRecordCount(sql);
		return result;
	}
	
	public Collection search(PageVO page)throws DAOException{
		String sql = "select * from teflow_finance_budget where 1=1 ";
		sql = sql + makeQueryCondition(page.getParamMap());
        Collection list = dbManager.query(sql,page.getPageSize(),page.getCurrentPage());
		if(list==null || list.size()==0){			
			return null;
		}
		Collection resultList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			FinanceBudgetVO vo = convertMap(map);
			resultList.add(vo);
		}
		return resultList;
	}
	
	private String makeQueryCondition(HashMap map){
		StringBuffer conditionStr = new StringBuffer("");
		if(map.get("orgId")!=null && !"".equals(map.get("orgId"))){
			conditionStr.append(" and org_id='").append(map.get("orgId")).append("' ");
		}
		if(map.get("departmentId")!=null && !"".equals(map.get("departmentId"))){
			conditionStr.append(" and department_id='").append(map.get("departmentId")).append("' ");
		}
		if(map.get("categoryId")!=null && !"".equals(map.get("categoryId"))){
			conditionStr.append(" and category_id='").append(map.get("categoryId")).append("' ");
		}
		return conditionStr.toString();
	}
	
	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		FinanceBudgetVO vo =(FinanceBudgetVO)arg0;
		String sql = "delete from teflow_finance_budget where org_id=? and department_id=? and category_id=? and sub_cat_id=?";
		Object[] obj = new Object[4];
		obj[0] = vo.getOrgId();
		obj[1] = vo.getDepartmentId();
		obj[2] = vo.getCategoryId();
		obj[3] = vo.getSubCategoryId();

		int[] dataType = {DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR,DataType.VARCHAR};
		dbManager.executeUpdate(sql,obj,dataType);	
		return 0;
	}

	public int annualSave(BaseVO arg0) throws DAOException {
        String sql = "insert into teflow_finance_budget(org_id,department_id,account_dc,category_id,sub_cat_id,category_name,sub_cat_name,budget_year,full_year_budget,balance"
                + ",original_budget_1,original_budget_2,original_budget_3,original_budget_4,original_budget_5,original_budget_6,original_budget_7"
                + ",original_budget_8,original_budget_9,original_budget_10,original_budget_11,original_budget_12) "
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] obj = new Object[22];
        FinanceBudgetVO vo = (FinanceBudgetVO) arg0;
        int i = 0;
        obj[i++] = vo.getOrgId();
        obj[i++] = vo.getDepartmentId();
        obj[i++] = vo.getAccountDC();
        obj[i++] = vo.getCategoryId();
        obj[i++] = vo.getSubCategoryId();
        obj[i++] = vo.getCategoryName();
        obj[i++] = vo.getSubCategoryName();
        obj[i++] = vo.getBudgetYear();
        obj[i++] = "" + vo.getFullYearBudget();
        obj[i++] = "" + vo.getFullYearBudget(); // Balance
        obj[i++] = "" + vo.getOriginalBudget1();
        obj[i++] = "" + vo.getOriginalBudget2();
        obj[i++] = "" + vo.getOriginalBudget3();
        obj[i++] = "" + vo.getOriginalBudget4();
        obj[i++] = "" + vo.getOriginalBudget5();
        obj[i++] = "" + vo.getOriginalBudget6();
        obj[i++] = "" + vo.getOriginalBudget7();
        obj[i++] = "" + vo.getOriginalBudget8();
        obj[i++] = "" + vo.getOriginalBudget9();
        obj[i++] = "" + vo.getOriginalBudget10();
        obj[i++] = "" + vo.getOriginalBudget11();
        obj[i++] = "" + vo.getOriginalBudget12();

        int[] dataType = { DataType.VARCHAR, DataType.INT, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR,
                DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.DOUBLE, DataType.DOUBLE,
                DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE,
                DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE };
        dbManager.executeUpdate(sql, obj, dataType);
        return 0;
    }
	
	public int annualUpdate(BaseVO arg0) throws DAOException {
        String sql = "update teflow_finance_budget set account_dc=?,category_name=?,sub_cat_name=?,budget_year=?,full_year_budget=?,"
                + "balance = case isnull(adjust_full_year_budget, 0) when 0 then (? - isnull(ytd_actual_expense, 0)) else balance end"
                + ",original_budget_1=?,original_budget_2=?,original_budget_3=?,original_budget_4=?,original_budget_5=?,original_budget_6=?"
                + ",original_budget_7=?,original_budget_8=?,original_budget_9=?,original_budget_10=?,original_budget_11=?,original_budget_12=? "
                + " where org_id=? and department_id=? and category_id=? and sub_cat_id=?";
        Object[] obj = new Object[22];
        FinanceBudgetVO vo = (FinanceBudgetVO) arg0;
        int i = 0;
        obj[i++] = vo.getAccountDC();
        obj[i++] = vo.getCategoryName();
        obj[i++] = vo.getSubCategoryName();
        obj[i++] = vo.getBudgetYear();
        obj[i++] = "" + vo.getFullYearBudget();
        obj[i++] = "" + vo.getFullYearBudget(); // Balance
        obj[i++] = "" + vo.getOriginalBudget1();
        obj[i++] = "" + vo.getOriginalBudget2();
        obj[i++] = "" + vo.getOriginalBudget3();
        obj[i++] = "" + vo.getOriginalBudget4();
        obj[i++] = "" + vo.getOriginalBudget5();
        obj[i++] = "" + vo.getOriginalBudget6();
        obj[i++] = "" + vo.getOriginalBudget7();
        obj[i++] = "" + vo.getOriginalBudget8();
        obj[i++] = "" + vo.getOriginalBudget9();
        obj[i++] = "" + vo.getOriginalBudget10();
        obj[i++] = "" + vo.getOriginalBudget11();
        obj[i++] = "" + vo.getOriginalBudget12();
        obj[i++] = vo.getOrgId();
        obj[i++] = vo.getDepartmentId();
        obj[i++] = vo.getCategoryId();
        obj[i++] = vo.getSubCategoryId();

        int[] dataType = { DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.DOUBLE,
                DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE,
                DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE,
                DataType.DOUBLE, DataType.VARCHAR, DataType.INT, DataType.VARCHAR, DataType.VARCHAR };
        dbManager.executeUpdate(sql, obj, dataType);
        return 0;
    }
	
	public int monthlyUpdate(BaseVO arg0) throws DAOException {
        String sql = "update teflow_finance_budget set current_month = ?, ytd_actual_expense = ?, adjust_full_year_budget = ?, ytd_budget = ?, balance = ? "
                + "where org_id = ? and department_id = ? and category_id = ? and sub_cat_id = ? and budget_year = ? ";

        Object[] obj = new Object[10];
        FinanceBudgetVO vo = (FinanceBudgetVO) arg0;
        int i = 0;
        obj[i++] = vo.getCurrentMonth();
        obj[i++] = "" + vo.getYtnActualExpense();
        obj[i++] = "" + vo.getAdjustFullYearBudget();
        obj[i++] = "" + vo.getYtnBudget();
        obj[i++] = "" + vo.getBanlance();
        obj[i++] = vo.getOrgId();
        obj[i++] = vo.getDepartmentId();
        obj[i++] = vo.getCategoryId();
        obj[i++] = vo.getSubCategoryId();
        obj[i++] = vo.getBudgetYear();

        int[] dataType = { DataType.VARCHAR, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE,
                DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR, DataType.VARCHAR };
        dbManager.executeUpdate(sql, obj, dataType);

        return 0;
    }
	
	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub

		return 0;
	}

	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub

		return 0;
	}

}
