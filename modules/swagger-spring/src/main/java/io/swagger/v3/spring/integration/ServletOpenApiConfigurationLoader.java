package io.swagger.v3.spring.integration;

import io.swagger.v3.oas.integration.ClasspathOpenApiConfigurationLoader;
import io.swagger.v3.oas.integration.FileOpenApiConfigurationLoader;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfigBuilder;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiConfigurationLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import java.io.IOException;

public class ServletOpenApiConfigurationLoader implements OpenApiConfigurationLoader {

    private static Logger LOGGER = LoggerFactory.getLogger(ServletOpenApiConfigurationLoader.class);

    private ServletConfig servletConfig;

    private FileOpenApiConfigurationLoader fileOpenApiConfigurationLoader = new FileOpenApiConfigurationLoader();
    private ClasspathOpenApiConfigurationLoader classpathOpenApiConfigurationLoader = new ClasspathOpenApiConfigurationLoader();

    public ServletOpenApiConfigurationLoader(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public OpenAPIConfiguration load(String path) throws IOException {
        if (servletConfig == null) {
            return null;
        }
        if (StringUtils.isBlank(path)) { // we want to resolve from servlet params
            SwaggerConfiguration configuration = new SwaggerConfiguration()
                    .resourcePackages(ServletConfigContextUtils.resolveResourcePackages(servletConfig))
                    .filterClass(ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_FILTER_KEY))
                    .resourceClasses(ServletConfigContextUtils.resolveResourceClasses(servletConfig))
                    .readAllResources(ServletConfigContextUtils.getBooleanInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_READALLRESOURCES_KEY))
                    .prettyPrint(ServletConfigContextUtils.getBooleanInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_PRETTYPRINT_KEY))
                    .readerClass(ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_READER_KEY))
                    .cacheTTL(ServletConfigContextUtils.getLongInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_CACHE_TTL_KEY))
                    .scannerClass(ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_SCANNER_KEY))
                    .objectMapperProcessorClass(ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_OBJECT_MAPPER_PROCESSOR_KEY))
                    .modelConverterClasses(ServletConfigContextUtils.resolveModelConverterClasses(servletConfig));

            return configuration;

        }
        String location = ServletConfigContextUtils.getInitParam(servletConfig, path);
        if (!StringUtils.isBlank(location)) {
            if (classpathOpenApiConfigurationLoader.exists(location)) {
                return classpathOpenApiConfigurationLoader.load(location);
            } else if (fileOpenApiConfigurationLoader.exists(location)) {
                return fileOpenApiConfigurationLoader.load(location);
            }
        }

        String builderClassName = ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_BUILDER_KEY);
        if (StringUtils.isNotBlank(builderClassName)) {
            try {
                Class cls = getClass().getClassLoader().loadClass(builderClassName);
                // TODO instantiate with configuration
                OpenAPIConfigBuilder builder = (OpenAPIConfigBuilder) cls.newInstance();
                return builder.build();
            } catch (Exception e) {
                LOGGER.error("error loading builder: " + e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public boolean exists(String path) {

        if (servletConfig == null) {
            return false;
        }
        if (StringUtils.isBlank(path)) {
            if (ServletConfigContextUtils.resolveResourcePackages(servletConfig) != null) {
                return true;
            }
            if (ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_FILTER_KEY) != null) {
                return true;
            }
            if (ServletConfigContextUtils.resolveResourceClasses(servletConfig) != null) {
                return true;
            }
            if (ServletConfigContextUtils.getBooleanInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_READALLRESOURCES_KEY) != null) {
                return true;
            }
            if (ServletConfigContextUtils.getBooleanInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_PRETTYPRINT_KEY) != null) {
                return true;
            }
            if (ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_READER_KEY) != null) {
                return true;
            }
            if (ServletConfigContextUtils.getLongInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_CACHE_TTL_KEY) != null) {
                return true;
            }
            if (ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_SCANNER_KEY) != null) {
                return true;
            }
            if (ServletConfigContextUtils.getInitParam(servletConfig, ServletConfigContextUtils.OPENAPI_CONFIGURATION_OBJECT_MAPPER_PROCESSOR_KEY) != null) {
                return true;
            }
            if (ServletConfigContextUtils.resolveModelConverterClasses(servletConfig) != null) {
                return true;
            }
            return false;
        }
        String location = ServletConfigContextUtils.getInitParam(servletConfig, path);
        if (!StringUtils.isBlank(location)) {
            if (classpathOpenApiConfigurationLoader.exists(location)) {
                return true;
            }
            return fileOpenApiConfigurationLoader.exists(location);
        }
        return false;
    }
}
