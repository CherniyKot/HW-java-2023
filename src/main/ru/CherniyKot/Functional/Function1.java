package ru.CherniyKot.Functional;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Function1<ReturnType, ArgType> {

    ReturnType apply(ArgType arg);

    @NotNull
    default <RetT> Function1<RetT, ArgType> compose(@NotNull Function1<RetT, ? super ReturnType> func) {
        return arg -> func.apply(this.apply(arg));
    }
}
