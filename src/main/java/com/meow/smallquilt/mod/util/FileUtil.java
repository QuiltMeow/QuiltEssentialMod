package com.meow.smallquilt.mod.util;

import com.meow.smallquilt.mod.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class FileUtil {

    public static boolean createFolder(String folder) {
        File path = new File(folder);
        if (!path.exists()) {
            Main.getLogger().info("指定位置 : {} 不存在 正在建立 ...", folder);
            try {
                path.mkdir();
                Main.getLogger().info("指定位置建立完成");
                return true;
            } catch (Exception ex) {
                Main.getLogger().warn("指定位置建立失敗", ex);
                return false;
            }
        }
        return true;
    }

    public static boolean writeStringData(String path, String data) {
        try (PrintStream stream = new PrintStream(new FileOutputStream(path))) {
            stream.print(data);
            return true;
        } catch (Exception ex) {
            Main.getLogger().warn("寫入檔案時發生例外狀況", ex);
            return false;
        }
    }
}