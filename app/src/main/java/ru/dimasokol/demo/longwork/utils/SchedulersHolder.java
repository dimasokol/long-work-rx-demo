package ru.dimasokol.demo.longwork.utils;

import io.reactivex.Scheduler;

/**
 * Холдер, возвращающий планировщики для RxJava. Используется для нужд тестирования
 * презентеров: им просто можно передать стабовую версию, использующую один поток.
 */
public interface SchedulersHolder {

    /**
     * I/O-планировщик
     *
     * @return Планировщик для операций ввода-вывода, для {@code subscribeOn}.
     */
    Scheduler getIoScheduler();

    /**
     * Планировщик для результатов
     *
     * @return Планировщик для получения результатов, для {@code observeOn}.
     */
    Scheduler getMainScheduler();

}
