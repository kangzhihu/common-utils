import com.meta.cache.redis.RedisPools;
import com.meta.cache.redis.RedisShardPools;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.IOException;

/**
 * Created by zhihu on 2016/12/11.
 * Email:kangzhihu@163.com
 * Descriptions:
 */
public class SimpleTest {
    private static final transient Logger logger = LoggerFactory.getLogger(SimpleTest.class);
    private static int index = 0;
    @Test
    public void testMethod() throws Exception{

        logger.error("dsdasd");
        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String key = String.valueOf(Thread.currentThread().getId()) + "_" + (index++);
                    JedisPool jds = null;
                    RedisPools tpool = null;
                    Jedis jsdis = jds.getResource();
                    try {
                        tpool = RedisPools.getInstance();
                        jds = tpool.getHashPool(key);
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

     public static void main(String[] args) throws Exception {
     }
}
