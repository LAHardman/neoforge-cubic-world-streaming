package com.lahardman.cubicworldstreaming.ticketing;

/**
 * Reason a cube was requested/kept active.
 *
 * <p>Purpose-aware ticketing allows separate policies for rendering, simulation, and generation prefetch.</p>
 */
public enum CubeTicketPurpose {
    RENDER_RELEVANCE,
    SIMULATION_RELEVANCE,
    WORLDGEN_PREFETCH,
    STRUCTURE_CONTINUITY,
    PORTAL_CONTINUITY
}
