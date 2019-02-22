package com.my.project.resources;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@OpenAPIDefinition(tags = {
        @Tag(name = "Definition First Tag"),
        @Tag(name = "Definition Second Tag full", description = "desc definition")
})
@Tag(name = "Second Tag")
@Tag(name = "Fourth Tag Full", description = "desc class", externalDocs = @ExternalDocumentation(description = "docs desc class"))
@Tag(name = "Fifth Tag Full", description = "desc class", externalDocs = @ExternalDocumentation(description = "docs desc class"))
@Tag(name = "Sixth Tag")
public class TagsResource {

    @RequestMapping(method = RequestMethod.GET, path="/")
    @Operation(tags = {"Example Tag", "Second Tag"})
    @Tag(name = "Third Tag")
    @Tag(name = "Second Tag")
    @Tag(name = "Fourth Tag Full", description = "desc", externalDocs = @ExternalDocumentation(description = "docs desc"))
    public ResponseEntity<String> getTags() {
        return ResponseEntity.ok("ok");
    }
}
