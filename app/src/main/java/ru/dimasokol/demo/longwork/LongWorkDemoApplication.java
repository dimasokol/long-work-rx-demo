package ru.dimasokol.demo.longwork;

import android.app.Application;

import ru.dimasokol.demo.longwork.data.DownloadRepositoryImpl;
import ru.dimasokol.demo.longwork.data.LongWorkRepositoryImpl;
import ru.dimasokol.demo.longwork.presentation.LongWorkPresenter;
import ru.dimasokol.demo.longwork.usecase.LongWorkDemoInteractorImpl;
import ru.dimasokol.demo.longwork.utils.RealSchedulersHolder;
import ru.dimasokol.demo.longwork.utils.SchedulersHolder;

public class LongWorkDemoApplication extends Application implements PresentersHolder {

    private LongWorkPresenter mLongWorkPresenter;
    private final SchedulersHolder mSchedulersHolder = new RealSchedulersHolder();

    @Override
    public void onCreate() {
        super.onCreate();
        mLongWorkPresenter = new LongWorkPresenter(
                new LongWorkDemoInteractorImpl(
                        new DownloadRepositoryImpl(), new LongWorkRepositoryImpl()
                ), mSchedulersHolder);
    }

    @Override
    public LongWorkPresenter getLongWorkPresenter() {
        return mLongWorkPresenter;
    }
}
