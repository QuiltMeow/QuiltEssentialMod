package com.meow.smallquilt.mod.command.nick;

import com.meow.smallquilt.mod.Main;
import com.meow.smallquilt.mod.util.FileUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class NickUtil {

    public static final int MAX_NICK_NAME_LENGTH = 15;
    public static final String NICK_SAVE_DATA = "quilt/nick";

    public static boolean createNickFolder() {
        return FileUtil.createFolder(NICK_SAVE_DATA);
    }

    public static String getNickSavePath(UUID uuid) {
        return NICK_SAVE_DATA + "/" + uuid + ".dat";
    }

    public static String queryNickData(UUID uuid) {
        String path = getNickSavePath(uuid);
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception ex) {
            Main.getLogger().warn("讀取玩家暱稱時發生例外狀況", ex);
            return null;
        }
    }

    public static boolean writeNickData(UUID uuid, String data) {
        String path = getNickSavePath(uuid);
        return FileUtil.writeStringData(path, data);
    }
}
