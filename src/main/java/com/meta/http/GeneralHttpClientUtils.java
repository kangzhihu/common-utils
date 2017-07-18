package com.meta.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by zhihu.kang<br/>
 * Time: 2017/6/13 0:18<br/>
 * Email:kangzhihu@163.com<br/>
 * Description: <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;Http工具类,依赖于org.apache.httpcomponents.httpclient，为commons-httpclient.commons-httpclient的升级版本，commons-httpclient已废弃<br/>
 *  <br/>
 */
public class GeneralHttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(GeneralHttpClientUtils.class);

    private GeneralHttpClientUtils(){}
    
    private static final CloseableHttpClient httpClient;

   static{
        httpClient = GeneralHttpClientConfiguration.getHttpClientConfiguration().getHttpClientBuilder()
            .evictExpiredConnections()//定期回收过期连接
            .setConnectionTimeToLive(60,TimeUnit.SECONDS)
            .build();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                try {
                    if(httpClient != null){
                        httpClient.close();
                        logger.warn("JVM shutdown,closing httpClient..");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 获取HttpClient
     * @return
     */
    private static CloseableHttpClient getHttpClient(){
        return httpClient;
    }

    /**
     *
     * @param url
     * @return
     */
    public static String httpGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
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

    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));

        return getResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
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
            }else{
                //response.close();   //不要这样关闭，将直接关闭Socket，导致长连接不能复用
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            if(response != null){
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        return HttpClientConfiguration.NULL_STRING;
    }
}
