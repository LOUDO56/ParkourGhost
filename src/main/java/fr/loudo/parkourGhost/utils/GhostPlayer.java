package fr.loudo.parkourGhost.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.stats.Stat;
import net.minecraft.world.damagesource.DamageSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GhostPlayer extends ServerPlayer
{
    private static final ClientInformation DEFAULT_CLIENT_INFO = ClientInformation.createDefault();

    public GhostPlayer(ServerLevel level, GameProfile profile)
    {
        super(level.getServer(), level, profile, DEFAULT_CLIENT_INFO);
        this.connection = new FakePlayerNetHandler(level.getServer(), this, profile);
        setInvulnerable(true);
    }


    @Override public void displayClientMessage(@NotNull Component chatComponent, boolean actionBar) { }
    @Override public void awardStat(@NotNull Stat stat, int amount) { }
    @Override public void die(@NotNull DamageSource source) { }
    @Override public void tick() { }

    private static class FakePlayerNetHandler extends ServerGamePacketListenerImpl
    {
        private static final Connection DUMMY_CONNECTION = new DummyConnection(PacketFlow.CLIENTBOUND);

        public FakePlayerNetHandler(MinecraftServer server, ServerPlayer player, GameProfile profile)
        {
            super(server, DUMMY_CONNECTION, player, new CommonListenerCookie(profile, 0, DEFAULT_CLIENT_INFO, false));
        }


    }

    private static class DummyConnection extends Connection
    {
        public DummyConnection(PacketFlow packetFlow)
        {
            super(packetFlow);
        }
    }
}