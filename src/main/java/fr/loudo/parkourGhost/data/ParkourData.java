package fr.loudo.parkourGhost.data;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.recordings.MovementData;
import fr.loudo.parkourGhost.recordings.Playback;
import fr.loudo.parkourGhost.recordings.Recording;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.commands.ParkourCommands;
import io.github.a5h73y.parkour.type.ParkourManager;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ParkourData {

    private static final HashMap<Player, Recording> SERVER_PLAYER_RECORDING_HASH_MAP = new HashMap<>();
    private static final HashMap<Player, Playback> SERVER_PLAYER_PLAYBACK_HASH_MAP = new HashMap<>();

    public static boolean joinPlayerParkour(Player player, String courseName) {

        if(SERVER_PLAYER_RECORDING_HASH_MAP.containsKey(player)) return false;

        Recording recording = new Recording(courseName, player);
        SERVER_PLAYER_RECORDING_HASH_MAP.put(player, recording);
        recording.start();

        return true;
    }

    public static boolean joinPlayerParkourAndStartPlayback(Player player, String courseName) {

        if(SERVER_PLAYER_PLAYBACK_HASH_MAP.containsKey(player)) return false;

        startPlaybackOfPlayer(player, courseName);
        Parkour.getInstance().getPlayerManager().joinCourse(player, courseName);

        return true;
    }

    public static boolean leavePlayerParkour(Player player, boolean force) {

        Recording recording = SERVER_PLAYER_RECORDING_HASH_MAP.get(player);
        if(recording == null) return false;

        SERVER_PLAYER_RECORDING_HASH_MAP.remove(player, recording);
        recording.stop(force);

        if(SERVER_PLAYER_PLAYBACK_HASH_MAP.containsKey(player)) {
            stopPlaybackOfPlayer(player);
        }

        return true;
    }


    public static boolean startPlaybackOfPlayer(Player player, String courseName) {
        PlayerData playerData;
        try {
            playerData = PlayersDataManager.getRecordingData(player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, List<MovementData>> recordedRuns = playerData.getRecordedRuns();
        if(recordedRuns == null) {
            return false;
        }

        Playback playback = new Playback(recordedRuns.get(courseName), player);
        if(!SERVER_PLAYER_PLAYBACK_HASH_MAP.containsKey(player)) {
            SERVER_PLAYER_PLAYBACK_HASH_MAP.put(player, playback);
        }
        playback.start();

        return true;

    }

    public static boolean stopPlaybackOfPlayer(Player player) {
        Playback playback = SERVER_PLAYER_PLAYBACK_HASH_MAP.get(player);
        if(playback == null) return false;

        SERVER_PLAYER_PLAYBACK_HASH_MAP.remove(player, playback);
        playback.stop();

        return true;


    }

}
