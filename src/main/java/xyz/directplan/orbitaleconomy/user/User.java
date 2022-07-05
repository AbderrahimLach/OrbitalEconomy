package xyz.directplan.orbitaleconomy.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import xyz.directplan.orbitaleconomy.ConfigKeys;

import java.util.UUID;

/**
 * @author DirectPlan
 */
@Data
@Getter
@Setter
public class User {

    private final UUID uuid;
    private Player player;

    private int balance = 0;
    private long lastTimeEarned = 0;
    private final long earningCooldown = ConfigKeys.EARNING_COOLDOWN.getInteger() * 1000L;

    public String getName() {
        if(player == null) {
            return null;
        }
        return player.getName();
    }
    public boolean canEarnMoney() {
        return getTimeLeftBeforeEarning() <= 0;
    }
        /*
    Calculated in seconds
     */
    public int getTimeLeftBeforeEarning() {
        long last = (System.currentTimeMillis() - lastTimeEarned);
        if(lastTimeEarned <= 0 || last < 0) {
            return 0;
        }
        return (int) (earningCooldown - last) / 1000;
    }

    public void increaseBalance(int amount) {
        balance += amount;
    }

    public void decreaseBalance(int amount) {
        if(balance >= amount) {
            balance -= amount;
        }
    }
}
