package com.postman.collection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.postman.collection.models.java.RestControllerClasses;
import com.postman.collection.models.postman.PostmanCollection;
import com.postman.collection.utils.Utils;
import icons.MyIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToolsMenuActionHandler extends BasicActionHandler {

    Utils utils = new Utils();
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public void handleAction(AnActionEvent e) {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(
                true,
                true,
                false,
                false,
                false,
                true);
        descriptor.setTitle("Select Endpoints");
        descriptor.setDescription("Select file(s) containing Controller classes to export Postman REST API collection.");

        FileChooser.chooseFiles(descriptor, e.getProject(), null,
                virtualFiles -> getControllers(e,virtualFiles));

    }

    private void getControllers(AnActionEvent e, List<VirtualFile> files) {

        List<VirtualFile> validFiles = new ArrayList<>();
        for(VirtualFile file : files)
            handleFiles(file, validFiles);

        List<RestControllerClasses> restControllerClasses = new ArrayList<>();
        for (VirtualFile validFile : validFiles) {
            restControllerClasses.addAll(utils.parseJavaFile(validFile.getCanonicalPath()));
        }

        List<String> validFileNames =
                validFiles.stream().map(VirtualFile::getNameWithoutExtension).collect(Collectors.toList());
        PostmanCollection collection = utils.extractCollectionFromFiles(restControllerClasses);
        try {
            String collectionJson = mapper.writeValueAsString(collection);
        } catch (JsonProcessingException ex) {
            Messages.showMessageDialog(
                    e.getProject(), String.join("\n", validFileNames),
                    "Failed to Export Postman Collection",
                    AllIcons.General.ErrorDialog);
        }

        Messages.showMessageDialog(
                e.getProject(), String.join("\n", validFileNames),
                "Exporting " + validFileNames.size() + " file(s)",
                AllIcons.General.SuccessDialog);

    }

    private void handleFiles(VirtualFile file, List<VirtualFile> validFiles) {

        if(file.isDirectory())
            for (VirtualFile child : file.getChildren())
                handleFiles(child, validFiles);

        else if ("JAVA".equals(file.getFileType().getName()))
            validFiles.add(file);
    }

}
