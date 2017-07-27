package com.meta.cache.redis;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/7/25 23:07<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:
 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;GeneralRedisPool redis连接池获取父类。<br/>
 */
public abstract class GeneralRedisPool {

    protected static String redisHosts;
    protected static String prefix;//环境
    protected static int maxTotal;
    protected static int maxIdle;
    protected static int minIdle;
    protected static int maxWait;

    protected static JedisPoolConfig poolConfig;

    protected static void initPoolConfig(){
        poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis((long)maxWait);
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        //poolConfig.setMinEvictableIdleTimeMillis(1800000);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出（5分钟）
        //不再根据MinEvictableIdleTimeMillis判断
        poolConfig.setSoftMinEvictableIdleTimeMillis(300000L);
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        poolConfig.setTimeBetweenEvictionRunsMillis(60000L);
        //在获取连接的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(false);
        //在空闲时检查有效性, 默认false
        poolConfig.setTestWhileIdle(false);
    }

    public String getRedisHosts() {
        return redisHosts;
    }

    public void setRedisHosts(String redisHosts) {
        redisHosts = redisHosts;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        prefix = prefix;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        minIdle = minIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        maxWait = maxWait;
    }

    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }
}
