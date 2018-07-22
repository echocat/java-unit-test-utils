package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.WrappedEvent.Interceptor;
import org.echocat.unittest.utils.nio.WrappedEvent.InterceptorEnabled;
import org.echocat.unittest.utils.nio.WrappedEvent.Type;

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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static java.util.Optional.ofNullable;
import static org.echocat.unittest.utils.nio.WrappedEvent.eventTypeOf;
import static org.echocat.unittest.utils.nio.WrappedExecution.withResult;
import static org.echocat.unittest.utils.nio.WrappedExecution.withoutResult;

@SuppressWarnings("FieldNamingConvention")
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

    public static final Type ET_getScheme = eventTypeOf(WrappedFileSystemProvider.class, "getScheme");

    @Override
    public String getScheme() {
        return withResult(this, ET_getScheme,
            FileSystemProvider::getScheme
        );
    }

    public static final Type ET_newFileSystem_URI = eventTypeOf(WrappedFileSystemProvider.class, "newFileSystem", URI.class, Map.class);

    @Override
    public FileSystem newFileSystem(final URI uri, final Map<String, ?> env) throws IOException {
        return withResult(this, ET_newFileSystem_URI, IOException.class,
            wrapped -> new WrappedFileSystem<>(
                wrappedPathType,
                wrapped.newFileSystem(uri, env),
                interceptor().orElse(null)
            )
            , uri, env);
    }

    public static final Type ET_getFileSystem_URI = eventTypeOf(WrappedFileSystemProvider.class, "getFileSystem", URI.class);

    @Override
    public FileSystem getFileSystem(final URI uri) {
        return withResult(this, ET_getFileSystem_URI,
            wrapped -> new WrappedFileSystem<>(
                wrappedPathType,
                wrapped.getFileSystem(uri),
                interceptor().orElse(null)
            )
            , uri);
    }

    public static final Type ET_getPath = eventTypeOf(WrappedFileSystemProvider.class, "getPath", URI.class);

    @Override
    public Path getPath(final URI uri) {
        return withResult(this, ET_getPath,
            wrapped -> wrapped.getPath(uri)
            , uri);
    }

    public static final Type ET_newFileSystem = eventTypeOf(WrappedFileSystemProvider.class, "newFileSystem", Path.class, Map.class);

    @Override
    public FileSystem newFileSystem(final Path path, final Map<String, ?> env) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_newFileSystem, IOException.class,
            wrapped -> wrapped.newFileSystem(unwrapped, env)
            , unwrapped, env);
    }

    public static final Type ET_newInputStream = eventTypeOf(WrappedFileSystemProvider.class, "newInputStream", Path.class, OpenOption[].class);

    @Override
    public InputStream newInputStream(final Path path, final OpenOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_newInputStream, IOException.class,
            wrapped -> wrapped.newInputStream(unwrapped, options)
            , unwrapped, options);
    }

    public static final Type ET_newOutputStream = eventTypeOf(WrappedFileSystemProvider.class, "newOutputStream", Path.class, OpenOption[].class);

    @Override
    public OutputStream newOutputStream(final Path path, final OpenOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_newOutputStream, IOException.class,
            wrapped -> wrapped.newOutputStream(unwrapped, options)
            , unwrapped, options);
    }

    public static final Type ET_newFileChannel = eventTypeOf(WrappedFileSystemProvider.class, "newFileChannel", Path.class, Set.class, FileAttribute[].class);

    @Override
    public FileChannel newFileChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_newFileChannel, IOException.class,
            wrapped -> wrapped.newFileChannel(unwrapped, options)
            , unwrapped, options);
    }

    public static final Type ET_newAsynchronousFileChannel = eventTypeOf(WrappedFileSystemProvider.class, "newAsynchronousFileChannel", Path.class, Set.class, ExecutorService.class, FileAttribute[].class);

    @Override
    public AsynchronousFileChannel newAsynchronousFileChannel(final Path path, final Set<? extends OpenOption> options, final ExecutorService executor, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_newAsynchronousFileChannel, IOException.class,
            wrapped -> wrapped.newAsynchronousFileChannel(unwrapped, options, executor, attrs)
            , unwrapped, options, executor, attrs);
    }

    public static final Type ET_newByteChannel = eventTypeOf(WrappedFileSystemProvider.class, "newByteChannel", Path.class, Set.class, FileAttribute[].class);

    @Override
    public SeekableByteChannel newByteChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_newByteChannel, IOException.class,
            wrapped -> wrapped.newByteChannel(unwrapped, options)
            , unwrapped, options);
    }

    public static final Type ET_newDirectoryStream = eventTypeOf(WrappedFileSystemProvider.class, "newDirectoryStream", Path.class, Filter.class);

    @Override
    public DirectoryStream<Path> newDirectoryStream(final Path dir, final Filter<? super Path> filter) throws IOException {
        final Path unwrapped = unwrap(dir);
        return withResult(this, ET_newDirectoryStream, IOException.class,
            wrapped -> wrapped.newDirectoryStream(unwrapped, filter)
            , unwrapped, filter);
    }

    public static final Type ET_createDirectory = eventTypeOf(WrappedFileSystemProvider.class, "createDirectory", Path.class, FileAttribute[].class);

    @Override
    public void createDirectory(final Path dir, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrapped = unwrap(dir);
        withoutResult(this, ET_createDirectory, IOException.class,
            wrapped -> wrapped.createDirectory(unwrapped, attrs)
            , unwrapped, attrs);
    }

    public static final Type ET_createSymbolicLink = eventTypeOf(WrappedFileSystemProvider.class, "createSymbolicLink", Path.class, Path.class, FileAttribute[].class);

    @Override
    public void createSymbolicLink(final Path link, final Path target, final FileAttribute<?>[] attrs) throws IOException {
        final Path unwrappedLink = unwrap(link);
        final Path unwrappedTarget = unwrap(target);
        withoutResult(this, ET_createSymbolicLink, IOException.class,
            wrapped -> wrapped.createSymbolicLink(unwrappedLink, unwrappedTarget, attrs)
            , unwrappedLink, unwrappedTarget, attrs);
    }

    public static final Type ET_createLink = eventTypeOf(WrappedFileSystemProvider.class, "createLink", Path.class, Path.class);

    @Override
    public void createLink(final Path link, final Path existing) throws IOException {
        final Path unwrappedLink = unwrap(link);
        final Path unwrappedExisting = unwrap(existing);
        withoutResult(this, ET_createLink, IOException.class,
            wrapped -> wrapped.createLink(unwrappedLink, unwrappedExisting)
            , unwrappedLink, unwrappedExisting);
    }

    public static final Type ET_delete = eventTypeOf(WrappedFileSystemProvider.class, "delete", Path.class);

    @Override
    public void delete(final Path path) throws IOException {
        final Path unwrapped = unwrap(path);
        withoutResult(this, ET_delete, IOException.class,
            wrapped -> wrapped.delete(unwrapped)
            , unwrapped);
    }

    public static final Type ET_deleteIfExists = eventTypeOf(WrappedFileSystemProvider.class, "deleteIfExists", Path.class);

    @Override
    public boolean deleteIfExists(final Path path) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_deleteIfExists, IOException.class,
            wrapped -> wrapped.deleteIfExists(unwrapped)
            , unwrapped);
    }

    public static final Type ET_readSymbolicLink = eventTypeOf(WrappedFileSystemProvider.class, "readSymbolicLink", Path.class);

    @Override
    public Path readSymbolicLink(final Path link) throws IOException {
        final Path unwrapped = unwrap(link);
        return withResult(this, ET_readSymbolicLink, IOException.class,
            wrapped -> wrapped.readSymbolicLink(unwrapped)
            , unwrapped);
    }

    public static final Type ET_copy = eventTypeOf(WrappedFileSystemProvider.class, "copy", Path.class, Path.class, CopyOption[].class);

    @Override
    public void copy(final Path source, final Path target, final CopyOption... options) throws IOException {
        final Path unwrappedSource = unwrap(source);
        final Path unwrappedTarget = unwrap(target);
        withoutResult(this, ET_copy, IOException.class,
            wrapped -> wrapped.copy(unwrappedSource, unwrappedTarget, options)
            , unwrappedSource, unwrappedTarget, options);
    }

    public static final Type ET_move = eventTypeOf(WrappedFileSystemProvider.class, "move", Path.class, Path.class, CopyOption[].class);

    @Override
    public void move(final Path source, final Path target, final CopyOption... options) throws IOException {
        final Path unwrappedSource = unwrap(source);
        final Path unwrappedTarget = unwrap(target);
        withoutResult(this, ET_move, IOException.class,
            wrapped -> wrapped.move(unwrappedSource, unwrappedTarget, options)
            , unwrappedSource, unwrappedTarget, options);
    }

    public static final Type ET_isSameFile = eventTypeOf(WrappedFileSystemProvider.class, "isSameFile", Path.class, Path.class);

    @Override
    public boolean isSameFile(final Path path, final Path path2) throws IOException {
        final Path unwrapped1 = unwrap(path);
        final Path unwrapped2 = unwrap(path2);
        return withResult(this, ET_isSameFile, IOException.class,
            wrapped -> wrapped.isSameFile(unwrapped1, unwrapped2)
            , unwrapped1, unwrapped2);
    }

    public static final Type ET_isHidden = eventTypeOf(WrappedFileSystemProvider.class, "isHidden", Path.class);

    @Override
    public boolean isHidden(final Path path) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_isHidden, IOException.class,
            wrapped -> wrapped.isHidden(unwrapped)
            , unwrapped);
    }

    public static final Type ET_getFileStore = eventTypeOf(WrappedFileSystemProvider.class, "getFileStore", Path.class);

    @Override
    public FileStore getFileStore(final Path path) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_getFileStore, IOException.class,
            wrapped -> wrapped.getFileStore(unwrapped)
            , unwrapped);
    }

    public static final Type ET_checkAccess = eventTypeOf(WrappedFileSystemProvider.class, "checkAccess", Path.class, AccessMode[].class);

    @Override
    public void checkAccess(final Path path, final AccessMode... modes) throws IOException {
        final Path unwrapped = unwrap(path);
        withoutResult(this, ET_checkAccess, IOException.class,
            wrapped -> wrapped.checkAccess(unwrapped, modes)
            , unwrapped, modes);
    }

    public static final Type ET_getFileAttributeView = eventTypeOf(WrappedFileSystemProvider.class, "getFileAttributeView", Path.class, Class.class, LinkOption[].class);

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(final Path path, final Class<V> type, final LinkOption... options) {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_getFileAttributeView,
            wrapped -> wrapped.getFileAttributeView(unwrapped, type, options)
            , unwrapped, type, options);
    }

    public static final Type ET_readAttributes_checked = eventTypeOf(WrappedFileSystemProvider.class, "readAttributes", Path.class, Class.class, LinkOption[].class);

    @Override
    public <A extends BasicFileAttributes> A readAttributes(final Path path, final Class<A> type, final LinkOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_readAttributes_checked, IOException.class,
            wrapped -> wrapped.readAttributes(unwrapped, type, options)
            , unwrapped, type, options);
    }

    public static final Type ET_readAttributes = eventTypeOf(WrappedFileSystemProvider.class, "readAttributes", Path.class, String.class, LinkOption[].class);

    @Override
    public Map<String, Object> readAttributes(final Path path, final String attributes, final LinkOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        return withResult(this, ET_readAttributes, IOException.class,
            wrapped -> wrapped.readAttributes(unwrapped, attributes, options)
            , unwrapped, attributes, options);
    }

    public static final Type ET_setAttribute = eventTypeOf(WrappedFileSystemProvider.class, "setAttribute", Path.class, String.class, Object.class, LinkOption[].class);

    @Override
    public void setAttribute(final Path path, final String attribute, final Object value, final LinkOption... options) throws IOException {
        final Path unwrapped = unwrap(path);
        withoutResult(this, ET_setAttribute, IOException.class,
            wrapped -> wrapped.setAttribute(unwrapped, attribute, value, options)
            , unwrapped, attribute, value, options);
    }
}
