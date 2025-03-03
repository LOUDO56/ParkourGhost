package fr.loudo.parkourGhost.data.player;

import fr.loudo.parkourGhost.recordings.RecordingData;

import java.util.HashMap;

public class PlayerData {

    private String username;
    private HashMap<String, RecordingData> recordedRuns;

    public PlayerData() {
        this.recordedRuns = new HashMap<>();
    }

    public PlayerData(String username) {
        this.username = username;
        this.recordedRuns = new HashMap<>();
    }

    public HashMap<String, RecordingData> getRecordedRuns() {
        return recordedRuns;
    }
}
