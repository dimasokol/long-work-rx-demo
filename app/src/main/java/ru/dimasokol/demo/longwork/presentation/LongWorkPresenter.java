package ru.dimasokol.demo.longwork.presentation;

import io.reactivex.disposables.Disposable;
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
    private LongWorkView mView;

    private LongWorkView.ViewState mViewState = new LongWorkView.ViewState(WorkStep.NOT_STARTED, null);

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
                .subscribeOn(mSchedulersHolder.getIoScheduler())
                .observeOn(mSchedulersHolder.getMainScheduler())
                .subscribe(workStep -> {
                    mViewState = new LongWorkView.ViewState(workStep, null);
                    notifyView();
                }, throwable -> {
                    UserException exception;
                    if (throwable instanceof UserException) {
                        exception = (UserException) throwable;
                    } else {
                        exception = UserException.from((Exception) throwable);
                    }
                    mViewState = new LongWorkView.ViewState(WorkStep.COMPLETED, exception);
                    notifyView();
                }, () -> {
                    if (mViewState.getException() == null) {
                        mViewState = new LongWorkView.ViewState(WorkStep.COMPLETED, null);
                        notifyView();
                    }
                });
    }

    private void notifyView() {
        if (mView != null) {
            mView.renderState(mViewState);
        }
    }
}
