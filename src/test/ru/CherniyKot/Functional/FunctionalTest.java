package ru.CherniyKot.Functional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("Convert2MethodRef")
public class FunctionalTest {

    @Test
    public void testFunction1Basic() {
        Function1<Integer, Integer> plusTen = arg -> arg + 10;
        Function1<String, Integer> toString = arg -> arg.toString();

        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i + 10, plusTen.apply(i));
            Assertions.assertEquals(String.valueOf(i), toString.apply(i));
        }
    }

    @Test
    public void testFunction1Compose() {
        Function1<Integer, Integer> plusTen = arg -> arg + 10;
        Function1<String, Integer> toString = arg -> arg.toString();

        Function1<String, Integer> plusTenToString = plusTen.compose(toString);
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(String.valueOf(i + 10), plusTenToString.apply(i));
        }
    }

    @Test
    public void testFunction2Basic() {
        Function2<Integer, Integer, Integer> sum = (arg1, arg2) -> arg1 + arg2;
        Function2<String, String, String> concat = (arg1, arg2) -> arg1.concat(arg2);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                Assertions.assertEquals(i + j, sum.apply(i, j));
                String is = String.valueOf(i);
                String js = String.valueOf(j);
                Assertions.assertEquals(is.concat(js), concat.apply(is, js));
            }
        }
    }

    @Test
    public void testFunction2Compose() {
        Function2<Integer, Integer, Integer> sum = (arg1, arg2) -> arg1 + arg2;
        Function1<String, Integer> toString = arg -> arg.toString();

        var sumToString = sum.compose(toString);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                String s = String.valueOf(i + j);
                Assertions.assertEquals(s, sumToString.apply(i, j));
            }
        }
    }

    @Test
    public void testFunction2Bind() {
        Function2<String, String, String> concat = (arg1, arg2) -> arg1.concat(arg2);
        var concatTenWith = concat.bind1("Ten");
        var concatWithTen = concat.bind2("Ten");

        for (int i = 0; i < 100; i++) {
            String is = String.valueOf(i);
            Assertions.assertEquals(is.concat("Ten"), concatWithTen.apply(is));
            Assertions.assertEquals("Ten".concat(is), concatTenWith.apply(is));
        }
    }

    @Test
    public void testFunction2Curry() {
        Function2<Integer, Integer, Integer> sum = (arg1, arg2) -> arg1 + arg2;
        Function2<String, String, String> concat = (arg1, arg2) -> arg1.concat(arg2);

        var sumCurry = sum.curry();
        var concatCurry = concat.curry();

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                Assertions.assertEquals(sum.apply(i, j), sumCurry.apply(i).apply(j));

                String is = String.valueOf(i);
                String js = String.valueOf(j);
                Assertions.assertEquals(concat.apply(is, js), concatCurry.apply(is).apply(js));
            }
        }
    }

    @Test
    public void testPredicateBasic() {
        Predicate<Integer> eqTen = arg -> arg == 10;
        Predicate<String> lenIsEven = arg -> arg.length() % 2 == 0;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            stringBuilder.append('a');
            Assertions.assertEquals(i == 10, eqTen.apply(i));
            Assertions.assertEquals(i % 2 == 1, lenIsEven.apply(stringBuilder.toString()));
        }
    }

    @Test
    public void testPredicateOperations() {
        Predicate<Integer> isDivisibleByNine = arg -> arg % 9 == 0;
        Predicate<Integer> isEven = arg -> arg % 2 == 0;

        var isEvenOrDivisibleByNine = isEven.or(isDivisibleByNine);
        var isDivisibleByNineOrEven = isDivisibleByNine.or(isEven);

        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i % 2 == 0 || i % 9 == 0, isEvenOrDivisibleByNine.apply(i));
            Assertions.assertEquals(i % 2 == 0 || i % 9 == 0, isDivisibleByNineOrEven.apply(i));
        }

        var isEvenAndDivisibleByNine = isEven.and(isDivisibleByNine);
        var isDivisibleByNineAndEven = isDivisibleByNine.and(isEven);

        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i % 2 == 0 && i % 9 == 0, isEvenAndDivisibleByNine.apply(i));
            Assertions.assertEquals(i % 2 == 0 && i % 9 == 0, isDivisibleByNineAndEven.apply(i));
        }

        var isOdd = isEven.not();

        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i % 2 == 1, isOdd.apply(i));
        }

        var truePredicate = new Predicate.ALWAYS_TRUE();
        var falsePredicate = new Predicate.ALWAYS_FALSE();

        Assertions.assertTrue(truePredicate.apply("I'm definitely very false"));
        Assertions.assertFalse(falsePredicate.apply(100100));
    }

    private <ReturnType> List<ReturnType> IteratorToList(Iterable<ReturnType> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    @Test
    public void testCollections() {
        var testIntList = Arrays.asList(1, 2, 3, 4, 5);
        var mapResultList = Arrays.asList(11, 12, 13, 14, 15);
        Assertions.assertEquals(mapResultList, IteratorToList(Collections.map(arg -> arg + 10, testIntList)));

        Assertions.assertEquals(testIntList.stream().filter(arg -> arg % 2 == 0).collect(Collectors.toList()), IteratorToList(Collections.filter(arg -> arg % 2 == 0, testIntList)));
        Assertions.assertEquals(testIntList.stream().takeWhile(arg -> arg < 3).collect(Collectors.toList()), IteratorToList(Collections.takeWhile(arg -> arg < 3, testIntList)));
        Assertions.assertEquals(testIntList.stream().takeWhile(arg -> !(arg < 3)).collect(Collectors.toList()), IteratorToList(Collections.takeUnless(arg -> arg < 3, testIntList)));

        var testStringList = Arrays.asList("Hello", "world", "!");
        var foldrResult = "Hello world ! Hey!";
        var foldlResult = "Hey! Hello world !";

        Assertions.assertEquals(foldrResult, Collections.foldr((arg1, arg2) -> arg1 + " " + arg2, "Hey!", testStringList));
        Assertions.assertEquals(foldlResult, Collections.foldl((arg1, arg2) -> arg1 + " " + arg2, "Hey!", testStringList));
    }
}
