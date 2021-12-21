package com.meow.smallquilt.mod.listener;

import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityControlListener {

    public static Biome.Category getBiomeCategory(IWorld world, double x, double y, double z) {
        BlockPos blockPosition = new BlockPos(x, y, z);
        return world.getBiome(blockPosition).getCategory();
    }

    @SubscribeEvent
    public void onLivingSpawn(LivingSpawnEvent event) {
        if (event.getEntity() instanceof PhantomEntity) {
            Biome.Category category = getBiomeCategory(event.getWorld(), event.getX(), event.getY(), event.getZ());
            if (category != Biome.Category.DESERT) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}