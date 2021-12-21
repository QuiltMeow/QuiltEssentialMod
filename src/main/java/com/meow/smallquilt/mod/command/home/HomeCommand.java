package com.meow.smallquilt.mod.command.home;

import com.meow.smallquilt.mod.command.Command;
import com.meow.smallquilt.mod.command.back.BackUtil;
import com.meow.smallquilt.mod.util.SerialLocationData;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;
import java.util.UUID;

public class HomeCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("home").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch (Exception ex) {
                return false;
            }
        }).then(Commands.argument("homeName", MessageArgument.message()).executes(command -> {
            ServerPlayerEntity player = command.getSource().asPlayer();
            String homeName = MessageArgument.getMessage(command, "homeName").getString();
            return executeCommand(player, homeName);
        }));
    }

    private int executeCommand(ServerPlayerEntity player, String homeName) {
        UUID uuid = player.getUniqueID();
        Map<String, SerialLocationData> homeMap = HomeUtil.queryHomeData(uuid);
        if (homeMap == null || homeMap.isEmpty()) {
            player.sendMessage(new StringTextComponent("尚未設定家").mergeStyle(TextFormatting.YELLOW), Util.DUMMY_UUID);
            return 0;
        }

        if (!homeMap.containsKey(homeName)) {
            player.sendMessage(new StringTextComponent("指定家不存在").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);

            StringBuilder sb = new StringBuilder("家列表 : ");
            for (String name : homeMap.keySet()) {
                sb.append(name).append(", ");
            }
            sb.setLength(sb.length() - 2);
            player.sendMessage(new StringTextComponent(sb.toString()), Util.DUMMY_UUID);
            return 0;
        }

        SerialLocationData target = homeMap.get(homeName);
        BackUtil.writeBackData(player);
        target.teleport(player);
        player.sendMessage(new StringTextComponent("傳送到 " + homeName + " 的家").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        return 1;
    }
}