package com.lahardman.cubicworldstreaming.runtime;

import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks active server runtimes.
 *
 * <p>Using a dedicated manager avoids static globals in service code and makes lifecycle cleanup explicit.</p>
 */
public final class CubicWorldStreamingRuntimeManager {

    private static final Map<MinecraftServer, CubicWorldStreamingServerRuntime> RUNTIME_BY_SERVER = new ConcurrentHashMap<>();

    private CubicWorldStreamingRuntimeManager() {
    }

    /**
     * Creates runtime if missing and returns it.
     */
    public static CubicWorldStreamingServerRuntime getOrCreate(MinecraftServer server) {
        return RUNTIME_BY_SERVER.computeIfAbsent(server, ignored -> new CubicWorldStreamingServerRuntime());
    }

    /**
     * Returns runtime or null if not initialized.
     */
    public static CubicWorldStreamingServerRuntime get(MinecraftServer server) {
        return RUNTIME_BY_SERVER.get(server);
    }

    /**
     * Removes runtime during server shutdown.
     */
    public static void remove(MinecraftServer server) {
        RUNTIME_BY_SERVER.remove(server);
    }
}
