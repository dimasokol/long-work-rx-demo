package ru.dimasokol.demo.longwork.exceptions;

/**
 * Сетевое исключение с указанием имени файла, который не удалось загрузить
 */
public class NetworkException extends FileRelatedException {

    public NetworkException(String fileName) {
        super(fileName);
    }

    public NetworkException() {
        this(null);
    }

}
