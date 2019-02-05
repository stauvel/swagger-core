package com.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class BasicFieldsResource {

    @GetMapping(path="/1")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getSummaryAndDescription(@RequestParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            schema = @Schema(
                    description = "the generated UUID",
                    type = "string",
                    format = "uuid",
                    accessMode = Schema.AccessMode.READ_ONLY)
    ) String subscriptionId) {
        return ResponseEntity.ok("ok");
    }

    @GetMapping(path="/2")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getSummaryAndDescription2(@RequestParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            array = @ArraySchema(maxItems = 10, minItems = 1,
                    schema = @Schema(implementation = Category.class, description = "the generated UUID"),
                    uniqueItems = true)
    ) String subscriptionId) {

        return ResponseEntity.ok("ok");
    }

    @GetMapping(path="/3")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getSummaryAndDescription3(@RequestParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            schema = @Schema(
                    implementation = Category.class,
                    description = "the generated UUID",
                    accessMode = Schema.AccessMode.READ_ONLY)
    ) String subscriptionId) {
        return ResponseEntity.ok("ok");
    }

    @GetMapping(path="/4")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getSummaryAndDescription4(@RequestParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true,
            array = @ArraySchema(maxItems = 10, minItems = 1,
                    schema = @Schema(format = "uuid", description = "the generated UUID"),
                    uniqueItems = true)
    ) String subscriptionId) {
        return ResponseEntity.ok("ok");
    }

    @GetMapping(path="/5")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getSummaryAndDescription5(@RequestParam("subscriptionId") @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
            required = true, description = "parameter description",
            allowEmptyValue = true, allowReserved = true, content = @Content(mediaType = "application/json")
    ) String subscriptionId) {
        return ResponseEntity.ok("ok");
    }

    @GetMapping(path="/6")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description")
    public ResponseEntity<String> getSummaryAndDescription6(
            @Parameter(
                    schema = @Schema(description = "test")
            ) String subscriptionId) {

        return ResponseEntity.ok("ok");
    }
}
