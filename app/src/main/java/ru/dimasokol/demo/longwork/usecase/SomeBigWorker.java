package ru.dimasokol.demo.longwork.usecase;

import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import ru.dimasokol.demo.longwork.data.DownloadRepository;
import ru.dimasokol.demo.longwork.data.LongWorkRepository;
import ru.dimasokol.demo.longwork.exceptions.NetworkException;
import ru.dimasokol.demo.longwork.exceptions.ProcessingException;
import ru.dimasokol.demo.longwork.exceptions.UserException;

/**
 * Реализация долгой-долгой работы
 */
class SomeBigWorker implements ObservableOnSubscribe<WorkStep> {

    private final DownloadRepository mDownloadRepository;
    private final LongWorkRepository mLongWorkRepository;

    SomeBigWorker(DownloadRepository downloadRepository, LongWorkRepository longWorkRepository) {
        mDownloadRepository = downloadRepository;
        mLongWorkRepository = longWorkRepository;
    }

    @Override
    public void subscribe(ObservableEmitter<WorkStep> emitter) throws UserException {
        try {
            List<String> stubFilenames = mDownloadRepository.getFileNames();

            int totalProgress = stubFilenames.size() * 2;
            int currentProgress = 0;

            // Теперь типа скачиваем файлы
            for (String filename : stubFilenames) {
                currentProgress++;
                emitter.onNext(WorkStep.downloading(filename, relativeProgress(currentProgress, totalProgress)));
                mDownloadRepository.downloadFile(filename);
            }

            // А теперь типа обрабатываем
            for (String filename : stubFilenames) {
                currentProgress++;
                emitter.onNext(WorkStep.processing(filename, relativeProgress(currentProgress, totalProgress)));
                mLongWorkRepository.processFile(filename);
            }

        } catch (NetworkException network) {
            throw UserException.from(network);
        } catch (ProcessingException processing) {
            throw UserException.from(processing);
        } catch (Exception other) {
            throw UserException.from(other);
        }

        emitter.onComplete();

    }


    private int relativeProgress(float current, float total) {
        return Math.round((current / total) * 100f);
    }
}
