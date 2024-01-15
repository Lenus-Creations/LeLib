package org.lenuscreations.lelib.bukkit.config;

import lombok.Data;

@Data
@Configuration("test")
public class TestConfig {

    @ConfigValue("test")
    private static String test;

}
