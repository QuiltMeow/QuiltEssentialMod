package com.meow.smallquilt.mod.command;

import com.meow.smallquilt.mod.util.Randomizer;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class MeowCommand implements Command {

    private static final SoundEvent[] SOUND = new SoundEvent[]{
            SoundEvents.ENTITY_CAT_STRAY_AMBIENT, SoundEvents.ENTITY_CAT_AMBIENT
    };

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("meow").requires(source -> {
            try {
                return source.asPlayer() != null;
            } catch (Exception ex) {
                return false;
            }
        }).executes(command -> {
            PlayerEntity player = command.getSource().asPlayer();
            player.getEntityWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SOUND[Randomizer.nextInt(SOUND.length)], SoundCategory.NEUTRAL, 1, 1);
            return 1;
        });
    }
}