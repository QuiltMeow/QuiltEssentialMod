package com.meow.smallquilt.mod.command.back;

import com.meow.smallquilt.mod.command.Command;
import com.meow.smallquilt.mod.util.SerialLocationData;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.UUID;

public class BackCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("back").requires(source -> {
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
        UUID uuid = player.getUniqueID();
        SerialLocationData location = BackUtil.queryBackData(uuid);
        if (location == null) {
            player.sendMessage(new StringTextComponent("找不到返回點").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }

        BackUtil.writeBackData(player);
        location.teleport(player);
        player.sendMessage(new StringTextComponent("傳送到上一位置").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        return 1;
    }
}
