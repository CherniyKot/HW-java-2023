package ru.itmo.mit.streams;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) throws IOException {
        return paths.stream().map(name -> {
            try {
                return new BufferedReader(new FileReader(name));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).flatMap(BufferedReader::lines).filter(x -> x.contains(sequence)).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        return piDividedBy4(10000);
    }

    public static double piDividedBy4(int n) {
        // not really sure if there is a way to do it in one line without two calls to nextDouble()
        var r = new Random();
        return (double) new Random().doubles(n)
                .mapToObj(x -> new AbstractMap.SimpleEntry<>(x, r.nextDouble()))
                .filter(x -> Math.hypot(x.getKey(), x.getValue()) <= 1d).count() / n;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream()
                .map(x -> new AbstractMap.SimpleEntry<>(x.getKey(), x.getValue().stream().mapToInt(String::length).sum()))
                .max(Map.Entry.comparingByValue()).map(AbstractMap.SimpleEntry::getKey).orElse("");
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap(x -> x.entrySet().stream()).collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
    }
}