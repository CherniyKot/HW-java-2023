package ru.CherniyKot.Functional;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Predicate<ArgType> extends Function1<Boolean, ArgType> {
    @NotNull
    default Predicate<ArgType> or(@NotNull Predicate<? super ArgType> predicate) {
        return arg -> this.apply(arg) || predicate.apply(arg);
    }

    @NotNull
    default Predicate<ArgType> and(@NotNull Predicate<? super ArgType> predicate) {
        return arg -> this.apply(arg) && predicate.apply(arg);
    }

    @NotNull
    default Predicate<ArgType> not() {
        return arg -> !this.apply(arg);
    }

    class ALWAYS_TRUE implements Predicate<Object> {
        @Override
        public Boolean apply(Object arg) {
            return true;
        }
    }

    class ALWAYS_FALSE implements Predicate<Object> {
        @Override
        public Boolean apply(Object arg) {
            return false;
        }
    }
}
