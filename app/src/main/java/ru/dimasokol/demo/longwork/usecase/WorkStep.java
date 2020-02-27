package ru.dimasokol.demo.longwork.usecase;

import androidx.annotation.NonNull;

/**
 * Шаг выполнения работы. Immutable-объект, хранящий в себе информацию о текущем этапе обработки,
 * имени обрабатываемого сейчас объекта и прогрессе по шкале 0–100.
 */
public final class WorkStep {

    public static final WorkStep NOT_STARTED = new WorkStep(Stage.NOT_STARTED);
    public static final WorkStep COMPLETED = new WorkStep(Stage.COMPLETED);

    private static final int PROGRESS_UNDEFINED = -1;

    @NonNull
    private final Stage mStage;
    @NonNull
    private final String mWorkSubject;
    private final int mTotalProgress;

    static WorkStep downloading(@NonNull String filename, int progress) {
        return new WorkStep(Stage.DOWNLOADING, filename, progress);
    }

    static WorkStep processing(@NonNull String filename, int progress) {
        return new WorkStep(Stage.PROCESSING, filename, progress);
    }

    WorkStep(@NonNull Stage stage, @NonNull String workSubject, int totalProgress) {
        mStage = stage;
        mWorkSubject = workSubject;
        mTotalProgress = totalProgress;
    }

    WorkStep(@NonNull Stage stage) {
        this(stage, "", PROGRESS_UNDEFINED);
    }

    /**
     * Состояние работы в данный момент.
     *
     * @return Состояние работы, не {@code null}
     */
    @NonNull
    public Stage getStage() {
        return mStage;
    }

    /**
     * Обрабатываемый сейчас объект.
     *
     * @return Имя обрабатываемого сейчас объекта, или пустая строка, если этап работы не касается
     * никаких конкретных объектов.
     */
    @NonNull
    public String getWorkSubject() {
        return mWorkSubject;
    }

    /**
     * Прогресс выполнения раюоты в целом
     *
     * @return Прогресс выполнения работы целиком, по шкале 0–100
     */
    public int getTotalProgress() {
        return mTotalProgress;
    }

    /**
     * Этап обработки
     */
    public enum Stage {
        /**
         * Ещё не стартовали
         */
        NOT_STARTED,
        /**
         * Стартуем, первичная инициализация, определение объёма работ
         */
        STARTING_UP,
        /**
         * Загрузка файлов
         */
        DOWNLOADING,
        /**
         * Обработка файлов
         */
        PROCESSING,
        /**
         * Процесс завершён, с ошибкой или без
         */
        COMPLETED
    }

}
