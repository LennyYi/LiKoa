package com.aiait.eflow.housekeeping.vo;

import com.aiait.framework.vo.BaseVO;

public class TeamVO extends BaseVO {
    protected String teamCode;
    protected String teamName;
    protected String orgId; // 所属公司的id
    protected String superiorscode;
    protected String superiorteamname;
    protected String status;
    protected String tlid;
    protected String tleadername;
    protected String modifieddate;
    protected String modifiedstaff;
    protected String orgChart; // 是否属于orgchart上的team，‘Y’是的
    protected String department; //是否为一个Department, 如果是，其team leader会成为Department Header group 的成员。
    protected String t2Code;

    protected boolean readOnly = false; // For view from 3rd source.

    public String getOrgChart() {
        return orgChart;
    }

    public void setOrgChart(String orgChart) {
        this.orgChart = orgChart;
    }

    public void setSuperiorsCode(String superiorscode) {
        this.superiorscode = superiorscode;
    }

    public String getSuperiorsCode() {
        return this.superiorscode;
    }

    public void setSuperiorTeamName(String superiorteamname) {
        this.superiorteamname = superiorteamname;
    }

    public String getSuperiorTeamName() {
        return this.superiorteamname;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setTlid(String tlid) {
        this.tlid = tlid;
    }

    public String getTlid() {
        return this.tlid;
    }

    public void setTLeaderName(String tleadername) {
        this.tleadername = tleadername;
    }

    public String getTLeaderName() {
        return this.tleadername;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public String getModifieddate() {
        return this.modifieddate;
    }

    public void setModifiedstaff(String modifiedstaff) {
        this.modifiedstaff = modifiedstaff;
    }

    public String getModifiedstaff() {
        return this.modifiedstaff;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * @param readOnly
     *            the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setDepartment(String department) {
		this.department = department;
	}

    public String getDepartment() {
		return department;
	}

	public String getSuperiorscode() {
		return superiorscode;
	}

	public void setSuperiorscode(String superiorscode) {
		this.superiorscode = superiorscode;
	}

	public String getSuperiorteamname() {
		return superiorteamname;
	}

	public void setSuperiorteamname(String superiorteamname) {
		this.superiorteamname = superiorteamname;
	}

	public String getTleadername() {
		return tleadername;
	}

	public void setTleadername(String tleadername) {
		this.tleadername = tleadername;
	}

	public String getT2Code() {
		return t2Code;
	}

	public void setT2Code(String t2Code) {
		this.t2Code = t2Code;
	}

}
