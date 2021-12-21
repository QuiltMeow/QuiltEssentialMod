package com.meow.smallquilt.mod.command.teleport;

import com.meow.smallquilt.mod.command.back.BackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TeleportUtil {

    private static final List<TeleportRequest> TELEPORT_REQUEST = new ArrayList<>();

    public static boolean requestTeleport(ServerPlayerEntity creator, ServerPlayerEntity player, ServerPlayerEntity target) {
        TeleportRequest find = findRequest(creator, player, target);
        if (find != null) {
            return false;
        }
        TeleportRequest request = new TeleportRequest(creator, player, target);
        TELEPORT_REQUEST.add(request);
        return true;
    }

    public static TeleportRequest findRequest(ServerPlayerEntity creator, ServerPlayerEntity player, ServerPlayerEntity target) {
        Iterator<TeleportRequest> iterator = TELEPORT_REQUEST.iterator();
        while (iterator.hasNext()) {
            TeleportRequest request = iterator.next();
            if (System.currentTimeMillis() > request.getTimeout()) {
                iterator.remove();
                continue;
            }
            if (request.getCreator() == creator && ((request.getPlayer() == player && request.getTarget() == target) || (request.getPlayer() == target && request.getTarget() == player))) {
                return request;
            }
        }
        return null;
    }

    public static TeleportRequest findRequestByResponse(ServerPlayerEntity creator, ServerPlayerEntity target) {
        return findRequest(target, creator, target);
    }

    public static void acceptRequest(TeleportRequest request) {
        teleport(request);
        TELEPORT_REQUEST.remove(request);
    }

    public static void declineRequest(TeleportRequest request) {
        TELEPORT_REQUEST.remove(request);
    }

    public static void teleport(TeleportRequest request) {
        ServerPlayerEntity player = request.getPlayer();
        BackUtil.writeBackData(player);
        request.getDestination().teleport(player);
    }

    public static void sendAcceptDenyMessage(PlayerEntity source, PlayerEntity target) {
        String sourceName = source.getName().getString();
        String acceptCommand = "/tpaccept " + sourceName;
        ClickEvent clickEventAccept = new ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCommand);
        HoverEvent hoverEventAccept = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(acceptCommand).mergeStyle(TextFormatting.GREEN));

        IFormattableTextComponent textAccept = new StringTextComponent("[接受傳送]").mergeStyle(TextFormatting.GREEN);
        Style acceptStyle = textAccept.getStyle().setClickEvent(clickEventAccept).setHoverEvent(hoverEventAccept);
        textAccept.setStyle(acceptStyle);
        target.sendMessage(textAccept, Util.DUMMY_UUID);

        String denyCommand = "/tpadeny " + sourceName;
        ClickEvent clickEventDeny = new ClickEvent(ClickEvent.Action.RUN_COMMAND, denyCommand);
        HoverEvent hoverEventDeny = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(denyCommand).mergeStyle(TextFormatting.RED));

        IFormattableTextComponent textDeny = new StringTextComponent("[拒絕傳送]").mergeStyle(TextFormatting.RED);
        Style denyStyle = textDeny.getStyle().setClickEvent(clickEventDeny).setHoverEvent(hoverEventDeny);
        textDeny.setStyle(denyStyle);
        target.sendMessage(textDeny, Util.DUMMY_UUID);
    }
}