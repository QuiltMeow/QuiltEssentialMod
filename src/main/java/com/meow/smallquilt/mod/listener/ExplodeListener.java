package com.meow.smallquilt.mod.listener;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExplodeListener {

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Start event) {
        if (!(event.getExplosion().getExploder() instanceof TNTEntity)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMobGriefing(EntityMobGriefingEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof WitherEntity || entity instanceof EndermanEntity) {
            event.setResult(Event.Result.DENY);
        }
    }
}