package ru.CherniyKot.Functional;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Collections {
    static <ReturnType, ArgType> Iterable<ReturnType> map(Function1<ReturnType, ArgType> func, Iterable<ArgType> iterable) {
        return new Iterable<>() {
            final Iterator<ArgType> internalIterator = iterable.iterator();

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

    static <ArgType> Iterable<ArgType> filter(Predicate<ArgType> pred, Iterable<ArgType> iterable) {
        return new Iterable<>() {
            final Iterator<ArgType> internalIterator = iterable.iterator();

            @NotNull
            @Override
            public Iterator<ArgType> iterator() {
                return new Iterator<>() {
                    ArgType next;

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

    static <ArgType> Iterable<ArgType> takeWhile(Predicate<ArgType> pred, Iterable<ArgType> iterable) {
        return new Iterable<>() {
            final Iterator<ArgType> internalIterator = iterable.iterator();

            @NotNull
            @Override
            public Iterator<ArgType> iterator() {
                return new Iterator<>() {
                    ArgType next;

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

    static <ArgType> Iterable<ArgType> takeUnless(Predicate<ArgType> pred, Iterable<ArgType> iterable) {
        return new Iterable<>() {
            final Iterator<ArgType> internalIterator = iterable.iterator();

            @NotNull
            @Override
            public Iterator<ArgType> iterator() {
                return new Iterator<>() {
                    ArgType next;

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

    static <ReturnType, ArgType> ReturnType foldl(Function2<ReturnType, ReturnType, ArgType> func, ReturnType init, Iterable<ArgType> iterable) {
        ReturnType result = init;
        for (ArgType arg : iterable) {
            result = func.apply(result, arg);
        }
        return result;
    }

    static <ReturnType, ArgType> ReturnType foldr(Function2<ReturnType, ArgType, ReturnType> func, ReturnType init, Iterable<ArgType> iterable) {
        return _foldr(func, init, iterable.iterator());
    }

    private static <ReturnType, ArgType> ReturnType _foldr(Function2<ReturnType, ArgType, ReturnType> func, ReturnType init, Iterator<ArgType> iterator) {
        if (!iterator.hasNext()) {
            return init;
        }
        return func.apply(iterator.next(), _foldr(func, init, iterator));
    }
}
