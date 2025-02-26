package fr.loudo.parkourGhost.recording;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.loudo.parkourGhost.utils.GhostPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;

import java.util.UUID;

public class Playback {

    private Recording recording;
    private boolean isPlayingBack;
    private ServerPlayer player;

    public Playback(Recording recording) {
        this.recording = recording;
        this.player = recording.getPlayer();
    }

    public boolean start()
    {
        if(isPlayingBack) return false;
        
        GameProfile ghostGameProfile = new GameProfile(UUID.randomUUID(), player.getDisplayName().getString());
        //TODO: Add skin of recorded player

        GhostPlayer ghostPlayer = new GhostPlayer(player.serverLevel(), ghostGameProfile);
        ServerGamePacketListenerImpl connection = player.connection;

        Scoreboard scoreboard = new Scoreboard();
        PlayerTeam team = new PlayerTeam(scoreboard, "Ghost");
        team.setSeeFriendlyInvisibles(true);
        team.setCollisionRule(Team.CollisionRule.NEVER);
        team.setNameTagVisibility(Team.Visibility.NEVER);
        scoreboard.addPlayerToTeam(player.getDisplayName().getString(), team);
        scoreboard.addPlayerToTeam(ghostPlayer.getDisplayName().getString(), team);

        // Enable all skin layers
        SynchedEntityData dataWatcherGhost = ghostPlayer.getEntityData();
        EntityDataAccessor<Byte> ENTITY_LAYER = new EntityDataAccessor<>(17, EntityDataSerializers.BYTE);
        dataWatcherGhost.set(ENTITY_LAYER, (byte) 0b01111111);

        ghostPlayer.setInvisible(true);

        MovementData firstLoc = recording.getMovements().getFirst();
        BlockPos startPos = new BlockPos((int) firstLoc.getX(), (int) firstLoc.getY(), (int) firstLoc.getZ());

        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ghostPlayer));
        connection.send(new ClientboundAddEntityPacket(ghostPlayer, 147, startPos)); // id 147 from https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Entity_metadata#Entities
        connection.send(new ClientboundSetEntityDataPacket(ghostPlayer.getId(), dataWatcherGhost.getNonDefaultValues()));
        connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));

        return true;

    }
}
