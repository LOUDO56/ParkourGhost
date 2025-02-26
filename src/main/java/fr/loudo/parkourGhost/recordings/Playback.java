package fr.loudo.parkourGhost.recordings;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.utils.GhostPlayer;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Playback {

    private List<MovementData> movementData;
    private ServerPlayer player;
    private ServerPlayer ghostPlayer;
    private boolean isPlayingBack;
    private BukkitTask task;

    public Playback(List<MovementData> movementData, Player player) {
        this.movementData = movementData;
        this.player = ((CraftPlayer) player).getHandle();
        isPlayingBack = false;
    }

    public boolean start() {
        if(isPlayingBack) return false;
        
        GameProfile ghostGameProfile = new GameProfile(UUID.randomUUID(), player.displayName);
        GameProfile playerProfile = player.getGameProfile();

        Property textures = playerProfile.getProperties().get("textures").iterator().next();
        if(textures != null) {
            ghostGameProfile.getProperties().put("textures", new Property("textures", textures.value(), textures.signature()));
        }

        ghostPlayer = new GhostPlayer(player.serverLevel(), ghostGameProfile);
        ServerGamePacketListenerImpl connection = player.connection;

        Scoreboard scoreboard = new Scoreboard();
        PlayerTeam team = new PlayerTeam(scoreboard, "Ghost");
        team.setSeeFriendlyInvisibles(true);
        team.setCollisionRule(Team.CollisionRule.NEVER);
        //team.setNameTagVisibility(Team.Visibility.NEVER);
        scoreboard.addPlayerToTeam(player.getDisplayName().getString(), team);
        scoreboard.addPlayerToTeam(ghostPlayer.getDisplayName().getString(), team);

        // Enable all skin layers
        SynchedEntityData dataWatcherGhost = ghostPlayer.getEntityData();
        EntityDataAccessor<Byte> ENTITY_LAYER = new EntityDataAccessor<>(17, EntityDataSerializers.BYTE);
        dataWatcherGhost.set(ENTITY_LAYER, (byte) 0b01111111);

        ghostPlayer.setInvisible(true);

        MovementData firstLoc = movementData.getFirst();
        ghostPlayer.moveTo(firstLoc.getX(), firstLoc.getY(), firstLoc.getZ());

        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ghostPlayer));
        //connection.send(new ClientboundAddEntityPacket(ghostPlayer, 147, startPos)); // id 147 from https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Entity_metadata#Entities
        //connection.send(new ClientboundSetEntityDataPacket(ghostPlayer.getId(), dataWatcherGhost.getNonDefaultValues()));

        player.serverLevel().addFreshEntity(ghostPlayer);

        connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));

        isPlayingBack = true;

        run();

        return true;

    }

    private void run() {

        AtomicInteger i = new AtomicInteger(0);

        task = Bukkit.getScheduler().runTaskTimer(ParkourGhost.getPlugin(), () -> {

            if (i.get() >= movementData.size()) {
                stop();
                return;
            }

            MovementData pos = movementData.get(i.get());

            ghostPlayer.setPos(new Vec3(pos.getX(), pos.getY(), pos.getZ()));
            ghostPlayer.setXRot(pos.getxRot());
            ghostPlayer.setYRot(pos.getyRot());
            ghostPlayer.setYHeadRot(pos.getyHeadRot());
            ghostPlayer.setYBodyRot(pos.getyBodyRot());

            i.incrementAndGet();
        }, 0L, 1L);
    }

    public boolean stop() {
        if(!isPlayingBack) return false;

        isPlayingBack = false;
        task.cancel();
        task = null;

        ghostPlayer.remove(Entity.RemovalReason.KILLED);

        player.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(ghostPlayer.getUUID())));
        player.connection.send(new ClientboundRemoveEntitiesPacket(ghostPlayer.getId()));

        return true;
    }
}
