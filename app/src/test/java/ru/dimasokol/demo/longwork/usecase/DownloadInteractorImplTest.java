package ru.dimasokol.demo.longwork.usecase;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import ru.dimasokol.demo.longwork.data.DownloadRepository;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DownloadInteractorImplTest {

    static final List<String> MOCK_FILES = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m");

    private DownloadInteractorImpl mInteractor;
    private DownloadRepository mRepository;

    @Before
    public void setUp() throws Exception {
        mRepository = mock(DownloadRepository.class);
        when(mRepository.getFileNames()).thenReturn(MOCK_FILES);
        mInteractor = new DownloadInteractorImpl(mRepository);
    }

    @Test
    public void downloadFiles() throws Exception {
        mInteractor
                .downloadFiles()
                .test()
                .assertValueAt(0, MOCK_FILES.get(0))
                .assertValueAt(1, MOCK_FILES.get(1))
                .assertComplete();

        verify(mRepository, times(MOCK_FILES.size())).downloadFile(anyString());

        for (String mockFile : MOCK_FILES) {
            verify(mRepository).downloadFile(eq(mockFile));
        }
    }

    @Test
    public void getFilesCount() {
        mInteractor.getFilesCount()
                .test()
                .assertValue(MOCK_FILES.size());
    }
}