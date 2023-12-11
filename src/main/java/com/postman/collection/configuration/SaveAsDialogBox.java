package com.postman.collection.configuration;

import com.github.javaparser.utils.Pair;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class SaveAsDialogBox {

    private static class MyDialogWrapper extends DialogWrapper {

        private final TextFieldWithBrowseButton filePath;
        private final JTextField fileName;

        protected MyDialogWrapper(Project project) {
            super(true); // use current window as parent

            setTitle("Save As");
            filePath = new TextFieldWithBrowseButton();
            FileChooserDescriptor folderDescriptor = new FileChooserDescriptor(
                    false,
                    true,
                    false,
                    false,
                    false,
                    false);
            filePath.addBrowseFolderListener("Destination Path", "", project, folderDescriptor);
            fileName = new JTextField();
            fileName.setText("example.postman_collection.json");

            init();
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new BorderLayout());

            JPanel pathPanel = new JPanel(new BorderLayout());
            pathPanel.add(new JLabel("Select destination:"), BorderLayout.NORTH);
            pathPanel.add(filePath, BorderLayout.CENTER);

            JPanel namePanel = new JPanel(new BorderLayout());
            namePanel.add(new JLabel("File Name:"), BorderLayout.NORTH);
            namePanel.add(fileName, BorderLayout.CENTER);

            panel.add(pathPanel, BorderLayout.NORTH);
            panel.add(namePanel, BorderLayout.CENTER);

            return panel;
        }
    }

    public Pair<String, String> showDialog(Project project) {
        MyDialogWrapper dialogWrapper = new MyDialogWrapper(project);
        boolean result = dialogWrapper.showAndGet();
        if(result
                && (dialogWrapper.filePath == null
                ||  dialogWrapper.fileName == null
                || dialogWrapper.filePath.getText().isEmpty()
                || dialogWrapper.fileName.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Please select a destination path and file name.");
            return showDialog(project);
        } else if (result) {
            return new Pair<>(dialogWrapper.filePath.getText(), dialogWrapper.fileName.getText());
        }

        return null;
    }

}
