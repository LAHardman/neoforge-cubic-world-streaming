package com.lahardman.cubicworldstreaming.command;

import com.lahardman.cubicworldstreaming.api.CubePosition;
import com.lahardman.cubicworldstreaming.api.CubeTicketSourceKey;
import com.lahardman.cubicworldstreaming.runtime.CubicWorldStreamingRuntimeManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;

/**
 * Registers debug commands for cube state visibility.
 */
public final class CubeDebugCommand {

    private CubeDebugCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("cube_debug")
                .then(Commands.literal("at_player")
                    .executes(context -> {
                        CommandSourceStack source = context.getSource();
                        ServerPlayer player = source.getPlayerOrException();

                        CubePosition cubePosition = CubePosition.fromBlockCoordinates(
                            player.blockPosition().getX(),
                            player.blockPosition().getY(),
                            player.blockPosition().getZ()
                        );

                        var runtime = CubicWorldStreamingRuntimeManager.get(source.getServer());
                        if (runtime == null) {
                            source.sendFailure(Component.literal("Cubic runtime not initialized."));
                            return 0;
                        }

                        String description = runtime.cubeDebugOverlayService().describeCube(cubePosition);
                        source.sendSuccess(() -> Component.literal(description), false);
                        return 1;
                    })
                )
                .then(Commands.literal("at_cube")
                    .then(Commands.argument("cubeX", IntegerArgumentType.integer())
                        .then(Commands.argument("cubeY", IntegerArgumentType.integer())
                            .then(Commands.argument("cubeZ", IntegerArgumentType.integer())
                                .executes(context -> {
                                    int cubeX = IntegerArgumentType.getInteger(context, "cubeX");
                                    int cubeY = IntegerArgumentType.getInteger(context, "cubeY");
                                    int cubeZ = IntegerArgumentType.getInteger(context, "cubeZ");

                                    CubePosition cubePosition = new CubePosition(cubeX, cubeY, cubeZ);
                                    CommandSourceStack source = context.getSource();

                                    var runtime = CubicWorldStreamingRuntimeManager.get(source.getServer());
                                    if (runtime == null) {
                                        source.sendFailure(Component.literal("Cubic runtime not initialized."));
                                        return 0;
                                    }

                                    String description = runtime.cubeDebugOverlayService().describeCube(cubePosition);
                                    source.sendSuccess(() -> Component.literal(description), false);
                                    return 1;
                                })
                            )
                        )
                    )
                )
                .then(Commands.literal("stats")
                    .executes(context -> {
                        CommandSourceStack source = context.getSource();
                        ServerPlayer player = source.getPlayerOrException();
                        var runtime = CubicWorldStreamingRuntimeManager.get(source.getServer());
                        if (runtime == null) {
                            source.sendFailure(Component.literal("Cubic runtime not initialized."));
                            return 0;
                        }

                        CubeTicketSourceKey sourceKey = CubeTicketSourceKey.player(player.getUUID().toString());
                        int activeTicketCount = runtime.cubeTicketManager().getActiveTicketCountForSource(sourceKey, Instant.now());

                        source.sendSuccess(() -> Component.literal("Active player tickets=" + activeTicketCount), false);
                        return 1;
                    })
                )
        );
    }
}
