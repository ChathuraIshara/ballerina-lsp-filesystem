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

        // 2. Register your file system
        FileSystem fs = FileSystems.newFileSystem(URI.create("web-bala:///"), Map.of());
        // 2. Get the provider from the file system
        FileSystemProvider provider = fs.provider();

        testWebBalaPathOperations(provider);

        System.out.println("All tests passed!");
    }

    private static void testGetPathOfProvider(FileSystemProvider provider) throws Exception {
        URI uri = URI.create("web-bala:///foo/bar.txt");
        System.out.println("Uri" + uri.toString());
        Path path = provider.getPath(uri);
        System.out.printf("Path: %s%n", path);
        System.out.println();
    }

    private static void testPathResolution(FileSystemProvider provider) throws Exception {
        URI uri = URI.create("web-bala:///src/test.bala");
        Path path = provider.getPath(uri);
        System.out.println("Testing path resolution...");
        System.out.println("Virtual path: " + uri);
        System.out.println("Resolved after provider getpath: " + path);
    }

    private static void testFileOperations(FileSystemProvider provider) throws Exception {
        URI uri = URI.create("web-bala:///mydir/newdir");
        System.out.printf("uri", uri.toString());
        Path dir = provider.getPath(uri);
        System.out.printf("path", dir.toString());
        Files.createDirectories(dir);
        System.out.println("Directory created: " + dir);
    }

    private static void testWebBalaUriToFileCreation(FileSystemProvider provider) throws Exception {
        // 1. Define a web-bala URI
        URI webBalaUri = URI.create("web-bala:///mydir/created.txt");
        // System.out.println("web bala uri",webBalaUri.toString() );

        // 2. Resolve to real file system path using the provider
        Path resolvedPath = provider.getPath(webBalaUri);
        System.out.println("Resolved path in testwebbalauritofilecreation: " + resolvedPath);

        // 3. Create parent directories if needed
        Files.createDirectories(resolvedPath.getParent());

        // 4. Create the file and write content
        String content = "Created via web-bala URI!";
        Files.writeString(resolvedPath, content);

        // 5. Verify file exists and content is correct
        System.out.println("Resolved real path: " + resolvedPath);
        System.out.println("File exists: " + Files.exists(resolvedPath));
        System.out.println("File content: " + Files.readString(resolvedPath));

        // 6. Also print the expected real file URI for clarity
        Path expectedPath = ((WebBalaFileSystemProvider) provider).getBaseDir().resolve("mydir/created.txt");
        System.out.println("Expected real file path: " + expectedPath);
    }

    private static void testUpdateExistingWebBalaFile(FileSystemProvider provider) throws Exception {
        // 1. Use the URI of an already existing file
        URI webBalaUri = URI.create("web-bala:///mydir/created.txt");

        // 2. Resolve to real file system path using the provider
        Path resolvedPath = provider.getPath(webBalaUri);

        // 3. Check that the file exists
        if (!Files.exists(resolvedPath)) {
            System.out.println("File does not exist: " + resolvedPath);
            return;
        }

        String oldReadContent = Files.readString(resolvedPath);
        System.out.println("Old file content: " + oldReadContent);

        // 4. Update the file content
        String updatedContent = "This is the updated content!with new changes";
        Files.writeString(resolvedPath, updatedContent);

        // 5. Verify the file content is updated
        String readContent = Files.readString(resolvedPath);
        System.out.println("Updated file content: " + readContent);

        if (updatedContent.equals(readContent)) {
            System.out.println("Content update successful!");
        } else {
            System.out.println("Content update failed!");
        }
    }

    private static void testRootDirectories(FileSystemProvider provider) throws Exception {
        // 1. Get the FileSystem instance for the web-bala scheme
        FileSystem fs = provider.getFileSystem(URI.create("web-bala:///"));

        // 2. Get root directories
        Iterable<Path> roots = fs.getRootDirectories();

        // 3. Print and check the root directories
        System.out.println("Root directories of web-bala file system:");
        int count = 0;
        for (Path root : roots) {
            System.out.println("Root: " + root);
            count++;
        }
        if (count == 0) {
            System.out.println("No root directories found!");
        } else {
            System.out.println("Root directories check passed!");
        }
    }

    private static void testCreateFileWithContent(FileSystemProvider provider) throws Exception {
        // 1. Define a virtual URI for the file
        URI fileUri = URI.create("web-bala:///mydir/sample.txt");
        Path filePath = provider.getPath(fileUri);

        // 2. Ensure parent directory exists
        Path parentDir = filePath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }

        // 3. Write content to the file
        String content = "Hello from the virtual file system!";
        Files.writeString(filePath, content);

        // 4. Read back and print the content
        String readContent = Files.readString(filePath);
        System.out.println("File created at: " + filePath);
        System.out.println("File content: " + readContent);
    }

    private static void testReadFileWithVirtualUri(FileSystemProvider provider) throws Exception {
        // 1. Define the virtual URI for the file
        URI fileUri = URI.create("web-bala:///mydir/sample.txt");
        Path filePath = provider.getPath(fileUri);

        // 2. Read the content
        String content = Files.readString(filePath);
        System.out.println("Read from virtual URI: " + filePath);
        System.out.println("File content: " + content);
    }

    private static void testWebBalaPathOperations(FileSystemProvider provider) throws Exception {
        URI uri = URI.create("web-bala:///mydir/sample.txt");
        FileSystem fs = provider.getFileSystem(URI.create("web-bala:///"));
        Path path = fs.getPath("/mydir/sample.txt");

        // Read file content (assumes file exists)
        if (Files.exists(path)) {
            String content = Files.readString(path);
            System.out.println("File content: " + content);
        } else {
            System.out.println("File does not exist: " + path);
        }

        // Path operations
        Path parent = path.getParent(); // /mydir
        Path fileName = path.getFileName(); // sample.txt
        boolean isAbs = path.isAbsolute(); // true
        Path root = path.getRoot(); // /
        int count = path.getNameCount(); // 2
        Path sub = path.subpath(0, 1); // mydir
        Path resolved = parent.resolve("other.txt"); // /mydir/other.txt
        boolean ends = path.endsWith("sample.txt"); // true
        URI fileUri = path.toUri(); // web-bala:///mydir/sample.txt

        System.out.println("parent: " + parent);
        System.out.println("fileName: " + fileName);
        System.out.println("isAbsolute: " + isAbs);
        System.out.println("root: " + root);
        System.out.println("nameCount: " + count);
        System.out.println("subpath(0,1): " + sub);
        System.out.println("resolved: " + resolved);
        System.out.println("endsWith('sample.txt'): " + ends);
        System.out.println("toUri: " + fileUri);
    }
}