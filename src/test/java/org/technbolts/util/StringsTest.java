package org.technbolts.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.technbolts.util.Strings;
import org.testng.annotations.Test;

import fj.F2;

public class StringsTest {

    @Test
    public void joinF() {
        F2<StringBuilder, String, StringBuilder> joinF = Strings.joinF("#");
        StringBuilder buffer = new StringBuilder();
        
        assertThat("joinF must return the same buffer", joinF.f(buffer, "Z"), sameInstance(buffer));
        assertThat(buffer.toString(), equalTo("Z"));
        assertThat("joinF must return the same buffer", joinF.f(buffer, "o"), sameInstance(buffer));
        assertThat(buffer.toString(), equalTo("Z#o"));
        assertThat("joinF must return the same buffer", joinF.f(buffer, "G"), sameInstance(buffer));
        assertThat(buffer.toString(), equalTo("Z#o#G"));
    }
}
