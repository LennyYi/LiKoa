package com.aiait.eflow.util;

import javax.servlet.*;

import jcifs.Config;

import com.aiait.framework.mvc.util.Global;

/**
 * Used for encrypted password.
 * 
 * @author Tinger Li
 */
public class NtlmHttpFilter extends jcifs.http.NtlmHttpFilter {

    public static final String KEY_PASSWORD = "jcifs.smb.client.password";

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        try {
            String password = Config.getProperty(KEY_PASSWORD);
            if (password != null) {
                String keyFile = filterConfig.getServletContext().getRealPath(
                        Global.AIAITMVC_CONFIG_XML_FILEPATH + EncryptUtil.KEY_FILE_NAME);
                // System.out.println("keyFile: " + keyFile);
                EncryptUtil.defaultKeyFile = keyFile;
                EncryptUtil encrypt = new EncryptUtil();
                password = encrypt.UnryptDES(password);
                Config.setProperty(KEY_PASSWORD, password);
                // System.out.println("password: " + password);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
