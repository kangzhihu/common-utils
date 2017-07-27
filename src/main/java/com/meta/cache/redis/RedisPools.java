package com.meta.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.CRC32;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/7/25 23:07<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:
 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;RedisPools 自定义的redis<strong> 分片 </strong>连接池获取工具类，配置入口。
 * 基于一致性哈稀分片的redis 连接池使用参看{@link RedisShardPools}。<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;实际工作中可以直接使用spring-data-redis整合单个redis，但是灵活度不够，故此处自己封装支持分布式redis。<br/>
 * spring 配置redis连接池示例：
 * <pre>{@code
 * <bean class="com.meta.cache.redis.RedisPools">
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
public class RedisPools extends GeneralRedisPool{
    private static final transient Logger logger = LoggerFactory.getLogger(RedisPools.class);
    private String[] hosts;
    private int point;
    private static volatile RedisPools redisPools = null;
    private ConcurrentHashMap<String, JedisPool> poolMap;

    public static RedisPools getInstance() {
        if(redisPools == null) {
            synchronized(RedisPools.class) {
                if(redisPools == null) {
                    initPoolConfig();
                    redisPools = new RedisPools();
                    redisPools.initParm(redisHosts);
                }
            }
        }
        return redisPools;
    }

    private RedisPools(){
    }

    protected void initParm(String hosts) {
        this.poolMap = new ConcurrentHashMap();
        this.hosts = hosts.split(",");
        this.point = 1024 / this.hosts.length;
        Arrays.stream(this.hosts).forEach(this::buildPool);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            redisPools = null;
            poolMap.values().forEach((pool)->pool.close());
        }));
    }

    /**
     * 获取本机指定 hash key 的redis连接池
     * @param key
     * @return
     */
    public JedisPool getHashPool(String key) {
        int vl = CrcSlot.hitServer(key);
        String host = this.hosts[(int)Math.floor((double)(vl / this.point))];
        return this.getHostPool(host);
    }

    /**
     * 获取本机中指定redis server 的redis连接池
     * @param host redis server地址与服务端口，如192.168.12.166:9090
     * @return
     */
    public JedisPool getHostPool(String host) {
        JedisPool pool = (JedisPool)this.poolMap.get(host);
        return pool != null?pool:this.buildPool(host);
    }

    /**
     * 随机获取本机所存在的redis连接池
     * @return
     */
    public JedisPool getAnyPool() {
        int vl = this.hitAnyServer();
        String host = this.hosts[(int)Math.floor((double)(vl / this.point))];
        return this.getHostPool(host);
    }

    private int hitAnyServer() {
        return (new Random()).nextInt(1023);
    }

    /**
     * 根据host配置返回指定redis连接池
     * @param host redis server地址与服务端口，如192.168.12.166:9090
     * @return
     */
    public JedisPool buildPool(String host) {
        logger.info("build redis pool,host: "+host);
        String[] hostToken = host.split(":");
        JedisPool pool = new JedisPool(getPoolConfig(), hostToken[0], Integer.parseInt(hostToken[1]));
        this.poolMap.put(host, pool);
        return pool;
    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public Map<String, JedisPool> getJedisPools() {
        return this.poolMap;
    }

    /**
     * CRC32会把字符串，生成一个long长整形的唯一性ID
     */
    private static class CrcSlot{
        public CrcSlot() {}

        public static int hitServer(String key) {
            return (new BigInteger(crc32(key) + "")).mod(new BigInteger("1024")).intValue();
        }

        private static Long crc32(String key) {
            CRC32 crc32 = new CRC32();
            crc32.update(key.getBytes());
            return Long.valueOf(Math.abs(crc32.getValue()));
        }

    }

    static int index = 0;
     public static void main(String[] args) throws Exception {
         logger.error("dsdasd");
         for (int i = 0; i < 50; i++) {
             new Thread(new Runnable() {
                 @Override
                 public void run() {

                     String key = String.valueOf(Thread.currentThread().getId()) + "_" + (index++);
                     JedisPool jds = null;
                     RedisPools tpool = null;
                     Jedis jsdis = null;
                     try {
                         tpool = RedisPools.getInstance();
                         jds = tpool.getHashPool(key);
                         jsdis = jds.getResource();
                         System.out.println(key + ":" + jsdis.getClient().getPort());
                         System.out.println("set -- "+jsdis.set(key, "11111111"));
                         logger.error("dsdasd");
                     } catch (Exception e) {
                         e.printStackTrace();
                     } finally {
                         if (jsdis != null) {
                             jsdis.close();
                         }
                     }
                 }
             }).start();
         }
     }
}
