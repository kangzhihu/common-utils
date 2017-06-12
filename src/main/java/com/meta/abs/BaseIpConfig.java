package com.meta.abs;

import com.meta.util.NetworkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zhihu.kang
 * Time: 2016/12/12 22:52
 * Email:kangzhihu@163.com
 * Descriptions：网络配置类，可用于继承实现自定义内部网络信息
 */
public abstract class BaseIpConfig {
    private static final List<String> searchList;
    private static final String serverIp;
    static {
        searchList = new ArrayList<String>();
        searchList.addAll(Arrays.asList(new String[]{"192.168.31."}));//对搜索列表进行扩展，可控制IP获取范围，参与特定环境范围下逻辑判断
        String[] host = BaseIpConfig.getServerAddr();
        serverIp = host[0];
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static String getHostName() {
        return NetworkUtils.getHostName(serverIp);
    }

    public static String[] getServerAddr(){
        Map<String, String> hostMap = NetworkUtils.getServerAddrMap();
        for(String search: searchList){
            for(String addr: hostMap.keySet()){
                if(addr.startsWith(search)){
                    return new String[]{addr, hostMap.get(addr)};
                }
            }
        }
        return new String[]{"127.0.0.1", "localhost"};
    }

    public abstract boolean isInnerIp();
}
