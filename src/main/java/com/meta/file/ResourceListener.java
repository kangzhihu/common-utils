package com.meta.file;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Author: zhihu.kang<br/>
 * Data: 2017/6/26 23:18<br/>
 * Email: kangzhihu@163.com<br/>
 * Description:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;文件目录内容变更监听器。可用于项目配置文件变更后自动加载。<br/><br/>
 */

public class ResourceListener {
    private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();
    private WatchService watchService;

    private ResourceListener(String path) {
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> { //JVM shutdown
                try {
                    watchService.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    /**
     * 监听指定目录
     * @param path 目录地址
     * @param eventMap 事件及处理方法map
     * @throws IOException
     *
     * <pre>exp:<br/>{@code
     * Map<WatchEvent.Kind<?>, Consumer<WatchEvent<?>>> eventMap = new HashMap<>();
     * eventMap.put(StandardWatchEventKinds.ENTRY_MODIFY, (event) -> {
     *     System.out.println("map modify-->"+event.context());
     * };
     * ResourceListener.addListener("D:/test",eventMap);
     * }
     * </pre>
     */
    public static void addListener(String path,Map<WatchEvent.Kind<?>, Consumer<WatchEvent<?>>> eventMap) throws IOException {
        ResourceListener resourceListener = new ResourceListener(path);
        Path p = Paths.get(path);
        p.register(resourceListener.watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_CREATE);
        resourceListener.startListener(eventMap);
    }

    private void startListener(Map<WatchEvent.Kind<?>, Consumer<WatchEvent<?>>> eventMap) {
        fixedThreadPool.execute(new Listner(watchService, eventMap));
    }
}

class Listner implements Runnable {
    private WatchService service;
    private Map<WatchEvent.Kind<?>, Consumer<WatchEvent<?>>> eventMap;

    public Listner(WatchService service, Map<WatchEvent.Kind<?>, Consumer<WatchEvent<?>>> eventMap) {
        this.service = service;
        this.eventMap = eventMap;
    }

    @Override
    public void run() {
        try {
            while (true) {
                WatchKey watchKey = service.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                watchEvents.forEach(event->{
                    if(eventMap !=null && eventMap.containsKey(event.kind())){
                        eventMap.get(event.kind()).accept(event);
                    }
                });
                watchKey.reset();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                service.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
