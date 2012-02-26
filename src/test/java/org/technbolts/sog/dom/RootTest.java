package org.technbolts.sog.dom;

import org.technbolts.sog.exception.InvalidPathExpression;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RootTest {
    private Root root;
    
    @BeforeMethod
    public void setUp () throws Exception {
        root = new Root("root");
    }
    
    @Test
    public void ensurePathIsValid_validSinglePathFragment_dontThrowException() {
        root.ensurePathIsValid("server");
    }

    @Test
    public void ensurePathIsValid_validPath_dontThrowException() {
        root.ensurePathIsValid("server/port");
    }
    
    @Test(expectedExceptions=InvalidPathExpression.class)
    public void ensurePathIsValid_pathCannotStartsWithSlash() {
        root.ensurePathIsValid("/server");
    }

    @Test(expectedExceptions=InvalidPathExpression.class)
    public void ensurePathIsValid_pathCannotEndsWithSlash() {
        root.ensurePathIsValid("server/port/");
    }
    

}
