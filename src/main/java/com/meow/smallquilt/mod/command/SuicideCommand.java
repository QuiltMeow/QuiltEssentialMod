package com.meow.smallquilt.mod.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

public class SuicideCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("suicide").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch (Exception ex) {
                return false;
            }
        }).executes(command -> {
            ServerPlayerEntity player = command.getSource().asPlayer();
            if (player.interactionManager.getGameType() != GameType.SURVIVAL) {
                player.sendMessage(new StringTextComponent("僅生存模式可使用自殺").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                return 0;
            }
            player.attackEntityFrom(DamageSource.OUT_OF_WORLD, player.getHealth());
            player.sendMessage(new StringTextComponent("你自殺了").mergeStyle(TextFormatting.YELLOW), Util.DUMMY_UUID);
            return 1;
        });
    }
}