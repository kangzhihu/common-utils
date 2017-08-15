package disruptor.workerpool;

import com.lmax.disruptor.WorkHandler;
import disruptor.model.MyEvent;

/**
 * Created by user on 2017/8/15.
 */
public class EventWorkerHandler<T> implements WorkHandler<MyEvent<T>> {
    private String name;
    public EventWorkerHandler(String name){
        this.name = name;
    }

    @Override
    public void onEvent(MyEvent<T> event) throws Exception {
        System.out.println("worker handdler "+name+",msg:"+event.getValue());
    }
}
