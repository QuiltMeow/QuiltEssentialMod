package com.meow.smallquilt.mod.command.home;

import com.meow.smallquilt.mod.util.FileUtil;
import com.meow.smallquilt.mod.util.SerialLocationData;
import com.meow.smallquilt.mod.util.SerializeUtil;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class HomeUtil {

    public static final int MAX_HOME_NAME_LENGTH = 10;
    public static final int MAX_HOME = 30;
    public static final String HOME_SAVE_DATA = "quilt/home";

    public static boolean createHomeFolder() {
        return FileUtil.createFolder(HOME_SAVE_DATA);
    }

    public static String getHomeSavePath(UUID uuid) {
        return HOME_SAVE_DATA + "/" + uuid + ".dat";
    }

    public static HashMap<String, SerialLocationData> queryHomeData(UUID uuid) {
        String path = getHomeSavePath(uuid);
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return SerializeUtil.reaadObject(path);
    }

    public static boolean writeHomeData(UUID uuid, HashMap<String, SerialLocationData> data) {
        String path = getHomeSavePath(uuid);
        return SerializeUtil.writeObject(path, data);
    }
}