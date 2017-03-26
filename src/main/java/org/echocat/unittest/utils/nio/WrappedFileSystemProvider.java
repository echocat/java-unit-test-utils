package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class WrappedFileSystemProvider<T extends WrappedPath> extends FileSystemProvider {

    @Nonnull
    private final Class<T> wrappedPathType;
    @Nonnull
    private final FileSystemProvider wrapped;

    protected WrappedFileSystemProvider(@Nonnull Class<T> wrappedPathType, @Nonnull FileSystemProvider wrapped) {
        this.wrappedPathType = wrappedPathType;
        this.wrapped = wrapped;
    }

    @Nonnull
    public FileSystemProvider wrapped() {
        return wrapped;
    }

    @Nonnull
    protected Class<T> wrappedPathType() {
        return wrappedPathType;
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

    @Override
    public String getScheme() {return wrapped().getScheme();}

    @Override
    public FileSystem newFileSystem(final URI uri, final Map<String, ?> env) throws IOException {
        return new WrappedFileSystem<>(wrappedPathType, wrapped().newFileSystem(uri, env));
    }

    @Override
    public FileSystem getFileSystem(final URI uri) {
        return new WrappedFileSystem<>(wrappedPathType, wrapped().getFileSystem(uri));
    }

    @Override
    public Path getPath(final URI uri) {return wrapped().getPath(uri);}

    @Override
    public FileSystem newFileSystem(final Path path, final Map<String, ?> env) throws IOException {return wrapped().newFileSystem(unwrap(path), env);}

    @Override
    public InputStream newInputStream(final Path path, final OpenOption... options) throws IOException {return wrapped().newInputStream(unwrap(path), options);}

    @Override
    public OutputStream newOutputStream(final Path path, final OpenOption... options) throws IOException {return wrapped().newOutputStream(unwrap(path), options);}

    @Override
    public FileChannel newFileChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>[] attrs) throws IOException {
        return wrapped().newFileChannel(unwrap(path), options, attrs);
    }

    @Override
    public AsynchronousFileChannel newAsynchronousFileChannel(final Path path, final Set<? extends OpenOption> options, final ExecutorService executor, final FileAttribute<?>[] attrs) throws IOException {
        return wrapped().newAsynchronousFileChannel(unwrap(path), options, executor, attrs);
    }

    @Override
    public SeekableByteChannel newByteChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>[] attrs) throws IOException {
        return wrapped().newByteChannel(unwrap(path), options, attrs);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(final Path dir, final Filter<? super Path> filter) throws IOException {
        return wrapped().newDirectoryStream(unwrap(dir), filter);
    }

    @Override
    public void createDirectory(final Path dir, final FileAttribute<?>[] attrs) throws IOException {wrapped().createDirectory(unwrap(dir), attrs);}

    @Override
    public void createSymbolicLink(final Path link, final Path target, final FileAttribute<?>[] attrs) throws IOException {
        wrapped().createSymbolicLink(unwrap(link), unwrap(target), attrs);
    }

    @Override
    public void createLink(final Path link, final Path existing) throws IOException {
        wrapped().createLink(unwrap(link), unwrap(existing));
    }

    @Override
    public void delete(final Path path) throws IOException {wrapped().delete(unwrap(path));}

    @Override
    public boolean deleteIfExists(final Path path) throws IOException {return wrapped().deleteIfExists(unwrap(path));}

    @Override
    public Path readSymbolicLink(final Path link) throws IOException {return wrapped().readSymbolicLink(unwrap(link));}

    @Override
    public void copy(final Path source, final Path target, final CopyOption... options) throws IOException {wrapped().copy(unwrap(source), unwrap(target), options);}

    @Override
    public void move(final Path source, final Path target, final CopyOption... options) throws IOException {wrapped().move(unwrap(source), unwrap(target), options);}

    @Override
    public boolean isSameFile(final Path path, final Path path2) throws IOException {return wrapped().isSameFile(unwrap(path), unwrap(path2));}

    @Override
    public boolean isHidden(final Path path) throws IOException {return wrapped().isHidden(unwrap(path));}

    @Override
    public FileStore getFileStore(final Path path) throws IOException {return wrapped().getFileStore(unwrap(path));}

    @Override
    public void checkAccess(final Path path, final AccessMode... modes) throws IOException {wrapped().checkAccess(unwrap(path), modes);}

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(final Path path, final Class<V> type, final LinkOption... options) {
        return wrapped().getFileAttributeView(unwrap(path), type, options);
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(final Path path, final Class<A> type, final LinkOption... options) throws IOException {
        return wrapped().readAttributes(unwrap(path), type, options);
    }

    @Override
    public Map<String, Object> readAttributes(final Path path, final String attributes, final LinkOption... options) throws IOException {
        return wrapped().readAttributes(unwrap(path), attributes, options);
    }

    @Override
    public void setAttribute(final Path path, final String attribute, final Object value, final LinkOption... options) throws IOException {
        wrapped().setAttribute(unwrap(path), attribute, value, options);
    }
}
