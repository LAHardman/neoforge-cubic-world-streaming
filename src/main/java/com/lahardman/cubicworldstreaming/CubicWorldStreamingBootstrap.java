package com.lahardman.cubicworldstreaming;

import com.lahardman.cubicworldstreaming.core.CubeStateRepository;
import com.lahardman.cubicworldstreaming.core.InMemoryCubeStateRepository;
import com.lahardman.cubicworldstreaming.debug.CubeDebugOverlayService;
import com.lahardman.cubicworldstreaming.ticketing.CubeLifecyclePolicy;
import com.lahardman.cubicworldstreaming.ticketing.CubeTicketManager;

/**
 * Temporary bootstrap container for wiring core services.
 *
 * <p>In actual NeoForge integration, this should be replaced by proper lifecycle setup
 * in mod initialization and server-level service registries.</p>
 */
public class CubicWorldStreamingBootstrap {

    private final CubeStateRepository cubeStateRepository;
    private final CubeTicketManager cubeTicketManager;
    private final CubeDebugOverlayService cubeDebugOverlayService;

    public CubicWorldStreamingBootstrap() {
        this.cubeStateRepository = new InMemoryCubeStateRepository();

        CubeLifecyclePolicy cubeLifecyclePolicy = new CubeLifecyclePolicy(
            10, // <= 10 becomes FULL
            30  // <= 30 becomes APPROX
        );

        this.cubeTicketManager = new CubeTicketManager(cubeLifecyclePolicy, cubeStateRepository);
        this.cubeDebugOverlayService = new CubeDebugOverlayService(cubeStateRepository);
    }

    public CubeStateRepository cubeStateRepository() {
        return cubeStateRepository;
    }

    public CubeTicketManager cubeTicketManager() {
        return cubeTicketManager;
    }

    public CubeDebugOverlayService cubeDebugOverlayService() {
        return cubeDebugOverlayService;
    }
}
