package com.postman.collection.models.postman;

import java.util.List;

public class PostmanCollection {
    public String name;
    public info info;
    public List<PostmanCollection> item;
    public List<event> event;
    public request request;
    public List<?> response;

}
