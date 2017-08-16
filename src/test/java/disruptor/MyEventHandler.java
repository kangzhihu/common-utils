package disruptor;

import com.lmax.disruptor.EventHandler;
import disruptor.model.ObjectEvent;

/**
 * Created by user on 2017/8/15.
 */
public class MyEventHandler<T> implements EventHandler<ObjectEvent<T>> {
    @Override
    public void onEvent(ObjectEvent<T> event, long sequence, boolean b) throws Exception {
        System.out.println("handdler-"+event.getValue());
    }
}
