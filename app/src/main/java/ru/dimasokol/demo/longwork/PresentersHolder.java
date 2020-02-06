package ru.dimasokol.demo.longwork;

import ru.dimasokol.demo.longwork.presentation.LongWorkPresenter;

/**
 * Интерфейс хранилища презентеров
 */
public interface PresentersHolder {

    LongWorkPresenter getLongWorkPresenter();

}
