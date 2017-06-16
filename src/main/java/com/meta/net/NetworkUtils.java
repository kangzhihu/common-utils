package com.meta.net;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhihu.kang<br/>
 * Time: 2016/12/13 22:18<br/>
 * Email:kangzhihu@163.com<br/>
 * Descriptions:<br/>
 &nbsp;nbsp;网络IP工具类<br/>
 */
public class NetworkUtils {
    private final static transient Logger logger = LoggerFactory.getLogger(NetworkUtils.class);

    private NetworkUtils(){}

    public static String getHostName(String ip){
        if(StringUtils.isBlank(ip)) return  null;
        return getServerAddrMap().get(ip);
    }

    public static Map<String, String> getServerAddrMap() {
        Map<String, String> ipMacInfo = new TreeMap<String, String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if(inetAddress instanceof Inet4Address) {
                        ipMacInfo.put(inetAddress.getHostAddress(), inetAddress.getHostName());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipMacInfo;
    }


    public static String inetAddressTypeName(InetAddress inetAddress) {
        return (inetAddress instanceof Inet4Address) ? "ipv4" :"ipv6";
    }
}
