package com.meta.io;

import com.meta.io.resource.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-11-05&nbsp;17:34<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;xml文件解析器<br/><br/>
 */
public class XmlDocumentReader {

    public Element getXmlDocumentRootElemen(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        return doc.getDocumentElement();
    }

    public Element getXmlDocumentRootElement(Resource resource) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(resource.getInputStream());
        return doc.getDocumentElement();
    }
}
