package com.lahardman.cubicworldstreaming.runtime;

import com.lahardman.cubicworldstreaming.CubicWorldStreamingBootstrap;
import com.lahardman.cubicworldstreaming.debug.CubeDebugOverlayService;
import com.lahardman.cubicworldstreaming.ticketing.CubeTicketManager;

/**
 * Per-server runtime container.
 *
 * <p>Keep this object simple and explicit so all server-scoped services are discoverable in one place.</p>
 */
public class CubicWorldStreamingServerRuntime {

    private final CubicWorldStreamingBootstrap bootstrap;

    public CubicWorldStreamingServerRuntime() {
        this.bootstrap = new CubicWorldStreamingBootstrap();
    }

    public CubeTicketManager cubeTicketManager() {
        return bootstrap.cubeTicketManager();
    }

    public CubeDebugOverlayService cubeDebugOverlayService() {
        return bootstrap.cubeDebugOverlayService();
    }
}
