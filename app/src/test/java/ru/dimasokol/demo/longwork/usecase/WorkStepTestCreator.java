package ru.dimasokol.demo.longwork.usecase;

/**
 * Создатель экземпляров {@link WorkStep} для нужд тестирования.
 *
 * <p>
 *     Здесь применена уловка: сам класс {@link WorkStep} имеет закрытые от внешнего
 *     мира конструкторы, т.е. создавать его в тестах не получится просто так. Потому
 *     здесь заведён специальный класс, лежащий в том же пакете. Он видит package-local,
 *     соответственно — может создать экземпляр.
 * </p>
 */
public final class WorkStepTestCreator {

    /**
     * Тестовое значение обрабатываемого файла
     */
    public static final String WORK_SUBJECT = "test.jpg";

    /**
     * Тестовое значение текущего прогресса
     */
    public static final int PROGRESS = 42;

    /**
     * Создаёт тестовый экземпляр класса
     *
     * @return Тестовый экземпляр, с полями {@link #WORK_SUBJECT} и {@link #PROGRESS}
     */
    public static WorkStep testWorkStep() {
        return new WorkStep(WorkStep.Stage.DOWNLOADING, WORK_SUBJECT, PROGRESS);
    }

}
