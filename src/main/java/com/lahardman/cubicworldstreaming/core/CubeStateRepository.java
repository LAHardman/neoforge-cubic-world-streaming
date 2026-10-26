package com.lahardman.cubicworldstreaming.core;

import com.lahardman.cubicworldstreaming.api.CubeLifecycleState;
import com.lahardman.cubicworldstreaming.api.CubePosition;

/**
 * Read/write view for cube lifecycle state.
 *
 * <p>This abstraction allows swapping in-memory implementation for persistent/backed implementations
 * without rewriting ticketing or generation logic.</p>
 */
public interface CubeStateRepository {

    /**
     * Returns current known state for a cube.
     * Implementations should default unknown entries to INACTIVE.
     */
    CubeLifecycleState getCurrentState(CubePosition cubePosition);

    /**
     * Sets current state for a cube.
     */
    void setCurrentState(CubePosition cubePosition, CubeLifecycleState newState);
}
