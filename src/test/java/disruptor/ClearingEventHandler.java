package disruptor;

import com.lmax.disruptor.EventHandler;
import disruptor.model.ObjectEvent;

/**
 * help GC
 * @param <T>
 */
public class ClearingEventHandler<T> implements EventHandler<ObjectEvent<T>>
{
    public void onEvent(ObjectEvent<T> event, long sequence, boolean endOfBatch)
    {
        event.clear();
        System.out.println("do clearing");
    }
}