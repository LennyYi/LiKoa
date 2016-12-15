package com.aiait.eflow.report.dao;

import java.sql.*;
import java.util.*;

import com.aiait.eflow.report.vo.MedicalVO;
import com.aiait.framework.db.*;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

public class MedicalReportDAO extends BaseDAOImpl {

    public MedicalReportDAO(IDBManager dbManager) {
        super(dbManager);
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
    
    public Collection getMedicalSummaryList(String formSystemId,String dateBeginStr, String dateEndStr, String monthBeginStr, String finYeaStr, String teamList) throws DAOException {
       
        String SQL=("select distinct s.staff_code, s.team_code, g.New_grade, b.connubial_name,b.child_name, "
			        +"staffCMonthBegin = b.staff_C_entitlement- ISNULL(mr.staffCMonthBegin, 0),staffCMonthSpent=ISNULL(mr.staffCMonthSpent,0), "
			        +"staffHMonthBegin = b.staff_H_entitlement- ISNULL(mr.staffHMonthBegin, 0),staffHMonthSpent=ISNULL(mr.staffHMonthSpent,0), "
			        +"connubialCMonthBegin = b.connubial_C_entitlement- ISNULL(mr.connubialCMonthBegin, 0),connubialCMonthSpent=ISNULL(mr.connubialCMonthSpent,0), "
			        +"connubialHMonthBegin = b.connubial_H_entitlement- ISNULL(mr.connubialHMonthBegin, 0),connubialHMonthSpent=ISNULL(mr.connubialHMonthSpent,0), "
			        +"childCMonthBegin = b.child_C_entitlement- ISNULL(mr.childCMonthBegin, 0),childCMonthSpent=ISNULL(mr.childCMonthSpent,0), "
			        +"childHMonthBegin = b.child_H_entitlement- ISNULL(mr.childHMonthBegin, 0),childHMonthSpent=ISNULL(mr.childHMonthSpent,0), "
			        +"ISNULL (mbt.totalMonthBeforeTax, 0)  as totalMonthBeforeTax "
			        +"from teflow_user_grade_history g, teflow_medical_balance b, tpma_staff s " 
			        //--The request records before this month, and in this month
			        +"left join ( select staff_code, "
			        			+"SUM (case when Submission_date < @monthBeginStr and type = 'staffC' then actual else 0 end) as staffCMonthBegin, "
			        			+"SUM (case when Submission_date >= @monthBeginStr and type = 'staffC' then actual else 0 end) as staffCMonthSpent, "
			        			+"SUM (case when Submission_date < @monthBeginStr and type = 'staffH' then actual else 0 end) as staffHMonthBegin, "
			        			+"SUM (case when Submission_date >= @monthBeginStr and type = 'staffH' then actual else 0 end) as staffHMonthSpent, "
			        			+"SUM (case when Submission_date < @monthBeginStr and type = 'connubialC' then actual else 0 end) as connubialCMonthBegin, "
			        			+"SUM (case when Submission_date >= @monthBeginStr and type = 'connubialC' then actual else 0 end) as connubialCMonthSpent, "
			        			+"SUM (case when Submission_date < @monthBeginStr and type = 'connubialH' then actual else 0 end) as connubialHMonthBegin, "
			        			+"SUM (case when Submission_date >= @monthBeginStr and type = 'connubialH' then actual else 0 end) as connubialHMonthSpent, "
			        			+"SUM (case when Submission_date < @monthBeginStr and type = 'childC' then actual else 0 end) as childCMonthBegin, "
			        			+"SUM (case when Submission_date >= @monthBeginStr and type = 'childC' then actual else 0 end) as childCMonthSpent, "
			        			+"SUM (case when Submission_date < @monthBeginStr and type = 'childH' then actual else 0 end) as childHMonthBegin, "
			        			+"SUM (case when Submission_date >= @monthBeginStr and type = 'childH' then actual else 0 end) as childHMonthSpent "
			        			+"from(select distinct s.staff_code, f.Submission_date, f.request_no, " 
			        				 +"case when f.status ='01' then s3.field_03_6 else s3.field_03_7 end as actual, "
			        				 +"case when charindex('Employee Clinic',s3.field_03_1)!=0 then 'staffC' "
			        					  +"when charindex('Employee Hospitalization',s3.field_03_1)!=0 then 'staffH' "
			        					  +"when charindex('Connubial Clinic',s3.field_03_1)!=0 then 'connubialC' "
			        					  +"when charindex('Connubial Hospitalization',s3.field_03_1)!=0 then 'connubialH' "
			        					  +"when charindex('Child Clinic',s3.field_03_1)!=0 then 'childC' "
			        					  +"when charindex('Child Hospitalization',s3.field_03_1)!=0 then 'childH' end as type "
			        				 +"from teflow_@formSystemId_01 s1, teflow_@formSystemId_03 s3, teflow_wkf_process f, tpma_staff s "
			        				 +"where s1.request_no = s3.request_no and s1.field_01_5=@finYeaStr "
			        				 +"and s3.request_no =  f.request_no and (f.Submission_date between @dateBeginStr and @dateEndStr ) and f.status <>'00' and f.status <>'03' "
			        				 +"and s.staff_code = f.request_staff_code and (s.status = 'A' or (s.status ='T' and s.to_date >= @dateBeginStr)) @teamList "
			        			+") bs "
			        			+"group by staff_code "
			        +")mr on (s.staff_code = mr.staff_code ) "
			        //--The staff applied "before tax" records in this month
			        +"left join (select SUM (case when f.status ='01' then a.field_02_5 else a.field_02_7 end ) as totalMonthBeforeTax, f.submit_staff_code as submit_staff_code "
								+"from teflow_@formSystemId_02 a, teflow_@formSystemId_01 b, teflow_wkf_process f "
								+"where a.field_02_10 = '1' and a.request_no =  f.request_no and (f.Submission_date between @monthBeginStr and @dateEndStr) and f.status <>'00' and f.status <>'03' "
								+"and b.request_no = a.request_no and b.field_01_5=@finYeaStr "
								+"group by f.submit_staff_code "
					+")mbt on (s.staff_code= mbt.submit_staff_code) "
			        +"where (s.status = 'A' or (s.status ='T' and s.to_date >= @dateBeginStr)) @teamList "
			        +"and g.Staff_code =s.staff_code and g.Effective_date = (select max(Effective_date) from teflow_user_grade_history g2 where g2.Effective_date <= @dateEndStr and g2.Staff_code = s.staff_code ) "
			        +"and b.staff_code =s.staff_code and b.year =@finYeaStr "
			        +"order by s.staff_code ");
       	

        String teamListReplace ="";
        if(teamList !=null && !("").equals(teamList)){
          teamListReplace = " and s.team_code in (" + teamList + ") ";
        }
        SQL=SQL.replace("@teamList", teamListReplace)
            .replace("@monthBeginStr", "'" + monthBeginStr + "'")
        	.replace("@dateBeginStr", "'" + dateBeginStr + "'")
        	.replace("@dateEndStr", "'" + dateEndStr + "'")
        	.replace("@finYeaStr", "'" + finYeaStr + "'")
        	.replace("@formSystemId", formSystemId);

        Connection conn = dbManager.getJDBCConnection();
        PreparedStatement stm = null;
        Collection detailList = new Vector();
       
        try {
        	stm = conn.prepareStatement(SQL);            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
            	MedicalVO medicalDetail = new MedicalVO();
            	medicalDetail.setStaffCode(rs.getString("staff_code"));
            	medicalDetail.setTeamCode(rs.getString("team_code"));
            	medicalDetail.setGrade(rs.getDouble("New_grade"));
            	medicalDetail.setConnubialName(rs.getString("connubial_name"));
            	medicalDetail.setChildName(rs.getString("child_name"));
            	
            	medicalDetail.setStaffCMonthBegin(rs.getDouble("staffCMonthBegin"));
            	medicalDetail.setStaffCMonthSpent(rs.getDouble("staffCMonthSpent"));
            	medicalDetail.setStaffHMonthBegin(rs.getDouble("staffHMonthBegin"));
            	medicalDetail.setStaffHMonthSpent(rs.getDouble("staffHMonthSpent"));          	
            	medicalDetail.setConnubialCMonthBegin(rs.getDouble("connubialCMonthBegin"));
            	medicalDetail.setConnubialCMonthSpent(rs.getDouble("connubialCMonthSpent"));
            	medicalDetail.setConnubialHMonthBegin(rs.getDouble("connubialHMonthBegin"));
            	medicalDetail.setConnubialHMonthSpent(rs.getDouble("connubialHMonthSpent"));        	
            	medicalDetail.setChildCMonthBegin(rs.getDouble("childCMonthBegin"));
            	medicalDetail.setChildCMonthSpent(rs.getDouble("childCMonthSpent"));
            	medicalDetail.setChildHMonthBegin(rs.getDouble("childHMonthBegin"));
            	medicalDetail.setChildHMonthSpent(rs.getDouble("childHMonthSpent"));
            	medicalDetail.setTotalMonthBeforeTax(rs.getDouble("totalMonthBeforeTax"));
            	
            	medicalDetail.calTotal();           	
                detailList.add(medicalDetail);
            }
            return detailList;
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
    }
}
