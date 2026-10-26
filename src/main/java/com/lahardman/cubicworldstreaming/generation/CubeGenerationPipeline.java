package com.lahardman.cubicworldstreaming.generation;

import com.lahardman.cubicworldstreaming.api.CubeLifecycleState;
import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.storage.ApproxCubeData;
import com.lahardman.cubicworldstreaming.storage.CubeStorageService;
import com.lahardman.cubicworldstreaming.storage.FullCubeData;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Coordinates async cube generation/refinement work.
 *
 * <p>This class intentionally separates APPROX and FULL generation paths for clarity.
 * Cancellable and priority-aware scheduling can be layered on top later.</p>
 */
public class CubeGenerationPipeline {

    private final CubeStorageService cubeStorageService;
    private final CubeTerrainApproximator cubeTerrainApproximator;
    private final CubeFullGenerator cubeFullGenerator;
    private final Executor generationExecutor;

    public CubeGenerationPipeline(
        CubeStorageService cubeStorageService,
        CubeTerrainApproximator cubeTerrainApproximator,
        CubeFullGenerator cubeFullGenerator,
        Executor generationExecutor
    ) {
        this.cubeStorageService = Objects.requireNonNull(cubeStorageService);
        this.cubeTerrainApproximator = Objects.requireNonNull(cubeTerrainApproximator);
        this.cubeFullGenerator = Objects.requireNonNull(cubeFullGenerator);
        this.generationExecutor = Objects.requireNonNull(generationExecutor);
    }

    /**
     * Ensures cube has at least APPROX data.
     */
    public CompletableFuture<Void> ensureApproxGenerated(CubePosition cubePosition) {
        return CompletableFuture.runAsync(() -> {
            ApproxCubeData approxCubeData = cubeTerrainApproximator.generateApproximation(cubePosition);
            cubeStorageService.saveApproxCubeData(cubePosition, approxCubeData);
        }, generationExecutor);
    }

    /**
     * Ensures cube has FULL data (and may use approx as seed).
     */
    public CompletableFuture<Void> ensureFullGenerated(CubePosition cubePosition) {
        return CompletableFuture.runAsync(() -> {
            FullCubeData fullCubeData = cubeFullGenerator.generateFullCube(cubePosition);
            cubeStorageService.saveFullCubeData(cubePosition, fullCubeData);
        }, generationExecutor);
    }

    /**
     * Entry point for state-driven generation escalation.
     */
    public CompletableFuture<Void> ensureGeneratedForState(CubePosition cubePosition, CubeLifecycleState targetState) {
        return switch (targetState) {
            case INACTIVE -> CompletableFuture.completedFuture(null);
            case APPROX -> ensureApproxGenerated(cubePosition);
            case FULL -> ensureApproxGenerated(cubePosition).thenCompose(ignored -> ensureFullGenerated(cubePosition));
        };
    }
}
