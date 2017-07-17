package com.meta.http;

import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-06-27&nbsp;22:38<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;一般配置的HttpClient<br/><br/>
 */
public class GeneralHttpClientConfiguration extends HttpClientConfiguration{

    private static Logger log = LoggerFactory.getLogger(GeneralHttpClientConfiguration.class);

    private static volatile HttpClientConfiguration httpClientConfiguration;

    //HttpClient设置连接超时时间
    private static Integer CONNECTION_TIMEOUT = 2 * 1000; //设置连接主机超时2秒钟 根据业务调整
    private static Integer SO_TIMEOUT = 2 * 1000; //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。 根据业务调整
    private static Integer CONN_MANAGER_TIMEOUT = 5 * 100; //设设置从connect Manager获取Connection 超时时间(排队) 根据业务调整

    private GeneralHttpClientConfiguration(){}

    @Override
    protected void initSocketFactoryRegistry() {
        socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
    }

    @Override
    protected void initPoolManager() {
        poolManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 整个连接池最大连接数
        poolManager.setMaxTotal(100);
        // 单路由最大连接数，默认值是2，控制连接到指定连接时并发量(而不是由池子大小控制，池子控制总并发量)
        // 如连接到http://qq.com和http://jd.com时，每个主机并发最多80个，总并发数量最多100个
        // 设置过小无法支撑大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)
        // 路由是对maxTotal的细分，http路由表示请求URL映射到handler处理器的过程
        poolManager.setDefaultMaxPerRoute(80);
        //从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s
        poolManager.setValidateAfterInactivity(5*1000);
    }

    @Override
    protected void initRequestConfig() {
        requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT)
                .setConnectionRequestTimeout(CONN_MANAGER_TIMEOUT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
    }

    @Override
    protected void initHttpClientBuilder() {
        httpClientBuilder = HttpClients.custom()
                .setRetryHandler(new DefaultHttpRequestRetryHandler())//默认失败后重发3次，可用别的构造方法指定重发次数
                .setDefaultRequestConfig(getRequestConfig())
                .setConnectionManager(getPoolManager());
    }

    public static HttpClientConfiguration getSingleton(){
        if(httpClientConfiguration == null){
            synchronized (GeneralHttpClientConfiguration.class){
                if(httpClientConfiguration == null){
                    httpClientConfiguration = new GeneralHttpClientConfiguration();
                }
            }
        }
        return httpClientConfiguration;
    }
}
