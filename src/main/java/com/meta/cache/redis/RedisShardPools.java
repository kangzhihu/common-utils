package com.meta.cache.redis;

import com.meta.common.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/7/25 23:07<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:
 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;RedisShardPools 基于一致性哈稀分片的redis<strong> 分片 </strong>连接池获取工具类，配置入口。
 * 自定义分片类redis 连接池使用参看{@link RedisPools}。
 * 扩容请参见<a href="http://blog.csdn.net/lang_man_xing/article/details/38405269 ">Pre-Sharding</a>。<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;实际工作中可以直接使用spring-data-redis整合单个redis，但是灵活度不够，故此处自己封装支持分布式redis。<br/>
 * spring 配置redis连接池示例：
 * <pre>{@code
 * <bean class="com.meta.cache.redis.RedisShardPools">
 *       <property name="redisHosts" value="10.104.59.21:6379,10.104.59.21:6380"/>
 *       <property name="prefix" value="${jedis.prefix}"/>
 *       <property name="maxTotal" value="${jedis.pool.maxTotal}"/>
 *       <property name="maxIdle" value="${jedis.pool.maxIdle}"/>
 *       <property name="minIdle" value="${jedis.pool.minIdle}"/>
 *       <property name="maxWait" value="${jedis.pool.maxWait}"/>
 * </bean>
 * }
 * </pre>
 */
public class RedisShardPools extends GeneralRedisPool {

    private static final Logger logger = LoggerFactory.getLogger(RedisShardPools.class);
    private static String[] hosts;
    private static volatile ShardedJedisPool redisPool = null;
    private static List<JedisShardInfo> jdsInfoList = null;

    private RedisShardPools() {
    }

    protected static void init(String hostStr) {
        hosts = hostStr.split(",");
        logger.warn("init hosts: " + hosts);
        jdsInfoList = new ArrayList<JedisShardInfo>(hosts.length);
        Arrays.stream(hosts).forEach((h) -> {
            jdsInfoList.add(buildShardInfo(h));
        });
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            if(redisPool != null){
                redisPool.close();
            }
        }));
    }

    private static JedisShardInfo buildShardInfo(String host) {
        String[] hostToken = host.split(":");
        logger.warn("build redis ShardInfo,host: " + host);
        return new JedisShardInfo(hostToken[0], Integer.parseInt(hostToken[1]));
    }


    public static ShardedJedisPool getInstance() {
        if (redisPool == null) {
            synchronized (ShardedJedisPool.class) {
                if (redisPool == null) {
                    initPoolConfig();
                    init(redisHosts);
                    redisPool = new ShardedJedisPool(poolConfig, jdsInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
                }
            }
        }
        return redisPool;
    }

    public static void returnResource(ShardedJedis jds) {
        if (jds != null) {
            jds.close();
        }
    }

}
