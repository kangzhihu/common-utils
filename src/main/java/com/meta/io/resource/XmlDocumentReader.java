package com.meta.io.resource;

import com.meta.file.XmlUtils;
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
 * &nbsp;&nbsp;&nbsp;&nbsp;xml文件common解析器,非SAX解析方式<br/><br/>
 * 另一个解析工具{@link XmlUtils}以sax 方式进行解析
 */
public class XmlDocumentReader {

    //注意操作完后需要关闭inputStream
    public Document getXmlDocument(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        return docBuilder.parse(inputStream);
    }

    //注意操作完后需要关闭inputStream
    public Element getXmlDocumentRootElemen(InputStream inputStream) throws Exception {
        Document doc = getXmlDocument(inputStream);
        return doc.getDocumentElement();
    }

    //注意操作完后需要关闭inputStream
    public Element getXmlDocumentRootElement(Resource resource) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(resource.getInputStream());
        return doc.getDocumentElement();
    }
}
