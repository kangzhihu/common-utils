package disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import disruptor.model.MyEvent;

/**
 * Created by user on 2017/8/15.
 */
public class EventProducer<T>{
    private RingBuffer<MyEvent<T>> ringBuffer;

    public EventProducer(RingBuffer<MyEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private final EventTranslatorOneArg TRANSLATOR = new EventTranslatorOneArg<MyEvent<T>, T>(){

        @Override
        public void translateTo(MyEvent<T> myEvent, long sequence, T value) {
            myEvent.setValue(value);
        }
    };

    public void onData(final T value) {
        ringBuffer.publishEvent(TRANSLATOR,value);
    }

}
