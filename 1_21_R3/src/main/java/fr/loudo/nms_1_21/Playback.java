package fr.loudo.nms_1_21;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.manager.ParkourGhostManager;
import fr.loudo.parkourGhost.nms.PlaybackInterface;
import fr.loudo.parkourGhost.playbacks.PlaybackCountdown;
import fr.loudo.parkourGhost.recordings.RecordingData;
import fr.loudo.parkourGhost.recordings.actions.ActionPlayer;
import fr.loudo.parkourGhost.recordings.actions.MovementData;
import fr.loudo.parkourGhost.utils.ParsePose;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.type.course.Course;
import net.minecraft.core.particles.ParticleTypes;
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
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import fr.loudo.nms_1_21.utils.GhostPlayer;

import java.util.List;
import java.util.UUID;

public class Playback implements PlaybackInterface {

    private RecordingData recordingData;
    private Player player;
    private ServerPlayer serverPlayer;
    private ServerPlayer ghostPlayer;
    private String courseName;
    private boolean isPlayingBack;
    private int tick;
    private BukkitTask countdownTask;
    private BukkitTask blockPlayerTask;
    private BukkitTask ghostPlayerTask;


    public Playback(Player player, RecordingData recordingData, String courseName) {
        this.recordingData = recordingData;
        this.player = player;
        this.serverPlayer = ((CraftPlayer)player).getHandle();
        this.courseName = courseName;
        isPlayingBack = false;
    }

    @Override
    public boolean start() {
        if(isPlayingBack) return false;
        isPlayingBack = true;

        createGhostPlayer();

        if(ParkourGhost.getPlugin().getConfig().getBoolean("playback.countdown")) {
            startCountdown();
        } else {
            runPlayback();
        }

        return true;

    }

    private void createGhostPlayer() {
        GameProfile ghostGameProfile = new GameProfile(UUID.randomUUID(), serverPlayer.displayName);
        GameProfile playerProfile = serverPlayer.getGameProfile();

        if (playerProfile.getProperties().containsKey("textures")) {
            Property textures = playerProfile.getProperties().get("textures").iterator().next();
            ghostGameProfile.getProperties().put("textures", new Property("textures", textures.value(), textures.signature()));
        }

        ghostPlayer = new GhostPlayer(serverPlayer.serverLevel(), ghostGameProfile);
        ServerGamePacketListenerImpl connection = serverPlayer.connection;


        boolean seeUsername = ParkourGhost.getPlugin().getConfig().getBoolean("ghostplayer.see-username");
        boolean ghostPlayerInvisible = ParkourGhost.getPlugin().getConfig().getBoolean("ghostplayer.invisible");

        Scoreboard scoreboard = new Scoreboard();
        PlayerTeam team = new PlayerTeam(scoreboard, "Ghost");
        team.setCollisionRule(Team.CollisionRule.NEVER);
        if(!seeUsername) {
            team.setNameTagVisibility(Team.Visibility.NEVER);
        }
        if(ghostPlayerInvisible) {
            ghostPlayer.setInvisible(true);
            team.setSeeFriendlyInvisibles(true);
        }
        scoreboard.addPlayerToTeam(serverPlayer.getDisplayName().getString(), team);
        scoreboard.addPlayerToTeam(ghostPlayer.getDisplayName().getString(), team);

        // Enable all skin layers
        SynchedEntityData dataWatcherGhost = ghostPlayer.getEntityData();
        EntityDataAccessor<Byte> ENTITY_LAYER = new EntityDataAccessor<>(17, EntityDataSerializers.BYTE);
        dataWatcherGhost.set(ENTITY_LAYER, (byte) 0b01111111);

        connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));

        MovementData firstLoc = recordingData.getMovementData().get(0);
        ghostPlayer.moveTo(firstLoc.getX(), firstLoc.getY(), firstLoc.getZ(), firstLoc.getxRot(), firstLoc.getyRot());
    }

    @Override
    public void runPlayback() {

        tick = 0;

        serverPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ghostPlayer));
        serverPlayer.serverLevel().addFreshEntity(ghostPlayer);

        if(ParkourGhost.getPlugin().getConfig().getBoolean("ghostplayer.particles-apparition")) {
            serverPlayer.connection.send(new ClientboundLevelParticlesPacket(
                    ParticleTypes.CLOUD,
                    false,
                    true,
                    ghostPlayer.getX(),
                    ghostPlayer.getY(),
                    ghostPlayer.getZ(),
                    0.5F,
                    1.3F,
                    0.5F,
                    0.05F,
                    50
            ));
        }

        ghostPlayerTask = new BukkitRunnable() {

            @Override
            public void run() {

                if (tick >= recordingData.getMovementData().size()) {
                    player.sendMessage(ChatColor.GREEN + "The " + ChatColor.YELLOW + "ghost" + ChatColor.GREEN + " finished the parkour.");
                    stop();
                    return;
                }

                MovementData pos = recordingData.getMovementData().get(tick);

                ghostPlayer.setPos(pos.getX(), pos.getY(), pos.getZ());
                ghostPlayer.setXRot(pos.getxRot());
                ghostPlayer.setYRot(pos.getyRot());
                ghostPlayer.setYHeadRot(pos.getHeadYRot());

                if(!recordingData.getActionsPlayer().isEmpty()) {
                    ActionPlayer actionPlayer = recordingData.getActionsPlayer().get(tick);
                    if(actionPlayer != null) {
                        switch (actionPlayer.getActionType()) {
                            case SWING:
                                ghostPlayer.swing(InteractionHand.MAIN_HAND);
                                break;
                            case POSE:
                                ghostPlayer.setPose(Pose.valueOf(ParsePose.parse(actionPlayer)));
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

        Course course = Parkour.getInstance().getCourseManager().findByName(courseName);
        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                playbackCountdown.update();
                if (playbackCountdown.getSeconds() < 0) {
                    playbackCountdown.getPlayback().runPlayback();
                    ParkourGhostManager.getCurrentPlayerRecording(player).start();
                    this.cancel();
                }
            }
        }.runTaskTimer(ParkourGhost.getPlugin(), 0L, 20L);
        blockPlayerTask = new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(course.getCheckpoints().get(0).getLocation());
                if(playbackCountdown.getSeconds() < 0) {
                    this.cancel();
                }

            }
        }.runTaskTimer(ParkourGhost.getPlugin(), 0L, 1L);
    }

    @Override
    public boolean stop() {
        if(!isPlayingBack) return false;

        if(blockPlayerTask != null) {
            blockPlayerTask.cancel();
            blockPlayerTask = null;
        }

        if(countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }

        if(ghostPlayerTask != null) {
            ghostPlayerTask.cancel();
            ghostPlayerTask = null;
        }

        if(ghostPlayer != null) {
            ghostPlayer.remove(Entity.RemovalReason.KILLED);
            serverPlayer.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(ghostPlayer.getUUID())));
        }

        isPlayingBack = false;

        return true;
    }

}
