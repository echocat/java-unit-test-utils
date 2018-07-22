package org.echocat.unittest.utils.nio;

import org.echocat.unittest.utils.nio.WrappedEvent.Interceptor;
import org.echocat.unittest.utils.nio.WrappedEvent.InterceptorEnabled;
import org.echocat.unittest.utils.nio.WrappedEvent.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import static org.echocat.unittest.utils.nio.WrappedEvent.eventTypeOf;
import static org.echocat.unittest.utils.nio.WrappedExecution.withResult;

@FunctionalInterface
@SuppressWarnings("FieldNamingConvention")
public interface WrappedPath extends Path, Wrapping<Path>, InterceptorEnabled {

    @Nonnull
    static WrappedPath wrap(@Nonnull Path original, @Nullable Interceptor interceptor) {
        return new Impl(original, interceptor);
    }

    @Nonnull
    static WrappedPath wrap(@Nonnull Path original) {
        return wrap(original, null);
    }

    @Nonnull
    @Override
    default Optional<Interceptor> interceptor() {
        return Optional.empty();
    }

    @Override
    @Nonnull
    Path wrapped();

    static Type ET_getFileSystem = eventTypeOf(WrappedPath.class, "getFileSystem");

    @Override
    default FileSystem getFileSystem() {
        return withResult(this, ET_getFileSystem,
            wrapped -> new WrappedFileSystem<>(
                getClass(),
                wrapped.getFileSystem(),
                ((InterceptorEnabled) this).interceptor().orElse(null)
            )
        );
    }

    static Type ET_isAbsolute = eventTypeOf(WrappedPath.class, "isAbsolute");

    @Override
    default boolean isAbsolute() {
        return withResult(this, ET_isAbsolute,
            Path::isAbsolute
        );
    }

    static Type ET_getRoot = eventTypeOf(WrappedPath.class, "getRoot");

    @Override
    default Path getRoot() {
        return withResult(this, ET_getRoot,
            Path::getRoot
        );
    }

    static Type ET_getFileName = eventTypeOf(WrappedPath.class, "getFileName");

    @Override
    default Path getFileName() {
        return withResult(this, ET_getFileName,
            Path::getFileName
        );
    }

    static Type ET_getParent = eventTypeOf(WrappedPath.class, "getParent");

    @Override
    default Path getParent() {
        return withResult(this, ET_getParent,
            Path::getParent
        );
    }

    static Type ET_getNameCount = eventTypeOf(WrappedPath.class, "getNameCount");

    @Override
    default int getNameCount() {
        return withResult(this, ET_getNameCount,
            Path::getNameCount
        );
    }

    static Type ET_getName = eventTypeOf(WrappedPath.class, "getName", int.class);

    @Override
    default Path getName(final int index) {
        return withResult(this, ET_getName,
            wrapped -> wrapped.getName(index)
            , index);
    }

    static Type ET_subpath = eventTypeOf(WrappedPath.class, "subpath", int.class, int.class);

    @Override
    default Path subpath(final int beginIndex, final int endIndex) {
        return withResult(this, ET_subpath,
            wrapped -> wrapped.subpath(beginIndex, endIndex)
            , beginIndex, endIndex);
    }

    static Type ET_startsWith = eventTypeOf(WrappedPath.class, "startsWith", Path.class);

    @Override
    default boolean startsWith(final Path other) {
        return withResult(this, ET_startsWith,
            wrapped -> wrapped.startsWith(other)
            , other);
    }

    static Type ET_startsWith_String = eventTypeOf(WrappedPath.class, "startsWith", String.class);

    @Override
    default boolean startsWith(final String other) {
        return withResult(this, ET_startsWith_String,
            wrapped -> wrapped.startsWith(other)
            , other);
    }

    static Type ET_endsWith = eventTypeOf(WrappedPath.class, "endsWith", Path.class);

    @Override
    default boolean endsWith(final Path other) {
        return withResult(this, ET_endsWith,
            wrapped -> wrapped.endsWith(other)
            , other);
    }

    static Type ET_endsWith_String = eventTypeOf(WrappedPath.class, "endsWith", Path.class);

    @Override
    default boolean endsWith(final String other) {
        return withResult(this, ET_endsWith_String,
            wrapped -> wrapped.endsWith(other)
            , other);
    }

    static Type ET_normalize = eventTypeOf(WrappedPath.class, "normalize");

    @Override
    default Path normalize() {
        return withResult(this, ET_normalize,
            Path::normalize
        );
    }

    static Type ET_resolve = eventTypeOf(WrappedPath.class, "resolve", Path.class);

    @Override
    default Path resolve(final Path other) {
        return withResult(this, ET_resolve,
            wrapped -> wrapped.resolve(other)
            , other);
    }

    static Type ET_resolve_String = eventTypeOf(WrappedPath.class, "resolve", String.class);

    @Override
    default Path resolve(final String other) {
        return withResult(this, ET_resolve_String,
            wrapped -> wrapped.resolve(other)
            , other);
    }

    static Type ET_resolveSibling = eventTypeOf(WrappedPath.class, "resolveSibling", Path.class);

    @Override
    default Path resolveSibling(final Path other) {
        return withResult(this, ET_resolveSibling,
            wrapped -> wrapped.resolveSibling(other)
            , other);
    }

    static Type ET_resolveSibling_String = eventTypeOf(WrappedPath.class, "resolveSibling", String.class);

    @Override
    default Path resolveSibling(final String other) {
        return withResult(this, ET_resolveSibling_String,
            wrapped -> wrapped.resolveSibling(other)
            , other);
    }

    static Type ET_relativize = eventTypeOf(WrappedPath.class, "relativize", Path.class);

    @Override
    default Path relativize(final Path other) {
        return withResult(this, ET_relativize,
            wrapped -> wrapped.relativize(other)
            , other);
    }

    static Type ET_toUri = eventTypeOf(WrappedPath.class, "toUri");

    @Override
    default URI toUri() {
        return withResult(this, ET_toUri,
            Path::toUri
        );
    }

    static Type ET_toAbsolutePath = eventTypeOf(WrappedPath.class, "toAbsolutePath");

    @Override
    default Path toAbsolutePath() {
        return withResult(this, ET_toAbsolutePath,
            Path::toAbsolutePath
        );
    }

    static Type ET_toRealPath = eventTypeOf(WrappedPath.class, "toRealPath", LinkOption[].class);

    @Override
    default Path toRealPath(final LinkOption... options) throws IOException {
        return withResult(this, ET_toRealPath, IOException.class,
            wrapped -> wrapped.toRealPath(options)
            , (Object) options);
    }

    static Type ET_toFile = eventTypeOf(WrappedPath.class, "toFile");

    @Override
    default File toFile() {
        return withResult(this, ET_toFile,
            Path::toFile
        );
    }

    static Type ET_register = eventTypeOf(WrappedPath.class, "register", WatchService.class, Kind[].class, Modifier[].class);

    @Override
    default WatchKey register(final WatchService watcher, final Kind<?>[] events, final Modifier... modifiers) throws IOException {
        return withResult(this, ET_register, IOException.class,
            wrapped -> wrapped.register(watcher, events, modifiers)
            , watcher, events, modifiers);
    }

    static Type ET_register_simple = eventTypeOf(WrappedPath.class, "register", WatchService.class, Kind[].class);

    @Override
    default WatchKey register(final WatchService watcher, final Kind<?>[] events) throws IOException {
        return withResult(this, ET_register_simple, IOException.class,
            wrapped -> wrapped.register(watcher, events)
            , watcher, events);
    }

    static Type ET_iterator = eventTypeOf(WrappedPath.class, "iterator");

    @Nonnull
    @Override
    default Iterator<Path> iterator() {
        return withResult(this, ET_iterator,
            Path::iterator
        );
    }

    static Type ET_compareTo = eventTypeOf(WrappedPath.class, "compareTo", Path.class);

    @Override
    default int compareTo(final Path other) {
        return withResult(this, ET_compareTo,
            wrapped -> wrapped.compareTo(other)
            , other);
    }

    class Impl extends Support {

        @Nonnull
        private final Path wrapped;
        @Nonnull
        private final Optional<Interceptor> interceptor;

        private Impl(@Nonnull Path wrapped, @Nullable Interceptor interceptor) {
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

        @Override
        public int compareTo(Path other) {
            return wrapped().compareTo(other);
        }

    }

}
