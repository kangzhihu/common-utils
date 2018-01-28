package com.meta.common;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {
    private static final transient Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 获取对象List的Map<unionField,Bean>格式的数据
     * @param beanList
     * @param unionField 唯一属性名
     * @param <K> unionField值类型
     *  @param <V> Bean类型
     * @return
     */
    public static <K, V> Map<K, V> getBeanListMap(List<V> beanList, String unionField) {
        Map<K, V> map = new HashMap<>();
        if (beanList == null || beanList.isEmpty()) {
            return map;
        }
        beanList.stream().forEach((bean) -> {
            try {
                Map tmap = BeanUtils.describe(bean);
                if (tmap.get(unionField) != null) {
                    map.put((K) tmap.get(unionField), bean);
                }
            } catch (Exception e) {
                logger.error("unexpected exception on bean operation!");
            }
        });
        return map;
    }

    /**
     * 获取简单list bean同key多value类型map
     * @param beanList
     * @param filed bean属性字段
     * @param value bean属性字段
     * @param <K>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <K,V,E> Multimap<K, V> getMapFromBeanList(List<E> beanList, String filed, String value) {
        Multimap<K, V> beanMultimap = ArrayListMultimap.create();
        if(beanList == null || beanList.isEmpty()){
            return beanMultimap;
        }
        for(E bean:beanList){
            Map tmap = null;
            try {
                tmap = BeanUtils.describe(bean);
            } catch (Exception e) {
                return ArrayListMultimap.create();
            }
            if (tmap.get(filed) != null) {
                beanMultimap.put((K) tmap.get(filed),(V) tmap.get(value));
            }
        }
        return beanMultimap;
    }

    /**
     * 获取简单list bean同key多value类型map
     * @param beanList
     * @param filed bean属性字段
     * @param <K>
     * @param <E>
     * @return
     */
    public static <K,E> Multimap<K, E> getMapFromBeanList(List<E> beanList, String filed) {
        Multimap<K, E> beanMultimap = ArrayListMultimap.create();
        if(beanList == null || beanList.isEmpty()){
            return beanMultimap;
        }
        for(E bean:beanList){
            Map tmap = null;
            try {
                tmap = BeanUtils.describe(bean);
            } catch (Exception e) {
                return ArrayListMultimap.create();
            }
            if (tmap.get(filed) != null) {
                beanMultimap.put((K) tmap.get(filed),bean);
            }
        }
        return beanMultimap;
    }
}
