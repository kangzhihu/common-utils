import com.coldfire.inter.CommonLogger;
import com.coldfire.util.CopyUtils;
import com.coldfire.util.DateUtils;
import com.coldfire.util.JsonUtils;
import com.coldfire.util.LoggerUtils;
import model.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhihu.coldfire on 2016/12/11.
 * Email:kangzhihu@163.com
 * Descriptions:
 */
public class TestJsonUtils {
    private static final CommonLogger dbLogger = LoggerUtils.getLogger(TestJsonUtils.class);
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
