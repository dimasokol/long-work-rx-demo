package ru.dimasokol.demo.longwork.presentation.utils;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.dimasokol.demo.longwork.utils.SchedulersHolder;

/**
 * Тестовый холдер возвращает тестовые планировщики
 */
public class TestSchedulersHolder implements SchedulersHolder {

    @Override
    public Scheduler getIoScheduler() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler getMainScheduler() {
        return Schedulers.trampoline();
    }
}
