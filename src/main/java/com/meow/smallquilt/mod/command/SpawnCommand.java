package com.meow.smallquilt.mod.command;

import com.meow.smallquilt.mod.command.back.BackUtil;
import com.meow.smallquilt.mod.util.Location;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

public class SpawnCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("spawn").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch (Exception ex) {
                return false;
            }
        }).executes(command -> {
            ServerPlayerEntity player = command.getSource().asPlayer();
            return executeCommand(player);
        });
    }

    private int executeCommand(ServerPlayerEntity player) {
        ServerWorld world = Location.getServerWorld(Location.MAIN_WORLD);
        Location location = new Location(world, world.getSpawnPoint());

        BackUtil.writeBackData(player);
        location.teleport(player);
        player.sendMessage(new StringTextComponent("傳送到世界出生點").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        return 1;
    }
}