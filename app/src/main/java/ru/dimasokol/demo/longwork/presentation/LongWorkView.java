package ru.dimasokol.demo.longwork.presentation;

public interface LongWorkView {

    void showProgress(int messageId, String name, int progress);

    void showError(int messageId);

    void onCompleted();
}
