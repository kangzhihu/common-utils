package disruptor;

import com.lmax.disruptor.EventHandler;
import disruptor.model.MyEvent;

/**
 * Created by user on 2017/8/15.
 */
public class MyEventHandler<T> implements EventHandler<MyEvent<T>> {
    @Override
    public void onEvent(MyEvent<T> event, long sequence, boolean b) throws Exception {
        System.out.println("handdler-"+event.getValue());
    }
}
