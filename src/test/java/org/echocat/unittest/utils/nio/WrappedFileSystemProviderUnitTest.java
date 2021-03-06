package org.echocat.unittest.utils.nio;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.net.URI;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.util.Collections.singleton;
import static org.echocat.unittest.utils.matchers.ThrowsException.throwsException;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class WrappedFileSystemProviderUnitTest {

    @Test
    public void wrapped() throws Exception {
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        assertThat(instance.wrapped(), sameInstance(mockFileSystemProvider));
    }

    @Test
    public void wrappedPathType() throws Exception {
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        assertThat(instance.wrappedPathType(), sameInstance(WrappedPath.class));
    }

    @Test
    public void unwrap() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        assertThat(() -> instance.unwrap(null), throwsException(NullPointerException.class, "No path provided."));
        assertThat(() -> instance.unwrap(mock(Path.class)), throwsException(IllegalArgumentException.class, "Could only handle paths of type '" + WrappedPath.class.getName() +
            "' but got an instance of type '.+'\\."));
        assertThat(instance.unwrap(path), sameInstance(path.wrapped()));
    }

    @Test
    public void getScheme() throws Exception {
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.getScheme();

        verify(mockFileSystemProvider, times(1)).getScheme();
    }

    @Test
    public void newFileSystem() throws Exception {
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final URI uri = URI.create("foo:bar");
        final Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");

        instance.newFileSystem(uri, map);

        verify(mockFileSystemProvider, times(1)).newFileSystem(uri, map);
    }

    @Test
    public void getFileSystem() throws Exception {
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final URI uri = URI.create("foo:bar");

        instance.getFileSystem(uri);

        verify(mockFileSystemProvider, times(1)).getFileSystem(uri);
    }

    @Test
    public void getPath() throws Exception {
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final URI uri = URI.create("foo:bar");

        instance.getPath(uri);

        verify(mockFileSystemProvider, times(1)).getPath(uri);
    }

    @Test
    public void newFileSystem1() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");

        instance.newFileSystem(path, map);

        verify(mockFileSystemProvider, times(1)).newFileSystem(path.wrapped(), map);
    }

    @Test
    public void newInputStream() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.newInputStream(path, APPEND);

        verify(mockFileSystemProvider, times(1)).newInputStream(path.wrapped(), APPEND);
    }

    @Test
    public void newOutputStream() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.newOutputStream(path, APPEND);

        verify(mockFileSystemProvider, times(1)).newOutputStream(path.wrapped(), APPEND);
    }

    @Test
    public void newFileChannel() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final FileAttribute<?>[] attrs = givenFileAttributes();
        final Set<StandardOpenOption> options = singleton(APPEND);

        instance.newFileChannel(path, options, attrs);

        verify(mockFileSystemProvider, times(1)).newFileChannel(path.wrapped(), options, attrs);
    }

    @Test
    public void newAsynchronousFileChannel() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final ExecutorService executorService = givenMockExecutorService();
        final FileAttribute<?>[] attrs = givenFileAttributes();
        final Set<StandardOpenOption> options = singleton(APPEND);

        instance.newAsynchronousFileChannel(path, options, executorService, attrs);

        verify(mockFileSystemProvider, times(1)).newAsynchronousFileChannel(path.wrapped(), options, executorService, attrs);
    }

    @Test
    public void newByteChannel() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final FileAttribute<?>[] attrs = givenFileAttributes();
        final Set<StandardOpenOption> options = singleton(APPEND);

        instance.newByteChannel(path, options, attrs);

        verify(mockFileSystemProvider, times(1)).newByteChannel(path.wrapped(), options, attrs);
    }

    @Test
    public void newDirectoryStream() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final Filter<? super Path> filter = givenFilter();

        instance.newDirectoryStream(path, filter);

        verify(mockFileSystemProvider, times(1)).newDirectoryStream(path.wrapped(), filter);
    }

    @Test
    public void createDirectory() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final FileAttribute<?>[] attrs = givenFileAttributes();

        instance.createDirectory(path, attrs);

        verify(mockFileSystemProvider, times(1)).createDirectory(path.wrapped(), attrs);
    }

    @Test
    public void createSymbolicLink() throws Exception {
        final WrappedPath path1 = givenMockWrappedPath();
        final WrappedPath path2 = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);
        final FileAttribute<?>[] attrs = givenFileAttributes();

        instance.createSymbolicLink(path1, path2, attrs);

        verify(mockFileSystemProvider, times(1)).createSymbolicLink(path1.wrapped(), path2.wrapped(), attrs);
    }

    @Test
    public void createLink() throws Exception {
        final WrappedPath path1 = givenMockWrappedPath();
        final WrappedPath path2 = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.createLink(path1, path2);

        verify(mockFileSystemProvider, times(1)).createLink(path1.wrapped(), path2.wrapped());
    }

    @Test
    public void delete() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.delete(path);

        verify(mockFileSystemProvider, times(1)).delete(path.wrapped());
    }

    @Test
    public void deleteIfExists() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.deleteIfExists(path);

        verify(mockFileSystemProvider, times(1)).deleteIfExists(path.wrapped());
    }

    @Test
    public void readSymbolicLink() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.readSymbolicLink(path);

        verify(mockFileSystemProvider, times(1)).readSymbolicLink(path.wrapped());
    }

    @Test
    public void copy() throws Exception {
        final WrappedPath path1 = givenMockWrappedPath();
        final WrappedPath path2 = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.copy(path1, path2);

        verify(mockFileSystemProvider, times(1)).copy(path1.wrapped(), path2.wrapped());
    }

    @Test
    public void move() throws Exception {
        final WrappedPath path1 = givenMockWrappedPath();
        final WrappedPath path2 = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.move(path1, path2);

        verify(mockFileSystemProvider, times(1)).move(path1.wrapped(), path2.wrapped());
    }

    @Test
    public void isSameFile() throws Exception {
        final WrappedPath path1 = givenMockWrappedPath();
        final WrappedPath path2 = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.isSameFile(path1, path2);

        verify(mockFileSystemProvider, times(1)).isSameFile(path1.wrapped(), path2.wrapped());
    }

    @Test
    public void isHidden() throws Exception {
        final WrappedPath path1 = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.isHidden(path1);

        verify(mockFileSystemProvider, times(1)).isHidden(path1.wrapped());
    }

    @Test
    public void getFileStore() throws Exception {
        final WrappedPath path1 = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.getFileStore(path1);

        verify(mockFileSystemProvider, times(1)).getFileStore(path1.wrapped());
    }

    @Test
    public void checkAccess() throws Exception {
        final WrappedPath path1 = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.checkAccess(path1);

        verify(mockFileSystemProvider, times(1)).checkAccess(path1.wrapped());
    }

    @Test
    public void getFileAttributeView() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.getFileAttributeView(path, AclFileAttributeView.class, NOFOLLOW_LINKS);

        verify(mockFileSystemProvider, times(1)).getFileAttributeView(path.wrapped(), AclFileAttributeView.class, NOFOLLOW_LINKS);
    }

    @Test
    public void readAttributes() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.readAttributes(path, TestFileAttributes.class, NOFOLLOW_LINKS);

        verify(mockFileSystemProvider, times(1)).readAttributes(path.wrapped(), TestFileAttributes.class, NOFOLLOW_LINKS);
    }

    @Test
    public void readAttributes1() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.readAttributes(path, "abc", NOFOLLOW_LINKS);

        verify(mockFileSystemProvider, times(1)).readAttributes(path.wrapped(), "abc", NOFOLLOW_LINKS);
    }

    @Test
    public void setAttribute() throws Exception {
        final WrappedPath path = givenMockWrappedPath();
        final FileSystemProvider mockFileSystemProvider = givenMockFileSystemProvider();
        final WrappedFileSystemProvider<WrappedPath> instance = givenInstanceFor(mockFileSystemProvider);

        instance.setAttribute(path, "abc", NOFOLLOW_LINKS);

        verify(mockFileSystemProvider, times(1)).setAttribute(path.wrapped(), "abc", NOFOLLOW_LINKS);
    }

    @Nonnull
    protected static WrappedFileSystemProvider<WrappedPath> givenInstanceFor(FileSystemProvider mockFileSystemProvider) {
        return new WrappedFileSystemProvider<>(WrappedPath.class, mockFileSystemProvider);
    }

    @Nonnull
    protected static FileSystemProvider givenMockFileSystemProvider() {
        return mock(FileSystemProvider.class);
    }

    @Nonnull
    protected static WrappedPath givenMockWrappedPath() {
        final WrappedPath mock = mock(WrappedPath.class);
        doReturn(mock(Path.class)).when(mock).wrapped();
        return mock;
    }

    @Nonnull
    protected static ExecutorService givenMockExecutorService() {
        return mock(ExecutorService.class);
    }

    @Nonnull
    protected static Filter<? super Path> givenFilter() {
        //noinspection unchecked
        return mock(Filter.class);
    }

    @Nonnull
    protected static FileAttribute<?>[] givenFileAttributes() {
        return new FileAttribute<?>[]{};
    }

    public static interface TestFileAttributes extends BasicFileAttributes {}

}
