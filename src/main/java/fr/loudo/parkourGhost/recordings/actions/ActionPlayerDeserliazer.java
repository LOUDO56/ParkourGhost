package fr.loudo.parkourGhost.recordings.actions;

import com.google.gson.*;
import net.minecraft.world.entity.Pose;

import java.lang.reflect.Type;

public class ActionPlayerDeserliazer implements JsonDeserializer<ActionPlayer> {
    @Override
    public ActionPlayer deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ActionType actionType = ActionType.valueOf(jsonObject.get("actionType").getAsString());

        if (actionType == ActionType.POSE) {
            Pose pose = Pose.valueOf(jsonObject.get("pose").getAsString());
            return new ChangePose(pose);
        }

        return new ActionPlayer(actionType);
    }
}
