package com.my.project.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class SimpleMethods {

    @GetMapping(path="/object")
    public TestBean getTestBean() {
        return new TestBean();
    }

    @GetMapping(path="/int")
    public int getInt() {
        return 0;
    }

    @GetMapping(path="/intArray")
    public int[] getIntArray() {
        return new int[]{0};
    }

    @GetMapping(path="/string")
    public String[] getStringArray() {
        return new String[]{};
    }

    @GetMapping(path="/stringArray")
    public void getWithIntArrayInput(@RequestParam("ids") int[] inputs) {
    }

    static class TestBean {
        public String foo;
        public TestChild testChild;
    }

    static class TestChild {
        public String foo;
    }
}