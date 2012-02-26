package org.technbolts.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.technbolts.util.Classes;
import org.testng.annotations.Test;

public class ClassesTest {

    
    @Test
    public void simpleNames_noElementInArray () {
        String string = Classes.toSimpleNames();
        assertThat(string, equalTo(""));
    }
    
    @Test
    public void simpleNames_oneElementInArray () {
        String string = Classes.toSimpleNames(ClassesTest.class);
        assertThat(string, equalTo("ClassesTest"));
    }
    
    @Test
    public void simpleNames_twoElementsInArray () {
        String string = Classes.toSimpleNames(ClassesTest.class, Classes.class);
        assertThat(string, equalTo("ClassesTest, Classes"));
    }

}
