package ru.dimasokol.demo.longwork.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.dimasokol.demo.longwork.exceptions.NetworkException;

public class DownloadRepositoryImpl implements DownloadRepository {

    /**
     * Вероятность ошибки в процентах. При обработке ошибка будет имитироваться
     * с указанной здесь вероятностью.
     */
    private static final int DOWNLOAD_ERROR_PROBABILITY = 0;
    private static final int LIST_ERROR_PROBABILITY = 0;

    /**
     * Максимальная продолжительность имитации загрузки
     */
    private static final int MAX_SLEEP_DOWNLOAD = 200;

    private final Random mRandom = new Random();

    @Override
    public List<String> getFileNames() throws NetworkException {
        // Имитируем долгую-долгую работу
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new NetworkException();
        }

        if (mRandom.nextInt(100) < LIST_ERROR_PROBABILITY) {
            throw new NetworkException();
        }

        List<String> stubFilenames = new ArrayList<>(1024);

        for (char first = 'a'; first <= 'z'; first++) {
            for (char second = 'a'; second <= 'z'; second++) {
                stubFilenames.add("" + first + second + ".jpg");
            }
        }

        return stubFilenames;
    }

    @Override
    public void downloadFile(String filename) throws NetworkException {
        if (mRandom.nextInt(100) < DOWNLOAD_ERROR_PROBABILITY) {
            throw new NetworkException(filename);
        }

        try {
            Thread.sleep(mRandom.nextInt(MAX_SLEEP_DOWNLOAD));
        } catch (InterruptedException e) {
            throw new NetworkException(filename);
        }
    }
}
