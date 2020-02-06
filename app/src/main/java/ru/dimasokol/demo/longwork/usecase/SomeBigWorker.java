package ru.dimasokol.demo.longwork.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Реализация долгой-долгой работы
 */
class SomeBigWorker implements ObservableOnSubscribe<WorkStep> {

    private static final int MAX_SLEEP_DOWNLOAD = 200;
    private static final int MAX_SLEEP_PROCESSING = 200;

    private final Random mRandom = new Random();

    @Override
    public void subscribe(ObservableEmitter<WorkStep> emitter) throws Exception {
        List<String> stubFilenames = new ArrayList<>(1024);

        for (char first = 'a'; first <= 'z'; first++) {
            for (char second = 'a'; second <= 'z'; second++) {
                stubFilenames.add("" + first + second + ".jpg");
            }
        }

        int totalProgress = stubFilenames.size() * 2;
        int currentProgress = 0;

        // Имитируем долгую-долгую работу
        Thread.sleep(2000);

        // Теперь типа скачиваем файлы
        for (String filename : stubFilenames) {
            currentProgress++;
            emitter.onNext(WorkStep.downloading(filename, relativeProgress(currentProgress, totalProgress)));
            Thread.sleep(mRandom.nextInt(MAX_SLEEP_DOWNLOAD));
        }

        // А теперь типа обрабатываем
        for (String filename : stubFilenames) {
            currentProgress++;
            emitter.onNext(WorkStep.processing(filename, relativeProgress(currentProgress, totalProgress)));
            Thread.sleep(mRandom.nextInt(MAX_SLEEP_PROCESSING));
        }

        emitter.onComplete();
    }


    private int relativeProgress(float current, float total) {
        return Math.round((current / total) * 100f);
    }
}
