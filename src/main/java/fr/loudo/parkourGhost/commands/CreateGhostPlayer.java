package fr.loudo.parkourGhost.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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

            CraftPlayer craftPlayer = (CraftPlayer) p;
            ServerPlayer sp = craftPlayer.getHandle();

            GameProfile ghostGameProfile = new GameProfile(UUID.randomUUID(), sp.getDisplayName().getString());
            GameProfile spGameProfile = sp.getGameProfile();
            if(!spGameProfile.getProperties().isEmpty()) {
                Property spTextures = spGameProfile.getProperties().get("textures").iterator().next();
                ghostGameProfile.getProperties().put("textures", new Property("textures", spTextures.value(), spTextures.signature()));
            }

            GhostPlayer ghostPlayer = new GhostPlayer(sp.serverLevel(), ghostGameProfile);
            ServerGamePacketListenerImpl connection = sp.connection;

            Scoreboard scoreboard = new Scoreboard();
            PlayerTeam team = new PlayerTeam(scoreboard, "Ghost");
            team.setSeeFriendlyInvisibles(true);
            team.setCollisionRule(Team.CollisionRule.NEVER);
            team.setNameTagVisibility(Team.Visibility.NEVER);
            scoreboard.addPlayerToTeam(sp.getDisplayName().getString(), team);
            scoreboard.addPlayerToTeam(ghostPlayer.getDisplayName().getString(), team);

            // Enable all skin layers
            SynchedEntityData dataWatcherGhost = ghostPlayer.getEntityData();
            EntityDataAccessor<Byte> ENTITY_LAYER = new EntityDataAccessor<>(17, EntityDataSerializers.BYTE);
            dataWatcherGhost.set(ENTITY_LAYER, (byte) 0b01111111);

            ghostPlayer.setInvisible(false);

            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ghostPlayer));
            connection.send(new ClientboundAddEntityPacket(ghostPlayer, 147, BlockPos.containing(sp.position()))); // "147" qui est l'ID d'un joueur du tableau https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Entity_metadata#Entities
            connection.send(new ClientboundSetEntityDataPacket(ghostPlayer.getId(), dataWatcherGhost.getNonDefaultValues()));
            connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true));

            p.sendMessage("§aGhost player spawned! Il est invisible uniquement pour toi.");
            p.sendMessage("ID GhostPlayer: " + ghostPlayer.getId());
            p.sendMessage("ID Player: " + sp.getId());

        }


        return true;
    }
}
