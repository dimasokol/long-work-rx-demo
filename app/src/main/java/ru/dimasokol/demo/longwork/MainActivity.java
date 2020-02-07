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
    public void renderState(ViewState state) {
        if (state.getException() != null) {
            mCompleted = true;
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressText.setText(getString(state.getException().getMessageRes(), state.getException().getMessageArgument()));
        } else {
            int messageId = R.string.app_name;

            switch (state.getStep().getStage()) {
                case STARTING_UP:
                    messageId = R.string.progress_starting;
                    break;
                case DOWNLOADING:
                    messageId = R.string.progress_downloading;
                    break;
                case PROCESSING:
                    messageId = R.string.progress_processing;
                    break;
                case COMPLETED:
                    mCompleted = true;
                    break;
            }

            mProgressBar.setIndeterminate(state.getStep().getTotalProgress() < 0);
            mProgressBar.setProgress(state.getStep().getTotalProgress());
            mProgressText.setText(getString(messageId, state.getStep().getWorkSubject(), state.getStep().getTotalProgress()));
        }
    }
}
