package xyz.directplan.orbitaleconomy.lib.storage;

import xyz.directplan.orbitaleconomy.OrbitalEconomy;
import xyz.directplan.orbitaleconomy.ConfigKeys;
import xyz.directplan.orbitaleconomy.SQLEconomyStorage;
import xyz.directplan.orbitaleconomy.lib.storage.misc.ConnectionData;
import xyz.directplan.orbitaleconomy.user.User;

import java.util.UUID;

/**
 * @author DirectPlan
 */

public class Storage {

    private final StorageConnection connection;

    public Storage(OrbitalEconomy plugin) {

        String host = ConfigKeys.STORAGE_HOST.getStringValue();
        int port = ConfigKeys.STORAGE_PORT.getInteger();
        if(port == 0) {
            port = 3306;
        }
        String username = ConfigKeys.STORAGE_USERNAME.getStringValue();
        String password = ConfigKeys.STORAGE_PASSWORD.getStringValue();

        String database = ConfigKeys.STORAGE_DATABASE.getStringValue();

        int maximumPoolSize = ConfigKeys.STORAGE_MAXIMUM_POOL_SIZE.getInteger();
        ConnectionData connectionData = new ConnectionData(host, username, password, database, port, maximumPoolSize);
        this.connection = new SQLEconomyStorage(plugin, connectionData);

        plugin.getLogger().info(connection.getName() + " database has been preferred for storing data. Preparing to connect...");
    }

    public User loadUser(UUID uuid) {
        return connection.loadData(uuid);
    }

    public void saveUser(User user) {
        connection.saveData(user);
    }

    public void connect() { connection.connect(); }

    public void close() {
        connection.close();
    }
}