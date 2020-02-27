package ru.dimasokol.demo.longwork.usecase;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DownloadInteractor {

    Flowable<String> downloadFiles();

    Single<Integer> getFilesCount();
    
}
