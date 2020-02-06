package ru.dimasokol.demo.longwork.usecase;

import io.reactivex.Observable;

public interface LongWorkDemoInteractor {

    Observable<WorkStep> startVeryLongWork();

}
