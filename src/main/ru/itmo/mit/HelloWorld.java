package ru.itmo.mit;

import ru.itmo.mit.util.HelloWorldProvider;

public class HelloWorld {
    public static void main(String[] args) {
        HelloWorldProvider provider = new HelloWorldProvider();
        System.out.println(provider.getHelloWorld());
    }
}
