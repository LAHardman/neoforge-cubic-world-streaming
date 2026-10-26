package com.lahardman.cubicworldstreaming.generation;

import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.storage.FullCubeData;

/**
 * Produces full-fidelity cube data for near-distance simulation.
 */
public interface CubeFullGenerator {

    FullCubeData generateFullCube(CubePosition cubePosition);
}
