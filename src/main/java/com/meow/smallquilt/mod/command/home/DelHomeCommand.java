package com.meow.smallquilt.mod.command.home;

import com.meow.smallquilt.mod.command.Command;
import com.meow.smallquilt.mod.util.SerialLocationData;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.UUID;

public class DelHomeCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("delhome").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch (Exception ex) {
                return false;
            }
        }).then(Commands.argument("homeName", MessageArgument.message()).executes(command -> {
            PlayerEntity player = command.getSource().asPlayer();
            String homeName = MessageArgument.getMessage(command, "homeName").getString();
            return executeCommand(player, homeName);
        }));
    }

    private int executeCommand(PlayerEntity player, String homeName) {
        UUID uuid = player.getUniqueID();
        HashMap<String, SerialLocationData> homeMap = HomeUtil.queryHomeData(uuid);
        if (homeMap == null) {
            player.sendMessage(new StringTextComponent("尚未設定家").mergeStyle(TextFormatting.YELLOW), Util.DUMMY_UUID);
            return 0;
        }

        if (!homeMap.containsKey(homeName)) {
            player.sendMessage(new StringTextComponent("指定家不存在").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }

        homeMap.remove(homeName);
        HomeUtil.writeHomeData(uuid, homeMap);
        player.sendMessage(new StringTextComponent("完成移除家 : " + homeName).mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        return 1;
    }
}