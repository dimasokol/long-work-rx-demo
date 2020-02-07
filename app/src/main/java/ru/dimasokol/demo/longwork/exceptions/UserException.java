package ru.dimasokol.demo.longwork.exceptions;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import ru.dimasokol.demo.longwork.R;

/**
 * Пользовательское исключение, с указанием ресурса строки, плюс с возможностью добавить аргумент
 */
public class UserException extends Exception {

    @StringRes
    private final int mMessageRes;
    @Nullable
    private final Object mMessageArgument;


    public static UserException from(NetworkException e) {
        String filename = e.getFileName();
        return new UserException(filename != null? R.string.error_downloading : R.string.error_listing, filename);
    }

    public static UserException from(ProcessingException e) {
        return new UserException(R.string.error_processing, e.getFileName());
    }

    public static UserException from(Exception e) {
        return new UserException(R.string.error_generic, null);
    }

    public UserException(@StringRes int messageRes, @Nullable Object messageArgument) {
        mMessageRes = messageRes;
        mMessageArgument = messageArgument;
    }

    @StringRes
    public int getMessageRes() {
        return mMessageRes;
    }

    @Nullable
    public Object getMessageArgument() {
        return mMessageArgument;
    }
}
