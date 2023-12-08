package com.postman.collection.utils;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.postman.collection.factory.CommonFactory;
import com.postman.collection.models.java.RestControllerClasses;
import com.postman.collection.models.java.RestControllerMethods;
import com.postman.collection.models.postman.PostmanCollection;
import com.postman.collection.models.postman.event;
import com.postman.collection.models.postman.info;
import com.postman.collection.models.postman.request;
import com.postman.collection.models.postman.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {

    public List<RestControllerClasses> parseJavaFile(String filePath) {
        List<RestControllerClasses> restControllerClasses = new ArrayList<>();
        try {
            CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
            cu.getTypes().forEach(type -> {

                RestControllerClasses javaClassObject = new RestControllerClasses();

                List<RestControllerMethods> methods = new ArrayList<>();
                for (MethodDeclaration method : type.getMethods()) {
                    RestControllerMethods javaMethodObject = new RestControllerMethods();
                    javaMethodObject.setName(method.getNameAsString());
                    javaMethodObject.setAnnotations(method.getAnnotations());
                    methods.add(javaMethodObject);
                }

                javaClassObject.setMethods(methods);
                javaClassObject.setName(type.getNameAsString());
                javaClassObject.setAnnotations(type.getAnnotations());
                restControllerClasses.add(javaClassObject);

            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return restControllerClasses;
    }


    public PostmanCollection extractCollectionFromFiles(List<RestControllerClasses> restControllerClasses) {

        PostmanCollection collection = new PostmanCollection();

        //User Input
        collection.name = "Postman Collection";

        collection.info = setInfo();
        collection.event = setEvents();

        collection.item = setGroupedItems(restControllerClasses);

        return collection;
    }

    private List<event> setEvents() {

        List<event> events = new ArrayList<>();

        event prerequest = new event();
        prerequest.listen = "prerequest";
        prerequest.script = new script();
        prerequest.script.type = "text/javascript";
        prerequest.script.exec = List.of("");

        event test = new event();
        test.listen = "test";
        test.script = new script();
        test.script.type = "text/javascript";
        test.script.exec = List.of("");

        events.add(prerequest);
        events.add(test);

        return events;
    }

    private info setInfo() {

        info info = new info();

        info._postman_id = UUID.randomUUID().toString();
        info._exporter_id = UUID.randomUUID().toString();
        info.name = "Postman Collection";
        info.schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

        return info;
    }

    private List<PostmanCollection> setGroupedItems(List<RestControllerClasses> restControllerClasses) {

        List<PostmanCollection> items = new ArrayList<>();
        for (RestControllerClasses restControllerClass : restControllerClasses) {

            PostmanCollection item = new PostmanCollection();
            item.name = restControllerClass.getName();
            item.event = setEvents();
            item.item = setItems(restControllerClass.getMethods());

            items.add(item);
        }

        return items;
    }

    private List<PostmanCollection> setItems(List<RestControllerMethods> restControllerMethods) {

        List<PostmanCollection> items = new ArrayList<>();
        for (RestControllerMethods restControllerMethod : restControllerMethods) {

            PostmanCollection item = new PostmanCollection();
            item.name = restControllerMethod.getName();
            item.request = setRequest(restControllerMethod);
            item.response = new ArrayList<>();

            items.add(item);
        }

        return items;
    }

    // TO DO: Add support for multiple annotations
    private request setRequest(RestControllerMethods restControllerMethod) {

        request request = new request();
        for (AnnotationExpr annotation : restControllerMethod.getAnnotations()) {
            if(CommonFactory.HTTP_METHOD_MAP.containsKey(annotation.getNameAsString())) {
                request.method = CommonFactory.HTTP_METHOD_MAP.get(annotation.getNameAsString());
            }
        }

        return request;
    }
}
