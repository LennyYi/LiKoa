package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class FinanceBudgetVO extends BaseVO {
    private String orgId;

    private String departmentId;

    private String budgetYear;

    private String categoryId;

    private String subCategoryId;

    private String categoryName = "";

    private String subCategoryName = "";

    private String accountDC; // 财务上的部门代码

    private double fullYearBudget;

    private double adjustFullYearBudget;

    private double ytnActualExpense;

    private double ytnBudget;

    private double banlance;

    private String currentMonth; // 当前月份

    private double originalBudget1; // 一月份的原计划费用

    private double originalBudget2; // 二月份的原计划费用

    private double originalBudget3; // 三月份的原计划费用

    private double originalBudget4;

    private double originalBudget5;

    private double originalBudget6;

    private double originalBudget7;

    private double originalBudget8;

    private double originalBudget9;

    private double originalBudget10;

    private double originalBudget11;

    private double originalBudget12;

    public double getBanlance2() {
        double fullYearBudget = 0;
        fullYearBudget = this.getAdjustFullYearBudget();
        //if (fullYearBudget == 0) {
        //    fullYearBudget = this.getFullYearBudget();
        //}
        //if (fullYearBudget != 0) {
            return fullYearBudget - this.getYtnActualExpense();
        //} else {
        //    return 0;
        //}
    }

    public double getBanlance() {
        return this.banlance;
    }

    public void setBanlance(double banlance) {
        this.banlance = banlance;
    }

    public String getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(String budgetYear) {
        this.budgetYear = budgetYear;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public double getFullYearBudget() {
        return fullYearBudget;
    }

    public void setFullYearBudget(double fullYearBudget) {
        this.fullYearBudget = fullYearBudget;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public double getYtnActualExpense() {
        return ytnActualExpense;
    }

    public void setYtnActualExpense(double ytnActualExpense) {
        this.ytnActualExpense = ytnActualExpense;
    }

    public double getYtnBudget() {
        return ytnBudget;
    }

    public void setYtnBudget(double ytnBudget) {
        this.ytnBudget = ytnBudget;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public double getOriginalBudget1() {
        return originalBudget1;
    }

    public void setOriginalBudget1(double originalBudget1) {
        this.originalBudget1 = originalBudget1;
    }

    public double getOriginalBudget10() {
        return originalBudget10;
    }

    public void setOriginalBudget10(double originalBudget10) {
        this.originalBudget10 = originalBudget10;
    }

    public double getOriginalBudget11() {
        return originalBudget11;
    }

    public void setOriginalBudget11(double originalBudget11) {
        this.originalBudget11 = originalBudget11;
    }

    public double getOriginalBudget12() {
        return originalBudget12;
    }

    public void setOriginalBudget12(double originalBudget12) {
        this.originalBudget12 = originalBudget12;
    }

    public double getOriginalBudget2() {
        return originalBudget2;
    }

    public void setOriginalBudget2(double originalBudget2) {
        this.originalBudget2 = originalBudget2;
    }

    public double getOriginalBudget3() {
        return originalBudget3;
    }

    public void setOriginalBudget3(double originalBudget3) {
        this.originalBudget3 = originalBudget3;
    }

    public double getOriginalBudget4() {
        return originalBudget4;
    }

    public void setOriginalBudget4(double originalBudget4) {
        this.originalBudget4 = originalBudget4;
    }

    public double getOriginalBudget5() {
        return originalBudget5;
    }

    public void setOriginalBudget5(double originalBudget5) {
        this.originalBudget5 = originalBudget5;
    }

    public double getOriginalBudget6() {
        return originalBudget6;
    }

    public void setOriginalBudget6(double originalBudget6) {
        this.originalBudget6 = originalBudget6;
    }

    public double getOriginalBudget7() {
        return originalBudget7;
    }

    public void setOriginalBudget7(double originalBudget7) {
        this.originalBudget7 = originalBudget7;
    }

    public double getOriginalBudget8() {
        return originalBudget8;
    }

    public void setOriginalBudget8(double originalBudget8) {
        this.originalBudget8 = originalBudget8;
    }

    public double getOriginalBudget9() {
        return originalBudget9;
    }

    public void setOriginalBudget9(double originalBudget9) {
        this.originalBudget9 = originalBudget9;
    }

    public String getAccountDC() {
        return accountDC;
    }

    public void setAccountDC(String accountDC) {
        this.accountDC = accountDC;
    }

    // Depreciated - 2008/11/17
    public double getYTDBudget(String month) {
        if (month == null || "".equals(month))
            return 0;

        if ("1".equals(month) || "01".equals(month)) {
            return this.getOriginalBudget1();
        } else if ("2".equals(month) || "02".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2());
        } else if ("3".equals(month) || "03".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3());
        } else if ("4".equals(month) || "04".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3() + this
                    .getOriginalBudget4());
        } else if ("5".equals(month) || "05".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3()
                    + this.getOriginalBudget4() + this.getOriginalBudget5());
        } else if ("6".equals(month) || "06".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3()
                    + this.getOriginalBudget4() + this.getOriginalBudget5() + this.getOriginalBudget6());
        } else if ("7".equals(month) || "07".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3()
                    + this.getOriginalBudget4() + this.getOriginalBudget5() + this.getOriginalBudget6() + this
                    .getOriginalBudget7());
        } else if ("8".equals(month) || "08".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3()
                    + this.getOriginalBudget4() + this.getOriginalBudget5() + this.getOriginalBudget6()
                    + this.getOriginalBudget7() + this.getOriginalBudget8());
        } else if ("9".equals(month) || "09".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3()
                    + this.getOriginalBudget4() + this.getOriginalBudget5() + this.getOriginalBudget6()
                    + this.getOriginalBudget7() + this.getOriginalBudget8() + this.getOriginalBudget9());
        } else if ("10".equals(month) || "10".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3()
                    + this.getOriginalBudget4() + this.getOriginalBudget5() + this.getOriginalBudget6()
                    + this.getOriginalBudget7() + this.getOriginalBudget8() + this.getOriginalBudget9() + this
                    .getOriginalBudget10());
        } else if ("11".equals(month) || "11".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3()
                    + this.getOriginalBudget4() + this.getOriginalBudget5() + this.getOriginalBudget6()
                    + this.getOriginalBudget7() + this.getOriginalBudget8() + this.getOriginalBudget9()
                    + this.getOriginalBudget10() + this.getOriginalBudget11());
        } else if ("12".equals(month) || "12".equals(month)) {
            return (this.getOriginalBudget1() + this.getOriginalBudget2() + this.getOriginalBudget3()
                    + this.getOriginalBudget4() + this.getOriginalBudget5() + this.getOriginalBudget6()
                    + this.getOriginalBudget7() + this.getOriginalBudget8() + this.getOriginalBudget9()
                    + this.getOriginalBudget10() + this.getOriginalBudget11() + this.getOriginalBudget12());
        } else {
            return 0;
        }
    }

    // Depreciated - 2008/11/17
    public double getBanlanceByMonth(String month) {
        if (month == null || "".equals(month))
            return 0;
        return (this.getFullYearBudget() - this.getYTDBudget(month));
    }

    public double getAdjustFullYearBudget() {
        return adjustFullYearBudget;
    }

    public void setAdjustFullYearBudget(double adjustFullYearBudget) {
        this.adjustFullYearBudget = adjustFullYearBudget;
    }

    /**
     * @return the subCategoryId
     */
    public String getSubCategoryId() {
        return subCategoryId;
    }

    /**
     * @param subCategoryId
     *            the subCategoryId to set
     */
    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName
     *            the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the subCategoryName
     */
    public String getSubCategoryName() {
        return subCategoryName;
    }

    /**
     * @param subCategoryName
     *            the subCategoryName to set
     */
    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

}
