package com.meow.smallquilt.mod.command.teleport;

import com.meow.smallquilt.mod.util.Location;
import net.minecraft.entity.player.ServerPlayerEntity;

public class TeleportRequest {

    private static final int REQUEST_TIMEOUT = 30000;

    private final ServerPlayerEntity creator;
    private final ServerPlayerEntity player;
    private final ServerPlayerEntity target;
    private final long timeout;

    public TeleportRequest(ServerPlayerEntity creator, ServerPlayerEntity player, ServerPlayerEntity target) {
        this.creator = creator;
        this.player = player;
        this.target = target;
        timeout = System.currentTimeMillis() + REQUEST_TIMEOUT;
    }

    public ServerPlayerEntity getCreator() {
        return creator;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public ServerPlayerEntity getTarget() {
        return target;
    }

    public Location getDestination() {
        return Location.getLocation(target);
    }

    public long getTimeout() {
        return timeout;
    }
}