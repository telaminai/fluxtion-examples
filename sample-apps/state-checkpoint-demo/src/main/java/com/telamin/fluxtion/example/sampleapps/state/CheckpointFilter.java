package com.telamin.fluxtion.example.sampleapps.state;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple checkpoint that persists the last processed id to a text file.
 * The file contains a single integer line number.
 */
public class CheckpointFilter {
    private final Path checkpointFile;
    private final AtomicInteger lastProcessed = new AtomicInteger(0);

    public CheckpointFilter(String checkpointFilePath) {
        this.checkpointFile = Path.of(checkpointFilePath);
        try {
            if (Files.exists(checkpointFile)) {
                String s = Files.readString(checkpointFile, StandardCharsets.UTF_8).trim();
                if (!s.isEmpty()) {
                    lastProcessed.set(Integer.parseInt(s));
                }
            } else {
                Files.createDirectories(checkpointFile.getParent());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise checkpoint file: " + checkpointFilePath, e);
        }
    }

    public int lastProcessed() {
        return lastProcessed.get();
    }

    /**
     * Persist the id as the last processed value. Overwrites the file with the integer.
     */
    public synchronized void store(int id) {
        lastProcessed.set(id);
        try {
            Files.writeString(checkpointFile, Integer.toString(id), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist checkpoint to " + checkpointFile, e);
        }
    }
}
