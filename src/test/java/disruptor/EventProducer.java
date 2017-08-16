package disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import disruptor.model.ObjectEvent;

/**
 * Created by user on 2017/8/15.
 */
public class EventProducer<T>{
    private RingBuffer<ObjectEvent<T>> ringBuffer;

    public EventProducer(RingBuffer<ObjectEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private final EventTranslatorOneArg TRANSLATOR = new EventTranslatorOneArg<ObjectEvent<T>, T>(){

        @Override
        public void translateTo(ObjectEvent<T> myEvent, long sequence, T value) {
            myEvent.setValue(value);
        }
    };

    public void onData(final T value) {
        ringBuffer.publishEvent(TRANSLATOR,value);
    }

}
