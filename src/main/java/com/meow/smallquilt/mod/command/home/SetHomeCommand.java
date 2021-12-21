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

public class SetHomeCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("sethome").requires(source -> {
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
        if (homeName.length() > HomeUtil.MAX_HOME_NAME_LENGTH) {
            player.sendMessage(new StringTextComponent("家名稱長度太長 無法設定").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }

        UUID uuid = player.getUniqueID();
        HashMap<String, SerialLocationData> homeMap = HomeUtil.queryHomeData(uuid);
        if (homeMap == null) {
            homeMap = new HashMap<>();
        }

        SerialLocationData homeData = SerialLocationData.getLocationData(player);
        homeMap.put(homeName, homeData);
        if (homeMap.size() > HomeUtil.MAX_HOME) {
            player.sendMessage(new StringTextComponent("已超過家設定上限 : " + HomeUtil.MAX_HOME + " 無法再新增").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }

        HomeUtil.writeHomeData(uuid, homeMap);
        player.sendMessage(new StringTextComponent("完成設定家 : " + homeName).mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        return 1;
    }
}