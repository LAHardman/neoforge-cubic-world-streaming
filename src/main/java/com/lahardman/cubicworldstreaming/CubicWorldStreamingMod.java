package com.lahardman.cubicworldstreaming;

import com.lahardman.cubicworldstreaming.command.CubeDebugCommand;
import com.lahardman.cubicworldstreaming.runtime.CubicWorldStreamingRuntimeManager;
import com.lahardman.cubicworldstreaming.runtime.CubicWorldStreamingTickCoordinator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * NeoForge mod entrypoint.
 *
 * <p>This class is intentionally explicit and heavily commented so lifecycle flow is easy to follow.</p>
 */
@Mod(CubicWorldStreamingMod.MOD_ID)
public class CubicWorldStreamingMod {

    public static final String MOD_ID = "cubicworldstreaming";

    private final CubicWorldStreamingTickCoordinator tickCoordinator = new CubicWorldStreamingTickCoordinator();

    public CubicWorldStreamingMod(IEventBus modEventBus) {
        // Register server lifecycle + tick + command listeners on NeoForge main event bus.
        // Using static NeoForge.EVENT_BUS keeps this simple for initial scaffolding.
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onServerStarted);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onServerStopped);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onServerTick);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onServerStarted(ServerStartedEvent event) {
        // Ensure per-server runtime exists as soon as server is up.
        CubicWorldStreamingRuntimeManager.getOrCreate(event.getServer());
    }

    private void onServerStopped(ServerStoppedEvent event) {
        // Explicit cleanup avoids accidental memory retention between dev runs.
        CubicWorldStreamingRuntimeManager.remove(event.getServer());
    }

    private void onServerTick(ServerTickEvent.Post event) {
        var runtime = CubicWorldStreamingRuntimeManager.get(event.getServer());
        if (runtime == null) {
            return;
        }

        tickCoordinator.onServerTick(runtime);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        CubeDebugCommand.register(event.getDispatcher());
    }
}
