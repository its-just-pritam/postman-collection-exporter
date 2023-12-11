package com.postman.collection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.javaparser.utils.Pair;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.postman.collection.configuration.SaveAsDialogBox;
import com.postman.collection.models.java.RestControllerClasses;
import com.postman.collection.models.postman.PostmanCollection;
import com.postman.collection.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ToolsMenuActionHandler extends BasicActionHandler {

    private final Utils utils = new Utils();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger LOGGER = Logger.getLogger(ToolsMenuActionHandler.class.getName());

    @Override
    public void handleAction(Project project) {

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
                virtualFiles -> getControllers(project,virtualFiles));

    }

    private void getControllers(Project project, List<VirtualFile> files) {

        List<VirtualFile> validFiles = new ArrayList<>();
        for(VirtualFile file : files)
            handleFiles(file, validFiles);

        List<RestControllerClasses> restControllerClasses = new ArrayList<>();
        for (VirtualFile validFile : validFiles) {
            restControllerClasses.addAll(utils.parseJavaFile(validFile.getCanonicalPath()));
        }

        SaveAsDialogBox saveAsDialogBox = new SaveAsDialogBox();
        Pair<String, String> savePath = saveAsDialogBox.showDialog(project);
        if(savePath == null)
            return;

        String collectionName = savePath.b;
        String collectionPath = savePath.a;
        PostmanCollection collection = utils.extractCollectionFromFiles(restControllerClasses, collectionName);

        try {
            String fullExportPath = collectionPath.concat("\\").concat(collectionName);
            LOGGER.info("Exporting Postman collection: " + fullExportPath);
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.writeValue(new File(fullExportPath), collection);
            Messages.showMessageDialog(project,
                    "Exported Postman collection: " + fullExportPath, "Success", AllIcons.General.SuccessDialog);
        } catch (IOException ex) {
            LOGGER.severe("Failed to export postman collection!");
            if(Messages.showDialog(
                    project, "Failed to export postman collection!", "Error",
                    ex.getLocalizedMessage(), List.of("OK", "Retry").toArray(new String[0]),
                    0, 0, AllIcons.General.ErrorDialog) == 1) {
                handleAction(project);
            }
        }

    }

    private void handleFiles(VirtualFile file, List<VirtualFile> validFiles) {

        if(file.isDirectory())
            for (VirtualFile child : file.getChildren())
                handleFiles(child, validFiles);

        else if ("JAVA".equals(file.getFileType().getName()))
            validFiles.add(file);
    }

}
