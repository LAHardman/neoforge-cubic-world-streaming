package com.lahardman.cubicworldstreaming.storage;

/**
 * Lightweight far-distance representation.
 *
 * <p>Example content ideas:</p>
 * <ul>
 *   <li>Height extrema by x/z within cube footprint</li>
 *   <li>Dominant biome IDs</li>
 *   <li>Coarse density field samples</li>
 * </ul>
 */
public record ApproxCubeData(byte[] encodedApproximationPayload) {
}
