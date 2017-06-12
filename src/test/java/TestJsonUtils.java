import com.meta.util.CopyUtils;
import com.meta.util.DateUtils;
import com.meta.util.JsonUtils;
import model.Person;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhihu on 2016/12/11.
 * Email:kangzhihu@163.com
 * Descriptions:
 */
public class TestJsonUtils {
    private static final transient Logger dbLogger = LoggerFactory.getLogger(TestJsonUtils.class);
    @Test
    public void testMethod(){
        Person person = Person.getDefaultPerson();
        List<Person> list = new ArrayList<Person>();
        list.add(Person.getDefaultPerson());
        list.add(Person.getDefaultPerson());
        list.add(Person.getDefaultPerson());
        List<Person> list1 = CopyUtils.copyBeanListProperties(Person.class,list);

        Person person1 =CopyUtils.copyProperties(Person.class,person);


        dbLogger.warn(JsonUtils.writeObjectToJson(DateUtils.getCurTruncTimestamp()));

    }
}
