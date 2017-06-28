package com.meta.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-06-27&nbsp;22:38<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;abstract HttpClientConfiguration<br/><br/>
 */
public abstract class HttpClientConfiguration {
    public static String DEFAULT_ENCODE = "UTF-8";
    public static String NULL_STRING = new String();
    //设置访问协议
    protected Registry<ConnectionSocketFactory> socketFactoryRegistry;
    protected PoolingHttpClientConnectionManager poolManager;
    protected RequestConfig requestConfig;
    protected HttpClientBuilder httpClientBuilder;

    protected abstract  void initSocketFactoryRegistry() ;

    protected abstract  void initPoolManager();

    protected abstract void initRequestConfig() ;

    protected abstract void initHttpClientBuilder() ;

    protected HttpClientConfiguration(){
        initPrams();
    }

    protected void initPrams(){
        initSocketFactoryRegistry() ;
        initPoolManager();
        initRequestConfig() ;
        initHttpClientBuilder() ;
    }

    public Registry<ConnectionSocketFactory> getSocketFactoryRegistry() {
        return socketFactoryRegistry;
    }

    public PoolingHttpClientConnectionManager getPoolManager() {
        return poolManager;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public HttpClientBuilder getHttpClientBuilder() {
        return httpClientBuilder;
    }

}
