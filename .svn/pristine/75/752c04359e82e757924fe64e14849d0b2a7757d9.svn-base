package com.aiait.eflow.housekeeping.vo;

/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0973	Young	12/26/2007	For Eflow User Management         */
/******************************************************************/

import java.util.*;

public class EflowStaffVO extends StaffVO {
    protected String title;
    protected String titleName;
    protected String createDateStr;
    protected String usertype;
    protected String fromdate;
    protected String todate;

    protected Collection approverGroups = new Vector();
    protected Collection roles = new Vector();

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitleName(String titelName) {
        this.titleName = titelName;
    }

    public String getTitleName() {
        return this.titleName;
    }

    /**
     * @return the approverGroups
     */
    public Collection getApproverGroups() {
        return approverGroups;
    }

    /**
     * @param approverGroups
     *            the approverGroups to set
     */
    public void setApproverGroups(Collection approverGroups) {
        this.approverGroups = approverGroups;
    }

    public void addApproverGroup(ApproverGroupVO approverGroup) {
        this.approverGroups.add(approverGroup);
    }

    /**
     * @return the roles
     */
    public Collection getRoles() {
        return roles;
    }

    /**
     * @param roles
     *            the roles to set
     */
    public void setRoles(Collection roles) {
        this.roles = roles;
    }

    public void addRole(RoleVO role) {
        this.roles.add(role);
    }

}
