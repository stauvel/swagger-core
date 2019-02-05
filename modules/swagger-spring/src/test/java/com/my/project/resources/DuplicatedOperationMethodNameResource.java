package com.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class DuplicatedOperationMethodNameResource {

    @GetMapping(path="/1")
    @Operation(operationId = "getSummaryAndDescription2",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getSummaryAndDescription1() {
        return  ResponseEntity.ok("ok");
    }

    @GetMapping(path="/2")
    public ResponseEntity<String> getSummaryAndDescription2() {
        return  ResponseEntity.ok("ok");
    }

    @PostMapping(path="/2")
    @Operation(operationId = "postSummaryAndDescription3",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> postSummaryAndDescription2() {
        return  ResponseEntity.ok("ok");
    }

    @GetMapping(path="/3")
    public ResponseEntity<String> getSummaryAndDescription3() {
        return  ResponseEntity.ok("ok");
    }

    @PostMapping(path="/3")
    public ResponseEntity<String> postSummaryAndDescription3() {
        return  ResponseEntity.ok("ok");
    }

    @GetMapping(path="/4")
    public ResponseEntity<String> getSummaryAndDescription3(String foo) {
        return  ResponseEntity.ok("ok");
    }

}
