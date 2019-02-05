package com.my.project.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
public class ResponsesResource {

    @GetMapping(path="/")
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithPayloadResponse",
            deprecated = true,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "voila!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SampleResponseSchema.class)
                            )
                    ),
                    @ApiResponse(
                            description = "boo",
                            content = @Content(
                                    mediaType = "*/*",
                                    schema = @Schema(implementation = GenericError.class)
                            )
                    )
            }
    )

    public void getResponses() {
    }

    @GetMapping(path="/oneOf", produces = "application/json")
    @Operation(summary = "Test inheritance / polymorphism",
            responses = {
                    @ApiResponse(description = "bean answer",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            oneOf = { MultipleSub1Bean.class, MultipleSub2Bean.class }
                                    )
                            )
                    )
            })
    public MultipleBaseBean getOneOf(
            HttpServletRequest req,
            @Parameter(description = "Test inheritance / polymorphism",
                    required = true,
                    example = "1")
            @RequestParam("number") final int beanNumber) {

        return null;
    }

    @GetMapping(path="/anyOf", produces = "application/json")
    @Operation(summary = "Test inheritance / polymorphism",
            responses = {
                    @ApiResponse(description = "bean answer",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            anyOf = { MultipleSub1Bean.class, MultipleSub2Bean.class }
                                    )
                            )
                    )
            })
    public MultipleBaseBean getAnyOf(
            HttpServletRequest req,
            @Parameter(description = "Test inheritance / polymorphism",
                    required = true,
                    example = "1")
            @RequestParam("number") final int beanNumber) {

        return null;
    }

    @GetMapping(path="/allOf", produces = "application/json")
    @Operation(summary = "Test inheritance / polymorphism",
            responses = {
                    @ApiResponse(description = "bean answer",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            allOf = { MultipleSub1Bean.class, MultipleSub2Bean.class }
                                    )
                            )
                    )
            })
    public MultipleBaseBean getAllOf(
            HttpServletRequest req,
            @Parameter(description = "Test inheritance / polymorphism",
                    required = true,
                    example = "1")
            @RequestParam("number") final int beanNumber) {

        return null;
    }

    static class SampleResponseSchema {
        @Schema(description = "the user id")
        private String id;
    }

    static class GenericError {
        private int code;
        private String message;
    }

}
