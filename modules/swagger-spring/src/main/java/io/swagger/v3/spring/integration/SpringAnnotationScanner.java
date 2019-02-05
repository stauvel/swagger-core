package io.swagger.v3.spring.integration;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.integration.IgnoredPackages;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiScanner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpringAnnotationScanner implements OpenApiScanner {

    static final Set<String> ignored = new HashSet();

    static {
        ignored.addAll(IgnoredPackages.ignored);
    }

    protected OpenAPIConfiguration openApiConfiguration;
    protected static Logger LOGGER = LoggerFactory.getLogger(SpringAnnotationScanner.class);


    public SpringAnnotationScanner openApiConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
        return this;
    }

    @Override
    public void setConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    @Override
    public Set<Class<?>> classes() {

        if (openApiConfiguration == null) {
            openApiConfiguration = new SwaggerConfiguration();
        }

        ClassGraph graph = new ClassGraph().enableAllInfo();
        Set<String> acceptablePackages = new HashSet<String>();
        Set<Class<?>> output = new HashSet<Class<?>>();

        // if classes are passed, use them
        if (openApiConfiguration.getResourceClasses() != null && !openApiConfiguration.getResourceClasses().isEmpty()) {
            for (String className : openApiConfiguration.getResourceClasses()) {
                if (!isIgnored(className)) {
                    try {
                        output.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        LOGGER.warn("error loading class from resourceClasses: " + e.getMessage(), e);
                    }
                }
            }
            return output;
        }

        boolean allowAllPackages = false;
        if (openApiConfiguration.getResourcePackages() != null && !openApiConfiguration.getResourcePackages().isEmpty()) {
            for (String pkg : openApiConfiguration.getResourcePackages()) {
                if (!isIgnored(pkg)) {
                    acceptablePackages.add(pkg);
                    graph.whitelistPackages(pkg);
                }
            }
        } else {
            allowAllPackages = true;
        }
        final Set<Class<?>> classes;
        try (ScanResult scanResult = graph.scan()) {
            //TODO generic
            classes = new HashSet<>(scanResult.getClassesWithAnnotation(Tag.class.getName()).loadClasses());
            classes.addAll(scanResult.getClassesWithAnnotation(OpenAPIDefinition.class.getName()).loadClasses());
//            classes = new HashSet<>(scanResult.getClassesWithAnnotation(javax.ws.rs.Path.class.getName()).loadClasses());
//            classes.addAll(new HashSet<>(scanResult.getClassesWithAnnotation(OpenAPIDefinition.class.getName()).loadClasses()));
        }

        for (Class<?> cls : classes) {
            if (allowAllPackages) {
                output.add(cls);
            } else {
                for (String pkg : acceptablePackages) {
                    if (cls.getPackage().getName().startsWith(pkg)) {
                        output.add(cls);
                    }
                }
            }
        }
        LOGGER.trace("classes() - output size {}", output.size());
        return output;
    }

    protected boolean isIgnored(String classOrPackageName) {
        if (StringUtils.isBlank(classOrPackageName)) {
            return true;
        }
        return ignored.stream().anyMatch(i -> classOrPackageName.startsWith(i));
    }

    @Override
    public Map<String, Object> resources() {
        return new HashMap<String, Object>();
    }
}
