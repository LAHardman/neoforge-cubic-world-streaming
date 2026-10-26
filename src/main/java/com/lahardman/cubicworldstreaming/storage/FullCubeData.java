package com.lahardman.cubicworldstreaming.storage;

/**
 * Full near-distance representation.
 *
 * <p>In a real implementation this would wrap section block states, lighting, block entities,
 * and any additional metadata required for full simulation.</p>
 */
public record FullCubeData(byte[] encodedFullPayload) {
}
