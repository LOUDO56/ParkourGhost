package fr.loudo.parkourGhost.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.data.player.PlayerData;
import fr.loudo.parkourGhost.recordings.RecordingData;
import fr.loudo.parkourGhost.recordings.actions.ActionPlayer;
import fr.loudo.parkourGhost.recordings.actions.ActionPlayerDeserliazer;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

public class PlayersDataManager {

    private static final File PLAYERS_FOLDER = new File(ParkourGhost.getPlugin().getDataFolder(), "players");

    public static void folderInit() {
        if(!PLAYERS_FOLDER.exists()) PLAYERS_FOLDER.mkdirs();
    }

    public static void writeRecordingData(RecordingData recordingData, Player player, String courseName) throws IOException {
        File playerDataFile = new File(PLAYERS_FOLDER, player.getUniqueId() + ".json");
        if(!playerDataFile.exists()) {
            Files.createFile(playerDataFile.toPath());
        }

        Gson gson = new GsonBuilder().create();
        PlayerData playerData;
        try (Reader reader = new FileReader(playerDataFile)) {
            playerData = gson.fromJson(reader, PlayerData.class);
        }

        if(playerData == null) {
            playerData = new PlayerData(player.getDisplayName());
        }

        recordingData.getMovementData().remove(0);
        HashMap<String, RecordingData> recordedRuns = playerData.getRecordedRuns();
        recordedRuns.remove(courseName);

        recordedRuns.put(courseName, recordingData);

        try(Writer writer = new BufferedWriter(new FileWriter(playerDataFile))) {
            gson.toJson(playerData, writer);
        }

    }

    public static PlayerData getRecordingData(Player player) throws IOException {
        File playerDataFile = new File(PLAYERS_FOLDER, player.getUniqueId() + ".json");
        if(!playerDataFile.exists()) {
            return null;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ActionPlayer.class, new ActionPlayerDeserliazer())
                .create();
        PlayerData playerData;
        try (Reader reader = new BufferedReader(new FileReader(playerDataFile))) {
            playerData = gson.fromJson(reader, PlayerData.class);
        }

        return playerData;

    }


}
