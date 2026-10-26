package com.lahardman.cubicworldstreaming.generation;

import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.storage.ApproxCubeData;

/**
 * Produces lightweight terrain approximations for far-distance cubes.
 */
public interface CubeTerrainApproximator {

    ApproxCubeData generateApproximation(CubePosition cubePosition);
}
