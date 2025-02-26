package fr.loudo.parkourGhost.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.recordings.MovementData;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class PlayersDataManager {

    private static final File PLAYERS_FOLDER = new File(ParkourGhost.getPlugin().getDataFolder(), "players");

    public static void folderInit() {
        if(!PLAYERS_FOLDER.exists()) PLAYERS_FOLDER.mkdirs();
    }

    public static void writeRecordingData(List<MovementData> movementDataList, String username, String courseName) throws IOException {
        File playerDataFile = new File(PLAYERS_FOLDER, username + ".json");
        if(!playerDataFile.exists()) {
            Files.createFile(playerDataFile.toPath());
        }

        Gson gson = new GsonBuilder().create();
        PlayerData playerData;
        try (Reader reader = new FileReader(playerDataFile)) {
            playerData = gson.fromJson(reader, PlayerData.class);
        }

        if(playerData == null) {
            playerData = new PlayerData();
        }

        HashMap<String, List<MovementData>> recordedRuns = playerData.getRecordedRuns();
        recordedRuns.remove(courseName);

        recordedRuns.put(courseName, movementDataList);

        try(Writer writer = new FileWriter(playerDataFile)) {
            gson.toJson(playerData, writer);
        }

    }

    public static PlayerData getRecordingData(String username) throws IOException {
        File playerDataFile = new File(PLAYERS_FOLDER, username + ".json");
        if(!playerDataFile.exists()) {
            return null;
        }

        Gson gson = new GsonBuilder().create();
        PlayerData playerData;
        try (Reader reader = new FileReader(playerDataFile)) {
            playerData = gson.fromJson(reader, PlayerData.class);
        }

        return playerData;

    }


}
