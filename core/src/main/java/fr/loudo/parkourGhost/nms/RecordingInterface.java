package fr.loudo.parkourGhost.nms;

import fr.loudo.parkourGhost.recordings.actions.ActionType;

import java.io.IOException;

public interface RecordingInterface {

    boolean start();

    void addAction(ActionType actionType);

    boolean stop(boolean force);

    void save() throws IOException;
}
