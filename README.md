# NeoForge Cubic World Streaming (Scaffold)

Experimental **NeoForge 26.3** scaffold for vertical cube-based loading, generation LOD, and simulation gating.

## Purpose
This repository provides heavily commented, readable skeleton code for:
- 3D cube coordinates and addressing
- 3D ticketing (`x, y, z, level, purpose`)
- Cube lifecycle states (`INACTIVE -> APPROX -> FULL`)
- Async generation pipeline stubs
- Simulation gating (tick/entity/block-entity guards)
- Basic debug overlay state dump hooks

## Status
This is a **design scaffold**, not production-ready gameplay code yet.

## Suggested implementation order
1. Implement persistence-backed `CubeStorageService`
2. Add real ticket sources (player/entity/portal)
3. Build async LOD transitions in `CubeGenerationPipeline`
4. Integrate simulation gates into server tick flow
5. Add profiling + boundary seam handling
