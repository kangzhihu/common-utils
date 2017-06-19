package com.meta.file;

import com.meta.http.PoolRestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.Map;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/6/17 23:38<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;上传文件到远程地址<br/><br/>
 */

public class UploadFileToCdn {
    /**
	 * 使用{@link PoolRestClient}上传文件
	 * @param url 远程地址
	 * @param file 文件
	 * @return
	 * Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
    public static Map<String, Object> postFile(String url, File file) {
		FileSystemResource resource = new FileSystemResource(file);
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
		param.add("file", resource);
		param.add("fileName", file.getName());
		
		HttpEntity<MultiValueMap<String, ?>> httpEntity = new HttpEntity<MultiValueMap<String, ?>>(param);
		ParameterizedTypeReference<Map<String, Object>> typeDef = new ParameterizedTypeReference<Map<String,Object>>(){};
		try {
			ResponseEntity<Map<String, Object>> response = PoolRestClient.getClient().exchange(url, HttpMethod.POST, httpEntity, typeDef);
			if(response.getStatusCode().is2xxSuccessful()){
			    return (Map<String, Object>) response.getBody().get("result");
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
