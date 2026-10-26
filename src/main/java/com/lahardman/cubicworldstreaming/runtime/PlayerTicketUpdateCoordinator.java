package com.lahardman.cubicworldstreaming.runtime;

import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles movement-sensitive player ticket updates.
 *
 * <p>To avoid heavy recalculation every tick, we only refresh a player's tickets when the player
 * changes cube coordinates.</p>
 */
public class PlayerTicketUpdateCoordinator {

    private final Map<UUID, String> lastKnownCubeKeyByPlayer = new HashMap<>();

    public void onPlayerTick(ServerPlayer player, CubicWorldStreamingServerRuntime runtime, Instant now) {
        int blockX = player.blockPosition().getX();
        int blockY = player.blockPosition().getY();
        int blockZ = player.blockPosition().getZ();

        int cubeX = Math.floorDiv(blockX, 16);
        int cubeY = Math.floorDiv(blockY, 16);
        int cubeZ = Math.floorDiv(blockZ, 16);

        String cubeKey = cubeX + "," + cubeY + "," + cubeZ;
        UUID playerUuid = player.getUUID();

        String previousCubeKey = lastKnownCubeKeyByPlayer.get(playerUuid);
        if (cubeKey.equals(previousCubeKey)) {
            return;
        }

        lastKnownCubeKeyByPlayer.put(playerUuid, cubeKey);
        runtime.playerCubeTicketTracker().refreshPlayerTickets(player, now);
    }

    public void onPlayerDisconnect(UUID playerUuid, CubicWorldStreamingServerRuntime runtime, Instant now) {
        lastKnownCubeKeyByPlayer.remove(playerUuid);
        runtime.playerCubeTicketTracker().clearPlayerTickets(playerUuid, now);
    }

    public void onServerStopped() {
        lastKnownCubeKeyByPlayer.clear();
    }
}
