package org.lenuscreations.lelib.bukkit.gui.old;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GUI {

    String title();

    int size() default 9 * 3;

    InventoryType type() default InventoryType.CHEST;

    boolean autoUpdate() default false;

    long autoUpdateTime() default 20L;

}
