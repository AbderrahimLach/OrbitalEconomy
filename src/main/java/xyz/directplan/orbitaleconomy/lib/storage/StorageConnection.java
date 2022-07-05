package xyz.directplan.orbitaleconomy.lib.storage;

import xyz.directplan.orbitaleconomy.user.User;

import java.util.UUID;

/**
 * @author DirectPlan
 */
public interface StorageConnection {

    String getName();

    void connect();

    void close();

    User loadData(UUID uuid);

    void saveData(User user);
}
