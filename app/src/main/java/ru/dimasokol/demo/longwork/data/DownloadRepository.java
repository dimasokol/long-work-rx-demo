package ru.dimasokol.demo.longwork.data;

import java.util.List;

import ru.dimasokol.demo.longwork.exceptions.NetworkException;

public interface DownloadRepository {

    List<String> getFileNames() throws NetworkException;

    void downloadFile(String filename) throws NetworkException;

}
