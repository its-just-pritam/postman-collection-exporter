package com.postman.collection.service;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public abstract class BasicActionHandler {
    public abstract void handleAction(AnActionEvent e);
}
