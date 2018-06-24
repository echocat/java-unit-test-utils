package org.echocat.unittest.utils.matchers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.echocat.unittest.utils.matchers.CompareTo.isGreaterThanOrEqualTo;
import static org.echocat.unittest.utils.matchers.HasItems.hasElements;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.IsOneOf.isOneOf;
import static org.echocat.unittest.utils.matchers.IterableMatchers.containsAtLeastOneElement;
import static org.echocat.unittest.utils.matchers.IterableMatchers.containsOnlyElements;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.matchers.WhereValueOf.whereValueOf;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class DemoUnitTest {

    @Test
    void matches() throws Exception {
        assertThat("hello", isEqualTo("hello"));

        final List<Moo> aListOfThings = asList(
            new Moo().setName("MyBert").setAge(45),
            new Moo().setName("MyTom").setAge(40)
        );

        assertThat(aListOfThings, hasElements());

        assertThat(aListOfThings, containsOnlyElements(
            whereValueOf(Moo::getName, "name", startsWith("My")),
            whereValueOf(Moo::getAge, "age", isGreaterThanOrEqualTo(40))
        ));

        assertThat(aListOfThings, containsAtLeastOneElement(
            whereValueOf(Moo::getName, "name", startsWith("My")),
            whereValueOf(Moo::getAge, "age", isGreaterThanOrEqualTo(45))
        ));

        assertThat(() -> {
            throw new IllegalStateException("Hello world!");
        }, throwsException(IllegalStateException.class));

        assertThat("hello", isOneOf("hello", "foo", "bar"));
    }

    private static class Moo {
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
