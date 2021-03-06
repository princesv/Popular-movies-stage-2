package com.prince.popularmovies;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    Executor discIo;
    Executor networkIo;
    Executor mainThread;
    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;
    private AppExecutor(Executor discIo, Executor networkIo, Executor mainThread) {
        this.discIo = discIo;
        this.networkIo = networkIo;
        this.mainThread = mainThread;
    }

    public static AppExecutor getInstance(){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new AppExecutor(Executors.newSingleThreadExecutor(),Executors.newFixedThreadPool(3)
                ,new MainThreadExecutor());
            }
        }
        return sInstance;
    }
    public Executor discIo(){return discIo;}
    public Executor networkIo(){return networkIo;}
    public Executor getMainThread(){return mainThread;}
    private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
