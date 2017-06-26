package com.meta.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/6/26 23:07<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:
 * <br/> &nbsp;&nbsp;&nbsp;&nbsp;Guava Cache 工具类。<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;一个全内存的本地缓存实现，它提供了线程安全的实现机制，是本地缓存。
 * Guava Cache与ConcurrentMap很相似，但也不完全一样。最基本的区别是ConcurrentMap会一直保存所有添加的元素，直到显式地移除。
 *  相对地，Guava Cache为了限制内存占用，通常都设定为自动回收元素。<br/><br/>
 */

public class GuavaCacheUtils {
    public GuavaCacheUtils() {
    }

    /**
     * 获取缓冲.
     * <br><b>Warning：</b>CacheLoader重载的load方法<b>不能</b>返回<b>null</b>，否则将抛出异常。如果实际业务有null返回，则<b>必须</b>做好null处理.
     * <pre>{@code
     *      //warning: if locad return null,it will throws a exception!!
     *     public static LoadingCache<String, City> CITYS = GuavaCacheUtils.loadingCached(
     *          //if the key not exists in the cache,the init it by the given method.
     *          new CacheLoader<String, City>() {
     *              public City load(String key) throws Exception {
     *                  try{
     *                       City city = findCityIdList(); //get it
     *                       return city; //warn: will throw excepton if is null.
     *                }catch(Exception e){//
     *                     e.printStackTrace();
     *                      return null;
     *                }
     *              }
     *      }, 1000, -1, 24 * 3600, 1800);
     *
     *
     *     public City getCityByCityId(String cityId) {
     *         City city = null;
     *         try{
     *             city = GuavaCaches.CITYS.getUnchecked(cityId);
     *         }catch(Exception e){
     *             // will get exception if  new CacheLoader().load() return null.
     *             GuavaCaches.CITYS.invalidate(cityId);
     *         }
     *         if(city == null){    //remove tht cached key
     *             GuavaCaches.CITYS.invalidate(cityId);
     *         }
     *         return city;
     *     }
     *
     * }</pre>
     * @param cacheLoader 针对整个cache给定统一初始值
     * @param maxmumSize 最大缓存对象数量
     * @param expireAfterWrite 某个键值对被创建或值被替换后多少时间移除（TimeUnit.SECONDS）
     * @param expireAfterAccess 某个键值对最后一次访问之后多少时间后移除（TimeUnit.SECONDS）
     * @param refreshAfterWrite //某个键值对被创建或值被替换后多少时间自动刷新cache（TimeUnit.SECONDS）
     * @return
     */
    public static <K, V> LoadingCache<K, V> loadingCached(CacheLoader<K, V> cacheLoader, int maxmumSize, long expireAfterWrite, long expireAfterAccess, long refreshAfterWrite) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        cacheBuilder.maximumSize((long)maxmumSize);
        if(expireAfterWrite > 0L) {
            cacheBuilder.expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS);
        }

        if(expireAfterAccess > 0L) {
            cacheBuilder.expireAfterAccess(expireAfterAccess, TimeUnit.SECONDS);
        }

        if(refreshAfterWrite > 0L) {
            cacheBuilder.refreshAfterWrite(refreshAfterWrite, TimeUnit.SECONDS);
        }

        LoadingCache<K, V> cache = cacheBuilder.build(cacheLoader);
        return cache;
    }

    /**
     * callable callback的方式实现缓存
     * <pre>exp:<br/>{@code
     * public static Cache<String, City> CITYS = GuavaCacheUtils.callableCached(100, 600, -1);
     *
     * public City getDistrictByCityId(String cityId) {
     *  City city =null;
     *  try{
     *      city = GuavaCaches.CITYS.get(cityId, () -> { //if not exists in cache,get in db or somewhere and cache it
     *         City c = dao.getCityById(cityId);
     *         c != null?return city: return null;
     *     });
     *     if(city == null){ //if still not get,remove the chached key
     *         GuavaCaches.CITYS.invalidate(cityId);
     *     }
     *  }catch(Exception e){
     *      e.printStackTrace();
     *      GuavaCaches.CITYS.invalidate(cityId);
     *  }
     *  return city;
     * }
     *
     *
     * }</pre>
     * @param maxmumSize 最大缓存对象数量
     * @param expireAfterWrite  某个键值对被创建或值被替换后多少时间移除（TimeUnit.SECONDS）
     * @param expireAfterAccess 某个键值对最后一次访问之后多少时间后移除（TimeUnit.SECONDS）
     * @return
     */
    public static <K, V> Cache<K, V> callableCached(int maxmumSize, long expireAfterWrite, long expireAfterAccess) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        cacheBuilder.maximumSize((long)maxmumSize);
        if(expireAfterWrite > 0L) {
            cacheBuilder.expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS);
        }
        if(expireAfterAccess > 0L) {
            cacheBuilder.expireAfterAccess(expireAfterAccess, TimeUnit.SECONDS);
        }
        Cache<K, V> cache = cacheBuilder.build();
        return cache;
    }
}