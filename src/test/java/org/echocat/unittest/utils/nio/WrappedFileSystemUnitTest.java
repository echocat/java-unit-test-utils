package org.echocat.unittest.utils.nio;

import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.nio.file.FileSystem;
import java.nio.file.spi.FileSystemProvider;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class WrappedFileSystemUnitTest {

    @Test
    void wrapped() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        assertThat(instance.wrapped(), sameInstance(mockFileSystem));
    }

    @Test
    void provider() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        final FileSystemProvider actual = instance.provider();

        assertThat(actual, instanceOf(WrappedFileSystemProvider.class));
        assertThat(((WrappedFileSystemProvider<?>) actual).wrappedPathType(), sameInstance(WrappedPath.class));
        assertThat(((WrappedFileSystemProvider<?>) actual).wrapped(), sameInstance(mockFileSystem.provider()));
    }

    @Test
    void close() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.close();

        verify(mockFileSystem, times(1)).close();
    }

    @Test
    void isOpen() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.isOpen();

        verify(mockFileSystem, times(1)).isOpen();
    }

    @Test
    void isReadOnly() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.isReadOnly();

        verify(mockFileSystem, times(1)).isReadOnly();
    }

    @Test
    void getSeparator() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.getSeparator();

        verify(mockFileSystem, times(1)).getSeparator();
    }

    @Test
    void getRootDirectories() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.getRootDirectories();

        verify(mockFileSystem, times(1)).getRootDirectories();
    }

    @Test
    void getFileStores() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.getFileStores();

        verify(mockFileSystem, times(1)).getFileStores();
    }

    @Test
    void supportedFileAttributeViews() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.supportedFileAttributeViews();

        verify(mockFileSystem, times(1)).supportedFileAttributeViews();
    }

    @Test
    void getPath() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);
        final String path1 = "1";
        final String[] otherPaths = {"2", "3"};

        instance.getPath(path1, otherPaths);

        verify(mockFileSystem, times(1)).getPath(path1, otherPaths);
    }

    @Test
    void getPathMatcher() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);
        final String syntax = "1";

        instance.getPathMatcher(syntax);

        verify(mockFileSystem, times(1)).getPathMatcher(syntax);
    }

    @Test
    void getUserPrincipalLookupService() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.getUserPrincipalLookupService();

        verify(mockFileSystem, times(1)).getUserPrincipalLookupService();
    }

    @Test
    void newWatchService() throws Exception {
        final FileSystem mockFileSystem = givenMockFileSystem();
        final WrappedFileSystem<WrappedPath> instance = givenInstanceFor(mockFileSystem);

        instance.newWatchService();

        verify(mockFileSystem, times(1)).newWatchService();
    }

    @Nonnull
    private static WrappedFileSystem<WrappedPath> givenInstanceFor(FileSystem mockFileSystem) {
        return new WrappedFileSystem<>(WrappedPath.class, mockFileSystem);
    }

    @Nonnull
    private static FileSystem givenMockFileSystem() {
        final FileSystem instance = mock(FileSystem.class);
        doReturn(mock(FileSystemProvider.class)).when(instance).provider();
        return instance;
    }

}
