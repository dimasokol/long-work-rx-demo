package ru.dimasokol.demo.longwork.usecase;

import io.reactivex.Observable;
import ru.dimasokol.demo.longwork.data.DownloadRepository;
import ru.dimasokol.demo.longwork.data.LongWorkRepository;

public class LongWorkDemoInteractorImpl implements LongWorkDemoInteractor {

    private final DownloadRepository mDownloadRepository;
    private final LongWorkRepository mLongWorkRepository;

    public LongWorkDemoInteractorImpl(DownloadRepository downloadRepository, LongWorkRepository longWorkRepository) {
        mDownloadRepository = downloadRepository;
        mLongWorkRepository = longWorkRepository;
    }

    @Override
    public Observable<WorkStep> startVeryLongWork() {
        // Вот так примитивно: обсёрвабл с долгой работой, и первый элемент укажем сразу
        // А вот Subscriber тут в принципе и не нужен
        return Observable.create(new SomeBigWorker(mDownloadRepository, mLongWorkRepository))
                .startWith(new WorkStep(WorkStep.Stage.STARTING_UP));
    }

}
