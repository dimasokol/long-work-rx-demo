package ru.dimasokol.demo.longwork.usecase;

import io.reactivex.Flowable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import ru.dimasokol.demo.longwork.data.LongWorkRepository;
import ru.dimasokol.demo.longwork.exceptions.NetworkException;
import ru.dimasokol.demo.longwork.exceptions.ProcessingException;
import ru.dimasokol.demo.longwork.exceptions.UserException;
import ru.dimasokol.demo.longwork.utils.SchedulersHolder;

/**
 * Реализация долгой-долгой работы
 */
class SomeBigWorker implements ObservableOnSubscribe<WorkStep> {

    /**
     * Более этого количества файлов загружаться не будет никогда
     */
    public static final int MAX_DOWNLOADED_FILES = 10;

    private final DownloadInteractor mDownloadInteractor;
    private final LongWorkRepository mLongWorkRepository;
    private final SchedulersHolder mSchedulersHolder;

    private int mCurrentProgress = 0;
    private Disposable mDisposable;

    SomeBigWorker(DownloadInteractor downloadInteractor, LongWorkRepository longWorkRepository, SchedulersHolder schedulersHolder) {
        mDownloadInteractor = downloadInteractor;
        mLongWorkRepository = longWorkRepository;
        mSchedulersHolder = schedulersHolder;
    }

    @Override
    public void subscribe(ObservableEmitter<WorkStep> emitter) throws UserException {
        int totalProgress = mDownloadInteractor
                .getFilesCount()
                .doOnError(throwable -> {
                    if (throwable instanceof NetworkException) {
                        emitter.onError(UserException.from((NetworkException) throwable));
                    } else {
                        emitter.onError(throwable);
                    }
                })
                .onErrorReturnItem(0)
                .blockingGet();

        Flowable<String> filesSource = mDownloadInteractor.downloadFiles();

        mCurrentProgress = 0;

        mDisposable = filesSource
                .subscribeOn(mSchedulersHolder.getIoScheduler())
                .observeOn(mSchedulersHolder.getProcessingScheduler(), false, MAX_DOWNLOADED_FILES)
                .subscribe(file -> {
                        mCurrentProgress++;
                        emitter.onNext(WorkStep.processing(file, relativeProgress(mCurrentProgress, totalProgress)));

                        try {
                            mLongWorkRepository.processFile(file);
                        } catch (ProcessingException unable) {
                            throw UserException.from(unable);
                        } finally {
                            // TODO: delete file
                        }
                    },
                    emitter::onError,
                    emitter::onComplete);
    }


    private int relativeProgress(float current, float total) {
        return Math.round((current / total) * 100f);
    }
}
