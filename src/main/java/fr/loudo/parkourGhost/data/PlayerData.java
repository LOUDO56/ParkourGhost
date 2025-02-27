package fr.loudo.parkourGhost.data;

import fr.loudo.parkourGhost.recordings.RecordingData;
import fr.loudo.parkourGhost.recordings.actions.MovementData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
