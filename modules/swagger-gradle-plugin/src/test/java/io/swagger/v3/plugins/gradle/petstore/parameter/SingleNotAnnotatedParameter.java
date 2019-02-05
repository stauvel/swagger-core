package io.swagger.v3.plugins.gradle.petstore.parameter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.plugins.gradle.resources.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Class with a single not annotated parameter.
 */
public class SingleNotAnnotatedParameter {
    @GetMapping(path="/singlenoannotatedparameter")
    @Operation(operationId = "create User")
    public User findUser(final String id) {
        return new User();
    }
}
