package xyz.directplan.orbitaleconomy;

import lombok.Getter;
import lombok.Setter;
import xyz.directplan.orbitaleconomy.lib.config.ConfigEntry;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author DirectPlan
 */
@Getter
public enum ConfigKeys implements ConfigEntry {

    /* config.yml keys */
    STORAGE_HOST("storage.host", "localhost"),
    STORAGE_PORT("storage.port", 0, false),
    STORAGE_USERNAME("storage.username", "username"),
    STORAGE_PASSWORD("storage.password", "password"),
    STORAGE_DATABASE("storage.database", "database"),
    STORAGE_MAXIMUM_POOL_SIZE("storage.maximum-pool-size", 10),

    EARNING_COOLDOWN("earning-cooldown", 60),
    MESSAGES_CHECK_SELF_BALANCE("messages.check-self-balance", "&aYour balance is $%balance%."),
    MESSAGES_CHECK_OTHER_BALANCE("messages.check-other-balance", "&a%player%'s balance is $%balance%."),

    MESSAGES_EARNED("messages.earned", "&aYou've earned $%amount%."),
    MESSAGES_EARNING_ON_COOLDOWN("messages.earning-on-cooldown", "&cYou need to wait %time-left% seconds before earning again."),
    MESSAGES_INSUFFICIENT_BALANCE("messages.insufficient-balance", "&cYour balance is insufficient to complete this operation."),
    MESSAGES_MONEY_RECEIVED("messages.money-received", "&aYou've received $%amount% from %player%."),
    MESSAGES_MONEY_SENT("messages.money-sent", "&aYou've sent $%amount% to %player%."),
    MESSAGES_BALANCE_SET("messages.balance-set", "&aYou've set %player%'s balance to $%balance%."),
    MESSAGES_SELF_TRANSFER_NOT_ALLOWED("messages.self-transfer-not-allowed", "&cYou cannot transfer money to yourself o_o"),
    ;
    private final String key;
    private final boolean forcedEntryDeclaration;
    @Setter private Object value;

    ConfigKeys(String key, Object defaultValue, boolean overwrite) {
        this.key = key;
        this.value = defaultValue;

        this.forcedEntryDeclaration = overwrite;
    }

    ConfigKeys(String key, Object value) {
        this(key, value, true);
    }
}
