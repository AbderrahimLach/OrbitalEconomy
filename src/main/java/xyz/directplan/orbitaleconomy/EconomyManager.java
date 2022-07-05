package xyz.directplan.orbitaleconomy;

import org.bukkit.entity.Player;
import xyz.directplan.orbitaleconomy.lib.config.replacement.Replacement;
import xyz.directplan.orbitaleconomy.user.User;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author DirectPlan
 */
public class EconomyManager {

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public void earnMoney(User user) {
        int timeLeft = user.getTimeLeftBeforeEarning();

        if(timeLeft > 0) {
            ConfigKeys.MESSAGES_EARNING_ON_COOLDOWN.sendMessage(user, new Replacement("time-left", timeLeft));
            return;
        }

        int randomAmount = random.nextInt(5) + 1;
        user.increaseBalance(randomAmount);
        user.setLastTimeEarned(System.currentTimeMillis());
        ConfigKeys.MESSAGES_EARNED.sendMessage(user, new Replacement("amount", randomAmount));
    }

    public void transferMoney(User issuer, User target, int amount) {
        if(issuer == target) {
            ConfigKeys.MESSAGES_SELF_TRANSFER_NOT_ALLOWED.sendMessage(issuer);
            return;
        }
        int userBalance = issuer.getBalance();
        if(userBalance < amount) {
            ConfigKeys.MESSAGES_INSUFFICIENT_BALANCE.sendMessage(issuer);
            return;
        }

        issuer.decreaseBalance(amount);
        target.increaseBalance(amount);

        Replacement commonReplacement = new Replacement("amount", amount);
        ConfigKeys.MESSAGES_MONEY_SENT.sendMessage(issuer,  new Replacement("player", target.getName()), commonReplacement);
        ConfigKeys.MESSAGES_MONEY_RECEIVED.sendMessage(target, new Replacement("player", issuer.getName()), commonReplacement);
    }

    public void setBalance(User issuer, User target, int balance) {
        target.setBalance(balance);
        Player playerTarget = target.getPlayer();
        ConfigKeys.MESSAGES_BALANCE_SET.sendMessage(issuer, new Replacement("player", playerTarget.getName()),
                new Replacement("balance", balance));
    }
}
