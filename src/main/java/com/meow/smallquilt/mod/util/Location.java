package com.meow.smallquilt.mod.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Location {

    public static final String MAIN_WORLD = "minecraft:overworld";

    public double x, y, z;
    public float rotationYaw, rotationPitch;
    public World world;

    public Location(World world, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        rotationYaw = rotationPitch = 0;
        this.world = world;
    }

    public Location(World world, BlockPos position) {
        x = position.getX();
        y = position.getY();
        z = position.getZ();
        rotationYaw = rotationPitch = 0;
        this.world = world;
    }

    public Location(World world, double x, double y, double z, float rotationYaw, float rotationPitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.world = world;
    }

    public static ServerWorld getServerWorld(String dimension) {
        return ServerLifecycleHooks.getCurrentServer().getWorld(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(dimension)));
    }

    public static Location getLocation(PlayerEntity player) {
        return new Location(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), player.rotationYaw, player.rotationPitch);
    }

    public ResourceLocation getResourceLocation() {
        return world.getDimensionKey().getLocation();
    }

    public boolean teleport(ServerPlayerEntity player) {
        try {
            player.teleport(getServerWorld(getResourceLocation().toString()), x, y, z, rotationYaw, rotationPitch);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getRotationYaw() {
        return rotationYaw;
    }

    public void setRotationYaw(float rotationYaw) {
        this.rotationYaw = rotationYaw;
    }

    public float getRotationPitch() {
        return rotationPitch;
    }

    public void setRotationPitch(float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return "世界 : " + getResourceLocation().toString() + " 座標 : (" + df.format(x) + ", " + df.format(y) + ", " + df.format(z) + ")";
    }
}