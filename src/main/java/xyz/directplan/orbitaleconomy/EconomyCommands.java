package xyz.directplan.orbitaleconomy;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;
import xyz.directplan.orbitaleconomy.lib.config.ConfigHandler;
import xyz.directplan.orbitaleconomy.lib.config.replacement.Replacement;
import xyz.directplan.orbitaleconomy.user.User;
import xyz.directplan.orbitaleconomy.user.UserManager;

/**
 * @author DirectPlan
 */
public class EconomyCommands extends BaseCommand {

    @Dependency
    private UserManager userManager;

    @Dependency
    private EconomyManager economyManager;

    @Dependency
    private ConfigHandler configHandler;

    @CommandAlias("bal")
    @Syntax("[player]")
    public void onBalanceCheck(User user, @Flags("other") @Optional Player target) {
        if(target != null) {
            User userTarget = userManager.getUser(target);
            int targetBalance = userTarget.getBalance();
            ConfigKeys.MESSAGES_CHECK_OTHER_BALANCE.sendMessage(user, new Replacement("player", target.getName()), new Replacement("balance", targetBalance));
            return;
        }
        int balance = user.getBalance();
        ConfigKeys.MESSAGES_CHECK_SELF_BALANCE.sendMessage(user, new Replacement("balance", balance));
    }


    @CommandAlias("give")
    @Syntax("<player> <amount>")
    public void onBalanceGive(User user, @Flags("other") Player target, int amount) {
        User userTarget = userManager.getUser(target);
        economyManager.transferMoney(user, userTarget, amount);
    }

    @CommandAlias("setbal")
    @Syntax("<target> <amount>")
    @CommandPermission("orbitaleconomy.commands.setbal")
    public void onBalanceSet(User user, @Flags("other") Player target, int amount) {

        User userTarget = userManager.getUser(target);
        economyManager.setBalance(user, userTarget, amount);
    }

    @CommandAlias("earn")
    public void onEarn(User user) {
        economyManager.earnMoney(user);
    }

}
