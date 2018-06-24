package org.echocat.unittest.utils.utils;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.echocat.unittest.utils.TestUtils.*;
import static org.echocat.unittest.utils.utils.StreamUtils.toStream;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class StreamUtilsUnitTest {

    @Test
    void constructur() throws Exception {
        new StreamUtils();
    }

    @Test
    void toStreamOfArray() throws Exception {
        final Object[] toConvert = givenArrayWithLength4();

        final Stream<Object> instance = toStream(toConvert);

        assertThat(instance.collect(toList()), equalTo(asList(toConvert)));
    }

    @Test
    void toStreamOfIterable() throws Exception {
        final Iterable<Integer> toConvert = givenCollectionOfLength4();

        final Stream<Integer> instance = toStream(toConvert);

        assertThat(instance.collect(toList()), equalTo(toConvert));
    }

    @Test
    void toStreamOfIterator() throws Exception {
        final Iterable<Integer> toConvert = givenCollectionOfLength4();

        final Stream<Integer> instance = toStream(toConvert.iterator());

        assertThat(instance.collect(toList()), equalTo(toConvert));
    }

    @Test
    void toStreamOfSpliterator() throws Exception {
        final Iterable<Integer> toConvert = givenCollectionOfLength4();

        final Stream<Integer> instance = toStream(toConvert.spliterator());

        assertThat(instance.collect(toList()), equalTo(toConvert));
    }

    @Test
    void toStreamOfStream() throws Exception {
        final Stream<Integer> toConvert = givenStreamWithLength4();

        final Stream<Integer> instance = toStream(toConvert);

        assertThat(instance, sameInstance(toConvert));
    }

}
