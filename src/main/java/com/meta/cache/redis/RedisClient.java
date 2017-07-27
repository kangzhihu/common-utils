package com.meta.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/7/25 23:07<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:
 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;PubJedis Redis操作客户端，与RedisPools配合使用。<br/>
 */
public class RedisClient {
    Logger logger = LoggerFactory.getLogger(RedisClient.class);
    private Jedis jedis;
    private String key;

    public String get() {
        try {
            String result = this.jedis.get(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }
        return "";
    }

    public byte[] getbyte() {
        try {
            byte[] result = this.jedis.get(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public String set(String value) {
        String result;
        try {
            result = this.jedis.set(this.key, value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public String set(byte[] value) {
        String result;
        try {
            result = this.jedis.set(this.key.getBytes(), value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public String setex(String value, int expireTime) {
        String result;
        try {
            result = this.jedis.setex(this.key, expireTime, value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public String setex(byte[] value, int expireTime) {
        String result;
        try {
            result = this.jedis.setex(this.key.getBytes(), expireTime, value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public long setnx(String value) {
        long result;
        try {
            result = this.jedis.setnx(this.key, value).longValue();
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long setnx(byte[] value) {
        Long result;
        try {
            result = this.jedis.setnx(this.key.getBytes(), value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public String hget(String field) {
        try {
            String result = this.jedis.hget(this.key, field);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return "";
    }

    public byte[] hgetbyte(String field) {
        try {
            byte[] result = this.jedis.hget(this.key.getBytes(), field.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public List<String> hmget(String... fields) {
        try {
            List result = this.jedis.hmget(this.key, fields);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return new ArrayList();
    }

    public List<byte[]> hmget(byte[]... fields) {
        try {
            List result = this.jedis.hmget(this.key.getBytes(), fields);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public Long hset(String field, String value) {
        Long result;
        try {
            result = this.jedis.hset(this.key, field, value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long hset(String field, byte[] value) {
        Long result;
        try {
            result = this.jedis.hset(this.key.getBytes(), field.getBytes(), value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long hsetbyte(String field, byte[] value) {
        Long result;
        try {
            result = this.jedis.hset(this.key.getBytes(), field.getBytes(), value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public String hmset(Map<String, String> hash) {
        try {
            String result = this.jedis.hmset(this.key, hash);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public String hmsetbyte(Map<byte[], byte[]> hash) {
        try {
            String result = this.jedis.hmset(this.key.getBytes(), hash);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public Long expire(int expireTime) {
        Long result;
        try {
            result = this.jedis.expire(this.key, expireTime);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long expirebyte(int expireTime) {
        Long result;
        try {
            result = this.jedis.expire(this.key.getBytes(), expireTime);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public String rpop() {
        try {
            String result = this.jedis.rpop(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return "";
    }

    public byte[] rpopbyte() {
        try {
            byte[] result = this.jedis.rpop(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public Long rpush(String value) {
        Long result;
        try {
            result = this.jedis.rpush(this.key, new String[]{value});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long rpushbyte(String value) {
        Long result;
        try {
            result = this.jedis.rpush(this.key.getBytes(), new byte[][]{value.getBytes()});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public String lpop() {
        try {
            String result = this.jedis.lpop(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return "";
    }

    public byte[] lpopbyte() {
        try {
            byte[] result = this.jedis.lpop(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public String lindex(long index) {
        try {
            String result = this.jedis.lindex(this.key, index);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return "";
    }

    public byte[] lindexbyte(long index) {
        try {
            byte[] result = this.jedis.lindex(this.key.getBytes(), index);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public List<String> blpop(int timeout) {
        try {
            List result = this.jedis.blpop(timeout, this.key);
            return result;
        } catch (JedisConnectionException e) {
            ;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return new ArrayList();
    }

    public List<byte[]> blpopbyte(int timeout) {
        try {
            List result = this.jedis.blpop(timeout, new byte[][]{this.key.getBytes()});
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public Long lrem(long count, String value) {
        try {
            Long result = this.jedis.lrem(this.key, count, value);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Long.valueOf(0L);
    }

    public Long lrembyte(long count, String value) {
        try {
            Long result = this.jedis.lrem(this.key.getBytes(), count, value.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Long.valueOf(0L);
    }

    public Long lpush(String value) {
        Long result;
        try {
            result = this.jedis.lpush(this.key, new String[]{value});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long lpushbyte(String value) {
        Long result;
        try {
            result = this.jedis.lpush(this.key.getBytes(), new byte[][]{value.getBytes()});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public List<String> lrange(long start, long end) {
        try {
            List e = this.jedis.lrange(this.key, start, end);
            return e;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return new ArrayList();
    }

    public List<byte[]> lrangebyte(long start, long end) {
        try {
            List e = this.jedis.lrange(this.key.getBytes(), start, end);
            return e;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public Set<String> hkeys() {
        try {
            Set result = this.jedis.hkeys(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return new HashSet();
    }

    public Set<byte[]> hkeysbyte() {
        try {
            Set result = this.jedis.hkeys(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public List<String> hvals() {
        try {
            List result = this.jedis.hvals(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return new ArrayList();
    }

    public List<byte[]> hvalsbyte() {
        try {
            List result = this.jedis.hvals(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public Map<String, String> hgetAll() {
        try {
            Map result = this.jedis.hgetAll(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public Map<byte[], byte[]> hgetAllbyte() {
        try {
            Map result = this.jedis.hgetAll(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return null;
    }

    public Long llen() {
        try {
            Long result = this.jedis.llen(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Long.valueOf(0L);
    }

    public Long llenbyte() {
        try {
            Long result = this.jedis.llen(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Long.valueOf(0L);
    }

    public Long hlen() {
        try {
            Long result = this.jedis.hlen(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Long.valueOf(0L);
    }

    public Long hlenbyte() {
        try {
            Long result = this.jedis.hlen(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Long.valueOf(0L);
    }

    public Long ttl() {
        try {
            Long result = this.jedis.ttl(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Long.valueOf(0L);
    }

    public Long ttlbyte() {
        try {
            Long result = this.jedis.ttl(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Long.valueOf(0L);
    }

    public Boolean exists() {
        try {
            Boolean result = this.jedis.exists(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Boolean.valueOf(false);
    }

    public Boolean existsbyte() {
        try {
            Boolean result = this.jedis.exists(this.key.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Boolean.valueOf(false);
    }

    public Boolean hexists(String field) {
        try {
            Boolean result = this.jedis.hexists(this.key, field);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Boolean.valueOf(false);
    }

    public Boolean hexistsbyte(String field) {
        try {
            Boolean result = this.jedis.hexists(this.key.getBytes(), field.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Boolean.valueOf(false);
    }

    public Long incrBy(int value) {
        Long result;
        try {
            result = this.jedis.incrBy(this.key, (long)value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long decrBy(int value) {
        Long result;
        try {
            result = this.jedis.decrBy(this.key, (long)value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long del() {
        Long result;
        try {
            result = this.jedis.del(this.key);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long delbyte() {
        Long result;
        try {
            result = this.jedis.del(this.key.getBytes());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long hdel(String field) {
        Long result;
        try {
            result = this.jedis.hdel(this.key, new String[]{field});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long hdelbyte(String field) {
        Long result;
        try {
            result = this.jedis.hdel(this.key.getBytes(), new byte[][]{field.getBytes()});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long sadd(String members) {
        Long result;
        try {
            result = this.jedis.sadd(this.key, new String[]{members});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long saddbyte(String member) {
        Long result;
        try {
            result = this.jedis.sadd(this.key.getBytes(), new byte[][]{member.getBytes()});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long sadd(String... members) {
        Long result;
        try {
            result = this.jedis.sadd(this.key, members);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long saddbyte(byte[]... members) {
        Long result;
        try {
            result = this.jedis.sadd(this.key.getBytes(), members);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long srem(String member) {
        Long result;
        try {
            result = this.jedis.srem(this.key, new String[]{member});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long srembyte(String member) {
        Long result;
        try {
            result = this.jedis.srem(this.key.getBytes(), new byte[][]{member.getBytes()});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Boolean sismember(String member) {
        try {
            Boolean result = this.jedis.sismember(this.key, member);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Boolean.valueOf(false);
    }

    public Set<String> smembers() {
        try {
            Set result = this.jedis.smembers(this.key);
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return new HashSet();
    }

    public Boolean sismemberbyte(String member) {
        try {
            Boolean result = this.jedis.sismember(this.key.getBytes(), member.getBytes());
            return result;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return Boolean.valueOf(false);
    }

    public Set<String> zrangeByScore(double min, double max) {
        try {
            Set e = this.jedis.zrangeByScore(this.key, min, max);
            return e;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(this.jedis);
        }

        return new HashSet();
    }

    public String getSet(String value) {
        String result;
        try {
            result = this.jedis.getSet(this.key, value);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public byte[] getSetbyte(String value) {
        byte[] result;
        try {
            result = this.jedis.getSet(this.key.getBytes(), value.getBytes());
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public Long publish(String message) {
        Long result;
        try {
            result = this.jedis.publish(this.key, message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

        return result;
    }

    public void subscribe(JedisPubSub pubSub) {
        try {
            this.jedis.subscribe(pubSub, new String[]{this.key});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

    }

    public void psubscribe(JedisPubSub pubSub) {
        try {
            this.jedis.psubscribe(pubSub, new String[]{this.key});
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw e;
        } finally {
            RedisUtils.close(this.jedis);
        }

    }

    public RedisClient() {
    }

    public RedisClient(Jedis jedis, int db, String key) {
        this.jedis = jedis;
        this.jedis.select(db);
        this.key = key;
    }

    public Jedis getJedis() {
        return this.jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
