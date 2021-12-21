package com.meow.smallquilt.mod.command.seen;

import com.meow.smallquilt.mod.Main;
import com.meow.smallquilt.mod.util.FileUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SeenUtil {

    public static final String SEEN_SAVE_DATA = "quilt/seen";

    public static boolean createSeenFolder() {
        return FileUtil.createFolder(SEEN_SAVE_DATA);
    }

    public static String getSeenSavePath(String name) {
        return SEEN_SAVE_DATA + "/" + name + ".dat";
    }

    public static String querySeenData(String name) {
        String path = getSeenSavePath(name);
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception ex) {
            Main.getLogger().warn("讀取玩家最後上線時間時發生例外狀況", ex);
            return null;
        }
    }

    public static void writeSeenDataAsync(String name) {
        new Thread(() -> {
            String path = getSeenSavePath(name);
            String date = new SimpleDateFormat("yyyy 年 MM 月 dd 日 E a hh 點 mm 分 ss 秒", Locale.TAIWAN).format(new Date());
            FileUtil.writeStringData(path, date);
        }).start();
    }
}