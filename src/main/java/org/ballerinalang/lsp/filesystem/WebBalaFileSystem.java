package org.ballerinalang.lsp.filesystem;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

public class WebBalaFileSystem extends FileSystem {
    private final WebBalaFileSystemProvider provider;

    public WebBalaFileSystem(WebBalaFileSystemProvider provider) {
        this.provider = provider;
    }

    @Override
    public FileSystemProvider provider() {
        return provider;
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileStores'");
    }

    @Override
    public Path getPath(String arg0, String... arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPath'");
    }

    @Override
    public PathMatcher getPathMatcher(String arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPathMatcher'");
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        return java.util.Collections.singletonList(new WebBalaPath(this, "/"));
    }

    @Override
    public String getSeparator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSeparator'");
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserPrincipalLookupService'");
    }

    @Override
    public boolean isOpen() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isOpen'");
    }

    @Override
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isReadOnly'");
    }

    @Override
    public WatchService newWatchService() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newWatchService'");
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supportedFileAttributeViews'");
    }

}