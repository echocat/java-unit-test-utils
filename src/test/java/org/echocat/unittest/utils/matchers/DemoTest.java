package org.echocat.unittest.utils.matchers;

import org.junit.Test;

import java.util.List;

import static org.echocat.unittest.utils.matchers.CompareTo.isGreatherThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.ContainsAtLeastOneElementThat.Iterables.containsAtLeastOneElementThat;
import static org.echocat.unittest.utils.matchers.ContainsOnlyElementsThat.Iterables.containsOnlyElementsThat;
import static org.echocat.unittest.utils.matchers.HasItems.hasElements;
import static org.echocat.unittest.utils.matchers.HasItems.hasItems;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsOneOf.isOneOf;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsExceptionWithMessage;
import static org.echocat.unittest.utils.matchers.WhereValueOf.whereValueOf;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

import static java.util.Arrays.asList;

public class DemoTest {

    @Test
    public void matches() throws Exception {
        assertThat("hello", isEqualTo("hello"));

        final List<Moo> aListOfThings = asList(
                new Moo().setName("MyBert").setAge(45),
                new Moo().setName("MyTom").setAge(40)
        );

        assertThat(aListOfThings, hasElements());

        assertThat(aListOfThings, containsOnlyElementsThat(
                whereValueOf(Moo::getName, "name", startsWith("My")),
                whereValueOf(Moo::getAge, "age", isGreatherThanOrEqualTo(40))
        ));

        assertThat(aListOfThings, containsAtLeastOneElementThat(
                whereValueOf(Moo::getName, "name", startsWith("My")),
                whereValueOf(Moo::getAge, "age", isGreatherThanOrEqualTo(45))
        ));

        assertThat(() -> {
            throw new IllegalStateException("Hello world!");
        }, throwsException(IllegalStateException.class));

        assertThat("hello", isOneOf("hello", "foo", "bar"));
    }

    public static class Moo {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public Moo setName(String name) {
            this.name = name;
            return this;
        }

        public Integer getAge() {
            return age;
        }

        public Moo setAge(Integer age) {
            this.age = age;
            return this;
        }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }
}