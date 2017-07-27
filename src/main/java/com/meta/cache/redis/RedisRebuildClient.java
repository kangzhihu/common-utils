package com.meta.cache.redis;

import com.meta.executor.callable.CallableExecutorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017-07-26&nbsp;22:15<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;redis防穿透缓存重建工具类<br/><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;该工具类默认容忍最多60s的重建时间
 */
public class RedisRebuildClient {

    Logger logger = LoggerFactory.getLogger(RedisRebuildClient.class);
    private static int MAX_REBUILD_TIME = 60;
    private static String UPDATE_LOCK_TAG = "UPLOCK_";

    private Jedis jedis;
    private String key;

    public RedisRebuildClient(Jedis jedis, int db, String key) {
        this.jedis = jedis;
        this.jedis.select(db);
        this.key = key;
    }

    public String get(Callable<String> task, int... seconds) {
        //jedis.select(1);  //读写分离？此处：0-读  1-写
        String val = jedis.get(key);
        // 缓存过期  && 获取锁成功，设置成功则表示当前缓存已失效
        // setnx:原子操作，如果不存在则设置值，并返回1。如果缓存存在，则返回0，设置缓存失败
        //不使用exists是处理并发获取锁失败的情况
        if (seconds.length > 0 && jedis.setnx(UPDATE_LOCK_TAG+ key, System.currentTimeMillis() + "") == 1) {
            /**
             *  将锁的有效时间设为60s，在60s内如果查询数据库成功，则更新该锁的失效时间=缓存时间。
             *  如果60s内出现异常，则60s后第一个请求又会去访问数据库...也防止锁被某个线程始终占有
             *  返回null表示没有查询到数据，外层代码需要到数据库获取数据并更新缓存
             */
            String result = null;
            int second = seconds[0];
            try {
                jedis.expire(UPDATE_LOCK_TAG+ key, MAX_REBUILD_TIME);
                Future<String> future = CallableExecutorUtils.getSingleton(true).addTask(task);
                result = future.get(MAX_REBUILD_TIME, TimeUnit.SECONDS);
            } catch (Exception e) {
                if (jedis != null) {
                    RedisUtils.close(this.jedis);
                }
                this.logger.error(e.getMessage(), e);
            }
            set(result, second);
        } else {
            //  jedis.select(0);
            // (缓存未过期) || (缓存过期，但是获取锁失败) then 返回旧的数据
            if (jedis != null) {
                RedisUtils.close(this.jedis);
            }
            return val;
        }
        return val;
    }

    public boolean set(String value, int seconds) {
        try {
            if (seconds > 0) {
                // 添加缓存，设置值与其缓存时间，不管存不存在，注意与setnx 区别，setnx 不存在时才设置成功
                // 缓存有效时间=真实时间+1天，缓存失效时仍然可用
                jedis.setex(key, seconds + 60 * 60 * 24, value);
                //  jedis.select(1);
            } else {  //不失效
                jedis.set(key, value);
                //  jedis.select(1);
            }
            jedis.del(UPDATE_LOCK_TAG + key);
            return true;
        } catch (JedisException e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                RedisUtils.close(this.jedis);
            }
        }
        return false;
    }

}
