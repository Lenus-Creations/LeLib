package org.lenuscreations.lelib.bukkit.language;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * Untested
 */
public class LanguageHandler {

    private final List<Language> registeredLanguages;

    public LanguageHandler() {
        this.registeredLanguages = new ArrayList<>();
    }

    @Nullable
    public Language getLanguage(String name) {
        return registeredLanguages.stream()
                .filter(language -> language.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public Language getLanguageByEnglishName(String name) {
        return registeredLanguages.stream()
                .filter(language -> language.getEnglishName() != null && language.getEnglishName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void registerLanguage(Language language) {
        registeredLanguages.add(language);
    }

    public void unregisterLanguage(Language language) {
        registeredLanguages.removeIf(lang -> lang.getName().equalsIgnoreCase(language.getName()));
    }

    public void unregisterLanguage(String name) {
        registeredLanguages.removeIf(lang -> lang.getName().equalsIgnoreCase(name));
    }

    @Nullable
    public String get(Language language, String key) {
        return get(language, key, null);
    }

    @Nullable
    public String get(Language language, String key, @Nullable String defaultValue) {
        File file = new File(AbstractPlugin.getInstance().getDataFolder(), "lang/" + language.fileName() + ".yml");
        if (!file.exists()) throw new RuntimeException("Language file " + language.fileName() + " does not exist.");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.contains(key)) return defaultValue;

        return config.getString(key);
    }

}
