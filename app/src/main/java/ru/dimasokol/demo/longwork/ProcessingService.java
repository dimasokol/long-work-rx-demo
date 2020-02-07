package ru.dimasokol.demo.longwork;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ru.dimasokol.demo.longwork.presentation.LongWorkPresenter;
import ru.dimasokol.demo.longwork.presentation.LongWorkView;

/**
 * Сервис, держащий приложение живым на момент ухода с экрана активити
 */
public class ProcessingService extends Service implements LongWorkView {

    public static final int NOTIFICATION_ID = R.string.service_title;

    private LongWorkPresenter mPresenter;
    private Notification mNotification;

    public ProcessingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = ((PresentersHolder) getApplication()).getLongWorkPresenter();

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.service_channel_id),
                    getString(R.string.service_channel_title), NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }

        mNotification = createNotification(R.string.progress_starting, "", -1);

        startForeground(NOTIFICATION_ID, mNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPresenter.attachViewIfNone(this);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.compareAndDetachView(this);
        mPresenter = null;
    }

    @Override
    public void showProgress(int messageId, String name, int progress) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        mNotification = createNotification(messageId, name, progress);

        manager.notify(NOTIFICATION_ID, mNotification);
    }

    @Override
    public void showError(int messageId, String argument) {
        // Можно показать оповещение
    }

    @Override
    public void onCompleted() {
        stopSelf();
    }

    private Notification createNotification(int messageId, String name, int progress) {
        return new NotificationCompat.Builder(this, getString(R.string.service_channel_id))
                .setContentTitle(getString(R.string.service_title))
                .setContentText(getString(messageId, name, progress))
                .setProgress(100, progress, progress < 0)
                .setSmallIcon(R.drawable.ic_ongoing_24dp)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(PendingIntent.getActivity(this, 0,
                        new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
    }
}
