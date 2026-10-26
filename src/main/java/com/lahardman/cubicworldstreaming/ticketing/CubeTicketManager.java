package com.lahardman.cubicworldstreaming/ticketing;

import com.lahardman.cubicworldstreaming.api.CubeLifecycleState;
import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.api.CubeTicket;
import com.lahardman.cubicworldstreaming.api.CubeTicketSourceKey;
import com.lahardman.cubicworldstreaming.core.CubeStateRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central manager for 3D cube tickets.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Track active tickets per cube</li>
 *   <li>Drop expired tickets</li>
 *   <li>Compute strongest pressure per cube</li>
 *   <li>Update desired lifecycle state in repository</li>
 *   <li>Support replacing/removing all tickets for a given source key (e.g., one player)</li>
 * </ul>
 */
public class CubeTicketManager {

    private final Map<CubePosition, List<CubeTicket>> ticketsByCubePosition = new ConcurrentHashMap<>();
    private final CubeLifecyclePolicy lifecyclePolicy;
    private final CubeStateRepository cubeStateRepository;

    public CubeTicketManager(CubeLifecyclePolicy lifecyclePolicy, CubeStateRepository cubeStateRepository) {
        this.lifecyclePolicy = lifecyclePolicy;
        this.cubeStateRepository = cubeStateRepository;
    }

    public void addTicket(CubeTicket cubeTicket) {
        ticketsByCubePosition
            .computeIfAbsent(cubeTicket.targetCubePosition(), ignored -> new ArrayList<>())
            .add(cubeTicket);

        recomputeStateForCube(cubeTicket.targetCubePosition(), Instant.now());
    }

    /**
     * Replaces all tickets from a specific source with a new ticket set.
     *
     * <p>This is ideal for player movement updates where we want the new footprint to atomically
     * supersede the old footprint.</p>
     */
    public synchronized void replaceTicketsForSource(CubeTicketSourceKey sourceKey, List<CubeTicket> replacementTickets, Instant now) {
        String sourceDescription = sourceKey.toSourceDescription();

        // Remove existing tickets from this source and track touched cubes for recomputation.
        Map<CubePosition, Boolean> touchedCubes = new HashMap<>();
        for (Map.Entry<CubePosition, List<CubeTicket>> entry : ticketsByCubePosition.entrySet()) {
            CubePosition cubePosition = entry.getKey();
            List<CubeTicket> cubeTickets = entry.getValue();

            boolean removedAny = cubeTickets.removeIf(ticket -> sourceDescription.equals(ticket.sourceDescription()));
            if (removedAny) {
                touchedCubes.put(cubePosition, Boolean.TRUE);
            }
        }

        // Add replacements.
        for (CubeTicket replacement : replacementTickets) {
            ticketsByCubePosition
                .computeIfAbsent(replacement.targetCubePosition(), ignored -> new ArrayList<>())
                .add(replacement);
            touchedCubes.put(replacement.targetCubePosition(), Boolean.TRUE);
        }

        // Recompute only affected cubes.
        for (CubePosition touchedCube : touchedCubes.keySet()) {
            recomputeStateForCube(touchedCube, now);
        }
    }

    /**
     * Removes all tickets from a given source and recomputes affected cubes.
     */
    public synchronized void removeTicketsForSource(CubeTicketSourceKey sourceKey, Instant now) {
        replaceTicketsForSource(sourceKey, List.of(), now);
    }

    public void purgeExpiredTicketsAndRecompute(Instant now) {
        for (Map.Entry<CubePosition, List<CubeTicket>> entry : ticketsByCubePosition.entrySet()) {
            CubePosition cubePosition = entry.getKey();
            List<CubeTicket> tickets = entry.getValue();
            tickets.removeIf(ticket -> ticket.isExpiredAt(now));
            recomputeStateForCube(cubePosition, now);
        }
    }

    public CubeTicket getStrongestTicketOrNull(CubePosition cubePosition, Instant now) {
        List<CubeTicket> tickets = ticketsByCubePosition.get(cubePosition);
        if (tickets == null || tickets.isEmpty()) {
            return null;
        }
        return tickets.stream()
            .filter(ticket -> !ticket.isExpiredAt(now))
            .min(Comparator.comparingInt(CubeTicket::ticketLevel))
            .orElse(null);
    }

    private void recomputeStateForCube(CubePosition cubePosition, Instant now) {
        CubeLifecycleState desiredState = computeDesiredState(cubePosition, now);
        cubeStateRepository.setCurrentState(cubePosition, desiredState);
    }

    private CubeLifecycleState computeDesiredState(CubePosition cubePosition, Instant now) {
        List<CubeTicket> tickets = ticketsByCubePosition.get(cubePosition);
        if (tickets == null || tickets.isEmpty()) {
            return CubeLifecycleState.INACTIVE;
        }

        int strongestLevel = tickets.stream()
            .filter(ticket -> !ticket.isExpiredAt(now))
            .mapToInt(CubeTicket::ticketLevel)
            .min()
            .orElse(Integer.MAX_VALUE);

        return lifecyclePolicy.desiredStateForLevel(strongestLevel);
    }
}
