package com.meta.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;


/**
 * Created by zhihu.kang<br/>
 * Time: 2017/6/13 0:18<br/>
 * Email:kangzhihu@163.com<br/>
 * Description: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;Http工具类,依赖于org.apache.httpcomponents.httpclient，为commons-httpclient.commons-httpclient的升级版本，commons-httpclient已废弃<br/>
 * <br/>
 */
public class GeneralHttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(GeneralHttpClientUtils.class);

    private GeneralHttpClientUtils() {
    }

    private static final CloseableHttpClient httpClient;

    static {
        httpClient = GeneralHttpClientConfiguration.getHttpClientConfiguration().getHttpClientBuilder().build();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (httpClient != null) {
                        httpClient.close();
                        logger.warn("JVM shutdown,closing httpClient..");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    public static String httpGet(String url, Map<String, Object> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet);
    }

    public static String httpGet(String url, Map<String, Object> headers, Map<String, Object> params)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }

    public static String httpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    public static String httpPost(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
        return getResult(httpPost);
    }

    public static String httpPost(String url, String jsonParm) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(jsonParm));
        return getResult(httpPost);
    }

    public static String httpPost(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
        return getResult(httpPost);
    }

    public static String httpPatch(String url) {
        HttpPatch httpPatch = new HttpPatch(url);
        return getResult(httpPatch);
    }

    public static String httpPatch(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPatch httpPatch = new HttpPatch(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPatch.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
        return getResult(httpPatch);
    }

    public static String httpPatch(String url, String jsonParm) throws UnsupportedEncodingException {
        HttpPatch httpPatch = new HttpPatch(url);
        httpPatch.setEntity(new StringEntity(jsonParm));
        return getResult(httpPatch);
    }

    public static String httpPatch(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPatch httpPatch = new HttpPatch(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPatch.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPatch.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
        return getResult(httpPatch);
    }

    public static String httpDelete(String url) {
        HttpDelete httpdelete = new HttpDelete(url);
        return getResult(httpdelete);
    }

    public static String httpDelete(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpdelete.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
        return getResult(httpdelete);
    }

    public static String httpDelete(String url, String jsonParm) throws UnsupportedEncodingException {
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
        httpDelete.setEntity(new StringEntity(jsonParm));
        return getResult(httpDelete);
    }

    public static String httpDelete(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpDeleteWithBody httpdelete = new HttpDeleteWithBody(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpdelete.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpdelete.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
        return getResult(httpdelete);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (params == null || params.isEmpty()) return pairs;
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request) {
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                EntityUtils.consume(entity);
            } else {
                //response.close();   //不要这样关闭，将直接关闭Socket，导致长连接不能复用
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e1) {
                    logger.error("restful 关闭异常："+e1.getMessage());
                }
            }
            logger.error("restful 请求异常："+e.getMessage());
        }
        return HttpClientConfiguration.NULL_STRING;
    }

    static class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {

        public static final String METHOD_NAME = "DELETE";

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }

        protected HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        protected HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        protected HttpDeleteWithBody() {
            super();
        }
    }
}
