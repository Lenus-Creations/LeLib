package org.lenuscreations.lelib.bukkit.gui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GUISettings {

    boolean autoUpdate() default false;

    long autoUpdateTime() default 20L;



}
