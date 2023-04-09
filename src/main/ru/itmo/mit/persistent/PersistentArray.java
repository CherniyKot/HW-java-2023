package ru.itmo.mit.persistent;

import org.jetbrains.annotations.Nullable;


interface PersistentArray<T> {
    int getSize();

    int indexOf(Object elem);
    boolean contains(Object elem);

    @Nullable T get(int index);
    PersistentArray<T> set(int index, @Nullable T x);

    /*
     * Optional
     * Итератор по текущим значениям.
     * Реализация обхода ожидается без последовательных вызовов get(int)
     */
    // Iterator<T> iterator();
}
