package disruptor;

import com.lmax.disruptor.ExceptionHandler;

class MyExceptionHandler implements ExceptionHandler {
        public void handleEventException(Throwable ex, long sequence, Object event) {}  
        public void handleOnStartException(Throwable ex) {}  
        public void handleOnShutdownException(Throwable ex) {}  
    } 