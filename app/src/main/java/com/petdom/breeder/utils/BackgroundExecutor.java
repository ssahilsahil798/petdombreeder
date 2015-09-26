package com.petdom.breeder.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by diwakar.mishra on 9/24/2015.
 */
public class BackgroundExecutor {
    private Executor executor;
    private static BackgroundExecutor instance = new BackgroundExecutor();

    private BackgroundExecutor() {
        executor = Executors.newFixedThreadPool(5);
    }

    public static BackgroundExecutor getInstance() {
        return instance;
    }

    public void run(Runnable runnable) {
        executor.execute(runnable);
    }
}
