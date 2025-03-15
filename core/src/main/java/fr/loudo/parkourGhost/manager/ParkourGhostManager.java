package fr.loudo.parkourGhost.manager;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.data.player.PlayerData;
import fr.loudo.parkourGhost.nms.NMSHandler;
import fr.loudo.parkourGhost.nms.PlaybackInterface;
import fr.loudo.parkourGhost.recordings.Recording;
import fr.loudo.parkourGhost.recordings.RecordingData;
import io.github.a5h73y.parkour.Parkour;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

public class ParkourGhostManager {

    private static final HashMap<Player, Recording> SERVER_PLAYER_RECORDING_HASH_MAP = new HashMap<>();
    private static final HashMap<Player, PlaybackInterface> SERVER_PLAYER_PLAYBACK_HASH_MAP = new HashMap<>();

    public static boolean joinPlayerParkour(Player player, String courseName) {

        if(SERVER_PLAYER_RECORDING_HASH_MAP.containsKey(player)) {
            restartPlayerParkour(player, courseName);
            return false;
        };

        Recording recording = new Recording(player, courseName);
        SERVER_PLAYER_RECORDING_HASH_MAP.put(player, recording);

        // If the player is playing a playback, we will start the recording after the countdown.
        if(SERVER_PLAYER_PLAYBACK_HASH_MAP.get(player) == null) {
            recording.start();
        }

        return true;
    }

    public static boolean restartPlayerParkour(Player player, String courseName) {

        if(!SERVER_PLAYER_RECORDING_HASH_MAP.containsKey(player)) return false;

        PlaybackInterface currentPlayback = SERVER_PLAYER_PLAYBACK_HASH_MAP.get(player);
        stopRecordOrPlayback(player, true);

        if(currentPlayback != null) {
            startPlaybackOfPlayer(player, courseName);
        }
        joinPlayerParkour(player, courseName);

        return true;
    }

    public static boolean joinPlayerParkourAndStartPlayback(Player player, String courseName) {

        if(!startPlaybackOfPlayer(player, courseName)) return false;

        if(ParkourGhost.getPlugin().getConfig().getBoolean("join_parkour_on_playback")) {
            Parkour.getInstance().getPlayerManager().joinCourse(player, courseName);
        }
        if(ParkourGhost.getPlugin().getConfig().getBoolean("playback.countdown")) {
            player.sendTitle(" ", " ", 0, 0, 0); // Override title sent when joining a parkour from Parkour plugin
        }

        return true;
    }


    public static boolean startPlaybackOfPlayer(Player player, String courseName) {
        PlayerData playerData;
        try {
            playerData = PlayersDataManager.getRecordingData(player);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't get player recording data: " + e);
        }

        if(playerData == null) return false;

        HashMap<String, RecordingData> recordedRuns = playerData.getRecordedRuns();
        if(!recordedRuns.containsKey(courseName)) {
            return false;
        }

        PlaybackInterface currentPlayback = SERVER_PLAYER_PLAYBACK_HASH_MAP.get(player);
        if(currentPlayback != null) {
            currentPlayback.stop();
        }

        PlaybackInterface playback = NMSHandler.getPlaybackInstance(player, recordedRuns.get(courseName), courseName);
        SERVER_PLAYER_PLAYBACK_HASH_MAP.put(player, playback);
        playback.start();

        return true;

    }

    public static void stopRecordOrPlayback(Player player, boolean force) {
        PlaybackInterface playback = SERVER_PLAYER_PLAYBACK_HASH_MAP.get(player);
        if(playback != null) {
            playback.stop();
            SERVER_PLAYER_PLAYBACK_HASH_MAP.remove(player, playback);
        }
        Recording recording = SERVER_PLAYER_RECORDING_HASH_MAP.get(player);
        if(recording != null) {
            recording.stop(force);
            SERVER_PLAYER_RECORDING_HASH_MAP.remove(player, recording);
        }
    }

    public static Recording getCurrentPlayerRecording(Player player) {
        return SERVER_PLAYER_RECORDING_HASH_MAP.get(player);
    }

    public static PlaybackInterface getCurrentPlayerPlayback(Player player) {
        return SERVER_PLAYER_PLAYBACK_HASH_MAP.get(player);
    }



}
