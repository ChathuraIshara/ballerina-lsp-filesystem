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
    private final WebBalaFileSystem fileSystem;
    private final String path;

    public WebBalaPath(WebBalaFileSystem fileSystem, String path) {
        this.fileSystem = fileSystem;
        this.path = path;
    }

    @Override
    public String toString() {
        return path; // or however you store the path string internally
    }

    @Override
    public int compareTo(Path other) {
        return this.toString().compareTo(other.toString());
    }

    @Override
    public boolean endsWith(Path other) {
        return this.toString().endsWith(other.toString());
    }

    @Override
    public boolean endsWith(String other) {
        return this.toString().endsWith(other);
    }

    @Override
    public Path getFileName() {
        String normalized = path;
        if (normalized == null || normalized.isEmpty() || normalized.equals("/")) {
            return null;
        }
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        int lastSlash = normalized.lastIndexOf('/');
        String fileName = (lastSlash >= 0) ? normalized.substring(lastSlash + 1) : normalized;
        return new WebBalaPath(fileSystem, fileName);
    }

    @Override
    public FileSystem getFileSystem() {
        return fileSystem; // Return the associated WebBalaFileSystem instance
    }

    @Override
    public Path getName(int index) {
        String normalized = path;
        if (normalized == null || normalized.isEmpty() || normalized.equals("/")) {
            throw new IllegalArgumentException("No name elements in root or empty path");
        }
        // Remove leading and trailing slashes
        if (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        String[] parts = normalized.isEmpty() ? new String[0] : normalized.split("/");
        if (index < 0 || index >= parts.length) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return new WebBalaPath((WebBalaFileSystem) getFileSystem(), parts[index]);
    }

    @Override
    public int getNameCount() {
        String normalized = path;
        System.out.println("getNameCount called with path: " + normalized);
        if (normalized == null || normalized.isEmpty() || normalized.equals("/")) {
            return 0;
        }
        // Remove leading slash if present
        if (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        // Remove trailing slash if present
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        if (normalized.isEmpty()) {
            return 0;
        }
        System.out.println("getNameCount: " + normalized.split("/"));
        return normalized.split("/").length;
    }

    @Override
    public Path getParent() {
        if (path == null || path.isEmpty() || path.equals("/")) {
            return null; // root or empty has no parent
        }
        String normalized = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        int lastSlash = normalized.lastIndexOf('/');
        if (lastSlash < 0) {
            return null; // no parent
        }
        String parentPath = (lastSlash == 0) ? "/" : normalized.substring(0, lastSlash);
        return new WebBalaPath((WebBalaFileSystem) getFileSystem(), parentPath);
    }

    @Override
    public Path getRoot() {
        return path.startsWith("/") ? new WebBalaPath(fileSystem, "/") : null;
    }

    @Override
    public boolean isAbsolute() {
        return path.startsWith("/"); // Assuming absolute paths start with '/'
    }

    @Override
    public Path normalize() {
        String[] parts = path.split("/");
        java.util.Deque<String> stack = new java.util.ArrayDeque<>();
        for (String part : parts) {
            if (part.isEmpty() || part.equals("."))
                continue;
            if (part.equals("..")) {
                if (!stack.isEmpty())
                    stack.removeLast();
            } else {
                stack.addLast(part);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (isAbsolute())
            sb.append("/");
        sb.append(String.join("/", stack));
        return new WebBalaPath(fileSystem, sb.toString());
    }

    @Override
    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public Path relativize(Path other) {
        String thisPath = this.toAbsolutePath().toString();
        String otherPath = other.toAbsolutePath().toString();
        System.out.println("Relativizing paths: thisPath=" + thisPath + ", otherPath=" + otherPath);

        // Remove leading slash for easier splitting
        if (thisPath.startsWith("/"))
            thisPath = thisPath.substring(1);
        if (otherPath.startsWith("/"))
            otherPath = otherPath.substring(1);

        String[] thisParts = thisPath.isEmpty() ? new String[0] : thisPath.split("/");
        String[] otherParts = otherPath.isEmpty() ? new String[0] : otherPath.split("/");

        // Find common prefix
        int i = 0;
        while (i < thisParts.length && i < otherParts.length && thisParts[i].equals(otherParts[i])) {
            i++;
        }

        // For each remaining part in this, add ".."
        StringBuilder rel = new StringBuilder();
        for (int j = i; j < thisParts.length; j++) {
            if (rel.length() > 0)
                rel.append("/");
            rel.append("..");
        }
        // For each remaining part in other, add the part
        for (int j = i; j < otherParts.length; j++) {
            if (rel.length() > 0)
                rel.append("/");
            rel.append(otherParts[j]);
        }

        return new WebBalaPath((WebBalaFileSystem) getFileSystem(), rel.toString());
    }

    @Override
    public Path resolve(Path other) {
        if (other.isAbsolute()) {
            return other;
        }
        String thisPath = this.path;
        String otherPath = other.toString();
        if (thisPath.endsWith("/")) {
            System.out
                    .println("Resolving paths with trailing slash: thisPath=" + thisPath + ", otherPath=" + otherPath);
            return new WebBalaPath((WebBalaFileSystem) getFileSystem(), thisPath + otherPath);
        } else if (thisPath.isEmpty() || thisPath.equals("/")) {
            System.out.println(
                    "Resolving paths with empty or root thisPath: thisPath=" + thisPath + ", otherPath=" + otherPath);
            return new WebBalaPath((WebBalaFileSystem) getFileSystem(), "/" + otherPath);
        } else {
            System.out.println("Resolving paths: thisPath=" + thisPath + ", otherPath=" + otherPath);
            return new WebBalaPath((WebBalaFileSystem) getFileSystem(), thisPath + "/" + otherPath);
        }
    }

    @Override
    public boolean startsWith(Path other) {
        return this.toString().startsWith(other.toString());
    }

    @Override
    public boolean startsWith(String other) {
        return this.toString().startsWith(other);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        String normalized = path;
        if (normalized.startsWith("/"))
            normalized = normalized.substring(1);
        if (normalized.endsWith("/"))
            normalized = normalized.substring(0, normalized.length() - 1);
        String[] parts = normalized.isEmpty() ? new String[0] : normalized.split("/");
        if (beginIndex < 0 || endIndex > parts.length || beginIndex >= endIndex) {
            throw new IllegalArgumentException("Invalid subpath range");
        }
        String sub = String.join("/", java.util.Arrays.copyOfRange(parts, beginIndex, endIndex));
        return new WebBalaPath(fileSystem, sub);
    }

    @Override
    public Path toAbsolutePath() {
        if (isAbsolute()) {
            return this;
        } else {
            // Prepend the root ("/") to make it absolute
            return new WebBalaPath((WebBalaFileSystem) getFileSystem(), "/" + path);
        }
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        // For a virtual FS, just return the normalized absolute path
        return toAbsolutePath().normalize();
    }

    @Override
    public URI toUri() {
        // Build a URI with the custom scheme
        String scheme = fileSystem.provider().getScheme();
        String p = isAbsolute() ? path : "/" + path;
        return URI.create(scheme + "://" + p);
    }

    @Override
    public java.util.Iterator<Path> iterator() {
        String normalized = path;
        if (normalized.startsWith("/"))
            normalized = normalized.substring(1);
        if (normalized.endsWith("/"))
            normalized = normalized.substring(0, normalized.length() - 1);
        String[] parts = normalized.isEmpty() ? new String[0] : normalized.split("/");
        java.util.List<Path> list = new java.util.ArrayList<>();
        for (String part : parts) {
            list.add(new WebBalaPath(fileSystem, part));
        }
        return list.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof WebBalaPath))
            return false;
        WebBalaPath other = (WebBalaPath) obj;
        return this.fileSystem.equals(other.fileSystem)
                && this.normalize().toString().equals(other.normalize().toString());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(fileSystem, normalize().toString());
    }
    // Implement Path interface methods
}