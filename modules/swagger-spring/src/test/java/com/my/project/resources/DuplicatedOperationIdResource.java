package com.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class DuplicatedOperationIdResource {

    @GetMapping(path="/")
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

    @GetMapping(path="/path")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getDuplicatedOperation() {
        return  ResponseEntity.ok("ok");
    }

}
