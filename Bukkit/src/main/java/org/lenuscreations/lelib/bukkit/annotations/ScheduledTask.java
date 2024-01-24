package org.lenuscreations.lelib.bukkit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledTask {

    long delay() default 0L;

    long interval() default 0L;

    boolean async() default false;

}
