package disruptor.workerpool;

import com.lmax.disruptor.WorkHandler;
import disruptor.model.ObjectEvent;

/**
 * Created by user on 2017/8/15.
 */
public class EventWorkerHandler1<T> implements WorkHandler<ObjectEvent<T>> {
    @Override
    public void onEvent(ObjectEvent<T> event) throws Exception {
        System.out.println("worker handdler1 处理类："+event.getValue());
    }
}
