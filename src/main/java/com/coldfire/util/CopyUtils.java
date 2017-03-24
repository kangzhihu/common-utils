package com.coldfire.util;

import com.coldfire.inter.CommonLogger;
import com.coldfire.support.Wrapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.commons.lang.ArrayUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Declaration:You can freely read, use or modify this file, and if you have a better idea, please contact the author to help upgrade this file.
 * Created by zhihu.kang
 * Time: 2016/12/26 22:57
 * Email:kangzhihu@163.com
 * Descriptions:对象复制工具类
 * 一整套实现还没看懂，在工作中看见使用十分方便就先记录下来。。
 */
public class CopyUtils {
    private static final transient CommonLogger dbLogger = LoggerUtils.getLogger(CopyUtils.class);
    /**
     * FastHashMap -- java.util.HashMap的一个实现，其适用的环境是有大量的读（如缓存数据），而很少有map结构的改动的环境，并且在这种环境下为线程安全。
     * 当以fast模式运行时,只读方法不是线程安全的,而写操作以下面的步骤进行操作
         1 克隆当前的数据集合
         2 在克隆对象上进行修改
         3 用修改过的克隆对象替换原对象
     *FastHashMap被初始化的时候,默认是以slow模式运行，所有的操作都要获得锁才能进行，以保证其线程安全。
     */
    //一个系统中，随着时间的增长缓存数据变多，此处copyPropsMap的读将极大的超过写，故用FastHashMap。
    private static Map<String/*srcClass+dstClass*/, List<String>> copyPropsMap = new FastHashMap();


    /**
     *复制并返回新对象列表
     * @param clazz 复制对象的类型
     * @param itemList 被复制对象List数组
     * @return clazz的实例对象列表
     */
    public static <S/*生成指定类型对象*/> List<S> copyBeanListProperties(Class<S> clazz, List itemList) {
        List<S> voItemList = new ArrayList<S>();
        if (CollectionUtils.isEmpty(itemList)){
            return voItemList;
        }
        try {
            for (Object item : itemList) {
                if(item==null) continue;
                Class src = item.getClass();
                Wrapper srcWrapper = Wrapper.getWrapper(src);
                Wrapper destWrapper = Wrapper.getWrapper(clazz);
                S itemVo = (S) destWrapper.gainNewInstance();
                List<String> props = getJoinProperties(srcWrapper.getReadPropertyNames(), src, clazz);
                copyInternal(itemVo, item, srcWrapper, destWrapper, props);
                voItemList.add(itemVo);
            }
        }catch(Exception e){
            dbLogger.warn(e, 10);
        }
        return voItemList;
    }

    /**
     * 复制对象的相同属性值
     * @param clazz 复制对象的类型
     * @param src 被复制对象/数据源
     * @return class类的实例对象
     */
    public static <S/*生成指定类型对象*/> S copyProperties(Class<S> clazz, Object src) {
        if (src == null)
            return null;
        try {
            Wrapper srcWrapper = Wrapper.getWrapper(src.getClass());
            Wrapper destWrapper = Wrapper.getWrapper(clazz);
            S itemVo = (S) destWrapper.gainNewInstance();
            List<String> props = getJoinProperties(srcWrapper.getReadPropertyNames(), src.getClass(), clazz);
            copyInternal(itemVo, src, srcWrapper, destWrapper, props);
            return itemVo;
        } catch (Exception e) {
            dbLogger.warn(e, 10);
        }
        return null;
    }

    /**
     * 筛选并缓存含有getter与setter的属性
     * @param srcProps
     * @param src
     * @param dest
     * @return
     */
    private static List<String> getJoinProperties(String[] srcProps, Class src, Class dest){
        String key = src.getCanonicalName() + "2" + dest.getCanonicalName();
        List<String> propList = copyPropsMap.get(key);
        if(propList == null){
            propList = new ArrayList<String>();
            synchronized(src){
                for (String name: srcProps) {
                    try{
                        PropertyDescriptor descriptor = new PropertyDescriptor(name, dest);//类属性描述器
                        if(PropertyUtils.getWriteMethod(descriptor)!=null){//对象的该属性能获取到setter方法
                            propList.add(name);
                        }
                    } catch (Exception e) {
                        //ignore e.printStackTrace();
                    }
                }
                copyPropsMap.put(key, UnmodifiableList.decorate(propList)/*不允许变更*/);
            }
        }
        return propList;
    }
    /**
     * 两个Bean之前简单属性copy
     * 限制：1）dst,src不能是Map，只能是POJO  2）无法级联
     * @param dst
     * @param src
     */
    public static void copy(Object dst, Object src)  {
        Wrapper srcWrapper = Wrapper.getWrapper(src.getClass());
        Wrapper destWrapper = Wrapper.getWrapper(dst.getClass());
        List<String> props = getJoinProperties(srcWrapper.getReadPropertyNames(), src.getClass(), dst.getClass());
        copyInternal(dst, src, srcWrapper, destWrapper, props);

    }

    /**
     * 复制对象
     * @param dst
     * @param src
     * @param allow 是否排除fields中的内容
     * @param fields
     */
    public static void copy(Object dst, Object src, boolean allow, String... fields){
        if(ArrayUtils.isEmpty(fields)){
            copy(dst, src);
        }else{
            Wrapper srcWrapper = Wrapper.getWrapper(src.getClass());
            Wrapper destWrapper = Wrapper.getWrapper(dst.getClass());
            List<String> copyProps = Arrays.asList(fields);
            if(!allow){
                List<String> props = getJoinProperties(srcWrapper.getReadPropertyNames(), src.getClass(), dst.getClass());
                List<String> remove = copyProps;

                copyProps = new ArrayList<String>(props);
                copyProps.removeAll(remove);
            }
            copyInternal(dst, src, srcWrapper, destWrapper, copyProps);
        }
    }

    private static void copyInternal(Object dst, Object src, Wrapper srcWrapper, Wrapper destWrapper, List<String> props) {
        for(String name: props){
            try{
                destWrapper.setPropertyValue(dst, name, srcWrapper.getPropertyValue(src, name));
            }catch(Throwable e){
                dbLogger.warn(name, e, 10);
            }
        }
    }

    /**
     * 目前只支持简单对象copy</br>
     * 参数beanmap，key是对象的属性，value是属性对应的值
     * @param dst
     * @param beanmap
     * @author leo
     * @addTime 2016年4月27日下午6:35:58
     */
    public static void copy(Object dst, Map<String, Object> beanmap)  {
        Wrapper destWrapper = Wrapper.getWrapper(dst.getClass());
        copyInternal(dst, destWrapper, beanmap);
    }

    private static void copyInternal(Object dst, Wrapper destWrapper, Map<String, Object> beanmap) {
        for(Map.Entry<String, Object> entry : beanmap.entrySet()){
            try{
                destWrapper.setPropertyValue(dst, entry.getKey(), entry.getValue());
            }catch(Throwable e){
                dbLogger.warn(entry.getKey(), e, 10);
            }
        }
    }
}
