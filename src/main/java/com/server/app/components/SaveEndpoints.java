package com.server.app.components;

import com.server.app.config.SecurityRules;
import com.server.app.services.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@Component
@AllArgsConstructor
public class SaveEndpoints {

    private final PermissionService permissionService;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @EventListener(ApplicationReadyEvent.class)
    public void registerEndpoints() {
        requestMappingHandlerMapping.getHandlerMethods().forEach((info, method) -> {
            Set<String> patterns = info.getPatternValues();
            Set<RequestMethod> httpMethods = info.getMethodsCondition().getMethods();

            for (String path : patterns) {
                if (SecurityRules.isIgnored(path)) continue;

                for (RequestMethod httpMethod : httpMethods) {
                    String methodStr = httpMethod.name();
                    if (!SecurityRules.isPublic(methodStr, path)) {
                        permissionService.createIfNotExists(path, methodStr);
                    }
                }
            }
        });
    }
}
