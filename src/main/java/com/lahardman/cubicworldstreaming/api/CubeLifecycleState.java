package com.lahardman.cubicworldstreaming.api;

/**
 * High-level lifecycle state for an individual cube.
 *
 * <p>State transitions should be controlled by a single coordinator to avoid race conditions.
 * Suggested transitions:
 * INACTIVE -> APPROX -> FULL and FULL -> APPROX -> INACTIVE (with hysteresis).</p>
 */
public enum CubeLifecycleState {
    /**
     * Cube is not actively simulated or fully generated.
     * Minimal metadata may still be retained in storage.
     */
    INACTIVE,

    /**
     * Cube has approximate terrain representation suitable for far-distance visuals/planning.
     * Expensive simulation should remain disabled.
     */
    APPROX,

    /**
     * Cube is fully generated, lit, and eligible for complete simulation.
     */
    FULL
}
