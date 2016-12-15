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
            System.out.println("׼������WebService����http://localhost:8030/Nova/HelloWebService"); 
            //����һ��WebService 
            Endpoint.publish("http://localhost:8030/Nova/HelloWebService", new HelloWebservice()); 
            System.out.println("�ѳɹ�����WebService����http://localhost:8030/Nova/HelloWebService"); 
           

    } 

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException { 
            System.out.println("��Servlet�������κ�ҵ���߼�������yonglai����һ��Web����http://localhost:8030/Nova/HelloWebService"); 
    } 

}
