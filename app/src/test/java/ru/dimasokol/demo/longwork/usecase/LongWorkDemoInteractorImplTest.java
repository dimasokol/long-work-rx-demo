package ru.dimasokol.demo.longwork.usecase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Arrays;
import java.util.List;

import ru.dimasokol.demo.longwork.data.DownloadRepository;
import ru.dimasokol.demo.longwork.data.LongWorkRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LongWorkDemoInteractorImplTest {

    private static final List<String> MOCK_FILES = Arrays.asList("a", "b", "c");

    private LongWorkDemoInteractorImpl mInteractor;
    private DownloadRepository mDownloadRepository;
    private LongWorkRepository mLongWorkRepository;

    @Before
    public void setUp() throws Exception {
        mDownloadRepository = mock(DownloadRepository.class);
        mLongWorkRepository = mock(LongWorkRepository.class);

        when(mDownloadRepository.getFileNames()).thenReturn(MOCK_FILES);

        mInteractor = new LongWorkDemoInteractorImpl(mDownloadRepository, mLongWorkRepository);
    }

    @Test
    public void startVeryLongWork() throws Exception {
        mInteractor
                .startVeryLongWork()
                .test()
                .assertComplete();

        verify(mDownloadRepository).getFileNames();
        verify(mDownloadRepository, times(MOCK_FILES.size())).downloadFile(ArgumentMatchers.anyString());
        verify(mLongWorkRepository, times(MOCK_FILES.size())).processFile(ArgumentMatchers.anyString());
    }
}