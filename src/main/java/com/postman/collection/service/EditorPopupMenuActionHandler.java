package com.postman.collection.service;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;
import java.util.logging.Logger;

public class EditorPopupMenuActionHandler extends BasicActionHandler {

    private final Logger LOGGER = Logger.getLogger(EditorPopupMenuActionHandler.class.getName());

    @Override
    public void handleAction(AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            Messages.showMessageDialog(e.getProject(),
                    "Project not selected!", "Error", AllIcons.General.ErrorDialog);
            return;
        }

        LOGGER.info("EditorPopupMenuActionHandler invoked");
        List<VirtualFile> selectedFiles = List.of(FileEditorManager.getInstance(project).getSelectedFiles());
        getControllers(e, project, selectedFiles);
        LOGGER.info("EditorPopupMenuActionHandler executed");
    }
}
