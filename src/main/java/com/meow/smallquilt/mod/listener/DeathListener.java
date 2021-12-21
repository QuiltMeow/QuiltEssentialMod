package com.meow.smallquilt.mod.listener;

import com.meow.smallquilt.mod.command.back.BackUtil;
import com.meow.smallquilt.mod.util.SerialLocationData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DeathListener {

    public static void dropHead(PlayerEntity player) {
        ItemStack item = new ItemStack(Items.PLAYER_HEAD, 1);
        CompoundNBT nbt = item.getOrCreateTag();
        nbt.putString("SkullOwner", player.getName().getString());

        World world = player.getEntityWorld();
        ItemEntity entity = new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), item);
        world.addEntity(entity);
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            dropHead(player);

            SerialLocationData location = BackUtil.writeDeathBackData(player);
            player.sendMessage(new StringTextComponent("最後死亡位置 " + location).mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
        }
    }
}