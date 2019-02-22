package io.swagger.v3.spring;

import com.my.project.resources.BasicFieldsResource;
import com.my.project.resources.CompleteFieldsResource;
import com.my.project.resources.DuplicatedOperationIdResource;
import com.my.project.resources.DuplicatedOperationMethodNameResource;
import com.my.project.resources.ExtensionsResource;
import com.my.project.resources.ExternalDocsReference;
import com.my.project.resources.OperationExtensionsResource;
import com.my.project.resources.ParameterExtensionsResource;
import com.my.project.resources.ResponsesResource;
import com.my.project.resources.ServersResource;
import com.my.project.resources.SimpleMethods;
import com.my.project.resources.TagsResource;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * Test for the Reader Class
 */
public class ReaderTest {
    private static final String EXAMPLE_TAG = "Example Tag";
    private static final String SECOND_TAG = "Second Tag";
    private static final String OPERATION_SUMMARY = "Operation Summary";
    private static final String OPERATION_DESCRIPTION = "Operation Description";
    private static final String RESPONSE_CODE_200 = "200";
    private static final String RESPONSE_DESCRIPTION = "voila!";
    private static final String EXTERNAL_DOCS_DESCRIPTION = "External documentation description";
    private static final String EXTERNAL_DOCS_URL = "http://url.com";
    private static final String PATH_REF = "/";
    private static final String PATH_1_REF = "/1";
    private static final String PATH_2_REF = "/path";

    private static final int RESPONSES_NUMBER = 2;
    private static final int TAG_NUMBER = 2;
    private static final int PATHS_NUMBER = 1;

    @Test(description = "test a simple resource class")
    public void testSimpleReadClass() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(BasicFieldsResource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(paths.size(), 6);
        PathItem pathItem = paths.get(PATH_1_REF);
        assertNotNull(pathItem);
        assertNull(pathItem.getPost());
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());
    }

    @Test(description = "scan methods")
    public void testCompleteReadClass() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(CompleteFieldsResource.class);
        Paths paths = openAPI.getPaths();
        assertEquals(PATHS_NUMBER, paths.size());
        PathItem pathItem = paths.get(PATH_REF);
        assertNotNull(pathItem);
        assertNull(pathItem.getPost());
        Operation operation = pathItem.getGet();
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());

        assertEquals(TAG_NUMBER, operation.getTags().size());
        assertEquals(EXAMPLE_TAG, operation.getTags().get(0));
        assertEquals(SECOND_TAG, operation.getTags().get(1));

        ExternalDocumentation externalDocs = operation.getExternalDocs();
        assertEquals(EXTERNAL_DOCS_DESCRIPTION, externalDocs.getDescription());
        assertEquals(EXTERNAL_DOCS_URL, externalDocs.getUrl());
    }

    @Test(description = "scan methods")
    public void testScanMethods() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods;
        methods = SimpleMethods.class.getMethods();
        for (final Method method : methods) {
            if (isValidRestPath(method)) {
                Operation operation = reader.parseMethod(method, null, null);
                assertNotNull(operation);
            }
        }
    }

    @Test(description = "Get a Summary and Description")
    public void testGetSummaryAndDescription() {
        Reader reader = new Reader(new OpenAPI());
        Method[] methods = BasicFieldsResource.class.getMethods();
        Operation operation = reader.parseMethod(methods[0], null, null);
        assertNotNull(operation);
        assertEquals(OPERATION_SUMMARY, operation.getSummary());
        assertEquals(OPERATION_DESCRIPTION, operation.getDescription());
    }

    @Test(description = "Get a Duplicated Operation Id")
    public void testResolveDuplicatedOperationId() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(DuplicatedOperationIdResource.class);

        Paths paths = openAPI.getPaths();
        assertNotNull(paths);
        Operation firstOperation = paths.get(PATH_REF).getGet();
        Operation secondOperation = paths.get(PATH_2_REF).getGet();
        Operation thirdOperation = paths.get(PATH_REF).getPost();
        assertNotNull(firstOperation);
        assertNotNull(secondOperation);
        assertNotNull(thirdOperation);
        assertNotEquals(firstOperation.getOperationId(), secondOperation.getOperationId());
        assertNotEquals(firstOperation.getOperationId(), thirdOperation.getOperationId());
        assertNotEquals(secondOperation.getOperationId(), thirdOperation.getOperationId());
    }

    @Test(description = "Get a Duplicated Operation Id with same id as method name")
    public void testResolveDuplicatedOperationIdMethodName() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(DuplicatedOperationMethodNameResource.class);

        Paths paths = openAPI.getPaths();
        assertNotNull(paths);
        Operation firstOperation = paths.get("/1").getGet();
        Operation secondOperation = paths.get("/2").getGet();
        Operation secondPostOperation = paths.get("/2").getPost();
        Operation thirdPostOperation = paths.get("/3").getPost();
        assertNotNull(firstOperation);
        assertNotNull(secondOperation);
        assertNotNull(secondPostOperation);
        assertNotNull(thirdPostOperation);
        assertNotEquals(firstOperation.getOperationId(), secondOperation.getOperationId());
        assertNotEquals(secondOperation.getOperationId(), secondPostOperation.getOperationId());
        assertNotEquals(secondPostOperation.getOperationId(), thirdPostOperation.getOperationId());
        Operation thirdOperation = paths.get("/3").getGet();
        Operation fourthOperation = paths.get("/4").getGet();
        assertNotNull(thirdOperation);
        assertNotNull(fourthOperation);
        assertNotEquals(thirdOperation.getOperationId(), fourthOperation.getOperationId());

    }


    @Test(description = "Get tags")
    public void testGetTags() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(TagsResource.class);
        Operation operation = openAPI.getPaths().get("/").getGet();
        assertNotNull(operation);
        assertEquals(6, operation.getTags().size());
        assertEquals(operation.getTags().get(3), EXAMPLE_TAG);
        assertEquals(operation.getTags().get(1), SECOND_TAG);
        assertEquals(openAPI.getTags().get(1).getDescription(), "desc definition");
        assertEquals(openAPI.getTags().get(2).getExternalDocs().getDescription(), "docs desc");
    }

    @Test(description = "Get servers")
    public void testGetServers() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ServersResource.class);
        Operation operation = openAPI.getPaths().get("/").getGet();
        assertNotNull(operation);
        assertEquals(5, operation.getServers().size());
        assertEquals(operation.getServers().get(0).getUrl(), "http://class1");
        assertEquals(operation.getServers().get(1).getUrl(), "http://class2");
        assertEquals(operation.getServers().get(2).getUrl(), "http://method1");
        assertEquals(operation.getServers().get(3).getUrl(), "http://method2");
        assertEquals(operation.getServers().get(4).getUrl(), "http://op1");

        assertEquals(operation.getServers().get(0).getVariables().size(), 2);
        assertEquals(operation.getServers().get(0).getVariables().get("var1").getDescription(), "var 1");
        assertEquals(operation.getServers().get(0).getVariables().get("var1").getEnum().size(), 2);

        assertEquals(openAPI.getServers().get(0).getDescription(), "definition server 1");
    }

    @Test(description = "Responses")
    public void testGetResponses() {
        Reader reader = new Reader(new OpenAPI());

        Method[] methods = ResponsesResource.class.getMethods();

        Operation responseOperation = reader.parseMethod(Arrays.stream(methods).filter(
                (method -> method.getName().equals("getResponses"))).findFirst().get(), null, null);
        assertNotNull(responseOperation);

        ApiResponses responses = responseOperation.getResponses();
        assertEquals(RESPONSES_NUMBER, responses.size());

        ApiResponse apiResponse = responses.get(RESPONSE_CODE_200);
        assertNotNull(apiResponse);
        assertEquals(RESPONSE_DESCRIPTION, apiResponse.getDescription());
    }


    @Test(description = "Responses with composition")
    public void testGetResponsesWithComposition() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ResponsesResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/GenericError'\n" +
                "      deprecated: true\n" +
                "  /allOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getAllOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                allOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "  /anyOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getAnyOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                anyOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "  /oneOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getOneOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                oneOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    MultipleSub2Bean:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        d:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      description: MultipleSub2Bean\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    MultipleBaseBean:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        beanType:\n" +
                "          type: string\n" +
                "        a:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        b:\n" +
                "          type: string\n" +
                "      description: MultipleBaseBean\n" +
                "    MultipleSub1Bean:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        c:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      description: MultipleSub1Bean\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "    SampleResponseSchema:\n" +
                "      type: object";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "External Docs")
    public void testGetExternalDocs() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ExternalDocsReference.class);
        Operation externalDocsOperation = openAPI.getPaths().get("/").getGet();

        ExternalDocumentation externalDocs = externalDocsOperation.getExternalDocs();
        assertEquals(externalDocs.getDescription(), "External documentation description in method");
        assertEquals(externalDocs.getUrl(), EXTERNAL_DOCS_URL);
        externalDocs = openAPI.getComponents().getSchemas().get("ExternalDocsSchema").getExternalDocs();
        assertEquals("External documentation description in schema", externalDocs.getDescription());
        assertEquals(externalDocs.getUrl(), EXTERNAL_DOCS_URL);
    }

    @Test(description = "OperationExtensions Tests")
    public void testOperationExtensions() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(OperationExtensionsResource.class);
        assertNotNull(openAPI);
        Map<String, Object> extensions = openAPI.getPaths().get("/").getGet().getExtensions();
        assertEquals(extensions.size(), 2);
        assertNotNull(extensions.get("x-operation"));
        assertNotNull(extensions.get("x-operation-extensions"));
    }

    @Test(description = "ParameterExtensions Tests")
    public void testParameterExtensions() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ParameterExtensionsResource.class);
        assertNotNull(openAPI);
        Map<String, Object> extensions = openAPI.getPaths().get("/").getGet().getParameters().get(0).getExtensions();
        assertNotNull(extensions);
        assertEquals(1, extensions.size());
        assertNotNull(extensions.get("x-parameter"));

    }

    @Test(description = "Extensions Tests")
    public void testExtensions() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ExtensionsResource.class);
        assertNotNull(openAPI);
        SerializationMatchers.assertEqualsToYaml(openAPI, ExtensionsResource.YAML);
    }

    private Boolean isValidRestPath(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        return annotation != null && annotation.method() != null && annotation.method().length > 0;
    }

    @Test
    public void testClassWithGenericType() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ClassWithGenericType.class);
        assertNotNull(openAPI);

        assertNotNull(openAPI.getComponents().getSchemas().get("IssueTemplateRet"));
        assertNotNull(openAPI.getComponents().getSchemas().get("B"));
        assertNotNull(openAPI.getComponents().getSchemas().get("B").getProperties().get("test"));
        assertEquals(((Schema) openAPI.getComponents().getSchemas().get("B").getProperties().get("test")).get$ref(), "#/components/schemas/IssueTemplateRet");
    }

    public static class A {
        public B b;
    }

    public static class IssueTemplate<T> {

        public T getTemplateTest() {
            return null;
        }

        public String getTemplateTestString() {
            return null;
        }
    }

    public static class B {
        public IssueTemplate<Ret> getTest() {
            return null;
        }
    }

    public static class Ret {
        public String c;

    }

    static class ClassWithGenericType {
        @RequestMapping(method = RequestMethod.GET, path = "/test", produces = "application/json", consumes = "application/json")
        //public void test1(@RequestParam("aa") String a) {
        public void test1(A a) {
        }
    }

}