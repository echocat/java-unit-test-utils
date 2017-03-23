package org.echocat.unittest.utils.utils;

import org.junit.Test;

import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.utils.ClassUtils.typeOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ClassUtilsUnitTest {

    @Test
    public void constructor() throws Exception {
        new ClassUtils();
    }

    @Test
    public void typeOfDoesNotAcceptNull() throws Exception {
        //noinspection ConstantConditions
        assertThat(() -> typeOf(Comparable.class, null), throwsException(NullPointerException.class, "The provided object value is null."));
    }

    @Test
    public void typeOfDoesNotAcceptNonComparable() throws Exception {
        assertThat(() -> typeOf(Comparable.class, new Object()), throwsException(IllegalArgumentException.class, "The provided object value is not of type.*"));
    }

    @Test
    public void typeOfHappyPath() throws Exception {
        assertThat(typeOf(Comparable.class, 1), sameInstance(Integer.class));
    }

}
