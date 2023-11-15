package org.lenuscreations.lelib.bukkit.gui.old;

import org.bukkit.event.inventory.ClickType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickListener {

    String itemName() default "";

    int[] slots() default {};

    ClickType[] clickTypes() default {};

}
