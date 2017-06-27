package com.meta.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.web.client.RestTemplate;

public abstract class GeneralPoolRestClient {
    /**
	 * spring restTemplate
	 */
	private RestTemplate restTemplate;
	/**
	 * httpClient
	 */
	private CloseableHttpClient httpClient;
	/**
	 * the timeout in live
	 */
	private int timeToLive = 30;
	/**
	 * the max connections
	 */
	private int maxTotal = 100;
	/**
	 * the max connections of every route
	 */
	private int defaultMaxPerRoute = 50;
	/**
	 * the timeout for waiting for data
	 */
	private int socketTimeout = 3000;
	/**
	 * the timeout in milliseconds until a connection is established
	 */
	private int connectTimeout = 3000;
	/**
	 * the timeout in milliseconds used when requesting a connection
     * from the connection manager
	 */
	private int requestTimeout = 3000;

	protected GeneralPoolRestClient() {
		initParams();
		initHttpClient();
		initRestTemplate();
	}

	/**
	 * init params
	 * void
	 */
	protected void initParams(){}
	/**
	 * build httpclient
	 * void
	 */
	protected abstract void initHttpClient();
	/**
	 * build resttemplate
	 * void
	 */
	protected abstract void initRestTemplate();

	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}

	protected void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	protected CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	protected void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	protected int getTimeToLive() {
		return timeToLive;
	}

	protected void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}

	protected int getMaxTotal() {
		return maxTotal;
	}

	protected void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	protected int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	protected void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	protected int getSocketTimeout() {
		return socketTimeout;
	}

	protected void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	protected int getConnectTimeout() {
		return connectTimeout;
	}

	protected void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	protected int getRequestTimeout() {
		return requestTimeout;
	}

	protected void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}
}
