package xyz.directplan.orbitaleconomy.lib.config;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.directplan.orbitaleconomy.lib.config.replacement.Replacement;
import xyz.directplan.orbitaleconomy.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author DirectPlan
 */
public interface ConfigEntry {
    
    Object getValue();

    String getKey();

    void setValue(Object value);

    default boolean isForcedEntryDeclaration() { return true; }

    default List<String> getStringList(boolean colored, Replacement... replacements){
        Object value = getValue();
        List<String> tempStringList = new ArrayList<>();
        if(!(value instanceof List)) {
            return tempStringList;
        }
        List<?> list = (List<?>) value;
        for(Object obj : list) {
            if(obj instanceof String) {
                String v = proceedReplacements(obj.toString(), replacements);
                if(colored) {
                    v = ChatColor.translateAlternateColorCodes('&', v);
                }
                if(v.contains("\n")) {
                    String[] lines = v.split("\n");
                    tempStringList.addAll(Arrays.asList(lines));
                    continue;
                }
                tempStringList.add(v);
            }
        }
        return tempStringList;
    }

    default List<String> getStringList(Replacement... replacements) {
        return getStringList(true, replacements);
    }

    default void broadcastMessage(Replacement... replacements) {
        if(getValue() instanceof List) {
            List<String> lines = getStringList(replacements);
            for(String line : lines) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', line));
            }
            return;
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getStringValue(replacements)));
    }

    default void sendMessage(User user, Replacement... replacements) {
        Player player = user.getPlayer();
        if(player != null && player.isOnline()) {
            sendMessage(player, replacements);
        }
    }

    default void sendMessage(CommandSender sender, Replacement... replacements) {
        Object value = getValue();
        if(value instanceof List) {
            List<String> lines = getStringList(replacements);
            for(String line : lines) {
                sender.sendMessage(line);
            }
            return;
        }
        String message = getStringValue(replacements);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    default void sendClickableMessage(Player player, String command, Replacement... replacements) {
        String message = getStringValue(replacements);

        TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
        // Add a click event to the component.
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));

        // Send it!
        player.spigot().sendMessage(component);
    }

    default String proceedReplacements(String text, Replacement... replacements) {
        for(Replacement replacement : replacements) {
            text = replacement.replace(text);
        }
        return text;
    }

    default String getStringValue(Replacement... replacements) {
        Object value = getValue();
        if (!(value instanceof String)) return null;

        String text = value.toString();
        return proceedReplacements(text, replacements);
    }

    default int getInteger() {
        return (int) getValue();
    }

    default boolean getBoolean() {
        return (boolean) getValue();
    }
}
