package xyz.directplan.orbitaleconomy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import xyz.directplan.orbitaleconomy.lib.storage.StorageConnection;
import xyz.directplan.orbitaleconomy.lib.storage.misc.ConnectionData;
import xyz.directplan.orbitaleconomy.user.User;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * @author DirectPlan
 */
@RequiredArgsConstructor
public class SQLEconomyStorage implements StorageConnection {


    private final OrbitalEconomy plugin;
    private final ConnectionData connectionData;
    private HikariDataSource hikariDataSource;

    private static final String TABLE_NAME = "economyData";

    @Override
    public String getName() {
        return "MySQL";
    }

    @Override
    public void connect() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName("OrbitalEconomy - MySQL Connection Pool");

        hikariConfig.setMaximumPoolSize(connectionData.getMaximumPoolSize());

        hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        Properties properties = new Properties();
        properties.put("serverName", connectionData.getHost());
        properties.put("port", connectionData.getPort());
        properties.put("databaseName", connectionData.getDatabase());
        properties.put("user", connectionData.getUsername());
        properties.put("password", connectionData.getPassword());
        hikariConfig.setDataSourceProperties(properties);
        hikariDataSource = new HikariDataSource(hikariConfig);

        plugin.getLogger().log(Level.INFO, "Successfully established connection! Storage DataSource: " + hikariConfig.getDataSourceClassName());

        createTableIfAbsent();
    }

    @Override
    public void close() {
        hikariDataSource.close();
    }

    @Override
    public User loadData(UUID uuid) {
        User user = new User(uuid);
        String uuidString = uuid.toString();

        requestConnection(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM "+ TABLE_NAME + " WHERE uuid = ?")) {
                ps.setString(1, uuidString);
                try (ResultSet result = ps.executeQuery()) {
                    if(result.next()) {
                        int balance = result.getInt(2);
                        user.setBalance(balance);
                    }
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return user;
    }

    @Override
    public void saveData(User user) {
        String uuidString = user.getUuid().toString();
        requestConnection(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO " + TABLE_NAME + "(uuid, balance) VALUES (?,?) ON DUPLICATE KEY UPDATE balance = ?")) {
                ps.setString(1, uuidString);
                ps.setInt(2, user.getBalance());
                ps.setInt(3, user.getBalance());
                ps.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void requestConnection(Consumer<Connection> connectionConsumer) {
        try (Connection connection = hikariDataSource.getConnection()) {
            connectionConsumer.accept(connection);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfAbsent() {
        requestConnection(connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.addBatch("CREATE TABLE IF NOT EXISTS economyData(uuid varchar(36), balance INT, CONSTRAINT ed_pk PRIMARY KEY (uuid));");
                statement.executeBatch();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
