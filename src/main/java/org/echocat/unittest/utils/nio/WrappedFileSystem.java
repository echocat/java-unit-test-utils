package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

public class WrappedFileSystem<T extends WrappedPath> extends FileSystem {

    @Nonnull
    private final Class<T> wrappedPathType;
    @Nonnull
    private final FileSystem wrapped;

    public WrappedFileSystem(@Nonnull Class<T> wrappedPathType, @Nonnull FileSystem wrapped) {
        this.wrappedPathType = wrappedPathType;
        this.wrapped = wrapped;
    }

    @Nonnull
    public FileSystem wrapped() {
        return wrapped;
    }

    @Override
    public FileSystemProvider provider() {
        return new WrappedFileSystemProvider<>(wrappedPathType, wrapped().provider());
    }

    @Override
    public void close() throws IOException {wrapped().close();}

    @Override
    public boolean isOpen() {return wrapped().isOpen();}

    @Override
    public boolean isReadOnly() {return wrapped().isReadOnly();}

    @Override
    public String getSeparator() {return wrapped().getSeparator();}

    @Override
    public Iterable<Path> getRootDirectories() {return wrapped().getRootDirectories();}

    @Override
    public Iterable<FileStore> getFileStores() {return wrapped().getFileStores();}

    @Override
    public Set<String> supportedFileAttributeViews() {return wrapped().supportedFileAttributeViews();}

    @Override
    public Path getPath(String first, String... more) {return wrapped().getPath(first, more);}

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {return wrapped().getPathMatcher(syntaxAndPattern);}

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {return wrapped().getUserPrincipalLookupService();}

    @Override
    public WatchService newWatchService() throws IOException {return wrapped().newWatchService();}
}
