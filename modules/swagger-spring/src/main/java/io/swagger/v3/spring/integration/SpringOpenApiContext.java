package io.swagger.v3.spring.integration;

import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.integration.api.OpenApiScanner;
import io.swagger.v3.spring.Reader;
import org.apache.commons.lang3.StringUtils;

public class SpringOpenApiContext<T extends SpringOpenApiContext> extends GenericOpenApiContext<SpringOpenApiContext> implements OpenApiContext {

    @Override
    protected OpenApiReader buildReader(OpenAPIConfiguration openApiConfiguration) throws Exception {
        OpenApiReader reader;
        if (StringUtils.isNotBlank(openApiConfiguration.getReaderClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getReaderClass());
            reader = (OpenApiReader) cls.newInstance();
        } else {
            reader = new Reader();
        }
        reader.setConfiguration(openApiConfiguration);
        return reader;
    }

    @Override
    protected OpenApiScanner buildScanner(OpenAPIConfiguration openApiConfiguration) throws Exception {

        OpenApiScanner scanner;
        if (StringUtils.isNotBlank(openApiConfiguration.getScannerClass())) {
            Class cls = getClass().getClassLoader().loadClass(openApiConfiguration.getScannerClass());
            scanner = (OpenApiScanner) cls.newInstance();
        } else {
            scanner = new SpringAnnotationScanner();
        }
        scanner.setConfiguration(openApiConfiguration);
        return scanner;
    }
}
