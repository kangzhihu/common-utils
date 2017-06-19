package com.meta.support.svg;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-06-17&nbsp;1:26<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/> 
 * &nbsp;&nbsp;&nbsp;&nbsp;SVG数据节点<br/><br/>
 */
public class SVGElement {
    private String id;
    private String name;
    private String content;

    private Integer splitSize = 0;

    private SVGElementType type = SVGElementType.TEXT;

    public SVGElement() {
    }

    public SVGElement(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public SVGElement(String id, String name, String content) {
        this(id, content);
        this.name = name;
    }

    public SVGElement(String id, String name, String content, int splitSize) {
        this(id, content);
        this.name = name;
        this.splitSize = splitSize;
    }

    public SVGElement(String id, String content, SVGElementType type) {
        this(id, content);
        this.type = type;
    }

    public SVGElement(String id, String name, String content, SVGElementType type) {
        this(id, content, type);
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSplitSize() {
        return splitSize;
    }

    public void setSplitSize(Integer splitSize) {
        this.splitSize = splitSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SVGElementType getType() {
        return type;
    }

    public void setType(SVGElementType type) {
        this.type = type;
    }

}
