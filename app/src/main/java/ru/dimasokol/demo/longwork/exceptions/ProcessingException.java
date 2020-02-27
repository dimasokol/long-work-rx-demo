package ru.dimasokol.demo.longwork.exceptions;

/**
 * Исключение пост-обработки, с указанием имени файла, который не удалось прожевать
 */
public class ProcessingException extends FileRelatedException {

    public ProcessingException(String fileName) {
        super(fileName);
    }
}
