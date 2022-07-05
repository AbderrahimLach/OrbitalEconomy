package xyz.directplan.orbitaleconomy;

import co.aikar.commands.*;
import lombok.Getter;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.directplan.orbitaleconomy.config.BukkitConfigHandler;
import xyz.directplan.orbitaleconomy.lib.config.ConfigHandler;
import xyz.directplan.orbitaleconomy.lib.storage.Storage;
import xyz.directplan.orbitaleconomy.user.User;
import xyz.directplan.orbitaleconomy.user.UserListener;
import xyz.directplan.orbitaleconomy.user.UserManager;

@Getter
public final class OrbitalEconomy extends JavaPlugin {

    private Storage storage;
    private UserManager userManager;
    private final EconomyManager economyManager = new EconomyManager();
    private BukkitConfigHandler configHandler;

    @Override
    public void onEnable() {

        configHandler = new BukkitConfigHandler(this);
        configHandler.loadConfiguration("config.yml", ConfigKeys.class);

        storage = new Storage(this);
        storage.connect();

        userManager = new UserManager(this);

        prepareCommands();

        getServer().getPluginManager().registerEvents(new UserListener(userManager), this);
    }

    public void prepareCommands() {

        BukkitCommandManager commandManager = new BukkitCommandManager(this);

        CommandContexts<BukkitCommandExecutionContext> commandContexts = commandManager.getCommandContexts();
        commandContexts.registerIssuerOnlyContext(User.class, resolver -> {
            if(resolver.getSender() instanceof ConsoleCommandSender) {
                throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE);
            }
            Player player = resolver.getPlayer();
            return userManager.getUser(player);
        });
        commandManager.registerDependency(UserManager.class, userManager);
        commandManager.registerDependency(EconomyManager.class, economyManager);
        commandManager.registerDependency(ConfigHandler.class, configHandler);

        commandManager.registerCommand(new EconomyCommands());
        commandManager.registerCommand(new OrbitalEconomyCommand());
    }

    @Override
    public void onDisable() {

        userManager.saveUsers();
        storage.close();
        configHandler.saveConfigurations();
    }
}
