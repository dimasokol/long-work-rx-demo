package ru.dimasokol.demo.longwork.usecase;

import io.reactivex.Observable;
import ru.dimasokol.demo.longwork.data.LongWorkRepository;
import ru.dimasokol.demo.longwork.utils.SchedulersHolder;

public class LongWorkDemoInteractorImpl implements LongWorkDemoInteractor {

    private final DownloadInteractor mDownloadInteractor;
    private final LongWorkRepository mLongWorkRepository;
    private final SchedulersHolder mSchedulersHolder;

    public LongWorkDemoInteractorImpl(DownloadInteractor downloadInteractor, LongWorkRepository longWorkRepository, SchedulersHolder schedulersHolder) {
        mDownloadInteractor = downloadInteractor;
        mLongWorkRepository = longWorkRepository;
        mSchedulersHolder = schedulersHolder;
    }

    @Override
    public Observable<WorkStep> startVeryLongWork() {
        // Вот так примитивно: обсёрвабл с долгой работой, и первый элемент укажем сразу
        // А вот Subscriber тут в принципе и не нужен
        return Observable.create(new SomeBigWorker(mDownloadInteractor, mLongWorkRepository, mSchedulersHolder))
                .startWith(new WorkStep(WorkStep.Stage.STARTING_UP));
    }

}
