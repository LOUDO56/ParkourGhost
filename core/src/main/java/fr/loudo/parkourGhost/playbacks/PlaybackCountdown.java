package fr.loudo.parkourGhost.playbacks;

import fr.loudo.parkourGhost.ParkourGhost;
import fr.loudo.parkourGhost.nms.PlaybackInterface;
import io.github.a5h73y.parkour.Parkour;
import io.github.a5h73y.parkour.type.player.session.ParkourSession;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlaybackCountdown {

    private Player player;
    private PlaybackInterface playback;
    private final Sound COUNTDOWN_SOUND = Sound.BLOCK_NOTE_BLOCK_BANJO;
    private int seconds;
    private ChatColor currentColor;
    private float currentPitch;

    public PlaybackCountdown(Player player, PlaybackInterface playback) {
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
            player.playSound(player, COUNTDOWN_SOUND, 1.0f, 1.0f);
            player.sendTitle(" ", ChatColor.GREEN +"GO!", 0, 15, 20);
            ParkourSession session = Parkour.getInstance().getParkourSessionManager().getParkourSession(player);
            session.resetTime();
        } else {
            player.playSound(player, COUNTDOWN_SOUND, 1.0f, currentPitch);
            player.sendTitle(" ", currentColor + String.valueOf(seconds), 0, 100, 0);
        }
        seconds--;
    }

    public int getSeconds() {
        return seconds;
    }

    public PlaybackInterface getPlayback() {
        return playback;
    }
}


