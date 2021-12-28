package net.megavex.scoreboardlibraryexample.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;

public class TestTranslator implements Translator {

    public static final String KEY = "test.translatable.key";

    @Override
    public @NotNull Key name() {
        return Key.key("scoreboard-manager", "example");
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        if (key.equals(KEY)) {
            return new MessageFormat(locale.toString(), locale);
        }
        return null;
    }
}
