package fr.loudo.parkourGhost.playbacks;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.manager.ParkourGhostManager;
import fr.loudo.parkourGhost.recordings.RecordingData;
import fr.loudo.parkourGhost.recordings.actions.ActionPlayer;
import fr.loudo.parkourGhost.recordings.actions.PlayerPoseChange;
import fr.loudo.parkourGhost.recordings.actions.MovementData;
import fr.loudo.parkourGhost.utils.GhostPlayer;
import io.github.a5h73y.parkour.Parkour;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class Playback {

    private RecordingData recordingData;
    private ServerPlayer serverPlayer;
    private Player player;
    private ServerPlayer ghostPlayer;
    private boolean isPlayingBack;
    private int tick;

    public Playback(RecordingData recordingData, Player player) {
        this.recordingData = recordingData;
        this.serverPlayer = ((CraftPlayer) player).getHandle();
        this.player = player;
        isPlayingBack = false;
    }

    public boolean start() {
        if(isPlayingBack) return false;

        startCountdown();

        return true;

    }

    private void createGhostPlayer() {
        GameProfile ghostGameProfile = new GameProfile(UUID.randomUUID(), serverPlayer.displayName);
        GameProfile playerProfile = serverPlayer.getGameProfile();

        Property textures = playerProfile.getProperties().get("textures").iterator().next();
        if(textures != null) {
            ghostGameProfile.getProperties().put("textures", new Property("textures", textures.value(), textures.signature()));
        }

        ghostPlayer = new GhostPlayer(serverPlayer.serverLevel(), ghostGameProfile);
        ServerGamePacketListenerImpl connection = serverPlayer.connection;

        Scoreboard scoreboard = new Scoreboard();
        PlayerTeam team = new PlayerTeam(scoreboard, "Ghost");
        team.setSeeFriendlyInvisibles(true);
        team.setCollisionRule(Team.CollisionRule.NEVER);
        //TODO: add option see nametag
        //team.setNameTagVisibility(Team.Visibility.NEVER);
        scoreboard.addPlayerToTeam(serverPlayer.getDisplayName().getString(), team);
        scoreboard.addPlayerToTeam(ghostPlayer.getDisplayName().getString(), team);

        // Enable all skin layers
        SynchedEntityData dataWatcherGhost = ghostPlayer.getEntityData();
        EntityDataAccessor<Byte> ENTITY_LAYER = new EntityDataAccessor<>(17, EntityDataSerializers.BYTE);
        dataWatcherGhost.set(ENTITY_LAYER, (byte) 0b01111111);

        ghostPlayer.setInvisible(true);

        MovementData firstLoc = recordingData.getMovementData().getFirst();
        ghostPlayer.moveTo(firstLoc.getX(), firstLoc.getY(), firstLoc.getZ());

        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ghostPlayer));
        connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));

        serverPlayer.serverLevel().addFreshEntity(ghostPlayer);

        isPlayingBack = true;
    }

    public void run() {

        createGhostPlayer();
        tick = 0;

        new BukkitRunnable() {

            @Override
            public void run() {
                if(!isPlayingBack) this.cancel();

                if (tick >= recordingData.getMovementData().size()) {
                    stop();
                    return;
                }

                MovementData pos = recordingData.getMovementData().get(tick);

                PositionMoveRotation positionMoveRotation = new PositionMoveRotation(
                        new Vec3(pos.getX(), pos.getY(), pos.getZ()),
                        new Vec3(0, 0, 0),
                        pos.getyRot(),
                        pos.getxRot()
                );

                serverPlayer.connection.send(new ClientboundEntityPositionSyncPacket(ghostPlayer.getId(), positionMoveRotation, false));
                serverPlayer.connection.send(new ClientboundRotateHeadPacket(ghostPlayer, (byte) (pos.getyRot() * 256.0F / 360.0F)));

                if(!recordingData.getActionsPlayer().isEmpty()) {
                    ActionPlayer actionPlayer = recordingData.getActionsPlayer().get(tick);
                    if(actionPlayer != null) {
                        switch (actionPlayer.getActionType()) {
                            case SWING:
                                ghostPlayer.swing(InteractionHand.MAIN_HAND);
                                break;
                            case POSE:
                                Pose pose = ((PlayerPoseChange) actionPlayer).getPose();
                                ghostPlayer.setPose(pose);
                                break;
                        }
                    }
                }
                tick++;
            }
        }.runTaskTimer(ParkourGhost.getPlugin(), 0L, 1L);
    }

    private void startCountdown() {
        PlaybackCountdown playbackCountdown = new PlaybackCountdown(player, this);
        MovementData firstLoc = recordingData.getMovementData().getFirst();
        new BukkitRunnable() {
            @Override
            public void run() {
                playbackCountdown.update();
                if(playbackCountdown.getSeconds() < 0) {
                    playbackCountdown.getPlayback().run();
                    ParkourGhostManager.getCurrentPlayerRecording(player).start();
                    this.cancel();
                }
                if(!Parkour.getInstance().getParkourSessionManager().isPlaying(player)) {
                    this.cancel();
                }
            }
        }.runTaskTimer(ParkourGhost.getPlugin(), 0L, 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(new Location(player.getWorld(), firstLoc.getX(), firstLoc.getY(), firstLoc.getZ()));
                if(playbackCountdown.getSeconds() < 0) {
                    this.cancel();
                }
                if(!Parkour.getInstance().getParkourSessionManager().isPlaying(player)) {
                    this.cancel();
                }
            }
        }.runTaskTimer(ParkourGhost.getPlugin(), 0L, 1L);
    }

    public boolean stop() {
        if(!isPlayingBack) return false;

        isPlayingBack = false;

        ghostPlayer.remove(Entity.RemovalReason.KILLED);
        serverPlayer.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(ghostPlayer.getUUID())));

        return true;
    }
}
