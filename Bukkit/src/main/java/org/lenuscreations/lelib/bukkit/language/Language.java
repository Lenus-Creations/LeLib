package org.lenuscreations.lelib.bukkit.language;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/*
 * Untested
 */
public interface Language {

    @NotNull
    String fileName();

    @NotNull
    String getName();

    @Nullable
    default String getEnglishName() {
        return null;
    }

}
