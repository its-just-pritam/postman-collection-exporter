package icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.IconManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MyIcons {
    private static @NotNull Icon load(@NotNull String path) {
        return IconLoader.getIcon(path, MyIcons.class);
    }
    public static final Icon exportLogo = load("/export-logo.png");
    public static final Icon exportLogoBig = load("/export-logo-mini.png");
}