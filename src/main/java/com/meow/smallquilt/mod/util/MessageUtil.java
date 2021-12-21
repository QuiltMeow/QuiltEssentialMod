package com.meow.smallquilt.mod.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.List;

public class MessageUtil {

    public static void broadcastMessage(ITextComponent message) {
        List<ServerPlayerEntity> online = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        for (int i = 0; i < online.size(); ++i) {
            try {
                PlayerEntity player = online.get(i);
                player.sendMessage(message, Util.DUMMY_UUID);
            } catch (Exception ex) {
            }
        }
    }

    public static void broadcastMessage(String message) {
        broadcastMessage(new StringTextComponent(message));
    }
}