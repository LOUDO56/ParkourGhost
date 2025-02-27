package fr.loudo.parkourGhost.recordings;

import fr.loudo.parkourGhost.recordings.actions.ActionPlayer;
import fr.loudo.parkourGhost.recordings.actions.MovementData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordingData {

    private List<MovementData> movementData;
    private HashMap<Integer, ActionPlayer> actionsPlayer;

    public RecordingData() {
        movementData = new ArrayList<>();
        actionsPlayer = new HashMap<>();
    }

    public List<MovementData> getMovementData() {
        return movementData;
    }

    public HashMap<Integer, ActionPlayer>  getActionsPlayer() {
        return actionsPlayer;
    }

}
