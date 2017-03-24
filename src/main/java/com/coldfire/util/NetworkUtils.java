package com.coldfire.util;

import com.coldfire.inter.CommonLogger;
import org.apache.commons.lang.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/13 22:18
 * Email:kangzhihu@163.com
 * Descriptions:网络IP工具类
 */
public class NetworkUtils {
    private static final CommonLogger dbLogger = LoggerUtils.getLogger(NetworkUtils.class);

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
