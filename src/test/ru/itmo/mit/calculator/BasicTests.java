package ru.itmo.mit.calculator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.itmo.java.calculator.CalculatorImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicTests {
    private static final double delta = 0e-7;
    private final CalculatorImpl calc = new CalculatorImpl();

    @ParameterizedTest(name = "Sum [{index}] {argumentsWithNames}")
    @CsvSource({"1,2", "0,0", "-1,5", "9,10000000", "0,1"})

    public void testSum(int a, int b) {
        assertEquals(a + b, calc.sum(a, b));
    }

    @ParameterizedTest(name = "Sub [{index}] {argumentsWithNames}")
    @CsvSource({"1,2", "0,0", "-1,5", "9,10000000", "0,1"})
    public void testSub(int a, int b) {
        assertEquals(a - b, calc.sub(a, b));
    }

    @ParameterizedTest(name = "Mul [{index}] {argumentsWithNames}")
    @CsvSource({"1,2", "0,0", "-1,5", "9,10000000", "0,1"})
    public void testMul(int a, int b) {
        assertEquals(a * b, calc.mul(a, b));
    }

    @ParameterizedTest(name = "Div [{index}] {argumentsWithNames}")
    @CsvSource({"1,2", "0,0", "-1,5", "9,10000000", "0,1"})
    public void testDiv(int a, int b) {
        assertEquals((double) a / b, calc.div(a, b), delta);
    }
}
