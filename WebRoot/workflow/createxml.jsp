<%@ page contentType="text/html; charset=gb2312"%>
<%@page import="com.aiait.eflow.wkf.vo.*,java.util.*,com.aiait.eflow.wkf.util.*" %>

<%
String type = (String)request.getParameter("type");
WorkFlowVO flow = new WorkFlowVO();
if("create".equals(type) || "load".equals(type)){
   if("create".equals(type)){
			flow.setFlowBaseId(1);			
			WorkFlowItemVO beginItem = new WorkFlowItemVO();
			beginItem.setItemId("0");
			beginItem.setName("开始节点");
			beginItem.setLimiteDate("0");
			beginItem.setPosX("1000px");
			beginItem.setPosY("1400px");
			
			WorkFlowItemVO endItem = new WorkFlowItemVO();
			endItem.setItemId("-1");
			endItem.setName("结否节点");
			endItem.setLimiteDate("0");
			endItem.setPosX("5000px");
			endItem.setPosY("1400px");	
			
			Collection itemList = new ArrayList();
			itemList.add(beginItem);
			itemList.add(endItem);

			flow.setItemList(itemList);
			
	}else if("load".equals(type)){
		String flowId = (String)request.getParameter("flowId");
		String path = pageContext.getServletContext().getRealPath("/");
	   Collection itemList = WorkFlowXmlUtil.readXmlFile(path+"/xmldata/"+flowId+".xml");
	   	flow.setFlowBaseId(34);
	   	flow.setItemList(itemList);
	}		
      response.setContentType("text/xml;charset=gb2312");
               
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma","no-cache");
            
			//String xmlData = XmlUtil.transDataForXml(flow);
			
			//out.println(xmlData);

       out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
			 out.println("<WorkFlow>");
			 if(flow.itemList!=null && flow.itemList.size()>0){
        	  Iterator it = flow.itemList.iterator();
        	  
        	  while(it.hasNext()){
			     	 out.println("<WorkFlowItem>");
        		 WorkFlowItemVO item = (WorkFlowItemVO)it.next();
        		 out.println("<ItemID>"+item.getItemId()+"</ItemID>");
        		 out.println("<Name>"+item.getName()+"</Name>");
        		 out.println("<LimiteDate>"+item.getLimiteDate()+"</LimiteDate>");
        		 out.println("<PosX>"+item.getPosX()+"</PosX>");
        		 out.println("<PosY>"+item.getPosY()+"</PosY>");
				     out.println("</WorkFlowItem>");
				     if(!item.getItemId().equals("0")){
				      if(item.getPriDepId()!=null && !"".equals(item.getPriDepId())){
				        if(item.getPriDepId().indexOf(",")>-1){
				           String[] priId = item.getPriDepId().split(",");
				           for(int i=0;i<priId.length;i++){
				             	out.println("<Relation>");
				              out.println("<MasterItem>"+priId[i]+"</MasterItem>");
				              out.println("<SecondItem>"+item.getItemId()+"</SecondItem>");
				              out.println("</Relation>");
				           }
				        }else{
				          out.println("<Relation>");
				          out.println("<MasterItem>"+item.getPriDepId()+"</MasterItem>");
				          out.println("<SecondItem>"+item.getItemId()+"</SecondItem>");
				          out.println("</Relation>");
				        }  
				      } 
				     }
        	 }
  			 
       }
      out.println("</WorkFlow>");
 }else if("save".equals(type)){
	 String path = pageContext.getServletContext().getRealPath("/");
	 String fileName = path+"/xmldata/"+request.getParameter("flowId")+".xml";
	 String xmldata = (String)request.getParameter("xmldata");
	 WorkFlowXmlUtil.parseXmlData(xmldata);
	 out.println("Work Flow save successfully!");
 }
%>