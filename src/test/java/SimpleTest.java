import com.meta.file.ResourceListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by zhihu on 2016/12/11.
 * Email:kangzhihu@163.com
 * Descriptions:
 */
public class SimpleTest {
    private static final transient Logger dbLogger = LoggerFactory.getLogger(SimpleTest.class);
    @Test
    public void testMethod() throws Exception{

    }

    public static void main(String[] args) throws IOException {
        Map<WatchEvent.Kind<?>, Consumer<WatchEvent<?>>> eventMap =new HashMap<>();
        eventMap.put(StandardWatchEventKinds.ENTRY_MODIFY, (event) -> {
            System.out.println("map modify-->"+event.context());
        });
        eventMap.put(StandardWatchEventKinds.ENTRY_CREATE, (event) -> {
            System.out.println("map crreat-->"+event.context());
        });
        ResourceListener.addListener("E:/222/111.txt",eventMap);
    }
}
