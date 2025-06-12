package org.ballerinalang.lsp.filesystem;

import java.net.URI;
import java.nio.file.*;
import java.util.Map;
import java.util.ServiceLoader;
import java.nio.file.spi.FileSystemProvider;

public class FileSystemTest {
    public static void main(String[] args) throws Exception {

        ServiceLoader<FileSystemProvider> loader = ServiceLoader.load(FileSystemProvider.class);
        loader.forEach(provider -> System.out.println("Found provider: " + provider.getClass()));

        // 1. Create a test directory structure
        Path tempDir = Files.createTempDirectory("web-bala-test");
        Files.createDirectories(tempDir.resolve("src"));
        Files.writeString(tempDir.resolve("src/test.bala"), "test content");

        // 2. Register your file system
        FileSystemProvider provider = new WebBalaFileSystemProvider(tempDir);
        FileSystems.newFileSystem(URI.create("web-bala:///"), Map.of("baseDir", tempDir));

        // 3. Test basic operations
        testPathResolution(provider);
        testFileOperations();

        System.out.println("All tests passed!");
    }

    private static void testPathResolution(FileSystemProvider provider) throws Exception {
        URI uri = URI.create("web-bala:///src/test.bala");
        Path path = provider.getPath(uri);

        System.out.println("Virtual path: " + uri);
        System.out.println("Real path: " + path);
        System.out.println("File exists: " + Files.exists(path));
    }

    private static void testFileOperations() throws Exception {
        Path virtualPath = Paths.get(URI.create("web-bala:///src/test.bala"));

        // Read content
        String content = Files.readString(virtualPath);
        System.out.println("File content: " + content);

        // Write new content
        Files.writeString(virtualPath, "new content");
        System.out.println("File updated successfully");
    }
}