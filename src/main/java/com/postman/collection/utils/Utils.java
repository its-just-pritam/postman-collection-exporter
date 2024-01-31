package com.postman.collection.utils;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.postman.collection.factory.CommonFactory;
import com.postman.collection.models.java.RestControllerClasses;
import com.postman.collection.models.java.RestControllerMethods;
import com.postman.collection.models.postman.PostmanCollection;
import com.postman.collection.models.postman.event;
import com.postman.collection.models.postman.info;
import com.postman.collection.models.postman.request;
import com.postman.collection.models.postman.script;
import com.postman.collection.models.postman.url;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Utils {

    private final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    public List<RestControllerClasses> parseJavaFile(String filePath) {
        List<RestControllerClasses> restControllerClasses = new ArrayList<>();
        try {
            LOGGER.info("Parsing file: " + filePath);
            CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
            cu.getTypes().forEach(type -> {

                if(type.isAnnotationPresent(Constants.REST_CONTROLLER)) {

                    RestControllerClasses javaClassObject = new RestControllerClasses();

                    List<RestControllerMethods> methods = new ArrayList<>();
                    for (MethodDeclaration method : type.getMethods()) {
                        RestControllerMethods javaMethodObject = new RestControllerMethods();
                        javaMethodObject.setName(method.getNameAsString());
                        javaMethodObject.setAnnotations(method.getAnnotations());
                        javaMethodObject.setParameters(method.getParameters().stream()
                                .collect(Collectors.toMap(NodeWithSimpleName::getNameAsString, Parameter::getAnnotations)));
                        methods.add(javaMethodObject);
                    }

                    javaClassObject.setMethods(methods);
                    javaClassObject.setName(type.getNameAsString());
                    javaClassObject.setAnnotations(type.getAnnotations());
                    restControllerClasses.add(javaClassObject);
                }

            });
        } catch (FileNotFoundException e) {
            LOGGER.info("File not found: " + filePath);
            e.printStackTrace();
        } catch (ParseProblemException e) {
            LOGGER.info("Parsing failed for file: " + filePath);
            e.printStackTrace();
        }
        return restControllerClasses;
    }


    public PostmanCollection extractCollectionFromFiles(List<RestControllerClasses> restControllerClasses, String collectionName) {

        PostmanCollection collection = new PostmanCollection();

        collection.name = collectionName.split("\\.")[0];
        collection.info = setInfo(collectionName);
        collection.event = setEvents();
        collection.item = setGroupedItems(restControllerClasses);

        return collection;
    }

    private List<event> setEvents() {

        List<event> events = new ArrayList<>();

        event prerequest = new event();
        prerequest.listen = Constants.PREREQUEST;
        prerequest.script = new script();
        prerequest.script.type = Constants.TEXT_JAVASCRIPT;
        prerequest.script.exec = List.of("");

        event test = new event();
        test.listen = "test";
        test.script = new script();
        test.script.type = Constants.TEXT_JAVASCRIPT;
        test.script.exec = List.of("");

        events.add(prerequest);
        events.add(test);

        return events;
    }

    private info setInfo(String collectionName) {

        info info = new info();

        info._postman_id = UUID.randomUUID().toString();
        info._exporter_id = UUID.randomUUID().toString();
        info.name = collectionName.split("\\.")[0];
        info.schema = Constants.POSTMAN_COLLECTION_SCHEMA;

        return info;
    }

    private List<PostmanCollection> setGroupedItems(List<RestControllerClasses> restControllerClasses) {

        List<PostmanCollection> items = new ArrayList<>();
        for (RestControllerClasses restControllerClass : restControllerClasses) {

            PostmanCollection item = new PostmanCollection();
            item.name = restControllerClass.getName();
            item.event = setEvents();
            String baseUrl = "";
            for (AnnotationExpr annotation : restControllerClass.getAnnotations()) {
                if(Constants.REQUEST_MAPPING.equals(annotation.getNameAsString())) {
                    baseUrl = processRequestAnnotation(annotation);
                }
            }
            item.item = setItems(restControllerClass.getMethods(), baseUrl);
            items.add(item);
        }

        return items;
    }

    private List<PostmanCollection> setItems(List<RestControllerMethods> restControllerMethods, String baseUrl) {

        List<PostmanCollection> items = new ArrayList<>();
        for (RestControllerMethods restControllerMethod : restControllerMethods) {

            PostmanCollection item = new PostmanCollection();
            item.name = restControllerMethod.getName();
            item.request = setRequest(restControllerMethod, baseUrl);
            item.response = new ArrayList<>();

            items.add(item);
        }

        return items;
    }

    // TO DO: Add support for multiple annotations
    private request setRequest(RestControllerMethods restControllerMethod, String baseUrl) {

        request request = new request();
        for (AnnotationExpr annotation : restControllerMethod.getAnnotations()) {
            if(CommonFactory.HTTP_METHOD_MAP.containsKey(annotation.getNameAsString())) {
                request.method = CommonFactory.HTTP_METHOD_MAP.get(annotation.getNameAsString());
                request.url = new url();
                request.url.host = List.of(Constants.LOCALHOST_8080);
                request.url.raw = baseUrl + processRequestAnnotation(annotation);
                String[] paths = request.url.raw.split("/");
                request.url.path = Arrays.asList(paths).subList(1, paths.length);
                request.url.protocol = Constants.HTTP;
            }
        }

        return request;
    }

    private String processRequestAnnotation(AnnotationExpr annotation) {
        if(annotation.getChildNodes().size() > 1) {
            String[] annotationValues = annotation.getChildNodes().get(1).toString().replaceAll("\\s", "").split(",");
            Map<String, String> map = new HashMap<>();
            for(String annotationValue : annotationValues) {
                if(annotationValue.contains("=") && annotationValue.split("=", 2).length > 1) {
                    map.put(annotationValue.split("=", 2)[0], annotationValue.split("=", 2)[1]);
                }
            }
            // Add an if case for a property in map containing type



            if(map.containsKey(Constants.VALUE)) {
                return map.get(Constants.VALUE).replaceAll("\"", "");
            } else if(map.size() == 0) {
                return annotation.getChildNodes().get(1).toString().replaceAll("\"", "");
            } else if(map.containsKey("type")) {
                return map.get("type").replaceAll("\"", "");
            } else {
                return "";
            }


        }
        return "";
    }
}
