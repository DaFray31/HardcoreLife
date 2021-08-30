package hardcorelife.chryscorelab.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import hardcorelife.chryscorelab.Touchy;
import hardcorelife.chryscorelab.helpers.PlayerLife;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        Server.Spigot server = new Server.Spigot();

        PlayerLife.removeLife(player);

        int remainingLives = PlayerLife.getLives(player);

        if (Touchy.get().globalLivesEnabled()) {
            // final TextComponent component = Component.text("The server has " +
            // remainingLives + " live(s) remaining.");
            // server.broadcast(component);
            Bukkit.broadcastMessage("The server has " + remainingLives + " live(s) remaining.");

            if (remainingLives == 0) {
                // handle server permadeath
                Bukkit.setDefaultGameMode(GameMode.SPECTATOR);

                // Change everyone's gamemode to spectator
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setGameMode(GameMode.SPECTATOR);
                }
            } else {
                respawnPlayer(player);
            }

        } else {
            // TODO - Change this to only send to the player,
            // once /lives supports getting life count of other players
            Bukkit.broadcastMessage(player.getName() + " has " + remainingLives + " live(s) remaining.");

            if (remainingLives == 0) {
                // handle player permadeath
                player.setGameMode(GameMode.SPECTATOR);

                // Prevent movement on death
                player.setFlySpeed(0);
                player.setWalkSpeed(0);
                player.teleport(deathLocation);

            } else {
                respawnPlayer(player);
            }
        }
    }

    private static void respawnPlayer(Player player) {
        // Handle respawning a new player
        // This may be necessary if hardcore == True
        player.setGameMode(GameMode.SURVIVAL);
    }

}