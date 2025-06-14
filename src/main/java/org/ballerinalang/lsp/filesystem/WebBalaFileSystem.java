package org.ballerinalang.lsp.filesystem;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Set;

public class WebBalaFileSystem extends FileSystem {
    private final WebBalaFileSystemProvider provider;
    private final boolean readOnly = false;
    private boolean open = true;

    public WebBalaFileSystem(WebBalaFileSystemProvider provider) {
        this.provider = provider;
    }

    @Override
    public FileSystemProvider provider() {
        return provider;
    }

    @Override
    public void close() throws IOException {
        open = false;
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        return Collections.emptyList();
    }

    @Override
    public Path getPath(String arg0, String... arg1) {
        String joined = arg0;
        if (arg1 != null && arg1.length > 0) {
            joined = String.join(getSeparator(), arg0, String.join(getSeparator(), arg1));
        }
        return new WebBalaPath(this, joined);
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
        return "/";
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserPrincipalLookupService'");
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public WatchService newWatchService() throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newWatchService'");
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        return Collections.singleton("basic");
    }

}