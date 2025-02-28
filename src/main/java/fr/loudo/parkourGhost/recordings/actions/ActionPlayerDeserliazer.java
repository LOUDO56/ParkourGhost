package fr.loudo.parkourGhost.recordings.actions;

import com.google.gson.*;
import net.minecraft.world.entity.Pose;

import java.lang.reflect.Type;

public class ActionPlayerDeserliazer implements JsonDeserializer<ActionPlayer> {
    @Override
    public ActionPlayer deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (!jsonObject.has("actionType")) return null;

        ActionType actionType = ActionType.valueOf(jsonObject.get("actionType").getAsString());

        if (actionType == ActionType.POSE) {
            if (!jsonObject.has("pose")) return null;
            Pose pose = Pose.valueOf(jsonObject.get("pose").getAsString());
            return new PlayerPoseChange(pose);
        }

        return new ActionPlayer(actionType);
    }
}
