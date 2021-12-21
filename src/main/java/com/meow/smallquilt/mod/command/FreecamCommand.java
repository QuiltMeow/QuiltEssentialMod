package com.meow.smallquilt.mod.command;

import com.meow.smallquilt.mod.util.Location;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FreecamCommand implements Command {

    private static final Map<UUID, Location> FREE_CAM_LOCATION = new HashMap<>();

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("freecam").requires(source -> {
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
        GameType gameMode = player.interactionManager.getGameType();
        UUID uuid = player.getUniqueID();
        if (gameMode == GameType.SURVIVAL) {
            FREE_CAM_LOCATION.put(uuid, Location.getLocation(player));
            player.setGameType(GameType.SPECTATOR);
            player.sendMessage(new StringTextComponent("Free Cam 已開啟").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
            return 1;
        } else if (gameMode == GameType.SPECTATOR) {
            Location location = FREE_CAM_LOCATION.get(uuid);
            if (location == null) {
                ServerWorld world = Location.getServerWorld(Location.MAIN_WORLD);
                Optional<BlockPos> bedPosition = player.getBedPosition();
                Location bedSpawn = bedPosition.isPresent() ? new Location(world, bedPosition.get()) : null;
                Location targetSpawn = bedSpawn != null ? bedSpawn : new Location(world, world.getSpawnPoint());
                location = targetSpawn;
                player.sendMessage(new StringTextComponent("找不到返回點 傳送至復活位置").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            }

            location.teleport(player);
            player.setGameType(GameType.SURVIVAL);
            FREE_CAM_LOCATION.remove(uuid);
            player.sendMessage(new StringTextComponent("Free Cam 已關閉").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
            return 1;
        } else {
            player.sendMessage(new StringTextComponent("僅生存模式可使用本功能").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }
    }
}