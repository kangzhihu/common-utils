package com.meta.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description: <br/>
 * &nbsp;&nbsp;池化RestClient，不直接提供功能，只提供给PoolRestClientUtils<br/>
 * Created by zhihu.kang<br/>
 * Time: 2017/6/13 22:36<br/>
 * Email:kangzhihu@163.com<br/>
 */

public class PoolRestClient extends GeneralPoolRestClient{
    private static volatile PoolRestClient instance;
	
	private static PoolRestClient getInstance(){
		// 先检查实例是否存在，如果不存在才进入下面的同步块  
        if (instance == null) {  
            // 同步块，线程安全地创建实例  
            synchronized (PoolRestClient.class) {  
                // 再次检查实例是否存在，如果不存在才真正地创建实例
				if(instance == null){
					instance = new PoolRestClient();
				}
            }
        }  
        return instance;  
	}
	
	public PoolRestClient(){
		super();
	}
	@Override
	protected void initHttpClient(){
		// 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(getTimeToLive(), TimeUnit.SECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(getMaxTotal());
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(getDefaultMaxPerRoute());
        
        RequestConfig requestConfig = RequestConfig.custom()
        		.setSocketTimeout(getSocketTimeout())
        		.setConnectTimeout(getConnectTimeout())
        		.setConnectionRequestTimeout(getRequestTimeout())
        		.build();
        
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        //连接的最大空闲时间，超过该时间将回收
        httpClientBuilder.evictIdleConnections(300, TimeUnit.SECONDS);
        // 重试次数，默认是3次，没有开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
        // 保持长连接配置，需要在头添加Keep-Alive
       /* httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        httpClientBuilder.setDefaultHeaders(headers);*/
        CloseableHttpClient httpClient = httpClientBuilder.build();
        setHttpClient(httpClient);
	}
	@Override
	protected void initRestTemplate(){
		// httpClient连接配置，底层是配置RequestConfig
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(this.get());
        // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
        // clientHttpRequestFactory.setBufferRequestBody(false);
        
        // 添加内容转换器
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        RestTemplate restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        setRestTemplate(restTemplate);
	}

	public static RestTemplate getClient(){
		return PoolRestClient.getInstance().getRestTemplate();
	}

	protected CloseableHttpClient get() {
		if (null == getHttpClient()) {
			throw new IllegalStateException("The HttpClient manager not ready,please init it first!");
		}
		return getHttpClient();
	}

	//Spring会默认清理HttpClient以及connections，所以在spring环境下不用显示调用
	protected void shutdown() throws IOException {
		getHttpClient().close();
	}
}
