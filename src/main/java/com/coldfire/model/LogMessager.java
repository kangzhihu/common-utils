package com.coldfire.model;

import com.coldfire.util.JsonUtils;
import com.coldfire.util.LoggerUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
public class LogMessager implements Serializable{
	private static final long serialVersionUID = 542743269400201193L;
	private Map data = new LinkedHashMap();
	public LogMessager(String type, String server, String systemId, Map paramsdata) {
		init(type, server, systemId);
		data.put("message", paramsdata);
	}
	public LogMessager(String type, String server, String systemId, String message) {
		init(type, server, systemId);
		data.put("message", message);
	}
	public LogMessager(String type, String server, String systemId, String message, Throwable t) {
		init(type, server, systemId);
		data.put("message", message);
		data.put("exception", t.getClass().getCanonicalName());
		data.put("exceptionTrace", LoggerUtils.getExceptionTrace(t, 200));
	}
	private void init(String type, String server, String systemId){
		if(type!=null){
			data.put("type", type);
		}
		data.put("server", server);
		data.put("systemid", systemId);
	}

	public String getDataStr() {
		return JsonUtils.writeObjectToJson(this.data);
	}
	public Map getDataMap() {
		return this.data;
	}
}
