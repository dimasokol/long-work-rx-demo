package ru.dimasokol.demo.longwork.exceptions;

import androidx.annotation.Nullable;

class FileRelatedException extends Exception {
    @Nullable
    private final String mFileName;

    FileRelatedException(@Nullable String fileName) {
        mFileName = fileName;
    }

    /**
     * Имя файла, который не удалось загрузить или обработать как-то иначе
     *
     * @return Имя файла, или {@code null}, если загрузить не удалось весь список
     */
    @Nullable
    public String getFileName() {
        return mFileName;
    }
}
