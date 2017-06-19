package com.meta.support.svg;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-06-17&nbsp;1:28<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;SVG节点类型<br/><br/>
 */
public enum SVGElementType {

    TEXT("0"),//text节点
    BARCODE("1"),//一维码image节点
    QRCODE("2");//二维码image节点

    String type;
    SVGElementType(String type) {
        this.type = type;
    }

    public static SVGElementType of(String type) {
        if ("1".equals(type)) {
            return SVGElementType.BARCODE;
        } else if ("2".equals(type)) {
            return SVGElementType.QRCODE;
        }
        return SVGElementType.TEXT;
    }
}
