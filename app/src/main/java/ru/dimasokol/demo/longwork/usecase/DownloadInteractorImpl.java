package ru.dimasokol.demo.longwork.usecase;

import android.util.Log;

import java.util.List;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.BiConsumer;
import ru.dimasokol.demo.longwork.data.DownloadRepository;
import ru.dimasokol.demo.longwork.exceptions.NetworkException;
import ru.dimasokol.demo.longwork.exceptions.UserException;

public class DownloadInteractorImpl implements DownloadInteractor {
    private final DownloadRepository mDownloadRepository;

    public DownloadInteractorImpl(DownloadRepository downloadRepository) {
        mDownloadRepository = downloadRepository;
    }

    @Override
    public Flowable<String> downloadFiles() {

        final BiConsumer<List<String>, Emitter<String>> downloader = new BiConsumer<List<String>, Emitter<String>>() {
            private int mCurrent = 0;

            @Override
            public void accept(List<String> files, Emitter<String> emitter) throws Exception {
                Log.d("DownloadInteractor", "Downloading " + files.get(mCurrent));

                try {
                    mDownloadRepository.downloadFile(files.get(mCurrent));
                    emitter.onNext(files.get(mCurrent));
                } catch (NetworkException network) {
                    emitter.onError(UserException.from(network));
                }

                mCurrent++;

                if (mCurrent == files.size()) {
                    emitter.onComplete();
                }
            }
        };

        return Flowable.generate(
                mDownloadRepository::getFileNames,
                downloader
        );
    }

    @Override
    public Single<Integer> getFilesCount() {
        return Single.fromCallable(() -> mDownloadRepository.getFileNames().size());
    }
}
