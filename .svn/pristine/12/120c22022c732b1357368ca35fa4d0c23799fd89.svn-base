package com.aiait.eflow.housekeeping.vo;

import java.util.*;

import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.util.DataMapUtil;
import com.aiait.framework.vo.BaseVO;

public class EmailTemplateVO extends BaseVO {
    protected int id;

    protected String name;

    protected String emailSubject;

    protected String emailContent;

    protected String description;

    protected String appliedAction;

    protected List formtypes;

    public static void main(String[] args) {
        EmailTemplateVO v = new EmailTemplateVO();
        v.setAppliedAction("01,02,03");
        System.out.println(v.convertActionToName());
        System.out
                .println("Dear <font color='red'> @receive_staff </font>,<br>Your requestundefined  form($request_no) had been rejected by $handle_by.And,the reject comments is : $comments <br> "
                        .replaceFirst("@receive_staff", "Robin Hou"));
        System.out
                .println("Dear <font color='red'> $receive_staff </font>,<br>Your requestundefined  form($request_no) had been rejected by $handle_by.And,the reject comments is : $comments <br> "
                        .indexOf("$receive_staff"));
    }

    public String convertActionToName() {
        String srcIds = this.getAppliedAction();
        if (srcIds == null || "".equals(srcIds))
            return "";
        String[] ids = StringUtil.split(srcIds, ",");
        String result = "";
        for (int i = 0; i < ids.length; i++) {
            result = result + DataMapUtil.convertHandleType(ids[i]) + ",";
        }
        if (!"".equals(result)) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public String getAppliedAction() {
        return appliedAction;
    }

    public void setAppliedAction(String appliedAction) {
        this.appliedAction = appliedAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the formtypes
     */
    public List getFormtypes() {
        if (this.formtypes == null) {
            this.formtypes = new ArrayList();
        }
        return this.formtypes;
    }

    /**
     * @param formtypes
     *            the formtypes to set
     */
    public void setFormtypes(List formtypes) {
        this.formtypes = formtypes;
    }

}
