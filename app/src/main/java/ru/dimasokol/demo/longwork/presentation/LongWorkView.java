package ru.dimasokol.demo.longwork.presentation;

import ru.dimasokol.demo.longwork.exceptions.UserException;
import ru.dimasokol.demo.longwork.usecase.WorkStep;

public interface LongWorkView {

    void renderState(ViewState state);

    class ViewState {
        private final WorkStep mStep;
        private final UserException mException;

        public ViewState(WorkStep step, UserException exception) {
            mStep = step;
            mException = exception;
        }

        public WorkStep getStep() {
            return mStep;
        }

        public UserException getException() {
            return mException;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ViewState viewState = (ViewState) o;

            if (!mStep.equals(viewState.mStep)) return false;
            return mException != null ? mException.equals(viewState.mException) : viewState.mException == null;
        }

        @Override
        public int hashCode() {
            int result = mStep.hashCode();
            result = 31 * result + (mException != null ? mException.hashCode() : 0);
            return result;
        }
    }
}
