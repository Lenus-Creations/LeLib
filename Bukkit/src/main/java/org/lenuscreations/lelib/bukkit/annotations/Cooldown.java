package org.lenuscreations.lelib.bukkit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cooldown {

    long value();

    String bypassPermission() default "";

    String message() default "&cYou must wait %s before using this again!";

}
