package com.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class OperationExtensionsResource {

    @GetMapping(path="/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description",
            extensions = {
                    @Extension(name = "x-operation", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "x-operation-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
                    })
    public ResponseEntity<String> getSummaryAndDescription() {
        return  ResponseEntity.ok("ok");
    }


}
