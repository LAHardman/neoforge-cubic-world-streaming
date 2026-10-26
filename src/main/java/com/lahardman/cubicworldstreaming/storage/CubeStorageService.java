package com.lahardman.cubicworldstreaming.storage;

import com.lahardman.cubicworldstreaming.api.CubePosition;

import java.util.Optional;

/**
 * Storage abstraction for cube payloads.
 *
 * <p>The payload model is intentionally split into FULL and APPROX representations so
 * far-distance cubes can remain lightweight.</p>
 */
public interface CubeStorageService {

    Optional<ApproxCubeData> loadApproxCubeData(CubePosition cubePosition);

    Optional<FullCubeData> loadFullCubeData(CubePosition cubePosition);

    void saveApproxCubeData(CubePosition cubePosition, ApproxCubeData approxCubeData);

    void saveFullCubeData(CubePosition cubePosition, FullCubeData fullCubeData);

    void unloadFromMemory(CubePosition cubePosition);
}
