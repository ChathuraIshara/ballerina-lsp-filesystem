package org.ballerinalang.lsp.filesystem;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebBalaFileSystemProvider extends FileSystemProvider {
    private final Map<String, WebBalaFileSystem> fileSystems = new HashMap<>();
    public Path baseDir;

    public WebBalaFileSystemProvider() {
        // Can initialize with default values
        this.baseDir = Paths.get(System.getProperty("user.dir"));
    }

    public WebBalaFileSystemProvider(Path tempDir) {
        this.baseDir = tempDir;
    }

    public Path getBaseDir() {
        return this.baseDir;
    }

    @Override
    public String getScheme() {
        return "web-bala";
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        WebBalaFileSystem fs = new WebBalaFileSystem(this);
        fileSystems.put(uri.getScheme(), fs);
        System.out.println("New file system created for scheme: " + uri.getScheme());
        System.out.println("registered file system: " + fileSystems);
        return fs;
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        String virtualPathStr = path.toString();
        if (virtualPathStr.startsWith("/")) {
            virtualPathStr = virtualPathStr.substring(1);
        }
        Path realPath = baseDir.resolve(virtualPathStr).normalize();
        if (!realPath.startsWith(baseDir)) {
            throw new SecurityException("Attempt to access path outside base directory");
        }
        if (!Files.exists(realPath)) {
            throw new NoSuchFileException(realPath.toString());
        }
        for (AccessMode mode : modes) {
            switch (mode) {
                case READ:
                    if (!Files.isReadable(realPath)) {
                        throw new AccessDeniedException("Read access denied: " + realPath);
                    }
                    break;
                case WRITE:
                    if (!Files.isWritable(realPath)) {
                        throw new AccessDeniedException("Write access denied: " + realPath);
                    }
                    break;
                case EXECUTE:
                    if (!Files.isExecutable(realPath)) {
                        throw new AccessDeniedException("Execute access denied: " + realPath);
                    }
                    break;
            }
        }
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'copy'");
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        // Convert the virtual path to a real path under baseDir
        String virtualPathStr = dir.toString();
        if (virtualPathStr.startsWith("/")) {
            virtualPathStr = virtualPathStr.substring(1);
        }
        Path realPath = baseDir.resolve(virtualPathStr).normalize();
        System.out.println("Creating directory at real path: " + realPath);
        System.out.println("Base directory: " + baseDir);

        // Security: Prevent directory traversal outside baseDir
        if (!realPath.startsWith(baseDir)) {
            throw new SecurityException("Attempt to create directory outside base directory");
        }

        // Create the directory
        Files.createDirectories(realPath, attrs);
    }

    @Override
    public void delete(Path path) throws IOException {
        String virtualPathStr = path.toString();
        if (virtualPathStr.startsWith("/")) {
            virtualPathStr = virtualPathStr.substring(1);
        }
        Path realPath = baseDir.resolve(virtualPathStr).normalize();
        if (!realPath.startsWith(baseDir)) {
            throw new SecurityException("Attempt to delete outside base directory");
        }
        Files.delete(realPath);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileAttributeView'");
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileStore'");
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        FileSystem fs = fileSystems.get(uri.getScheme());
        if (fs == null) {
            throw new FileSystemNotFoundException("No filesystem found for URI: " + uri);
        }
        return fs;
    }

    @Override
    public Path getPath(URI uri) {
        // 1. Verify the URI scheme matches our provider
        if (!uri.getScheme().equalsIgnoreCase(this.getScheme())) {
            throw new IllegalArgumentException("URI scheme must be '" + this.getScheme() + "'");
        }

        // 2. Get the FileSystem for this scheme
        FileSystem fs = getFileSystem(uri);

        // 3. Handle null/empty paths (return root)
        String uriPath = uri.getPath();
        if (uriPath == null || uriPath.isEmpty() || uriPath.equals("/")) {
            return new WebBalaPath((WebBalaFileSystem) fs, "/");
        }

        // 4. Decode URL-encoded characters
        String decodedPath = URLDecoder.decode(uriPath, StandardCharsets.UTF_8);

        // 5. Remove leading slash if present
        if (decodedPath.startsWith("/")) {
            decodedPath = decodedPath.substring(1);
        }

        // 6. Return a WebBalaPath representing the virtual path
        return new WebBalaPath((WebBalaFileSystem) fs, decodedPath);
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isHidden'");
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isSameFile'");
    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
            throws IOException {
        // Convert the virtual path to a real path under baseDir
        String virtualPathStr = path.toString();
        if (virtualPathStr.startsWith("/")) {
            virtualPathStr = virtualPathStr.substring(1);
        }
        Path realPath = baseDir.resolve(virtualPathStr).normalize();
        if (!realPath.startsWith(baseDir)) {
            throw new SecurityException("Attempt to access path outside base directory");
        }
        return Files.newByteChannel(realPath, options, attrs);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, Filter<? super Path> filter) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newDirectoryStream'");
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options)
            throws IOException {
        String virtualPathStr = path.toString();
        if (virtualPathStr.startsWith("/")) {
            virtualPathStr = virtualPathStr.substring(1);
        }
        Path realPath = baseDir.resolve(virtualPathStr).normalize();
        if (!realPath.startsWith(baseDir)) {
            throw new SecurityException("Attempt to access path outside base directory");
        }
        return Files.readAttributes(realPath, type, options);
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAttributes'");
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAttribute'");
    }

    // Implement other required methods
}