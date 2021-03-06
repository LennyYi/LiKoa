package com.aiait.eflow.wkf.util;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import com.aiait.eflow.util.StringUtil;
import com.aiait.eflow.wkf.vo.*;

public class WorkFlowXmlUtil {

    public static Collection parseXmlData(String xmlData) throws Exception {
        // System.out.println(xmlData);
        Collection resultList = new ArrayList();
        try {
            StringReader sr = new StringReader(xmlData);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = builder.parse(is);

            NodeList itemList = xmlDoc.getElementsByTagName("OnlySp_FlowDetail");
            if (itemList != null) {
                for (int i = 0; i < itemList.getLength(); i++) {
                    Node item = itemList.item(i);

                    WorkFlowItemVO vo = new WorkFlowItemVO();
                    for (Node node = item.getFirstChild(); node != null; node = node.getNextSibling()) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            if (node.getNodeName().equals("FlowBaseID")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setFlowId(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("Name")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setName(node.getFirstChild().getNodeValue());
                                }
                            }
                            // IT0958 DS-004 Begin
                            if (node.getNodeName().equals("NodeAlias")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setNodeAlias(node.getFirstChild().getNodeValue());
                                }
                            }
                            // IT0958 DS-004 End
                            if (node.getNodeName().equals("NodeType")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setItemType(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("processorField")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setProcessorField(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("companyField")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setCompanyField(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("delaytimeField")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setDelaytimeField(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("approveHandle")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setApproveHandle(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("rejectHandle")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setRejectHandle(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("approveAlias")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setApproveAlias(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("rejectAlias")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setRejectAlias(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("DepartID")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setItemId(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("PriDepID")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    vo.setPriDepId(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("ApproverList")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    String tmpIdStr = node.getFirstChild().getNodeValue();
                                    String[] approverId = StringUtil.split(tmpIdStr, ",");
                                    String approverGroup = "";
                                    String approverStaff = "";
                                    for (int j = 0; j < approverId.length; j++) {
                                        // && approverId[j].toUpperCase().indexOf("C")==0
                                        // staffCode的长度 不一定是5，但是approverGroupId的长度是2
                                        if (approverId[j].trim().length() == 2) {
                                            approverGroup = approverGroup + approverId[j].trim() + ",";
                                        } else {
                                            approverStaff = approverStaff + approverId[j].trim() + ",";
                                        }
                                    }
                                    // 去掉尾部多余的一个符号“,”
                                    if (!"".equals(approverGroup)) {
                                        approverGroup = approverGroup.trim();
                                        approverGroup = approverGroup.substring(0, approverGroup.length() - 1);
                                    }
                                    if (!"".equals(approverStaff)) {
                                        approverStaff = approverStaff.trim();
                                        approverStaff = approverStaff.substring(0, approverStaff.length() - 1);
                                    }
                                    vo.setApproveGroupId(approverGroup);
                                    vo.setApprovestaffCode(approverStaff);
                                }
                            }
                            if (node.getNodeName().equals("UpdateSections")) {

                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    String sections = node.getFirstChild().getNodeValue();
                                    if (sections != null && !"".equals(sections)) {
                                        sections = sections.trim();
                                        if ((sections.substring(sections.length() - 1)).equals(",")) {
                                            vo.setUpdateSections(sections.substring(0, sections.length() - 1));
                                        } else {
                                            vo.setUpdateSections(sections);
                                        }
                                    }
                                } else {
                                    vo.setUpdateSections("");
                                }
                            }
                            if (node.getNodeName().equals("NewSectionFields")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    String sectionFieldIds = node.getFirstChild().getNodeValue();
                                    if (sectionFieldIds != null && !"".equals(sectionFieldIds)) {
                                        sectionFieldIds = sectionFieldIds.trim();
                                        if ((sectionFieldIds.substring(sectionFieldIds.length() - 1)).equals(",")) {
                                            vo.setFillSectionFields(sectionFieldIds.substring(0,
                                                    sectionFieldIds.length() - 1));
                                        } else {
                                            vo.setFillSectionFields(sectionFieldIds);
                                        }
                                    }
                                } else {
                                    vo.setFillSectionFields("");
                                }
                            }
                            
                            if (node.getNodeName().equals("HiddenSectionFields")) {
                                if (node.getFirstChild() != null && node.getFirstChild().getNodeValue() != null) {
                                    String sectionFieldIds = node.getFirstChild().getNodeValue();
                                    if (sectionFieldIds != null && !"".equals(sectionFieldIds)) {
                                        sectionFieldIds = sectionFieldIds.trim();
                                        if ((sectionFieldIds.substring(sectionFieldIds.length() - 1)).equals(",")) {
                                            vo.setHiddenSectionFields(sectionFieldIds.substring(0,
                                                    sectionFieldIds.length() - 1));
                                        } else {
                                            vo.setHiddenSectionFields(sectionFieldIds);
                                        }
                                    }
                                } else {
                                    vo.setHiddenSectionFields("");
                                }
                            }
                            
                            
                            
                            if (node.getNodeName().equals("LimiteDate")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setLimiteDate(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("PosX")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setPosX(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("PosY")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setPosY(node.getFirstChild().getNodeValue());
                                }
                            }
                        }
                    }
                    resultList.add(vo);
                }
            }
            xmlDoc = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static Collection readXmlFile(String fileName) throws Exception {
        Collection resultList = new ArrayList();

        DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dombuilder = domfac.newDocumentBuilder();

            InputStream is = new FileInputStream(fileName);
            Document doc = dombuilder.parse(is);

            NodeList itemList = doc.getElementsByTagName("OnlySp_FlowDetail");
            if (itemList != null) {
                for (int i = 0; i < itemList.getLength(); i++) {
                    Node item = itemList.item(i);

                    WorkFlowItemVO vo = new WorkFlowItemVO();
                    for (Node node = item.getFirstChild(); node != null; node = node.getNextSibling()) {
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            if (node.getNodeName().equals("FlowBaseID")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setItemId(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("Name")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setName(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("NodeAlias")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setNodeAlias(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("DepartID")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setItemId(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("PriDepID")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setPriDepId(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("LimiteDate")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setLimiteDate(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("PosX")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setPosX(node.getFirstChild().getNodeValue());
                                }
                            }
                            if (node.getNodeName().equals("PosY")) {
                                if (node.getFirstChild().getNodeValue() != null) {
                                    vo.setPosY(node.getFirstChild().getNodeValue());
                                }
                            }
                        }
                    }
                    resultList.add(vo);
                }
            }
            doc = null;
            is.close();
            domfac = null;
            // System.out.println("size="+resultList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // StringBuffer xmldata = new StringBuffer("<?xml version=\"1.0\" encoding=\"gb2312\"?>");

        // if(resultList.size()>0){

        // }

        return resultList;
    }

    public static String readXmlFileNs(String fileName) throws Exception {
        Collection resultList = new ArrayList();

        DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dombuilder = domfac.newDocumentBuilder();
            InputStream is = new FileInputStream(fileName);
            Document doc = dombuilder.parse(is);

            doc.getDocumentElement().normalize();
            Node node = doc.getFirstChild();
            NodeList items = node.getChildNodes();

            // Element root=doc.getDocumentElement();
            // NodeList items = root.getChildNodes();

            if (items != null) {
                for (int i = 0; i < items.getLength(); i++) {
                    Node item = items.item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println(i + ":" + item.getNodeName());
                        NodeList childs = item.getChildNodes();
                        System.out.println("");
                        if (childs != null) {
                            // WorkFlowItemVO vo = new WorkFlowItemVO();
                            for (int j = 0; j < childs.getLength(); j++) {
                                Node child = childs.item(j);
                                if (child.getNodeType() == Node.ELEMENT_NODE) {
                                    if ("FlowBaseID".equals(child.getNodeName())) {
                                        System.out.println("    FlowBaseID=" + child.getNodeValue());
                                    }
                                    if ("DepartID".equals(child.getNodeName())) {
                                        System.out.println("    DepartID=" + child.getNodeValue());
                                    }
                                    if ("PriDepID".equals(child.getNodeName())) {
                                        System.out.println("    PriDepID=" + child.getNodeValue());
                                    }
                                    if ("LimiteDate".equals(child.getNodeName())) {
                                        System.out.println("    LimiteDate=" + child.getNodeValue());
                                    }
                                    if ("PosX".equals(child.getNodeName())) {
                                        System.out.println("    PosX=" + child.getNodeValue());
                                    }
                                    if ("PosY".equals(child.getNodeName())) {
                                        System.out.println("    PosY=" + child.getNodeValue());

                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String transDataForXml(WorkFlowVO flow) {
        StringBuffer xmlData = new StringBuffer("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
        xmlData.append("<WorkFlow>");
        if (flow.itemList != null && flow.itemList.size() > 0) {
            Iterator it = flow.itemList.iterator();
            xmlData.append("<WorkFlowItem>");
            while (it.hasNext()) {
                WorkFlowItemVO item = (WorkFlowItemVO) it.next();
                xmlData.append("<ItemID>").append(item.getItemId()).append("</ItemID>").append("<Name>")
                        .append(item.getName()).append("</Name>").append("<LimiteDate>").append(item.getLimiteDate())
                        .append("</LimiteDate>").append("<PosX>").append(item.getPosX()).append("</PosX>")
                        .append("<PosY>").append(item.getPosY()).append("</PosY>");

            }
            xmlData.append("</WorkFlowItem>");
            // xmlData.append("<Relation></Relation>");
        }
        xmlData.append("</WorkFlow>");
        return xmlData.toString();
    }

    public static void main(String[] args) throws Exception {
        // XmlUtil.readXmlFile("WorkFlowData.xml");
        String s = "asdfa,asfd,asdfasd";
        String[] t = s.split(",");
        // for(int i=0;i<t.length;i++){
        // System.out.println(t[i]);
        // }
        System.out.println("10C343,".substring(0, "10C343,".length() - 1));
    }

}
