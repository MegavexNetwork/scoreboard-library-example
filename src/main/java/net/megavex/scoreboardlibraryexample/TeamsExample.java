package net.megavex.scoreboardlibraryexample;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.megavex.scoreboardlibrary.api.team.ScoreboardTeam;
import net.megavex.scoreboardlibrary.api.team.TeamInfo;
import net.megavex.scoreboardlibrary.api.team.TeamManager;
import net.megavex.scoreboardlibraryexample.util.ColorUtil;
import net.megavex.scoreboardlibraryexample.util.TestTranslator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static net.kyori.adventure.text.Component.*;

public final class TeamsExample implements Listener {

    private final ExamplePlugin plugin;
    private final TeamManager teamManager;
    private final TeamInfo globalInfo;

    public TeamsExample(ExamplePlugin plugin) {
        this.plugin = plugin;

        teamManager = plugin.scoreboardManager().teamManager();
        ScoreboardTeam team = teamManager.createIfAbsent("epic");
        globalInfo = team.globalInfo();
        globalInfo.displayName(text("Epic Team"));
        globalInfo.prefix(text("[Epic Prefix] ").color(NamedTextColor.AQUA)); // Can also use custom colors, those will be converted to the nearest one for 1.8
        globalInfo.suffix(space().append(translatable(TestTranslator.KEY)));
        globalInfo.playerColor(NamedTextColor.RED);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Do random stuff
        new BukkitRunnable() {
            @Override
            public void run() {
                State state = globalInfo.prefix().decoration(TextDecoration.BOLD);
                Component newPrefix = globalInfo.prefix().decoration(TextDecoration.BOLD, state == State.TRUE ? State.FALSE : State.TRUE);
                globalInfo.prefix(newPrefix);

                globalInfo.suffix(globalInfo.suffix().color(ColorUtil.randomNamedColor()));
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        globalInfo.addEntry(player.getName());
        teamManager.addPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Tests if players are automatically removed after they quit the server
        Player player = event.getPlayer();
        if (!teamManager.players().contains(player)) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (teamManager.players().contains(player)) {
                    plugin.getLogger().warning("Player was not removed from the TeamManager! This is a bug.");
                }
            }
        }.runTaskLater(plugin, 1);
    }
}
