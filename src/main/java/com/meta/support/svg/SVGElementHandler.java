package com.meta.support.svg;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.zxing.WriterException;
import com.meta.io.QRCodeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-06-17&nbsp;1:33<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;SVG元素节点操作Hander<br/><br/>
 */
public class SVGElementHandler {
    private final static Logger logger = LoggerFactory.getLogger(SVGElementHandler.class);

    private static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //直接将填充image节点base64数据
    private final static String BASE65_PREFIX = "data:image/png;base64,";

    /**
     * 填充svg数据
     * @param doc svg root document
     * @param svgElements data element
     */
    public static void padding(SVGDocument doc, Collection<SVGElement> svgElements) {
        if (svgElements == null) {
            logger.warn(" svgElements cannot be empty ");
            return;
        }
        for (SVGElement e : svgElements) {
            String content = SVGElementHandler.dispose(e.getContent(), e.getType());
            padding(doc, e.getType(), e.getName(), content, e.getSplitSize());
        }
    }

    /**
     * 根据name属性值查找节点
     * @param doc
     * @param name
     * @return
     */
    public static NodeList getElementByName(SVGDocument doc, String name) {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        NodeList nl = null;
        try {
            XPathExpression expr = xpath.compile("//*[@name=\"" + name + "\"]");
            nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return nl;

    }

    /**
     * 将内容填充到依据name查找的(多个)节点中
     * @param doc svg root document
     * @param type 填充数据类型
     * @param name 要填充的节点name属性值
     * @param content 要填充的内容
     * @param splitSize 对内容填充的text节点数
     */
    public static void padding(SVGDocument doc, SVGElementType type, String name, String content, int splitSize) {
        NodeList nodeList = getElementByName(doc, name);
        if (nodeList == null) {
            logger.warn(" svg dom node not found with name : {}", name);
            return;
        }
        if (content == null) {
            logger.warn(" transformed can not empty with name : {}", name);
            return;
        }
        int size = nodeList.getLength();
        // repeated element with diff name
        if (splitSize == 0) {
            for (int i = 0; i < size; i++) {
                if (type == SVGElementType.BARCODE || type == SVGElementType.QRCODE) {
                    NamedNodeMap attrs = nodeList.item(i).getAttributes();
                    attrs.getNamedItemNS("http://www.w3.org/1999/xlink", "href").setNodeValue(content);
                } else {
                    nodeList.item(i).setTextContent(content);
                }
            }
        } else {
            // 将内容依次填充到紧邻的后面text节点中
            String[] splits = Iterables.toArray(Splitter.fixedLength(splitSize).split(content), String.class);
            int length = splits.length;
            int finalSize = length < size ? length : size;
            for (int i = 0; i < finalSize; i++) {
                nodeList.item(i).setTextContent(splits[i]);
            }
        }
    }

    /**
     * 将内容填充到依据id查找的第一个节点中
     * @param doc svg root document
     * @param type 节点类型
     * @param id 节点id
     * @param content 填充内容
     */
    public static void padding(SVGDocument doc, SVGElementType type, String id, String content) {
        if (doc == null) {
            logger.warn("document not found ");
            return;
        }
        Element e = doc.getElementById(id);
        if (e == null) {
            logger.warn("element not found with id : {} ", id);
            return;
        }
        if (type == SVGElementType.BARCODE || type == SVGElementType.QRCODE) {
            e.setAttributeNS("http://www.w3.org/1999/xlink", "href", content);
        } else {
            e.setTextContent(content);
        }
    }

    /**
     * 根据type类型，解析转换出内容
     * @param content
     * @param type
     * @return
     */
    public static String dispose(String content, SVGElementType type) {
        String result = "";
        switch (type) {
            case BARCODE:
                result = BarCodeConverter.convert(content,null,null);
                break;
            case QRCODE:
                result = QrCodeConverter.convert(content,null,null);
                break;
            default:
                result = TextConverter.convert(content);
                break;
        }
        return result;
    }

    private final static class TextConverter {
        static String convert(String content) {
            return content;
        }
    }

    /**
     * 条形码生成类
     */
    private final static class BarCodeConverter {
        static String convert(String content, Integer width, Integer height) {
            String destPath = System.currentTimeMillis() + "_" + StringUtils.substring(content,0,32)+"_barcode.jpg";
            byte[] b = null;
            try {
                QRCodeUtil.encodeBarCode128(content, destPath, width, height, 0);
                b = FileUtils.readFileToByteArray(new File(destPath));
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            } finally {
                threadPool.execute(() -> {
                    try {
                        FileUtils.forceDelete(new File(destPath));
                        logger.info(" has deleted file : {}", destPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.warn(" deleted file : {} error occurs : {} ", destPath, e.getMessage());
                    }
                });
            }
            return BASE65_PREFIX + Base64Utils.encodeToString(b);
        }
    }

    /**
     * 二维码生成类
     */
    private final static class QrCodeConverter {
        static String convert(String content, Integer width, Integer height) {
            String destPath = System.currentTimeMillis() + "_" + StringUtils.substring(content,0,32)+"_qrcode.jpg";
            byte[] b = null;
            try {
                QRCodeUtil.encode(content, destPath , width, height,0);
                b = FileUtils.readFileToByteArray(new File(destPath));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                threadPool.execute(() -> {
                    try {
                        FileUtils.forceDelete(new File(destPath));
                        logger.info(" has deleted file : {}", destPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.warn(" deleted file : {} error occurs : {} ", destPath, e.getMessage());
                    }
                });

            }
            return BASE65_PREFIX + Base64Utils.encodeToString(b);
        }
    }


}
