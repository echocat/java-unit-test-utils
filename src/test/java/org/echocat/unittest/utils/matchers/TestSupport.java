package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;

import javax.annotation.Nonnull;

public abstract class TestSupport {

    @Nonnull
    protected Description givenDescription() {
        return new StringDescription();
    }


}
