package com.my.project.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class SimpleMethods {

    @RequestMapping(method = RequestMethod.GET, path="/object")
    public TestBean getTestBean() {
        return new TestBean();
    }

    @RequestMapping(method = RequestMethod.GET, path="/int")
    public int getInt() {
        return 0;
    }

    @RequestMapping(method = RequestMethod.GET, path="/intArray")
    public int[] getIntArray() {
        return new int[]{0};
    }

    @RequestMapping(method = RequestMethod.GET, path="/string")
    public String[] getStringArray() {
        return new String[]{};
    }

    @RequestMapping(method = RequestMethod.GET, path="/stringArray")
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