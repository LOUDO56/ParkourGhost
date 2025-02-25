package fr.loudo.parkourGhost.commands;

import com.mojang.authlib.GameProfile;
import fr.loudo.parkourGhost.utils.GhostPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.commands.EffectCommands;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CreateGhostPlayer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {

            // Récupération du joueur côté serveur uniquement
            CraftPlayer craftPlayer = (CraftPlayer) p;
            ServerPlayer sp = craftPlayer.getHandle();

            // Création d'un faux joueur (GhostPlayer qui est une classe qui hérite de ServerPlayer en créant son faux profil et une fausse connexion
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), sp.getDisplayName().getString());
            GhostPlayer ghostPlayer = new GhostPlayer(sp.serverLevel(), gameProfile);
            ServerGamePacketListenerImpl connection = sp.connection;


            // Création d'une team pour que le npc et le joueur ne se rentrent pas dedans et que le joueur voit le npc en invisible.
            Scoreboard scoreboard = sp.serverLevel().getScoreboard();
            PlayerTeam team = scoreboard.getPlayersTeam("Ghost");
            if(team == null) {
                team = scoreboard.addPlayerTeam("Ghost");
                team.setSeeFriendlyInvisibles(true);
                team.setCollisionRule(Team.CollisionRule.NEVER);
            }
            scoreboard.addPlayerToTeam(ghostPlayer.getDisplayName().getString(), team);

            // Le tricky commence :
            // On accède au donnée du npc
            // On y ajoute la valeur 32 en héxadécimal
            // Et ça correspond à l'invisibilité du joueur
            SynchedEntityData dataWatcherGhost = ghostPlayer.getEntityData();
            EntityDataAccessor<Byte> ENTITY_FLAGS = new EntityDataAccessor<>(0, EntityDataSerializers.BYTE);
            dataWatcherGhost.set(ENTITY_FLAGS, (byte) 0x20);

            // Enfin on envoie 2 packets pour :
            // - Simuler le fait qu'un joueur rejoint le serveur
            // - Le spawn pour le rendre visibile
            // Et tout ça uniquement côté client.
            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ghostPlayer));
            connection.send(new ClientboundAddEntityPacket(ghostPlayer, 147, BlockPos.containing(sp.position())));

            p.sendMessage("§aGhost player spawned! Il est invisible uniquement pour toi.");
            p.sendMessage("ID GhostPlayer: " + ghostPlayer.getId());
            p.sendMessage("ID Player: " + sp.getId());

        }


        return true;
    }
}
