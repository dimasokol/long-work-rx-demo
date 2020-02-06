package ru.dimasokol.demo.longwork.usecase;

import io.reactivex.Observable;

public class LongWorkDemoInteractorImpl implements LongWorkDemoInteractor {
    @Override
    public Observable<WorkStep> startVeryLongWork() {
        // Вот так примитивно: обсёрвабл с долгой работой, и первый элемент укажем сразу
        // А вот Subscriber тут в принципе и не нужен
        return Observable.create(new SomeBigWorker())
                .startWith(new WorkStep(WorkStep.Stage.STARTING_UP));
    }

}
