package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.Interceptor.InterceptorEnabled;
import org.echocat.unittest.utils.utils.Wrapping;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.close;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.getFileStores;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.getPath;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.getPathMatcher;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.getRootDirectories;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.getSeparator;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.getUserPrincipalLookupService;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.isOpen;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.isReadOnly;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.newWatchService;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.provider;
import static org.echocat.unittest.utils.nio.EventType.FileSystems.supportedFileAttributeViews;
import static org.echocat.unittest.utils.nio.WrappedExecution.withResult;
import static org.echocat.unittest.utils.nio.WrappedExecution.withoutResult;
import static org.echocat.unittest.utils.nio.WrappedPath.wrap;

public class WrappedFileSystem<T extends WrappedPath> extends FileSystem implements Wrapping<FileSystem>, InterceptorEnabled {

    @Nonnull
    private final Class<T> wrappedPathType;
    @Nonnull
    private final FileSystem wrapped;
    @Nonnull
    private final Optional<Interceptor> interceptor;

    public WrappedFileSystem(@Nonnull Class<T> wrappedPathType, @Nonnull FileSystem wrapped) {
        this(wrappedPathType, wrapped, null);
    }

    public WrappedFileSystem(@Nonnull Class<T> wrappedPathType, @Nonnull FileSystem wrapped, @Nullable Interceptor interceptor) {
        this.wrappedPathType = wrappedPathType;
        this.wrapped = wrapped;
        this.interceptor = ofNullable(interceptor);
    }

    @Nonnull
    @Override
    public Optional<Interceptor> interceptor() {
        return interceptor;
    }

    @Override
    @Nonnull
    public FileSystem wrapped() {
        return wrapped;
    }

    @Nonnull
    protected WrappedPath rewrap(@Nonnull Path original) {
        return wrap(original, interceptor().orElse(null));
    }

    @Override
    public FileSystemProvider provider() {
        return withResult(this, provider, wrapped ->
            new WrappedFileSystemProvider<>(wrappedPathType, wrapped().provider(), interceptor().orElse(null))
        );
    }

    @Override
    public void close() throws IOException {
        withoutResult(this, close, IOException.class,
            FileSystem::close
        );
    }

    @Override
    public boolean isOpen() {
        return withResult(this, isOpen,
            FileSystem::isOpen
        );
    }

    @Override
    public boolean isReadOnly() {
        return withResult(this, isReadOnly,
            FileSystem::isReadOnly
        );
    }

    @Override
    public String getSeparator() {
        return withResult(this, getSeparator,
            FileSystem::getSeparator
        );
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        return withResult(this, getRootDirectories,
            FileSystem::getRootDirectories
        );
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        return withResult(this, getFileStores,
            FileSystem::getFileStores
        );
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        return withResult(this, supportedFileAttributeViews,
            FileSystem::supportedFileAttributeViews
        );
    }

    @Override
    public Path getPath(String first, String... more) {
        return rewrap(withResult(this, getPath,
            wrapped -> wrapped.getPath(first, more)
            , first, more));
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        return withResult(this, getPathMatcher,
            wrapped -> wrapped.getPathMatcher(syntaxAndPattern)
            , syntaxAndPattern);
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return withResult(this, getUserPrincipalLookupService,
            FileSystem::getUserPrincipalLookupService
        );
    }

    @Override
    public WatchService newWatchService() throws IOException {
        return withResult(this, newWatchService, IOException.class,
            FileSystem::newWatchService
        );
    }
}
