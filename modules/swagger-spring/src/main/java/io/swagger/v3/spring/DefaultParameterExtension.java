package io.swagger.v3.spring;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ParameterProcessor;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.spring.ext.AbstractOpenAPIExtension;
import io.swagger.v3.spring.ext.OpenAPIExtension;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DefaultParameterExtension extends AbstractOpenAPIExtension {
    private static String QUERY_PARAM = "query";
    private static String HEADER_PARAM = "header";
    private static String COOKIE_PARAM = "cookie";
    private static String PATH_PARAM = "path";
    private static String FORM_PARAM = "form";

    final ObjectMapper mapper = Json.mapper();

    @Override
    public ResolvedParameter extractParameters(List<Annotation> annotations,
                                               Type type,
                                               Set<Type> typesToSkip,
                                               Components components,
                                               RequestMapping classAnnotation,
                                               RequestMapping  methodAnnotation,
                                               boolean includeRequestBody,
                                               JsonView jsonViewAnnotation,
                                               Iterator<OpenAPIExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ResolvedParameter();
        }


        Parameter parameter = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof RequestParam) {
                RequestParam param = (RequestParam) annotation;
                Parameter qp = new Parameter();
                qp.setIn(QUERY_PARAM);
                qp.setName(param.value());
                parameter = qp;
            } else if (annotation instanceof PathVariable) {
                PathVariable param = (PathVariable) annotation;
                Parameter pp = new Parameter();
                pp.setIn(PATH_PARAM);
                pp.setName(param.value());
                parameter = pp;
            } else if (annotation instanceof io.swagger.v3.oas.annotations.Parameter) {
                if (((io.swagger.v3.oas.annotations.Parameter) annotation).hidden()) {
                    return new ResolvedParameter();
                }
                if (parameter == null) {
                    parameter = new Parameter();
                }
                if (StringUtils.isNotBlank(((io.swagger.v3.oas.annotations.Parameter) annotation).ref())) {
                    parameter.$ref(((io.swagger.v3.oas.annotations.Parameter) annotation).ref());
                }
            }
        }
        List<Parameter> parameters = new ArrayList<>();
        ResolvedParameter extractParametersResult = new ResolvedParameter();

        if (parameter != null && (StringUtils.isNotBlank(parameter.getIn()) || StringUtils.isNotBlank(parameter.get$ref()))) {
            parameters.add(parameter);
        } else if (includeRequestBody) {
            Parameter unknownParameter = ParameterProcessor.applyAnnotations(
                    null,
                    type,
                    annotations,
                    components,
                    classAnnotation == null ? new String[0] : classAnnotation.consumes(),
                    methodAnnotation == null ? new String[0] : methodAnnotation.consumes(),
                    jsonViewAnnotation);
            if (unknownParameter != null) {
                if (StringUtils.isNotBlank(unknownParameter.getIn()) && !"form".equals(unknownParameter.getIn())) {
                    extractParametersResult.parameters.add(unknownParameter);
                } else if ("form".equals(unknownParameter.getIn())) {
                    unknownParameter.setIn(null);
                    extractParametersResult.formParameters.add(unknownParameter);
                } else {            // return as request body
                    extractParametersResult.requestBody = unknownParameter;
                }
            }
        }
        for (Parameter p : parameters) {
            Parameter processedParameter = ParameterProcessor.applyAnnotations(
                    p,
                    type,
                    annotations,
                    components,
                    classAnnotation == null ? new String[0] : classAnnotation.consumes(),
                    methodAnnotation == null ? new String[0] : methodAnnotation.consumes(),
                    jsonViewAnnotation);
            if (processedParameter != null) {
                extractParametersResult.parameters.add(processedParameter);
            }
        }
        return extractParametersResult;
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        return cls.getName().startsWith("javax.ws.rs.");
    }

}
