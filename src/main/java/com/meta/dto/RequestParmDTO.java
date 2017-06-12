package com.meta.dto;

import java.io.Serializable;
import java.util.Map;

public class RequestParmDTO implements Serializable {

    private static final long serialVersionUID = -8235649960601447283L;

    /** 请求头参数 */
    private Map<String, String> header;

    /** 请求体参数，针对post请求 */
    private Map<String, String> body;

    /** 请求体参数，针对post请求，xml数据格式 */
    private String body4Xml;

    /** 请求体参数，针对post请求，json数据格式 */
    private String body4Json;

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }

    public String getBody4Xml() {
        return body4Xml;
    }

    public void setBody4Xml(String body4Xml) {
        this.body4Xml = body4Xml;
    }

    public String getBody4Json() {
        return body4Json;
    }

    public void setBody4Json(String body4Json) {
        this.body4Json = body4Json;
    }
}
