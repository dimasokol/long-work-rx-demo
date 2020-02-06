package ru.dimasokol.demo.longwork.presentation;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;
import ru.dimasokol.demo.longwork.presentation.utils.TestSchedulersHolder;
import ru.dimasokol.demo.longwork.usecase.LongWorkDemoInteractor;
import ru.dimasokol.demo.longwork.usecase.WorkStep;
import ru.dimasokol.demo.longwork.usecase.WorkStepTestCreator;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class LongWorkPresenterTest {

    private LongWorkDemoInteractor mInteractor;
    private LongWorkPresenter mPresenter;
    private LongWorkView mFirstView, mSecondView;
    private WorkStep mWorkStep = WorkStepTestCreator.testWorkStep();

    @Before
    public void setUp() throws Exception {
        mInteractor = mock(LongWorkDemoInteractor.class);
        mFirstView = mock(LongWorkView.class);
        mSecondView = mock(LongWorkView.class);
        mPresenter = spy(new LongWorkPresenter(mInteractor, new TestSchedulersHolder()));
    }

    @Test
    public void attachView_error() {
        when(mInteractor.startVeryLongWork()).thenReturn(Observable.error(new RuntimeException()));
        mPresenter.attachView(mFirstView);

        verify(mFirstView).showProgress(anyInt(), eq(""), eq(-1));

        mPresenter.startLongWork();
        verify(mFirstView).showError(anyInt());
    }

    @Test
    public void attachView() {
        when(mInteractor.startVeryLongWork()).thenReturn(Observable.just(mWorkStep));
        mPresenter.attachView(mFirstView);
        verify(mFirstView).showProgress(anyInt(), eq(""), eq(-1));

        mPresenter.startLongWork();
        verify(mFirstView).showProgress(anyInt(), eq(WorkStepTestCreator.WORK_SUBJECT), eq(WorkStepTestCreator.PROGRESS));
        verify(mFirstView).onCompleted();
    }

    @Test
    public void attachViewIfNone() {
        mPresenter.attachViewIfNone(mFirstView);
        verify(mFirstView).showProgress(anyInt(), eq(""), eq(-1));

        mPresenter.attachViewIfNone(mSecondView);
        verifyZeroInteractions(mSecondView);
    }

    @Test
    public void compareAndDetachView() {
        when(mInteractor.startVeryLongWork()).thenReturn(Observable.just(mWorkStep));
        mPresenter.attachView(mFirstView);

        mPresenter.compareAndDetachView(mSecondView);
        mPresenter.startLongWork();

        verifyZeroInteractions(mSecondView);
        verify(mFirstView).showProgress(anyInt(), eq(WorkStepTestCreator.WORK_SUBJECT), eq(WorkStepTestCreator.PROGRESS));
        verify(mFirstView, never()).showError(anyInt());
    }

    @Test
    public void compareAndDetachView_matches() {
        when(mInteractor.startVeryLongWork()).thenReturn(Observable.just(mWorkStep));
        mPresenter.attachView(mFirstView);

        mPresenter.compareAndDetachView(mFirstView);
        mPresenter.startLongWork();

        // Только один вызов после первого аттача, не после работы
        verify(mFirstView).showProgress(anyInt(), eq(""), eq(-1));
        verify(mFirstView, never()).onCompleted();
    }

    @Test
    public void startLongWork() {
        when(mInteractor.startVeryLongWork()).thenReturn(Observable.never());
        mPresenter.attachView(mFirstView);

        mPresenter.startLongWork();
        mPresenter.startLongWork();
        mPresenter.startLongWork();

        verify(mFirstView).showProgress(anyInt(), eq(""), eq(-1));
    }
}