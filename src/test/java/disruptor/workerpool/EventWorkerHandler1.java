package disruptor.workerpool;

import com.lmax.disruptor.WorkHandler;
import disruptor.model.MyEvent;

/**
 * Created by user on 2017/8/15.
 */
public class EventWorkerHandler1<T> implements WorkHandler<MyEvent<T>> {
    @Override
    public void onEvent(MyEvent<T> event) throws Exception {
        System.out.println("worker handdler1 处理类："+event.getValue());
    }
}
