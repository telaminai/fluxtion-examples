package com.telamin.fluxtion.example.sampleapps.state;

/**
 * User-defined filter that consults the checkpoint to skip already processed items.
 */
public class UserFilter {
    private final CheckpointFilter checkpoint;

    public UserFilter(CheckpointFilter checkpoint) {
        this.checkpoint = checkpoint;
    }

    /**
     * Allow only events with id strictly greater than the last processed id.
     */
    public boolean allow(LineEvent e) {
        return e.id() > checkpoint.lastProcessed();
    }
}
