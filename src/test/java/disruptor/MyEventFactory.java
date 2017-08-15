package disruptor;

import disruptor.model.MyEvent;
import com.lmax.disruptor.EventFactory;

/**
 * Created by user on 2017/8/15.
 */
public class MyEventFactory<T> implements EventFactory<MyEvent<T>>  {
    @Override
    public MyEvent<T> newInstance() {
        return new MyEvent<T>();
    }
}
