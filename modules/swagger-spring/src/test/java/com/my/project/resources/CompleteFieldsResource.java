package com.my.project.resources;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class CompleteFieldsResource {

    @GetMapping(path="/")
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description",
            tags = {"Example Tag", "Second Tag"},
            externalDocs =
            @ExternalDocumentation(
                    description = "External documentation description",
                    url = "http://url.com"
            ),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "subscriptionId",
                            required = true, description = "parameter description",
                            allowReserved = true,
                            schema = @Schema(
                                    type = "string",
                                    format = "uuid",
                                    description = "the generated UUID",
                                    accessMode = Schema.AccessMode.READ_ONLY)
                    )},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponsesResource.SampleResponseSchema.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "default",
                            description = "boo",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = ResponsesResource.GenericError.class)
                            )
                    )
            }
    )
    public ResponseEntity<String> getSummaryAndDescription() {
        return  ResponseEntity.ok("ok");
    }

}
