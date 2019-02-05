package com.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class ParameterExtensionsResource {

    @GetMapping(path="/")
    @Operation(operationId = "Id")
    public void getParameters(@Parameter(extensions = @Extension(name = "x-parameter", properties = {
            @ExtensionProperty(name = "parameter", value = "value")}))
                              @RequestParam("subscriptionId") String subscriptionId) {
    }

}
