package com.meta.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Description:<br/> 克隆
 * Created by zhihu.kang <br/>
 * Time: 22:20<br/>
 * Email:kangzhihu@163.com<br/>
 */
public class CloneUtils {
    private static transient Logger logger = LoggerFactory.getLogger(CloneUtils.class);

    public static Object cloneObj(Serializable obj) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
             ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());){
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(obj);
            ObjectInputStream in = new ObjectInputStream(bais);
            return in.readObject();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }
}