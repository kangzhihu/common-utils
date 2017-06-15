package com.meta.util.http;

import com.meta.dto.RequestParmDTO;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Description: Http 工具类，在spring环境下推荐使用PoolResultClientUtils
 * Created by zhihu.kang.
 * Time: 2017/6/13 0:18
 * Email:kangzhihu@163.com
 */

public class HttpUtils {

    private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     *
     * @Title: sendGet
     * @Description: 向指定URL发送GET方法的请求，http协议
     * @param url
     *            请求的url
     * @param req
     *            请求参数：包含header、body的参数
     */
    public static String sendGet(String url, RequestParmDTO req) throws Exception {
        String result = "";
        HttpClient client = new HttpClient();

        GetMethod get = new GetMethod(url);
        try {
            // 处理请求参数
            if (null != req){
                // 请求头参数
                setHeaderParm(get, req.getHeader());

                // 请求体参数
                setBobyParm(get, req.getBody());
            }

            log.info("Get Url : " + get.getURI().toString());

            // 发送请求
            int state = client.executeMethod(get);
            if (state == HttpStatus.SC_OK){ // 请求成功
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(get.getResponseBodyAsStream()));

                String line = "";
                while (null != (line = br.readLine())) {
                    result += line;
                }
            } else {
                throw new RuntimeException("http request faile! REQUEST_URL : "
                        + url + ", RESPONSE_CODE : " + state);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            // 释放链接
            get.releaseConnection();
        }
        return result;
    }

    /**
     *
     * @Title: sendPost
     * @Description: 向指定URL发送POST方法的请求，http协议
     * @param url
     *            请求的url
     * @param req
     *            请求参数：包含header、body的参数
     */
    public static String sendPost(String url, RequestParmDTO req) throws Exception {
        String result = "";
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        try {
            // 处理请求参数
            if (null != req){
                // 请求头参数
                setHeaderParm(post, req.getHeader());

                // 请求体参数
                setBobyParm(post, req.getBody());

                // 请求体参数(xml)
                if (StringUtils.isNotEmpty(req.getBody4Xml())){
                    StringRequestEntity xmlEntity = new StringRequestEntity(req.getBody4Xml(), "application/xml","utf-8");
                    post.setRequestEntity(xmlEntity);
                }

                // 请求体参数(json)
                if (StringUtils.isNotEmpty(req.getBody4Json())){
                    StringRequestEntity jsonEntity = new StringRequestEntity(req.getBody4Json(), "application/json","utf-8");
                    post.setRequestEntity(jsonEntity);
                }
            }

            log.info("Post Url : " + post.getURI().toString());

            // 发送请求
            int state = client.executeMethod(post);
            if (state == HttpStatus.SC_OK){ // 请求成功
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(post.getResponseBodyAsStream()));

                String line = "";
                while (null != (line = br.readLine())) {
                    result += line;
                }
            } else {
                throw new RuntimeException("http request faile! REQUEST_URL : "
                        + url + ", RESPONSE_CODE : " + state);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            // 释放链接
            post.releaseConnection();
        }
        return result;
    }

    /**
     *
     * @Title: sendPost
     * @Description: 向指定URL发送POST方法的请求，http协议
     * @param url
     *            请求的url
     * @param req
     *            请求参数：包含header、body的参数
     */
    public static String sendPost(String url, RequestParmDTO req,int timeout) throws Exception {
        String result = "";
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        try {
            // 处理请求参数
            if (null != req){
                // 请求头参数
                setHeaderParm(post, req.getHeader());

                // 请求体参数
                setBobyParm(post, req.getBody());

                // 请求体参数(xml)
                if (StringUtils.isNotEmpty(req.getBody4Xml())){
                    StringRequestEntity xmlEntity = new StringRequestEntity(req.getBody4Xml(), "application/xml","utf-8");
                    post.setRequestEntity(xmlEntity);
                }
            }

            log.info("Post Url : " + post.getURI().toString());
            client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout * 1000);
            client.getHttpConnectionManager().getParams().setSoTimeout(timeout * 1000);
            // 发送请求
            int state = client.executeMethod(post);
            if (state == HttpStatus.SC_OK){ // 请求成功
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(post.getResponseBodyAsStream()));

                String line = "";
                while (null != (line = br.readLine())) {
                    result += line;
                }
            } else {
                throw new RuntimeException("http request faile! REQUEST_URL : "
                        + url + ", RESPONSE_CODE : " + state);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            // 释放链接
            post.releaseConnection();
        }
        return result;
    }

    /**
     *
     * @Title: sendPostByHttps
     * @Description: 向指定URL发送POST方法的请求，https协议
     * @param url
     *            请求的url
     * @param req
     *            请求参数：包含header、body的参数
     */
    public static String sendPostByHttps(String url, RequestParmDTO req) throws Exception {
        String result = "";
        HttpClient client = new HttpClient();

        PostMethod post = new PostMethod(url);
        try {
            // 处理请求参数
            if (null != req){
                // 请求头参数
                setHeaderParm(post, req.getHeader());

                // 请求体参数
                setBobyParm(post, req.getBody());
            }

            // 发送请求
            int state = client.executeMethod(post);
            if (state == HttpStatus.SC_OK){ // 请求成功
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(post.getResponseBodyAsStream()));

                String line = "";
                while (null != (line = br.readLine())) {
                    result += line;
                }
            } else {
                throw new RuntimeException("http request faile! REQUEST_URL : "
                        + url + ", RESPONSE_CODE : " + state);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            // 释放链接
            post.releaseConnection();
        }
        return result;
    }

    /**
     *
     * @Title: setHeaderParm
     * @Description: 设置请求头参数
     * @param method 请求方法，GetMethod或PostMethod
     * @param header 请求头参数
     */
    public static void setHeaderParm(HttpMethod method, Map<String, String> header) {
        if (null == header || header.size() == 0)
            return;

        Iterator<String> it = header.keySet().iterator();
        while (it.hasNext()) {
            String pname = it.next(); // 参数名
            method.setRequestHeader(pname, header.get(pname));
        }
    }

    /**
     *
     * @Title: setBobyParm
     * @Description: 设置请求体参数
     * @param method 请求方法，GetMethod或PostMethod
     * @param body 请求体参数
     */
    public static void setBobyParm(HttpMethod method, Map<String, String> body) {
        if (null == body || body.size() == 0)
            return;

        List<NameValuePair> list = new ArrayList<NameValuePair>();

        Iterator<String> it = body.keySet().iterator();
        while (it.hasNext()) {
            String pname = it.next(); // 参数名
            list.add(new NameValuePair(pname, body.get(pname)));
        }

        NameValuePair[] nvps = new NameValuePair[list.size()];

        if (method instanceof GetMethod) {
            ((GetMethod)method).setQueryString(list.toArray(nvps));
        }
        if (method instanceof PostMethod) {
            ((PostMethod)method).addParameters(list.toArray(nvps));
        }
    }
}
