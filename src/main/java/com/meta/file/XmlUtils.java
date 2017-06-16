package com.meta.file;

import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * XML工具类
 * Created by Administrator on 2017/6/12.
 */
public class XmlUtils {
    private final static transient Logger logger = LoggerFactory.getLogger(XmlUtils.class);
    public static String formatXml(String str, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            formatXml(str, encoding, writer);
            writer.close();
            return writer.toString();
        } catch (Exception e) {
            logger.warn(str + e);
            return str;
        }
    }
    /**
     * 直接写入，不关闭writer
     * @param str
     * @param encoding
     * @param writer
     * @throws IOException
     */
    public static void formatXml(String str, String encoding, Writer writer) throws IOException {
        OutputFormat format = OutputFormat.createCompactFormat();
        format.setEncoding(encoding);
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        try {
            org.dom4j.Document document = DocumentHelper.parseText(str);
            xmlWriter.write(document);
        } catch (DocumentException e) {
            logger.warn(str + e);
        }

    }

    /**
     * 读取XML文件
     * @param xml
     * @return
     */
    public static Document getDocument(String xml){
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new CharArrayReader(xml.toCharArray()));
        } catch (DocumentException e) {
            logger.error(e.getMessage());;
        }
        return document;
    }

    public static String filterInvalid(String xml){
        StringBuffer sb = new StringBuffer(xml);
        for(int i=0;i<sb.length();i++){
            int c = sb.charAt(i);
            if(c < 0x20 && c!= 0x09/*\t*/ && c!=0x0A/*\n*/ && c!= 0x0D/*\r*/){
                sb.delete(i, i+1);
            }
        }
        return sb.toString();
    }
    /**
     * @param xml
     * @param xpath
     * @return
     */
    public static String getNodeText(String xml, String xpath){
        return getNodeText(getDocument(xml), xpath);
    }
    /**
     * @param document
     * @param xpath
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getNodeText(Document document, String xpath){
        if(document == null) return null;
        try{
            List<Node> nodeList = document.selectNodes(xpath);
            if(nodeList.isEmpty()) return null;
            return getText(nodeList.get(0));
        }catch(Exception e){
            logger.error(document.getText() + e);
            return null;
        }
    }
    public static List<String> getNodeTextList(String xml, String xpath, boolean ignoreEmpty){
        return getNodeTextList(getDocument(xml), xpath, ignoreEmpty);
    }

    @SuppressWarnings("unchecked")
    public static List<String> getNodeTextList(Document document, String xpath, boolean ignoreEmpty){
        List<String> result = new ArrayList<String>();
        if(document == null) return result;
        try{
            List<Node> nodeList = document.selectNodes(xpath);
            for(Node node: nodeList){
                String s = getText(node);
                if(StringUtils.isNotBlank(s) || !ignoreEmpty) result.add(s);
            }
        }catch(Exception e){
            logger.error(document.getText() + e);
        }
        return result;
    }
    private static String getText(Node node){
        if(node instanceof Attribute){
            return ((Attribute)node).getValue();
        }else{
            return node.getText();
        }
    }
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> xml2Map(String infoXML) {
        Document document;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            document = DocumentHelper.parseText(infoXML);
            Element root = document.getRootElement();
            Iterator it = root.elements().iterator();
            while (it.hasNext()) {
                Element info = (Element) it.next();
                map.put(info.getName(), info.getText());
                Iterator itc = info.elements().iterator();
                while (itc.hasNext()) {
                    Element infoc = (Element) itc.next();
                    map.put(infoc.getName(), infoc.getText());
                }
            }
        } catch (DocumentException e1) {
            logger.warn(e1.getMessage());;
        }
        return map;
    }
}
