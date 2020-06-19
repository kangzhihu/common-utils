package com.meta.lock.redissionLock;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SentinelServersConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 */
@Configuration
public class RedissonManager {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RedissonManager.class);

    @Value("${public.redis.address}")
    private String cluster;
    @Value("${public.redis.password}")
    private String password;

    private static final String PROFIX = "redis://";

    private static final String MASTER_NAME = "mymaster";

    //集群哨兵模式
    @org.springframework.context.annotation.Bean
    public RedissonClient getRedissonOfSentinel() {
        String[] nodes = cluster.split(",");
        for (int i = 0; i < nodes.length; i++) {
            if(StringUtils.isNotBlank(nodes[i]) && !StringUtils.startsWith(nodes[i],PROFIX)){
                //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
                nodes[i] = PROFIX + nodes[i];
            }
        }
        Config config = new Config();
        SentinelServersConfig clusterServersConfig = config.useSentinelServers();
        clusterServersConfig.setScanInterval(2000) //设置集群状态扫描时间
                .addSentinelAddress(nodes)
                .setMasterName(MASTER_NAME) //必填
                .setReadMode(ReadMode.SLAVE)
                .setConnectTimeout(6000)
                .setPassword(password);
        return Redisson.create(config);

    }
}