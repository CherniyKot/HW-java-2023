package ru.itmo.mit.persistent;

import java.util.Iterator;

public interface VersionedPersistentArray<T> extends PersistentArray<T> {
    /*
     * Итератор по предыдущим состояниям.
     * Обход от текущего состояния по предыдущим.
     * Предыдущее - то, которое было до вызова изменения.
     * Не поддерживает add/remove.
     */
    Iterator<PersistentArray<T>> versionsIterator();

    /*
     * Возвращает состояние массива, отличное от текущего на k шагов.
     * Модификация никак не меняет версии исходного.
     */
    PersistentArray<T> getPreviousVersion(int k);
}
