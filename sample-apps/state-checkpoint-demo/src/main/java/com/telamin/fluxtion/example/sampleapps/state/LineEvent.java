package com.telamin.fluxtion.example.sampleapps.state;

/**
 * Simple immutable event containing a sequential id (line number) and the line text.
 */
public record LineEvent(int id, String line) {}
