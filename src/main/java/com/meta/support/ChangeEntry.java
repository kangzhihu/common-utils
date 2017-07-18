package com.meta.support;

import com.meta.common.DateUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeEntry {
    
    private static final transient Logger logger = LoggerFactory.getLogger(ChangeEntry.class);
    
    public ChangeEntry(){
    }
    public ChangeEntry(Map<String, ?> oldMap){
        this.oldMap = oldMap;
    }
    public ChangeEntry(Object oldObj){
        setOld(oldObj);
    }
    private Map<String, ?> oldMap;
    @SuppressWarnings("unchecked")
    public void setOld(Object oldObj){
        if(oldObj!=null) {
            try {
                oldMap = BeanUtils.describe(oldObj);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        if(oldMap==null) oldMap = new HashMap<String, Object>();
    }

    /**
     * 获取简单对象属性的变更
     * @param newObj
     * @return
     */
    public Map<String, String> getChangeMap(Object newObj){
        Map<String, String> changeMap = new HashMap<String, String>();
        if(newObj == null){
            return changeMap;
        }
        Map<String, ?> tmpMap = new HashMap<String, Object>(oldMap);
        Map<String, ?> newMap = null;
        try {
            newMap = BeanUtils.describe(newObj);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        List<String> keySet = new ArrayList<String>();
        keySet.addAll(tmpMap.keySet());
        keySet.addAll(newMap.keySet());
        for(String key: keySet){
            Object oldvalue = tmpMap.remove(key);
            Object newvalue = newMap.remove(key);
            String ov = getStringValue(oldvalue);
            String nv = getStringValue(newvalue);
            if(!StringUtils.equals(ov, nv)){
                changeMap.put(key,nv);
            }
        }
        return changeMap;
    }
    public String getChangeMap(Map<String, String> changeMap){
        if(changeMap.isEmpty()) return "";
        String change = changeMap.toString();
        return change;
    }
    public String getStringValue(Object value){
        if(value==null) return "";
        if(value instanceof String) return (String) value;
        if(ClassUtils.isPrimitiveOrWrapper(value.getClass())) return ""+value;
        if(value instanceof Timestamp) return DateUtils.format((Timestamp) value,"yyyy-MM-dd-HH-mm-ss");
        if(value instanceof Date) return DateUtils.format((Date)value,"yyyy-MM-dd-HH-mm-ss");
        return ""+value;
    }
}
