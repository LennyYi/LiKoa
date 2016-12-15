package com.aiait.eflow.util;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.AuthorityHelper;
import com.aiait.eflow.housekeeping.vo.StaffVO;

public class AuthorityCheckingFilter implements Filter {
    private String messageFile;

    public void init(FilterConfig filterConfig) throws ServletException {
        messageFile = filterConfig.getInitParameter("messageFile");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        boolean nextFilter = true;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        // 获取传入的两个参数，moduleId 和 operateId
        String moduleId = (String) request.getParameter("moduleId");
        String operateId = (String) request.getParameter("operateId");
        // 只有当两个参数都存在时才能进行当前操作的权限检查
        if (moduleId != null && operateId != null) {
            StaffVO currentStaff = (StaffVO) session.getAttribute(CommonName.CURRENT_STAFF_INFOR);
            AuthorityHelper authority = AuthorityHelper.getInstance();
            if (!authority.checkAuthority(currentStaff.getCurrentRoleId(), Integer.parseInt(moduleId), Integer
                    .parseInt(operateId))) {
                nextFilter = false;
            }
        }

        if (nextFilter) {
            // Pass control on to the next filter
            chain.doFilter(request, response);
        } else {
            String error = "You have not authority for this function!";
            httpRequest.setAttribute("error", "You have not authority for this function!");
            ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + messageFile + "?error="
                    + error);
        }
    }

    public void destroy() {
        messageFile = null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    }

}
