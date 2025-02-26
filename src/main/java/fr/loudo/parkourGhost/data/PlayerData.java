package fr.loudo.parkourGhost.data;

import fr.loudo.parkourGhost.recordings.MovementData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerData {

    private HashMap<String, List<MovementData>> recordedRuns;

    public PlayerData() {
        this.recordedRuns = new HashMap<>();
    }

    public PlayerData(String username, HashMap<String, List<MovementData>> recordedRuns) {
        this.recordedRuns = recordedRuns;
    }

    public HashMap<String, List<MovementData>> getRecordedRuns() {
        return recordedRuns;
    }

    public void setRecordedRuns(HashMap<String, List<MovementData>> recordedRuns) {
        this.recordedRuns = recordedRuns;
    }
}
