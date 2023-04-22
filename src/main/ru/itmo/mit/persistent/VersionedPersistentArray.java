package ru.itmo.mit.persistent;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface VersionedPersistentArray<T> extends PersistentArray<T> {
    @Override
    VersionedPersistentArray<T> set(int index, @Nullable T x);

    /*
     * Итератор по предыдущим состояниям.
     * Обход от текущего состояния по предыдущим.
     * Предыдущее - то, которое было до вызова изменения.
     * Не поддерживает add/remove.
     */
    Iterator<VersionedPersistentArray<T>> versionsIterator();

    /*
     * Возвращает состояние массива, отличное от текущего на k шагов.
     * Модификация никак не меняет версии исходного.
     */
    VersionedPersistentArray<T> getPreviousVersion(int k);
}
