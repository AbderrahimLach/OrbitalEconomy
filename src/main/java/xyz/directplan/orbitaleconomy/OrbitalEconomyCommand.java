package xyz.directplan.orbitaleconomy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.directplan.orbitaleconomy.lib.config.ConfigHandler;

/**
 * @author DirectPlan
 */
@CommandAlias("orbitaleconomy|economy")
public class OrbitalEconomyCommand extends BaseCommand {

    @Dependency
    private ConfigHandler configHandler;

    @Subcommand("reload")
    public void onReload(Player player) {
        configHandler.reloadConfigurations();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig files has beeen reloaded!"));
    }
}
