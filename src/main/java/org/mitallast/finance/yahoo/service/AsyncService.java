package org.mitallast.finance.yahoo.service;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

abstract public class AsyncService implements Closeable {
    private final LinkedBlockingQueue<Runnable> queue;
    protected final ExecutorService pool;

    public AsyncService() {
        int threads = Runtime.getRuntime().availableProcessors();
        queue = new LinkedBlockingQueue<>();
        pool = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, queue);
    }

    public final int size() {
        return queue.size();
    }

    @Override
    public final void close() throws IOException {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                // Cancel currently executing tasks
                pool.shutdownNow();
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
