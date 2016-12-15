package com.aiait.eflow.util;

import java.io.OutputStream;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * XML Utility
 * 
 * @version 2010-12-07
 */
public class XMLUtil {

    protected static DocumentBuilderFactory docBuilderFactory;
    protected static DocumentBuilder docBuilder;

    static {
        try {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Document newDocument(String root) throws Exception {
        Document document = docBuilder.newDocument();
        document.appendChild(document.createElement(root));
        return document;
    }

    public static void writeXML(Document doc, OutputStream out, String encoding) throws Exception {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.ENCODING, encoding);
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(out));
    }

}
