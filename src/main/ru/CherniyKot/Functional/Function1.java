package ru.CherniyKot.Functional;

@FunctionalInterface
public interface Function1<ReturnType, ArgType> {

    ReturnType apply(ArgType arg);

    default <RetT> Function1<RetT, ArgType> compose(Function1<RetT, ReturnType> func) {
        return (arg) -> func.apply(this.apply(arg));
    }
}
