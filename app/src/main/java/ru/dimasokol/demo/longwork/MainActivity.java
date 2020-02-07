package ru.dimasokol.demo.longwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.dimasokol.demo.longwork.presentation.LongWorkPresenter;
import ru.dimasokol.demo.longwork.presentation.LongWorkView;

public class MainActivity extends AppCompatActivity implements LongWorkView {

    private TextView mProgressText;
    private ProgressBar mProgressBar;

    private LongWorkPresenter mPresenter;

    // Вспомогательный флаг, по которому мы определяем необходимость запускать сервис
    private boolean mCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressText = findViewById(R.id.progress_text);

        mPresenter = ((PresentersHolder) getApplication()).getLongWorkPresenter();

        if (savedInstanceState == null) {
            mPresenter.startLongWork();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attachView(this);

        Intent runningService = new Intent(this, ProcessingService.class);
        stopService(runningService);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.compareAndDetachView(this);

        if (!mCompleted) {
            Intent goingToService = new Intent(this, ProcessingService.class);
            // startForegroundService не нужен: приложение у нас ещё не считается ушедшим в фон
            startService(goingToService);
        }
    }

    @Override
    public void showProgress(int messageId, String name, int progress) {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressText.setText(getString(messageId, name, progress));
        mProgressBar.setIndeterminate(progress < 0);
        mProgressBar.setProgress(progress);
    }

    @Override
    public void showError(int messageId, String argument) {
        mProgressText.setText(getString(messageId, argument));
        mProgressBar.setVisibility(View.INVISIBLE);
        mCompleted = true;
    }

    @Override
    public void onCompleted() {
        mCompleted = true;
        mProgressText.setText(R.string.progress_completed);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
