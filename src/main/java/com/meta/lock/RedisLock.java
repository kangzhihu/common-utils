package com.meta.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-11-02&nbsp;22:28<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;RedisLock分布式锁<br/><br/>
 * Warn:待测试
 */
public class RedisLock {


    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private static final int SURE_COUNT = 5;

    private static final String LOCK_PRE = "t_i_lock_";

    private static final String FILTER_PRE = "t_i_filter_";

    private static final String THRESHOLD_PRE = "t_i_threshold_";

    /**
     * 根据key值获取一个锁，并设定超时时间
     *
     * @param jedis
     * @param key
     * @param autoReleaseSec
     * @return
     */
    public static boolean acquireLock(Jedis jedis, String key, int autoReleaseSec) {
        if (autoReleaseSec <= 0) {
            return true; // 获取锁成功
        }
        String newKey = LOCK_PRE + key;
        try {
            Long count = jedis.incr(newKey);
            boolean state = count == 1;
            if (state || (count % SURE_COUNT == 0 && jedis.ttl(newKey) == -1)) {
                jedis.expire(newKey, autoReleaseSec);
            }
            if (!state) {
                logger.info("acquireLock key={} autoReleaseSec={} state={}.", newKey, autoReleaseSec, state);
            } else {
                logger.debug("acquireLock key={} autoReleaseSec={} state={}.", newKey, autoReleaseSec, state);
            }
            return state;
        } catch (Exception e) {
            logger.warn("acquireLock key={} fail.", newKey, e);
            return false;
        }
    }

    /**
     * 根据key值获取一个锁，并设定超时时间1分钟
     *
     * @param jedis
     * @param key
     * @return
     */
    public static boolean acquireLock(Jedis jedis, String key) {
        return acquireLock(jedis, key, 60);
    }

    /**
     * 根据key值释放一个锁
     *
     * @param jedis
     * @param key
     */
    public static void releaseLock(Jedis jedis, String key) {
        String newKey = LOCK_PRE + key;
        jedis.del(newKey);
    }

    /**
     * 使用超时对一个key值进行过滤
     *
     * @param jedis
     * @param key
     * @param sec 不需要过滤时超时秒数
     * @return
     */
    public static boolean filter(Jedis jedis, String key, int sec) {
        if (sec <= 0) {
            return false; // 不需要过滤
        }
        String newKey = FILTER_PRE + key;
        try {
            Long count = jedis.incr(newKey);
            boolean state = count == 1;
            if (state || (count % SURE_COUNT == 0 && jedis.ttl(newKey) == -1)) {
                jedis.expire(newKey, sec);
            } else {
                logger.info("filter key={}.", newKey);
            }
            return !state;
        } catch (Exception e) {
            logger.warn("filter key={} fail.", newKey, e);
            return false;
        }
    }

    /**
     * 是否满足单位时间多少流量的阈值限制 maxLimit / perSec 比如：50/2 表示每两秒的流量限制是 50
     *
     * @param jedis
     * @param key
     * @param maxLimit 阈值的上限值
     * @param perSec 单位时间
     * @return
     */
    public static boolean threshold(Jedis jedis, String key, int maxLimit, int perSec) {
        String newKey = THRESHOLD_PRE + "_" + key;
        try {
            Long val = jedis.incr(newKey);
            if (val % maxLimit == 1 && jedis.ttl(newKey) == -1) {
                jedis.expire(newKey, perSec);
            }
            return val < maxLimit;
        } catch (Exception e) {
            logger.warn("threshold key={} fail.", newKey, e);
            return true;
        }

    }

}
