package disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import disruptor.model.ObjectEvent;
import disruptor.workerpool.EventWorkerHandler;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 2017/8/15.
 */
public class DistruptorTest {
    int BUFFER_SIZE = 1024;
    int THREAD_NUMBERS = 4;
    ExecutorService executor = Executors.newFixedThreadPool(4);
    @Test
    public void testUseDisruptor() throws Exception{
        //创建disruptor
        Disruptor<ObjectEvent<String>> disruptor = new Disruptor<ObjectEvent<String>>(new MyEventFactory(), BUFFER_SIZE, new MyEventThreadFactory(), ProducerType.SINGLE, new BusySpinWaitStrategy());

        // 注册事件消费处理器, 也即消费者.
        // 以消费组的形式来处理消息
        EventHandlerGroup<ObjectEvent<String>> handlerGroup = disruptor.handleEventsWith(new MyEventHandler<String>(), new MyEventHandler1<String>());
        //声明在C1,C2完事之后执行C3,最后可启动一个清理Handler去处理最后的数据
        handlerGroup.then(new MyEventHandler2<String>()).then(new ClearingEventHandler<String>());

        // 启动
        disruptor.start();

        //2.将数据装入RingBuffer
        RingBuffer<ObjectEvent<String>> ringBuffer = disruptor.getRingBuffer();
        // 创建生产者
        EventProducer<String> producer = new EventProducer<>(ringBuffer);
        Random random = new Random();
        //发布消息
        for(int i = 0; i < 2; ++i){
            producer.onData("test**"+random.nextInt(20));
        }
        Thread.sleep(5000); //测试，让运行完毕
        disruptor.shutdown(); //关闭 disruptor  阻塞直至所有事件都得到处理
        executor.shutdown(); // 需关闭 disruptor使用的线程池, 上一步disruptor关闭时不会连带关闭线程池
    }


    @Test
    public void testOneEventProcessor() throws Exception{

        // 这里直接获得RingBuffer. createSingleProducer创建一个单生产者的RingBuffer

        // 第一个参数为EventFactory，产生数据Trade的工厂类
        // 第二个参数是RingBuffer的大小，需为2的N次方
        // 第三个参数是WaitStrategy, 消费者阻塞时如何等待生产者放入Event
        final RingBuffer<ObjectEvent<String>> ringBuffer = RingBuffer.createSingleProducer(new MyEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        //SequenceBarrier, 协调消费者与生产者, 消费者链的先后顺序. 阻塞后面的消费者(没有Event可消费时)
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //创建事件处理器 (消费者): 处理ringBuffer, 用MyEventHandler的方法处理(实现EventHandler), 用sequenceBarrier协调生成-消费
        //如果存在多个消费者(老api, 可用workpool解决) 那重复执行 创建事件处理器-注册进度-提交消费者的过程, 把其中TradeHandler换成其它消费者类
        BatchEventProcessor<ObjectEvent<String>> processor = new BatchEventProcessor<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new MyEventHandler());
        //把消费者的消费进度情况注册给RingBuffer结构(生产者)    !如果只有一个消费者的情况可以省略
        ringBuffer.addGatingSequences(processor.getSequence());

        //创建线程池
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMBERS);
        //把消费者提交到线程池, !说明EventProcessor实现了callable接口
        executors.submit(processor);

        // 创建生产者
        EventProducer<String> producer = new EventProducer<>(ringBuffer);
        Random random = new Random();
        //发布消息
        for(int i = 0; i < 2; ++i){
            producer.onData("test**"+random.nextInt(20));
        }

        Thread.sleep(1000); //等上1秒，等待消费完成
        processor.halt(); //通知事件处理器  可以结束了（并不是马上结束!）
        executors.shutdown();

    }


    //pipeline串行消费，方法基本类似testOneEventProcessor
    //通过在构造SequenceBarrier的时候传递依赖关系。
    @Test
    public void testThreeEventProcessorWithDependency() throws Exception{

        final RingBuffer<ObjectEvent<String>> ringBuffer = RingBuffer.createSingleProducer(new MyEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        // *** 消费者1
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        BatchEventProcessor<ObjectEvent<String>> processor = new BatchEventProcessor<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new MyEventHandler());

        // *** 消费者2
        SequenceBarrier sequenceBarrier1 = ringBuffer.newBarrier(processor.getSequence());
        BatchEventProcessor<ObjectEvent<String>> processor1 = new BatchEventProcessor<ObjectEvent<String>>(ringBuffer, sequenceBarrier1, new MyEventHandler1());

        // *** 消费者3
        SequenceBarrier sequenceBarrier2 = ringBuffer.newBarrier(processor1.getSequence());
        BatchEventProcessor<ObjectEvent<String>> processor2 = new BatchEventProcessor<ObjectEvent<String>>(ringBuffer, sequenceBarrier2, new MyEventHandler2());

        //构造反向依赖，依赖最小(晚)的Sequences
        ringBuffer.addGatingSequences(processor2.getSequence());

        //创建线程池
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMBERS);
        //把消费者提交到线程池, !说明EventProcessor实现了callable接口
        executors.submit(processor);
        executors.submit(processor1);
        executors.submit(processor2);

        // 创建生产者
        EventProducer<String> producer = new EventProducer<>(ringBuffer);
        Random random = new Random();
        //发布消息
        for(int i = 0; i < 2; ++i){
            producer.onData("test**"+random.nextInt(20));
        }

        Thread.sleep(1000); //等上1秒，等待消费完成
        executors.shutdown();

    }

    //三者并行消费，无依赖关系(或者说都依赖于初始sequenceBarrier)
    @Test
    public void testThreeEventProcessorWithOutDependency() throws Exception{

        final RingBuffer<ObjectEvent<String>> ringBuffer = RingBuffer.createSingleProducer(new MyEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        // *** 消费者1
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        BatchEventProcessor<ObjectEvent<String>> processor = new BatchEventProcessor<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new MyEventHandler());

        // *** 消费者2
        SequenceBarrier sequenceBarrier1 = ringBuffer.newBarrier(processor.getSequence());
        BatchEventProcessor<ObjectEvent<String>> processor1 = new BatchEventProcessor<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new MyEventHandler1());

        // *** 消费者3
        SequenceBarrier sequenceBarrier2 = ringBuffer.newBarrier(processor1.getSequence());
        BatchEventProcessor<ObjectEvent<String>> processor2 = new BatchEventProcessor<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new MyEventHandler2());

        //构造反向依赖，依赖最小(晚)的Sequences
        ringBuffer.addGatingSequences(processor.getSequence(),processor1.getSequence(),processor2.getSequence());

        //把消费者提交到线程池, !说明EventProcessor实现了callable接口
        executor.submit(processor);
        executor.submit(processor1);
        executor.submit(processor2);

        // 创建生产者
        EventProducer<String> producer = new EventProducer<>(ringBuffer);
        Random random = new Random();
        //发布消息
        for(int i = 0; i < 2; ++i){
            producer.onData("test**"+random.nextInt(20));
        }

        Thread.sleep(1000); //等上1秒，等待消费完成
        executor.shutdown();

    }


    //单个消费组，若组内有多个消费者，只会被一个消费者消费
    //多并行相同消费者时推荐使用
    @Test
    public void testOneWorkerPool() throws Exception{

        final RingBuffer<ObjectEvent<String>> ringBuffer = RingBuffer.createSingleProducer(new MyEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 第三个参数: 异常处理器, 这里用ExceptionHandler; 第四个参数必须为实现WorkHandler的处理类, 可为数组(即传入多个相同的消费者)
        WorkerPool<ObjectEvent<String>> workerPool = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new MyExceptionHandler(), new EventWorkerHandler<String>("single"));

        //消费组
 /*       final WorkHandler[] handlers = new EventWorkerHandler[2];
        for(int i=0;i<2;i++){
            handlers[i] = new EventWorkerHandler("precessor-"+i);
        }
        WorkerPool<ObjectEvent<String>> workerPool = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), handlers);
*/
        //一个消费组的时候貌似可以省略
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //启动处理器
        workerPool.start(executor);

        // 创建生产者
        EventProducer<String> producer = new EventProducer<>(ringBuffer);
        Random random = new Random();
        //发布消息
        for(int i = 0; i < 5; ++i){
            producer.onData("test**"+random.nextInt(100));
        }

        Thread.sleep(1000); //等上1秒，等待消费完成
        workerPool.halt();
        executor.shutdown();

    }

    //多个并行消费组，若组内有多个消费者，只会被每个消费组中的一个消费者消费
    @Test
    public void testMoreWorkerPool() throws Exception{

        final RingBuffer<ObjectEvent<String>> ringBuffer = RingBuffer.createSingleProducer(new MyEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

       /*   // 第三个参数: 异常处理器, 这里用ExceptionHandler; 第四个参数必须为实现WorkHandler的处理类, 可为数组(即传入多个相同的消费者)
        WorkerPool<ObjectEvent<String>> workerPool = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), new EventWorkerHandler<String>("worker pool one"));

        // 第三个参数: 异常处理器, 这里用ExceptionHandler; 第四个参数必须为实现WorkHandler的处理类, 可为数组(即传入多个相同的消费者)
        WorkerPool<ObjectEvent<String>> workerPool1 = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), new EventWorkerHandler<String>("worker pool two"));
        */
        //消费组
        final WorkHandler[] handlers = new EventWorkerHandler[2];
        for(int i=0;i<2;i++){
            handlers[i] = new EventWorkerHandler("worker pool one-"+i);
        }
        WorkerPool<ObjectEvent<String>> workerPool = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), handlers);

        //消费组
        final WorkHandler[] handlers1 = new EventWorkerHandler[2];
        for(int i=0;i<2;i++){
            handlers1[i] = new EventWorkerHandler("worker pool two-"+i);
        }
        WorkerPool<ObjectEvent<String>> workerPool1 = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), handlers1);

        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        ringBuffer.addGatingSequences(workerPool1.getWorkerSequences());
        //启动处理器
        workerPool.start(executor);
        workerPool1.start(executor);
        // 创建生产者
        EventProducer<String> producer = new EventProducer<>(ringBuffer);
        Random random = new Random();
        //发布消息
        for(int i = 0; i < 5; ++i){
            producer.onData("test**"+random.nextInt(100));
        }

        Thread.sleep(1000); //等上1秒，等待消费完成
        workerPool.halt();
        workerPool1.halt();
        executor.shutdown();

    }

    //消费组pipeline串行消费
    @Test
    public void testMoreWorkerPoolInPipeline() throws Exception{

        final RingBuffer<ObjectEvent<String>> ringBuffer = RingBuffer.createSingleProducer(new MyEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

       /*   // 第三个参数: 异常处理器, 这里用ExceptionHandler; 第四个参数必须为实现WorkHandler的处理类, 可为数组(即传入多个相同的消费者)
        WorkerPool<ObjectEvent<String>> workerPool = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), new EventWorkerHandler<String>("worker pool one"));

        // 第三个参数: 异常处理器, 这里用ExceptionHandler; 第四个参数必须为实现WorkHandler的处理类, 可为数组(即传入多个相同的消费者)
        WorkerPool<ObjectEvent<String>> workerPool1 = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), new EventWorkerHandler<String>("worker pool two"));
        */
        //消费组
        final WorkHandler[] handlers = new EventWorkerHandler[2];
        for(int i=0;i<2;i++){
            handlers[i] = new EventWorkerHandler("worker pool one-"+i);
        }
        WorkerPool<ObjectEvent<String>> workerPool = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), handlers);

        SequenceBarrier sequenceBarrier1 = ringBuffer.newBarrier(workerPool.getWorkerSequences());
        //消费组
        final WorkHandler[] handlers1 = new EventWorkerHandler[2];
        for(int i=0;i<2;i++){
            handlers1[i] = new EventWorkerHandler("worker pool two-"+i);
        }
        WorkerPool<ObjectEvent<String>> workerPool1 = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier1, new IgnoreExceptionHandler(), handlers1);

        //依赖最近序列
        ringBuffer.addGatingSequences(workerPool1.getWorkerSequences());
        //启动处理器
        workerPool.start(executor);
        workerPool1.start(executor);
        // 创建生产者
        EventProducer<String> producer = new EventProducer<>(ringBuffer);
        Random random = new Random();
        //发布消息
        for(int i = 0; i < 5; ++i){
            producer.onData("test**"+random.nextInt(100));
        }

        Thread.sleep(1000); //等上1秒，等待消费完成
        workerPool.halt();
        workerPool1.halt();
        executor.shutdown();

    }


    //多生产者，消费组pipeline串行消费
    @Test
    public void testMoreProcedureMoreWorkerPoolInPipeline() throws Exception{

        //为何这种创建方式也可以？？？
        //final RingBuffer<ObjectEvent<String>> ringBuffer = RingBuffer.createSingleProducer(new MyEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        final RingBuffer<ObjectEvent<String>> ringBuffer = RingBuffer.createMultiProducer(new MyEventFactory(), BUFFER_SIZE, new YieldingWaitStrategy());

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

       /*   // 第三个参数: 异常处理器, 这里用ExceptionHandler; 第四个参数必须为实现WorkHandler的处理类, 可为数组(即传入多个相同的消费者)
        WorkerPool<ObjectEvent<String>> workerPool = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), new EventWorkerHandler<String>("worker pool one"));

        // 第三个参数: 异常处理器, 这里用ExceptionHandler; 第四个参数必须为实现WorkHandler的处理类, 可为数组(即传入多个相同的消费者)
        WorkerPool<ObjectEvent<String>> workerPool1 = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), new EventWorkerHandler<String>("worker pool two"));
        */
        //消费组
        final WorkHandler[] handlers = new EventWorkerHandler[2];
        for(int i=0;i<2;i++){
            handlers[i] = new EventWorkerHandler("worker pool one-"+i);
        }
        WorkerPool<ObjectEvent<String>> workerPool = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), handlers);

        SequenceBarrier sequenceBarrier1 = ringBuffer.newBarrier(workerPool.getWorkerSequences());
        //消费组
        final WorkHandler[] handlers1 = new EventWorkerHandler[2];
        for(int i=0;i<2;i++){
            handlers1[i] = new EventWorkerHandler("worker pool two-"+i);
        }
        WorkerPool<ObjectEvent<String>> workerPool1 = new WorkerPool<ObjectEvent<String>>(ringBuffer, sequenceBarrier1, new MyExceptionHandler(), handlers1);

        //依赖最近序列
        ringBuffer.addGatingSequences(workerPool1.getWorkerSequences());
        //启动处理器
        workerPool.start(executor);
        workerPool1.start(executor);
        Random random = new Random();
        //发布消息
        for(int i = 0; i < 5; ++i){
            // 创建生产者
            EventProducer<String> producer = new EventProducer<>(ringBuffer);
            producer.onData("producer "+i+" send "+random.nextInt(100));
        }

        Thread.sleep(1000); //等上1秒，等待消费完成
        workerPool.halt();
        workerPool1.halt();
        executor.shutdown();

    }


}
