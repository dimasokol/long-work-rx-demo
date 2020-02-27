package ru.dimasokol.demo.longwork.utils;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * «Боевая» реализация планировщиков
 */
public class RealSchedulersHolder implements SchedulersHolder {

    @Override
    public Scheduler getIoScheduler() {
        return Schedulers.io();
    }

    @Override
    public Scheduler getProcessingScheduler() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler getMainScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
