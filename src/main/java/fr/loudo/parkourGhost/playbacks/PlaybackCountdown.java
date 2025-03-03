package fr.loudo.parkourGhost.playbacks;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.utils.TitleUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PlaybackCountdown {

    private Player player;
    private Playback playback;
    private final Sound COUNTDOWN_SOUND =
            Arrays.asList("1.8", "1.9", "1.10", "1.11", "1.12", "1.13").contains(ParkourGhost.getVersion())
                    ?
                    Sound.valueOf("NOTE_STICK")
                    :
                    Sound.valueOf("BLOCK_NOTE_BLOCK_BANJO");
    private int seconds;
    private ChatColor currentColor;
    private float currentPitch;

    public PlaybackCountdown(Player player, Playback playback) {
        this.player = player;
        this.playback = playback;
        seconds = ParkourGhost.getPlugin().getConfig().getInt("playback.start-second");
    }

    public void update() {
        switch (seconds) {
            case 3:
                currentPitch = 0.5f;
                currentColor = ChatColor.GREEN;
                break;
            case 2:
                currentPitch = 0.6f;
                currentColor = ChatColor.YELLOW;
                break;
            case 1:
                currentPitch = 0.9f;
                currentColor = ChatColor.RED;
                break;
            default:
                currentPitch = 0.5f;
                currentColor = ChatColor.GREEN;
                break;
        }
        if(seconds == 0) {
            player.playSound(player.getLocation(), COUNTDOWN_SOUND, 1.0f, 1.0f);
            TitleUtils.send(player, "", ChatColor.GREEN +"GO!", 0, 30, 3);
        } else {
            player.playSound(player.getLocation(), COUNTDOWN_SOUND, 1.0f, currentPitch);
            TitleUtils.send(player, "", currentColor + String.valueOf(seconds), 0, 30, 0);
        }
        seconds--;
    }

    public int getSeconds() {
        return seconds;
    }

    public Playback getPlayback() {
        return playback;
    }
}


