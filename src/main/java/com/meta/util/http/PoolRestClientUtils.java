package com.meta.util.http;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.util.Map;

/**
 * Description: restTemplate 请求连接池工具类，依赖于spring-webmvc包
 * Created by zhihu.kang
 * Time: 2017/6/13 22:17
 * Email:kangzhihu@163.com
 */

public class PoolRestClientUtils {
    private static transient Logger logger = LoggerFactory.getLogger(PoolRestClientUtils.class.getName());
    /**
     * post请求
     * @param url
     * @param formParams
     * @return
     */
    public static String doPost(String url, Map<String, String> formParams) {
        if (MapUtils.isEmpty(formParams)) {
            return doPost(url);
        }
        try {
            MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
            formParams.keySet().stream().forEach(key -> requestEntity.add(key, MapUtils.getString(formParams, key, "")));
            return PoolRestClient.getClient().postForObject(url, requestEntity, String.class);
        } catch (Exception e) {
            logger.error("POST请求出错：{}", url, e);
            return "";
        }
    }

    /**
     * post请求
     * @param url
     * @return
     */
    public static String doPost(String url) {
        try {
            return PoolRestClient.getClient().postForObject(url, HttpEntity.EMPTY, String.class);
        } catch (Exception e) {
            logger.error("POST请求出错：{}", url, e);
        }
        return "";
    }

    public static <T> T postResponse(String url, Map<String, Object> formParams) {
        HttpEntity<Map<String, ?>> restRequest = new HttpEntity<Map<String, ?>>(formParams);
        ParameterizedTypeReference<T> typeRef = new ParameterizedTypeReference<T>() {};
        try {
            ResponseEntity<T> response = PoolRestClient.getClient().exchange(url,
                    HttpMethod.POST, restRequest, typeRef);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String post4Json(String url, String jsonParm) {
        try {
            MediaType type = MediaType
                    .parseMediaType("application/json; charset=UTF-8");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> entity = new HttpEntity<String>(jsonParm, headers);

            logger.error("post url:" + url);
            return PoolRestClient.getClient().postForObject(url, entity, String.class);
        } catch (Exception e) {
            logger.error("post<json_parm> error url:" + url + ", parm:" + jsonParm);
        }
        return "";
    }

    /**
     * get请求
     * @param url
     * @return
     */
    public static String doGet(String url) {
        try {
            return PoolRestClient.getClient().getForObject(url, String.class);
        } catch (Exception e) {
            logger.error("GET请求出错：{}", url, e);
        }
        return "";
    }

    /**
     * 通用post
     * @param url
     * @param formParams
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T generalPost(String url, Map<String, Object> formParams, Class<T> clazz) {
        try {
            ResponseEntity<T> response = PoolRestClient.getClient().postForEntity(UriComponentsBuilder.fromHttpUrl(url).build().toUri(), formParams, clazz);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通用get
     * @param url
     * @param queryParams
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T generalGet(String url, Map<String, Object> queryParams, Class<T> clazz) {
        try {
            ResponseEntity<T> response = PoolRestClient.getClient().getForEntity(buildUrl(url, queryParams), clazz);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    private static String buildUrl(String url, Map<String, Object> map){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if(map != null && map.size() > 0){
            for(Map.Entry<String, Object> entity : map.entrySet()){
                builder.queryParam(entity.getKey(), entity.getValue());
            }
        }
        return builder.build().encode().toUriString();
    }

    public static byte[] download(String url){
        HttpEntity entity = new HttpEntity(new HttpHeaders());
        ResponseEntity<byte[]> responseEntity = PoolRestClient.getClient().exchange(url, HttpMethod.GET, entity, byte[].class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return responseEntity.getBody();
        }
        return null;
    }

    public static <T> T upload(String url, File file, Class<T> clazz){
        try {
            FileSystemResource resource = new FileSystemResource(file);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("file", resource);
            param.add("fileName", file.getName());
            HttpEntity<MultiValueMap<String, ?>> httpEntity = new HttpEntity<>(param);
            ParameterizedTypeReference<T> typeDef = new ParameterizedTypeReference<T>() {};
            ResponseEntity<T> response = PoolRestClient.getClient().exchange(url, HttpMethod.POST, httpEntity, typeDef);
            if(response.getStatusCode().is2xxSuccessful()){
                return (T) response.getBody();
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
