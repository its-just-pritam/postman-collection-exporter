package com.postman.collection.service;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProjectViewPopupMenuActionHandler extends BasicActionHandler {

    private final Logger LOGGER = Logger.getLogger(ProjectViewPopupMenuActionHandler.class.getName());

    @Override
    public void handleAction(AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            Messages.showMessageDialog(e.getProject(),
                    "Project not selected!", "Error", AllIcons.General.ErrorDialog);
            return;
        }

        LOGGER.info("ProjectViewPopupMenuActionHandler invoked");
        VirtualFile[] selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
        List<VirtualFile> listOfFiles = selectedFiles == null ?
                new ArrayList<>() : List.of(selectedFiles);
        getControllers(e, project, listOfFiles);
        LOGGER.info("ProjectViewPopupMenuActionHandler executed");
    }

}
