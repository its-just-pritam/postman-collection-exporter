package icons;

import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MyIcons {
    private static @NotNull Icon load(@NotNull String path, int cacheKey, int flags) {
        return IconManager.getInstance().loadRasterizedIcon(path, MyIcons.class.getClassLoader(), cacheKey, flags);
    }
    public static final Icon exportLogo = load("export-logo.png", 1686190501, 2);
    public static final Icon pluginLogo = load("pluginIcon.svg", 1457921589, 2);
}