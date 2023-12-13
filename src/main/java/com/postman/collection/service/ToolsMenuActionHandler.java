package com.postman.collection.service;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import java.util.logging.Logger;

public class ToolsMenuActionHandler extends BasicActionHandler {

    private final Logger LOGGER = Logger.getLogger(ToolsMenuActionHandler.class.getName());

    @Override
    public void handleAction(AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            Messages.showMessageDialog(e.getProject(),
                    "Project not selected!", "Error", AllIcons.General.ErrorDialog);
            return;
        }

        LOGGER.info("ToolsMenuActionHandler invoked");
        String baseProjectPath = project.getBasePath();
        if (baseProjectPath == null) {
            LOGGER.info("Project base path not found!");
            Messages.showMessageDialog(project,
                    "Project base path not found!", "Error", AllIcons.General.ErrorDialog);
            return;
        }

        FileChooserDescriptor fileDescriptor = new FileChooserDescriptor(
                true,
                true,
                false,
                false,
                false,
                true).withRoots(LocalFileSystem.getInstance().findFileByPath(baseProjectPath));

        fileDescriptor.setTitle("Select Endpoints");
        fileDescriptor.setDescription("Select file(s) containing Controller classes to export Postman REST API collection.");
        FileChooser.chooseFiles(fileDescriptor, project, null,
                virtualFiles -> getControllers(e, project, virtualFiles));
        LOGGER.info("ToolsMenuActionHandler executed");
    }

}
