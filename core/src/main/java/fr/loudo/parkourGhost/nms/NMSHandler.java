package fr.loudo.parkourGhost.nms;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.recordings.RecordingData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NMSHandler {

    public static PlaybackInterface getPlaybackInstance(Player player, RecordingData recordingData, String courseName) {

        String version = ParkourGhost.getVersion();
        String clazzName = "fr.loudo.nms_" + version + ".Playback";

        try {
            Class<? extends PlaybackInterface> clazz = (Class<? extends PlaybackInterface>) Class.forName(clazzName);
            Constructor<?> constructor = clazz.getDeclaredConstructor(Player.class, RecordingData.class, String.class);
            return (PlaybackInterface) constructor.newInstance(player, recordingData, courseName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("This version is not supported: " + e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("This version is not supported: " + e);
        } catch (InstantiationException e) {
            throw new RuntimeException("This version is not supported: " + e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("This version is not supported: " + e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("This version is not supported: " + e);
        }
    }
}
