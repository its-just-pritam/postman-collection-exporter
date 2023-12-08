package com.postman.collection.factory;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.postman.collection.service.BasicActionHandler;
import com.postman.collection.service.EditorPopupMenuActionHandler;
import com.postman.collection.service.ProjectViewPopupMenuActionHandler;
import com.postman.collection.service.ToolsMenuActionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class CommonFactory {

    public static final Map<Predicate<String>, Function<AnActionEvent, BasicActionHandler>> ACTION_FACTORY_MAP = new LinkedHashMap<>();
    public static final Map<String, String> HTTP_METHOD_MAP = new LinkedHashMap<>();

    static {

        ACTION_FACTORY_MAP.put("EditorPopup"::equals, args -> new EditorPopupMenuActionHandler());
        ACTION_FACTORY_MAP.put("ProjectViewPopup"::equals, args -> new ProjectViewPopupMenuActionHandler());
        ACTION_FACTORY_MAP.put("MainMenu"::equals, args -> new ToolsMenuActionHandler());

        HTTP_METHOD_MAP.put("GetMapping", "GET");
        HTTP_METHOD_MAP.put("PostMapping", "POST");
        HTTP_METHOD_MAP.put("PutMapping", "PUT");
        HTTP_METHOD_MAP.put("DeleteMapping", "DELETE");
        HTTP_METHOD_MAP.put("PatchMapping", "PATCH");

    }

}
