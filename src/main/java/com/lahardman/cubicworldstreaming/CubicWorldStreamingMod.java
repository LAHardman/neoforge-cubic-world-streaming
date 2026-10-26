package com.lahardman.cubicworldstreaming;

import com.lahardman.cubicworldstreaming.command.CubeDebugCommand;
import com.lahardman.cubicworldstreaming.runtime.CubicWorldStreamingRuntimeManager;
import com.lahardman.cubicworldstreaming.runtime.CubicWorldStreamingTickCoordinator;
import com.lahardman.cubicworldstreaming.runtime.PlayerTicketUpdateCoordinator;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.time.Instant;

/**
 * NeoForge mod entrypoint.
 */
@Mod(CubicWorldStreamingMod.MOD_ID)
public class CubicWorldStreamingMod {

    public static final String MOD_ID = "cubicworldstreaming";

    private final CubicWorldStreamingTickCoordinator serverTickCoordinator = new CubicWorldStreamingTickCoordinator();
    private final PlayerTicketUpdateCoordinator playerTicketUpdateCoordinator = new PlayerTicketUpdateCoordinator();

    public CubicWorldStreamingMod(IEventBus modEventBus) {
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onServerStarted);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onServerStopped);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onServerTick);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedOut);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onServerStarted(ServerStartedEvent event) {
        CubicWorldStreamingRuntimeManager.getOrCreate(event.getServer());
    }

    private void onServerStopped(ServerStoppedEvent event) {
        var runtime = CubicWorldStreamingRuntimeManager.get(event.getServer());
        if (runtime != null) {
            playerTicketUpdateCoordinator.onServerStopped();
        }
        CubicWorldStreamingRuntimeManager.remove(event.getServer());
    }

    private void onServerTick(ServerTickEvent.Post event) {
        var runtime = CubicWorldStreamingRuntimeManager.get(event.getServer());
        if (runtime == null) {
            return;
        }

        serverTickCoordinator.onServerTick(runtime);
    }

    private void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (serverPlayer.level().isClientSide()) {
            return;
        }

        var runtime = CubicWorldStreamingRuntimeManager.get(serverPlayer.server);
        if (runtime == null) {
            return;
        }

        playerTicketUpdateCoordinator.onPlayerTick(serverPlayer, runtime, Instant.now());
    }

    private void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }

        var runtime = CubicWorldStreamingRuntimeManager.get(serverPlayer.server);
        if (runtime == null) {
            return;
        }

        playerTicketUpdateCoordinator.onPlayerDisconnect(serverPlayer.getUUID(), runtime, Instant.now());
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        CubeDebugCommand.register(event.getDispatcher());
    }
}
