package com.lahardman.cubicworldstreaming.ticketing;

import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.api.CubeTicket;
import com.lahardman.cubicworldstreaming.api.CubeTicketPurpose;
import com.lahardman.cubicworldstreaming.api.CubeTicketSourceKey;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Emits player-driven 3D tickets and updates them when players cross cube boundaries.
 */
public class PlayerCubeTicketTracker {

    private final CubeTicketManager cubeTicketManager;
    private final PlayerTicketEmissionConfig emissionConfig;
    private final PlayerTicketFootprintBuilder footprintBuilder;

    public PlayerCubeTicketTracker(
        CubeTicketManager cubeTicketManager,
        PlayerTicketEmissionConfig emissionConfig,
        PlayerTicketFootprintBuilder footprintBuilder
    ) {
        this.cubeTicketManager = cubeTicketManager;
        this.emissionConfig = emissionConfig;
        this.footprintBuilder = footprintBuilder;
    }

    /**
     * Called when player joins/tracked or when they move.
     */
    public void refreshPlayerTickets(ServerPlayer player, Instant now) {
        CubePosition centerCube = CubePosition.fromBlockCoordinates(
            player.blockPosition().getX(),
            player.blockPosition().getY(),
            player.blockPosition().getZ()
        );

        CubeTicketSourceKey sourceKey = CubeTicketSourceKey.player(player.getUUID().toString());
        List<CubeTicket> replacementTickets = buildTicketsForCenterCube(sourceKey, centerCube, now);
        cubeTicketManager.replaceTicketsForSource(sourceKey, replacementTickets, now);
    }

    /**
     * Called when player disconnects.
     */
    public void clearPlayerTickets(UUID playerUuid, Instant now) {
        CubeTicketSourceKey sourceKey = CubeTicketSourceKey.player(playerUuid.toString());
        cubeTicketManager.removeTicketsForSource(sourceKey, now);
    }

    private List<CubeTicket> buildTicketsForCenterCube(
        CubeTicketSourceKey sourceKey,
        CubePosition centerCube,
        Instant now
    ) {
        List<CubeTicket> allTickets = new ArrayList<>();

        var fullFootprint = footprintBuilder.buildFullFootprint(centerCube, emissionConfig);
        for (CubePosition cubePosition : fullFootprint) {
            allTickets.add(sourceKey.buildTicket(
                cubePosition,
                emissionConfig.fullTicketLevel(),
                CubeTicketPurpose.SIMULATION_RELEVANCE,
                now,
                null
            ));
        }

        var approxFootprint = footprintBuilder.buildApproxFootprint(centerCube, emissionConfig);
        for (CubePosition cubePosition : approxFootprint) {
            allTickets.add(sourceKey.buildTicket(
                cubePosition,
                emissionConfig.approxTicketLevel(),
                CubeTicketPurpose.RENDER_RELEVANCE,
                now,
                null
            ));
        }

        return allTickets;
    }
}
