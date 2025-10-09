package com.telamin.fluxtion.example.sampleapps.state;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * Utility to append a new line to an input file for demo purposes.
 */
public final class DemoFileGenerator {
    private DemoFileGenerator() {}

    public static synchronized void appendLine(String inputPath) throws IOException {
        Path p = Path.of(inputPath);
        if (!Files.exists(p)) {
            Files.createDirectories(p.getParent());
            Files.createFile(p);
        }
        String line = "Generated @ " + LocalDateTime.now();
        String prefix = Files.size(p) == 0 ? "" : System.lineSeparator();
        Files.writeString(p, prefix + line, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
    }
}
