package com.meow.smallquilt.mod.command.nick;

import com.meow.smallquilt.mod.command.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class NickCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("nick").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch (Exception ex) {
                return false;
            }
        }).then(Commands.argument("nickName", MessageArgument.message()).executes(command -> {
            PlayerEntity player = command.getSource().asPlayer();
            String nickName = MessageArgument.getMessage(command, "nickName").getString();
            return executeCommand(player, nickName);
        }));
    }

    @SubscribeEvent
    public void renderName(PlayerEvent.NameFormat event) {
        UUID uuid = event.getPlayer().getUniqueID();
        String nick = NickUtil.queryNickData(uuid);
        if (nick == null) {
            return;
        }
        event.setDisplayname(new StringTextComponent(nick));
    }

    private int executeCommand(PlayerEntity player, String nickName) {
        if (nickName.length() > NickUtil.MAX_NICK_NAME_LENGTH) {
            player.sendMessage(new StringTextComponent("玩家暱稱長度太長 無法設定").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }

        UUID uuid = player.getUniqueID();
        NickUtil.writeNickData(uuid, nickName);
        player.refreshDisplayName();
        player.sendMessage(new StringTextComponent("已設定玩家暱稱 : " + nickName).mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        return 1;
    }
}