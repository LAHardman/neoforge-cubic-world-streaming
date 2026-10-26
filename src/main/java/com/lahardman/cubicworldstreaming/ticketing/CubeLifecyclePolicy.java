package com.lahardman.cubicworldstreaming.ticketing;

import com.lahardman.cubicworldstreaming.api.CubeLifecycleState;

/**
 * Converts ticket pressure (numeric level) into desired lifecycle state.
 *
 * <p>Keep thresholds centralized here so balancing is simple and readable.</p>
 */
public class CubeLifecyclePolicy {

    private final int fullThresholdInclusive;
    private final int approxThresholdInclusive;

    /**
     * @param fullThresholdInclusive ticket levels <= this become FULL
     * @param approxThresholdInclusive ticket levels <= this become APPROX (unless FULL)
     */
    public CubeLifecyclePolicy(int fullThresholdInclusive, int approxThresholdInclusive) {
        if (fullThresholdInclusive > approxThresholdInclusive) {
            throw new IllegalArgumentException("full threshold must be <= approx threshold");
        }
        this.fullThresholdInclusive = fullThresholdInclusive;
        this.approxThresholdInclusive = approxThresholdInclusive;
    }

    /**
     * Determines desired state from the strongest applicable ticket level.
     */
    public CubeLifecycleState desiredStateForLevel(int strongestTicketLevel) {
        if (strongestTicketLevel <= fullThresholdInclusive) {
            return CubeLifecycleState.FULL;
        }
        if (strongestTicketLevel <= approxThresholdInclusive) {
            return CubeLifecycleState.APPROX;
        }
        return CubeLifecycleState.INACTIVE;
    }
}
