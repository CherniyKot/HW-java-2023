package ru.itmo.java.calculator;

public class CalculatorImpl implements Calculator {
    @Override
    public int sum(int x, int y) {
        return x + y;
    }

    @Override
    public int sub(int x, int y) {
        return x - y;
    }

    @Override
    public int mul(int x, int y) {
        return x * y;
    }

    @Override
    public double div(int x, int y) {
        return (double) x / y;
    }
}
