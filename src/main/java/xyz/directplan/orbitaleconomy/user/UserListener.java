package xyz.directplan.orbitaleconomy.user;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author DirectPlan
 */
@RequiredArgsConstructor
public class UserListener implements Listener {

    private final UserManager userManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        userManager.loadUser(player.getUniqueId(), user -> user.setPlayer(player), true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        userManager.saveUser(player.getUniqueId(), true);
    }
}
