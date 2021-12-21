package com.meow.smallquilt.mod.command;

import com.meow.smallquilt.mod.util.Location;
import com.meow.smallquilt.mod.util.MessageUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class HereCommand implements Command {

    private static final int GLOW_DURATION = 15;

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("here").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch (Exception ex) {
                return false;
            }
        }).executes(command -> {
            PlayerEntity player = command.getSource().asPlayer();
            return executeCommand(player);
        });
    }

    private int executeCommand(PlayerEntity player) {
        Location location = Location.getLocation(player);
        MessageUtil.broadcastMessage(player.getName().getString() + " 所在位置 [" + location + "]");
        player.addPotionEffect(new EffectInstance(Effects.GLOWING, GLOW_DURATION * 20, 0));
        player.sendMessage(new StringTextComponent("已套用發光效果 " + GLOW_DURATION + " 秒").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
        return 1;
    }
}