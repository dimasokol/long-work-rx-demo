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

    public static UserException from(Throwable t) {
        if (t instanceof UserException) {
            return (UserException) t;
        }

        if (t instanceof NetworkException) {
            String filename = ((NetworkException) t).getFileName();
            return new UserException(filename != null? R.string.error_downloading : R.string.error_listing, filename);
        }

        if (t instanceof ProcessingException) {
            return new UserException(R.string.error_processing, ((ProcessingException) t).getFileName());
        }

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
