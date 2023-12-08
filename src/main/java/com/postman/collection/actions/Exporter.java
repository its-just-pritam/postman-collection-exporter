package com.postman.collection.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.postman.collection.factory.CommonFactory;
import com.postman.collection.service.BasicActionHandler;
import icons.MyIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class Exporter extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        if (project == null) return;
        actionExecutor(e);
    }

    void actionExecutor(AnActionEvent e) {

        Optional<BasicActionHandler> actionHandlerObject = CommonFactory.ACTION_FACTORY_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().test(e.getPlace()))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(func -> func.apply(e));

        actionHandlerObject.ifPresent(basicActionHandler -> basicActionHandler.handleAction(e));
    }


}
