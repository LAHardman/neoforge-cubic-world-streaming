package com.lahardman.cubicworldstreaming.api;

import com.lahardman.cubicworldstreaming.ticketing.CubeTicketPurpose;

import java.time.Instant;

/**
 * A 3D ticket describing desired lifecycle pressure for a cube.
 *
 * @param targetCubePosition cube target
 * @param ticketLevel lower means stronger priority (similar to vanilla distance-manager semantics)
 * @param ticketPurpose why this ticket exists
 * @param sourceDescription readable source label (e.g., "player:UUID", "portal:nether_gate_12")
 * @param createdAt when this ticket was created
 * @param expiresAt nullable expiration timestamp; null means persistent until explicit removal
 */
public record CubeTicket(
    CubePosition targetCubePosition,
    int ticketLevel,
    CubeTicketPurpose ticketPurpose,
    String sourceDescription,
    Instant createdAt,
    Instant expiresAt
) {

    /**
     * Checks if the ticket is expired at the supplied instant.
     */
    public boolean isExpiredAt(Instant now) {
        return expiresAt != null && now.isAfter(expiresAt);
    }
}
