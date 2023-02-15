package ru.itmo.mit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itmo.mit.util.HelloWorldProvider;

public class HelloWorldTest {
    @Test
    public void testProvider() {
        HelloWorldProvider helloWorldProvider = new HelloWorldProvider();
        Assertions.assertEquals("Hello, world!", helloWorldProvider.getHelloWorld());
    }

    @Test
    public void testProviderOther() {
        HelloWorldProvider helloWorldProvider = new HelloWorldProvider();
        Assertions.assertEquals("Hello, world", helloWorldProvider.getHelloWorld());
    }
}
