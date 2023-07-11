package org.lenuscreations.lelib.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Command {

    String name();

    String[] aliases() default {};

    String description() default "";

    boolean playerOnly() default false;

    boolean consoleOnly() default false;

    String permission() default "";

}
