package org.technbolts.scoop.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

public class EqualsTest {

    @Test
    public void areEquals() {
        assertThat(Equals.areEquals(null, null), is(true));
        assertThat(Equals.areEquals(null, 1234), is(false));
        assertThat(Equals.areEquals(1234, null), is(false));
        assertThat(Equals.areEquals(1234, 1234), is(true));
        assertThat(Equals.areEquals(1234, 4321), is(false));
        assertThat(Equals.areEquals(1234, "23"), is(false));
    }
}
