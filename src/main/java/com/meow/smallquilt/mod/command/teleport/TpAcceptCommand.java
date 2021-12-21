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

public class TpAcceptCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("tpaccept").requires(source -> {
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
        TeleportRequest request = TeleportUtil.findRequestByResponse(source, target);
        if (request == null) {
            source.sendMessage(new StringTextComponent("找不到傳送請求").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }

        source.sendMessage(new StringTextComponent("接受玩家 " + target.getDisplayName().getString() + " 的傳送請求").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        target.sendMessage(new StringTextComponent("玩家 " + source.getDisplayName().getString() + " 接受了你的傳送請求").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        TeleportUtil.acceptRequest(request);
        return 1;
    }
}