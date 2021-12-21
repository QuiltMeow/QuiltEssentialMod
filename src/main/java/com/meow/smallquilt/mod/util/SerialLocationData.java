package com.meow.smallquilt.mod.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SerialLocationData implements Serializable {

    private double x, y, z;
    private String dimension;

    public SerialLocationData(String dimension, double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    public static SerialLocationData getLocationData(PlayerEntity player) {
        String dimension = player.getEntityWorld().getDimensionKey().getLocation().toString();
        return new SerialLocationData(dimension, player.getPosX(), player.getPosY(), player.getPosZ());
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

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public boolean teleport(ServerPlayerEntity player) {
        try {
            player.teleport(Location.getServerWorld(dimension), x, y, z, player.rotationYaw, player.rotationPitch);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return "世界 : " + dimension + " 座標 : (" + df.format(x) + ", " + df.format(y) + ", " + df.format(z) + ")";
    }
}