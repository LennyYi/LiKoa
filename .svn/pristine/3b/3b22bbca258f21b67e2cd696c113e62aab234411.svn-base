package com.aiait.eflow.util;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.servlet.*;
import javax.servlet.http.*;

import jcifs.UniAddress;
import jcifs.smb.*;

import com.aiait.eflow.common.CommonName;

public class LogonFilter implements Filter {

    public static final String WELCOME_PAGE = "/welcome.jsp";
    public static final String LOGON_PAGE = "/login.jsp";
    public static final String LOGON_ERROR_PAGE = "/login_error.jsp";
    public static final String LOGON_ACTION = "/logonAction.it";
    public static final String LOGON_METHOD = "logOn";
    public static final String NONE_SSO = "noneSSO";
    public static final String NTLM_HTTP_FILTER = "NtlmHttpFilter";
    public static final String ENTRY = "entry";

    public static final String PARAM_DOMAIN = "jcifs.smb.client.domain";

    public static final String AUTH_OK = "OK";
    public static final String MESSAGE = "message";

    protected Vector domains;
    protected Vector domainCtrls;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        String action = httpRequest.getRequestURI();
        action = action.substring(httpRequest.getContextPath().length());
        // System.out.println("Action=" + action);

        if (action.equalsIgnoreCase(LOGON_ACTION) && LOGON_METHOD.equalsIgnoreCase(httpRequest.getParameter("method"))) {
            String postData = (String) httpRequest.getParameter("postData");
            System.out.println("From login page with post data: " + postData);
            if (!"yes".equalsIgnoreCase(postData)) {
                // System.out.println("Not from login page or post data disabled by IE initially");
                if (session.getAttribute(NONE_SSO) != null) {
                    if (session.getAttribute(CommonName.CURRENT_STAFF_INFOR) == null) {
                        request.getRequestDispatcher(LOGON_PAGE).forward(request, response);
                    } else {
                        request.getRequestDispatcher(action).forward(request, response);
                    }
                } else {
                    if (session.getAttribute(ENTRY) != null
                            && NONE_SSO.equalsIgnoreCase(httpRequest.getParameter("type"))) {
                        // Post data disabled by IE initially
                        session.setAttribute(NTLM_HTTP_FILTER, "Y");
                        System.out.println("NtlmHttpFilter: Y");
                    }
                    chain.doFilter(request, response);
                }
                return;
            }
            // Process authentication
            String logonId = (String) httpRequest.getParameter("logonId");
            String password = (String) httpRequest.getParameter("password");
            if (logonId == null || logonId.trim().equals("") || password == null || password.trim().equals("")) {
                request.setAttribute(MESSAGE, "Login Id or Password can not be empty!");
                request.getRequestDispatcher(LOGON_PAGE).forward(request, response);
                return;
            }
            String auth = "";
            for (int i = 0; i < this.domains.size(); i++) {
                String domain = (String) this.domains.get(i);
                // System.out.println("domain: " + domain);
                String[] domainCtrls = (String[]) this.domainCtrls.get(i);
                if (domainCtrls != null) {
                    for (int j = 0; j < domainCtrls.length; j++) {
                        // System.out.println("domainCtrl: " + domainCtrls[j]);
                        auth = this.authenticate(domainCtrls[j], domain, logonId, password);
                        if (auth.equals(AUTH_OK)) {
                            break;
                        }
                    }
                } else {
                    auth = this.authenticate(null, domain, logonId, password);
                }
                if (auth.equals(AUTH_OK)) {
                    break;
                }
            }
            if (auth.equals(AUTH_OK)) {
                if (session.getAttribute(CommonName.CURRENT_STAFF_INFOR) == null
                        && session.getAttribute(NTLM_HTTP_FILTER) == null) {
                    session.setAttribute(NONE_SSO, "Y");
                    System.out.println("NONE_SSO: Y");
                }
                request.getRequestDispatcher(action).forward(request, response);
            } else {
                if (auth.indexOf("supporter") != -1) {
                    request.getRequestDispatcher(LOGON_ERROR_PAGE).forward(request, response);
                    return;
                }
                request.setAttribute(MESSAGE, auth);
                request.getRequestDispatcher(LOGON_PAGE).forward(request, response);
            }
            return;
        } else if (action.equals("/") || action.equalsIgnoreCase(WELCOME_PAGE) || action.equalsIgnoreCase(LOGON_PAGE)) {
            session.setAttribute(ENTRY, "Y");
            request.getRequestDispatcher(action).forward(request, response);
        } else if (session.getAttribute(NONE_SSO) != null) {
            if (httpRequest.getMethod().equalsIgnoreCase("POST")) {
                String auth = httpRequest.getHeader("Authorization");
                if (auth != null && auth.startsWith("NTLM ")) {
                    System.out.println("The IE recognized the server with NTLM support.");
                    session.removeAttribute(NONE_SSO);
                    session.setAttribute(NTLM_HTTP_FILTER, "Y");
                    System.out.println("NtlmHttpFilter: Y");
                    chain.doFilter(request, response);
                    return;
                }
            }
            request.getRequestDispatcher(action).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        this.domains = new Vector();
        this.domainCtrls = new Vector();
        String[] domArray = config.getInitParameter(PARAM_DOMAIN).split(";");
        for (int i = 0; i < domArray.length; i++) {
            String[] domStr = domArray[i].split("/");
            this.domains.add(domStr[0]);
            String[] ctrls = null;
            if (domStr.length > 1) {
                ctrls = domStr[1].split(",");
            }
            this.domainCtrls.add(ctrls);
        }
    }

    protected String authenticate(String domainCtrl, String domain, String logonId, String password) {
        try {
            System.out.println("domain: " + domain);
            UniAddress domainController = null;
            if (domainCtrl != null) {
                // domainController = UniAddress.getByName(domainCtrl, true);
                domainController = UniAddress.getByName(domainCtrl);
            } else {
                domainController = UniAddress.getByName(domain, true);
            }
            System.out.println("domainController: " + domainController);
            NtlmPasswordAuthentication creds = new NtlmPasswordAuthentication(domain, logonId, password);
            SmbSession.logon(domainController, creds);
            return AUTH_OK;
        } catch (SmbAuthException ex) {
            System.out.println("SmbAuthException: " + ex.getMessage());
            return "Login failed: unknown login id or bad password.";
        } catch (UnknownHostException ex) {
            System.out.println("UnknownHostException: " + ex.getMessage());
            return "Can not get domain controler for domain: " + domain + ", please contact technique supporter.";
        } catch (SmbException ex) {
            System.out.println("SmbException: " + ex.getMessage());
            System.out.println("NtStatus: " + ex.getNtStatus());
            return "Network problem, please contact technique supporter.";
        }
    }

}
