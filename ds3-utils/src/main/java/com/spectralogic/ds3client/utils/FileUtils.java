package com.spectralogic.ds3client.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtils {
    private FileUtils() {}

    public static Path resolveForSymbolic(final Path path) throws IOException {
        if (Files.isSymbolicLink(path)) {
            final Path simLink = Files.readSymbolicLink(path);
            if (!simLink.isAbsolute()) {
                // Resolve the path such that the path is relative to the symbolically
                // linked file's directory
                final Path symLinkParent = path.toAbsolutePath().getParent();
                if (symLinkParent == null) {
                    throw new IOException("Could not resolve real path of symbolic link");
                }
                return symLinkParent.resolve(simLink);
            }

            return simLink;
        }
        return path;
    }
}