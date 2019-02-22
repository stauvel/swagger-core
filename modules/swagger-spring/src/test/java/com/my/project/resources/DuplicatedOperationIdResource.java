package com.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class DuplicatedOperationIdResource {

    @RequestMapping(method = RequestMethod.GET, path="/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getSummaryAndDescription() {
        return  ResponseEntity.ok("ok");
    }

    @PostMapping
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> postSummaryAndDescription() {
        return  ResponseEntity.ok("ok");
    }

    @RequestMapping(method = RequestMethod.GET, path="/path")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getDuplicatedOperation() {
        return  ResponseEntity.ok("ok");
    }

}
