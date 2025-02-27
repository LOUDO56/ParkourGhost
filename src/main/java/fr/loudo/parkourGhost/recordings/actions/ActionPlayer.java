package fr.loudo.parkourGhost.recordings.actions;

public class ActionPlayer {

    private ActionType actionType;

    public ActionPlayer(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public String toString() {
        return "ActionPlayer{" +
                "actionType=" + actionType +
                '}';
    }
}
