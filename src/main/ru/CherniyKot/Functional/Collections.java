package ru.CherniyKot.Functional;

import java.util.ArrayList;
import java.util.Iterator;

public class Collections {
    static <ReturnType, ArgType> ArrayList<ReturnType> map(Function1<ReturnType, ArgType> func, Iterable<ArgType> iterable) {
        ArrayList<ReturnType> result = new ArrayList<>();
        for (ArgType arg : iterable) {
            result.add(func.apply(arg));
        }
        return result;
    }

    static <ArgType> ArrayList<ArgType> filter(Predicate<ArgType> pred, Iterable<ArgType> iterable) {
        ArrayList<ArgType> result = new ArrayList<>();
        for (ArgType arg : iterable) {
            if (pred.apply(arg)) {
                result.add(arg);
            }
        }
        return result;
    }

    static <ArgType> ArrayList<ArgType> takeWhile(Predicate<ArgType> pred, Iterable<ArgType> iterable) {
        ArrayList<ArgType> result = new ArrayList<>();
        for (ArgType arg : iterable) {
            if (!pred.apply(arg)) {
                break;
            }
            result.add(arg);
        }
        return result;
    }

    static <ArgType> ArrayList<ArgType> takeUnless(Predicate<ArgType> pred, Iterable<ArgType> iterable) {
        ArrayList<ArgType> result = new ArrayList<>();
        for (ArgType arg : iterable) {
            if (pred.apply(arg)) {
                break;
            }
            result.add(arg);
        }
        return result;
    }

    static <ReturnType, ArgType> ReturnType foldl(Function2<ReturnType, ReturnType, ArgType> func, ReturnType init, Iterable<ArgType> iterable){
        ReturnType result = init;
        for (ArgType arg: iterable) {
            result = func.apply(result, arg);
        }
        return result;
    }

    static <ReturnType, ArgType> ReturnType foldr(Function2<ReturnType, ArgType, ReturnType> func, ReturnType init, Iterable<ArgType> iterable){
        return _foldr(func, init, iterable.iterator());
    }

    private static <ReturnType, ArgType> ReturnType _foldr(Function2<ReturnType, ArgType, ReturnType> func, ReturnType init, Iterator<ArgType> iterator){
        if(!iterator.hasNext()){
            return init;
        }
        return func.apply(iterator.next(), _foldr(func, init, iterator));
    }
}
