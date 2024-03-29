package org.lenuscreations.lelib.bukkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Plugin {

    String name();
    String version();
    String[] authors();
    String description() default "";
    String website() default "";

    String generatedClassName() default "Generated";

    String[] depend() default {};
    String[] softDepend() default {};

}
