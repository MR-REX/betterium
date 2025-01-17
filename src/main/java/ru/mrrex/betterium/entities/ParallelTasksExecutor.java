package ru.mrrex.betterium.entities;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelTasksExecutor {

    private final ExecutorService threadPool;

    public ParallelTasksExecutor(int threadsCount) {
        threadPool = Executors.newFixedThreadPool(threadsCount);
    }

    public ParallelTasksExecutor() {
        int availableThreadsCount = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(availableThreadsCount);
    }

    public boolean run(List<Callable<Void>> tasks) throws ExecutionException {
        boolean isAllTasksCompleted = false;

        try {
            threadPool.invokeAll(tasks).forEach(future -> {
                try {
                    future.get();
                } catch (ExecutionException exception) {
                    threadPool.shutdownNow();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            });

            isAllTasksCompleted = true;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } finally {
            threadPool.shutdown();
        }

        return isAllTasksCompleted;
    }
}
