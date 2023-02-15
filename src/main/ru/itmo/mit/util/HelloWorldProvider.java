package ru.itmo.mit.util;

// keyword implements
public class HelloWorldProvider implements AbstractHelloWorldProvider {
    private final String helloWorld = "Hello, world";

    @Override
    public String getHelloWorld() {
        return helloWorld;
    }
}
