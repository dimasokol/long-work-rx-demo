package ru.dimasokol.demo.longwork.data;

import java.util.Random;

import ru.dimasokol.demo.longwork.exceptions.ProcessingException;

public class LongWorkRepositoryImpl implements LongWorkRepository {

    /**
     * Вероятность ошибки в процентах. При обработке ошибка будет имитироваться
     * с указанной здесь вероятностью.
     */
    private static final int ERROR_PROBABILITY = 0;

    /**
     * Максимальная продолжительность имитации работы
     */
    private static final int MAX_SLEEP_PROCESSING = 200;

    private final Random mRandom = new Random();

    @Override
    public void processFile(String fileName) throws ProcessingException {
        if (mRandom.nextInt(100) < ERROR_PROBABILITY) {
            throw new ProcessingException(fileName);
        }

        try {
            Thread.sleep(mRandom.nextInt(MAX_SLEEP_PROCESSING));
        } catch (InterruptedException e) {
            throw new ProcessingException(fileName);
        }
    }
}
