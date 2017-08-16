package disruptor;

import disruptor.model.ObjectEvent;
import com.lmax.disruptor.EventFactory;

/**
 * Created by user on 2017/8/15.
 */
public class MyEventFactory<T> implements EventFactory<ObjectEvent<T>>  {
    @Override
    public ObjectEvent<T> newInstance() {
        return new ObjectEvent<T>();
    }
}
