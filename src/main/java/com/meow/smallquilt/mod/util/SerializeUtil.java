package com.meow.smallquilt.mod.util;

import com.meow.smallquilt.mod.Main;

import java.io.*;

public class SerializeUtil {

    public static boolean writeObject(String path, Object source) {
        try (OutputStream fos = new FileOutputStream(path)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(source);
                return true;
            }
        } catch (Exception ex) {
            Main.getLogger().warn("輸出物件時發生例外狀況", ex);
            return false;
        }
    }

    public static <T> T reaadObject(String path) {
        try (InputStream fis = new FileInputStream(path)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                return (T) ois.readObject();
            }
        } catch (Exception ex) {
            Main.getLogger().warn("載入物件時發生例外狀況", ex);
            return null;
        }
    }
}