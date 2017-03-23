package org.echocat.unittest.utils.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static org.echocat.unittest.utils.TestUtils.givenDescription;
import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.WhereValueOf.*;
import static org.echocat.unittest.utils.matchers.WhereValueOfUnitTest.Person.person;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class WhereValueOfUnitTest {

    protected static final Matcher<String> IS_EQUAL_TO_BERT = isEqualTo("Bert");
    protected static final java.util.function.Function<Person, String> GET_NAME_OF_PERSON = Person::getName;

    @Test
    public void factoryMethodWhereValueOf() throws Exception {
        final Matcher<Person> instance = whereValueOf(GET_NAME_OF_PERSON, "name", IS_EQUAL_TO_BERT);

        assertThat(instance, instanceOf(WhereValueOf.class));
        assertThat(((WhereValueOf<?, ?>) instance).mapper(), sameInstance(GET_NAME_OF_PERSON));
        assertThat(((WhereValueOf<?, ?>) instance).mapperDescription(), equalTo("name"));
        assertThat(((WhereValueOf<?, ?>) instance).subMatcher(), sameInstance(IS_EQUAL_TO_BERT));
    }

    @Test
    public void factoryMethodElement() throws Exception {
        final Matcher<Person> instance = element(GET_NAME_OF_PERSON, "name", IS_EQUAL_TO_BERT);

        assertThat(instance, instanceOf(WhereValueOf.class));
        assertThat(((WhereValueOf<?, ?>) instance).mapper(), sameInstance(GET_NAME_OF_PERSON));
        assertThat(((WhereValueOf<?, ?>) instance).mapperDescription(), equalTo("name"));
        assertThat(((WhereValueOf<?, ?>) instance).subMatcher(), sameInstance(IS_EQUAL_TO_BERT));
    }

    @Test
    public void factoryMethodElementMatches() throws Exception {
        final Matcher<Person> instance = elementMatches(GET_NAME_OF_PERSON, "name", IS_EQUAL_TO_BERT);

        assertThat(instance, instanceOf(WhereValueOf.class));
        assertThat(((WhereValueOf<?, ?>) instance).mapper(), sameInstance(GET_NAME_OF_PERSON));
        assertThat(((WhereValueOf<?, ?>) instance).mapperDescription(), equalTo("name"));
        assertThat(((WhereValueOf<?, ?>) instance).subMatcher(), sameInstance(IS_EQUAL_TO_BERT));
    }

    @Test
    public void factoryMethodValueOfMatches() throws Exception {
        final Matcher<Person> instance = valueOfMatches(GET_NAME_OF_PERSON, "name", IS_EQUAL_TO_BERT);

        assertThat(instance, instanceOf(WhereValueOf.class));
        assertThat(((WhereValueOf<?, ?>) instance).mapper(), sameInstance(GET_NAME_OF_PERSON));
        assertThat(((WhereValueOf<?, ?>) instance).mapperDescription(), equalTo("name"));
        assertThat(((WhereValueOf<?, ?>) instance).subMatcher(), sameInstance(IS_EQUAL_TO_BERT));
    }

    @Test
    public void constructor() throws Exception {
        final WhereValueOf<Person, String> instance = new WhereValueOf<>(GET_NAME_OF_PERSON, "name", IS_EQUAL_TO_BERT);

        assertThat(instance, instanceOf(WhereValueOf.class));
        assertThat(instance.mapper(), sameInstance(GET_NAME_OF_PERSON));
        assertThat(instance.mapperDescription(), equalTo("name"));
        assertThat(instance.subMatcher(), sameInstance(IS_EQUAL_TO_BERT));
    }

    @Test
    public void matches() throws Exception {
        final WhereValueOf<Person, String> instance = givenEqualsNameWithBertInstance();

        assertThat(instance.matches(person("Bert", 35)), equalTo(true));
        assertThat(instance.matches(person("Tom", 35)), equalTo(false));
        assertThat(instance.matches("666"), equalTo(false));
    }

    @Test
    public void describeTo() throws Exception {
        final Description description = givenDescription();
        final WhereValueOf<Person, String> instance = givenEqualsNameWithBertInstance();

        instance.describeTo(description);

        assertThat(description.toString(), equalTo("where value of \"name\" is equal to \"Bert\""));
    }

    @Test
    public void describeMismatch() throws Exception {
        final Description description = givenDescription();
        final WhereValueOf<Person, String> instance = givenEqualsNameWithBertInstance();

        instance.describeMismatch(person("Tom", 35), description);

        assertThat(description.toString(), equalTo("was \"Tom\""));
    }

    @Test
    public void describeMismatchWithUnexpectedType() throws Exception {
        final Description description = givenDescription();
        final WhereValueOf<Person, String> instance = givenEqualsNameWithBertInstance();

        instance.describeMismatch("666", description);

        assertThat(description.toString(), equalTo("was unexpected type <" + String.class + ">"));
    }

    @Nonnull
    protected static WhereValueOf<Person, String> givenEqualsNameWithBertInstance() {
        return new WhereValueOf<>(GET_NAME_OF_PERSON, "name", IS_EQUAL_TO_BERT);
    }

    protected static class Person {

        @Nonnull
        public static Person person(@Nonnull String name, @Nonnegative int ageInYears) {
            return new Person(name, ageInYears);
        }

        @Nonnull
        private final String name;
        @Nonnegative
        private final int ageInYears;

        public Person(@Nonnull String name, @Nonnegative int ageInYears) {
            this.name = name;
            this.ageInYears = ageInYears;
        }

        @Nonnull
        public String getName() {
            return name;
        }

        @Nonnegative
        public int getAgeInYears() {
            return ageInYears;
        }
    }

}
