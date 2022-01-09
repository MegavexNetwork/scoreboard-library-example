package net.megavex.scoreboardlibraryexample;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.megavex.scoreboardlibrary.api.sidebar.AbstractSidebar;
import net.megavex.scoreboardlibrary.api.sidebar.line.SidebarLine;
import net.megavex.scoreboardlibraryexample.util.ColorUtil;
import net.megavex.scoreboardlibraryexample.util.TestTranslator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.util.Collection;
import java.util.Locale;
import java.util.function.Supplier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;

public class AbstractSidebarExample extends AbstractSidebar implements Listener {

    private final BukkitTask task;

    public AbstractSidebarExample(ExamplePlugin plugin) {
        super(plugin.scoreboardManager().sidebar(15, null));

        sidebar.title(text("Epic Sidebar"));

        registerEmptyLine(0);
        SidebarLine unixLine = registerLine(1, (Supplier<Component>)
                () -> text("Unix time: " + System.currentTimeMillis())
                        .color(TextColor.color(Color.RED.getRGB()))); // ?

        registerEmptyLine(2);
        registerStaticLine(3, text("VeryLongLineThatShouldBeCutOffFor1.12AndBelowPlayersButNotOnNewerVersions", NamedTextColor.AQUA, TextDecoration.BOLD));
        SidebarLine flashingLine = registerLine(4, (Supplier<Component>)
                () -> text("Line that changes colors", ColorUtil.randomNamedColor())); // ?

        registerEmptyLine(5);
        registerStaticLine(6, text("Identical line"));
        registerStaticLine(7, text("Identical line"));

        registerEmptyLine(8);
        registerStaticLine(10, text("Your locale: ").append(translatable(TestTranslator.KEY)));

        // Do random stuff to see if everything works
        task = new BukkitRunnable() {

            byte tick = 0;
            boolean flag = false;
            boolean hidden = true;

            @Override
            public void run() {
                tick++;

                if (hidden && tick == 20) {
                    if (flag) {
                        sidebar.visible(true);
                    } else {
                        sidebar.addPlayers((Collection<Player>) Bukkit.getOnlinePlayers());
                    }
                    flag = !flag;
                    hidden = false;
                    tick = 0;
                    return;
                }

                if (tick % 20 == 0) {
                    sidebar.title(sidebar.title().color(ColorUtil.randomColor()));
                    updateLine(flashingLine);
                    if (sidebar.line(9) == null) {
                        sidebar.line(9, text("Disappearing line"));
                    } else {
                        sidebar.line(9, null);
                    }
                }

                if (tick == 100) {
                    tick = 0;
                    if (flag) {
                        sidebar.visible(false);
                    } else {
                        sidebar.removePlayers(sidebar.players());
                    }

                    hidden = true;

                }

                updateLine(unixLine);
            }
        }.runTaskTimer(plugin, 1, 1);

        sidebar.visible(true);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    protected void onClosed() {
        task.cancel();
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        sidebar.addPlayer(event.getPlayer());
    }
}
