package com.postman.collection.service;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public abstract class BasicActionHandler {
    public abstract void handleAction(Project project);
}
