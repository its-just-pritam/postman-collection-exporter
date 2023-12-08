package com.postman.collection.models.java;

import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.List;

public class RestControllerClasses {

    private String name;
    private List<AnnotationExpr> annotations;
    private List<RestControllerMethods> methods;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setAnnotations(List<AnnotationExpr> annotations) {
        this.annotations = annotations;
    }
    public List<AnnotationExpr> getAnnotations() {
        return annotations;
    }
    public void setMethods(List<RestControllerMethods> methods) {
        this.methods = methods;
    }
    public List<RestControllerMethods> getMethods() {
        return methods;
    }

}
