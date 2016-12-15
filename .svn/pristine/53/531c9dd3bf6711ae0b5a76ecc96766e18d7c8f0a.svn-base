package com.aiait.eflow.util;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import com.aiait.eflow.common.CommonName;

/**
 * SessionCheckingFilter
 * 
 * @version 2010-11-18
 */
public class SessionCheckingFilter implements Filter {

    public static final String SERVICE_ACTION = "Service.it";
    public static final String SERVICE_METHOD = "Svc";
    public static final String TEST_JSP = "Test.jsp";

    public static final String TEAM_ACTION = "/teamManageAction.it";
    public static final String TEAMLIST_METHOD = "getTeamList";
    public static final String FORM_ACTION = "/formManageAction.it";
    public static final String EDITFORM_METHOD = "editForm";

    private String loginFile; // store the login page or action
    private String ignoreFiles; // the files needn't check session

    /**
     * Initialize the filter, get the parameter from config
     * 
     * @param filterConfig
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        loginFile = filterConfig.getInitParameter("loginFile");
        ignoreFiles = filterConfig.getInitParameter("ignoreFiles");
    }

    /**
     * Release the variable
     */
    public void destroy() {
        loginFile = null;
    }

    /**
     * Select and set (if specified) the character encoding to be used to interpret request parameters for this request.
     * 
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     * @param chain
     *            The filter chain we are processing
     * @throws java.io.IOException
     *             if an input/output error occurs
     * @throws ServletException
     *             if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        boolean nextFilter = true;

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        String action = httpRequest.getRequestURI();

        if (httpRequest.getContextPath() != null && !"".equals(httpRequest.getContextPath())) {
            action = action.substring(httpRequest.getContextPath().length());
        }
        String method = httpRequest.getParameter("method");
        method = method == null ? "" : method.trim();

        // System.out.println("action=" + action);
        // System.out.println("--"+ignoreFiles.indexOf(action));

        if (action.endsWith(".js") || action.endsWith(".css") || action.endsWith(".gif")
                || action.endsWith(SERVICE_ACTION) || action.endsWith(TEST_JSP)
                || (action.endsWith(".it") && method.endsWith(SERVICE_METHOD))
                || (action.equalsIgnoreCase(TEAM_ACTION) && TEAMLIST_METHOD.equalsIgnoreCase(method))
                || (action.equalsIgnoreCase(FORM_ACTION) && EDITFORM_METHOD.equalsIgnoreCase(method))) {
            // System.out.println(action);
            if (session == null || session.getAttribute(CommonName.CURRENT_STAFF_INFOR) == null) {
                request.getRequestDispatcher(action).forward(request, response);
            } else {
                chain.doFilter(request, response);
            }
            return;
        }

        if (ignoreFiles.indexOf(action) < 0) {
            if (session == null) {
                nextFilter = false;
            } else if (session.getAttribute(CommonName.CURRENT_STAFF_INFOR) == null) {
                nextFilter = false;
                session.invalidate();
            }
        }

        if (nextFilter) {
            // Pass control on to the next filter
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(httpRequest.getContextPath() + loginFile + "?IS_TIMEOUT=1");
        }
    }

}