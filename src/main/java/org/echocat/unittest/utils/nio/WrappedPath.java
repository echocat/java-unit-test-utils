package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.Interceptor.InterceptorEnabled;
import org.echocat.unittest.utils.utils.Wrapping;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.echocat.unittest.utils.nio.EventType.Paths.compareTo;
import static org.echocat.unittest.utils.nio.EventType.Paths.endsWith;
import static org.echocat.unittest.utils.nio.EventType.Paths.endsWith_String;
import static org.echocat.unittest.utils.nio.EventType.Paths.getFileName;
import static org.echocat.unittest.utils.nio.EventType.Paths.getFileSystem;
import static org.echocat.unittest.utils.nio.EventType.Paths.getName;
import static org.echocat.unittest.utils.nio.EventType.Paths.getNameCount;
import static org.echocat.unittest.utils.nio.EventType.Paths.getParent;
import static org.echocat.unittest.utils.nio.EventType.Paths.getRoot;
import static org.echocat.unittest.utils.nio.EventType.Paths.isAbsolute;
import static org.echocat.unittest.utils.nio.EventType.Paths.iterator;
import static org.echocat.unittest.utils.nio.EventType.Paths.normalize;
import static org.echocat.unittest.utils.nio.EventType.Paths.register;
import static org.echocat.unittest.utils.nio.EventType.Paths.register_simple;
import static org.echocat.unittest.utils.nio.EventType.Paths.relativize;
import static org.echocat.unittest.utils.nio.EventType.Paths.resolve;
import static org.echocat.unittest.utils.nio.EventType.Paths.resolveSibling;
import static org.echocat.unittest.utils.nio.EventType.Paths.resolveSibling_String;
import static org.echocat.unittest.utils.nio.EventType.Paths.resolve_String;
import static org.echocat.unittest.utils.nio.EventType.Paths.startsWith;
import static org.echocat.unittest.utils.nio.EventType.Paths.startsWith_String;
import static org.echocat.unittest.utils.nio.EventType.Paths.subpath;
import static org.echocat.unittest.utils.nio.EventType.Paths.toAbsolutePath;
import static org.echocat.unittest.utils.nio.EventType.Paths.toFile;
import static org.echocat.unittest.utils.nio.EventType.Paths.toRealPath;
import static org.echocat.unittest.utils.nio.EventType.Paths.toUri;
import static org.echocat.unittest.utils.nio.WrappedExecution.withResult;

@FunctionalInterface
public interface WrappedPath extends Path, Wrapping<Path>, InterceptorEnabled {

    @Nonnull
    static WrappedPath create(@Nonnull Path original, @Nullable Interceptor interceptor) {
        return new Default(original, interceptor);
    }

    @Nonnull
    static WrappedPath create(@Nonnull Path original) {
        return create(original, null);
    }

    @Nonnull
    @Override
    default Optional<Interceptor> interceptor() {
        return Optional.empty();
    }

    @Override
    @Nonnull
    Path wrapped();

    @Nonnull
    default WrappedPath wrap(@Nonnull Path original) {
        if (original instanceof WrappedPath) {
            return (WrappedPath) original;
        }
        return create(original, interceptor().orElse(null));
    }

    @Override
    default FileSystem getFileSystem() {
        return withResult(this, getFileSystem,
            wrapped -> new WrappedFileSystem<>(
                getClass(),
                wrapped.getFileSystem(),
                ((InterceptorEnabled) this).interceptor().orElse(null)
            )
        );
    }

    @Override
    default boolean isAbsolute() {
        return withResult(this, isAbsolute,
            Path::isAbsolute
        );
    }

    @Override
    default Path getRoot() {
        return wrap(withResult(this, getRoot,
            Path::getRoot
        ));
    }

    @Override
    default Path getFileName() {
        return wrap(withResult(this, getFileName,
            Path::getFileName
        ));
    }

    @Override
    default Path getParent() {
        return wrap(withResult(this, getParent,
            Path::getParent
        ));
    }

    @Override
    default int getNameCount() {
        return withResult(this, getNameCount,
            Path::getNameCount
        );
    }

    @Override
    default Path getName(final int index) {
        return wrap(withResult(this, getName,
            wrapped -> wrapped.getName(index)
            , index));
    }

    @Override
    default Path subpath(final int beginIndex, final int endIndex) {
        return wrap(withResult(this, subpath,
            wrapped -> wrapped.subpath(beginIndex, endIndex)
            , beginIndex, endIndex));
    }

    @Override
    default boolean startsWith(final Path other) {
        return withResult(this, startsWith,
            wrapped -> wrapped.startsWith(other)
            , other);
    }

    @Override
    default boolean startsWith(final String other) {
        return withResult(this, startsWith_String,
            wrapped -> wrapped.startsWith(other)
            , other);
    }

    @Override
    default boolean endsWith(final Path other) {
        return withResult(this, endsWith,
            wrapped -> wrapped.endsWith(other)
            , other);
    }

    @Override
    default boolean endsWith(final String other) {
        return withResult(this, endsWith_String,
            wrapped -> wrapped.endsWith(other)
            , other);
    }

    @Override
    default Path normalize() {
        return wrap(withResult(this, normalize,
            Path::normalize
        ));
    }

    @Override
    default Path resolve(final Path other) {
        return wrap(withResult(this, resolve,
            wrapped -> wrapped.resolve(other)
            , other));
    }

    @Override
    default Path resolve(final String other) {
        return wrap(withResult(this, resolve_String,
            wrapped -> wrapped.resolve(other)
            , other));
    }

    @Override
    default Path resolveSibling(final Path other) {
        return wrap(withResult(this, resolveSibling,
            wrapped -> wrapped.resolveSibling(other)
            , other));
    }

    @Override
    default Path resolveSibling(final String other) {
        return wrap(withResult(this, resolveSibling_String,
            wrapped -> wrapped.resolveSibling(other)
            , other));
    }

    @Override
    default Path relativize(final Path other) {
        return wrap(withResult(this, relativize,
            wrapped -> wrapped.relativize(other)
            , other));
    }

    @Override
    default URI toUri() {
        return withResult(this, toUri,
            Path::toUri
        );
    }

    @Override
    default Path toAbsolutePath() {
        return wrap(withResult(this, toAbsolutePath,
            Path::toAbsolutePath
        ));
    }

    @Override
    default Path toRealPath(final LinkOption... options) throws IOException {
        return wrap(withResult(this, toRealPath, IOException.class,
            wrapped -> wrapped.toRealPath(options)
            , (Object) options));
    }

    @Override
    default File toFile() {
        return withResult(this, toFile,
            Path::toFile
        );
    }

    @Override
    default WatchKey register(final WatchService watcher, final Kind<?>[] events, final Modifier... modifiers) throws IOException {
        return withResult(this, register, IOException.class,
            wrapped -> wrapped.register(watcher, events, modifiers)
            , watcher, events, modifiers);
    }

    @Override
    default WatchKey register(final WatchService watcher, final Kind<?>[] events) throws IOException {
        return withResult(this, register_simple, IOException.class,
            wrapped -> wrapped.register(watcher, events)
            , watcher, events);
    }

    @Nonnull
    @Override
    default Iterator<Path> iterator() {
        return withResult(this, iterator,
            Path::iterator
        );
    }

    @Override
    default int compareTo(final Path other) {
        return withResult(this, compareTo,
            wrapped -> {
                if (other instanceof WrappedPath) {
                    return wrapped().compareTo(((WrappedPath) other).wrapped());
                }
                return wrapped().compareTo(other);
            }
            , other);
    }

    @Immutable
    class Default extends Support {

        @Nonnull
        private final Path wrapped;
        @Nonnull
        private final Optional<Interceptor> interceptor;

        protected Default(@Nonnull Path wrapped, @Nullable Interceptor interceptor) {
            this.wrapped = wrapped;
            this.interceptor = ofNullable(interceptor);
        }

        @Nonnull
        @Override
        public Path wrapped() {
            return wrapped;
        }

        @Nonnull
        @Override
        public Optional<Interceptor> interceptor() {
            return interceptor;
        }

    }

    abstract class Support implements WrappedPath {

        @Override
        public String toString() {return wrapped().toString();}

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof WrappedPath) {
                return wrapped().equals(((WrappedPath) o).wrapped());
            }
            if (o instanceof Path) {
                return wrapped().equals(o);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return wrapped().hashCode();
        }

    }

}
