package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.WrappedEvent.Interceptor;
import org.echocat.unittest.utils.nio.WrappedEvent.InterceptorEnabled;
import org.echocat.unittest.utils.nio.WrappedEvent.Type;

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
import static org.echocat.unittest.utils.nio.WrappedEvent.eventTypeOf;
import static org.echocat.unittest.utils.nio.WrappedExecution.withResult;
import static org.echocat.unittest.utils.nio.WrappedExecution.withoutResult;

@SuppressWarnings("FieldNamingConvention")
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

    public static final Type ET_provider = eventTypeOf(WrappedFileSystem.class, "provider");

    @Override
    public FileSystemProvider provider() {
        return withResult(this, ET_provider, wrapped ->
            new WrappedFileSystemProvider<>(wrappedPathType, wrapped().provider(), interceptor().orElse(null))
        );
    }

    public static final Type ET_close = eventTypeOf(WrappedFileSystem.class, "close");

    @Override
    public void close() throws IOException {
        withoutResult(this, ET_close, IOException.class,
            FileSystem::close
        );
    }

    public static final Type ET_isOpen = eventTypeOf(WrappedFileSystem.class, "isOpen");

    @Override
    public boolean isOpen() {
        return withResult(this, ET_isOpen,
            FileSystem::isOpen
        );
    }

    public static final Type ET_isReadOnly = eventTypeOf(WrappedFileSystem.class, "isReadOnly");

    @Override
    public boolean isReadOnly() {
        return withResult(this, ET_isReadOnly,
            FileSystem::isReadOnly
        );
    }

    public static final Type ET_getSeparator = eventTypeOf(WrappedFileSystem.class, "getSeparator");

    @Override
    public String getSeparator() {
        return withResult(this, ET_getSeparator,
            FileSystem::getSeparator
        );
    }

    public static final Type ET_getRootDirectories = eventTypeOf(WrappedFileSystem.class, "getRootDirectories");

    @Override
    public Iterable<Path> getRootDirectories() {
        return withResult(this, ET_getRootDirectories,
            FileSystem::getRootDirectories
        );
    }

    public static final Type ET_getFileStores = eventTypeOf(WrappedFileSystem.class, "getFileStores");

    @Override
    public Iterable<FileStore> getFileStores() {
        return withResult(this, ET_getFileStores,
            FileSystem::getFileStores
        );
    }

    public static final Type ET_supportedFileAttributeViews = eventTypeOf(WrappedFileSystem.class, "supportedFileAttributeViews");

    @Override
    public Set<String> supportedFileAttributeViews() {
        return withResult(this, ET_supportedFileAttributeViews,
            FileSystem::supportedFileAttributeViews
        );
    }

    public static final Type ET_getPath = eventTypeOf(WrappedFileSystem.class, "getPath", String.class, String[].class);

    @Override
    public Path getPath(String first, String... more) {
        return withResult(this, ET_getPath,
            wrapped -> wrapped.getPath(first, more)
            , first, more);
    }

    public static final Type ET_getPathMatcher = eventTypeOf(WrappedFileSystem.class, "getPathMatcher", String.class);

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        return withResult(this, ET_getPathMatcher,
            wrapped -> wrapped.getPathMatcher(syntaxAndPattern)
            , syntaxAndPattern);
    }

    public static final Type ET_getUserPrincipalLookupService = eventTypeOf(WrappedFileSystem.class, "getUserPrincipalLookupService");

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return withResult(this, ET_getUserPrincipalLookupService,
            FileSystem::getUserPrincipalLookupService
        );
    }

    public static final Type ET_newWatchService = eventTypeOf(WrappedFileSystem.class, "newWatchService");

    @Override
    public WatchService newWatchService() throws IOException {
        return withResult(this, ET_newWatchService, IOException.class,
            FileSystem::newWatchService
        );
    }
}
