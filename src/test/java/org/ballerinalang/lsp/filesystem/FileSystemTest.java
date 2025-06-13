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
        // Path tempDir = Files.createTempDirectory("web-bala-test");
        // System.out.println("Temporary directory created: " + tempDir);
        // Files.createDirectories(tempDir.resolve("src"));
        // Files.writeString(tempDir.resolve("src/test.bala"), "test content");

        // 2. Register your file system
        FileSystem fs = FileSystems.newFileSystem(URI.create("web-bala:///"), Map.of());

        // 2. Get the provider from the file system
        FileSystemProvider provider = fs.provider();
        // System.out.println("Mapped web-bala:/// to real path: " +provider. );

        // 3. Test basic operations
         testPathResolution(provider);
        // testFileOperations();
        // testWebBalaUriToFileCreation(provider);
        // testUpdateExistingWebBalaFile(provider);
        //testRootDirectories(provider);

        System.out.println("All tests passed!");
    }

    private static void testPathResolution(FileSystemProvider provider) throws Exception {
        URI uri = URI.create("web-bala:///src/test.bala");
        Path path = provider.getPath(uri);
        System.out.println("Testing path resolution...");
        System.out.println("Virtual path: " + uri);
        System.out.println("Resolved after provider getpath: " + path);
        // System.out.println("file system: of "+uri + provider.getFileSystem(uri));

        // System.out.println("Virtual path: " + uri);
        // System.out.println("Real path: " + path);
        // System.out.println("File exists: " + Files.exists(path));
    }

    private static void testFileOperations() throws Exception {
        System.out.println("Testing file operations...");
        Path virtualPath = Paths.get(URI.create("web-bala:///src/test.bala"));
        System.out.println("Virtual path in testFileOperations: " + virtualPath);

        // Read content
        String content = Files.readString(virtualPath);
        System.out.println("File content: " + content);

        // Write new content
        Files.writeString(virtualPath, "new content");
        System.out.println("File updated successfully");
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

}