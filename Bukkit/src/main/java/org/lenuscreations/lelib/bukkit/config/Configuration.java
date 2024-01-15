package org.lenuscreations.lelib.bukkit.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

    String value() default "config";
    Type type() default Type.YAML;

    enum Type {
        YAML("yml", "yaml");

        private final String[] extensions;

        Type(String... extensions) {
            this.extensions = extensions;
        }

        public String[] getExtensions() {
            return extensions;
        }
    }

}
