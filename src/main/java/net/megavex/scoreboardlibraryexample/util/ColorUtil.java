package net.megavex.scoreboardlibraryexample.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public final class ColorUtil {

  private final static Random random = new Random();
  private final static List<NamedTextColor> colors = new ArrayList<>(NamedTextColor.NAMES.values());

  public static NamedTextColor randomNamedColor() {
    return colors.get(random.nextInt(colors.size()));
  }

  public static TextColor randomColor() {
    return TextColor.color((int) (Math.random() * 0x1000000));
  }
}
