package com.meow.smallquilt.mod.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class HatCommand implements Command {

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("hat").requires(source -> {
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
        PlayerInventory inventory = player.inventory;
        ItemStack helmetItem = inventory.armorInventory.get(3).copy();
        ItemStack handItem = player.getHeldItem(Hand.MAIN_HAND);
        if (handItem.getItem() == Items.AIR) {
            player.sendMessage(new StringTextComponent("請不要將手手戴到頭上").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
            return 0;
        }

        inventory.setInventorySlotContents(39, handItem);
        for (int i = 0; i < 9; ++i) {
            if (handItem.equals(inventory.getStackInSlot(i))) {
                inventory.setInventorySlotContents(i, helmetItem);
                break;
            }
        }
        player.sendMessage(new StringTextComponent("頭盔裝備完成").mergeStyle(TextFormatting.GREEN), Util.DUMMY_UUID);
        return 1;
    }
}