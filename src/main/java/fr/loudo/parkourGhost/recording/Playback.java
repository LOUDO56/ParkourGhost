package fr.loudo.parkourGhost.recording;

import com.mojang.authlib.GameProfile;
import fr.loudo.parkourGhost.utils.GhostPlayer;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class Playback {

    private Recording recording;
    private ServerPlayer player;
    private boolean isPlayingBack;

    public Playback(Recording recording, ServerPlayer player) {
        this.recording = recording;
        this.player = player;
    }

    public boolean start()
    {
        if(isPlayingBack) return false;

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getDisplayName().getString());
        GhostPlayer ghostPlayer = new GhostPlayer(player.serverLevel(), gameProfile);

        player.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ghostPlayer));

        return true;

    }
}
