package com.meow.smallquilt.mod.command.teleport;

import com.meow.smallquilt.mod.command.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class TpaHereCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("tpahere").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch (Exception ex) {
                return false;
            }
        }).then(Commands.argument("targetPlayer", EntityArgument.players()).executes(command -> {
            ServerPlayerEntity player = command.getSource().asPlayer();
            ServerPlayerEntity target = EntityArgument.getPlayer(command, "targetPlayer");
            return executeCommand(player, target);
        }));
    }

    private int executeCommand(ServerPlayerEntity source, ServerPlayerEntity target) {
        if (source == target) {
            source.sendMessage(new StringTextComponent("無法向自己發送傳送請求").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }

        String targetName = target.getDisplayName().getString();
        if (!TeleportUtil.requestTeleport(source, target, source)) {
            source.sendMessage(new StringTextComponent("您已經向 " + targetName + " 發送了傳送請求").mergeStyle(TextFormatting.YELLOW), Util.DUMMY_UUID);
            return 0;
        }

        source.sendMessage(new StringTextComponent("將傳送請求發送給 " + targetName).mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        target.sendMessage(new StringTextComponent("玩家 " + source.getDisplayName().getString() + " 想要將你傳送到他那裡").mergeStyle(TextFormatting.YELLOW), Util.DUMMY_UUID);
        TeleportUtil.sendAcceptDenyMessage(source, target);
        return 1;
    }
}