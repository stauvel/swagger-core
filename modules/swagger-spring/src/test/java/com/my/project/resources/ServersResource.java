package com.my.project.resources;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@OpenAPIDefinition(
    servers = {
        @Server(
            description = "definition server 1",
            url = "http://definition1",
            variables = {
                @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"}),
                @ServerVariable(name = "var2", description = "var 2", defaultValue = "1", allowableValues = {"1", "2"})
            })
    }
)
@Server(
        description = "class server 1",
        url = "http://class1",
        variables = {
                @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"}),
                @ServerVariable(name = "var2", description = "var 2", defaultValue = "1", allowableValues = {"1", "2"})
        })
@Server(
        description = "class server 2",
        url = "http://class2",
        variables = {
                @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"})
        })
public class ServersResource {

    @GetMapping(path = "/")
    @Operation(servers = {
            @Server(
                    description = "operation server 1",
                    url = "http://op1",
                    variables = {
                            @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"})
                    })
    })
    @Server(
            description = "method server 1",
            url = "http://method1",
            variables = {
                    @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"})
            })
    @Server(
            description = "method server 2",
            url = "http://method2"
    )
    public ResponseEntity<String> getServers() {
        return ResponseEntity.ok("ok");
    }
}
