package com.meta.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/7/25 23:07<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:
 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;RedisUtils Redis工具类。<br/>
 */
public class RedisShardUtils {

    private static Logger logger = LoggerFactory.getLogger(RedisShardUtils.class);
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

    public RedisShardUtils() {
    }

    public static ShardedJedis getRedisClient() {
        try {
            return RedisShardPools.getInstance().getResource();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static void close(ShardedJedis jedis) {
        if(jedis != null){
            jedis.close();
        }
    }
}
