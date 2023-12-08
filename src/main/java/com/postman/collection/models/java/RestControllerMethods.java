package com.postman.collection.models.java;

import com.github.javaparser.ast.expr.AnnotationExpr;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class RestControllerMethods {

    private String name;
    private List<AnnotationExpr> annotations;
    private Map<String, List<AnnotationExpr>> parameters;

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
    public void setParameters(Map<String, List<AnnotationExpr>> parameters) {
        this.parameters = parameters;
    }
    public Map<String, List<AnnotationExpr>> getParameters() {
        return parameters;
    }

}
