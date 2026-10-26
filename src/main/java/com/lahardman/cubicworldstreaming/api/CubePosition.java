package com.lahardman.cubicworldstreaming.api;

/**
 * Represents a 3D cube address in cube-space (not block-space).
 *
 * <p>Each coordinate maps to one cube of {@link #CUBE_EDGE_LENGTH_IN_BLOCKS} blocks per edge.
 * Keeping this type immutable avoids subtle bugs in ticketing and map-key usage.</p>
 */
public record CubePosition(int cubeX, int cubeY, int cubeZ) {

    /**
     * Edge length of a cube in blocks.
     *
     * <p>Using a named constant keeps all conversions explicit and readable.</p>
     */
    public static final int CUBE_EDGE_LENGTH_IN_BLOCKS = 16;

    /**
     * Creates a cube position from block coordinates by floor-dividing by cube size.
     */
    public static CubePosition fromBlockCoordinates(int blockX, int blockY, int blockZ) {
        return new CubePosition(
            floorDiv(blockX, CUBE_EDGE_LENGTH_IN_BLOCKS),
            floorDiv(blockY, CUBE_EDGE_LENGTH_IN_BLOCKS),
            floorDiv(blockZ, CUBE_EDGE_LENGTH_IN_BLOCKS)
        );
    }

    /**
     * Converts this cube position to the minimum corner block coordinates.
     */
    public BlockPosition toMinimumBlockPosition() {
        return new BlockPosition(
            cubeX * CUBE_EDGE_LENGTH_IN_BLOCKS,
            cubeY * CUBE_EDGE_LENGTH_IN_BLOCKS,
            cubeZ * CUBE_EDGE_LENGTH_IN_BLOCKS
        );
    }

    /**
     * Manhattan distance in cube units.
     *
     * <p>This is useful for simple ring-based ticket falloff.</p>
     */
    public int manhattanDistanceTo(CubePosition otherCubePosition) {
        int deltaX = Math.abs(this.cubeX - otherCubePosition.cubeX());
        int deltaY = Math.abs(this.cubeY - otherCubePosition.cubeY());
        int deltaZ = Math.abs(this.cubeZ - otherCubePosition.cubeZ());
        return deltaX + deltaY + deltaZ;
    }

    private static int floorDiv(int value, int divisor) {
        return Math.floorDiv(value, divisor);
    }
}
