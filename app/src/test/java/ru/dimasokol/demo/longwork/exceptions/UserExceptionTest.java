package ru.dimasokol.demo.longwork.exceptions;

import org.junit.Test;

import ru.dimasokol.demo.longwork.R;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class UserExceptionTest {

    private static final Object ARGUMENT = new Object();
    private static final int ERROR_GENERIC = R.string.error_generic;
    private static final int ERROR_LISTING = R.string.error_listing;
    private static final int ERROR_PROCESSING = R.string.error_processing;

    private static final String FILENAME = "aaa.jpg";

    @Test
    public void from() {
        UserException userException = new UserException(ERROR_GENERIC, ARGUMENT);
        assertSame(userException, UserException.from(userException));

        assertSame(ARGUMENT, userException.getMessageArgument());
        assertEquals(ERROR_GENERIC, userException.getMessageRes());

        userException = UserException.from(new NetworkException());
        assertEquals(ERROR_LISTING, userException.getMessageRes());

        userException = UserException.from(new NetworkException(FILENAME));
        assertEquals(FILENAME, userException.getMessageArgument());

        userException = UserException.from(new ProcessingException(FILENAME));
        assertEquals(FILENAME, userException.getMessageArgument());
        assertEquals(ERROR_PROCESSING, userException.getMessageRes());
    }
}