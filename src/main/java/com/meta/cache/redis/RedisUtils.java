package com.meta.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/7/25 23:07<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:
 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;RedisUtils Redis工具类。<br/>
 */
public class RedisUtils {

    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    public static final Integer DB_ZERO = Integer.valueOf(0);
    public static final Integer DB_ONE = Integer.valueOf(1);
    public static final Integer DB_TWO = Integer.valueOf(2);
    public static final Integer DB_THREE = Integer.valueOf(3);
    public static final Integer DB_FOUR = Integer.valueOf(4);
    public static final Integer DB_FIVE = Integer.valueOf(5);
    public static final Integer DB_SIX = Integer.valueOf(6);
    public static final Integer DB_SEVEN = Integer.valueOf(7);
    public static final Integer DB_EIGHT = Integer.valueOf(8);
    public static final Integer DB_NINE = Integer.valueOf(9);

    public RedisUtils() {
    }

    public static void main(String[] args) {
        String key = "user.queue";
        System.out.println(getRedisClientByHost(key, "10.66.114.49:28000").lrange(0L, -1L));
    }

    public static RedisClient getRedisClient(String key) {
        try {
            JedisPool pool = RedisPools.getInstance().getHashPool(key);
            return new RedisClient(pool, 0, RedisPools.prefix + key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static RedisClient getAnyoneJedis(String key) {
        try {
            JedisPool pool = RedisPools.getInstance().getAnyPool();
            return new RedisClient(pool, 0, key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<RedisClient> getSplitRedisClient(String key) {
        List<RedisClient> RedisClients = new ArrayList();
        Iterator iterator = RedisPools.getInstance().getJedisPools().values().iterator();

        while(iterator.hasNext()) {
            RedisClients.add(new RedisClient((JedisPool)iterator.next(), 0, RedisPools.prefix + key));
        }

        return RedisClients;
    }

    public static RedisClient getRedisClientByHost(String key, String host) {
        try {
            JedisPool pool = RedisPools.getInstance().getHostPool(host);
            return new RedisClient(pool, 0, RedisPools.prefix + key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static RedisClient getRedisClientByDb(String key, int db) {
        try {
            JedisPool pool = RedisPools.getInstance().getHashPool(key);
            return new RedisClient(pool, db, RedisPools.prefix + key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static void broken(Jedis jedis) {
        if(jedis != null){
            jedis.close();
        }
    }

    public static void close(Jedis jedis) {
        if(jedis != null){
            jedis.close();
        }
    }
}
