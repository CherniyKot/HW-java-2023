package ru.CherniyKot.Functional;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("SameParameterValue")
public class Collections {
    @NotNull
    static <ReturnType, ArgType> Iterable<ReturnType> map(@NotNull Function1<ReturnType, ? super ArgType> func, @NotNull Iterable<ArgType> iterable) {
        return new Iterable<>() {
            private final Iterator<ArgType> internalIterator = iterable.iterator();

            @NotNull
            @Override
            public Iterator<ReturnType> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return internalIterator.hasNext();
                    }

                    @Override
                    public ReturnType next() {
                        return func.apply(internalIterator.next());
                    }
                };
            }
        };
    }

    @NotNull
    static <ArgType> Iterable<ArgType> filter(@NotNull Predicate<? super ArgType> pred, @NotNull Iterable<ArgType> iterable) {
        return new Iterable<>() {
            private final Iterator<ArgType> internalIterator = iterable.iterator();

            @NotNull
            @Override
            public Iterator<ArgType> iterator() {
                return new Iterator<>() {
                    private ArgType next;

                    {
                        while (internalIterator.hasNext()) {
                            ArgType curr = internalIterator.next();
                            if (pred.apply(curr)) {
                                next = curr;
                                break;
                            }
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public ArgType next() {
                        if (next == null) {
                            throw new NoSuchElementException();
                        }
                        ArgType curr = next;
                        next = null;
                        while (internalIterator.hasNext()) {
                            ArgType t = internalIterator.next();
                            if (pred.apply(t)) {
                                next = t;
                                break;
                            }
                        }
                        return curr;
                    }
                };
            }
        };
    }

    @NotNull
    static <ArgType> Iterable<ArgType> takeWhile(@NotNull Predicate<? super ArgType> pred, @NotNull Iterable<ArgType> iterable) {
        return new Iterable<>() {
            private final Iterator<ArgType> internalIterator = iterable.iterator();

            @NotNull
            @Override
            public Iterator<ArgType> iterator() {
                return new Iterator<>() {
                    private ArgType next;

                    {
                        if (internalIterator.hasNext()) {
                            ArgType curr = internalIterator.next();
                            if (pred.apply(curr)) {
                                next = curr;
                            }
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public ArgType next() {
                        if (next == null) {
                            throw new NoSuchElementException();
                        }
                        ArgType curr = next;
                        if (internalIterator.hasNext()) {
                            next = internalIterator.next();
                            if (!pred.apply(next)) {
                                next = null;
                            }
                        } else {
                            next = null;
                        }
                        return curr;
                    }
                };
            }
        };
    }

    @NotNull
    static <ArgType> Iterable<ArgType> takeUnless(@NotNull Predicate<? super ArgType> pred, @NotNull Iterable<ArgType> iterable) {
        return new Iterable<>() {
            private final Iterator<ArgType> internalIterator = iterable.iterator();

            @NotNull
            @Override
            public Iterator<ArgType> iterator() {
                return new Iterator<>() {
                    private ArgType next;

                    {
                        if (internalIterator.hasNext()) {
                            ArgType curr = internalIterator.next();
                            if (!pred.apply(curr)) {
                                next = curr;
                            }
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public ArgType next() {
                        if (next == null) {
                            throw new NoSuchElementException();
                        }
                        ArgType curr = next;
                        if (internalIterator.hasNext()) {
                            next = internalIterator.next();
                            if (pred.apply(next)) {
                                next = null;
                            }
                        } else {
                            next = null;
                        }
                        return curr;
                    }
                };
            }
        };
    }

    @NotNull
    static <ReturnType, ArgType> ReturnType foldl(@NotNull Function2<? extends ReturnType, ? super ReturnType, ? super ArgType> func,
                                                  ReturnType init, @NotNull Iterable<ArgType> iterable) {
        ReturnType result = init;
        for (ArgType arg : iterable) {
            result = func.apply(result, arg);
        }
        return result;
    }

    static <ReturnType, ArgType> ReturnType foldr(@NotNull Function2<? extends ReturnType, ? super ArgType, ? super ReturnType> func,
                                                  ReturnType init, @NotNull Iterable<ArgType> iterable) {
        return _foldr(func, init, iterable.iterator());
    }

    private static <ReturnType, ArgType> ReturnType _foldr(@NotNull Function2<? extends ReturnType, ? super ArgType, ? super ReturnType> func,
                                                           ReturnType init, @NotNull Iterator<ArgType> iterator) {
        if (!iterator.hasNext()) {
            return init;
        }
        return func.apply(iterator.next(), _foldr(func, init, iterator));
    }
}
