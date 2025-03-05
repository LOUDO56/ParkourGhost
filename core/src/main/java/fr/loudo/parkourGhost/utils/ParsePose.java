package fr.loudo.parkourGhost.utils;

import fr.loudo.parkourGhost.recordings.actions.ActionPlayer;
import fr.loudo.parkourGhost.recordings.actions.PlayerPoseChange;
import org.bukkit.entity.Pose;

public class ParsePose {

    public static String parse(ActionPlayer actionPlayer) {
        Pose poseBukkit = ((PlayerPoseChange) actionPlayer).getPose();
        String poseName = poseBukkit.name();
        if(poseName.equals("SNEAKING")) {
            poseName = "CROUCHING";
        }
        return poseName;
    }

}
