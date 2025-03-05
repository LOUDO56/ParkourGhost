package fr.loudo.nms_1_19.utils;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerPlayer;

public class TpPacket {

    public static void send(ServerPlayer player1, ServerPlayer player2, double x, double y, double z, double pitch, double yaw)
    {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeVarInt(player2.getId());
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeByte((byte) (yaw * 255F / 360F));
        buf.writeByte((byte) (pitch * 255F / 360F));
        buf.writeBoolean(true);

        player1.connection.send(new ClientboundTeleportEntityPacket(buf));
    }

}
