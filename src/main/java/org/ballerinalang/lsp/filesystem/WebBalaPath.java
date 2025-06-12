package org.ballerinalang.lsp.filesystem;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class WebBalaPath implements Path {

    @Override
    public int compareTo(Path other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public boolean endsWith(Path other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'endsWith'");
    }

    @Override
    public Path getFileName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileName'");
    }

    @Override
    public FileSystem getFileSystem() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileSystem'");
    }

    @Override
    public Path getName(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public int getNameCount() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNameCount'");
    }

    @Override
    public Path getParent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getParent'");
    }

    @Override
    public Path getRoot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoot'");
    }

    @Override
    public boolean isAbsolute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAbsolute'");
    }

    @Override
    public Path normalize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'normalize'");
    }

    @Override
    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public Path relativize(Path other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'relativize'");
    }

    @Override
    public Path resolve(Path other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resolve'");
    }

    @Override
    public boolean startsWith(Path other) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startsWith'");
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subpath'");
    }

    @Override
    public Path toAbsolutePath() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toAbsolutePath'");
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRealPath'");
    }

    @Override
    public URI toUri() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toUri'");
    }
    // Implement Path interface methods
}