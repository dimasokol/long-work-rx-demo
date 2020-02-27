package ru.dimasokol.demo.longwork.presentation;

import androidx.annotation.StringRes;

import io.reactivex.disposables.Disposable;
import ru.dimasokol.demo.longwork.R;
import ru.dimasokol.demo.longwork.exceptions.UserException;
import ru.dimasokol.demo.longwork.usecase.LongWorkDemoInteractor;
import ru.dimasokol.demo.longwork.usecase.WorkStep;
import ru.dimasokol.demo.longwork.utils.SchedulersHolder;

/**
 * Презентер, отличающийся небольшой спецификой из-за работы с несколькими вью попеременно
 */
public class LongWorkPresenter {

    private final LongWorkDemoInteractor mInteractor;
    private final SchedulersHolder mSchedulersHolder;

    private Disposable mDisposable;
    private WorkStep mWorkStep = WorkStep.NOT_STARTED;

    private LongWorkView mView;
    private UserException mException;

    /**
     * Конструктор принимает обязательный интерактор, и обязательный источник планировщиков
     *
     * @param interactor интерактор
     * @param schedulersHolder источник планировщиков для потоков RX
     */
    public LongWorkPresenter(LongWorkDemoInteractor interactor, SchedulersHolder schedulersHolder) {
        mInteractor = interactor;
        mSchedulersHolder = schedulersHolder;
    }

    /**
     * Безусловно привязывает вью к презентеру
     *
     * @param view Вью для привязки к презентеру
     */
    public void attachView(LongWorkView view) {
        mView = view;
        notifyView();
    }

    /**
     * Привязывает вью к презентеру, только если никакая другая вью ещё не привязана. Это необходимо
     * для взаимодействия с сервисом: он не должен затирать активити.
     *
     * @param view Вью для привязки к презентеру
     */
    public void attachViewIfNone(LongWorkView view) {
        if (mView == null) {
            mView = view;
            notifyView();
        }
    }

    /**
     * Отвязывает вью от презентера, но только если сейчас привязана именно указанная. Это необходимо
     * для того, чтобы компоненты отсоединяли лишь свою вью.
     *
     * @param expected Ожидаемая вью; если к презентеру сейчас привязана она, то будет выполнено
     *                 обнуление ссылки на вью
     */
    public void compareAndDetachView(LongWorkView expected) {
        if (mView == expected) {
            mView = null;
        }
    }

    /**
     * Запуск имитации долгой работы, можно вызывать неоднократно без перезапуска
     */
    public void startLongWork() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            return;
        }

        mDisposable = mInteractor.startVeryLongWork()
                .subscribeOn(mSchedulersHolder.getProcessingScheduler())
                .observeOn(mSchedulersHolder.getMainScheduler())
                .subscribe(workStep -> {
                    mWorkStep = workStep;
                    notifyView();
                }, throwable -> {
                    throwable.printStackTrace();

                    if (throwable instanceof UserException) {
                        mException = (UserException) throwable;
                    } else {
                        mException = UserException.from((Exception) throwable);
                    }
                    notifyView();
                }, () -> {
                    mWorkStep = WorkStep.COMPLETED;
                    notifyView();
                });
    }

    private void notifyView() {
        if (mView == null) {
            return;
        }

        // Есть эксепшен = ошибка
        if (mException != null) {
            mView.showError(mException.getMessageRes(),
                    mException.getMessageArgument() != null? mException.getMessageArgument().toString() : null);
            return;
        }

        // Процесс завершён без эксепшена = порядок
        if (mWorkStep.getStage() == WorkStep.Stage.COMPLETED) {
            mView.onCompleted();
            return;
        }

        // Иначе процесс у нас идёт
        @StringRes int message = R.string.app_name;

        switch (mWorkStep.getStage()) {
            case STARTING_UP:
                message = R.string.progress_starting;
                break;
            case DOWNLOADING:
                message = R.string.progress_downloading;
                break;
            case PROCESSING:
                message = R.string.progress_processing;
                break;
        }

        mView.showProgress(message, mWorkStep.getWorkSubject(), mWorkStep.getTotalProgress());
    }
}
