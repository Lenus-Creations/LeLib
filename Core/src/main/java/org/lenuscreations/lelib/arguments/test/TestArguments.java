package org.lenuscreations.lelib.arguments.test;

import org.lenuscreations.lelib.arguments.ArgValue;
import org.lenuscreations.lelib.arguments.Argument;

public class TestArguments {

    @Argument(name = "test", description = "Test argument")
    public static void test() {
        System.out.println("Test");
    }

    @Argument(name = "hello", values = {
            @ArgValue(name = "hi", type = String.class)
    })
    public static void hello(String hi) {
        System.out.println("fkjsdkgjdf " + hi);
    }

}
