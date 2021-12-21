package com.meow.smallquilt.mod.command.debug;

import com.meow.smallquilt.mod.Main;
import com.meow.smallquilt.mod.command.Command;
import com.meow.smallquilt.mod.util.FileUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.lang.reflect.Method;

public class ListLoadChunkCommand implements Command {

    private static final int PERMISSION_LEVEL = 4;
    private static Method GET_LOAD_CHUNK_METHOD;

    static {
        try {
            Method method = ObfuscationReflectionHelper.findMethod(ChunkManager.class, "func_223491_f"); // getLoadedChunksIterable
            method.setAccessible(true);
            GET_LOAD_CHUNK_METHOD = method;
        } catch (Exception ex) {
            Main.getLogger().warn("載入區塊方法存取失敗", ex);
        }
    }

    public static void writeFileAsync(CommandSource source, String fileName, String data) {
        new Thread(() -> {
            if (FileUtil.writeStringData(fileName, data)) {
                source.sendFeedback(new StringTextComponent("區塊紀錄輸出完成 檔案名稱 : " + fileName), true);
            } else {
                source.sendFeedback(new StringTextComponent("區塊紀錄輸出失敗").mergeStyle(TextFormatting.RED), true);
            }
        }).start();
    }

    public static void writeLoadChunk(CommandSource source) {
        source.sendFeedback(new StringTextComponent("準備輸出區塊紀錄 ..."), true);
        String fileName = "LoadChunk-" + System.currentTimeMillis() + ".log";
        StringBuilder sb = new StringBuilder();

        Iterable<ServerWorld> worldList = ServerLifecycleHooks.getCurrentServer().getWorlds();
        for (ServerWorld world : worldList) {
            ServerChunkProvider serverChunkProvider = world.getChunkProvider();
            try {
                Iterable<ChunkHolder> chunkList = (Iterable<ChunkHolder>) GET_LOAD_CHUNK_METHOD.invoke(serverChunkProvider.chunkManager);
                sb.append("世界 : ").append(world.getDimensionKey().getLocation()).append(System.lineSeparator());

                int chunkCount = 0;
                for (ChunkHolder chunkHolder : chunkList) {
                    Chunk chunk = chunkHolder.getChunkIfComplete();
                    if (chunk == null) {
                        continue;
                    }

                    ++chunkCount;
                    ChunkPos position = chunk.getPos();
                    int x = position.x;
                    int z = position.z;
                    int entityCount = chunk.getEntityLists().length;
                    int tileEntityCount = chunk.getTileEntityMap().size();
                    sb.append("X : ").append(x << 4).append(" Z : ").append(z << 4).append(" 活動實體數 : ").append(entityCount).append(" 固定實體數 : ").append(tileEntityCount).append(System.lineSeparator());
                }
                sb.append("載入區塊 : ").append(chunkCount).append(System.lineSeparator()).append(System.lineSeparator());
            } catch (Exception ex) {
                Main.getLogger().warn("讀取區塊時發生例外狀況", ex);
            }
        }

        String output = sb.toString();
        writeFileAsync(source, fileName, output);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> getCommand() {
        return Commands.literal("listloadchunk").requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL)).executes(command -> {
            writeLoadChunk(command.getSource());
            return 1;
        });
    }
}