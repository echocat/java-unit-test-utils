package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.Interceptor.InterceptorEnabled;
import org.echocat.unittest.utils.utils.Wrapping;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static java.util.Optional.ofNullable;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.checkAccess;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.copy;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.createDirectory;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.createLink;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.createSymbolicLink;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.delete;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.deleteIfExists;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.getFileAttributeView;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.getFileStore;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.getFileSystem_URI;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.getPath;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.getScheme;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.isHidden;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.isSameFile;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.move;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.newAsynchronousFileChannel;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.newByteChannel;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.newDirectoryStream;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.newFileChannel;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.newFileSystem;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.newFileSystem_URI;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.newInputStream;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.newOutputStream;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.readAttributes;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.readAttributes_checked;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.readSymbolicLink;
import static org.echocat.unittest.utils.nio.EventType.FileSystemProviders.setAttribute;
import static org.echocat.unittest.utils.nio.WrappedExecution.withResult;
import static org.echocat.unittest.utils.nio.WrappedExecution.withoutResult;

public class WrappedFileSystemProvider<T extends WrappedPath> extends FileSystemProvider implements Wrapping<FileSystemProvider>, InterceptorEnabled {

    @Nonnull
    private final Class<T> wrappedPathType;
    @Nonnull
    private final FileSystemProvider wrapped;
    @Nonnull
    private final Optional<Interceptor> interceptor;

    protected WrappedFileSystemProvider(@Nonnull Class<T> wrappedPathType, @Nonnull FileSystemProvider wrapped, @Nullable Interceptor interceptor) {
        this.wrappedPathType = wrappedPathType;
        this.wrapped = wrapped;
        this.interceptor = ofNullable(interceptor);
    }

    @Override
    @Nonnull
    public FileSystemProvider wrapped() {
        return wrapped;
    }

    @Nonnull
    protected Class<T> wrappedPathType() {
        return wrappedPathType;
    }

    @Nonnull
    @Override
    public Optional<Interceptor> interceptor() {
        return interceptor;
    }

    @Nonnull
    protected Path unwrap(@Nullable Path plain) {
        if (plain == null) {
            throw new NullPointerException("No path provided.");
        }
        if (!wrappedPathType.isInstance(plain)) {
            throw new IllegalArgumentException("Could only handle paths of type '" + wrappedPathType.getName() + "'" +
                " but got an instance of type '" + plain.getClass().getName() + "'.");
        }
        return wrappedPathType.cast(plain).wrapped();
    }

    @Nonnull
    protected WrappedPath wrap(@Nonnull Path original) {
        if (original instanceof WrappedPath) {
            return (WrappedPath) original;
        }
        return WrappedPath.create(original, interceptor().orElse(null));
    }

    @Override
    public String getScheme() {
        return withResult(this, getScheme,
            FileSystemProvider::getScheme
        );
    }

    @Override
    public FileSystem newFileSystem(final URI uri, final Map<String, ?> env) throws IOException {
        return withResult(this, newFileSystem_URI, IOException.class,
            wrapped -> new WrappedFileSystem<>(
                wrappedPathType,
                wrapped.newFileSystem(uri, env),
                interceptor().orElse(null)
            )
            , uri, env);
    }

    @Override
    public FileSystem getFileSystem(final URI uri) {
        return withResult(this, getFileSystem_URI,
            wrapped -> new WrappedFileSystem<>(
                wrappedPathType,
                wrapped.getFileSystem(uri),
                interceptor().orElse(null)
            )
            , uri);
    }

    @Override
    public Path getPath(final URI uri) {
        return wrap(withResult(this, getPath,
            wrapped -> wrapped.getPath(uri)
            , uri));
    }

    @Override
    public FileSystem newFileSystem(final Path path, final Map<String, ?> env) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, newFileSystem, IOException.class,
            wrapped -> wrapped.newFileSystem(unwrapped, env)
            , unwrapped, env);
    }

    @Override
    public InputStream newInputStream(final Path path, final OpenOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, newInputStream, IOException.class,
            wrapped -> wrapped.newInputStream(unwrapped, options)
            , unwrapped, options);
    }

    @Override
    public OutputStream newOutputStream(final Path path, final OpenOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, newOutputStream, IOException.class,
            wrapped -> wrapped.newOutputStream(unwrapped, options)
            , unwrapped, options);
    }

    @Override
    public FileChannel newFileChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, newFileChannel, IOException.class,
            wrapped -> wrapped.newFileChannel(unwrapped, options)
            , unwrapped, options);
    }

    @Override
    public AsynchronousFileChannel newAsynchronousFileChannel(final Path path, final Set<? extends OpenOption> options, final ExecutorService executor, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, newAsynchronousFileChannel, IOException.class,
            wrapped -> wrapped.newAsynchronousFileChannel(unwrapped, options, executor, attrs)
            , unwrapped, options, executor, attrs);
    }

    @Override
    public SeekableByteChannel newByteChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, newByteChannel, IOException.class,
            wrapped -> wrapped.newByteChannel(unwrapped, options)
            , unwrapped, options);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(final Path dir, final Filter<? super Path> filter) throws IOException {
        final Path unwrapped = unwrap(dir);
        return new WrappedDirectoryStream(withResult(this, newDirectoryStream, IOException.class,
            wrapped -> wrapped.newDirectoryStream(unwrapped, filter)
            , unwrapped, filter));
    }

    @Override
    public void createDirectory(final Path dir, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrapped = unwrap(dir);
        withoutResult(this, createDirectory, IOException.class,
            wrapped -> wrapped.createDirectory(unwrapped, attrs)
            , unwrapped, attrs);
    }

    @Override
    public void createSymbolicLink(final Path link, final Path target, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrappedLink = unwrap(link);
        final Path unwrappedTarget = unwrap(target);
        withoutResult(this, createSymbolicLink, IOException.class,
            wrapped -> wrapped.createSymbolicLink(unwrappedLink, unwrappedTarget, attrs)
            , unwrappedLink, unwrappedTarget, attrs);
    }

    @Override
    public void createLink(final Path link, final Path existing) throws IOException {
        final Path unwrappedLink = unwrap(link);
        final Path unwrappedExisting = unwrap(existing);
        withoutResult(this, createLink, IOException.class,
            wrapped -> wrapped.createLink(unwrappedLink, unwrappedExisting)
            , unwrappedLink, unwrappedExisting);
    }

    @Override
    public void delete(final Path path) throws IOException {
        final Path unwrapped = unwrap(path);
        withoutResult(this, delete, IOException.class,
            wrapped -> wrapped.delete(unwrapped)
            , unwrapped);
    }

    @Override
    public boolean deleteIfExists(final Path path) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, deleteIfExists, IOException.class,
            wrapped -> wrapped.deleteIfExists(unwrapped)
            , unwrapped);
    }

    @Override
    public Path readSymbolicLink(final Path link) throws IOException {
        final Path unwrapped = unwrap(link);
        return wrap(withResult(this, readSymbolicLink, IOException.class,
            wrapped -> wrapped.readSymbolicLink(unwrapped)
            , unwrapped));
    }

    @Override
    public void copy(final Path source, final Path target, final CopyOption... options) throws IOException {
        final Path unwrappedSource = unwrap(source);
        final Path unwrappedTarget = unwrap(target);
        withoutResult(this, copy, IOException.class,
            wrapped -> wrapped.copy(unwrappedSource, unwrappedTarget, options)
            , unwrappedSource, unwrappedTarget, options);
    }

    @Override
    public void move(final Path source, final Path target, final CopyOption... options) throws IOException {
        final Path unwrappedSource = unwrap(source);
        final Path unwrappedTarget = unwrap(target);
        withoutResult(this, move, IOException.class,
            wrapped -> wrapped.move(unwrappedSource, unwrappedTarget, options)
            , unwrappedSource, unwrappedTarget, options);
    }

    @Override
    public boolean isSameFile(final Path path, final Path path2) throws IOException {
        final Path unwrapped1 = unwrap(path);
        final Path unwrapped2 = unwrap(path2);
        return withResult(this, isSameFile, IOException.class,
            wrapped -> wrapped.isSameFile(unwrapped1, unwrapped2)
            , unwrapped1, unwrapped2);
    }

    @Override
    public boolean isHidden(final Path path) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, isHidden, IOException.class,
            wrapped -> wrapped.isHidden(unwrapped)
            , unwrapped);
    }

    @Override
    public FileStore getFileStore(final Path path) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, getFileStore, IOException.class,
            wrapped -> wrapped.getFileStore(unwrapped)
            , unwrapped);
    }

    @Override
    public void checkAccess(final Path path, final AccessMode... modes) throws IOException {
        final Path unwrapped = unwrap(path);
        withoutResult(this, checkAccess, IOException.class,
            wrapped -> wrapped.checkAccess(unwrapped, modes)
            , unwrapped, modes);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(final Path path, final Class<V> type, final LinkOption... options) {
        final Path unwrapped = unwrap(path);
        return withResult(this, getFileAttributeView,
            wrapped -> wrapped.getFileAttributeView(unwrapped, type, options)
            , unwrapped, type, options);
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(final Path path, final Class<A> type, final LinkOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, readAttributes_checked, IOException.class,
            wrapped -> wrapped.readAttributes(unwrapped, type, options)
            , unwrapped, type, options);
    }

    @Override
    public Map<String, Object> readAttributes(final Path path, final String attributes, final LinkOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, readAttributes, IOException.class,
            wrapped -> wrapped.readAttributes(unwrapped, attributes, options)
            , unwrapped, attributes, options);
    }

    @Override
    public void setAttribute(final Path path, final String attribute, final Object value, final LinkOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        withoutResult(this, setAttribute, IOException.class,
            wrapped -> wrapped.setAttribute(unwrapped, attribute, value, options)
            , unwrapped, attribute, value, options);
    }

    protected class WrappedDirectoryStream implements DirectoryStream<Path> {

        private final DirectoryStream<Path> delegate;

        protected WrappedDirectoryStream(@Nonnull DirectoryStream<Path> delegate) {
            this.delegate = delegate;
        }

        @Nonnull
        @Override
        public Iterator<Path> iterator() {
            final Iterator<Path> delegate = this.delegate.iterator();
            return new Iterator<Path>() {
                @Override
                public boolean hasNext() {
                    return delegate.hasNext();
                }

                @Override
                public Path next() {
                    return wrap(delegate.next());
                }
            };
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }

    }

}
