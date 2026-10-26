package com.lahardman.cubicworldstreaming.debug;

import com.lahardman.cubicworldstreaming.api.CubeLifecycleState;
import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.core.CubeStateRepository;

/**
 * Simple debug surface used to expose cube states.
 *
 * <p>Later this can be wired to on-screen debug HUD, commands, or logs.</p>
 */
public class CubeDebugOverlayService {

    private final CubeStateRepository cubeStateRepository;

    public CubeDebugOverlayService(CubeStateRepository cubeStateRepository) {
        this.cubeStateRepository = cubeStateRepository;
    }

    /**
     * Returns a readable one-line status string for a cube.
     */
    public String describeCube(CubePosition cubePosition) {
        CubeLifecycleState currentState = cubeStateRepository.getCurrentState(cubePosition);
        return "Cube " + cubePosition + " state=" + currentState;
    }
}
