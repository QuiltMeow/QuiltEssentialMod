package com.meow.smallquilt.mod.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public interface Command {

    LiteralArgumentBuilder<CommandSource> getCommand();
}