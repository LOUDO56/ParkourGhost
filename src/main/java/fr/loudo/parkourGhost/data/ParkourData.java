package fr.loudo.parkourGhost.data;

import fr.loudo.parkourGhost.recordings.MovementData;
import fr.loudo.parkourGhost.recordings.Playback;
import fr.loudo.parkourGhost.recordings.Recording;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ParkourData {

    private static final HashMap<ServerPlayer, Recording> SERVER_PLAYER_RECORDING_HASH_MAP = new HashMap<>();
    private static final HashMap<ServerPlayer, Playback> SERVER_PLAYER_PLAYBACK_HASH_MAP = new HashMap<>();

    public static boolean joinPlayerParkour(ServerPlayer player, String courseName) {

        if(SERVER_PLAYER_RECORDING_HASH_MAP.containsKey(player)) return false;

        Recording recording = new Recording(courseName, player);
        SERVER_PLAYER_RECORDING_HASH_MAP.put(player, recording);
        recording.start();

        return true;
    }

    public static boolean leavePlayerParkour(ServerPlayer player, String courseName, boolean force) {

        Recording recording = SERVER_PLAYER_RECORDING_HASH_MAP.get(player);
        if(recording == null) return false;

        SERVER_PLAYER_RECORDING_HASH_MAP.remove(player, recording);
        recording.stop(force);

        return true;
    }

    public static boolean startPlaybackOfPlayer(ServerPlayer player, String playerToPlayback, String courseName) {
        PlayerData playerData;
        try {
            playerData = PlayersDataManager.getRecordingData(playerToPlayback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, List<MovementData>> recordedRuns = playerData.getRecordedRuns();
        if(recordedRuns == null) {
            return false;
        }

        Playback playback = new Playback(recordedRuns.get(courseName), player, playerToPlayback);
        SERVER_PLAYER_PLAYBACK_HASH_MAP.put(player, playback);
        playback.start();

        return true;

    }

    public static boolean stopPlaybackOfPlayer(ServerPlayer player) {
        Playback playback = SERVER_PLAYER_PLAYBACK_HASH_MAP.get(player);
        if(playback == null) return false;

        SERVER_PLAYER_RECORDING_HASH_MAP.remove(player, playback);
        playback.stop();

        return true;


    }

}
