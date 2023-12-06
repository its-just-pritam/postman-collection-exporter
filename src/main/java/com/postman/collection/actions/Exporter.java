package com.postman.collection.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import icons.MyIcons;
import org.jetbrains.annotations.NotNull;

public class Exporter extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog("Hello world!", "Postman Collection Exporter", MyIcons.exportLogoBig);
    }
}
