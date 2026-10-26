package com.lahardman.cubicworldstreaming.ticketing;

import com.lahardman.cubicworldstreaming.api.CubeLifecycleState;
import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.api.CubeTicket;
import com.lahardman.cubicworldstreaming.core.CubeStateRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
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

    /**
     * Adds a new ticket and immediately recomputes desired state for that cube.
     */
    public void addTicket(CubeTicket cubeTicket) {
        ticketsByCubePosition
            .computeIfAbsent(cubeTicket.targetCubePosition(), ignored -> new ArrayList<>())
            .add(cubeTicket);

        recomputeStateForCube(cubeTicket.targetCubePosition(), Instant.now());
    }

    /**
     * Removes expired tickets across all cubes and recomputes affected states.
     */
    public void purgeExpiredTicketsAndRecompute(Instant now) {
        for (Map.Entry<CubePosition, List<CubeTicket>> entry : ticketsByCubePosition.entrySet()) {
            CubePosition cubePosition = entry.getKey();
            List<CubeTicket> tickets = entry.getValue();
            tickets.removeIf(ticket -> ticket.isExpiredAt(now));
            recomputeStateForCube(cubePosition, now);
        }
    }

    /**
     * Returns the currently strongest (lowest-level) ticket for observability/debug use.
     */
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
