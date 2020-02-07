package ru.dimasokol.demo.longwork.exceptions;

import androidx.annotation.Nullable;

/**
 * Сетевое исключение с указанием имени файла, который не удалось загрузить
 */
public class NetworkException extends Exception {

    @Nullable
    private final String mFileName;

    public NetworkException(String fileName) {
        mFileName = fileName;
    }

    public NetworkException() {
        this(null);
    }

    /**
     * Имя файла, который не удалось загрузить
     *
     * @return Имя файла, или {@code null}, если загрузить не удалось весь список
     */
    @Nullable
    public String getFileName() {
        return mFileName;
    }
}
