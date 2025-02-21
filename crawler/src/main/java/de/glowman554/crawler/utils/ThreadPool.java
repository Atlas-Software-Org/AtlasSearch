package de.glowman554.crawler.utils;

import de.glowman554.search.utils.Logger;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class ThreadPool {
    private final ArrayList<Thread> threads = new ArrayList<>();
    private final Stack<Runnable> tasks = new Stack<>();

    private boolean running = true;

    private int maxQueueSize = 100;

    public ThreadPool(int size) {
        startThreads(size);
    }

    public ThreadPool() {
        String maxThreadsStr = System.getenv("MAX_THREADS");
        int maxThreads = 32;
        if (maxThreadsStr != null) {
            try {
                maxThreads = Integer.parseInt(maxThreadsStr);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid MAX_THREADS: " + maxThreads);
            }
        }
        startThreads(maxThreads);
    }

    public static void safeSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startThreads(int size) {
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(this::poll);
            threads.add(thread);
            thread.start();
        }
    }

    public void submit(Runnable task) {
        if (!running) {
            throw new IllegalStateException("Thread pool is not running");
        }

        while (tasks.size() > maxQueueSize) {
            safeSleep(1);
        }
        tasks.push(task);
    }

    private void poll() {
        while (running) {
            try {
                Runnable task = tasks.pop();
                task.run();
            } catch (EmptyStackException e) {
                safeSleep(100);
            } catch (Exception e) {
                Logger.exception(e);
            }
        }
    }

    public void stop() {
        while (!tasks.isEmpty()) {
            safeSleep(100);
        }

        running = false;
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
