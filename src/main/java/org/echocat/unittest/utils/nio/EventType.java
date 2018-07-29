package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchService;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.joining;
import static org.echocat.unittest.utils.utils.ClassUtils.methodBy;

public interface EventType extends Comparable<EventType> {

    @SuppressWarnings("FieldNamingConvention")
    interface FileSystems {
        EventType provider = eventTypeOf(FileSystem.class, "provider");
        EventType close = eventTypeOf(FileSystem.class, "close");
        EventType isOpen = eventTypeOf(FileSystem.class, "isOpen");
        EventType isReadOnly = eventTypeOf(FileSystem.class, "isReadOnly");
        EventType getSeparator = eventTypeOf(FileSystem.class, "getSeparator");
        EventType getRootDirectories = eventTypeOf(FileSystem.class, "getRootDirectories");
        EventType getFileStores = eventTypeOf(FileSystem.class, "getFileStores");
        EventType supportedFileAttributeViews = eventTypeOf(FileSystem.class, "supportedFileAttributeViews");
        EventType getPath = eventTypeOf(FileSystem.class, "getPath", String.class, String[].class);
        EventType getPathMatcher = eventTypeOf(FileSystem.class, "getPathMatcher", String.class);
        EventType getUserPrincipalLookupService = eventTypeOf(FileSystem.class, "getUserPrincipalLookupService");
        EventType newWatchService = eventTypeOf(FileSystem.class, "newWatchService");
    }

    @SuppressWarnings("FieldNamingConvention")
    interface FileSystemProviders {
        EventType getScheme = eventTypeOf(FileSystemProvider.class, "getScheme");
        EventType newFileSystem = eventTypeOf(FileSystemProvider.class, "newFileSystem", Path.class, Map.class);
        EventType newFileSystem_URI = eventTypeOf(FileSystemProvider.class, "newFileSystem", URI.class, Map.class);
        EventType getFileSystem_URI = eventTypeOf(FileSystemProvider.class, "getFileSystem", URI.class);
        EventType getPath = eventTypeOf(FileSystemProvider.class, "getPath", URI.class);
        EventType newInputStream = eventTypeOf(FileSystemProvider.class, "newInputStream", Path.class, OpenOption[].class);
        EventType newOutputStream = eventTypeOf(FileSystemProvider.class, "newOutputStream", Path.class, OpenOption[].class);
        EventType newFileChannel = eventTypeOf(FileSystemProvider.class, "newFileChannel", Path.class, Set.class, FileAttribute[].class);
        EventType newAsynchronousFileChannel = eventTypeOf(FileSystemProvider.class, "newAsynchronousFileChannel", Path.class, Set.class, ExecutorService.class, FileAttribute[].class);
        EventType newByteChannel = eventTypeOf(FileSystemProvider.class, "newByteChannel", Path.class, Set.class, FileAttribute[].class);
        EventType newDirectoryStream = eventTypeOf(FileSystemProvider.class, "newDirectoryStream", Path.class, Filter.class);
        EventType createDirectory = eventTypeOf(FileSystemProvider.class, "createDirectory", Path.class, FileAttribute[].class);
        EventType createSymbolicLink = eventTypeOf(FileSystemProvider.class, "createSymbolicLink", Path.class, Path.class, FileAttribute[].class);
        EventType createLink = eventTypeOf(FileSystemProvider.class, "createLink", Path.class, Path.class);
        EventType delete = eventTypeOf(FileSystemProvider.class, "delete", Path.class);
        EventType deleteIfExists = eventTypeOf(FileSystemProvider.class, "deleteIfExists", Path.class);
        EventType readSymbolicLink = eventTypeOf(FileSystemProvider.class, "readSymbolicLink", Path.class);
        EventType copy = eventTypeOf(FileSystemProvider.class, "copy", Path.class, Path.class, CopyOption[].class);
        EventType move = eventTypeOf(FileSystemProvider.class, "move", Path.class, Path.class, CopyOption[].class);
        EventType isSameFile = eventTypeOf(FileSystemProvider.class, "isSameFile", Path.class, Path.class);
        EventType isHidden = eventTypeOf(FileSystemProvider.class, "isHidden", Path.class);
        EventType getFileStore = eventTypeOf(FileSystemProvider.class, "getFileStore", Path.class);
        EventType checkAccess = eventTypeOf(FileSystemProvider.class, "checkAccess", Path.class, AccessMode[].class);
        EventType getFileAttributeView = eventTypeOf(FileSystemProvider.class, "getFileAttributeView", Path.class, Class.class, LinkOption[].class);
        EventType readAttributes_checked = eventTypeOf(FileSystemProvider.class, "readAttributes", Path.class, Class.class, LinkOption[].class);
        EventType readAttributes = eventTypeOf(FileSystemProvider.class, "readAttributes", Path.class, String.class, LinkOption[].class);
        EventType setAttribute = eventTypeOf(FileSystemProvider.class, "setAttribute", Path.class, String.class, Object.class, LinkOption[].class);
    }

    @SuppressWarnings("FieldNamingConvention")
    interface Paths {
        EventType getFileSystem = eventTypeOf(Path.class, "getFileSystem");
        EventType isAbsolute = eventTypeOf(Path.class, "isAbsolute");
        EventType getRoot = eventTypeOf(Path.class, "getRoot");
        EventType getFileName = eventTypeOf(Path.class, "getFileName");
        EventType getParent = eventTypeOf(Path.class, "getParent");
        EventType getNameCount = eventTypeOf(Path.class, "getNameCount");
        EventType getName = eventTypeOf(Path.class, "getName", int.class);
        EventType subpath = eventTypeOf(Path.class, "subpath", int.class, int.class);
        EventType startsWith = eventTypeOf(Path.class, "startsWith", Path.class);
        EventType startsWith_String = eventTypeOf(Path.class, "startsWith", String.class);
        EventType endsWith = eventTypeOf(Path.class, "endsWith", Path.class);
        EventType endsWith_String = eventTypeOf(Path.class, "endsWith", Path.class);
        EventType normalize = eventTypeOf(Path.class, "normalize");
        EventType resolve = eventTypeOf(Path.class, "resolve", Path.class);
        EventType resolve_String = eventTypeOf(Path.class, "resolve", String.class);
        EventType resolveSibling = eventTypeOf(Path.class, "resolveSibling", Path.class);
        EventType resolveSibling_String = eventTypeOf(Path.class, "resolveSibling", String.class);
        EventType relativize = eventTypeOf(Path.class, "relativize", Path.class);
        EventType toUri = eventTypeOf(Path.class, "toUri");
        EventType toAbsolutePath = eventTypeOf(Path.class, "toAbsolutePath");
        EventType toRealPath = eventTypeOf(Path.class, "toRealPath", LinkOption[].class);
        EventType toFile = eventTypeOf(Path.class, "toFile");
        EventType register = eventTypeOf(Path.class, "register", WatchService.class, Kind[].class, Modifier[].class);
        EventType register_simple = eventTypeOf(Path.class, "register", WatchService.class, Kind[].class);
        EventType iterator = eventTypeOf(Path.class, "iterator");
        EventType compareTo = eventTypeOf(Path.class, "compareTo", Path.class);
    }

    @Nonnull
    static EventType eventTypeOf(@Nonnull Class<?> source, String methodName, Class<?>... argumentTypes) {
        final Method method = methodBy(source, methodName, argumentTypes);
        //noinspection unchecked,rawtypes
        return new Default(
            source,
            methodName,
            asList(argumentTypes),
            method.getReturnType(),
            asList((Class[]) method.getExceptionTypes())
        );
    }

    @Nonnull
    Class<?> sourceType();

    @Nonnull
    String name();

    @Nonnull
    List<Class<?>> argumentTypes();

    @Nonnull
    Class<?> returnType();

    @Nonnull
    Set<Class<? extends Throwable>> allowedThrowableTypes();

    @Immutable
    class Default implements EventType {

        @Nonnull
        private final Class<?> sourceType;
        @Nonnull
        private final String name;
        @Nonnull
        private final List<Class<?>> argumentTypes;
        @Nonnull
        private final Class<?> returnType;

        @Nonnull
        private final Set<Class<? extends Throwable>> allowedThrowableTypes;

        protected Default(
            @Nonnull Class<?> sourceType,
            @Nonnull String name,
            @Nonnull Collection<Class<?>> argumentTypes,
            @Nonnull Class<?> returnType,
            @Nonnull Collection<Class<? extends Throwable>> allowedThrowableTypes
        ) {
            this.sourceType = sourceType;
            this.name = name;
            this.argumentTypes = unmodifiableList(normalize(argumentTypes));
            this.returnType = normalize(returnType);
            this.allowedThrowableTypes = unmodifiableSet(new HashSet<>(allowedThrowableTypes));
        }

        protected List<Class<?>> normalize(@Nonnull Collection<Class<?>> input) {
            final List<Class<?>> result = new ArrayList<>(input.size());
            for (final Class<?> candidate : input) {
                result.add(normalize(candidate));
            }
            return result;
        }

        protected Class<?> normalize(Class<?> input) {
            if (Boolean.TYPE.equals(input)) {
                return Boolean.class;
            }
            if (Character.TYPE.equals(input)) {
                return Character.class;
            }
            if (Byte.TYPE.equals(input)) {
                return Byte.class;
            }
            if (Short.TYPE.equals(input)) {
                return Short.class;
            }
            if (Integer.TYPE.equals(input)) {
                return Integer.class;
            }
            if (Long.TYPE.equals(input)) {
                return Long.class;
            }
            if (Float.TYPE.equals(input)) {
                return Float.class;
            }
            if (Double.TYPE.equals(input)) {
                return Double.class;
            }
            if (Void.TYPE.equals(input)) {
                return Void.class;
            }
            return input;
        }

        @Override
        @Nonnull
        public Class<?> sourceType() {
            return sourceType;
        }

        @Override
        @Nonnull
        public String name() {
            return name;
        }

        @Override
        @Nonnull
        public List<Class<?>> argumentTypes() {
            return argumentTypes;
        }

        @Override
        @Nonnull
        public Class<?> returnType() {
            return returnType;
        }

        @Override
        @Nonnull
        public Set<Class<? extends Throwable>> allowedThrowableTypes() {
            return allowedThrowableTypes;
        }

        @Override
        public int compareTo(@Nonnull EventType that) {
            final int resultOfSourceType = sourceType().getName().compareTo(that.sourceType().getName());
            if (resultOfSourceType != 0) {
                return resultOfSourceType;
            }
            final int resultOfName = name().compareTo(that.name());
            if (resultOfName != 0) {
                return resultOfName;
            }
            return toString(argumentTypes()).compareTo(toString(that.argumentTypes()));
        }

        @Nonnull
        protected String toString(@Nonnull Collection<Class<?>> types) {
            return types.stream()
                .map(Class::getName)
                .collect(joining(":"));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (!(o instanceof EventType)) { return false; }
            final EventType type = (EventType) o;
            return Objects.equals(sourceType(), type.sourceType()) &&
                Objects.equals(name(), type.name()) &&
                Objects.equals(argumentTypes(), type.argumentTypes());
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceType(), name(), argumentTypes());
        }

        @Override
        public String toString() {
            return returnType().getSimpleName() + ": " + sourceType().getSimpleName() + "#" + name()
                + argumentTypes().stream().map(Class::getSimpleName).collect(joining(", ", "(", ")"));
        }

    }

}
