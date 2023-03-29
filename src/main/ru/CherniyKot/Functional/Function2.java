package ru.CherniyKot.Functional;

import java.util.Map;

@FunctionalInterface
public interface Function2<RetType, ArgType1, ArgType2> extends Function1<RetType, Map.Entry<ArgType1, ArgType2>> {
    RetType apply(ArgType1 arg1, ArgType2 arg2);

    @Override
    default RetType apply(Map.Entry<ArgType1, ArgType2> arg) {
        return apply(arg.getKey(), arg.getValue());
    }

    default <RetT> Function2<RetT, ArgType1, ArgType2> compose(Function1<RetT, RetType> func) {
        return (arg1, arg2) -> func.apply(this.apply(arg1, arg2));
    }


    default Function1<RetType, ArgType2> bind1(ArgType1 arg1) {
        return arg2 -> this.apply(arg1, arg2);
    }

    default Function1<RetType, ArgType1> bind2(ArgType2 arg2) {
        return arg1 -> this.apply(arg1, arg2);
    }

    default Function1<Function1<RetType, ArgType2>, ArgType1> curry() {
        return arg1 -> arg2 -> this.apply(arg1, arg2);
    }
}
