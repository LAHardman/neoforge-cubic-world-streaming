package com.lahardman.cubicworldstreaming.ticketing;

/**
 * Readable configuration for player-driven cube ticket emission.
 *
 * <p>Rings are emitted in true 3D (x/y/z cube-space), allowing vertical streaming focus.</p>
 */
public class PlayerTicketEmissionConfig {

    private final int fullHorizontalRadiusInCubes;
    private final int fullVerticalRadiusInCubes;
    private final int approxHorizontalRadiusInCubes;
    private final int approxVerticalRadiusInCubes;

    private final int fullTicketLevel;
    private final int approxTicketLevel;

    public PlayerTicketEmissionConfig(
        int fullHorizontalRadiusInCubes,
        int fullVerticalRadiusInCubes,
        int approxHorizontalRadiusInCubes,
        int approxVerticalRadiusInCubes,
        int fullTicketLevel,
        int approxTicketLevel
    ) {
        this.fullHorizontalRadiusInCubes = fullHorizontalRadiusInCubes;
        this.fullVerticalRadiusInCubes = fullVerticalRadiusInCubes;
        this.approxHorizontalRadiusInCubes = approxHorizontalRadiusInCubes;
        this.approxVerticalRadiusInCubes = approxVerticalRadiusInCubes;
        this.fullTicketLevel = fullTicketLevel;
        this.approxTicketLevel = approxTicketLevel;
    }

    public static PlayerTicketEmissionConfig defaultConfig() {
        return new PlayerTicketEmissionConfig(
            3,
            2,
            7,
            4,
            10,
            30
        );
    }

    public int fullHorizontalRadiusInCubes() {
        return fullHorizontalRadiusInCubes;
    }

    public int fullVerticalRadiusInCubes() {
        return fullVerticalRadiusInCubes;
    }

    public int approxHorizontalRadiusInCubes() {
        return approxHorizontalRadiusInCubes;
    }

    public int approxVerticalRadiusInCubes() {
        return approxVerticalRadiusInCubes;
    }

    public int fullTicketLevel() {
        return fullTicketLevel;
    }

    public int approxTicketLevel() {
        return approxTicketLevel;
    }
}
