package com.aiait.eflow.housekeeping.vo;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.framework.vo.BaseVO;

public class TitleVO extends BaseVO {
    private int titleId;

    private String abrev;

    private String description;

    private String titleLevel;

    private String active;

    public String getAbrev() {
        return abrev;
    }

    public void setAbrev(String abrev) {
        this.abrev = abrev;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public String getTitleLevel() {
        return titleLevel;
    }

    public void setTitleLevel(String titleLevel) {
        this.titleLevel = titleLevel;
    }

    public static String[] getGradeList() {
        String gradeList = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_GRADE_LIST);
        gradeList = gradeList == null ? "" : gradeList;
        int i = gradeList.lastIndexOf(":");
        if (i != -1) {
            gradeList = gradeList.substring(i + 1);
        }
        return gradeList.split(",");
    }

    public static String getGradeName() {
        String gradeList = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_GRADE_LIST);
        gradeList = gradeList == null ? "" : gradeList;
        int i = gradeList.lastIndexOf(":");
        if (i != -1) {
            return gradeList.substring(0, i).trim();
        } else {
            return "Grade";
        }
    }
}
