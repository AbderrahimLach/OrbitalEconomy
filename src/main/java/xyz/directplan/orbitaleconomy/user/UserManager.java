package xyz.directplan.orbitaleconomy.user;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChainFactory;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import xyz.directplan.orbitaleconomy.OrbitalEconomy;
import xyz.directplan.orbitaleconomy.lib.storage.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author DirectPlan
 */
@RequiredArgsConstructor
public class UserManager {

    private final Storage storage;
    private final TaskChainFactory taskChainFactory;

    public UserManager(OrbitalEconomy plugin) {
        storage = plugin.getStorage();
        taskChainFactory = BukkitTaskChainFactory.create(plugin);
    }

    private final Map<UUID, User> users = new HashMap<>();

    public User getUser(UUID uuid) {
        return users.get(uuid);
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public void loadUser(UUID uuid, Consumer<User> consumer) {
        loadUser(uuid, consumer, false);
    }

    public void loadUser(UUID uuid, Consumer<User> consumer, boolean cache) {

        taskChainFactory.newChain().asyncFirst(() -> {
            User user = storage.loadUser(uuid);
            if(cache) {
                users.put(uuid, user);
            }
            return user;
        }).syncLast(consumer::accept).execute();
    }
    public User saveUser(UUID uuid, boolean remove) {
        return saveUser(uuid, null, remove);
    }

    public User saveUser(UUID uuid, Consumer<User> consumer, boolean remove) {
        User user = getUser(uuid);
        if(user != null) {
            saveUser(user, consumer, remove);
        }
        return user;
    }

    public void saveUser(User user, Consumer<User> consumer, boolean remove) {
        taskChainFactory.newChain().asyncFirst(() -> {
            storage.saveUser(user);
            if(remove) {
                users.remove(user.getUuid());
            }
            return user;
        }).syncLast(input -> {if(consumer != null) consumer.accept(input);}).execute();
    }

    /**
     * This is a synchronous operation and should only be called if the server is shutting down
     */
    public void saveUsers() {
        users.forEach((uuid, user) -> saveUser(user, null, false));
    }
}
