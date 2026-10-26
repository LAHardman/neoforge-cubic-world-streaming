package com.lahardman.cubicworldstreaming.simulation;

import com.lahardman.cubicworldstreaming.api.CubeLifecycleState;
import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.core.CubeStateRepository;

/**
 * Central policy checks for whether expensive simulation work is allowed for a cube.
 *
 * <p>Integrate calls from entity tick loops, block entity tickers, random tick dispatch,
 * and other simulation-heavy hooks.</p>
 */
public class CubeSimulationGate {

    private final CubeStateRepository cubeStateRepository;

    public CubeSimulationGate(CubeStateRepository cubeStateRepository) {
        this.cubeStateRepository = cubeStateRepository;
    }

    /**
     * True only when cube is FULL.
     */
    public boolean isFullSimulationAllowed(CubePosition cubePosition) {
        CubeLifecycleState currentState = cubeStateRepository.getCurrentState(cubePosition);
        return currentState == CubeLifecycleState.FULL;
    }

    /**
     * True for operations that can run in APPROX or FULL (lightweight logic only).
     */
    public boolean isApproxOrFullAllowed(CubePosition cubePosition) {
        CubeLifecycleState currentState = cubeStateRepository.getCurrentState(cubePosition);
        return currentState == CubeLifecycleState.APPROX || currentState == CubeLifecycleState.FULL;
    }
}
