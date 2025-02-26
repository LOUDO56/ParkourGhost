package fr.loudo.parkourGhost.data;

import fr.loudo.parkourGhost.recordings.MovementData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerData {

    private String username;
    private HashMap<String, List<MovementData>> recordedRuns;

    public PlayerData() {
        this.recordedRuns = new HashMap<>();
    }

    public PlayerData(String username) {
        this.username = username;
        this.recordedRuns = new HashMap<>();
    }

    public HashMap<String, List<MovementData>> getRecordedRuns() {
        return recordedRuns;
    }
}
