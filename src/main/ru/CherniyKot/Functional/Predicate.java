package ru.CherniyKot.Functional;

@FunctionalInterface
public interface Predicate<ArgType> extends Function1<Boolean, ArgType> {
    default Predicate<ArgType> or(Predicate<ArgType> predicate) {
        return arg -> this.apply(arg) || predicate.apply(arg);
    }

    default Predicate<ArgType> and(Predicate<ArgType> predicate) {
        return arg -> this.apply(arg) && predicate.apply(arg);
    }

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
