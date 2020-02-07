package ru.dimasokol.demo.longwork.exceptions;

/**
 * Исключение пост-обработки, с указанием имени файла, который не удалось прожевать
 */
public class ProcessingException extends Exception {

    private final String mFileName;

    public ProcessingException(String fileName) {
        mFileName = fileName;
    }

    public String getFileName() {
        return mFileName;
    }
}
