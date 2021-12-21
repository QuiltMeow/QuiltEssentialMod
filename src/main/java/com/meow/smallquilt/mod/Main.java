package com.meow.smallquilt.mod;

import com.meow.smallquilt.mod.command.back.BackUtil;
import com.meow.smallquilt.mod.command.home.HomeUtil;
import com.meow.smallquilt.mod.command.nick.NickUtil;
import com.meow.smallquilt.mod.command.seen.SeenUtil;
import com.meow.smallquilt.mod.listener.CommandRegisterListener;
import com.meow.smallquilt.mod.listener.DeathListener;
import com.meow.smallquilt.mod.listener.EntityControlListener;
import com.meow.smallquilt.mod.listener.ExplodeListener;
import com.meow.smallquilt.mod.util.FileUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.MOD_ID)
public class Main {

    public static final String MOD_ID = "quiltmod";
    private static final String MAIN_FOLDER = "quilt";
    private static final Logger LOGGER = LogManager.getLogger();

    public Main() {
        LOGGER.info("Quilt Mod 正在初始化 ...");
        setupFolder();
        registerEvent();
        LOGGER.info("喵嗚 :3 ~");
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public void setupFolder() {
        FileUtil.createFolder(MAIN_FOLDER);
        BackUtil.createBackFolder();
        HomeUtil.createHomeFolder();
        NickUtil.createNickFolder();
        SeenUtil.createSeenFolder();
    }

    public void registerEvent() {
        MinecraftForge.EVENT_BUS.register(new DeathListener());
        MinecraftForge.EVENT_BUS.register(new EntityControlListener());
        MinecraftForge.EVENT_BUS.register(new ExplodeListener());
        MinecraftForge.EVENT_BUS.register(new CommandRegisterListener());
    }
}