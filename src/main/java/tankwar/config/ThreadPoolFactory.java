package tankwar.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadPoolFactory {
    private static volatile ExecutorService executorService;
    public static ExecutorService getExecutor(){
        if (null == executorService) {
            synchronized (ThreadPoolFactory.class){
                if (null == executorService){
                    executorService = Executors.newCachedThreadPool();
                }
                return executorService;
            }
        }
        return executorService;
    }
}
