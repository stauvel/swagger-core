package com.my.project.resources;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@ExternalDocumentation(
        description = "External documentation description in class",
        url = "http://url.com"
)
public class ExternalDocsReference {
    @RequestMapping(method = RequestMethod.GET, path="/")
    @Operation(externalDocs =
    @ExternalDocumentation(
            description = "External documentation description in @Operation",
            url = "http://url.com"
    )
    )
    @ExternalDocumentation(
            description = "External documentation description in method",
            url = "http://url.com"
    )
    public void setRequestBody(@RequestBody ExternalDocsSchema schema) {
    }

    @Schema(externalDocs = @ExternalDocumentation(
            description = "External documentation description in schema",
            url = "http://url.com"
    ))
    public class ExternalDocsSchema {
        public String foo;
    }

}
