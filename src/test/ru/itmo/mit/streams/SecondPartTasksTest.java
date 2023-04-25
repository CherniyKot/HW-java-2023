package ru.itmo.mit.streams;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        List<String> paths = new ArrayList<>();
        try {
            File tmp1 = File.createTempFile("Quotes", null);
            tmp1.deleteOnExit();

            File tmp2 = File.createTempFile("Quotes", null);
            tmp1.deleteOnExit();

            File tmp3 = File.createTempFile("Quotes", null);
            tmp1.deleteOnExit();

            paths.add(tmp1.getAbsolutePath());
            paths.add(tmp2.getAbsolutePath());
            paths.add(tmp3.getAbsolutePath());

            try (var writer = new FileWriter(tmp1)) {
                writer.write("Hello, world!\n");
                writer.write("How are you?\n");
                writer.write("What a nice weather\n");
            }

            try (var writer = new FileWriter(tmp2)) {
                writer.write("Hola, mundo!\n");
                writer.write("Como estas?\n");
                writer.write("El clima es muy bueno\n");
            }

            try (var writer = new FileWriter(tmp3)) {
                writer.write("Sorry, I don't speak spanish.\n");
                writer.write("So, I'l just say \"Hello, world!\"\n");
            }

            assertEquals(List.of("Hello, world!", "So, I'l just say \"Hello, world!\""), SecondPartTasks.findQuotes(paths, "world"));
            assertEquals(List.of("Como estas?", "El clima es muy bueno"), SecondPartTasks.findQuotes(paths, "es"));
            assertEquals(List.of(), SecondPartTasks.findQuotes(paths, "!!!"));


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            for (var file : paths) {
                //noinspection ResultOfMethodCallIgnored
                new File(file).delete();  //not really working for some reason and neither is "deleteOnExit" as far as I'm concerned
            }
        }
    }

    @Test
    public void testPiDividedBy4() {
        assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), 0.01);
    }

    @Test
    public void testFindPrinter() {
        var e1 = new AbstractMap.SimpleEntry<>("A1", List.of("abc", "123", "q", "")); //7
        var e2 = new AbstractMap.SimpleEntry<>("A2", List.of("abcdefg")); //7
        var e3 = new AbstractMap.SimpleEntry<>("A3", List.of("", "", "1", "2", "3", "4", "")); //4
        var e4 = new AbstractMap.SimpleEntry<>("A4", List.of("12345678")); //8

        assertEquals("A4", SecondPartTasks.findPrinter(Map.ofEntries(e1, e2, e3, e4)));

        var n2 = SecondPartTasks.findPrinter(Map.ofEntries(e1, e2, e3));
        assertTrue(n2.equals("A1") || n2.equals("A2"));

        assertEquals("", SecondPartTasks.findPrinter(Map.ofEntries()));
        assertEquals("A3", SecondPartTasks.findPrinter(Map.ofEntries(e3)));
    }

    @Test
    public void testCalculateGlobalOrder() {
        var o1 = Map.of("1", 1, "2", 1);
        var o2 = Map.of("1", 1, "3", 1);
        var o3 = Map.of("1", 10, "2", 3);
        var o4 = Map.of("1", 1, "2", 1, "4", 1);
        var o5 = Map.of("2", 1);


        assertEquals(Map.of("1", 13, "2", 6, "3", 1, "4", 1), SecondPartTasks.calculateGlobalOrder(List.of(o1, o2, o3, o4, o5)));
        assertEquals(Map.of("2", 1), SecondPartTasks.calculateGlobalOrder(List.of(o5)));
        assertEquals(Map.of("1", 1, "2", 2, "4", 1), SecondPartTasks.calculateGlobalOrder(List.of(o4, o5)));
        assertEquals(Map.of(), SecondPartTasks.calculateGlobalOrder(List.of()));
    }
}