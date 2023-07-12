package org.lenuscreations.lelib.bukkit.gui;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GUI {

    String title();

    int size() default 9 * 3;

    InventoryType type() default InventoryType.CHEST;

}
