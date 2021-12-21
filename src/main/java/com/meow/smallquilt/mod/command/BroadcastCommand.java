package com.meow.smallquilt.mod.command;

import com.meow.smallquilt.mod.Main;
import com.meow.smallquilt.mod.util.MessageUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.ITextComponent;

public class BroadcastCommand implements Command {

    private static final int PERMISSION_LEVEL = 2;

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("bc").requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL)).then(Commands.argument("message", MessageArgument.message()).executes(command -> {
            ITextComponent message = MessageArgument.getMessage(command, "message");
            MessageUtil.broadcastMessage(message);
            Main.getLogger().info("伺服器廣播訊息 : {}", message.getString());
            return 1;
        }));
    }
}