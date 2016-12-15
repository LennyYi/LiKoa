package com.aiait.nova.webservices.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.ws.Endpoint;

import com.aiait.nova.webservices.*;

public class WebServiceServlet extends GenericServlet {
	@Override 
    public void init(ServletConfig servletConfig) throws ServletException { 
            super.init(servletConfig); 
            System.out.println("准备启动WebService服务：http://localhost:8030/Nova/HelloWebService"); 
            //发布一个WebService 
            Endpoint.publish("http://localhost:8030/Nova/HelloWebService", new HelloWebservice()); 
            System.out.println("已成功启动WebService服务：http://localhost:8030/Nova/HelloWebService"); 
           

    } 

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException { 
            System.out.println("此Servlet不处理任何业务逻辑，仅仅yonglai发布一个Web服务：http://localhost:8030/Nova/HelloWebService"); 
    } 

}
