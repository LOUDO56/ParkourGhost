package fr.loudo.nms_1_20.utils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PacketTP {
    public static ClientboundTeleportEntityPacket createTeleportPacket(FriendlyByteBuf buf) {
        try {
            Constructor<ClientboundTeleportEntityPacket> constructor =
                    ClientboundTeleportEntityPacket.class.getDeclaredConstructor(FriendlyByteBuf.class);

            constructor.setAccessible(true);

            return constructor.newInstance(buf);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}
