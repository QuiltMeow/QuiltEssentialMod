package com.meow.smallquilt.mod.command.back;

import com.meow.smallquilt.mod.util.FileUtil;
import com.meow.smallquilt.mod.util.SerialLocationData;
import com.meow.smallquilt.mod.util.SerializeUtil;
import net.minecraft.entity.player.PlayerEntity;

import java.io.File;
import java.util.UUID;

public class BackUtil {

    public static final String BACK_SAVE_DATA = "quilt/back";

    public static boolean createBackFolder() {
        return FileUtil.createFolder(BACK_SAVE_DATA);
    }

    public static String getBackSavePath(UUID uuid) {
        return BACK_SAVE_DATA + "/" + uuid + ".dat";
    }

    public static SerialLocationData queryBackData(UUID uuid) {
        String path = getBackSavePath(uuid);
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return SerializeUtil.reaadObject(path);
    }

    public static boolean writeBackData(UUID uuid, SerialLocationData data) {
        String path = getBackSavePath(uuid);
        return SerializeUtil.writeObject(path, data);
    }

    public static SerialLocationData writeBackData(PlayerEntity player) {
        SerialLocationData data = SerialLocationData.getLocationData(player);
        writeBackData(player.getUniqueID(), data);
        return data;
    }

    public static SerialLocationData writeDeathBackData(PlayerEntity player) {
        SerialLocationData data = SerialLocationData.getLocationData(player);
        data.setY(data.getY() + 1);
        writeBackData(player.getUniqueID(), data);
        return data;
    }
}