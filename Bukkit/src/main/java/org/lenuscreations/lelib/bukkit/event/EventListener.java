package org.lenuscreations.lelib.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EventListener {

     Class<? extends Event> event();

     boolean ignoreCancelled() default false;

     EventPriority priority() default EventPriority.NORMAL;

}
