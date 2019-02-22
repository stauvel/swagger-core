package com.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class ParameterExtensionsResource {

    @RequestMapping(method = RequestMethod.GET, path="/")
    @Operation(operationId = "Id")
    public void getParameters(@Parameter(extensions = @Extension(name = "x-parameter", properties = {
            @ExtensionProperty(name = "parameter", value = "value")}))
                              @RequestParam("subscriptionId") String subscriptionId) {
    }

}
