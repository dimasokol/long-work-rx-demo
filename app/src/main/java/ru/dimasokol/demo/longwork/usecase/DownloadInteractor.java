package ru.dimasokol.demo.longwork.usecase;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DownloadInteractor {

    /**
     * Запускает процесс загрузки файлов
     *
     * @return Загрузчик файлов: объект с поддержкой backpressure, чтобы количество файлов можно
     *         было лимитировать при {@code observeOn}.
     */
    Flowable<String> downloadFiles();

    /**
     * Возвращает общее количество файлов для загрузки
     *
     * @return Сингл-значение общего количества файлов
     */
    Single<Integer> getFilesCount();
    
}
