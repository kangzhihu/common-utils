package disruptor.model;

/**
 * Created by user on 2017/8/15.
 */
public class MyEvent<T> {
    private T value;
    public void setValue(T value){
        this.value = value;
    }

    public T getValue(){
        return value;
    }
}
