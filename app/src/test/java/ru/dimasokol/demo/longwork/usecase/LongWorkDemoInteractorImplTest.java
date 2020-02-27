package ru.dimasokol.demo.longwork.usecase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.dimasokol.demo.longwork.data.LongWorkRepository;
import ru.dimasokol.demo.longwork.presentation.utils.TestSchedulersHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.dimasokol.demo.longwork.usecase.DownloadInteractorImplTest.MOCK_FILES;

public class LongWorkDemoInteractorImplTest {

    private LongWorkDemoInteractorImpl mInteractor;
    private LongWorkRepository mLongWorkRepository;
    private DownloadInteractor mDownloadInteractor;

    @Before
    public void setUp() throws Exception {
        mLongWorkRepository = mock(LongWorkRepository.class);
        mDownloadInteractor = mock(DownloadInteractor.class);
        when(mDownloadInteractor.downloadFiles()).thenReturn(Flowable.fromIterable(MOCK_FILES));
        when(mDownloadInteractor.getFilesCount()).thenReturn(Single.just(MOCK_FILES.size()));

        mInteractor = new LongWorkDemoInteractorImpl(mDownloadInteractor, mLongWorkRepository, new TestSchedulersHolder());
    }

    @Test
    public void startVeryLongWork() throws Exception {
        mInteractor
                .startVeryLongWork()
                .test()
                .assertComplete();

        verify(mLongWorkRepository, times(MOCK_FILES.size())).processFile(ArgumentMatchers.anyString());
    }
}