package org.echocat.unittest.utils.nio;

import org.junit.Test;

import static org.echocat.unittest.utils.matchers.IsEqualTo.isEqualTo;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.echocat.unittest.utils.nio.Relation.classRelationFor;
import static org.echocat.unittest.utils.nio.Relation.objectRelationFor;
import static org.echocat.unittest.utils.nio.Relation.targetOf;
import static org.echocat.unittest.utils.nio.Relation.typeOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class RelationUnitTest {

    @Test
    public void classRelationFor_consuming_class_works_as_expected() {
        final Relation.Class<Integer> actual = classRelationFor(Integer.class);

        assertThat(actual.get(), isEqualTo(Integer.class));
    }

    @Test
    public void classRelationFor_consuming_object_works_as_expected() {
        final Relation.Class<Integer> actual = classRelationFor(123);

        assertThat(actual.get(), isEqualTo(Integer.class));
    }

    @Test
    public void objectRelationFor_works_as_expected() {
        final Relation.Object<Integer> actual = objectRelationFor(123);

        assertThat(actual.get(), isEqualTo(123));
    }

    @Test
    public void typeOf_for_ClassRelation_returns_content() {
        final Relation<?> instance = classRelationFor(Integer.class);

        final Class<?> actual = typeOf(instance);

        assertThat(actual, isEqualTo(Integer.class));
    }

    @Test
    public void typeOf_for_ObjectRelation_returns_content() {
        final Relation<?> instance = objectRelationFor(123);

        final Class<?> actual = typeOf(instance);

        assertThat(actual, isEqualTo(Integer.class));
    }

    @Test
    public void typeOf_for_other_Relation_fails() {
        final Relation<?> instance = () -> 123;

        assertThat(() -> typeOf(instance)
            , throwsException(IllegalArgumentException.class, "Could not handle relation.+"));
    }

    @Test
    public void targetOf_for_ClassRelation_returns_content() {
        final Relation<?> instance = classRelationFor(Integer.class);

        final Object actual = targetOf(instance);

        assertThat(actual, isEqualTo(null));
    }

    @Test
    public void targetOf_for_ObjectRelation_returns_content() {
        final Relation<?> instance = objectRelationFor(123);

        final Object actual = targetOf(instance);

        assertThat(actual, isEqualTo(123));
    }

    @Test
    public void targetOf_for_other_Relation_fails() {
        final Relation<?> instance = () -> 123;

        assertThat(() -> targetOf(instance)
            , throwsException(IllegalArgumentException.class, "Could not handle relation.+"));
    }

}