package com.lahardman.cubicworldstreaming.runtime;

import com.lahardman.cubicworldstreaming.CubicWorldStreamingBootstrap;
import com.lahardman.cubicworldstreaming.debug.CubeDebugOverlayService;
import com.lahardman.cubicworldstreaming.ticketing.CubeTicketManager;
import com.lahardman.cubicworldstreaming.ticketing.PlayerCubeTicketTracker;
import com.lahardman.cubicworldstreaming.ticketing.PlayerTicketEmissionConfig;
import com.lahardman.cubicworldstreaming.ticketing.PlayerTicketFootprintBuilder;

/**
 * Per-server runtime container.
 */
public class CubicWorldStreamingServerRuntime {

    private final CubicWorldStreamingBootstrap bootstrap;
    private final PlayerCubeTicketTracker playerCubeTicketTracker;

    public CubicWorldStreamingServerRuntime() {
        this.bootstrap = new CubicWorldStreamingBootstrap();
        this.playerCubeTicketTracker = new PlayerCubeTicketTracker(
            bootstrap.cubeTicketManager(),
            PlayerTicketEmissionConfig.defaultConfig(),
            new PlayerTicketFootprintBuilder()
        );
    }

    public CubeTicketManager cubeTicketManager() {
        return bootstrap.cubeTicketManager();
    }

    public CubeDebugOverlayService cubeDebugOverlayService() {
        return bootstrap.cubeDebugOverlayService();
    }

    public PlayerCubeTicketTracker playerCubeTicketTracker() {
        return playerCubeTicketTracker;
    }
}
