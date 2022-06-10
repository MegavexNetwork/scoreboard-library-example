package net.megavex.scoreboardlibraryexample;

import net.kyori.adventure.translation.GlobalTranslator;
import net.megavex.scoreboardlibrary.ScoreboardLibraryImplementation;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.ScoreboardManager;
import net.megavex.scoreboardlibrary.exception.ScoreboardLibraryLoadException;
import net.megavex.scoreboardlibraryexample.util.TestTranslator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ExamplePlugin extends JavaPlugin implements Listener {

    private final TestTranslator translator = new TestTranslator();
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        System.setProperty(ScoreboardLibrary.NAMESPACE + ".debug", "true"); // Enable debug output

        try {
            ScoreboardLibraryImplementation.init();
        } catch (ScoreboardLibraryLoadException e) {
            getLogger().log(Level.SEVERE, "Couldn't load ScoreboardLibrary", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        GlobalTranslator.translator().addSource(translator);
        scoreboardManager = ScoreboardManager.scoreboardManager(this);
        new TeamsExample(this);
        new AbstractSidebarExample(this);
    }

    @Override
    public void onDisable() {
        GlobalTranslator.translator().removeSource(translator);
        if (scoreboardManager != null) scoreboardManager.close();
        ScoreboardLibraryImplementation.close();
    }

    public ScoreboardManager scoreboardManager() {
        return scoreboardManager;
    }
}
