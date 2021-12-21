package com.meow.smallquilt.mod.command.seen;

import com.meow.smallquilt.mod.command.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SeenCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("seen").then(Commands.argument("playerName", StringArgumentType.string()).executes(command -> {
            CommandSource source = command.getSource();
            String name = StringArgumentType.getString(command, "playerName");
            return executeCommand(source, name);
        }));
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        String name = event.getPlayer().getName().getString();
        SeenUtil.writeSeenDataAsync(name);
    }

    private int executeCommand(CommandSource source, String name) {
        String result = SeenUtil.querySeenData(name);
        if (result == null) {
            source.sendFeedback(new StringTextComponent("找不到相關紀錄").mergeStyle(TextFormatting.RED), true);
            return 0;
        }
        source.sendFeedback(new StringTextComponent("玩家 : " + name + " 最後上線時間 : " + result).mergeStyle(TextFormatting.GREEN), true);
        return 1;
    }
}