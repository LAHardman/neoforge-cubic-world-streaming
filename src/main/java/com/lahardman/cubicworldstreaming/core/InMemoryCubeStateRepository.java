package com.lahardman.cubicworldstreaming.core;

import com.lahardman.cubicworldstreaming.api.CubeLifecycleState;
import com.lahardman.cubicworldstreaming.api.CubePosition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple thread-safe in-memory state repository.
 *
 * <p>Useful for early integration testing before persistence wiring is complete.</p>
 */
public class InMemoryCubeStateRepository implements CubeStateRepository {

    private final Map<CubePosition, CubeLifecycleState> cubeStateByPosition = new ConcurrentHashMap<>();

    @Override
    public CubeLifecycleState getCurrentState(CubePosition cubePosition) {
        return cubeStateByPosition.getOrDefault(cubePosition, CubeLifecycleState.INACTIVE);
    }

    @Override
    public void setCurrentState(CubePosition cubePosition, CubeLifecycleState newState) {
        cubeStateByPosition.put(cubePosition, newState);
    }
}
