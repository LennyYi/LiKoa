package com.aiait.eflow.housekeeping.dao;
/******************************************************************/
/*Task_ID	Author	Modify_Date	Description                       */
/*IT0973	Young	12/27/2007	For Eflow Team Management         */
/******************************************************************/
import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;
import com.aiait.eflow.common.helper.StaffTeamHelper;
import com.aiait.eflow.formmanage.vo.FormSectionFieldVO;
import com.aiait.eflow.housekeeping.vo.ApproverGroupMemberVO;
import com.aiait.eflow.housekeeping.vo.StaffVO;
import com.aiait.eflow.housekeeping.vo.TeamVO;
import com.aiait.eflow.util.FieldUtil;
import com.aiait.eflow.util.StringUtil;
import com.aiait.framework.common.DataType;
import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.page.PageVO;
import com.aiait.framework.vo.BaseVO;
import com.aiait.eflow.housekeeping.vo.ViewTeamTypeVO;
import com.aiait.eflow.housekeeping.vo.TeamTypeVO;

public class TeamDAO extends BaseDAOImpl {
	
	public TeamDAO(IDBManager dbManager){
		super(dbManager);
	}
	
	/**
	 * 检查某个员工是否是TL
	 * @param staffCode
	 * @return true---是TL, false --- 不是tl
	 * @throws DAOException
	 */
	public boolean checkTL(String staffCode)throws DAOException{
		String sql = "select count(*) from tpma_team where status='A' and tl_id in(select logon_id from tpma_staffbasic where staff_code='"
			          +staffCode+"' and status='A')";
		int count = dbManager.getRecordCount(sql);
		if(count>0) return true;
		return false;
	}
	
	public int getTeamAmountForTL (String tlid, boolean department)throws DAOException{
		String sql = "select count(*) from tpma_team where (status='A') and (tl_id ='"+tlid+ "') ";
		if(department){
		   sql += " and (department = 'Y')";
		}else{
		   sql += " and (department = 'N' or department is null )";
		}
		return dbManager.getRecordCount(sql);
	}
	
	/**
	 * 获取指定team下的所有非orgchart上的直接下级team
	 * @param teamCode
	 * @return 7，2，2
	 * @throws DAOException
	 */
	public String getNoOrgChartLowerTeamCode(int teamCode)throws DAOException{
		//String sql = "select team_code from tpma_team where subordinate=" + teamCode + " and org_chart='N' ";
		String sql = "select * from tpma_team where subordinate=" + teamCode +" and status='A' ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return "";
		String result = "";
		Iterator it = list.iterator();
		String subTeam = "";
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			if("".equals(FieldUtil.convertSafeString(map,"ORG_CHART")) || "Y".equals(FieldUtil.convertSafeString(map,"ORG_CHART"))){//如果是Org-chart上的team,则不需要增加到返回结果中
				subTeam = getNoOrgChartLowerTeamCode(FieldUtil.convertSafeInt(map,"TEAM_CODE",-999));
			}else if("N".equals(FieldUtil.convertSafeString(map,"ORG_CHART"))){ //如果不是org-chart上的team,则需增加到返回结果中
			   result = (String)map.get("TEAM_CODE")+","+result;
			   subTeam = getNoOrgChartLowerTeamCode(FieldUtil.convertSafeInt(map,"TEAM_CODE",-999));
			}
			if(subTeam!=null && !"".equals(subTeam)){
				if("".equals(result)){
					result =  subTeam;
				}else{
				  result =  result + "," + subTeam;
				}
			}
			//System.out.println(result);
		}
		if(result!=null && result.length()>0 && result.substring(result.length()-1,result.length()).equals(",") ){
			result = result.substring(0,result.length()-1);
		}
		if(result!=null && result.length()>0 && result.substring(0,1).equals(",") ){
			result = result.substring(1,result.length()-1);
		}
		return result;
	}
	
	public String getLowerTeamCodes(int teamCode) throws DAOException {
        String sql = "select * from tpma_team where subordinate = ? and status='A'";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        String teamCodes = "";
        List codeList = new ArrayList();

        try {
            stm = conn.prepareStatement(sql);
            stm.setInt(1, teamCode);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int _teamCode = rs.getInt("team_code");
                codeList.add(Integer.valueOf(_teamCode));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        String sp = "";
        for (int i = 0; i < codeList.size(); i++) {
            int _teamCode = (Integer) codeList.get(i);
            teamCodes += sp + _teamCode;
            sp = ",";
            String _teamCodes = this.getLowerTeamCodes(_teamCode);
            if ("".equals(_teamCodes)) {
                continue;
            }
            // System.out.println("_teamCodes: " + _teamCodes);
            teamCodes += "," + _teamCodes;
        }
        return teamCodes;
    }
	
	public String getManagedTeamCodesByTL(String staffCode) throws DAOException {
        String sql = "select a.* from tpma_team a, tpma_staffbasic b where a.tl_id = b.logon_id and a.status = 'A' and b.staff_code = ?";

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        String teamCodes = "";
        List codeList = new ArrayList();

        try {
            stm = conn.prepareStatement(sql);
            stm.setString(1, staffCode);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int teamCode = rs.getInt("team_code");
                codeList.add(Integer.valueOf(teamCode));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DAOException(ex);
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        String sp = "";
        for (int i = 0; i < codeList.size(); i++) {
            int teamCode = (Integer) codeList.get(i);
            // System.out.println("teamCode: " + teamCode);
            teamCodes += sp + teamCode;
            sp = ",";
            String _teamCodes = this.getLowerTeamCodes(teamCode);
            if ("".equals(_teamCodes)) {
                continue;
            }
            // System.out.println("_teamCodes: " + _teamCodes);
            teamCodes += "," + _teamCodes;
        }
        // Remove duplicated team codes.
        String[] aryCode = teamCodes.split(",");
        HashMap codeMap = new HashMap();
        for (int i = 0; i < aryCode.length; i++) {
            codeMap.put(aryCode[i], aryCode[i]);
        }
        Iterator it = codeMap.values().iterator();
        teamCodes = "";
        sp = "";
        while (it.hasNext()) {
            teamCodes += sp + (String) it.next();
            sp = ",";
        }
        return teamCodes;
    }
	
	public void updateteamconf(ViewTeamTypeVO vttvo) throws DAOException{
		String sql = "update teflow_teamtype_mapping set team_type='"+vttvo.getTeam_type()+"' where team_code="+vttvo.getTeamCode();
		dbManager.executeUpdate(sql);
	}
	public void insertteamconf(ViewTeamTypeVO vttvo) throws DAOException{
		String sql = "insert into teflow_teamtype_mapping(team_code,team_type) values("+vttvo.getTeamCode()+",'"+vttvo.getTeam_type()+"')";
		dbManager.executeUpdate(sql);
	}
	
	public boolean Isexist(ViewTeamTypeVO vttvo) throws DAOException{
		String sql = "select * from teflow_teamtype_mapping where team_code="+vttvo.getTeamCode();
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) 
			return false;
		else
			return true;		
	}
	
	public void deleteTeam(String teamCode)throws DAOException{
		String sql = "update teflow_team set status='T' where team_code=" + teamCode;
		dbManager.executeUpdate(sql);
	}
	
	/**
	 * 检查指定的team是否存在有效的直接下级机构
	 * @param teamCode
	 * @return true,存在；false,不存在
	 * @throws DAOException
	 */
	public boolean checkHasSubTeam(String teamCode)throws DAOException{
		String sql = "select count(*) from teflow_team where status='A' and superiors_code=" + teamCode;
		int count = dbManager.getRecordCount(sql);
		if(count>0) return true;
		return false;
	}
	
	/**
	 * 获取某个公司下的所有team集合
	 * @param orgId
	 * @return
	 * @throws DAOException
	 */
	public Collection getTeamListByCompany(String orgId)throws DAOException{
		Collection result = new ArrayList();
		String sql = null;
		if(orgId != null && orgId.length() > 0){
			sql = "select * from tpma_team where org_id='" + orgId+"' and status='A' order by team_name ";
		}else{
			sql = "select * from tpma_team where status='A' order by team_name ";
		}
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return result;
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			TeamVO team = convertefteam(map);
			result.add(team);
		}
		return result;
	}
	
	/**
	 * 获取所有所属公司及其下级公司的TEAM
	 * @return
	 * @throws DAOException
	 */
	public Collection getTeamList(String lowerOrgIds)throws DAOException{
		String sql = "select * from tpma_team where status='A' ";
		if(lowerOrgIds!=null && !"".equals(lowerOrgIds)){
			sql = sql + " and org_id in (" + lowerOrgIds +") ";
		}
		sql = sql + " order by team_name ";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			TeamVO team = convertefteam(map);
			//team.setTeamCode(FieldUtil.convertSafeString(map,"TEAM_CODE"));
			//team.setTeamName(FieldUtil.convertSafeString(map,"TEAM_NAME"));
			result.add(team);
		}
		sql = null;
		return result;
	}

	/** Commented by Robin 2008-06-20 
	public Collection getMergedTeamList()throws DAOException{		
		String sql = "select team_code,team_name from teflow_team where status='A' order by team_name";
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) return null;
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			TeamVO team = new TeamVO();
			team.setTeamCode(FieldUtil.convertSafeString(map,"TEAM_CODE"));
			team.setTeamName(FieldUtil.convertSafeString(map,"TEAM_NAME"));
			result.add(team);
		}
		return result;
	}
	**/
	
	public TeamVO getTeamByTeamCode(String teamcode)throws DAOException{
		/**
		StringBuffer sql = new StringBuffer("select O.TEAM_CODE,O.TEAM_NAME,O.SUPERIORS_CODE,S.team_name SUPERIOR_TEAM_NAME,O.STATUS,O.TL_ID,O.TEAMLEADER_NAME,o.org_chart ");
		sql.append(" FROM ")
		   .append(" (select T.TEAM_CODE,T.TEAM_NAME,T.SUPERIORS_CODE,T.STATUS,T.TL_ID,U.STAFF_NAME AS TEAMLEADER_NAME,t.org_chart ")
		   .append("  FROM   TEFLOW_TEAM T LEFT JOIN TEFLOW_USER U ON T.TL_ID=U.LOGONID where T.TEAM_CODE=").append(teamcode+") O ")		   
		   .append(" LEFT JOIN")
		   .append(" (select a.superiors_code,b.team_name ")
		   .append("  FROM   (select distinct superiors_code from TEFLOW_TEAM) a ")
		   .append("  LEFT JOIN TEFLOW_TEAM b ")
		   .append(" ON a.superiors_code=b.team_code) S ")
		   .append(" ON O.SUPERIORS_CODE=S.superiors_code");	
		   **/
		if(StringUtil.isEmptyString(teamcode)) return null;
		//StringBuffer sql = new StringBuffer("select * from tpma_team where 1=1 ");
		//sql.append(" and team_code=").append(teamcode);
		StringBuffer sql = new StringBuffer("select t.*,s.staff_name TEAMLEADER_NAME from tpma_team t left join tpma_staffbasic s on  t.tl_id =s.logon_id and s.status='A' where 1=1");  
		sql.append(" and t.team_code=").append(teamcode);
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) {return null;}
		Iterator it = list.iterator();
		HashMap map = (HashMap)it.next();
				
		TeamVO eflowteam = convertefteam(map);
		
		if(isTableTeamT2Exists()){
			sql = new StringBuffer("select * from TEFLOW_TEAM_T2 where team_code = ");
			sql.append(teamcode);
			
			list = dbManager.query(sql.toString());
			if(list==null || list.size()==0) {				
			}else{
				it = list.iterator();
				map = (HashMap)it.next();
				eflowteam.setT2Code((String)map.get("T2_CODE"));
			}
		}
		return eflowteam;
	}
	
	public Collection getTeamType() throws DAOException{
		StringBuffer sql = new StringBuffer("select * from teflow_teamtype");
		Collection list =dbManager.query(sql.toString());
		if(list==null || list.size()==0){
			return null;
		}
		ArrayList result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){		
			HashMap map = (HashMap)it.next();
			TeamTypeVO teamtypevo = new TeamTypeVO();
			teamtypevo.setTeamTypeId(FieldUtil.convertSafeString(map,"TEAMTYPE_ID"));
			teamtypevo.setTeamTypeName(FieldUtil.convertSafeString(map,"TEAMTYPE_NAME"));
			teamtypevo.setTeamTypeDes(FieldUtil.convertSafeString(map,"TEAMTYPE_DESCRIPTION"));
			result.add(teamtypevo);
		}
		return result;
	}
	
	public Collection getSubteamArr(String teamcode) throws DAOException{
		StringBuffer sql= new StringBuffer(" select T.*,c.team_type from (select a.*,b.team_name SUPERIORTEAMNAME from tpma_team a left join tpma_team b on a.subordinate=b.team_code")
		  .append(" where a.subordinate = ").append(teamcode).append(" and a.status='A') T left join teflow_teamtype_mapping c on T.team_code = c.team_code ");
		//System.out.println(sql.toString());
		Collection list =dbManager.query(sql.toString());		
		if(list==null || list.size()==0){
			return null;
		}
		ArrayList result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){		
			HashMap map = (HashMap)it.next();
			ViewTeamTypeVO vtvo = new ViewTeamTypeVO();
			vtvo.setTeamCode(FieldUtil.convertSafeString(map,"TEAM_CODE"));
			vtvo.setTeamName(FieldUtil.convertSafeString(map,"TEAM_NAME"));
			vtvo.setStatus(FieldUtil.convertSafeString(map,"STATUS"));
			vtvo.setSuperiorsCode(FieldUtil.convertSafeString(map,"SUBORDINATE"));
			vtvo.setSuperiorTeamName(FieldUtil.convertSafeString(map,"SUPERIORTEAMNAME"));
			vtvo.setTeam_type(FieldUtil.convertSafeString(map,"TEAM_TYPE"));
			vtvo.setOrgChart(FieldUtil.convertSafeString(map,"ORG_CHART"));
			result.add(vtvo);
		}				
		return result;		
	}
	
	/**
	 * 
	 * @param teamcode
	 * @return
	 * @throws DAOException
	 */
	private ArrayList getsubTeam(String teamcode) throws DAOException{	
		//StringBuffer sql = new StringBuffer(" select a.team_code from teflow_team a ")
		StringBuffer sql = new StringBuffer(" select a.team_code from tpma_team a ")
		  // .append("  where a.superiors_code = ").append(teamcode).append(" ")
	     .append("  where a.subordinate = ").append(teamcode).append(" ")
		   .append("  and a.status='A' ");		
		//System.out.println(sql.toString());
		Collection list =dbManager.query(sql.toString());		
		if(list==null || list.size()==0){
			return null;
		}
		ArrayList result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){		
			HashMap map = (HashMap)it.next();			
			result.add(FieldUtil.convertSafeString(map,"TEAM_CODE"));
		}		
		
		return result;
	}
	
	/**
	 * 获取指定team的所有下级team
	 * @param teamcode 类似：1,2
	 * @return
	 * @throws DAOException
	 */
	public String groupSubTeam(String teamcode) throws DAOException {
		String str ="";		
		ArrayList arr = getsubTeam(teamcode);		
		if (arr==null){			
			return str;
		}
		for(int i=0;i<arr.size();i++){
			str += arr.get(i).toString()+","+groupSubTeam(arr.get(i).toString());
		}			
		return str;
	}
	
/**	
	public Collection getViewTeamList() throws DAOException{
		StringBuffer sql = new StringBuffer("select a.team_code,a.team_name,a.subordinate superiorscode,b.team_name superiorteamname,a.status ");
		sql.append(" from ")		
			.append(" (select * from tpma_team where status='A') a ")
			.append(" left join (select * from tpma_team where status='A') b ")
			.append(" on a.subordinate = b.team_code ")
			.append(" order by a.team_name ");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) {			
			return null;
		}
		Collection result = new ArrayList();
		Iterator it = list.iterator();		
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			ViewTeamVO viewteam = new ViewTeamVO();
			viewteam.setTeamCode(FieldUtil.convertSafeString(map,"TEAM_CODE"));
			viewteam.setTeamName(FieldUtil.convertSafeString(map,"TEAM_NAME"));
			viewteam.setSuperiorscode(FieldUtil.convertSafeString(map,"SUPERIORSCODE"));
			viewteam.setSuperiorscode(FieldUtil.convertSafeString(map,"SUPERIORTEAMNAME"));
			viewteam.setStatus(FieldUtil.convertSafeString(map,"STATUS"));
			viewteam.setOrgChart(FieldUtil.convertSafeString(map,"ORG_CHART"));
			result.add(viewteam);
		}
		return result;
	}
	
	
	public Collection getEflowTeamList()throws DAOException{
		StringBuffer sql = new StringBuffer("select O.TEAM_CODE,O.TEAM_NAME,O.SUPERIORS_CODE,S.team_name SUPERIOR_TEAM_NAME,O.STATUS,O.TL_ID,O.TEAMLEADER_NAME ");
		sql.append(" FROM ")
		   .append(" (select T.TEAM_CODE,T.TEAM_NAME,T.SUPERIORS_CODE,T.STATUS,T.TL_ID,U.STAFF_NAME AS TEAMLEADER_NAME")
		   .append("  FROM   TEFLOW_TEAM T LEFT JOIN TEFLOW_USER U ON T.TL_ID=U.LOGONID where T.status='A') O ")		   
		   .append(" LEFT JOIN")
		   .append(" (select a.superiors_code,b.team_name ")
		   .append("  FROM   (select distinct superiors_code from TEFLOW_TEAM) a ")
		   .append("  LEFT JOIN TEFLOW_TEAM b ")
		   .append(" ON a.superiors_code=b.team_code) S ")
		   .append(" ON O.SUPERIORS_CODE=S.superiors_code");
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) {			
			return null;
		}
		Collection result = new ArrayList();
		Iterator it = list.iterator();		
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			EflowTeamVO eflowteam = convertefteam(map);
			result.add(eflowteam);
		}
		return result;
	}
	public Collection getEflowTeamListWithoutTeamCode(String teamCode) throws DAOException{
		StringBuffer sql = new StringBuffer("select O.TEAM_CODE,O.TEAM_NAME,O.SUPERIORS_CODE,S.team_name SUPERIOR_TEAM_NAME,O.STATUS ");
		sql.append(" FROM ")
		   .append(" (select T.TEAM_CODE,T.TEAM_NAME,T.subordinate as SUPERIORS_CODE,T.STATUS,T.TL_ID,U.STAFF_NAME AS TEAMLEADER_NAME")
		   .append("  FROM   TPMA_TEAM T LEFT JOIN TPMA_STAFF U ON T.TL_ID=U.LOGON_ID where T.status='A' and T.TEAM_CODE not in (").append(teamCode).append(")) O ")		   
		   .append(" LEFT JOIN")
		   .append(" (select a.superiors_code,b.team_name ")
		   .append("  FROM   (select distinct subordinate as superiors_code from TPMA_TEAM) a ")
		   .append("  LEFT JOIN TPMA_TEAM b ")
		   .append(" ON a.superiors_code=b.team_code) S ")
		   .append(" ON O.SUPERIORS_CODE=S.superiors_code");
		//System.out.println(sql.toString());
		Collection list = dbManager.query(sql.toString());
		if(list==null || list.size()==0) {			
			return null;
		}
		Collection result = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			ViewTeamVO viewteam = new ViewTeamVO();
			viewteam.setTeamCode(FieldUtil.convertSafeString(map,"TEAM_CODE"));
			viewteam.setTeamName(FieldUtil.convertSafeString(map,"TEAM_NAME"));
			viewteam.setSuperiorscode(FieldUtil.convertSafeString(map,"SUPERIORS_CODE"));
			viewteam.setSuperiorscode(FieldUtil.convertSafeString(map,"SUPERIOR_TEAM_NAME"));
			viewteam.setStatus(FieldUtil.convertSafeString(map,"STATUS"));
			viewteam.setOrgChart(FieldUtil.convertSafeString(map,"ORG_CHART"));
			result.add(viewteam);
		}
		return result;
	}
	**/
	
	public TeamVO convertefteam(HashMap map) {
        TeamVO teamvo = new TeamVO();
        teamvo.setTeamCode(FieldUtil.convertSafeString(map, "TEAM_CODE"));
        teamvo.setTeamName(FieldUtil.convertSafeString(map, "TEAM_NAME"));
        // teamvo.setSuperiorsCode(FieldUtil.convertSafeString(map,"SUBORDINATE"));
        teamvo.setSuperiorsCode("-1".equals(FieldUtil.convertSafeString(map, "SUBORDINATE")) ? "" : FieldUtil
                .convertSafeString(map, "SUBORDINATE"));
        teamvo.setSuperiorTeamName(FieldUtil.convertSafeString(map, "SUPERIOR_TEAM_NAME"));
        teamvo.setStatus(FieldUtil.convertSafeString(map, "STATUS"));
        teamvo.setTlid(FieldUtil.convertSafeString(map, "TL_ID"));
        teamvo.setTLeaderName(FieldUtil.convertSafeString(map, "TEAMLEADER_NAME"));
        teamvo.setOrgChart(FieldUtil.convertSafeString(map, "ORG_CHART"));
        teamvo.setDepartment(FieldUtil.convertSafeString(map, "DEPARTMENT"));
        teamvo.setOrgId(FieldUtil.convertSafeString(map, "ORG_ID"));
        teamvo.setReadOnly("".equals(FieldUtil.convertSafeString(map, "MODIFIED_STAFF")));
        return teamvo;
    }
	
	public int getTotalRecordsNum(PageVO page)throws DAOException{
		HashMap paramMap = page.getParamMap();
		//StringBuffer staffSQL = new StringBuffer("select count(t.team_code) from teflow_team t where 1=1 ");
		StringBuffer staffSQL = new StringBuffer("select count(t.team_code) from tpma_team t where 1=1 ");
		staffSQL.append(makeQueryCondition(paramMap));
		int i = dbManager.getRecordCount(staffSQL.toString());
		return i;
	}
	
	public Collection searchTeamList(PageVO page)throws DAOException{
		HashMap paramMap = page.getParamMap();
		
		//StringBuffer sql = new StringBuffer("select t.*,s.staff_name TEAMLEADER_NAME from teflow_team t left join tpma_staffbasic s on  t.tl_id =s.logon_id and s.status='A' where 1=1  ");
		StringBuffer sql = new StringBuffer("select t.*,s.staff_name TEAMLEADER_NAME from tpma_team t left join tpma_staffbasic s on  t.tl_id =s.logon_id and s.status='A' where 1=1  ");
		
		sql.append(makeQueryCondition(paramMap));
		//System.out.println(sql.toString());
		Collection list = dbManager.query(sql.toString(),page.getPageSize(),page.getCurrentPage());
		
		if(list==null || list.size()==0) {			
			return null;
		}
		Collection result = new ArrayList();
		Iterator it = list.iterator();		
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			TeamVO eflowteam = convertefteam(map);
			result.add(eflowteam);
		}
		return result;
	}
	
	private String makeQueryCondition(HashMap paramMap){
		StringBuffer strCondition = new StringBuffer("");
		if (paramMap!=null && paramMap.get("status")!=null && !"".equals(paramMap.get("status"))){			   
			strCondition.append(" and T.STATUS='").append(paramMap.get("status")).append("' ");
		}
		if (paramMap!=null && paramMap.get("teamCode")!=null && !"".equals(paramMap.get("teamCode"))){
		   strCondition.append(" and cast(T.TEAM_CODE as varchar)='").append(paramMap.get("teamCode")).append("' ");
		}
		if (paramMap!=null && paramMap.get("teamName")!=null && !"".equals(paramMap.get("teamName"))){
		   strCondition.append(" and T.TEAM_NAME like '%").append(paramMap.get("teamName")).append("%' ");
		}
		if (paramMap!=null && paramMap.get("teamLeader")!=null && !"".equals(paramMap.get("teamLeader"))){
			   strCondition.append(" and T.tl_id in (select logon_id from tpma_staffbasic where status='A' and STAFF_NAME like '%").append(paramMap.get("teamLeader")).append("%') ");
		}
		//if (paramMap!=null && paramMap.get("department")!=null && "Y".equals(paramMap.get("department"))){
		//	   strCondition.append(" and t.department = 'Y' ");
		if (paramMap!=null && paramMap.get("department")!=null){
			if("Y".equals(paramMap.get("department")))
				strCondition.append(" and t.department = 'Y' ");
			else if("N".equals(paramMap.get("department")))
				strCondition.append(" and (t.department <> 'Y' or t.department is null) ");
		}				
		if (paramMap!=null && paramMap.get("orgId")!=null && !"".equals(paramMap.get("orgId"))){
			   strCondition.append(" and t.org_id in (").append(paramMap.get("orgId")).append(")");
		}
		return strCondition.toString();
	}
	
	public String generateTeamcode() throws DAOException{
		String team_code=null;
		String sql = "select max(team_code)+1 team_code from teflow_team";		
		Collection list = dbManager.query(sql);
		if(list==null || list.size()==0) {			
			team_code = "1000";
			return team_code;
		}
		Iterator it = list.iterator();		
		while(it.hasNext()){
			HashMap map = (HashMap)it.next();
			team_code = FieldUtil.convertSafeString(map,"TEAM_CODE");
		}
		return team_code;
	}
	public int save(BaseVO vo, HttpServletRequest request) throws DAOException {
		TeamVO eteamvo = (TeamVO)vo;
		String requestFormDate = "";
		String team_code = generateTeamcode();
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		StringBuffer insertSQL = new StringBuffer("insert into teflow_team(team_code,team_name,Superiors_code,Status,Tl_id,Modified_date,Modified_staff,org_chart,department,org_id) ");
		insertSQL.append(" values("+team_code+",'").append(eteamvo.getTeamName().replaceAll("'", "''")).append("',");
		if (eteamvo.getSuperiorsCode()==null||"".equals(eteamvo.getSuperiorsCode())){
			insertSQL.append("-1").append(",'");
		}else{
			insertSQL.append(eteamvo.getSuperiorsCode()).append(",'");
		}				         
		insertSQL.append(eteamvo.getStatus()).append("','")
		         .append(eteamvo.getTlid()).append("','").append(requestFormDate).append("','")
		         .append(staff.getStaffCode()).append("','"+eteamvo.getOrgChart()+"','"+eteamvo.getDepartment()+"','"+eteamvo.getOrgId()+"')");		
		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}
	public int update(BaseVO vo, HttpServletRequest request) throws DAOException {
		TeamVO eteamvo = (TeamVO)vo;
		String requestFormDate = "";
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		StringBuffer insertSQL = new StringBuffer("update teflow_team ");
		insertSQL.append(" set TEAM_NAME='").append(eteamvo.getTeamName().replaceAll("'", "''")).append("' ");
		if (eteamvo.getSuperiorsCode() != null && !"".equals(eteamvo.getSuperiorsCode())) {
            insertSQL.append(",SUPERIORS_CODE=").append(eteamvo.getSuperiorsCode());
        } else {
            insertSQL.append(",SUPERIORS_CODE=-1");
        }
		insertSQL.append(",TL_ID='").append(eteamvo.getTlid().trim())
				 .append("',Status='").append(eteamvo.getStatus())
				 .append("',Modified_date='").append(requestFormDate.trim())
				 .append("',Modified_Staff='").append(staff.getStaffCode().trim())
				 .append("',org_chart='"+eteamvo.getOrgChart())
				 .append("',department='"+eteamvo.getDepartment()+"', ")
				 .append("org_id='").append(eteamvo.getOrgId()).append("' ")
				 .append(" where TEAM_CODE=").append(eteamvo.getTeamCode().trim());		
		//System.out.println(insertSQL.toString());
		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}
	

	public int saveHistory(BaseVO vo, HttpServletRequest request) throws DAOException {
		TeamVO eteamvo = (TeamVO)vo;
		String requestFormDate = "";
		requestFormDate = StringUtil.getDateStr(new java.util.Date(),"MM/dd/yyyy HH:mm:ss");
		StaffVO staff = (StaffVO)request.getSession().getAttribute(CommonName.CURRENT_STAFF_INFOR);
		
		StringBuffer insertSQL = new StringBuffer("insert into teflow_team_History ");
		insertSQL.append(" values(").append(eteamvo.getTeamCode()).append(",'").append(eteamvo.getTeamName().replaceAll("'", "''")).append("',");
		if (eteamvo.getSuperiorsCode()==null||"".equals(eteamvo.getSuperiorsCode())){
			insertSQL.append("-1").append(",'");
		}else{
			insertSQL.append(eteamvo.getSuperiorsCode()).append(",'");
		}				         
		insertSQL.append(eteamvo.getStatus()).append("','")
		         .append(eteamvo.getTlid()).append("','").append(requestFormDate).append("','")
		         .append(staff.getStaffCode()).append("','"+eteamvo.getOrgChart()+"','"+eteamvo.getOrgId()+"')");		
		dbManager.executeUpdate(insertSQL.toString());
		return 0;
	}
	
	public int saveT2(BaseVO vo, HttpServletRequest request) throws DAOException {

		TeamVO eteamvo = (TeamVO)vo;
		 
		if (isTableTeamT2Exists()) {
			String deleteSQL = new String("delete from teflow_team_t2 where team_code=?");
			dbManager.executeUpdate(deleteSQL.toString(),new Object[]{eteamvo.getTeamCode()},new int[]{DataType.INT});
			
			String insertSQL = new String("insert into teflow_team_t2(team_code, t2_code) values(?, ?)");
			Object[] obj = new Object[2];
			obj[0] = eteamvo.getTeamCode();
			obj[1] = eteamvo.getT2Code();
			
			int[] dataType = {DataType.INT,DataType.VARCHAR};
			dbManager.executeUpdate(insertSQL.toString(),obj,dataType);
		}
		
		return 0;
	}
	/**
	 * 某个员工改变NTID时，需要修改他领导的所有team的tlid信息
	 * @param oNTID 旧NTID
	 * @param NTID 新NTID
	 * @return 0
	 * @throws DAOException
	 */
	public int updateNTIDForTL(String oNTID, String NTID) throws DAOException {

		String sql = "update teflow_team set tl_id ='"+NTID+"' where tl_id ='"+oNTID+ "' ";		
		dbManager.executeUpdate(sql);
		return 0;
	}
	
	/**
	 * 检查有无teflow_team_t2
	 * @return true/false
	 * @throws DAOException
	 */
	private boolean isTableTeamT2Exists() throws DAOException {
		
		Connection conn = dbManager.getJDBCConnection();
		ResultSet rs;
		try {
			rs = conn.getMetaData().getTables(null, null,  "teflow_team_t2", null );
		 
			if (rs.next())  {
				return true;
			}else  {
				return false;				
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Sync Dept. flag from PMA 2013-01-08
	 * @return 0
	 * @throws DAOException
	 */
	public int syncDeptFromPMA() throws DAOException {

		StaffDAO staffdao = new StaffDAO(dbManager);
		ApproverGroupMemberDAO memberDao = new ApproverGroupMemberDAO(dbManager);
		String sql = "select team_code from "+ParamConfigHelper.getInstance().getParamValue("PMA_DB")
			+".dbo.tpma_team where is_dept='Y' and status='A'";
		Collection<HashMap> list = dbManager.query(sql);

		dbManager.startTransaction();
		sql = "delete from teflow_approver_group_member where approver_group_id='12'";
		dbManager.executeUpdate(sql);

		sql = "delete from teflow_teamtype_mapping";
		dbManager.executeUpdate(sql);
		
		for(HashMap map:list){
			String teamCode = (String)map.get("TEAM_CODE");
			markDeptDeep(teamCode, staffdao, memberDao);
		}
		dbManager.commit();
		return 0;
	}
	/**
	 * 被syncDeptFromPMA调用，[递归向上] 2013-01-08
	 * 
	 * @throws DAOException
	 */
	private void markDeptDeep(String teamCode, StaffDAO staffdao, ApproverGroupMemberDAO memberdao) throws DAOException {

		StaffTeamHelper teamHelper = StaffTeamHelper.getInstance();
		
		ViewTeamTypeVO vttvo = new ViewTeamTypeVO();
		vttvo.setTeamCode(teamCode);
		vttvo.setTeam_type("Y");
		
		if (this.Isexist(vttvo)) {
			updateteamconf(vttvo);
		} else {
			insertteamconf(vttvo);
		}
		String staffCode = staffdao.getStaffCodeByLogonId(teamHelper.getTeamByCode(teamCode).getTlid());
		ApproverGroupMemberVO apgm = new ApproverGroupMemberVO();
		apgm.setGroupId("12");
		apgm.setStaffCode(staffCode);
		memberdao.save(apgm);
		
		String superiorTeam = this.getTeamByTeamCode(teamCode).getSuperiorsCode();
		if(!"".equals(superiorTeam.trim())){			
			markDeptDeep(superiorTeam, staffdao, memberdao);
		}
	}
	
	public int delete(ArrayList teamList, HttpServletRequest request) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
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
