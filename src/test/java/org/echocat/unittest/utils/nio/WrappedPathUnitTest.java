package org.echocat.unittest.utils.nio;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchService;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WrappedPathUnitTest {

    @Test
    public void wrapped() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        assertThat(instance.wrapped(), sameInstance(mockPath));
    }

    @Test
    public void getFileSystem() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getFileSystem();

        verify(mockPath, times(1)).getFileSystem();
    }

    @Test
    public void isAbsolute() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.isAbsolute();

        verify(mockPath, times(1)).isAbsolute();
    }

    @Test
    public void getRoot() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getRoot();

        verify(mockPath, times(1)).getRoot();
    }

    @Test
    public void getFileName() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getFileName();

        verify(mockPath, times(1)).getFileName();
    }

    @Test
    public void getParent() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getParent();

        verify(mockPath, times(1)).getParent();
    }

    @Test
    public void getNameCount() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getNameCount();

        verify(mockPath, times(1)).getNameCount();
    }

    @Test
    public void getName() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.getName(666);

        verify(mockPath, times(1)).getName(666);
    }

    @Test
    public void subpath() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.subpath(11, 66);

        verify(mockPath, times(1)).subpath(11, 66);
    }

    @Test
    public void startsWith() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.startsWith("abc");

        verify(mockPath, times(1)).startsWith("abc");
    }

    @Test
    public void startsWith1() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.startsWith(mockPath2);

        verify(mockPath1, times(1)).startsWith(mockPath2);
    }

    @Test
    public void endsWith() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.endsWith("abc");

        verify(mockPath, times(1)).endsWith("abc");
    }

    @Test
    public void endsWith1() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.endsWith(mockPath2);

        verify(mockPath1, times(1)).endsWith(mockPath2);
    }

    @Test
    public void normalize() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.normalize();

        verify(mockPath, times(1)).normalize();
    }

    @Test
    public void resolve() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.resolve("abc");

        verify(mockPath, times(1)).resolve("abc");
    }

    @Test
    public void resolve1() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.resolve(mockPath2);

        verify(mockPath1, times(1)).resolve(mockPath2);
    }

    @Test
    public void resolveSibling() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.resolveSibling("abc");

        verify(mockPath, times(1)).resolveSibling("abc");
    }

    @Test
    public void resolveSibling1() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.resolveSibling(mockPath2);

        verify(mockPath1, times(1)).resolveSibling(mockPath2);
    }

    @Test
    public void relativize() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.relativize(mockPath2);

        verify(mockPath1, times(1)).relativize(mockPath2);
    }

    @Test
    public void toUri() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.toUri();

        verify(mockPath, times(1)).toUri();
    }

    @Test
    public void toAbsolutePath() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.toAbsolutePath();

        verify(mockPath, times(1)).toAbsolutePath();
    }

    @Test
    public void toRealPath() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.toRealPath();

        verify(mockPath, times(1)).toRealPath();
    }

    @Test
    public void toFile() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.toFile();

        verify(mockPath, times(1)).toFile();
    }

    @Test
    public void register() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);
        final WatchService watchService = givenWatchService();
        final Kind<?>[] kinds = givenEmptyKinds();

        instance.register(watchService, kinds);

        verify(mockPath, times(1)).register(watchService, kinds);
    }

    @Test
    public void register1() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);
        final WatchService watchService = givenWatchService();
        final Modifier[] modifiers = givenEmptyModifiers();
        final Kind<?>[] kinds = givenEmptyKinds();

        instance.register(watchService, kinds, modifiers);

        verify(mockPath, times(1)).register(watchService, kinds, modifiers);
    }

    @Test
    public void iterator() throws Exception {
        final Path mockPath = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath);

        instance.iterator();

        verify(mockPath, times(1)).iterator();
    }

    @Test
    public void compareTo() throws Exception {
        final Path mockPath1 = givenMockPath();
        final Path mockPath2 = givenMockPath();
        final WrappedPath instance = givenInstanceFor(mockPath1);

        instance.compareTo(mockPath2);

        verify(mockPath1, times(1)).compareTo(mockPath2);
    }

    @Nonnull
    protected static WrappedPath givenInstanceFor(@Nonnull Path wrapped) {
        return new WrappedPathImpl(wrapped);
    }

    @Nonnull
    protected static Path givenMockPath() {
        return mock(Path.class);
    }

    @Nonnull
    protected static Modifier[] givenEmptyModifiers() {
        return new Modifier[]{};
    }

    @Nonnull
    protected static Kind<?>[] givenEmptyKinds() {
        return new Kind[]{};
    }

    @Nonnull
    protected static WatchService givenWatchService() {
        return mock(WatchService.class);
    }

    protected static class WrappedPathImpl implements WrappedPath {

        @Nonnull
        private final Path wrapped;

        public WrappedPathImpl(@Nonnull Path wrapped) {
            this.wrapped = wrapped;
        }

        @Nonnull
        @Override
        public Path wrapped() {
            return wrapped;
        }

    }

}
