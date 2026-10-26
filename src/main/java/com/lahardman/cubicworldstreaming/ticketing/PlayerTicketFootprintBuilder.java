package com.lahardman.cubicworldstreaming.ticketing;

import com.lahardman.cubicworldstreaming.api.CubePosition;

import java.util.HashSet;
import java.util.Set;

/**
 * Builds cube sets for FULL and APPROX emission around a player center cube.
 */
public class PlayerTicketFootprintBuilder {

    public Set<CubePosition> buildFullFootprint(CubePosition centerCube, PlayerTicketEmissionConfig config) {
        return buildBox(
            centerCube,
            config.fullHorizontalRadiusInCubes(),
            config.fullVerticalRadiusInCubes()
        );
    }

    public Set<CubePosition> buildApproxFootprint(CubePosition centerCube, PlayerTicketEmissionConfig config) {
        return buildBox(
            centerCube,
            config.approxHorizontalRadiusInCubes(),
            config.approxVerticalRadiusInCubes()
        );
    }

    private Set<CubePosition> buildBox(CubePosition centerCube, int horizontalRadius, int verticalRadius) {
        Set<CubePosition> result = new HashSet<>();

        for (int deltaX = -horizontalRadius; deltaX <= horizontalRadius; deltaX++) {
            for (int deltaY = -verticalRadius; deltaY <= verticalRadius; deltaY++) {
                for (int deltaZ = -horizontalRadius; deltaZ <= horizontalRadius; deltaZ++) {
                    result.add(new CubePosition(
                        centerCube.cubeX() + deltaX,
                        centerCube.cubeY() + deltaY,
                        centerCube.cubeZ() + deltaZ
                    ));
                }
            }
        }

        return result;
    }
}
