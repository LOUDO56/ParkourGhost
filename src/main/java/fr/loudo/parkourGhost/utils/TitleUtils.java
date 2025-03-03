package fr.loudo.parkourGhost.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import fr.loudo.parkourGhost.ParkourGhost;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TitleUtils {

    public static void send(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        PacketContainer titlePacket = new PacketContainer(PacketType.Play.Server.SET_TITLE_TEXT);
        titlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
        titlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(title));

        PacketContainer subTitlePacket = new PacketContainer(PacketType.Play.Server.SET_SUBTITLE_TEXT);
        subTitlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
        subTitlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(subtitle));

        PacketContainer titleAnimationPacket = new PacketContainer(PacketType.Play.Server.SET_TITLES_ANIMATION);
        titleAnimationPacket.getIntegers().write(0, fadeIn);
        titleAnimationPacket.getIntegers().write(1, stay);
        titleAnimationPacket.getIntegers().write(2, fadeOut);

        ParkourGhost.getProtocolManager().sendServerPacket(player, titlePacket);
        ParkourGhost.getProtocolManager().sendServerPacket(player, subTitlePacket);
        ParkourGhost.getProtocolManager().sendServerPacket(player, titleAnimationPacket);
    }

}
