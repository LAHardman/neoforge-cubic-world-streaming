package com.lahardman.cubicworldstreaming.api;

import java.time.Instant;
import java.util.Objects;

/**
 * Stable key that identifies a ticket source for precise replacement/removal.
 */
public record CubeTicketSourceKey(String sourceType, String sourceId) {

    public CubeTicketSourceKey {
        Objects.requireNonNull(sourceType, "sourceType");
        Objects.requireNonNull(sourceId, "sourceId");
    }

    public String toSourceDescription() {
        return sourceType + ":" + sourceId;
    }

    public static CubeTicketSourceKey player(String playerUuidString) {
        return new CubeTicketSourceKey("player", playerUuidString);
    }

    public CubeTicket buildTicket(
        CubePosition targetCubePosition,
        int ticketLevel,
        CubeTicketPurpose ticketPurpose,
        Instant createdAt,
        Instant expiresAt
    ) {
        return new CubeTicket(targetCubePosition, ticketLevel, ticketPurpose, toSourceDescription(), createdAt, expiresAt);
    }
}
