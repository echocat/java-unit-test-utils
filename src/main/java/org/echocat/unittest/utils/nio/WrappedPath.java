package org.echocat.unittest.utils.nio;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.Iterator;

@FunctionalInterface
public interface WrappedPath extends Path {

    @Nonnull
    Path wrapped();

    @Override
    default FileSystem getFileSystem() {
        return new WrappedFileSystem<>(getClass(), wrapped().getFileSystem());
    }

    @Override
    default boolean isAbsolute() {return wrapped().isAbsolute();}

    @Override
    default Path getRoot() {return wrapped().getRoot();}

    @Override
    default Path getFileName() {return wrapped().getFileName();}

    @Override
    default Path getParent() {return wrapped().getParent();}

    @Override
    default int getNameCount() {return wrapped().getNameCount();}

    @Override
    default Path getName(final int index) {return wrapped().getName(index);}

    @Override
    default Path subpath(final int beginIndex, final int endIndex) {return wrapped().subpath(beginIndex, endIndex);}

    @Override
    default boolean startsWith(final Path other) {return wrapped().startsWith(other);}

    @Override
    default boolean startsWith(final String other) {return wrapped().startsWith(other);}

    @Override
    default boolean endsWith(final Path other) {return wrapped().endsWith(other);}

    @Override
    default boolean endsWith(final String other) {return wrapped().endsWith(other);}

    @Override
    default Path normalize() {return wrapped().normalize();}

    @Override
    default Path resolve(final Path other) {return wrapped().resolve(other);}

    @Override
    default Path resolve(final String other) {return wrapped().resolve(other);}

    @Override
    default Path resolveSibling(final Path other) {return wrapped().resolveSibling(other);}

    @Override
    default Path resolveSibling(final String other) {return wrapped().resolveSibling(other);}

    @Override
    default Path relativize(final Path other) {return wrapped().relativize(other);}

    @Override
    default URI toUri() {return wrapped().toUri();}

    @Override
    default Path toAbsolutePath() {return wrapped().toAbsolutePath();}

    @Override
    default Path toRealPath(final LinkOption... options) throws IOException {return wrapped().toRealPath(options);}

    @Override
    default File toFile() {return wrapped().toFile();}

    @Override
    default WatchKey register(final WatchService watcher, final Kind<?>[] events, final Modifier... modifiers) throws IOException {return wrapped().register(watcher, events, modifiers);}

    @Override
    default WatchKey register(final WatchService watcher, final Kind<?>[] events) throws IOException {return wrapped().register(watcher, events);}

    @Nonnull
    @Override
    default Iterator<Path> iterator() {return wrapped().iterator();}

    @Override
    default int compareTo(final Path other) {return wrapped().compareTo(other);}

}
