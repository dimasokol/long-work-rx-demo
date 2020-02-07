package ru.dimasokol.demo.longwork.data;

import ru.dimasokol.demo.longwork.exceptions.ProcessingException;

public interface LongWorkRepository {

    void processFile(String fileName) throws ProcessingException;

}
