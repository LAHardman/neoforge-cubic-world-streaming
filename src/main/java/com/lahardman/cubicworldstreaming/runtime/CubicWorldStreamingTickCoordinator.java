package com.lahardman.cubicworldstreaming.runtime;

import java.time.Instant;

/**
 * Coordinates periodic maintenance tasks for cube systems.
 *
 * <p>Initial responsibilities:</p>
 * <ul>
 *   <li>Purge expired tickets</li>
 *   <li>Recompute cube desired states after ticket cleanup</li>
 * </ul>
 */
public class CubicWorldStreamingTickCoordinator {

    /**
     * How often (in server ticks) to run maintenance.
     *
     * <p>20 ticks ~= 1 second at 20 TPS.</p>
     */
    private static final int MAINTENANCE_INTERVAL_TICKS = 20;

    private int ticksSinceLastMaintenance = 0;

    public void onServerTick(CubicWorldStreamingServerRuntime runtime) {
        ticksSinceLastMaintenance++;
        if (ticksSinceLastMaintenance < MAINTENANCE_INTERVAL_TICKS) {
            return;
        }

        ticksSinceLastMaintenance = 0;
        runtime.cubeTicketManager().purgeExpiredTicketsAndRecompute(Instant.now());
    }
}
