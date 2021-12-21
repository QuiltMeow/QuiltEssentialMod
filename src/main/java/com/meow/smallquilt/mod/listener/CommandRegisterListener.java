package com.meow.smallquilt.mod.listener;

import com.meow.smallquilt.mod.command.*;
import com.meow.smallquilt.mod.command.back.BackCommand;
import com.meow.smallquilt.mod.command.debug.ListLoadChunkCommand;
import com.meow.smallquilt.mod.command.home.DelHomeCommand;
import com.meow.smallquilt.mod.command.home.HomeCommand;
import com.meow.smallquilt.mod.command.home.SetHomeCommand;
import com.meow.smallquilt.mod.command.nick.NickCommand;
import com.meow.smallquilt.mod.command.seen.SeenCommand;
import com.meow.smallquilt.mod.command.teleport.TpAcceptCommand;
import com.meow.smallquilt.mod.command.teleport.TpaCommand;
import com.meow.smallquilt.mod.command.teleport.TpaDenyCommand;
import com.meow.smallquilt.mod.command.teleport.TpaHereCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegisterListener {

    @SubscribeEvent
    public void onRegisterCommand(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(new BackCommand().getCommand());
        dispatcher.register(new FreecamCommand().getCommand());
        dispatcher.register(new HatCommand().getCommand());
        dispatcher.register(new HereCommand().getCommand());
        dispatcher.register(new MeowCommand().getCommand());
        dispatcher.register(new SpawnCommand().getCommand());
        dispatcher.register(new SuicideCommand().getCommand());
        dispatcher.register(new TrashCommand().getCommand());

        NickCommand nick = new NickCommand();
        dispatcher.register(nick.getCommand());
        MinecraftForge.EVENT_BUS.register(nick);

        SeenCommand seen = new SeenCommand();
        dispatcher.register(seen.getCommand());
        MinecraftForge.EVENT_BUS.register(seen);

        dispatcher.register(new BroadcastCommand().getCommand());
        dispatcher.register(new ListLoadChunkCommand().getCommand());

        dispatcher.register(new HomeCommand().getCommand());
        dispatcher.register(new SetHomeCommand().getCommand());
        dispatcher.register(new DelHomeCommand().getCommand());

        dispatcher.register(new TpaCommand().getCommand());
        dispatcher.register(new TpaHereCommand().getCommand());
        dispatcher.register(new TpAcceptCommand().getCommand());
        dispatcher.register(new TpaDenyCommand().getCommand());
    }
}
