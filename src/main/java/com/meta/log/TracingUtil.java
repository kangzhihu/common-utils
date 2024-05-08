
import com.google.errorprone.annotations.MustBeClosed;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

import java.util.HashMap;
import java.util.Map;

public class TracingUtil {
    private final static Tracer tracer = GlobalTracer.get();

    /**
     * Starts a new span for a method execution.
     *
     * @param operationName the name of the operation to trace
     * @return the started Span
     */
    public static Span startMethodSpan(String operationName) {
        return tracer.buildSpan(operationName).start();
    }

    @MustBeClosed
    public static Scope activateSpan(Span span) {
        return tracer.activateSpan(span);
    }

    /**
     * Finishes the span for a method execution.
     *
     * @param span the Span to finish
     */
    public static void finishSpan(Span span) {
        if (span != null) {
            span.finish();
        }
    }

    public static void run(String spanName, Runnable runnable) {
        Span span = tracer.buildSpan(spanName).start();
        try (Scope ignore = TracingUtil.activateSpan(span)) {
            runnable.run();
        } catch (Throwable throwable) {
            onError(throwable, span);
            throw throwable;
        } finally {
            span.finish();
        }
    }

    public static void runNew(String spanName, Runnable runnable) {
        Span span = tracer.buildSpan(spanName).ignoreActiveSpan().start();
        try (Scope ignore = TracingUtil.activateSpan(span)) {
            runnable.run();
        } catch (Throwable throwable) {
            onError(throwable, span);
            throw throwable;
        } finally {
            span.finish();
        }
    }

    static void onError(Throwable throwable, Span span) {
        Tags.ERROR.set(span, Boolean.TRUE);

        if (throwable != null) {
            span.log(errorLogs(throwable));
        }
    }

    static Map<String, Object> errorLogs(Throwable throwable) {
        Map<String, Object> errorLogs = new HashMap<>(3);
        errorLogs.put("event", Tags.ERROR.getKey());
        errorLogs.put("error.object", throwable);
        return errorLogs;
    }
}
